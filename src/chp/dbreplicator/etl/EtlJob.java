package chp.dbreplicator.etl;

import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jhg.util.TextFile;

import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import chp.dbreplicator.ColumnDefinition;
import chp.dbreplicator.Database;
import chp.dbreplicator.DatabaseManager;
import chp.dbreplicator.Index;
import chp.dbreplicator.Log;

public class EtlJob {

	
	private static final String EXIT_FAIL_MSG = "Exit due to premature failure.";
	private static final String D = "\t";
	private static final String noq = "";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private static String Q = noq;	
	
	private static int getMagnitude(int countResult) {
		final int X = 20;//affects buffer/speed. 10 fast and hoggish, 100 slow and birdish
		final int F = 10;
		final int Y = X*F;
		
		int rv = X;
		if(countResult>Y){
			rv = (int)countResult/X;
		}
		return rv;
	}		
	
	/*
	 * ETL Jobs:
	 * 
	 * Please BACKUP first before use!!!
	 * 
	 *    NAME							METHOD							TESTED?
	 * 1. Benchmarking: Hewitt			etlBenchmarkingHewitt			false
	 * 2. Benchmarking: TowersWatson	etlBenchmarkingTowers			false
	 * 3. Benchmarking: Mercer			etlBenchmarkingMercer			false	
	 * 4. MarketQuest					etlMarketReports				false		MY2015 = 2014u2, CY2014 = 2014u1 (MY subtract 1 & u2, CY no change, u1)
	 * 5. NetworkCompare 				etlNetworkCompare				false
	 * 6. EmployerSearch 				etlEmployerSearch				false
	 * 7. Blue Solutions Catalog		etlBlueSolutionsCatalog			false
	 * 
	 * 
	 * Steps to take:
	 * 
	 * 1. ensure your files in data/pgcfg are correct (market reports requires the upload year and number.
	 * 2. verify that your schemas match or will match if updated.
	 * 3. verify your connections, user, schema, server
	 * 4. make a backup
	 * 
	 * 
	 * 
	 * 
	 */
	
	public static void main(String[] args){
		//testSourceAndTarget();
		//compareSchemas(Database.DMCUST , Database.DMFRW , "employer");
		etlBenchmarkingHewitt("1019");
		etlBenchmarkingTowers("1016");
		//also need to insert into a row into the dataset.
		//etlBlueSolutionsCatalog(true);
		//etlMarketReports(true,"2014","2");
		//etlNetworkCompare(true);
		//etlBenchmarkingShared(true);
		//etlBenchmarkingMercer(true);
		//etlTestEmployerSearch(true);
		//etlEmployerSearch(true);
	}
	
	//does this have the new tiered network in the table?

	
	public static void etlBenchmarkingHewitt(String dataSet){
		etl(dataSet,Database.DW,Database.DMDEVNEW,"benchmarking_hewitt");
	}	
	
	public static void etlBenchmarkingTowers(String dataSet){
		etl(dataSet,Database.DW,Database.DMDEVNEW,"benchmarking_towers");
	}
	
	public static void etlBenchmarkingMercer(String dataSet){
		etl(dataSet,Database.DW,Database.DMDEVNEW,"benchmarking_mercer");
	}
	
	/*
	 * Data_sets table needs a manual insert before ETL if doing a new set.
	 */
	public static void etlBenchmarkingShared(boolean cleanTarget){
		etl(cleanTarget,Database.DW,Database.DMDEVNEW,"benchmarking_shared");
	}		
	
	
	public static void etlBenchmarking(boolean cleanTarget){
		etl(cleanTarget,Database.DW,Database.DMDEVNEW,"benchmarking");
	}	
	
	/*
	 * Add the new schema in advance. e.g.:select "valuequest"."create_partition_namespace"(2014,2)
	 * Change the year and upload in mapping file.
	 * Insert into valuequest_2014u2.data_set (cq_year,upload,incurred_start,incurred_end,paid,type)values(2014,1,'2014-07-01','2015-06-30','2015-08-31','MY')
	 */
	public static void etlMarketReports(boolean cleanTarget, String year, String upload){
		etl(cleanTarget,Database.DW,Database.DMFDEV,"market_reports",year,upload);
		//getCounts(Database.DMFDEV,"valuequest_2014u1","2014","1");
	}
	
	
	public static void etlNetworkCompare(boolean cleanTarget){
		etl(cleanTarget,Database.DW,Database.DMFPRD,"network_compare");//IDSProd on MSSQL -> dbtest/foundation_data_mart
	}	
	
	public static void etlTestEmployerSearch(boolean cleanTarget){
		etl(cleanTarget,Database.DWTEST,Database.DMFDEV,"employer_search");
	}	
	
	public static void etlEmployerSearch(boolean cleanTarget){
		etl(cleanTarget,Database.DW,Database.DMFDEV,"employer_search");
	}

	public static void etlBlueSolutionsCatalog(boolean cleanTarget){
		etl(cleanTarget,Database.DW,Database.DMDEVNEW,"blue_solutions_catalog");
	}
	
	@SuppressWarnings("unused")
	private static void getCounts(Database db,String schema){
		Log.pl("\n\nStarting Verification of "+db.name()+" on "+new java.util.Date());
		DatabaseManager database = new DatabaseManager(db);
		database.connect();
		boolean connected = database.test();
		Log.pl("Connected to "+db.name()+" is connected: "+connected);		
		if(!connected){
			Log.pl(EXIT_FAIL_MSG);
			database.close();
			return;
		}
		
		List<String> tables = database.getTables(schema);
		for(String table:tables){
			String sql = "select count(*) from "+schema+"."+table;
			database.query(sql);
			int count = database.getCountResult();
			Log.pl(table+":"+count+"\n");
		}
		Log.pl(" ");
		database.close();
		Log.pl("\n\nFinished on "+new java.util.Date()+"!");	
	}
	
	private static void etl(boolean cleanTarget,Database source, Database target, String filename){
		etl(cleanTarget,source,target,filename,"","");
	}
	
	//@SuppressWarnings("boxing")
	private static void etl(boolean cleanTarget,Database source, Database target, String filename, String year, String upload){
		Log.pl("Starting Copy of "+source.name()+" to "+target.name()+" with "+filename+" on "+new java.util.Date());
		Log.pl("java.lib.path -- Be sure to copy lib/sqljdbc_auth.dll here: "+System.getProperty("java.library.path"));
		DatabaseManager sourceDatabase = new DatabaseManager(source);
		DatabaseManager targetDatabase = new DatabaseManager(target);	//	
		sourceDatabase.connect();
		boolean connected = sourceDatabase.test();
		Log.pl("Connected to "+source.name()+" is connected: "+connected);
	
		
		if(!connected){
			Log.pl(EXIT_FAIL_MSG);
			targetDatabase.close();
			return;
		}
		
		targetDatabase.connect();
		connected = targetDatabase.test();
		Log.pl("Connected to "+target.name()+" is connected: "+connected);		
		if(!connected){
			Log.pl(EXIT_FAIL_MSG);
			targetDatabase.close();
			return;
		}
		
		
		TextFile f = new TextFile("data/pgcfg/"+filename+".txt");
		Map<String,String> viewTableMapping = f.getMapping();

		List<String> l = new ArrayList<String>(viewTableMapping.keySet());
		Collections.reverse(l);
		
		for(String sourceRelation:l){
			String destTable = viewTableMapping.get(sourceRelation);
		
			if(cleanTarget){
				cleanTable(targetDatabase,destTable);
			}	
		}
		Log.pl("\n ");	
		for(String sourceRelation:viewTableMapping.keySet()){
			String destTable = viewTableMapping.get(sourceRelation);

			String schema = destTable.substring(0,destTable.indexOf('.')); 
			String table = "";
			Log.pl("Schema: "+schema+"  , Table:"+table+" ");
			
			List<Index> indexes = targetDatabase.getIndexes(schema, table);
			
			
			for(Index idx:indexes){
				//Log.pl("Index: "+idx);
				String idxName = idx.getIndexName();
				if(!idxName.endsWith("_pkey")){
					String ddl = "DROP INDEX "+schema+"."+idxName;
					Log.pl("Dropping index :  '"+ddl+"'");
					targetDatabase.execute(ddl);
				}
			}				
			
			
			performCopy( sourceDatabase, targetDatabase, sourceRelation, destTable, year, upload);
			
			
			boolean indexesExist = targetDatabase.doesAnyIndexExist(schema, table);
			if(!indexesExist){
				
				for(Index idx:indexes){
					String idxName = idx.getIndexName();
					if(!idxName.endsWith("_pkey")){
					
						String ddl = "CREATE INDEX "+idxName+" ON "+idx.getTableSchema()+"."+idx.getTableName()+" USING btree ("+idx.getColumnName()+")";
						targetDatabase.execute(ddl);
					}
						//boolean indexesExist = sourceDatabase.doesAnyIndexExist(schema, table);
				}
			}			
			
		}
		
		sourceDatabase.close();
		targetDatabase.close();
		
		Log.pl("\n\nFinished on "+new java.util.Date()+"!");			
	}		
	
	private static void etl(String dataSet,Database source, Database target, String filename){
		Log.pl("Starting Copy of "+source.name()+" to "+target.name()+" with "+filename+" on "+new java.util.Date());
		Log.pl("java.lib.path -- Be sure to copy lib/sqljdbc_auth.dll here: "+System.getProperty("java.library.path"));
		DatabaseManager sourceDatabase = new DatabaseManager(source);
		DatabaseManager targetDatabase = new DatabaseManager(target);	//	
		sourceDatabase.connect();
		boolean connected = sourceDatabase.test();
		Log.pl("Connected to "+source.name()+" is connected: "+connected);
	
		
		if(!connected){
			Log.pl(EXIT_FAIL_MSG);
			targetDatabase.close();
			return;
		}
		
		targetDatabase.connect();
		connected = targetDatabase.test();
		Log.pl("Connected to "+target.name()+" is connected: "+connected);		
		if(!connected){
			Log.pl(EXIT_FAIL_MSG);
			targetDatabase.close();
			return;
		}
		
		
		TextFile f = new TextFile("data/pgcfg/"+filename+".txt");
		Map<String,String> viewTableMapping = f.getMapping();

		List<String> listSourceTables = new ArrayList<String>(viewTableMapping.keySet());
		Collections.reverse(listSourceTables);
		

		Log.pl("\n ");	
		for(String sourceRelation:viewTableMapping.keySet()){
			String destTable = viewTableMapping.get(sourceRelation);

			String schema = destTable.substring(0,destTable.indexOf('.')); 
			String table = destTable.substring(destTable.indexOf('.')+1,destTable.length());
			Log.pl("Schema: '"+schema+"'  , Table: '"+table+"' ");
			
			List<Index> indexes = targetDatabase.getIndexes(schema, table);
			
			
			for(Index idx:indexes){
				//Log.pl("Index: "+idx);
				String idxName = idx.getIndexName();
				if(!idxName.endsWith("_pkey")){
				String ddl = "DROP INDEX "+schema+"."+idxName;
				Log.pl("Dropping index :  '"+ddl+"'");
				targetDatabase.execute(ddl);
				}
				
			}				
			
			String deleteByDataSetDML = "delete from "+destTable+" where data_set = "+dataSet+"";
			targetDatabase.execute(deleteByDataSetDML);
			
			performCopy( sourceDatabase, targetDatabase, sourceRelation, destTable, dataSet);
			
			
			boolean indexesExist = targetDatabase.doesAnyIndexExist(schema, table);
			if(!indexesExist){
				
				for(Index idx:indexes){
					String idxName = idx.getIndexName();
					if(!idxName.endsWith("_pkey")){
					
						String ddl = "CREATE INDEX "+idxName+" ON "+idx.getTableSchema()+"."+idx.getTableName()+" USING btree ("+idx.getColumnName()+")";
						targetDatabase.execute(ddl);
					}
						//boolean indexesExist = sourceDatabase.doesAnyIndexExist(schema, table);
				}
			}			
			
		}
		
		sourceDatabase.close();
		targetDatabase.close();
		
		Log.pl("\n\nFinished on "+new java.util.Date()+"!");			
	}		

	//@SuppressWarnings("boxing")
	private static void performCopy(DatabaseManager sourceDatabase, DatabaseManager targetDatabase,
			String sourceRelation, String destTable, 
			String year, String upload) {
		
		
		List<ColumnDefinition> cds = sourceDatabase.getColumnDefsFromDbMeta(sourceRelation);
		//for(ColumnDefinition cd:cds){
			//int cdcoltype = cd.getColType();
			//Log.pl("Column: "+cd.getColName()+" type:"+DatabaseManager.TYPES.get(cdcoltype)+"");
		//}
		String countQuery = "select count(*) from "+sourceRelation;
		if(sourceRelation.startsWith("market_reports.")){
			countQuery += " where cq_year="+year+" and upload="+upload;
		}		
		sourceDatabase.query(countQuery);
		int countResult = sourceDatabase.getCountResult();
		Log.pl("Source Result Size:"+countResult);
		int max = getMagnitude(countResult);
		String query = "select * from "+sourceRelation;
		
		if(sourceRelation.startsWith("market_reports.")){
			query += " where cq_year="+year+" and upload="+upload;
		}
		
		Log.pl("Source query: "+query);
		int count=0;
		ResultSet rs = sourceDatabase.queryLarge(query);


		
		try{
			//Log.pl("Rows:"+rs.getFetchSize());
			StringBuilder sb = new StringBuilder();
			while(rs.next()){
				
				boolean first = true;
				for(ColumnDefinition cd:cds){
					if(!first){sb.append(D);}else{first=false;}
					extractResultIntoStringBuilder(rs, sb, cd);
					
				}//for(ColumnDefinition cd:cds)
				sb.append("\n");
				//Log.profile("    Read Record "+count+"  appending to file:"+filename);
				if(count%max==0){
					Log.pl(new Date()+" Copying data to target: "+destTable+" at count: "+count);
					
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
		Log.pl("Finished copying "+destTable+".\n\n");
	}

	//@SuppressWarnings("boxing")
	private static void performCopy(DatabaseManager sourceDatabase, DatabaseManager targetDatabase,
			String sourceRelation, String destTable, 
			String dataSet) {
		
		
		List<ColumnDefinition> cds = sourceDatabase.getColumnDefsFromDbMeta(sourceRelation);
		//for(ColumnDefinition cd:cds){
			//int cdcoltype = cd.getColType();
			//Log.pl("Column: "+cd.getColName()+" type:"+DatabaseManager.TYPES.get(cdcoltype)+"");
		//}
		String countQuery = "select count(*) from "+sourceRelation;
		if(sourceRelation.startsWith("benchmarking.")){
			countQuery += " where data_set="+dataSet+" ";
		}		
		sourceDatabase.query(countQuery);
		int countResult = sourceDatabase.getCountResult();
		Log.pl("Source Result Size:"+countResult);
		int max = getMagnitude(countResult);
		String query = "select * from "+sourceRelation;
		
		if(sourceRelation.startsWith("benchmarking.")){
			query += " where data_set="+dataSet+"";
		}
		
		Log.pl("Source query: "+query);
		int count=0;
		ResultSet rs = sourceDatabase.queryLarge(query);


		
		try{
			//Log.pl("Rows:"+rs.getFetchSize());
			StringBuilder sb = new StringBuilder();
			while(rs.next()){
				
				boolean first = true;
				for(ColumnDefinition cd:cds){
					if(!first){sb.append(D);}else{first=false;}
					extractResultIntoStringBuilder(rs, sb, cd);
					
				}//for(ColumnDefinition cd:cds)
				sb.append("\n");
				//Log.profile("    Read Record "+count+"  appending to file:"+filename);
				if(count%max==0){
					Log.pl(new Date()+" Copying data to target: "+destTable+" at count: "+count);
					
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
		Log.pl("Finished copying "+destTable+".\n\n");
	}	
	
	private static void extractResultIntoStringBuilder(ResultSet rs,
			StringBuilder sb, ColumnDefinition cd) throws SQLException {
		int cdcoltype = cd.getColType();
		
		switch (cdcoltype){
			
			//from DW: case Types.VARCHAR:sb.append("\""+rs.getString(cd.getColName())+"\"");break;
			case Types.VARCHAR:
			case Types.NVARCHAR:sb.append(Q+rs.getString(cd.getColName())+Q);break;
			
			case Types.INTEGER:
				int ival = rs.getInt(cd.getColName());
				if(rs.wasNull()){
					sb.append("null");
				}else{
					sb.append(ival);
				}
				break;
			case Types.BIGINT:
				long lval = rs.getLong(cd.getColName());
				if(rs.wasNull()){
					sb.append("null");
				}else{
					sb.append(lval);
				}
				break;
			case Types.DOUBLE:
			case Types.NUMERIC: 
			case Types.DECIMAL:
				double dval = rs.getDouble(cd.getColName());
				if(rs.wasNull()){
					sb.append("null");
				}else{
					sb.append(dval);
				}
				break;
			case Types.DATE:sb.append(Q+formatDate(rs.getDate(cd.getColName()))+"\"");break;
			
			
			case Types.ARRAY:sb.append(""+rs.getArray(cd.getColName())+"");break;
			case 1111: sb.append(""+rs.getString(cd.getColName())+"");break;
			
			//case Types.ARRAY Types.BIGINT, BINARY, BIT, BLOB, BOOLEAN, CHAR CLOB
			//DATALINK, DISTINCT,DOUBLE,FLOAT,JAVA_OBJECT,LONGVARCHAR,LONGNVARCHAR,LONGVARBINARY
			//NCHAR,NCLOB,NULL,OTHER,REAL,REF,REF_CURSOR,ROWID,SMALLINT,SQLXML,STRUCT,TIME,TIME_WITH_TIMEZONE,TIMESTAMP,TIMESTAMP_WITH_TIMEZONE
			//TINYINT,VARBINARY,VARCHAR
			default : sb.append("\""+rs.getString(cd.getColName())+"\"");break;
		}
	}		

	private static String formatDate(java.sql.Date inDate){
		String rv = "";
		if(inDate==null){
			rv = "null";
		}else{
			rv = sdf.format(inDate);
		}
		return rv;
	}
	
	private static void cleanTable(DatabaseManager targetDatabase,	String destTable) {
		targetDatabase.clearTable(destTable);
	}


	
	
	private static void copyPostgres(DatabaseManager targetDatabase,String destTable, String s) throws SQLException {
		CopyIn cpIN=null;
		CopyManager cm = new CopyManager((BaseConnection) targetDatabase.getConnection());
		String sql = "COPY "+destTable+" FROM STDIN  WITH DELIMITER '"+D+"' NULL 'null' ";
		Log.pl("Copy statement:"+sql);
		cpIN = cm.copyIn(sql);
		byte[] bytes = s.getBytes();
		cpIN.writeToCopy(bytes, 0, bytes.length);
		cpIN.endCopy();
		
	}
			
	

	
	//@SuppressWarnings("boxing")
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


	
	public static void testSourceAndTarget(){
		test(Database.DMCUST, Database.DMFDEV);
	}
	
	

	
	
	public static void testPostgresDataMart(){
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

		
/*
	DM(       Rdbms.POSTGRESQL,	"org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5444/DM_DEV",				"whs_viewer",	"whs_viewer"),
	DMDEVNEW( Rdbms.POSTGRESQL,	"org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5432/data_mart",				"whs_viewer",	"whs_viewer"),
	DMTESTOLD(Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbtest01.corp.chpinfo.com:5444/DM_TEST",				"whs_viewer",	"whs_viewer"),
	DMTESTNEW(Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbtest01.corp.chpinfo.com:5432/data_mart",				"whs_viewer",	"whs_viewer"),
	DMPPRDOLD(Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbprp01.corp.chpinfo.com:5444/DM_PROD",				"whs_viewer",	"whs_viewer"),
	DMPPRDNEW(Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbprp01.corp.chpinfo.com:5432/data_mart",				"whs_viewer",	"whs_viewer"),
	DMPRODOLD(Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbprp01.corp.chpinfo.com:5444/DM_PROD",				"whs_viewer",	"whs_viewer"),
	DMPRODNEW(Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbprp01.corp.chpinfo.com:5432/data_mart",				"whs_viewer",	"whs_viewer"),
	
 */
	}
	public static void testPostgresFoundation(){
		
	}
	public static void testSQLIDSProd(){
		
	}
	
	
	
	
	
	
	@SuppressWarnings("unused")
	private static void compareSchemas(Database source, Database target, String schema){
		Log.pl("Starting Comparison of "+source.name()+" to "+target.name()+" with schema: "+schema+" on "+new java.util.Date());
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

