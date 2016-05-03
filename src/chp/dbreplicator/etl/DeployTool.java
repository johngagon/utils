package chp.dbreplicator.etl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
//import java.util.Map;

import org.postgresql.core.BaseConnection;

import chp.dbreplicator.ColumnDefinition;
import chp.dbreplicator.Database;
import chp.dbreplicator.DatabaseManager;
import chp.dbreplicator.Log;
//import jhg.util.TextFile;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;

public class DeployTool {
	
	/*
	 * D : tab delimiter
	 * noq: non quoted for some data types in some database vendors do not use quotes in some cases while others do.
	 * Q : switches between dq or noq
	 * 
	 */
	private static final String D = "\t";

	private static final String noq = "";
	
	@SuppressWarnings("unused")
	private static final String dq = "\"";
	
	private static String Q = noq;
	
	/*
	 * 	 For data refresh only.
	 *   For schema development updates, we have to manually apply schema changes if any.
	 *   For all uses, please BACKUP first!!!
	 * 
	 *   EDB                        dbdev03:5444->dbtest01:5444	dbtest01:5444->dbprp01:5444		dbtest01:5444->dbprod04:5444
	 *   --------------------------------------------------------------------------------------------------------------------        
	 *   EmployerSearch (ES)		esUAT						esPRP							esPRD
	 *   MarketReports (MR)			mrUAT(2XXXuY)				mrPRP(2XXXuY					mrPRD(2XXXuY)
	 *   Network Compare (NC)		ncUAT						ncPRP							ncPRD
	 *   
	 *   ====================================================================================================================
	 *   
	 *   9.3						5432-->
	 *   --------------------------------------------------------------------------------------------------------------------
	 *   Benchmarking (SDB)			bmUAT(consultant)			bmPRP(consultant)				bmPRD(consultant)
	 *   Blue Solutions (BSC)		bscUAT						bscPRP							bscPRD
	 * 
	 *   
	 * 	main() 						: calls an etl
	 *  testSourceAndTarget			: calls test(source,target).
	 *  test(source,target)			: tests the source and target
	 *  testLegacy()				: tests all the connections for postgres known. Inherited 9.3 new and the enterprisedb old. (foundation not included)
	 *  
	 *  deploy						: (called by each of the above)
	 *  performCopy					: called by deploy
	 *  cleanTable					: called by deploy
	 *  copyPostgres				: called by performCopy
	 *  getMagnitude				: determines the modulus factor (multiple of 10) for progress reporting, called by performCopy
	 *  
	 *  insertPostgres				: not used anywhere, plan on moving to DatabaseManager (with intelligent logic to check database is postgres)
	 *  handle						: not used anywhere, very generic way of converting ResultSet ot Object[]s
	 */
	public static void main(String[] args){
		//esUAT2PRD();
		ncRestore();
	}
	public static void testSourceAndTarget(){
		//test(Database.DMCUST, Database.DMFRW);
	}
	
	/*
	 * Employer Search
	 */
	public static void esUAT2PRD(){
		deploy(true, Database.DMFUAT, Database.DMFPRD,"employer");
	}
	
	public static void esUAT(){
		deploy(true, Database.DMFDEV, Database.DMFUAT,"employer");
	}

	public static void esPRP(){
		deploy(true, Database.DMFDEV, Database.DMFPRP,"employer");
	}
	
	public static void esPRD(){
		deploy(true, Database.DMFDEV, Database.DMFPRD,"employer");
	}	
	
	
	/*
	 * Market Reports
	 * 		Need the specific dataset for this one
	 * 		Need to create new table manually.
	 * 		Need to update valuequest partition manually.
	 */
	public static void mrUAT(String dataset){
		deploy(true, Database.DMFDEV, Database.DMFUAT,"valuequest_"+dataset);
	}

	public static void mrPRP(String dataset){
		deploy(true, Database.DMFDEV, Database.DMFPRP,"valuequest_"+dataset);
	}
	
	public static void mrPRD(String dataset){
		deploy(true, Database.DMFDEV, Database.DMFPRD,"valuequest_"+dataset);
	}		

	/*
	 * Network Compare
	 */
	
	//Custom: restore DM_DEV to foundation_data_mart
	public static void ncRestore(){
		deploy(true, Database.DM, Database.DMFDEV,"whs_viewer");
	}
	
	public static void ncUAT(){
		deploy(true, Database.DMFDEV, Database.DMFUAT,"whs_viewer");
	}

	public static void ncPRP(){
		deploy(true, Database.DMFDEV, Database.DMFPRP,"whs_viewer");
	}
	
	public static void ncPRD(){
		deploy(true, Database.DMFDEV, Database.DMFPRD,"whs_viewer");
	}			
	
	
	/*
	 * Benchmarking 		
	 */
	
	public static void bmUAT(){
		deploy(true, Database.DMDEVNEW, Database.DMTESTNEW,"benchmarking");
	}
	public static void bmPRP(){
		deploy(true, Database.DMTESTNEW, Database.DMPPRDNEW,"benchmarking");
	}	
	public static void bmPRD(){
		deploy(true, Database.DMTESTNEW, Database.DMPRODNEW,"benchmarking");
	}		
	
	public static void bmUAT(String consultant){//hewitt,mercer,towers
		deploy(true, Database.DMDEVNEW, Database.DMTESTNEW,"benchmarking_"+consultant);
	}
	public static void bmPRP(String consultant){
		deploy(true, Database.DMTESTNEW, Database.DMPPRDNEW,"benchmarking_"+consultant);
	}	
	public static void bmPRD(String consultant){
		deploy(true, Database.DMTESTNEW, Database.DMPRODNEW,"benchmarking"+consultant);
	}			
	
	/*
	 * Blue Solutions Catalog
	 */
	public static void bscUAT(){
		deploy(true, Database.DMDEVNEW, Database.DMTESTNEW,"blue_solutions");
	}
	
	public static void bscPRP(){
		deploy(true, Database.DMTESTNEW, Database.DMPPRDNEW,"blue_solutions");
	}	
	
	public static void bscPRD(){
		deploy(true, Database.DMTESTNEW, Database.DMPRODNEW,"blue_solutions");
	}	

	
	
	
	@SuppressWarnings("unused")
	private static void test(Database source, Database target){
		Log.pl("Testing connections of "+source.name()+" and "+target.name()+" on "+new java.util.Date());
		Log.pl("java.lib.path -- Be sure to copy lib/sqljdbc_auth.dll here: "+System.getProperty("java.library.path")+" \n\n");
		DatabaseManager sourceDatabase = new DatabaseManager(source);
		DatabaseManager targetDatabase = new DatabaseManager(target);	//	
		sourceDatabase.connect();
		Log.pl("Connected to source: "+source.name()+" is connected: "+sourceDatabase.test()+"\n");
	
		targetDatabase.connect();
		Log.pl("Connected to target: "+target.name()+" is connected: "+targetDatabase.test()+"\n");		
		
		sourceDatabase.close();
		targetDatabase.close();
		
		Log.pl("Finished on "+new java.util.Date()+"!");			
	}

	//@SuppressWarnings("boxing")
	private static void deploy(boolean cleanTarget,Database source, Database target, String schema){
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
			if(!"request_log".equals(table)){
				performCopy(cleanTarget, sourceDatabase, targetDatabase, sourceRelation, destTable);
			}
		}
		
		sourceDatabase.close();
		targetDatabase.close();
		
		Log.pl("Finished on "+new java.util.Date()+"!");			
	}
	



	
	/**
	 * Built for a postgres to postgres copy.
	 * 
	 * @param cleanTarget  true if you want to delete data.
	 * @param sourceDatabase
	 * @param targetDatabase
	 * @param sourceRelation
	 * @param destTable
	 */
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


	//TODO move this to database manager
    static Object[] handle(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }
    
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
        Object[] result = new Object[cols];

        for (int i = 0; i < cols; i++) {
            result[i] = rs.getObject(i + 1);
        }

        return result;
    }	
    
	private static int getMagnitude(int countResult) {
		int rv = 10;
		if(countResult>100){
			rv = (int)countResult/10;
		}
		return rv;
	}    
	
	//TODO move this to DatabaseManager
	static boolean insertPostgres(DatabaseManager targetDatabase, String insertQuery, List<ColumnDefinition> cds, Object[] row) throws SQLException {
		
		List<Object> objArr = Arrays.asList(row);
		
		boolean success =  targetDatabase.executeUpdate(insertQuery, cds, objArr);
		if(!success){
			Log.pl("Insert fail:"+insertQuery);
			Log.pl("Object Array:");
			Log.hr(20,"-");
			Log.print(objArr);
			Log.hr(20,"-");
		}else{
			Log.pl("Inserted row.");
		}
		return success;
	}	    
    

	
	static void testLegacy(){
		DatabaseManager test1 = new DatabaseManager(Database.DM);
		DatabaseManager test2 = new DatabaseManager(Database.DMDEVNEW);
		DatabaseManager test3 = new DatabaseManager(Database.DMTESTOLD);
		DatabaseManager test4 = new DatabaseManager(Database.DMTESTNEW);
		DatabaseManager test5 = new DatabaseManager(Database.DMPPRDOLD);
		DatabaseManager test6 = new DatabaseManager(Database.DMPPRDNEW);
		DatabaseManager test7 = new DatabaseManager(Database.DMPRODOLD);
		DatabaseManager test8 = new DatabaseManager(Database.DMPRODNEW);
		
		Log.pl("Connecting 1");
		test1.connect();
		Log.pl("1 Connected "+test1.test()+"\n\n");		//PASS
		
		Log.pl("Connecting 2");
		test2.connect();
		Log.pl("2 Connected "+test2.test()+"\n\n");		//FAIL: FATAL: password authentication failed for user "whs_viewer" (for benchmarking)
		
		Log.pl("Connecting 3");
		test3.connect();
		Log.pl("3 Connected "+test3.test()+"\n\n");		//PASS
		
		Log.pl("Connecting 4");
		test4.connect();
		Log.pl("4 Connected "+test4.test()+"\n\n");		//FAIL: null
		
		Log.pl("Connecting 5");
		test5.connect();
		Log.pl("5 Connected "+test5.test()+"\n\n");		//FAIL: FATAL: password authentication failed for user "whs_viewer"
		
		Log.pl("Connecting 6");
		test6.connect();
		Log.pl("6 Connected "+test6.test()+"\n\n");		//FAIL: FATAL: password authentication failed for user "whs_viewer"
		
		Log.pl("Connecting 7");
		test7.connect();
		Log.pl("7 Connected "+test7.test()+"\n\n");		//PASS
		
		Log.pl("Connecting 8");
		test8.connect();
		Log.pl("8 Connected "+test8.test()+"\n\n");		//FAIL: FATAL: password authentication failed for user "whs_viewer"
	}
	

	
	@SuppressWarnings("unused")
	private static void compareSchemas(Database source, Database target, String schema){
		Log.pl("Starting Copy of "+source.name()+" to "+target.name()+" with schema: "+schema+" on "+new java.util.Date());
		Log.pl("java.lib.path -- Be sure to copy lib/sqljdbc_auth.dll here: "+System.getProperty("java.library.path"));
		DatabaseManager sourceDatabase = new DatabaseManager(source);
		DatabaseManager targetDatabase = new DatabaseManager(target);	//	
		sourceDatabase.connect();
		Log.pl("Connected to "+source.name()+" is connected: "+sourceDatabase.test());
	
		targetDatabase.connect();
		Log.pl("Connected to "+target.name()+" is connected: "+targetDatabase.test());
		
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
				}else{
					ColumnDefinition targetCol = targetCols.get(targetCols.indexOf(sourceCol));
					Log.pl("Found :"+sourceCol.getColName()+ " matches "+targetCol.getColName());
				}
			}
		}
		Log.pl("----------- ");
		sourceDatabase.close();
		targetDatabase.close();
		
		Log.pl("Finished on "+new java.util.Date()+"!");	
		
		
		//for(ColumnDefinition cd:cds){
		//	int cdcoltype = cd.getColType();
		//	Log.pl("Column: "+cd.getColName()+" type:"+DatabaseManager.TYPES.get(cdcoltype)+"");
		//}		
	}	
}



/*

public static void etlCustom(boolean cleanTarget){
	deploy(cleanTarget, Database.DMCUST, Database.DMFRW,"employer");//uses schema, not file
}	

@SuppressWarnings("unused")
private static void etlAvention(boolean clean){
	//test(Database.DWP, Database.DMFRW);
	etl(clean,Database.DWP,Database.DMFRW,"employer_search");
}

//@SuppressWarnings("boxing")
private static void etl(boolean cleanTarget,Database source, Database target, String filename){
	Log.pl("Starting Copy of "+source.name()+" to "+target.name()+" with "+filename+" on "+new java.util.Date());
	Log.pl("java.lib.path -- Be sure to copy lib/sqljdbc_auth.dll here: "+System.getProperty("java.library.path"));
	DatabaseManager sourceDatabase = new DatabaseManager(source);
	DatabaseManager targetDatabase = new DatabaseManager(target);	//	
	sourceDatabase.connect();
	Log.pl("Connected to "+source.name()+" is connected: "+sourceDatabase.test());

	targetDatabase.connect();
	Log.pl("Connected to "+target.name()+" is connected: "+targetDatabase.test());		
	
	TextFile f = new TextFile("data/pgcfg/"+filename+".txt");
	Map<String,String> viewTableMapping = f.getMapping();
	
	for(String sourceRelation:viewTableMapping.keySet()){
		String destTable = viewTableMapping.get(sourceRelation);
		
		performCopy(cleanTarget, sourceDatabase, targetDatabase, sourceRelation, destTable);
	}
	
	sourceDatabase.close();
	targetDatabase.close();
	
	Log.pl("Finished on "+new java.util.Date()+"!");			
}

	//@SuppressWarnings("boxing")
	public static void etlBenchmarkingOldDev(boolean cleanTarget){
		etl(cleanTarget,Database.DW,Database.DMFRW,"benchmarking");//IDSProd on MSSQL -> DM_DEV on postgres
	}
	
	//@SuppressWarnings("boxing")
	public static void etlNetworkCompareNew(boolean cleanTarget){
		etl(cleanTarget,Database.DW,Database.DMFRW,"network_compare");//IDSProd on MSSQL -> DM_DEV on postgres
	}
	
	//@SuppressWarnings("boxing")
	public static void etlMarketReportsNew(boolean cleanTarget){
		etl(cleanTarget,Database.DW,Database.DMFRW,"market_reports");
	}
	
	//@SuppressWarnings("boxing")
	public static void etlEmployerSearchNew(boolean cleanTarget){
		etl(cleanTarget,Database.DW,Database.DMFRW,"employer_search");
	}
	
		
*/
/*
public static void testPostgresFoundation(){
	
}
public static void testSQLIDSProd(){
	
}
*/