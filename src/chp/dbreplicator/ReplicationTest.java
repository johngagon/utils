package chp.dbreplicator;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import chp.dbreplicator.ProgressReporter.Marker;

@SuppressWarnings("unused")
public class ReplicationTest {

	private ReplicationTest() {
		
	}
	private static boolean ALLCLEAN = true;
	private static boolean CLEANDW = true;
	private static boolean CLEANDM = true;
	private static boolean CLEANES = true;
	

	private static void replicateDwToLocal(){
		Log.line("\nReplicating Datawarehouse");
		DatabaseManager dataWarehouseDb = new DatabaseManager(Database.DW);
		DatabaseManager localDatabase = new DatabaseManager(Database.LFB);	
		String[] tableList = {"companies_benefit_decision_makers",
				"companies_blue_competitors",
				"companies_blue_plans",
				"companies_branch_carriers",
				"companies_branch_locations",
				"companies_branches",
				"companies_companies",
				"companies_company_carriers",
				"companies_data_updated",
				"companies_employee_counts",
				"companies_foreign_addresses",
				"companies_fortune_1000",
				"companies_parents",
				"companies_sales",
				"companies_sales_growth_3_year",
				"companies_sales_growth_5_year",
				"companies_start_years",
				"companies_stock_symbols",
				"companies_usa_addresses",
				"companies_web_sites",
				"naics_national_industries",
				"naics_industries",
				"naics_groups",
				"naics_subsectors",
				"naics_sectors",
				"sic_industries",
				"sic_industry_groups",
				"sic_major_groups",
				"carriers_carriers",
				"carriers_types",
				"geography_building_blocks",
				"geography_block_groups",
				"geography_census_tracts",
				"geography_counties",
				"geography_states" };
		MappingTable mapping = new MappingTable(tableList);
		ReplicationJob job = new ReplicationJob(dataWarehouseDb,localDatabase,mapping);
		job.exec();
		Log.line("Replicated Datawarehouse");
	}
	
	private static void replicateDmToLocal(){
		Log.line("\nReplicating Datamart");
		DatabaseManager dataMartDb = new DatabaseManager(Database.DM);
		DatabaseManager localDatabase = new DatabaseManager(Database.LFB);
		String[] tableList = {"security.mq_user_ca_mapping","refer.carrier","refer.carrier_group","refer.carrier_type","refer.naics","refer.sic"};
		MappingTable mapping = new MappingTable(tableList);
		ReplicationJob job = new ReplicationJob(dataMartDb,localDatabase,mapping);
		job.exec();	
		Log.line("Replicated Datamart");
	}

	private static void materializeEmployer(){
		Log.line("\nMaterializing Employer Search.");
		Log.line("Starting at "+new java.util.Date());
		DatabaseManager localDatabase = new DatabaseManager(Database.LFB);
		localDatabase.connect();
		
		Log.line("\n1. Running query job on employer.");
		String employerSchema = "CREATE SCHEMA employer";

		
		Index companyCarriersIndex = new Index("company_carriers_index","COMPANY_CARRIERS","duns_number,carrier");
		Index companiesBranchIndex = new Index("companies_branch_carriers_index","COMPANIES_BRANCH_CARRIERS","branch_duns_number, carrier");
		Index sicIndex = new Index("sic_index","REFER.SIC","sic_industry_code");
		Index naicsIndex = new Index("naics_index","REFER.NAICS","naics,naics_industry_code");
		Index branchDunsIndex = new Index("branch_duns_index","COMPANIES_BRANCHES","duns_number");
		Index branchCarriersBranchIndex = new Index("companies_branch_carriers_branch_index","COMPANIES_BRANCH_CARRIERS","branch_duns_number");
		Index branchCarriersCarrierIndex = new Index("companies_branch_carriers_carrier_index","COMPANIES_BRANCH_CARRIERS","carrier");
		Index carrierCarriersTypeIndex = new Index("carriers_carriers_type_index","CARRIERS_CARRIERS","\"type\"");
		Index carrierTypeNameIndex = new Index("carriers_types_name_index","CARRIERS_TYPES","\"name\"");
		Index companiesPurchOptIndex = new Index("companies_purchase_opt_index","COMPANIES_COMPANIES","purchase_option");
		Index companiesDomesticUltIndex = new Index("companies_du_index","COMPANIES_COMPANIES","domestic_ultimate");
		Index carriersGroupIndex = new Index("carriers_group_index","CARRIERS_CARRIERS","\"group\"");
		Index[] indexes = {companyCarriersIndex,companiesBranchIndex,sicIndex,naicsIndex,branchDunsIndex,branchCarriersBranchIndex,branchCarriersCarrierIndex,carrierCarriersTypeIndex,carrierTypeNameIndex,companiesPurchOptIndex,companiesDomesticUltIndex,carriersGroupIndex};

		String employeeCountsTable =        "CREATE TABLE employer.employer_du_employee_counts (domestic_ultimate_duns varchar(10) primary key,carrier_group_code integer, employees integer)";
		String queryFinal =		
				//"explain plan for "+
				"SELECT lpad ( c.domestic_ultimate, 9 , '0' ) as domestic_ultimate_duns, "
				+"a.\"group\" as carrier_group_code, "
				+"sum(b.employees) as employees "
				+"from companies_companies c "
				+"join companies_branches b on b.duns_number = c.duns_number "
				+"join companies_branch_carriers r on r.branch_duns_number = b.branch_duns_number "
				+"join carriers_carriers a on a.id = r.carrier "
				+"join carriers_types t on t.id = a.type "
				+"where t.name in ('Consortium Member','Non-Consortium BCBS') "
				+"and b.employees > 0 "
				+"and c.purchase_option = 'Base' "
				+"group by c.domestic_ultimate, a.\"group\" "
				+"order by domestic_ultimate_duns ";
		String insertQuery = "INSERT INTO employer.employer_du_employee_counts (domestic_ultimate_duns,carrier_group_code, employees ) values (?,?,?)";		
		
		//localDatabase.listAllTables(false);
		Log.line("");
		//75817 current postgres count
		if(!localDatabase.doesSchemaExist("employer")){
			Log.line("Schema:"+employerSchema);
			localDatabase.execute(employerSchema);
		}
		if(!localDatabase.doesTableExist("employer_du_employee_counts")){
			Log.line("Table:"+employeeCountsTable);
			localDatabase.execute(employeeCountsTable);			
		}
		Log.line("");
		for(Index index:indexes){
			if(!localDatabase.doesIndexExist(index.getIndexName(),index.getTable())){
				Log.line("Index:"+index.getIndexName());
				localDatabase.execute(index.sql());
			}
		}
		Log.line("QUERY:"+queryFinal);
		localDatabase.query(queryFinal);
		ProgressReporter pr = new ProgressReporter(75817,Marker.TENTHS);
		//pr.addListener(new SimpleProgressListener());
		if(localDatabase.haveResult()){
			//ResultSet rs = localDatabase.getResult();
			localDatabase.printResult();
			try{
				/*
				Log.println("Inserting records.");
				while(rs.next()){
					List<Object> data = new ArrayList<Object>();
					data.add(rs.getString(1));
					data.add(rs.getInt(2));
					data.add(rs.getInt(3));
					localDatabase.executeUpdate(insertQuery, data);
					pr.completeWork();
				}
				Log.println("Finished inserting records.");
				*/
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		//create the one table

		localDatabase.close();
		Log.line("Ending at "+new java.util.Date());
		Log.line("Materialized Employer Search.");
	}
	
	private static void loadIntoCacheLocal(){
		Log.line("Caching Employer Search.");
		DatabaseManager employerSearchDatabase = new DatabaseManager(Database.LFB);
		//DatabaseManager datamartLocalDb = new DatabaseManager(Database.LFB);
		//String[] dmTables = {"security_mq_user_ca_mapping","refer_carrier","refer_carrier_group","refer_carrier_type","refer_naics","refer_sic"};
		DatabaseManager remoteDatabase = new DatabaseManager(Database.LMC);		
		Log.line("Cached Employer Search.");
	}
	
	
	public static void main(String[] args){
		Log.line("Starting all jobs.");
		/*
		if(ALLCLEAN || CLEANDW){
			replicateDwToLocal();	
		}
		if(ALLCLEAN || CLEANDM){
			replicateDmToLocal();	
		}
		*/
		if(ALLCLEAN || CLEANES){
			materializeEmployer();
		}
			
		/*
		loadIntoCacheLocal();
		*/
		//each job should give results of the tables created and in what area.
		Log.line("Finished jobs.");
	}
	
	private static void old(){
		DatabaseManager localDatabase = new DatabaseManager(Database.LFB);
		DatabaseManager remoteDatabase = new DatabaseManager(Database.DW);
		String[] tableList = {"benefit_decision_makers","blue_competitors","blue_plans","branch_carriers","branch_locations","branches","companies","company_carriers","data_updated","employee_counts","foreign_addresses","fortune_1000","parents","sales","sales_growth_3_year","sales_growth_5_year","start_years","stock_symbols","usa_addresses","web_sites"};//"usa_census","usa_state_governments","executive_personnel","locations",
		String sourceQual = "companies_";
		
		/*
		 * 
		 */
		
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
/*
private static boolean notExistLocalFileWarehouse(){
	Log.println("Checking if local datawarehouse exists.");
	boolean rv = true;
	DatabaseManager localDatabase = new DatabaseManager(Database.LFB);
	localDatabase.connect();
	if(localDatabase.isConnected()){
		String table = "companies_web_sites";
		rv = !localDatabase.doesTableExist(table);
	}
	localDatabase.close();
	return rv;
}
private static boolean notExistLocalFileDatamart(){
	Log.println("Checking if local datamart exists.");
	boolean rv = true;
	DatabaseManager localDatabase = new DatabaseManager(Database.LFB);	
	localDatabase.connect();
	String table = "security.mq_user_ca_mapping";
	rv = !localDatabase.doesTableExist(table);
	localDatabase.close();
	return rv;
}
private static boolean notExistLocalFileEmployerSearch(){
	Log.println("\nChecking if local employer_search exists.");
	boolean rv = true;
	DatabaseManager employerSearchDatabase = new DatabaseManager(Database.LFB);
	employerSearchDatabase.connect();
	String table = "employer_company";
	rv = !employerSearchDatabase.doesTableExist(table);
	employerSearchDatabase.close();
	return rv;
}
*/
//ReplicationJob job = new ReplicationJob(localDatabase,employeeCountsTableQuery);
//job.execCreateFromQueryJob();
/*
 * reference the hsql tables.  
 */
/*
		String employerSchema = "CREATE SCHEMA employer";
		String employeeCountsTable = "CREATE CACHED TABLE "
				+"employer.employer_du_employee_counts "
				+"(domestic_ultimate_duns, carrier_group_code, employees) AS ( "
				+"select lpad ( c.domestic_ultimate, 9 , '0' ) as domestic_ultimate_duns, "
				+"a.\"group\" as carrier_group_code, "
				+"sum(b.employees) as employees "
				+"from companies_companies c "
				+"join companies_branches b on b.duns_number = c.duns_number "
				+"join companies_branch_carriers r on r.branch_duns_number = b.branch_duns_number "
				+"join carriers_carriers a on a.id = r.carrier "
				+"join carriers_types t on t.id = a.type "
				+"where t.name in ('Consortium Member','Non-Consortium BCBS') "
				+"and b.employees > 0 "
				+"and c.purchase_option = 'Base' "
				+"group by c.domestic_ultimate, a.\"group\" "
				+"order by domestic_ultimate_duns "
				+") WITH DATA";
		Log.println("QUERY:"+employeeCountsTable);
*/


/*
String employeePreCountsTable =		"CREATE TABLE employer.employer_du_counts(domestic_ultimate varchar(10),\"group\" varchar(10),employees integer,carrier_type varchar(255),purchase_option varchar(255))";
String employeePreCountsQuery =     "SELECT c.domestic_ultimate,a.\"group\",b.employees,t.name as carrier_type,c.purchase_option,"
		+"from companies_companies c "
		+"join companies_branches b on b.duns_number = c.duns_number "
		+"join companies_branch_carriers r on r.branch_duns_number = b.branch_duns_number "
		+"join carriers_carriers a on a.id = r.carrier "
		+"join carriers_types t on t.id = a.type ";
*/