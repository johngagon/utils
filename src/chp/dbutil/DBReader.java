package chp.dbutil;


import java.sql.*;

import chp.dbreplicator.Log;


public class DBReader {
	
	private Database database;
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
	
	public DBReader(Database db) {
		
		this.database = db;
		
	}
	public void connect(){
		if(database.PG.equals(database.rdbms())){
			connectPG();
		}else{
			connectMS();
		}
	}
	private void connectMS(){
		try{
			Class.forName(database.driver());
			System.out.println("Connecting with URL:'"+database.url()+"'");
			conn = DriverManager.getConnection(database.url());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void connectPG() {
		//"jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5444/DM_DEV"
		// jdbc:postgresql://chp-dbdev03.corp.chpinfo.com:5444/DM_DEV
		try{
			Class.forName(database.driver());
			Log.line("Connecting with URL:'"+database.url()+"'");
			conn = DriverManager.getConnection(database.url(),database.user(),database.password());
		}catch(Exception e){
			e.printStackTrace();
		}		
	}	
	public void closeRs(){
		try{
			if(rs!=null){
				Statement stmt = rs.getStatement();
				if(stmt!=null){
					stmt.close();
				}
				rs.close();
				rs = null;
			}
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
		Log.line("SQL:"+sql);
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
		DBReader db = new DBReader(Database.SQLPROD);
		String[] tableNames = {"valuequest_vq_carrier"};
		db.connect();
		if(db.isConnected()){
			for(String table:tableNames){
				db.query("select * from "+table);
				if(db.haveResult()){
					
				}			
			}
		}
		db.close();
		/*
		 * Read from a table.
		 * 
		 */
		System.out.println("Finished.");
	}
	
	public int getCountResult(){
		int val = -1;
		try{
			if(rs.next()){
				val = rs.getInt(1);
			} 
		}catch(Exception e){
			Log.line(e.getMessage());
		}
		return val;
	}	
	
	
	
	public static void testSimpleTableRead(){
		System.out.println("Starting.");
		DBReader db = new DBReader(Database.SQLPROD);
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


