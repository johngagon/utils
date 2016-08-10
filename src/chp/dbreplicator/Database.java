package chp.dbreplicator;

//import jhg.util.JG;

public enum Database {
	//"jdbc:hsqldb:file:
	LFB(Rdbms.HSQLDB,		"org.hsqldb.jdbc.JDBCDriver",					"jdbc:hsqldb:file:/hsqldat/company",													"foundation",	"f0vnd4t10n"),
	LMC(Rdbms.HSQLDB,		"org.hsqldb.jdbc.JDBCDriver",					"jdbc:hsqldb:mem:employer_search",											"foundation",	"f0vnd4t10n"),
	
	DW(     Rdbms.SQLSERVER,	"com.microsoft.sqlserver.jdbc.SQLServerDriver",	"jdbc:sqlserver://sqlprod01.corp.chpinfo.com:1433;databaseName=IDSProd",	"app_transfer",		"4pp_+ransf3r"),
	DWP(    Rdbms.SQLSERVER,	"com.microsoft.sqlserver.jdbc.SQLServerDriver",	"jdbc:sqlserver://chp-sqldev02.corp.chpinfo.com:1433;databaseName=IDSPreProd",	"app_transfer",		"4pp_+ransf3r"),
	DWTEST( Rdbms.SQLSERVER,	"com.microsoft.sqlserver.jdbc.SQLServerDriver",	"jdbc:sqlserver://chp-sqldev02.corp.chpinfo.com:1433;databaseName=IDStest",	"app_transfer",		"4pp_+ransf3r"),
	DWDEV(  Rdbms.SQLSERVER,	"com.microsoft.sqlserver.jdbc.SQLServerDriver",	"jdbc:sqlserver://chp-sqldev02.corp.chpinfo.com:1433;databaseName=IDSDev",	"app_transfer",		"4pp_+ransf3r"),	
	
	DWF( Rdbms.SQLSERVER,	"com.microsoft.sqlserver.jdbc.SQLServerDriver",	"jdbc:sqlserver://chp-sqldev02.corp.chpinfo.com:1433;databaseName=Foundation_App_Logic;integratedSecurity=true",	"jgagon",		                                                            JG.WPASS()),

	DM(       Rdbms.POSTGRESQL,	"org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5444/DM_DEV",				"whs_viewer",	"whs_viewer"),
	DMDEVNEW( Rdbms.POSTGRESQL,	"org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5432/data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",				"postgres",	"p0stgres"),
	
	DMTESTOLD(Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbtest01.corp.chpinfo.com:5444/DM_TEST?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",				"whs_viewer",	"whs_viewer"),
	DMTESTNEW(Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbtest01.corp.chpinfo.com:5432/data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory&sslmode=allow",				"postgres",	"p0stgres"),
	
	DMPPRDOLD(Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbprp01.corp.chpinfo.com:5444/DM_PROD?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",				"whs_viewer",	"whs_viewer"),
	DMPPRDNEW(Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbprp01.corp.chpinfo.com:5432/data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",				"whs_viewer",	"whs_viewer"),
	
	DMPRODOLD(Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbprod04.corp.chpinfo.com:5444/DM_PROD?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",				"whs_viewer",	"whs_viewer"),
	DMPRODNEW(Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbprod04.corp.chpinfo.com:5432/data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory&sslmode=allow",				"postgres",	"p0stgres"),
	
	DMF( Rdbms.POSTGRESQL,	"org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5432/foundation_data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",	            "jgagon",	    JG.WPASS()),
	
	/*
	 * General purpose
	 */
	DMFDEV( Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5444/foundation_data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",	"foundation_data_management","naive-qS_uA"),
	DMFUAT( Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbtest01.corp.chpinfo.com:5444/foundation_data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",	"whs_viewer","whs_viewer"),
	DMFPRP( Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbprp01.corp.chpinfo.com:5444/foundation_data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",	"whs_viewer","whs_viewer"),
	DMFPRD( Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbprod04.corp.chpinfo.com:5444/foundation_data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",	"whs_viewer","whs_viewer"),
	
	NULL( Rdbms.POSTGRESQL, "",	"",	"", ""),
	
	DMCUST(Rdbms.POSTGRESQL, "org.postgresql.Driver",				        "jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5444/PG-439",				"whs_viewer",	"whs_viewer"),
	
	DMFMR( Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5444/foundation_data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",	"app_market_reports",	    "4pp_m4rk3t"  ),
	DMFBM( Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5444/foundation_data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",	"app_benchmarking"   ,	    "tbd"  ),
	DMFES( Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5444/foundation_data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",	"app_employer_search",	    "tbd"  ),
	DMFNC( Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5444/foundation_data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",	"app_network_compare",	    "tbd"  )

	
	//chp-dbprod04.corp.chpinfo.com:5444
	//chp-dbprod04.corp.chpinfo.com:5432
	
	;
	
	private String url;
	private String user;
	private String password;
	private String driver;
	private Rdbms rdbms;
	private Database(Rdbms _rdbms, String _driver, String _url, String _user, String _password){
		this.rdbms = _rdbms;
		this.driver = _driver;
		this.url = _url;
		this.user = _user;
		this.password = _password;
	}
	
	public Rdbms rdbms(){
		return this.rdbms;
	}	
	
	public String driver(){
		return this.driver;
	}
	public String user(){
		return this.user;
	}
	public String password(){
		return this.password;
	}
	
	public String url(){
		return this.url;
	}
	
	                                                                                                                                                                                                                                                                            private static class JG{public static String WPASS(){return "Shamrock72!";}}
}
//LFM(Rdbms.HSQLDB,		"org.hsqldb.jdbc.JDBCDriver",					"jdbc:hsqldb:file:security",												"foundation",	"f0vnd4t10n"),
//LME(Rdbms.HSQLDB,		"org.hsqldb.jdbc.JDBCDriver",					"jdbc:hsqldb:file:employer",												"foundation",	"f0vnd4t10n"),
