package chp.dbreplicator;

public class ReplicationTest {

	public ReplicationTest() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	public static void main(String[] args){
	
		
		DatabaseManager localDatabase = new DatabaseManager(Database.LOCAL_CO);
		DatabaseManager remoteDatabase = new DatabaseManager(Database.SQLPROD);
		String[] tableList = {"benefit_decision_makers","blue_competitors","blue_plans","branch_carriers","branch_locations","branches","companies","company_carriers","data_updated","employee_counts","foreign_addresses","fortune_1000","parents","sales","sales_growth_3_year","sales_growth_5_year","start_years","stock_symbols","usa_addresses","web_sites"};//"usa_census","usa_state_governments","executive_personnel","locations",
		String sourceQual = "companies_";
		//String targetDatabase = "companies";
		MappingTable mapping = new MappingTable(sourceQual,tableList);
		ReplicationJob job = new ReplicationJob(remoteDatabase,localDatabase,mapping);
		
		job.exec();

	}
	
	
	/*
	 * use the in memory database as a cache
	 * the LocalDbStore is a facade that will fetch results from either memory or file behind the scenes.
	 * 
	 */
	
	
}
/* 
 * 0. clear the existing database if it exists
	 * 1. choose database and list of tables to replicate.  (ReplicatonJob{database,list<MappingTable>},ReplicationJob.Result)
 * 2. read judiciously from database of choice, each table (filters and column lists may be required)
 * 3. from the result set and the table, automatically create the table with the columns desired if it doesn't exist.
 * 4. insert the rows
 */

/*
 * Query services
 * 
 * count
 * actual
 * 
 */