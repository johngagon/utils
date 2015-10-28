package chp.dbreplicator;

public enum Rdbms {
	POSTGRESQL("SELECT 1"),
	SQLSERVER("SELECT 1"),
	HSQLDB("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS")
	;
	private String testQuery;
	private Rdbms(String s){
		this.testQuery = s;
	}
	public String getTestQuery() {
		return testQuery;
	}
	
}
