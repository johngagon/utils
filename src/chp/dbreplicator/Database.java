package chp.dbreplicator;

public enum Database {

	LOCAL_CO("org.hsqldb.jdbc.JDBCDriver","jdbc:hsqldb:mem:company","foundation","f0vnd4t10n"),
	LOCAL_ES("org.hsqldb.jdbc.JDBCDriver","jdbc:hsqldb:mem:employer_search","foundation","f0vnd4t10n"),
	SQLPROD("com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver://sqlprod01.corp.chpinfo.com:1433;databaseName=IDSProd","app_etl","fact-H3d4x")
	;
	private String url;
	private String user;
	private String password;
	private String driver;
	private Database(String _driver, String _url, String _user, String _password){
		this.driver = _driver;
		this.url = _url;
		this.user = _user;
		this.password = _password;
	}
	public String driver(){
		return this.driver;
	}	
	public String url(){
		return this.url;
	}
	public String user(){
		return this.user;
	}
	public String password(){
		return this.password;
	}

}
