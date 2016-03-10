package chp.dbreplicator;

import jhg.util.JG;

public enum Database {
	//"jdbc:hsqldb:file:
	LFB(Rdbms.HSQLDB,		"org.hsqldb.jdbc.JDBCDriver",					"jdbc:hsqldb:file:/hsqldat/company",													"foundation",	"f0vnd4t10n"),
	LMC(Rdbms.HSQLDB,		"org.hsqldb.jdbc.JDBCDriver",					"jdbc:hsqldb:mem:employer_search",											"foundation",	"f0vnd4t10n"),
	DW( Rdbms.SQLSERVER,	"com.microsoft.sqlserver.jdbc.SQLServerDriver",	"jdbc:sqlserver://sqlprod01.corp.chpinfo.com:1433;databaseName=IDSProd",	"app_etl",		"fact-H3d4x"),
	
	DM( Rdbms.POSTGRESQL,	"org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5444/DM_DEV",				"whs_viewer",	"whs_viewer"),
	
	DMF( Rdbms.POSTGRESQL,	"org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5432/foundation_data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",	            "jgagon",	    JG.WPASS()),
	DWF( Rdbms.SQLSERVER,	"com.microsoft.sqlserver.jdbc.SQLServerDriver",	"jdbc:sqlserver://chp-sqldev02.corp.chpinfo.com:1433;databaseName=Foundation_App_Logic;integratedSecurity=true",	"jgagon",		                                                            JG.WPASS()),
	
	
	DMFRW( Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5432/foundation_data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",	"foundation_data_management","naive-qS_uA"),

	DMFMR( Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5432/foundation_data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",	"app_market_reports",	    "4pp_m4rk3t"  ),
	DMFBM( Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5432/foundation_data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",	"app_benchmarking"   ,	    "tbd"  ),
	DMFES( Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5432/foundation_data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",	"app_employer_search",	    "tbd"  ),
	DMFNC( Rdbms.POSTGRESQL, "org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5432/foundation_data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",	"app_network_compare",	    "tbd"  )
	
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
}
//LFM(Rdbms.HSQLDB,		"org.hsqldb.jdbc.JDBCDriver",					"jdbc:hsqldb:file:security",												"foundation",	"f0vnd4t10n"),
//LME(Rdbms.HSQLDB,		"org.hsqldb.jdbc.JDBCDriver",					"jdbc:hsqldb:file:employer",												"foundation",	"f0vnd4t10n"),
