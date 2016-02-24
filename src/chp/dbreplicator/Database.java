package chp.dbreplicator;

public enum Database {
	//"jdbc:hsqldb:file:
	LFB(Rdbms.HSQLDB,		"org.hsqldb.jdbc.JDBCDriver",					"jdbc:hsqldb:file:/hsqldat/company",													"foundation",	"f0vnd4t10n"),
	LMC(Rdbms.HSQLDB,		"org.hsqldb.jdbc.JDBCDriver",					"jdbc:hsqldb:mem:employer_search",											"foundation",	"f0vnd4t10n"),
	DW( Rdbms.SQLSERVER,	"com.microsoft.sqlserver.jdbc.SQLServerDriver",	"jdbc:sqlserver://sqlprod01.corp.chpinfo.com:1433;databaseName=IDSProd",	"app_etl",		"fact-H3d4x"),
	
	DM( Rdbms.POSTGRESQL,	"org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5444/DM_DEV",				"whs_viewer",	"whs_viewer"),
	
	DMF( Rdbms.POSTGRESQL,	"org.postgresql.Driver",						"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5432/foundation_data_mart?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",	            "jgagon",	    "FireWork72!"),
	DWF( Rdbms.SQLSERVER,	"com.microsoft.sqlserver.jdbc.SQLServerDriver",	"jdbc:sqlserver://chp-sqldev02.corp.chpinfo.com:1433;databaseName=Foundation_App_Logic;integratedSecurity=true",	"jgagon",		                                                            "FireWork72!")
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
