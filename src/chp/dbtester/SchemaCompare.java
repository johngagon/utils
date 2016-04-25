package chp.dbtester;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import chp.dbreplicator.ColumnDefinition;
import chp.dbreplicator.Database;
import chp.dbreplicator.DatabaseManager;
import chp.dbreplicator.Log;

@SuppressWarnings("unused")
public class SchemaCompare {
	
	public static class Mapping{
		public String table;
		public String standardIdColumn;
		public String trialIdColumn;		
		public Mapping(String _table, String _source, String _target){
			this.table = _table;
			this.standardIdColumn = _source;
			this.trialIdColumn = _target;
		}

	}
	
	private static Set<String> getListFromRs(ResultSet rs){
		Set<String> list = new HashSet<String>();
		try {
			while(rs.next()){
				String idNum = rs.getString(1);
				
				list.add(idNum);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static void report(String reportName,Set<String> list){
		Log.pl(reportName+" : "+list.size());
		for(String id:list){
			Log.pl("ID:"+id);
		}
	}
	
	/*
	 * company: 
	 * 	source: 267272 (distinct), 279936 non distinct (12,664 duplicates)
	 *  target: 267272 (distinct), 279795 non distinct (12,523 duplicates) 
	 *  diff:   141
	 *  
	 *  branch: missing
	 */
	
	public static void performCustom(){
		
		//add a "has provider column" and insert that as a condition in where for the new tables.
		
		Mapping benefitRange = new Mapping("employer.benefit_range","id","id");
		Mapping company = new Mapping("employer.company","duns_number","duns_number");
		Mapping companyBranch = new Mapping("employer.company_branch","branch_duns::int","branch");
		Mapping companySic = new Mapping("employer.company_sic","duns_number::int","company");
		Mapping duEmployeeCounts = new Mapping("employer.du_employee_counts","domestic_ultimate_duns::int","domestic_ultimate");
		Mapping employeeCount = new Mapping("employer.employee_count","id","id");
		Mapping fortuneRank = new Mapping("employer.fortune_rank","id","id");
		
		Mapping[] mappings = {employeeCount};//,benefitRange,company,companyBranch,companySic,duEmployeeCounts,employeeCount,fortuneRank};
	
		DatabaseManager standardDb = new DatabaseManager(Database.DMPRODOLD);
		DatabaseManager trialDb = new DatabaseManager(Database.DMCUST);	
		boolean standardConnected = standardDb.isConnected();
		boolean trialConnected = trialDb.isConnected();
		Log.pl("  Source Connected:"+standardConnected);
		Log.pl("  Target Connected:"+trialConnected);
		boolean pass = standardConnected && trialConnected;		
		
		standardDb.connect();
		trialDb.connect();
		Log.cr();
		for(Mapping m:mappings){
			String standardQuery = "select "+m.standardIdColumn+" from "+m.table;//+" order by "+m.standardIdColumn;
			String trialQuery = "select "+m.trialIdColumn+" from "+m.table;//+" order by "+m.trialIdColumn;
			
			ResultSet standardRs = standardDb.query(standardQuery);
			Set<String> standardIds = getListFromRs(standardRs);    
			Log.pl("Standard size: "+standardIds.size());
			
			ResultSet trialRs = trialDb.query(trialQuery);
			Set<String> trialIds = getListFromRs(trialRs);
			Log.pl("Trial's size: "+trialIds.size());
			Log.cr();
			Set<String> missing = new HashSet<String>(standardIds);
			
			Set<String> extras = new HashSet<String>(trialIds);
			boolean hasRemoved = missing.removeAll(trialIds);
			hasRemoved = extras.removeAll(standardIds);
			report("Missing",missing);
			report("Extras",extras);
		}
		Log.cr();
		standardDb.close();
		trialDb.close();				
		Log.pl("Finished on "+new java.util.Date()+"!");		
	}
	
	public static void main(String[] args){
		performCustom();

		
		
		/*
		 * 
		 * 1. test connections.
		 *  
		 * 2. check for table list match
		 * 
		 * 3. compare structure. standard (prod) and target (local) should be subset. target shouldn't have any missing columns.
		 * 
		 * 3. list tables, perform counts
		 * 
		 * 4. select data from standard on a table. Get column names. select those columns in target. 
		 * 
		 *   extra: 
		 *   
		 *   1. be able to use queries
		 * 
		 */

		
		//test(Database.DMPPRDOLD, Database.DMFRW);
		//compare(Database.DMPPRDOLD, Database.DMFRW,"employer");
		//compareSchemasStandalone(Database.DMPPRDOLD, Database.DMFRW,"employer");
	}
	
	public static void compare(Database source, Database target,String schema){
		Log.pl("Comparing Schemas of "+source.name()+" and "+target.name()+" with schema: "+schema+" on "+new java.util.Date());
		
		/*
		 * Test Connection
		 */
		Log.pl("Testing connections:\n");
		DatabaseManager sourceDatabase = new DatabaseManager(source);
		DatabaseManager targetDatabase = new DatabaseManager(target);	
		sourceDatabase.connect();
		targetDatabase.connect();
		boolean sourceConnected = sourceDatabase.isConnected();
		boolean targetConnected = targetDatabase.isConnected();
		Log.pl("  Source Connected:"+sourceConnected);
		Log.pl("  Target Connected:"+targetConnected);
		boolean pass = sourceConnected && targetConnected;
		
		if(pass){Log.pl("\nPASS!\n");}else{Log.pl("\nFAIL!\n");return;}

		/*
		 * Check tables
		 */
		Log.pl("Checking table count:\n");
		List<String> sourceTables = sourceDatabase.getTables(schema);
		Log.hr(80);
		List<String> targetTables = targetDatabase.getTables(schema);
	
		Log.pl("Source table Count: "+sourceTables.size());
		Log.pl("Target table Count: "+targetTables.size());
		pass = (sourceTables.size()== targetTables.size());
		
		if(pass){Log.pl("\nPASS!\n");}else{Log.pl("\nFAIL!\n");return;}
		
		
		/*
		 * Compare table names
		 */
		Log.pl("Comparing table names:\n");
		int discrepancyCount = 0;
		for(String table:sourceTables){
			if(!targetTables.contains(table)){
				Log.pl("Table from source: "+table+ " not found in target.");
				discrepancyCount++;
			}
		}
		Log.pl("Discrepancy Count: "+discrepancyCount);
		pass = (discrepancyCount==0);
		if(pass){Log.pl("\nPASS!\n");}else{Log.pl("\nFAIL!\n");return;}		
		
		/*
		 * Compare structures
		
		int discrepancyCount = compareSchemas(schema, sourceDatabase, targetDatabase, sourceTables);
		/
		* 
		*  Compare schema column types
		* 
		/
		Log.pl("Discrepancy Count: "+discrepancyCount);
		pass = (discrepancyCount==0);
		if(pass){Log.pl("\nPASS!\n");}else{Log.pl("\nFAIL!\n");return;}
		 */		
		
		/*
		 * Compare table record counts.
			
		Log.pl("Comparing table counts:\n");
		discrepancyCount = 0;
		int[] sourceCounts = new int[7];
		int[] targetCounts = new int[7];

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
		pass = (discrepancyCount==0);
		if(pass){Log.pl("\nPASS!\n");}else{Log.pl("\nFAIL!\n");return;}			
		 */
		
		/*
		 * Compare data.
		 */	
		
		discrepancyCount = 0;	
		int max_discrepancies = 10;
		String[] sourceQueries = {
				"select id, name from employer.benefit_range order by 1",
				"select duns_number, decision_maker_code, company_name, company_country_name, company_address_1, company_city, company_state, company_zip, global_ultimate_duns, domestic_ultimate_duns, parent_duns, company_level, fortune_1000_rank, year_started, parent_name, domestic_ultimate_name, primary_stock_symbol, company_website, commercial_credit_score, paydex_score, financial_stress_score, carrier_code, carrier_name, carrier_type_name, carrier_type_code, naics_national_name, naics_national_code, naics_industry_name, naics_industry_code, naics_group_name, naics_group_code, naics_subsector_name, naics_subsector_code, naics_sector_name, naics_sector_code, sic_industry_name, sic_industry_code, sic_industry_group_name, sic_industry_group_code, sic_major_group_code, sic_major_group_name, sic_division_name, sic_division_code, state_name, state_fips, data_as_of, adj_dom_employees_here, adj_dom_employees_rollup, adj_dom_employees_total, company_sales, company_pct_gr_sales_3_yr, company_pct_gr_sales_5_yr, domestic_ultimate_carrier_name, domestic_ultimate_carrier_code, employee_band, fortune_band, du_chp_member, stakeholder_code from employer.company order by 1",
				"select branch_duns, duns_number, company_name, state_abbreviation, carrier_code, branch_employee_rollup_ch from employer.company_branch  order by 1",
				"select duns_number, sic_industry_code, sic_industry_group_code, sic_major_group_code from employer.company_sic  order by 1",
				"select domestic_ultimate_duns, carrier_group_code, employees from employer.du_employee_counts  order by 1",
				"select id, name, code from employer.employee_count  order by 1",
				"select id, name, code from employer.fortune_rank  order by 1"
				
		};
		String[] targetQueries = {
				"select id, name from employer.benefit_range  order by 1",
				"select duns_number, decision_maker_code, company_name, company_country_name, company_address_1, company_city, company_state, company_zip, global_ultimate, domestic_ultimate, parent_duns, company_level, fortune_1000_rank, year_started, parent_name, domestic_ultimate_name, primary_stock_symbol, company_website, commercial_credit_score, paydex_score, financial_stress_score, carrier_code, carrier_name, carrier_type_name, carrier_type_code, naics_national_name, naics_national_code, naics_industry_name, naics_industry_code, naics_group_name, naics_group_code, naics_subsector_name, naics_subsector_code, naics_sector_name, naics_sector_code, sic_industry_name, sic_industry_code, sic_industry_group_name, sic_industry_group_code, sic_major_group_code, sic_major_group_name, sic_division_name, sic_division_code, state_name, state_fips, data_as_of, adj_dom_employees_here, adj_dom_employees_rollup, adj_dom_employees_total, company_sales, company_pct_gr_sales_3_yr, company_pct_gr_sales_5_yr, domestic_ultimate_carrier_name, domestic_ultimate_carrier_code, employee_band, fortune_band, du_chp_member, stakeholder_code from employer.company order by 1",
				"select branch, company, company_name, state_abbreviation, carrier_code, branch_employee_rollup_ch from employer.company_branch  order by 1",
				"select company, sic_industry_code, sic_industry_group_code, sic_major_group_code from employer.company_sic  order by 1",
				"select domestic_ultimate, carrier_group_code, employees from employer.du_employee_counts  order by 1",
				"select id, name, code from employer.employee_count  order by 1",
				"select id, name, code from employer.fortune_rank order by 1"				
		};
		for(int i=0;i<sourceQueries.length && i<targetQueries.length;i++){
			
			String sourceQuery = sourceQueries[i];
			String targetQuery = targetQueries[i];
			Log.pl("Comparing Data");
			Log.pl("  Source query: "+sourceQuery);
			Log.pl("  Target query: "+targetQuery);
			ResultSet sourceRs = sourceDatabase.query(sourceQuery);
			ResultSet targetRs = targetDatabase.query(targetQuery);
			int sourceColCount = sourceDatabase.getResultColCount();
			int targetColCount = targetDatabase.getResultColCount();
			if(sourceColCount!=targetColCount){
				Log.pl("Column counts do not match! : "+sourceColCount+" <-> "+targetColCount);
			}
			if(!sourceDatabase.haveResult()){
				Log.pl("Source query: no result"+sourceQuery);
				break;
			}
			if(!targetDatabase.haveResult()){
				Log.pl("Target query: no result"+targetQuery);
				break;
			}
			int count = 0;
			try{
				while(sourceRs.next() && targetRs.next() && discrepancyCount<max_discrepancies){
					List<String> sourceData = getRecordFromResultSet(sourceRs,sourceColCount);
					List<String> targetData = getRecordFromResultSet(targetRs,targetColCount);
					discrepancyCount += matches(sourceData,targetData,count);
					count++;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			Log.pl("Row Count: "+count);
			Log.pl("Discrepancy Count: "+discrepancyCount);
			pass = (discrepancyCount==0);
			if(pass){Log.pl("\nPASS!\n");}else{Log.pl("\nFAIL!\n");discrepancyCount=0;}				
		}
				
		
		
		sourceDatabase.close();
		targetDatabase.close();				
		Log.pl("Finished on "+new java.util.Date()+"!");
	}

	private static List<String> getRecordFromResultSet(ResultSet rs, int colCount) throws SQLException{
		List<String> rv = new ArrayList<String>();
		for(int i=0;i<colCount-1;i++){
			rv.add(rs.getString(i+1));
		}
		return rv;
	}
	
	private static int matches(List<String> a, List<String> b,int count){
		int rv = 0;
		List<String> cloneA = new ArrayList<String>(a);
		List<String> cloneB = new ArrayList<String>(b);
		cloneA.removeAll(b);
		cloneB.removeAll(a);
		
		if(!cloneA.isEmpty() || !cloneB.isEmpty()){
			Log.print(count+": "+cloneA.size()+":"+cloneB.size());
			Log.print("'"+a+"'");//.get(0)
			Log.print("  -- doesn't match -- ");
			Log.print("'"+b+"'");
			Log.cr();
			rv = 1;
		}else{
			if(false){
			Log.print(count+": "+cloneA.size()+":"+cloneB.size());
			Log.print("'"+a+"'");//.get(0)
			Log.print("  -- matches -- ");
			Log.print("'"+b+"'");
			Log.cr();
			}
		}
		return rv;
	}

	
	private static int compareSchemas(String schema,
			DatabaseManager sourceDatabase, DatabaseManager targetDatabase,
			List<String> sourceTables) {
		int discrepancyCount = 0;
		for(String table:sourceTables){
			Log.pl("Comparing both schemas for table: "+table);
			String srcTable = schema+"."+table;
			String destTable = schema+"."+table;
			
			List<ColumnDefinition> sourceCols = sourceDatabase.getColumnDefsFromDbMeta(srcTable);
			List<ColumnDefinition> targetCols = targetDatabase.getColumnDefsFromDbMeta(destTable);
			
			for(ColumnDefinition sourceCol:sourceCols){
			
				if(!targetCols.contains(sourceCol)){
					Log.pl("!!Column: "+sourceCol.getColName()+" in source didn't match a column in destination for table "+table);
					discrepancyCount++;
				}else{
					ColumnDefinition targetCol = targetCols.get(targetCols.indexOf(sourceCol));
					//Log.pl("Found :"+sourceCol.getColName()+ " matches "+targetCol.getColName());
				}
			}
		}
		return discrepancyCount;
	}
	
	//@SuppressWarnings("boxing")
	private static void test(Database source, Database target){
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
	
	private static void compareSchemasStandalone(Database source, Database target, String schema){
		Log.pl("Starting Compare of "+source.name()+" to "+target.name()+" with schema: "+schema+" on "+new java.util.Date());
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
private static int matchesPerLine(List<String> a, List<String> b){
	int rv = 0;
	List<String> cloneA = new ArrayList<String>(a);
	List<String> cloneB = new ArrayList<String>(b);
	cloneA.removeAll(b);
	cloneB.removeAll(a);
	
	if(!cloneA.isEmpty() || !cloneB.isEmpty()){
		Log.print(a.get(0));
		Log.print("  -- doesn't match -- ");
		Log.print(b.get(0));
		Log.cr();
		rv++;
	}
	return rv;
}
*/




/*
 * Report the differences between DMPRODOLD
 * 
 * I. CONNECTION TEST
 * 
 *   PASS - Can connect to <db1>
 *   PASS - Can connect to <db2>
 *   
 * II. TABLE COUNT
 * 
 *   FAIL - <dbx> missing tables:
 *   .......employer.request_log
 *   
 * III. TABLE STRUCTURE TEST - SKIPPED
 *   
 * IV. TABLE ROW COUNT TEST 
 * 
 *   FAIL - <dbx> Row count mismatch on these tables in target database:
 *   .......employer.company:         25 extra rows
 *   .......employer.benefit_range:  125 missing rows
 * 
 * V. TABLE DATA ANALYSIS                 (fields that do match in two queries, identifier fields used to match rows)
 *                 (any other logic used to fix identifies like removing padded zeros)
 *                 
 *   FAIL - <dbx> Table <t> has 503 rows that do not match. The following are the ids missing and extra.
 *   
 *   Missing:
 *        id:
 *        id:
 *   Extra  :
 *        id:
 *        id:
 *        
 *           
 * VI. TABLE
 *   
 *   (Detail level 2)
 *   .......Row:[duns:23432152] values in these fields do not match:
 *   
 *   ...........company_name:    'Blue Cross' <-x-> 'Blue Cross Blue Shield'
 *   ...........employee_count:           234 <-x-> 34225
 * 
 */
