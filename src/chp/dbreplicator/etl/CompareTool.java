package chp.dbreplicator.etl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.List;

import chp.dbreplicator.ColumnDefinition;
import chp.dbreplicator.Database;
import chp.dbreplicator.DatabaseManager;
import chp.dbreplicator.Log;

public class CompareTool {
	//private static final String D = "\t";
	//private static final String noq = "";
	private static final String quot = "'";
	//private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private static String Q = quot;	
	
	public static void main(String[] args){
		/*
		 * Take a schema in one database and another database.
		 * Compare the schemas.
		 * Then compare the row counts for each table.
		 */
		
		if(compare(Database.DMDEVNEW, Database.DMTESTNEW,"blue_solutions")){
			Log.pl("\n\nFINAL RESULT: PASS");
		}else{
			Log.pl("\n\nFINAL RESULT: FAIL");			
		}
		
	}
	
	public static boolean compare(Database firstDb, Database secondDb, String schema){
		boolean rv=true;
		try {
			rv = compareSchemas(firstDb,secondDb,schema);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rv;
	}
	
	private static boolean compareSchemas(Database source, Database target, String schema) throws SQLException{
		Log.pl("Starting Comparison of "+source.name()+" to "+target.name()+" with schema: "+schema+" on "+new java.util.Date());
		Log.pl("java.lib.path -- Be sure to copy lib/sqljdbc_auth.dll here: "+System.getProperty("java.library.path"));
		DatabaseManager sourceDatabase = new DatabaseManager(source);
		DatabaseManager targetDatabase = new DatabaseManager(target);	//	
		sourceDatabase.connect();
		Log.pl("Connected to "+source.name()+" is connected: "+sourceDatabase.test());
	
		targetDatabase.connect();
		Log.pl("Connected to "+target.name()+" is connected: "+targetDatabase.test());
		
		boolean passesSchemaCompare = true;
		
		
		List<String> tables = sourceDatabase.getTables(schema);
		Log.pl("");
		for(String table:tables){
			Log.pl("\n\nComparing both schemas for table: "+table);
			String srcTable = schema+"."+table;
			String destTable = schema+"."+table;
			
			List<ColumnDefinition> sourceCols = sourceDatabase.getColumnDefsFromDbMeta(srcTable);
			List<ColumnDefinition> targetCols = targetDatabase.getColumnDefsFromDbMeta(destTable);
			
			for(ColumnDefinition sourceCol:sourceCols){
			
				if(!targetCols.contains(sourceCol)){
					Log.pl("!!Column: "+sourceCol.getColName()+" in source didn't match a column in destination for table "+table);
					passesSchemaCompare = false;
				}else{
					ColumnDefinition targetCol = targetCols.get(targetCols.indexOf(sourceCol));
					Log.pl("Found :"+sourceCol.getColName()+ " matches "+targetCol.getColName());
				}
			}
		}
		
		
		boolean passesRowCounts = compareRowCounts(sourceDatabase,targetDatabase,schema);
		//mapping
		//compareRandomColumns(firstDb,secondDb,schema);		
		
		Log.pl("----------- ");
		sourceDatabase.close();
		targetDatabase.close();
		
		Log.pl("Finished on "+new java.util.Date()+"!");	
		
		
		//for(ColumnDefinition cd:cds){
		//	int cdcoltype = cd.getColType();
		//	Log.pl("Column: "+cd.getColName()+" type:"+DatabaseManager.TYPES.get(cdcoltype)+"");
		//}	
		return passesSchemaCompare && passesRowCounts;
	}		
	
	private static boolean compareRowCounts(DatabaseManager sourceDatabase, DatabaseManager targetDatabase, String schema) throws SQLException{
		Log.pl("Comparing table counts:\n");
		boolean passesTableCounts = true;
		int discrepancyCount = 0;
		int[] sourceCounts = new int[7];
		int[] targetCounts = new int[7];
		List<String> sourceTables = sourceDatabase.getTables(schema);
		
		//List<String> targetTables = targetDatabase.getTables(schema);
		
		int idx = 0;
		for(String table:sourceTables){
			String fqTable = schema+"."+table;
			String query = "select count(*) from "+fqTable;
			sourceDatabase.query(query);
			if(sourceDatabase.haveResult()){
				sourceCounts[idx] = sourceDatabase.getCountResult();
			}else{
				sourceCounts[idx] = -1;
			}
			targetDatabase.query(query);
			if(targetDatabase.haveResult()){
				targetCounts[idx] = targetDatabase.getCountResult();
			}else{
				targetCounts[idx] = -1;
			}
			if(sourceCounts[idx]!=targetCounts[idx]){
				Log.pl("Source table "+table+" count: "+sourceCounts[idx]+" was not the same in target: "+targetCounts[idx]);
				discrepancyCount++;
			}
			idx++;							
		}
		Log.pl("Discrepancy Count: "+discrepancyCount);
		boolean pass = (discrepancyCount==0);
		if(pass){Log.pl("\nPASS!\n");
			//compareRandom(sourceDatabase,targetDatabase,schema,100);
		}else{Log.pl(
			"\nFAIL!\n");
			passesTableCounts = false;
		}			
		boolean passesRandom = compareRandom(sourceDatabase,targetDatabase,schema,5);
		return passesRandom && passesTableCounts;
	}
	
	@SuppressWarnings("unused")
	private static boolean compareRandom(DatabaseManager sourceDatabase, DatabaseManager targetDatabase, String schema,int limit){
		Log.pl("\n\nRandom Testing.\n\n");
		boolean rv = true;
		List<String> sourceTables = sourceDatabase.getTables(schema);
		List<String> targetTables = targetDatabase.getTables(schema);	
		
		for(String table:sourceTables){
			
			int discrepancyCount = 0;
			String fqTable = schema+"."+table;
			Log.pl("Randomly checking "+fqTable+" with "+limit+" samples.");
			List<ColumnDefinition> cdefs = sourceDatabase.getColumnDefsFromDbMeta(fqTable);
			String query = "select * from "+fqTable+" order by random() limit "+limit;
			sourceDatabase.query(query);
			if(sourceDatabase.haveResult()){
				ResultSet rs = sourceDatabase.getResult();
				try {
					while(rs.next()){
						String proofQuery = makeSelectWhere(cdefs,fqTable,rs);
						
						discrepancyCount += checkTarget(targetDatabase,proofQuery);
					}
				} catch (SQLException e) {
					Log.pl("Exception in Random Query: "+ query + " e.getMessage()"+e.getMessage());
					//e.printStackTrace();
				}
				
			}else{
				Log.pl("No results for: "+query);
			}
			Log.pl("Discrepancy count for "+table+" : "+discrepancyCount+"\n\n");
			if(discrepancyCount>0){
				rv = false;
			}
		}
		return rv;
	}

	private static int checkTarget(DatabaseManager targetDatabase,String proofQuery) {
		int rv = 0;
		try{
			targetDatabase.queryThrow(proofQuery);
			if(!targetDatabase.haveResult()){
				rv = 1;
				Log.pl("No result for: "+proofQuery);
			}else{
				ResultSet rs = targetDatabase.getResult();
				if(rs.next()){
					Log.pl("   PASS Rows found in target from source: query:"+proofQuery);
				}else{
					rv = 1;
					Log.pl("   FAIL Rows not found using query: "+proofQuery);
				}
				
			}
		}catch(SQLException sqle){
			Log.pl("Exception "+proofQuery);
			rv = 1;
		}
		return rv;
	}
	
	
	private static String makeSelectWhere(List<ColumnDefinition> cdefs,String fqTable,ResultSet rs) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("select *  from "+fqTable+" where ");
		String sep = "";
		boolean first = true;
		for(ColumnDefinition cd:cdefs){
			String colName = cd.getColName();
			
			if(first){
				sep = "";
				first = false;
			}else{
				sep = " and ";
				
			}
			sb.append(sep);
			int cdcoltype = cd.getColType();
			switch (cdcoltype){
			
			//from DW: case Types.VARCHAR:sb.append("\""+rs.getString(cd.getColName())+"\"");break;
				case Types.VARCHAR:
				case Types.NVARCHAR:sb.append(colName+" = "+Q+removeSingleQuotes(rs.getString(cd.getColName()))+Q);break;
				
				case Types.INTEGER:
					int ival = rs.getInt(cd.getColName());
					if(rs.wasNull()){
						sb.append(colName+" is null ");
					}else{
						sb.append(colName+" = "+ival);
					}
					break;
				case Types.BIGINT:
					long lval = rs.getLong(cd.getColName());
					if(rs.wasNull()){
						sb.append(colName+" is null ");
					}else{
						sb.append(colName+" = "+lval);
					}
					break;
				case Types.DOUBLE:
				case Types.NUMERIC: 
				case Types.DECIMAL:
					double dval = rs.getDouble(cd.getColName());
					if(rs.wasNull()){
						sb.append(colName+" is null ");
					}else{
						sb.append(colName+" = "+dval);
					}
					break;
				case Types.DATE:sb.append(colName+" = "+Q+rs.getDate(cd.getColName())+Q);break;
				
				
				case Types.ARRAY:sb.append(colName+" = "+rs.getArray(cd.getColName())+"");break;
				case 1111: sb.append(colName+" = "+Q+rs.getString(cd.getColName())+Q);break;
				
				//case Types.ARRAY Types.BIGINT, BINARY, BIT, BLOB, BOOLEAN, CHAR CLOB
				//DATALINK, DISTINCT,DOUBLE,FLOAT,JAVA_OBJECT,LONGVARCHAR,LONGNVARCHAR,LONGVARBINARY
				//NCHAR,NCLOB,NULL,OTHER,REAL,REF,REF_CURSOR,ROWID,SMALLINT,SQLXML,STRUCT,TIME,TIME_WITH_TIMEZONE,TIMESTAMP,TIMESTAMP_WITH_TIMEZONE
				//TINYINT,VARBINARY,VARCHAR
				default : sb.append(colName+" = "+"\""+rs.getString(cd.getColName())+"\"");break;			
			}
		}
		
		return sb.toString();
	}


	
	private static String removeSingleQuotes(String string) {
		return string.replaceAll("'", "");
	}

	@SuppressWarnings("unused")
	private static void compareData(DatabaseManager sourceDatabase, DatabaseManager targetDatabase, String schema){
	
	}
	


}
