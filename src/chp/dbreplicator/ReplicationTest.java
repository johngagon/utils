package chp.dbreplicator;

import java.sql.ResultSet;
import java.util.*;

import chp.dbreplicator.ProgressReporter.Marker;

@SuppressWarnings("unused")
public class ReplicationTest {

	private static final String EMPLOYER = "employer";

	private ReplicationTest() {
		
	}
	private static boolean ALLCLEAN = true;
	private static boolean CLEANDW = true;
	private static boolean CLEANDM = true;
	private static boolean CLEANES = true;
	
	private static void replicateDwEsToDmEs(){
		//FIXME - relies on Abdul's work
	}
	
	private static void replicateDwToLocal(){
		Log.pl("\nReplicating Datawarehouse");
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
		Log.pl("Replicated Datawarehouse");
	}
	
	private static void replicateDmToLocal(){
		Log.pl("\nReplicating Datamart");
		DatabaseManager dataMartDb = new DatabaseManager(Database.DM);
		DatabaseManager localDatabase = new DatabaseManager(Database.LFB);
		String[] tableList = {"security.mq_user_ca_mapping","refer.carrier","refer.carrier_group","refer.carrier_type","refer.naics","refer.sic"};
		//employer.benefit_range,employer.employee_count,employer.fortune_rank
		MappingTable mapping = new MappingTable(tableList);
		ReplicationJob job = new ReplicationJob(dataMartDb,localDatabase,mapping);
		job.exec();	
		Log.pl("Replicated Datamart");
	}
	
	private static final String COMPANY_LEVELS = "employer.company_levels";
	private static final String COMPANY = "employer.company";
	private static final String DU_COMPANY_COUNTS = "employer.du_employee_counts";
	private static final String COMPANY_SIC = "employer.company_sic";
	private static final String COMPANY_BRANCH = "employer.company_branch";
	
	
	
	private static void materializeEmployer(){
		Log.pl("\nMaterializing Employer Search.");
		Log.pl("Starting at "+new java.util.Date());
		DatabaseManager localDatabase = new DatabaseManager(Database.LFB);
		localDatabase.connect();
		
		Log.pl("\n1. Running query job on employer.");
		String employerSchema = "CREATE SCHEMA employer";
		
		Index companiesParentsParentIndex = new Index("companies_parents_parent_index",        "COMPANIES_PARENTS","parents");
		Index companiesEmpCtTotalIndex = new Index("companies_employee_counts_total_index",    "COMPANIES_EMPLOYEE_COUNTS","employees_total");
		Index companiesFortuneRankIndex = new Index("companies_fortune_1000_rank_index",       "COMPANIES_FORTUNE_1000","rank");
		Index naicsNatlindustryIndustryIndex = new Index("naics_natl_industry_industry_index", "NAICS_NATIONAL_INDUSTRIES","industry");
		Index naicsIndustryGroupIndex = new Index("naics_industries_group_index",              "NAICS_INDUSTRIES","group");
		Index naicsGroupsSubSectorIndex = new Index("naics_groups_subsector_index",            "NAICS_GROUPS","subsector");
		Index naicsSubsectorsSectorIndex = new Index("naics_subsectors_sector_index",          "NAICS_SUBSECTORS","sector");
		Index sicMajorGroupsDivisionIndex = new Index("sic_major_groups_division_index",       "SIC_MAJOR_GROUPS","division");
		Index companiesUsaAddressesStateIndex = new Index("companies_usa_adds_state_index",    "COMPANIES_USA_ADDRESSES","state");
		//Index xxxxxxxx01 = new Index("xxxxxx","yyyyyyy","zzzzzzz");

		Index carrierCarriersTypeIndex = new Index("carriers_carriers_type_index",             "CARRIERS_CARRIERS","type");
		Index carriersGroupIndex = new Index("carriers_group_index",                           "CARRIERS_CARRIERS","group");
		Index carrierTypeNameIndex = new Index("carriers_types_name_index",                    "CARRIERS_TYPES","name");
		Index branchDunsIndex = new Index("branch_duns_index",                                 "COMPANIES_BRANCHES","duns_number");
		Index companiesBranchIndex = new Index("companies_branch_carriers_index",              "COMPANIES_BRANCH_CARRIERS","branch_duns_number, carrier");
		Index branchCarriersBranchIndex = new Index("companies_branch_carriers_branch_index",  "COMPANIES_BRANCH_CARRIERS","branch_duns_number");		
		Index branchCarriersCarrierIndex = new Index("companies_branch_carriers_carrier_index","COMPANIES_BRANCH_CARRIERS","carrier");
		Index branchLocBbIndex = new Index("branch_locations_bb_index",                        "COMPANIES_BRANCH_LOCATIONS","building_block");
		Index companyCarriersIndex = new Index("company_carriers_index",                       "COMPANIES_COMPANY_CARRIERS","duns_number,carrier");
		Index companyCarriersIndex2 = new Index("company_carriers2_index",                     "COMPANIES_COMPANY_CARRIERS","carrier");
		Index companiesDomesticUltIndex = new Index("companies_du_index",                      "COMPANIES_COMPANIES","domestic_ultimate");
		Index companiesNaicsIndex = new Index("companies_companies_naics_index",               "COMPANIES_COMPANIES","naics_national_industry");
		Index companiesPurchOptIndex = new Index("companies_purchase_opt_index",               "COMPANIES_COMPANIES","purchase_option");
		Index companiesSicIndex = new Index("companies_companies_sic_index",                   "COMPANIES_COMPANIES","sic_industry");
		Index geoBgTractIndex = new Index("geo_bg_tract_index",                                "GEOGRAPHY_BLOCK_GROUPS","census_tract");
		Index geoBbsGrpIndex = new Index("geo_bbs_grp_index",                                  "GEOGRAPHY_BUILDING_BLOCKS","block_group");
		Index geoTractsCountyIndex = new Index("geo_tracts_county_index",                      "GEOGRAPHY_CENSUS_TRACTS","county");
		Index geoCountiesStateIndex = new Index("geo_counties_state_index",                    "GEOGRAPHY_COUNTIES","state");
		Index naicsIndex = new Index("naics_index",                                            "REFER.NAICS","naics,naics_industry_code");
		Index sicIndex = new Index("sic_index",                                                "REFER.SIC","sic_industry_code");
		Index sicIndGrpIndex = new Index("sic_industries_group_index",                         "SIC_INDUSTRIES","industry_group");
		Index sicMajorGrpIndex = new Index("sic_industry_groups_majorgrp_index",               "SIC_INDUSTRY_GROUPS","major_group");
		
		
		
		Index[] indexes = {companyCarriersIndex,companiesBranchIndex,sicIndex,naicsIndex,branchDunsIndex,branchCarriersBranchIndex,branchCarriersCarrierIndex,carrierCarriersTypeIndex,carrierTypeNameIndex,companiesPurchOptIndex,companiesDomesticUltIndex,carriersGroupIndex,companiesSicIndex,sicIndGrpIndex,sicMajorGrpIndex,branchLocBbIndex,geoBbsGrpIndex,geoBgTractIndex,geoTractsCountyIndex,geoCountiesStateIndex,companiesParentsParentIndex,companiesEmpCtTotalIndex,companiesFortuneRankIndex,naicsNatlindustryIndustryIndex,naicsIndustryGroupIndex,naicsGroupsSubSectorIndex,naicsSubsectorsSectorIndex,sicMajorGroupsDivisionIndex,companiesUsaAddressesStateIndex};

		String createCompanyLevelsTable = "CREATE TABLE "+COMPANY_LEVELS+" (duns_number varchar(10), depth integer, global_ultimate varchar(10))";
		String queryCompanyLevels = getCompanyLevelsQuery();
		String insertCompanyLevels = "INSERT INTO "+COMPANY_LEVELS+" (duns_number,depth,global_ultimatee) values (?,?,?)";
		
		
		String createCompanyTable = "CREATE TABLE "+COMPANY+" (duns_number varchar(10) , decision_maker_code varchar(1), company_name varchar(500), company_country_name varchar(4), company_address_1 varchar(255), company_city varchar(255), company_state varchar(3), company_zip varchar(10), global_ultimate_duns varchar(10), domestic_ultimate_duns varchar(10), parent_duns varchar(10), "+"company_level integer, fortune_1000_rank integer, year_started integer, parent_name varchar(500),	domestic_ultimate_name varchar(500), primary_stock_symbol varchar(15), company_website varchar(500), commercial_credit_score varchar(100), paydex_score varchar(100), financial_stress_score varchar(100), carrier_code integer, carrier_name varchar(100), carrier_type_name varchar(50), carrier_type_code integer, naics_national_name varchar(255), "	+"naics_national_code integer, naics_industry_name varchar(255), naics_industry_code integer, naics_group_name varchar(255), naics_group_code integer, naics_subsector_name varchar(255), naics_subsector_code integer, naics_sector_name varchar(255), naics_sector_code integer, sic_industry_name varchar(255), sic_industry_code varchar(10), sic_industry_group_name varchar(255), sic_industry_group_code varchar(10), sic_major_group_code varchar(10), "	+"sic_major_group_name varchar(255), sic_division_name varchar(255), sic_division_code varchar(255), state_name varchar(50), state_fips varchar(5), data_as_of date, adj_dom_employees_here integer, adj_dom_employees_rollup integer, adj_dom_employees_total integer, company_sales bigint, company_pct_gr_sales_3_yr integer, company_pct_gr_sales_5_yr integer, domestic_ultimate_carrier_name varchar(100), domestic_ultimate_carrier_code varchar(10), "	+"employee_band integer, fortune_band integer, du_chp_member varchar(2), stakeholder_code varchar(25))";
		String queryCompany = getCompanyQuery();
		String insertCompany = "INSERT INTO "+COMPANY+" (duns_number,decision_maker_code,company_name,company_country_name,company_address_1,company_city,company_state,company_zip,global_ultimate_duns,domestic_ultimate_duns,parent_duns,company_level,fortune_1000_rank,year_started,parent_name,domestic_ultimate_name,primary_stock_symbol,company_website,commercial_credit_score,paydex_score,financial_stress_score,carrier_code,carrier_name,carrier_type_name,carrier_type_code,naics_national_name,naics_national_code,naics_industry_name,naics_industry_code,naics_group_name,naics_group_code,naics_subsector_name,naics_subsector_code,naics_sector_name,naics_sector_code,sic_industry_name,sic_industry_code,sic_industry_group_name,sic_industry_group_code,sic_major_group_code,sic_major_group_name,sic_division_name,sic_division_code,state_name,state_fips,data_as_of,adj_dom_employees_here,adj_dom_employees_rollup,adj_dom_employees_total,company_sales,company_pct_gr_sales_3_yr,company_pct_gr_sales_5_yr,domestic_ultimate_carrier_name,domestic_ultimate_carrier_code,employee_band,fortune_band,du_chp_member,stakeholder_code) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		
		String createEmployeeCountsTable = "CREATE TABLE "+DU_COMPANY_COUNTS+" (domestic_ultimate_duns varchar(10), carrier_group_code integer, employees integer)";
		String queryEmployeeCounts = getEmployeeCounts();						//"explain plan for "+
		String insertEmployeeCounts = "INSERT INTO "+DU_COMPANY_COUNTS+" (domestic_ultimate_duns,carrier_group_code, employees ) values (?,?,?)";		
		
		String createCompanySicTable = "CREATE TABLE "+COMPANY_SIC+" (duns_number varchar(10) primary key,sic_industry_code varchar(10), sic_industry_group_code varchar(10), sic_major_group_code varchar(10) )";
		String queryCompanySic = getCompanySic();
		String insertCompanySic = "INSERT INTO "+COMPANY_SIC+" (duns_number,sic_industry_code,sic_industry_group_code,sic_major_group_code) values (?,?,?,?) ";

		String createCompanyBranchTable = "CREATE TABLE "+COMPANY_BRANCH+" (branch_duns varchar(10), duns_number varchar(10), company_name varchar(255), state_abbreviation varchar(2), carrier_code integer, branch_employee_rollup_ch integer)";
		String queryCompanyBranch = getCompanyBranch();
		String insertCompanyBranch = "INSERT INTO "+COMPANY_BRANCH+" (branch_duns, duns_number, company_name, state_abbreviation, carrier_code, branch_employee_rollup_ch) values (?,?,?,?,?,?)";
		
		//FIXME: create indexes on all these new tables when done.
		
		//localDatabase.listAllTables(false);
		Log.cr();
		//75817 current postgres count
		if(!localDatabase.doesSchemaExist(EMPLOYER)){
			Log.pl("Creating Schema:"+employerSchema);
			localDatabase.execute(employerSchema);
		}

		Log.pl("Creating Table:"+COMPANY_LEVELS);
		if(localDatabase.doesTableExist(COMPANY_LEVELS)){
			localDatabase.dropTable(COMPANY_LEVELS);
		}			
		Log.pl("Creating Table:"+COMPANY);
		if(localDatabase.doesTableExist(COMPANY)){
			localDatabase.dropTable(COMPANY);
		}		
		Log.pl("Creating Table:"+DU_COMPANY_COUNTS);
		if(localDatabase.doesTableExist(DU_COMPANY_COUNTS)){
			localDatabase.dropTable(DU_COMPANY_COUNTS);
		}
		localDatabase.execute(createEmployeeCountsTable);

		Log.pl("Creating Table:"+COMPANY_SIC);
		if(localDatabase.doesTableExist(COMPANY_SIC)){
			localDatabase.dropTable(COMPANY_SIC);
		}
		localDatabase.execute(createCompanySicTable);
		
		Log.pl("Creating Table:"+COMPANY_BRANCH);
		if(localDatabase.doesTableExist(COMPANY_BRANCH)){
			localDatabase.dropTable(COMPANY_BRANCH);
		}
		localDatabase.execute(createCompanyBranchTable);
		

		localDatabase.execute(createCompanyTable);
		String sicDivisionsTable = "sic_divisions";
		if(localDatabase.doesTableExist(sicDivisionsTable)){
			localDatabase.dropTable(sicDivisionsTable);
		}
		String[] sicDivisionFields = {"code","name"};
		String[] sicDevisionFieldDef = {"varchar(1)","varchar(100)"};
		String[] d01 = {"A","Agricultural, Forestry, And Fishing"};
		String[] d02 = {"B","Mining"};
		String[] d03 = {"C","Construction"};
		String[] d04 = {"D","Manufacturing"};
		String[] d05 = {"E","Transportation, Communications, Electric, Gas, And Sanitary Services"};
		String[] d06 = {"F","Wholesale Trade"};
		String[] d07 = {"G","Retail Trade"};
		String[] d08 = {"H","Finance, Insurance, And Real Estate"};
		String[] d09 = {"I","Services"};
		String[] d10 = {"J","Public Administration"};
		String[] d11 = {"K","Nonclassifiable Establishments"};
		List<String[]> data = new ArrayList<String[]>();
		data.add(d01);data.add(d02);data.add(d03);data.add(d04);data.add(d05);data.add(d06);data.add(d07);data.add(d08);data.add(d09);data.add(d10);data.add(d11);
		localDatabase.createReferenceTable(sicDivisionsTable, sicDivisionFields, sicDevisionFieldDef, data);
		
	
		
		for(Index index:indexes){
			if(!localDatabase.doesIndexExist(index.getIndexName(),index.getTable())){
				Log.pl("Creating Index:"+index.getIndexName()+":"+index.sql());
				localDatabase.execute(index.sql());
			}
		}
		
		Log.cr(2);
		//if(!localDatabase.tableHasRecords(COMPANY_LEVELS,279000)){
			Log.pl("QUERY:"+queryCompanyLevels);
			localDatabase.query(queryCompanyLevels);
			ProgressReporter pr = new ProgressReporter(279936,Marker.TENTHS);//!!!! FIXME guess 279 936
			localDatabase.insertFromResult(3, insertCompanyLevels, null);
		//}	
		/*
		//if(!localDatabase.tableHasRecords(COMPANY,279000)){
			Log.pl("QUERY:"+queryCompany);
			localDatabase.query(queryCompany);
			ProgressReporter pr = new ProgressReporter(279936,Marker.TENTHS);
			localDatabase.insertFromResult(58, insertCompany, pr);
		//}	
		
		//if(!localDatabase.tableHasRecords(DU_COMPANY_COUNTS,75000)){
			Log.pl("QUERY:"+queryEmployeeCounts);
			localDatabase.query(queryEmployeeCounts);
			ProgressReporter pr = new ProgressReporter(75817,Marker.TENTHS);
			localDatabase.insertFromResult(3, insertEmployeeCounts, pr);
		//}
		
		//if(!localDatabase.tableHasRecords(COMPANY_SIC,142000)){
			Log.pl("QUERY:"+queryCompanySic);
			localDatabase.query(queryCompanySic);
			ProgressReporter pr = new ProgressReporter(142782,Marker.TENTHS);
			localDatabase.insertFromResult(4, insertCompanySic, pr);
		//}
		
		//if(!localDatabase.tableHasRecords(COMPANY_BRANCH,1535000)){
			Log.pl("QUERY:"+queryCompanyBranch);
			localDatabase.query(queryCompanyBranch);
			ProgressReporter pr = new ProgressReporter(1535094,Marker.TENTHS);
			localDatabase.insertFromResult(6, insertCompanyBranch, pr);
		//}	
		*/
		

		//create indexes on the new tables
		//look at increasing the cache size and making cached tables or using a memory copy or primer queries.
		
		localDatabase.close();
		Log.pl("Ending at "+new java.util.Date());
		Log.pl("Materialized Employer Search.");
		Log.flushErrors();
		
	}
	
	private static void cleanIfTableNotEmpty(String tableName){
		
	}
	
	private static void loadIntoCacheLocal(){
		Log.pl("Caching Employer Search.");
		DatabaseManager employerSearchDatabase = new DatabaseManager(Database.LFB);
		//DatabaseManager datamartLocalDb = new DatabaseManager(Database.LFB);
		//String[] dmTables = {"security_mq_user_ca_mapping","refer_carrier","refer_carrier_group","refer_carrier_type","refer_naics","refer_sic"};
		DatabaseManager remoteDatabase = new DatabaseManager(Database.LMC);		
		Log.pl("Cached Employer Search.");
	}
	private static void testQuery(){
		DatabaseManager localDatabase = new DatabaseManager(Database.LFB);
		localDatabase.connect();
		String query = "SELECT c.duns_number, "
				+"group_concat(a.name order by a.name separator ' or ') as domestic_ultimate_carrier_name, "
				+"group_concat(a.id order by a.id separator ',') as domestic_ultimate_carrier_code, "
				+"casewhen(t.name = 'Consortium Member','Y','N') as du_chp_member "
				+"from companies_company_carriers c "
				+"JOIN carriers_carriers a ON a.id = c.carrier "
				+"JOIN carriers_types t ON t.id = a.type "
				+"GROUP BY c.duns_number, du_chp_member";
		localDatabase.query(query);
		if(localDatabase.haveResult()){
			localDatabase.printResult();
		}
		localDatabase.close();		
	}
	
	public static void main(String[] args){
		Log.pl("Starting all jobs.");
		
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
		Log.pl("Finished jobs.");
	}
	private static String getCompanyQuery(){
		String rv = 
				/*" WITH RECURSIVE company_levels (duns_number,depth,global_ultimate) as ( \n"
						+"        SELECT c.duns_number, 1 AS depth, c.duns_number AS global_ultimate "
						+"        FROM COMPANIES_COMPANIES c "
						+"        WHERE NOT (EXISTS ( SELECT 1 "
						+"                            FROM COMPANIES_PARENTS p "
						+"                            WHERE p.duns_number = c.duns_number)) "
						+"        UNION ALL  "
						+"        SELECT p.duns_number, a.depth + 1, a.global_ultimate "
						+"        FROM COMPANIES_PARENTS p "
						+"        JOIN company_levels a ON a.duns_number = p.parent "
						+"        ) \n"
						+
						*/
						"SELECT lpad ( c.duns_number, 9 , '0' ) as duns_number,  "
						+"	      casewhen(b.duns_number is not null,'D','U') AS decision_maker_code, "
						+"        c.name AS company_name,  "
						+"        c.country AS company_country_name,  "
						+"        a.street_address AS company_address_1,  "
						+"        a.city AS company_city,  "
						+"        a.state_abbreviation AS company_state,  "
						+"        a.postal_code AS company_zip,  "
						+"        lpad ( v.global_ultimate,   9, '0' )AS global_ultimate_duns,  "
						+"        lpad ( c.domestic_ultimate, 9, '0') AS domestic_ultimate_duns,  "
						+"        lpad ( p.parent,            9, '0') AS parent_duns,  "
						+"        v.depth as company_level,  "
						+"        f.rank as fortune_1000_rank,  "
						+"        y.year_started,  "
						+"        pc.name as parent_name,  "
						+"        uc.name as domestic_ultimate_name,  "
						+"        m.stock_symbol as primary_stock_symbol,  "
						+"        w.web_site as company_website,  "
						+"        cast(null as varchar(100)) as commercial_credit_score,  "
						+"        cast(null as varchar(100)) as paydex_score,  "
						+"        cast(null as varchar(100)) financial_stress_score,  "
						+"        s.id as carrier_code,  "
						+"        s.name as carrier_name,  "
						+"        t.name as carrier_type_name,  "
						+"        t.id as carrier_type_code,  "
						+"        nn.name as naics_national_name,  "
						+"        lpad( nn.id, 6, '0') as naics_national_code,  "
						+"        ni.name as naics_industry_name,  "
						+"        ni.id as naics_industry_code,  "
						+"        ng.name as naics_group_name,  "
						+"        ng.id as naics_group_code,  "
						+"        nb.name as naics_subsector_name,  "
						+"        nb.id as naics_subsector_code,  "
						+"        ns.name as naics_sector_name,  "
						+"        cast(ns.id as varchar(10)) as naics_sector_code,  "
						+"        si.name as sic_industry_name,  "
						+"        lpad( si.id, 4, '0') as sic_industry_code,  "
						+"        sg.name AS sic_industry_group_name,  "
						+"        lpad(sg.id, 3, '0') as sic_industry_group_code,  "
						+"        lpad(sm.id, 2, '0') as sic_major_group_code,  "
						+"        sm.name as sic_major_group_name,  "
						+"        d.name as sic_division_name,  "
						+"        d.code as sic_division_code,  "
						+"        a.state_name,  "
						+"        lpad(a.state_id, 2, '0') as state_fips,  "
						+"        u.data_updated as data_as_of,  "
						+"        e.employees_here as adj_dom_employees_here,  "
						+"        e.employees_rollup as adj_dom_employees_rollup,  "
						+"        e.employees_total as adj_dom_employees_total,  "
						+"        l.sales as company_sales,  "
						+"        \"3\".growth as company_pct_gr_sales_3_yr,  "
						+"        \"5\".growth as company_pct_gr_sales_5_yr,  "
						+"        r.domestic_ultimate_carrier_name,  "
						+"        r.domestic_ultimate_carrier_code,  "
						+"        CASE "
						+"          WHEN e.employees_total <= 999 THEN 1 "
						+"          WHEN e.employees_total >= 1000 AND e.employees_total <= 2499 THEN 2 "
						+"          WHEN e.employees_total >= 2500 AND e.employees_total <= 4999 THEN 3 "
						+"          WHEN e.employees_total >= 5000 AND e.employees_total <= 9999 THEN 4 "
						+"          ELSE 5 "
						+"        END AS employee_band,  "
						+"        CASE "
						+"          WHEN f.rank IS NULL THEN 0 "
						+"          WHEN f.rank <= 100 THEN 1 "
						+"          WHEN f.rank >= 101 AND f.rank <= 500 THEN 2 "
						+"          ELSE 3 "
						+"        END AS fortune_band,  "
						+"        r.du_chp_member,  "
						+"        'DNB-' || lpad(c.duns_number, 9, '0') AS stakeholder_code \n"
						+" FROM companies_companies c \n"
						+" JOIN naics_national_industries nn ON nn.id = c.naics_national_industry "
						+" JOIN naics_industries ni ON ni.id = nn.industry "
						+" JOIN naics_groups ng ON ng.id = ni.\"group\" "
						+" JOIN naics_subsectors nb ON nb.id = ng.subsector "
						+" JOIN naics_sectors ns ON ns.id = nb.sector "
						+" JOIN sic_industries si ON si.id = c.sic_industry "
						+" JOIN sic_industry_groups sg ON sg.id = si.industry_group "
						+" JOIN sic_major_groups sm ON sm.id = sg.major_group "
						+" JOIN sic_divisions d ON d.code = sm.division "
						+" JOIN companies_companies uc ON uc.duns_number = c.domestic_ultimate "
						+" JOIN company_levels v ON v.duns_number = c.duns_number "
						+" LEFT JOIN (         "
						+"  SELECT a.duns_number, a.street_address, a.city, s.abbreviation AS state_abbreviation, s.name AS state_name, s.id AS state_id,  "
						+" 	         replace(cast(a.postal_code as varchar(20)), '-', '') AS postal_code "
						+"  FROM companies_usa_addresses a "
						+"  JOIN geography_states s ON s.id = a.state "
						+"  UNION ALL  "
						+"  SELECT fa.duns_number, fa.street_address, fa.city, fa.state, NULL, NULL, fa.postal_code "
						+"  FROM companies_foreign_addresses fa "
						+"  ) a ON a.duns_number = c.duns_number "
						+" LEFT JOIN companies_benefit_decision_makers b ON b.duns_number = c.duns_number "
						+" LEFT JOIN companies_parents p ON p.duns_number = c.duns_number "
						+" LEFT JOIN companies_companies pc ON pc.duns_number = p.parent "
						+" LEFT JOIN companies_fortune_1000 f ON f.duns_number = c.duns_number "
						+" LEFT JOIN companies_start_years y ON y.duns_number = c.duns_number "
						+" LEFT JOIN companies_stock_symbols m ON m.duns_number = c.duns_number "
						+" LEFT JOIN companies_web_sites w ON w.duns_number = c.duns_number "
						+" LEFT JOIN companies_sales l ON l.duns_number = c.duns_number "
						+" LEFT JOIN companies_sales_growth_3_year \"3\" ON \"3\".duns_number = c.duns_number "
						+" LEFT JOIN companies_sales_growth_5_year \"5\" ON \"5\".duns_number = c.duns_number "
						+" LEFT JOIN companies_employee_counts e ON e.duns_number = c.duns_number "
						+" LEFT JOIN companies_company_carriers j ON j.duns_number = c.duns_number "
						+" LEFT JOIN carriers_carriers s ON s.id = j.carrier "
						+" LEFT JOIN carriers_types t ON t.id = s.type "
						+" LEFT JOIN (  "
						+"     SELECT c.duns_number, "
						+"     group_concat(a.name order by a.name separator ' or ') as domestic_ultimate_carrier_name, "
						+"     group_concat(a.id order by a.id separator ',') as domestic_ultimate_carrier_code, "
						+"     casewhen(t.name = 'Consortium Member','Y','N') as du_chp_member "
						+"     from companies_company_carriers c "
						+"     JOIN carriers_carriers a ON a.id = c.carrier "
						+"     JOIN carriers_types t ON t.id = a.type "
						+"     GROUP BY c.duns_number, du_chp_member "
						+"  ) r ON r.duns_number = c.domestic_ultimate \n"
						+" CROSS JOIN companies_data_updated u "
						+"   WHERE NOT (EXISTS ( SELECT 1 "
						+"                       FROM companies_blue_plans x "
						+"                       WHERE x.duns_number = c.duns_number)) "
						+"     AND NOT (EXISTS ( SELECT 1 "
						+"                       FROM companies_blue_competitors x "
						+"                       WHERE x.duns_number = c.duns_number)) "
						+"\n   AND (    c.purchase_option =  'Base' or c.purchase_option =  'Foreign'    )";				
		return rv;
		
	}
	private static String getEmployeeCounts(){
		return "SELECT lpad ( c.domestic_ultimate, 9 , '0' ) as domestic_ultimate_duns, "
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
	}
	
	private static String getCompanySic(){
		return "SELECT lpad ( c.duns_number, 9 , '0' ) as duns_number, "
				+"i.id AS sic_industry_code, "
				+"g.id AS sic_industry_group_code, "
				+"m.id AS sic_major_group_code "
				+"from companies_companies c "
				+"join sic_industries i on i.id = c.sic_industry "
				+"join sic_industry_groups g on g.id = i.industry_group "
				+"join sic_major_groups m ON m.id = g.major_group "
				+"where not (exists ( select 1 from companies_blue_plans x "
				+"  where x.duns_number = c.duns_number)) "
				+"and not   (exists ( select 1 from companies_blue_competitors x "
				+"  where x.duns_number = c.duns_number)) "
				+"and c.purchase_option = 'Base' "
				+"order by duns_number ";
	}
	private static String getCompanyBranch(){
		return "SELECT lpad ( b.branch_duns_number, 9 , '0' ) as branch_duns, "
				+"lpad ( b.duns_number, 9 , '0' ) as duns_number, "
				+"c.name AS company_name, "
				+"s.abbreviation AS state_abbreviation, "
				+"r.carrier AS carrier_code, "
				+"b.employees AS branch_employee_rollup_ch "
				+"from companies_branches b "
				+"join companies_companies c on c.duns_number = b.duns_number "
				+"join companies_branch_carriers r on r.branch_duns_number = b.branch_duns_number "
				+"join companies_branch_locations l on l.branch_duns_number = b.branch_duns_number "
				+"join geography_building_blocks o on o.id = l.building_block "
				+"join geography_block_groups g on g.id = o.block_group "
				+"join geography_census_tracts t on t.id = g.census_tract "
				+"join geography_counties u on u.id = t.county "
				+"join geography_states s on s.id = u.state "
				+"  where not (exists ( select 1 from companies_blue_plans x "
				+"    where x.duns_number = c.duns_number))  "
				+"  and not (exists ( select 1 from companies_blue_competitors x   "
				+"    where x.duns_number = c.duns_number))  "
				+"  and c.purchase_option = 'Base' "
				+"  order by branch_duns ";			
	}
	private static String getCompanyLevelsQuery(){
		return "  WITH RECURSIVE company_levels_t (duns_number,depth,global_ultimate) as ( "
	    +"         SELECT c.duns_number, 1 AS depth, c.duns_number AS global_ultimate "
		+"         FROM COMPANIES_COMPANIES c "
		+"         WHERE NOT (EXISTS ( SELECT 1 "
		+"                            FROM COMPANIES_PARENTS p "
		+"                            WHERE p.duns_number = c.duns_number)) "
		+"         UNION ALL  "
		+"         SELECT p.duns_number, a.depth + 1, a.global_ultimate "
		+"         FROM COMPANIES_PARENTS p "
		+"         JOIN company_levels_t a ON a.duns_number = p.parent "
		+") select duns_number,depth,global_ultimate from company_levels_t \n";		
	}
	
}



