package chp.dbreplicator.etl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import chp.dbreplicator.ColumnDefinition;
import chp.dbreplicator.Database;
import chp.dbreplicator.DatabaseManager;
import chp.dbreplicator.Log;

public class Backup {

	
	private static final String D = "\t";
	private static final String noq = "";
	private static String Q = noq;		
	private static final String EXIT_FAIL_MSG = "Exiting without finishing.";
	
	public static void main(String[] args){
		backup(Database.DMDEVNEW,"benchmarking");
		//test(Database.DMDEVNEW,"benchmarking_backup");
	}
	
	public static void test(Database db, String schema){
		//Connect.
		DatabaseManager database = new DatabaseManager(db);
		database.connect();
		Log.pl("Connection to "+db.name()+" is connected: "+database.test());		
		List<String> tables = database.getTables(schema);
		for(String table:tables){
			Log.pl("Table: "+table);
		}
	}
	
	public static void backup(Database db, String schema){
		/*
		 *  existing schema
		 *  new schema to create defaults to <name>_backup
		 *  
		 *  create new schema.
		 *  get list of tables,  make ddl from existing.
		 *  
		 */
		
		String newSchema = schema+"_backup";
		Log.pl("Starting Backup of "+schema+" to "+newSchema+" on "+new java.util.Date());

		//Connect.
		DatabaseManager database = new DatabaseManager(db);
		database.connect();
		Log.pl("Connection to "+db.name()+" is connected: "+database.test());
	
		//Create backup schema if it doesn't exist.
		if(!database.doesSchemaExist(newSchema)){
			Log.pl("Creating schema: "+newSchema);
			String sql = "create schema "+newSchema+" ";
			database.execute(sql);
			
			if(!database.doesSchemaExist(newSchema)){
				Log.pl("Couldn't create schema: "+newSchema);
				Log.pl(EXIT_FAIL_MSG);
				database.close();
				return;
			}else{
				Log.pl("Created schema: "+newSchema);
			}			
		}else{
			Log.pl("Schema already exists: "+newSchema);
		}
		
		//Create backup tables and perform backup.
		List<String> tables = database.getTables(schema);
		for(String table:tables){
			String fqSourceTable = schema+"."+table;
			String fqNewTable = newSchema +"."+ table;
			String ddl = database.getDDL(schema,fqSourceTable,fqNewTable);
			String alterOwner = database.getDDLOwnerAlter(schema,fqSourceTable,fqNewTable,"postgres");
			String grantOwner = database.getDDLOwnerGrant(schema,fqSourceTable,fqNewTable,"postgres");
			String grantViewer = database.getDDLViewerGrant(schema,fqSourceTable,fqNewTable,"staff");
			String commit = "commit";
			Log.pl("\n\nDetermining existing backup for "+fqNewTable);
			boolean tableExists = database.doesTableExist(fqNewTable);
			if(!tableExists){
				Log.pl("Table doesn't exist: "+fqNewTable);
				Log.pl("Executing DDL for "+fqSourceTable+" : "+ddl);
				database.execute(ddl);
				database.execute(alterOwner);
				database.execute(grantOwner);
				database.execute(grantViewer);
				database.execute(commit);
				tableExists = database.doesTableExist(fqNewTable);
				if(!tableExists){
					Log.pl("Couldn't create table: "+fqNewTable);
					database.close();
					Log.pl(EXIT_FAIL_MSG);
					return;
				}else{
					Log.pl("Created table: "+fqNewTable);
					performCopy(false, database, fqSourceTable, fqNewTable);
					database.execute(commit);
				}
			}else{
				Log.pl("Table already exist: "+fqNewTable);
				performCopy(true, database, fqSourceTable, fqNewTable);
				database.execute(commit);
			}
			Log.pl("-------");
		}
	
		
		database.close();
		
		Log.pl("Finished Backup of "+schema+" to "+newSchema+" at "+new java.util.Date());
	}

	@SuppressWarnings("boxing")
	private static void performCopy(boolean cleanTarget, DatabaseManager database, String sourceTable, String destTable) {
		
		if(cleanTarget){
			cleanTable(database,destTable);
		}			
		List<ColumnDefinition> cds = database.getColumnDefsFromDbMeta(sourceTable);
		for(ColumnDefinition cd:cds){
			int cdcoltype = cd.getColType();
			Log.pl("Column: "+cd.getColName()+" type:"+DatabaseManager.TYPES.get(cdcoltype)+"");
		}
		String countQuery = "select count(*) from "+sourceTable;
		database.query(countQuery);
		int countResult = database.getCountResult();
		Log.pl("Source Result Size:"+countResult);
		int max = getMagnitude(countResult);
		String query = "select * from "+sourceTable;
		Log.pl("Source Reading rows:"+query);
		int count=0;
		ResultSet rs = database.queryLarge(query);
		try{
			StringBuilder sb = new StringBuilder();
			while(rs.next()){
				
				boolean first = true;
				for(ColumnDefinition cd:cds){
					if(!first){sb.append(D);}else{first=false;}
					int cdcoltype = cd.getColType();
					
					switch (cdcoltype){
						
						//from DW: case Types.VARCHAR:sb.append("\""+rs.getString(cd.getColName())+"\"");break;
						case Types.VARCHAR:sb.append(Q+rs.getString(cd.getColName())+Q);break;
						case Types.NVARCHAR:sb.append(Q+rs.getString(cd.getColName())+Q);break;
						
						case Types.INTEGER:sb.append(rs.getInt(cd.getColName()));break;
						case Types.BIGINT:sb.append(rs.getLong(cd.getColName()));break;
						
						case Types.NUMERIC: sb.append(rs.getDouble(cd.getColName()));break;
						case Types.DECIMAL: sb.append(rs.getDouble(cd.getColName()));break;
						case Types.DATE:sb.append(Q+rs.getDate(cd.getColName())+"\"");break;
						
						case Types.DOUBLE:sb.append(""+rs.getDouble(cd.getColName())+"");break;
						case Types.ARRAY:sb.append(""+rs.getArray(cd.getColName())+"");break;
						case 1111: sb.append(""+rs.getString(cd.getColName())+"");break;
						
						//case Types.ARRAY Types.BIGINT, BINARY, BIT, BLOB, BOOLEAN, CHAR CLOB
						//DATALINK, DISTINCT,DOUBLE,FLOAT,JAVA_OBJECT,LONGVARCHAR,LONGNVARCHAR,LONGVARBINARY
						//NCHAR,NCLOB,NULL,OTHER,REAL,REF,REF_CURSOR,ROWID,SMALLINT,SQLXML,STRUCT,TIME,TIME_WITH_TIMEZONE,TIMESTAMP,TIMESTAMP_WITH_TIMEZONE
						//TINYINT,VARBINARY,VARCHAR
						default : sb.append("\""+rs.getString(cd.getColName())+"\"");break;
					}
					
				}//for(ColumnDefinition cd:cds)
				sb.append("\n");
				//Log.profile("    Read Record "+count+"  appending to file:"+filename);
				if(count%max==0){
					Log.pl("Copying data to target: "+destTable+" at count: "+count);
					
					copyPostgres(database, destTable, sb.toString());
					sb = new StringBuilder();
				}
				count++;
				//progrpt.completeWork();
			}//while(rs.next())
			
			copyPostgres(database, destTable, sb.toString());
			sb = new StringBuilder();				
			
		}catch(Exception e){
			e.printStackTrace();
		}//trycatch
	}		
	
	private static int getMagnitude(int countResult) {
		int rv = 10;
		if(countResult>100){
			rv = (int)countResult/10;
		}
		return rv;
	}	

	private static void cleanTable(DatabaseManager targetDatabase,	String destTable) {
		targetDatabase.clearTable(destTable);
	}	
	
	private static void copyPostgres(DatabaseManager targetDatabase,String destTable, String s) throws SQLException {
		CopyIn cpIN=null;
		CopyManager cm = new CopyManager((BaseConnection) targetDatabase.getConnection());
		cpIN = cm.copyIn("COPY "+destTable+" FROM STDIN  WITH DELIMITER '"+D+"' NULL 'null' ");
		byte[] bytes = s.getBytes();
		cpIN.writeToCopy(bytes, 0, bytes.length);
		cpIN.endCopy();
		//targetDatabase.execute("commit");
	}
			
		
	
}





