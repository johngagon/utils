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
				Log.line(e.getMessage());
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
			Log.line(e.getMessage());
		}
		return val;
	}
	
	public DatabaseManager(Database db) {
		this.database = db;
	}
	
	public void createReferenceTable(String tableName, String[] fields, String[] fielddeclarations, List<String[]> data){
		/*
		 * create table with fieldnames
		 * looping list, insert into table (String[] loop fields) values( String[] loop data of List)
		 * 
		 */
	}
	
	public boolean doesSchemaExist(String schema){
		Log.line("Checking if schema "+schema+" exists.");
		boolean rv = false;
		try{
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getSchemas();
			while (rs.next()) {
				String found = rs.getString(1);
				if(schema.equalsIgnoreCase(found)){
					Log.line("Schema found: "+found);
					rv = true;
					break;
				}
			}
			if(!rv){
				Log.line("Schema not found: "+schema);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return rv;
	}	
	
	public boolean grantAllOnAllTableToUser(String schema, String user){
		
		boolean rv = true;
		List<String> tables = getTables(schema);
		Log.line("Tables found:"+tables.size());
		for(String table:tables){
			String sql = "GRANT ALL ON "+table+" TO "+user;
			Log.line(sql);
			try{
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.execute();
				
			}catch(Exception e){
				e.printStackTrace();
				rv = false;
				break;
			}
		}
		return rv;
	}
	
	public List<String> getTables(String schema){
		List<String> list = new ArrayList<String>();
		
		try{
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, null, null);
			while(rs.next()){
				String catFound = rs.getString(1);
				String schemaFound = rs.getString(2);
				String tableFound = rs.getString(3);
				Log.line("Get tables: found "+catFound+"."+schemaFound+"."+tableFound);
				list.add(tableFound);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public void listAllTables(boolean includeSystem){
		Log.line("\nListing tables.");
		try{
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, null, null);
			while (rs.next()) {
				String schema = rs.getString(2);
				if(     (  
						   (!"INFORMATION_SCHEMA".equals(schema)) 
						&& (!"SYSTEM_LOBS".equals(schema))
						) 
						|| includeSystem){
					Log.line(rs.getString(2)+"."+rs.getString(3));
				}
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public boolean doesTableExist(String table){
		
		boolean rv = false;
		//split if contains .
		String schema = null;
		if(table.indexOf(".")!=-1){
			String[] names = table.split("\\.");
			schema = names[0];
			table = names[1];
		}
		Log.line("Checking if schema: "+schema+", table: "+table+" exists.");
		try{
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, schema, table.toLowerCase(), null);
			if (rs.next()) {
				Log.line("Table found: "+rs.getString(3));
				rv = true;
			}
			rs.close();
			rs = md.getTables(null, null, table.toUpperCase(), null);
			if (rs.next()) {
				Log.line("Table found: "+rs.getString(3));
				rv = true;
			}
			rs.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		if(!rv){
			Log.line("Table not found: "+table);
		}
		return rv;
	}
	public boolean doesIndexExist(String index, String table) {
		boolean rv = false;
		//split if contains .
		String schema = null;
		int i = 0;
		Log.line("Checking if index: "+index+" exists.");
		try{
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getIndexInfo(null,null,table,false,false);//.getTables(null, schema, table.toLowerCase(), null);
			
			while(rs.next()) {
				String foundIndex = rs.getString(6);
				if(foundIndex.equalsIgnoreCase(index)){
					Log.line("Index found: "+foundIndex);
					rv = true;
					break;
				}
				i++;
			}
			rs.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		if(!rv){
			Log.line("Index not found: "+index+" checked: "+i+" entries.");
			
		}
		return rv;
	}
	
	
	public void connect(){
		try{
			Class.forName(database.driver());
			Log.line("Connecting with URL:'"+database.url()+"'");
			conn = DriverManager.getConnection(database.url(),database.user(),database.password());
			Log.line("Connected to:'"+database.url()+"'");
		}catch(Throwable t){
			t.printStackTrace();
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
			Log.line("Closed connection. - "+database.url());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public boolean test(){
		boolean rv = false;
		String sql = this.database.rdbms().getTestQuery();
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				rv = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return rv;
	}
	
	public void execute(String sql){
		//Log.println("SQL:"+sql);
		try{
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.execute();
			Log.line("  Executed SQL :"+sql);
		}catch(Exception e){
			Log.line("  Exception SQL:"+sql);
			e.printStackTrace();
		}
		
	}	
	public boolean executeUpdate(String sql, List<Object> data){
		//Log.println("SQL:"+sql);
		boolean rv = false;
		Object datum = null;
		try{
			PreparedStatement stmt = conn.prepareStatement(sql);
			for(int i = 0;i<data.size();i++){
				datum = data.get(i);
				stmt.setObject(i+1, datum);
			}
			stmt.executeUpdate();
			rv = true;
		}catch(Exception e){
			Log.line("SQL:"+sql);
			Log.line("Datum:"+datum);
			e.printStackTrace();
		}
		return rv;
	}
	
	public Database getDatabase(){
		return this.database;
	}
	
	public ResultSet query(String sql){
		//Log.println("SQL:"+sql);
		try{
			PreparedStatement stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			Log.line("Executed query:"+sql);
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
		Database[] dbs = Database.values();
		for(Database db:dbs){
			DatabaseManager dbm = new DatabaseManager(db);
			dbm.connect();
			if(dbm.isConnected()){
				if(dbm.test()){
					Log.line("  PASS!");
				}else{
					Log.line("  FAIL!");
				}
			}else{
				Log.line("No connection was made for: "+db.name()+":"+db.url());
			}
			dbm.close();
		}
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
