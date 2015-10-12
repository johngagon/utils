package chp.dbreplicator;

import java.sql.*;
import java.util.*;

public class DatabaseManager {
	
	private Database database;
	private Connection conn;
	private ResultSet rs;
	
	public boolean isConnected(){
		boolean rv = false;
		if(conn!=null){
			try{
				rv = !conn.isClosed();
			}catch(Exception e){
				Log.println(e.getMessage());
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
	
	public int getCountResult(){
		int val = -1;
		try{
			if(rs.next()){
				val = rs.getInt(1);
			} 
		}catch(Exception e){
			Log.println(e.getMessage());
		}
		return val;
	}
	
	public DatabaseManager(Database db) {
		this.database = db;
	}
	
	public void connect(){
		try{
			Class.forName(database.driver());
			Log.println("Connecting with URL:'"+database.url()+"'");
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

	public void execute(String sql){
		//Log.println("SQL:"+sql);
		try{
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.execute();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}	
	public void executeUpdate(String sql, List<Object> data){
		//Log.println("SQL:"+sql);
		try{
			PreparedStatement stmt = conn.prepareStatement(sql);
			for(int i = 0;i<data.size();i++){
				stmt.setObject(i+1, data.get(i));
			}
			stmt.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}		
	
	public ResultSet query(String sql){
		//Log.println("SQL:"+sql);
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
		int row = 0;
			while(rs.next()){
				row++;
				ResultSetMetaData rsmd = rs.getMetaData();
				int colcount = rsmd.getColumnCount();
				Log.print(row+":");
				boolean first = true;
				for(int i=1;i<=colcount;i++){
					if(!first){
						Log.print(",");	
					}else{	
						first=false;
					}					
					String s = rs.getString(i);
					Log.print(s);
				}
				Log.print("\n");
				
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public ResultSet getResult(){
		return this.rs;
	}
	
	public List<ColumnDefinition> getDefinition(){
		List<ColumnDefinition> defs = new ArrayList<ColumnDefinition>();
		try{
			ResultSetMetaData rsmd = rs.getMetaData();
			int colcount = rsmd.getColumnCount();
			ColumnDefinition cd = null;
			for(int i = 1; i<=colcount; i++){
				String colName = rsmd.getColumnName(i);
				int colType = rsmd.getColumnType(i);
				int colLen = rsmd.getPrecision(i);
				int colScal = rsmd.getScale(i);
				cd = new ColumnDefinition(colName,colType,colLen,colScal);
				defs.add(cd);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return defs;
	}
	
	public static void main(String[] args)
	{
		
		/*
		Database remote = Database.SQLPROD;
		Database local = Database.LOCAL;
		DatabaseManager store = new DatabaseManager(remote,local);
		store.connectLocal();
		
		store.connectRemote();
		*/
		//String connStr = "jdbc:hsqldb:mem:mymemdb";//Connection c = DriverManager.getConnection(, "SA", "");
		
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
		
	}

	public List<Object> nextRow() {
		List<Object> data = null;
		try{
			if(this.rs.next()){
				data = new ArrayList<Object>();
				int colcount = rs.getMetaData().getColumnCount();
				for(int i=1;i<=colcount;i++){
					data.add(rs.getObject(i));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return data;
	}
	

}
/*

	public void printResult(){
		try{
			int i = 0;
			while(remoteResult.next()){
			i++;
			String s1 = remoteResult.getString(1);
			System.out.println(i+":"+s1);
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
*/