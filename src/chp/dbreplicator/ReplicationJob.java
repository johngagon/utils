package chp.dbreplicator;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.Date;

import chp.dbreplicator.ProgressReporter.Marker;

public class ReplicationJob {

	private static final int VARCHAR_DEFAULT = 255;
	private static final int VARCHAR_LIMIT = 2000;
	//private Map<Integer, String> jdbcTypes;
	private DatabaseManager remoteDatabase, localDatabase;
	private MappingTable mapping;
	private int workCount;
	
	private Map<String,List<ColumnDefinition>> tableDefs;
	private ProgressReporter progrpt;
	private String queryCreateAs;
	private List<String> createdSchemas;
	
	public ReplicationJob(DatabaseManager _localDatabase, String _query) {
		//this.remoteDatabase = _remoteDatabase;
		this.localDatabase = _localDatabase;
		this.queryCreateAs = _query;
		this.workCount = 0;
		
		//this.jdbcTypes = this.getAllJdbcTypeNames();
		this.tableDefs = new Hashtable<String,List<ColumnDefinition>>();
		this.createdSchemas = new ArrayList<String>();
	}
	
	public ReplicationJob(DatabaseManager _remoteDatabase,DatabaseManager _localDatabase, MappingTable _mapping) {
		this.remoteDatabase = _remoteDatabase;
		this.localDatabase = _localDatabase;
		this.mapping = _mapping;
		this.workCount = 0;
		
		//this.jdbcTypes = this.getAllJdbcTypeNames();
		this.tableDefs = new Hashtable<String,List<ColumnDefinition>>();
		this.createdSchemas = new ArrayList<String>();
	}
	
	//subclass?
	public void execCreateFromQueryJob(){
		Log.pl("Starting at "+new java.util.Date());
		clean();
		this.localDatabase.connect(); 
		
		if(!localDatabase.isConnected()){
			Log.pl("Local database not connected");
			return;
		}
		localDatabase.execute(queryCreateAs);
		
		localDatabase.close();
		Log.pl("\nClosing database connections.");		
	}
	
	public void exec(){
		Log.pl("Starting at "+new java.util.Date());
		
		clean();
		
		
		if(!calculateWork()){
			return;
		}
		
		
		//Connect
		Log.pl("\n3. Connecting");
		this.localDatabase.connect(); //should create the database
		this.remoteDatabase.connect();
		
		
		if(!localDatabase.isConnected()){
			Log.pl("Local database not connected");
			return;
		}
		if(!remoteDatabase.isConnected()){
			Log.pl("Remote database not connected");
			return;
		}
		
		Log.pl("\n4. Creating Tables.");
		//Create local tables
		String[] tables = mapping.getTableList();
		for(String table:tables){
			String query = "";
			if(remoteDatabase.getDatabase().rdbms().equals(Rdbms.POSTGRESQL)){
				query = "select * ";
			}else{
				query = "select top 1 * ";
			}
			query +=" from "+mapping.getSourceQual()+table;
			if(remoteDatabase.getDatabase().rdbms().equals(Rdbms.POSTGRESQL)){
				query += " limit 1";
			}
			remoteDatabase.query(query);
			if(remoteDatabase.haveResult()){
				ResultSet rs = remoteDatabase.getResult();
				if(createLocalTable(table,rs)){
					Log.pl("Created table "+table);
				}else{
					Log.pl("Couldn't create table:"+table+" in local database.");
				}
				remoteDatabase.closeRs();
			}
		}
		Log.pl("Local tables created.");
		
		
		Log.pl("\n5. Starting data transfer.");
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
					if(!localDatabase.executeUpdate(insertQuery,data)){
						break;//break while
					}
					progrpt.completeWork();	
				}
				remoteDatabase.closeRs();
			}
			testLocal(table);
			
		}//for
		Log.pl("\nEnd data transfer.");
		Log.pl("Ended at "+new java.util.Date());
		

		
		remoteDatabase.close();
		localDatabase.close();
		Log.pl("\nClosing database connections.");
		
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
			String colname = def.getColName();
			if("group".equalsIgnoreCase(colname.trim().toLowerCase())){
				colname = "\""+colname+"\"";
			}
			query+=colname;
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

		Log.pl("INSERT:"+query);
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
	
	private void createLocalSchema(String schemaName){
		if(!createdSchemas.contains(schemaName)){
			String ddl = "CREATE SCHEMA "+schemaName;
			Log.pl("Created schema "+schemaName);
			localDatabase.execute(ddl);
			createdSchemas.add(schemaName);
		}
		return;
	}
	
	private boolean createLocalTable(String tableName, ResultSet rs){
		boolean rv = false;
		String ddl = "";
		if(localDatabase.doesTableExist(tableName)){
			ddl = "DROP TABLE "+tableName;
			localDatabase.execute(ddl);
		}
		List<ColumnDefinition> defs = remoteDatabase.getDefinition();
		tableDefs.put(tableName, defs);
		if(tableName.contains(".")){
			String schema = tableName.substring(0,tableName.indexOf("."));
			if(!localDatabase.doesSchemaExist(schema)){
				createLocalSchema(schema);
			}
		}
		ddl = "CREATE CACHED TABLE "+tableName + "(";
		boolean first = true;
		boolean doPK = false;
		for(ColumnDefinition def:defs){
			String colname = def.getColName();
			if(!first){
				ddl +=",";
			}else{
				doPK = true;
				first = false;
			}
			
			if("group".equals(colname.toLowerCase())){
				colname = "\""+colname+"\"";
			}
			//Log.println("  "+def.toString()+" type:"+jdbcTypes.get(def.getColType()));
			ddl += colname+" "+getHsqlDataType(def.getColType());
			if(def.hasLength()){
				int collength = def.getColLen();
				if(collength>0 && collength<VARCHAR_LIMIT ){
					ddl+= "("+collength+")";
				}else{
					if("description".equals(colname)){
						ddl+= "("+VARCHAR_LIMIT+")";
					}else{
						ddl+= "("+VARCHAR_DEFAULT+")";
					}
				}
			}
			if(doPK){
				if((!tableName.toUpperCase().contains("COMPANY_CARRIERS")) 
						&& (!tableName.toUpperCase().contains("COMPANIES_BRANCH_CARRIERS"))
						&& (!tableName.toUpperCase().contains("SIC"))
						&& (!tableName.toUpperCase().contains("NAICS"))
					){
					ddl+=" PRIMARY KEY";
				}
				doPK=false;
			}
		}
		ddl+=")";
		Log.pl("DDL:"+ddl);
		localDatabase.execute(ddl);
		
		rv = true;// testLocal(tableName);
		
		return rv;
	}



	private boolean testLocal(String tableName) {
		boolean rv = false;
		String testCreateSQL = "select count(*) from "+tableName;
		localDatabase.query(testCreateSQL);
		if(localDatabase.haveResult()){
			rv = true;
			int count = localDatabase.getCountResult();
			if(count!=-1){
				Log.pl("  PASS:"+testCreateSQL+" .. count: "+count);
			}else{
				Log.pl("  FAIL:"+testCreateSQL+" .. count: "+count);
			}
			localDatabase.closeRs();
		}else{
			Log.pl("Did not get result.");
		}
		
		return rv;
	}

	private void clean(){
		//Clean (necessary for in memory?)
		Log.pl("\n1. Cleaning existing local database for:"+localDatabase.getDatabase().url());
		
		localDatabase.connect();
		if(localDatabase.isConnected()){
			String sql = "SHUTDOWN";
			localDatabase.execute(sql);
			localDatabase.close();
		}
		Log.pl("Cleaning done.\n");
	}
	
	private boolean calculateWork() {
		Log.pl("2. Calculating work.");
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
			Log.pl("Row Count Total: "+workCount);
			progrpt = new ProgressReporter(workCount,Marker.TENTHS);
			//progrpt.addListener(new SimpleProgressListener());
			this.remoteDatabase.closeRs();
			this.remoteDatabase.close();
			rv = true;
		}else{
			Log.pl("Could not connect to remote");
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
			Log.error(e);
		}

	    return result;
	}
	
}
/*
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
*/
//testResultsLocal();