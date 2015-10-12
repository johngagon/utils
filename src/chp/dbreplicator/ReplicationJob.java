package chp.dbreplicator;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.Date;

import chp.dbreplicator.ProgressReporter.Marker;

public class ReplicationJob {

	private Map<Integer, String> jdbcTypes;
	private DatabaseManager remoteDatabase, localDatabase;
	private MappingTable mapping;
	private int workCount;
	private int progress;
	private Map<String,List<ColumnDefinition>> tableDefs;
	private ProgressReporter progrpt;
	
	public ReplicationJob(DatabaseManager _remoteDatabase,DatabaseManager _localDatabase, MappingTable _mapping) {
		this.remoteDatabase = _remoteDatabase;
		this.localDatabase = _localDatabase;
		this.mapping = _mapping;
		this.workCount = 0;
		this.progress = 0;
		this.jdbcTypes = this.getAllJdbcTypeNames();
		this.tableDefs = new Hashtable<String,List<ColumnDefinition>>();
		
	}

	
	public void exec(){
		Log.println("Starting at "+new java.util.Date());
		
		clean();
		
		Log.println("Calculating work.");
		if(!calculateWork()){
			return;
		}
		
		
		//Connect
		this.localDatabase.connect(); //should create the database
		this.remoteDatabase.connect();
		
		
		if(!localDatabase.isConnected()){
			Log.println("Local database not connected");
			return;
		}
		if(!remoteDatabase.isConnected()){
			Log.println("Remote database not connected");
			return;
		}
		Log.println("");
		
		//Create local tables
		String[] tables = mapping.getTableList();
		for(String table:tables){
			String query = "select top 1 * from "+mapping.getSourceQual()+table;
			remoteDatabase.query(query);
			if(remoteDatabase.haveResult()){
				ResultSet rs = remoteDatabase.getResult();
				if(createLocalTable(table,rs)){
					Log.println("Created table "+table);
				}else{
					Log.println("Couldn't create table:"+table+" in local database.");
				}
				remoteDatabase.closeRs();
			}
		}
		Log.println("Local tables created.\n");
		
		
		Log.println("Starting data transfer.");
		for(String table:tables){
			String query = "select * from "+mapping.getSourceQual()+table;
			remoteDatabase.query(query);
			if(remoteDatabase.haveResult()){
				//ResultSet rs = remoteDatabase.getResult();
				String insertQuery = createInsertQuery(table);
				//List<ColumnDefinition> defs = tableDefs.get(table);
				//int colcount = defs.size();
				List<Object> data = null;
				while((data = remoteDatabase.nextRow()) !=null){
					localDatabase.executeUpdate(insertQuery,data);	
					progress++;
					progrpt.logProgress(progress);	
				}
				remoteDatabase.closeRs();
			}
			testLocal(table);
			
		}
		Log.println("End data transfer.");
		Log.println("Ended at "+new java.util.Date());
		
		Log.println("\n TEST");
		for(String table:tables){
			String query = "select top 5 * from "+table;
			localDatabase.query(query);
			Log.println("  TABLE:"+table);
			Log.println("-----------------------  ");
			if(localDatabase.haveResult()){
				localDatabase.printResult();
				localDatabase.closeRs();
			}
			Log.println("-----------------------  \n");
		}		
		//testResultsLocal();
		
		remoteDatabase.close();
		localDatabase.close();
		Log.println("\nClosing database connections.");
		
	}

	private String createInsertQuery(String tableName){
		String query = "INSERT INTO "+tableName+" (";
		List<ColumnDefinition> defs = tableDefs.get(tableName);
		boolean first = true;
		for(ColumnDefinition def:defs){
			if(!first){
				query+=", ";	
			}else{	
				first=false;
			}
			query+=def.getColName();
		}
		query += ") VALUES (";
		first = true;
		for(int i=1;i<=defs.size();i++){
			if(!first){
				query+=", ";	
			}else{	
				first=false;
			}
			query+="?";
		}
		query += ")";

		//Log.println("INSERT:"+query);
		return query;		
	}
	
	private String getHsqlDataType(int type){
		switch (type){
			case Types.VARCHAR:return "VARCHAR";
			case Types.INTEGER:return "INTEGER";
			case Types.BIGINT:return "BIGINT";
			case Types.NVARCHAR:return "VARCHAR";
			case Types.DATE:return "DATE";
			default : return "VARCHAR";
		}
		//return null;
		
	}
	
	private boolean createLocalTable(String tableName, ResultSet rs){
		boolean rv = false;
		List<ColumnDefinition> defs = remoteDatabase.getDefinition();
		tableDefs.put(tableName, defs);
		
		String ddl = "CREATE TABLE "+tableName + "(";
		boolean first = true;
		for(ColumnDefinition def:defs){
			if(!first){
				ddl +=",";
			}else{
				first = false;
			}
			//Log.println("  "+def.toString()+" type:"+jdbcTypes.get(def.getColType()));
			ddl += def.getColName()+" "+getHsqlDataType(def.getColType());
			if(def.getColLen()!=0 && def.hasLength() ){
				ddl+= "("+def.getColLen()+")";
			}
		}
		ddl+=")";
		//Log.println("DDL:"+ddl);
		localDatabase.execute(ddl);
		
		rv = true;// testLocal(tableName);
		
		return rv;
	}

	/*
	private boolean testResultsLocal(String tableName) {
		boolean rv = false;
		String testCreateSQL = "select count(*) from "+tableName;
		localDatabase.query(testCreateSQL);
		if(localDatabase.haveResult()){
			rv = true;
			int count = localDatabase.getCountResult();
			if(count!=-1){
				Log.println("  PASS:"+testCreateSQL+" .. count: "+count);
			}else{
				Log.println("  FAIL:"+testCreateSQL+" .. count: "+count);
			}
			localDatabase.closeRs();
		}else{
			Log.println("Did not get result.");
		}
		
		return rv;
	}	
	*/

	private boolean testLocal(String tableName) {
		boolean rv = false;
		String testCreateSQL = "select count(*) from "+tableName;
		localDatabase.query(testCreateSQL);
		if(localDatabase.haveResult()){
			rv = true;
			int count = localDatabase.getCountResult();
			if(count!=-1){
				Log.println("  PASS:"+testCreateSQL+" .. count: "+count);
			}else{
				Log.println("  FAIL:"+testCreateSQL+" .. count: "+count);
			}
			localDatabase.closeRs();
		}else{
			Log.println("Did not get result.");
		}
		
		return rv;
	}

	private void clean(){
		//Clean (necessary for in memory?)
		Log.println("Cleaning existing local database");
		
		localDatabase.connect();
		if(localDatabase.isConnected()){
			String sql = "SHUTDOWN";
			localDatabase.execute(sql);
			localDatabase.close();
		}
		Log.println("\n");
	}
	
	private boolean calculateWork() {
		
		boolean rv = false;
		this.remoteDatabase.connect();
		if(remoteDatabase.isConnected()){
			String[] tables = mapping.getTableList();
			for(String table:tables){
				
				String query = "select count(*) from "+mapping.getSourceQual()+table;
				remoteDatabase.query(query);
				if(remoteDatabase.haveResult()){
					int count = remoteDatabase.getCountResult();
					//Log.println("Table:"+table+", count:"+count);
					if(count!=-1){
						workCount+=count;
					}//if ... already prints error
				}
			}
			Log.println("Row Count Total: "+workCount+"\n");
			progrpt = new ProgressReporter(workCount,Marker.TENTHS);
			this.remoteDatabase.closeRs();
			this.remoteDatabase.close();
			rv = true;
		}else{
			Log.println("Could not connect to remote");
		}
		return rv;
	}
	
	public Map<Integer, String> getAllJdbcTypeNames() {
	    Map<Integer, String> result = new HashMap<Integer, String>();
		try{
		    for (Field field : Types.class.getFields()) {
		        result.put((Integer)field.get(null), field.getName());
		    }
		}catch(Exception e){
			e.printStackTrace();
		}

	    return result;
	}
	
}
