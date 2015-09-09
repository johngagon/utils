package cache;

import java.sql.*;
//import com.microsoft.sqlserver.jdbc.*;
import java.util.*;


public class DBReader {
	//jdbc:sqlserver://sqlprod01.corp.chpinfo.com:1433;databaseName=IDSProd       ;user=app_etl ;password=fact-H3d4x
	//jdbc:sqlserver://localhost                 :1433;databaseName=AdventureWorks;user=UserName;password=*****
	private static final String host = "sqlprod01.corp.chpinfo.com";
	private static final String port = "1433";
	private static final String database = "IDSProd";
	private static final String user = "app_etl";
	private static final String password = "fact-H3d4x";//hedax
	
	private String connUrl;
	private Connection conn;
	private ResultSet rs;
	
	public boolean isConnected(){
		boolean rv = false;
		if(conn!=null){
			try{
				rv = !conn.isClosed();
			}catch(Exception e){
				System.out.println(e.getMessage());
				rv = false;
			}
		}
		return rv;
	}
	public boolean haveResult(){
		boolean rv = false;
		if(rs!=null){
			rv = true;
		}
		return rv;
	}
	
	public DBReader() {
		
		connUrl = "jdbc"
				+":"+"sqlserver"+"://"
				+host+":"
				+port+";"
				+"databaseName="+database+";"
				+"user="+user+";"
				+"password="+password;
		
	}
	public void connect(){
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			System.out.println("Connecting with URL:'"+connUrl+"'");
			conn = DriverManager.getConnection(connUrl);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void close(){
		try{
			if(conn!=null){
				conn.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ResultSet query(String sql){
		System.out.println("SQL:"+sql);
		try{
			PreparedStatement stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return rs;
	}
	
	public void printResult(){
		try{
		int i = 0;
			while(rs.next()){
				i++;
				String s1 = rs.getString(1);
				System.out.println(i+":"+s1);
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public ResultSet getResult(){
		return this.rs;
	}

	public static void main(String[] args){
		System.out.println("Starting.");
		DBReader db = new DBReader();
		db.connect();
		if(db.isConnected()){
			db.query("select * from valuequest_vq_carrier");
			if(db.haveResult()){
				db.printResult();
			}			
		}
		db.close();
		System.out.println("Finished.");
	}
	/*
	public List<Table> configure(String[] tableList) {
		List<Table> tables = new ArrayList<Table>();
		DatabaseMetaData dbmd;
		try {
			dbmd = conn.getMetaData();
	
			for(String tableName:tableList){
				ResultSet dbmdrs = dbmd.getColumns(null,null,tableName,null);
				Table table = new Table(tableName);
				while(dbmdrs.next()){
					String colname = dbmdrs.getString(4);
					//int    columnType = dbmdrs.getInt(5);
					Field f = new Field(colname);
					table.addField(f);
				}
				dbmdrs = dbmd.getBestRowIdentifier(null, null, tableName, DatabaseMetaData.bestRowSession,false);  //.getPrimaryKeys(null,null,tableName);
				int pkCount = 0;
				while(dbmdrs.next()){
					pkCount++;
					if(pkCount>1){
						break;
					}else{
					    String pkname = dbmdrs.getString(2);//4
					    table.setPk(pkname);
					}
				}
				tables.add(table);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return tables;
	}
	*/
}


/*
 * do a read of certain years? read datamart.
 * 
 * 
 * 1. have a list of tables
 * 2. do a count of rows on the table.
 * 3. get a size of each row on the table.
 * 
 * 
 */


