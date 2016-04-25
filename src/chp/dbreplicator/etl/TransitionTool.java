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


@SuppressWarnings("unused")
public class TransitionTool {



	private static final String D = "\t";
	
	private static final String dq = "\"";
	private static final String noq = "";
	private static String Q = noq;
	
	
	/*
	 * Transition: 
	 * 
	 * Cases:
	 * 1. Moving from DM_DEV to foundation_data_mart<dev>
	 * 2. Moving from DM_TEST to foundation_data_mart<test>
	 * 
	 * 
	 * 
	 * 
	 */
	
	public static void main(String[] args){
		Database dmdev = Database.DM;
		Database fdmdev = Database.DMF;
		
		Database dmuat = Database.DMTESTOLD;
		Database fdmuat = Database.DMFUAT;
		
		etlDirect(true,dmdev,fdmuat,"employer");
		
	}
	
	

	//@SuppressWarnings("boxing")
	private static void etlDirect(boolean cleanTarget,Database source, Database target, String schema){
		Log.pl("Starting Copy of "+source.name()+" to "+target.name()+" with schema: "+schema+" on "+new java.util.Date());
		Log.pl("java.lib.path -- Be sure to copy lib/sqljdbc_auth.dll here: "+System.getProperty("java.library.path"));
		DatabaseManager sourceDatabase = new DatabaseManager(source);
		DatabaseManager targetDatabase = new DatabaseManager(target);	//	
		sourceDatabase.connect();
		Log.pl("Connected to "+source.name()+" is connected: "+sourceDatabase.test());
	
		targetDatabase.connect();
		Log.pl("Connected to "+target.name()+" is connected: "+targetDatabase.test());
		
		List<String> tables = sourceDatabase.getTables(schema);
		
		for(String table:tables){
			String sourceRelation = schema+"."+table;
			String destTable = schema+"."+table;
			
			performCopy(cleanTarget, sourceDatabase, targetDatabase, sourceRelation, destTable);
		}
		
		sourceDatabase.close();
		targetDatabase.close();
		
		Log.pl("Finished on "+new java.util.Date()+"!");			
	}	
	
	@SuppressWarnings("boxing")
	private static void performCopy(boolean cleanTarget,
			DatabaseManager sourceDatabase, DatabaseManager targetDatabase,
			String sourceRelation, String destTable) {
		if(cleanTarget){
			cleanTable(targetDatabase,destTable);
		}			
		
		List<ColumnDefinition> cds = sourceDatabase.getColumnDefsFromDbMeta(sourceRelation);
		for(ColumnDefinition cd:cds){
			int cdcoltype = cd.getColType();
			Log.pl("Column: "+cd.getColName()+" type:"+DatabaseManager.TYPES.get(cdcoltype)+"");
		}
		String countQuery = "select count(*) from "+sourceRelation;
		sourceDatabase.query(countQuery);
		int countResult = sourceDatabase.getCountResult();
		Log.pl("Source Result Size:"+countResult);
		int max = getMagnitude(countResult);
		String query = "select * from "+sourceRelation;
		Log.pl("Source Reading rows:"+query);
		int count=0;
		ResultSet rs = sourceDatabase.queryLarge(query);
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
					
					copyPostgres(targetDatabase,destTable, sb.toString());
					sb = new StringBuilder();
				}
				count++;
				//progrpt.completeWork();
			}//while(rs.next())
			
			copyPostgres(targetDatabase, destTable, sb.toString());
			sb = new StringBuilder();				
			
		}catch(Exception e){
			e.printStackTrace();
		}//trycatch
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
		
	}
	
	private static int getMagnitude(int countResult) {
		int rv = 10;
		if(countResult>100){
			rv = (int)countResult/10;
		}
		return rv;
	}		
	
}

//etlCustom(true);
/*
public static void etlCustom(boolean cleanTarget){
	etlDirect(cleanTarget, Database.DMTESTOLD, Database.DMFPRD,"employer");//uses schema, not file
}
*/		
