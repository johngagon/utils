package chp.dbutil;

public enum Database {

	SQLPROD("com.microsoft.sqlserver.jdbc.SQLServerDriver","sqlserver","sqlprod01.corp.chpinfo.com","1433","IDSProd","app_etl","fact-H3d4x"),
	PGDEVOLD("org.postgresql.Driver","postgresql","chp-dbdev03.corp.chpinfo.com","5444","DM_DEV","whs_viewer","whs_viewer"),
	PGDEVNEWMR("org.postgresql.Driver","postgresql","chp-dbdev03.corp.chpinfo.com","5432","foundation_data_mart","app_market_reports","4pp_m4rk3t"),
	//PGDEVNEW
	//PGUATOLD
	//PGUATNEW
	//PGPRODOLD
	//PGPRODNEW
	;
	public final String PG = "postgresql";
	public final String MS = "sqlserver";
	
	private String host;
	private String rdbms;
	private String port;
	private String database;
	private String user;
	private String password;
	private String driver;
	private Database(String _driver,String _rdbms, String _host, String _port, String _database, String _user, String _password){
		this.rdbms = _rdbms;
		this.host = _host;
		this.database = _database;
		this.port = _port;
		this.user = _user;
		this.password = _password;
		this.driver = _driver;
	}
	public String driver(){
		return this.driver;
	}	
	public String url(){
		String connUrl = "";
		if(PG.equals(rdbms)){
			connUrl = "jdbc"
					+":"+rdbms+"://"
					+host+":"
					+port+"/"
					+database;
			
		}else if(MS.equals(rdbms)){
			connUrl = "jdbc"
				+":"+rdbms+"://"
				+host+":"
				+port+";"
				+"databaseName="+database+";"
				+"user="+user+";"
				+"password="+password;
		}
		return connUrl;
	}
	public String user(){
		return this.user;
	}
	public String password(){
		return this.password;
	}
	public String rdbms(){
		return this.rdbms;
	}
}
