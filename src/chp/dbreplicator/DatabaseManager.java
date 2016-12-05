package chp.dbreplicator;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DatabaseManager {
	
	public static Map<Integer,String> TYPES = getAllJdbcTypeNames();
	
	private Database database;
	private Connection conn;
	private ResultSet rs;
	
	public boolean isConnected(){
		boolean rv = false;
		if(conn!=null){
			try{
				rv = !conn.isClosed();
			}catch(Exception e){
				Log.error(e);
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
			Log.error(e);
		}
		return val;
	}
	private Logging log;
	
	public DatabaseManager(Database db, Logging aLog) {
		this.database = db;
		this.log = aLog;
	}
	public DatabaseManager(Database db) {
		this.database = db;
		this.log = new Log();
	}	
	public void createReferenceTable(String tableName, String[] fields, String[] fielddeclarations, List<String[]> data){
		
		String ddl = "CREATE TABLE "+tableName+" (";
		String insertQueryHead = "INSERT INTO "+tableName+" ( ";
		String insertQueryTail = "(";
		boolean first = true;
		for(int i=0;i<fields.length;i++){
			if(first){
				first = false;
			}else{
				ddl+=",";
				insertQueryHead+=",";
				insertQueryTail+=",";
			}
			ddl += fields[i] + " " + fielddeclarations[i];
			insertQueryHead += fields[i];
			insertQueryTail +="?";
		}
		ddl += ")";
		insertQueryHead +=")";
		insertQueryTail +=")";
		String insertQuery = insertQueryHead + " VALUES " + insertQueryTail;
		/*
		 * create table with fieldnames
		 * looping list, insert into table (String[] loop fields) values( String[] loop data of List)
		 * 
		 */
		try{
			execute(ddl);
			int x = 0;
			for(String[] row:data){
				x++;
				List<Object> rowObj = new ArrayList<Object>();
				for(String val:row){
					rowObj.add(val);
				}
				executeUpdate(insertQuery,rowObj);
			}		
			log.pp("Inserted "+x+" records into "+tableName+" .");
		}catch(Exception e){
			Log.error(e);
		}
	}
	
	public DatabaseMetaData getMetaData(){
		DatabaseMetaData dbmd = null;
		try{
			dbmd = conn.getMetaData();
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		return dbmd;
	}


	public boolean doesSchemaExist(String schema){
		log.pp("Checking if schema "+schema+" exists.");
		boolean rv = false;
		try{
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getSchemas();
			while (rs.next()) {
				String found = rs.getString(1);
				if(schema.equalsIgnoreCase(found)){
					log.pp("Schema found: "+found);
					rv = true;
					break;
				}
			}
			if(!rv){
				Log.err("Schema not found: "+schema);
			}
		}catch(Exception e){
			Log.error(e);
		}
		return rv;
	}	
	
	public boolean grantAllOnAllTableToUser(String schema, String user){
		
		boolean rv = true;
		List<String> tables = getTables(schema);
		log.pp("Tables found:"+tables.size());
		for(String table:tables){
			String sql = "GRANT ALL ON "+table+" TO "+user;
			log.pp(sql);
			try{
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.execute();
				
			}catch(Exception e){
				Log.error(e);
				rv = false;
				break;
			}
		}
		return rv;
	}
	
	public static Map<Integer, String> getAllJdbcTypeNames() {
	    Map<Integer, String> result = new HashMap<Integer, String>();
		try{
		    for (Field field : Types.class.getFields()) {
		        result.put((Integer)field.get(null), field.getName());
		    }
		}catch(Exception e){
			Log.error(e);
		}

	    return result;
	}	
	
	public Connection getConnection(){
		return conn;
	}
	
	public List<ColumnDefinition> getColumns(String schema, String table){
		String query = "";
		if(getDatabase().rdbms().equals(Rdbms.POSTGRESQL)){
			query = "select * ";
		}else{
			query = "select top 1 * ";
		}
		query +=" from "+schema+"."+table;
		if(getDatabase().rdbms().equals(Rdbms.POSTGRESQL)){
			query += " limit 1";
		}
		query(query);
		return getDefinition();
	}
	public List<String> getPKColumns(String schema, String table){
		List<String> defs = new ArrayList<String>();
		try{
			DatabaseMetaData dbmd = conn.getMetaData();
			String catalog = null;
			
			ResultSet rs = dbmd.getPrimaryKeys(catalog, schema, table);
			while(rs.next()){
				defs.add(rs.getString(4));
			}

		}catch(Exception e){
			Log.error(e);
		}
		return defs;		
	}
	public List<Index> getIndexes(String schema,String table){
		List<Index> list = new ArrayList<Index>();
		try{
			DatabaseMetaData md = conn.getMetaData();
			String catalogPattern = null;
			ResultSet rs = md.getIndexInfo(catalogPattern, schema, table, false, false);
			while(rs.next()){
				Index idx = new Index(rs);
				if(idx.isValid()){
					list.add(idx);
				}
			}
		}catch(Exception e){
			Log.error(e);
			return new ArrayList<Index>();
		}
		return list;		
	}
	
	
	public List<String> getTables(String schema){
		List<String> list = new ArrayList<String>();
		
		try{
			DatabaseMetaData md = conn.getMetaData();
			String catalogPattern = null;
			String tablePattern = null;
			String[] types = null;
			ResultSet rs = md.getTables(catalogPattern, schema, tablePattern, types);
			while(rs.next()){
				String catFound = rs.getString(1);
				
				@SuppressWarnings("unused")
				String schemaFound = rs.getString(2);
				
				String tableFound = rs.getString(3);
				String tableType = rs.getString(4);
				
				@SuppressWarnings("unused")
				String cat = "";
				if(catFound!=null){
					cat = catFound+".";
				}
				
				if("TABLE".equals(tableType)){//FIXME : Postgres specific
					//log.pp("  Get tables: found "+cat+schemaFound+"."+tableFound+".");//+" tableType:"+tableType);
					list.add(tableFound);
				}
			}
		}catch(Exception e){
			Log.error(e);
		}
		return list;
	}
	
	public void listAllTables(boolean includeSystem){
		log.pp("\nListing tables.");
		try{
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, null, null);
			while (rs.next()) {
				String schema = rs.getString(2);
				String table = rs.getString(3);
				if(     (  
						   (!"INFORMATION_SCHEMA".equals(schema)) 
						&& (!"SYSTEM_LOBS".equals(schema))
						) 
						|| includeSystem){
					log.pp(schema+"."+table);
					listAllIndexes(table);
				}
			}
			rs.close();
		}catch(Exception e){
			Log.error(e);
		}		
	}

	
	public boolean tableNotEmpty(String _table){
		boolean hasRecords = tableHasRecords(_table,0);
		if(hasRecords){
			log.pp("Table "+_table+" not empty.");
		}
		return hasRecords;
	}
	public void dropTable(String _table) {
		String sql = "DROP TABLE "+_table;
		this.execute(sql);
		log.pp("  Dropped table: '"+_table+"'");
	}	
	
	public void clearTable(String _table){
		String sql = "DELETE FROM "+_table;
		this.execute(sql);
		log.pp("  Deleted all records from "+_table);
	}


	
	public void insertFromResult(int fieldCount,String insertSql, ProgressReporter pr){
		if(this.haveResult()){
			try{
				log.pp("Inserting records..fail sensitive");
				int count = 0;
				while(rs.next()){
					count++;
					List<Object> data = new ArrayList<Object>();
					for(int i=1;i<=fieldCount;i++){
						data.add(rs.getString(i));
					}
					boolean success = this.executeUpdate(insertSql, data);
					if(!success){
						log.pp("Insert fail.");
						break;
					}
					if(pr!=null){
						pr.completeWork();
					}
				}
				log.pp("Inserted "+count+" records.");
			
			}catch(Exception e){
				Log.error(e);
			}	
		}else{
			Log.err("Error, must set result first.");
		}
	}
	public boolean tableHasRecords(String _table, int recordMin) {
		boolean rv = false;
		String sql = "select count(*) from "+_table;
		this.query(sql);
		if(this.haveResult()){
			int count = -1;
			try {
				if(rs.next()){
					count = this.rs.getInt(1);
					log.pp("Found :"+count+" records on table: "+_table+" min:"+recordMin);
				}else{
					log.pp("No count result found for table: "+_table);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				Log.error(e);
			}
			rv = count>=recordMin;
			this.closeRs();
		}else{
			log.pp("tableHasRecords('"+_table+"',"+recordMin+") no result.");
		}
		return rv;
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
		log.pp("  Checking if schema: "+schema+", table: "+table+" exists.");
		try{
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, schema, table.toLowerCase(), null);
			if (rs.next()) {
				log.pp("  Table found: "+rs.getString(3));
				rv = true;
			}
			rs.close();
			rs = md.getTables(null, null, table.toUpperCase(), null);
			if (rs.next()) {
				log.pp("  Table found: "+rs.getString(3));
				rv = true;
			}
			rs.close();
			
		}catch(Exception e){
			Log.error(e);
		}
		if(!rv){
			log.pp("  Table not found: "+table);
		}
		return rv;
	}
	private void listAllIndexes(String table){
		log.pp("  Listing indexes on table: '"+table+"'");
		try{
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getIndexInfo(null,null,table,false,false);//.getTables(null, schema, table.toLowerCase(), null);
	
			while(rs.next()) {
				String foundIndex = rs.getString(6);
				log.pp("   "+foundIndex);
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();//Log.error(e);
		}
	}	
	public boolean doesIndexExist(String index, String table) {
		boolean rv = false;
		//split if contains .
		//String schema = null;
		int i = 0;
		String schema = null;
		if(table.indexOf(".")!=-1){
			String[] names = table.split("\\.");
			schema = names[0];
			table = names[1];
		}		
		log.pp("Checking if index: "+index+" exists on "+schema+" - '"+table+"'");
		try{
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getIndexInfo(null,null,table,false,false);//.getTables(null, schema, table.toLowerCase(), null);
			
			while(rs.next()) {
				String foundIndex = rs.getString(6);
				boolean isFound = foundIndex.equalsIgnoreCase(index.toUpperCase()); 
				if(isFound){
					log.pp("  Index found: "+foundIndex);
					rv = true;
					break;
				}
				i++;
			}
			rs.close();
			
		}catch(Exception e){
			e.printStackTrace();//Log.error(e);
		}
		if(!rv){
			log.pp("  Index not found: "+index+" checked: "+i+" entries.");
		}
		return rv;
	}
	public boolean doesAnyIndexExist(String schema, String table) {
		boolean rv = false;
		//split if contains .
		//String schema = null;
		
		log.pp("Checking if any indexes exists on "+schema+" - '"+table+"'");
		try{
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getIndexInfo(null,schema,table,false,false);//.getTables(null, schema, table.toLowerCase(), null);
			
			while(rs.next()) {
				rv = true;
				break;
			}
			rs.close();
			
		}catch(Exception e){
			e.printStackTrace();//Log.error(e);
		}
		return rv;
	}	
	
	public void connect(){
		try{
			Class.forName(database.driver());
			log.pp("Connecting with URL:'"+database.url()+"'");
			conn = DriverManager.getConnection(database.url(),database.user(),database.password());
			log.pp("Connected to:'"+database.url()+"'");
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
			Log.error(e);
		}
	}
	
	public void close(){
		try{
			if(conn!=null){
				conn.close();
			}
			log.pp("Closed connection. - "+database.url());
		}catch(Exception e){
			Log.error(e);
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
			Log.error(e);
		}
		return rv;
	}
	
	public void execute(String sql){
		//log.ppln("  Call to Execute SQL:"+sql);
		try{
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.execute();
			log.pp("  Executed SQL :"+sql);
		}catch(Exception e){
			log.pp("  Exception SQL:"+sql);
			Log.error(e);
		}
		
	}	

	public boolean executeUpdate(String sql, List<ColumnDefinition> cds, List<Object> data){
		//log.ppln("SQL:"+sql);
		boolean rv = false;
		Object datum = null;
		int j = 0;
		try{
			PreparedStatement stmt = conn.prepareStatement(sql);
			for(int i = 0;i<data.size();i++){
				j = i+1;
				datum = data.get(i);
				ColumnDefinition cd = cds.get(i);
				setStatementByType(stmt,j,cd,datum);
				//stmt.setObject(j, datum);
			}
			stmt.executeUpdate();
			rv = true;
		}catch(SQLException sqle){
			sqle.printStackTrace();
			Log.printt("SQLException SQL:"+sql+" .");
			Log.cr();
			//log.pp("  Datum("+j+"):"+datum);
			Log.printt(data);
			Log.cr();
			Log.printt("SQLException:"+sqle.getMessage());
		}catch(Exception e){
			Log.exception(e);
		}
		return rv;
	}	
	
	@SuppressWarnings("boxing")
	private void setStatementByType(PreparedStatement stmt, int j,	ColumnDefinition cd, Object datum) {
		int colType = cd.getColType();
		log.pp("Setting field: "+j+"  coltype: "+TYPES.get(colType)+"  to "+datum.toString()+" "+datum.getClass().getCanonicalName()+"");
		
		try{
			switch(colType){
				
				case          1111 : stmt.setObject(j, datum);break;
				case Types.VARCHAR : stmt.setString(j, (String)datum);break;
				case Types.INTEGER : stmt.setLong(j, (Long)datum);break;
				case Types.BIGINT  : stmt.setLong(j, (Long)datum);break;
				case Types.NVARCHAR: stmt.setString(j, (String)datum);break;
				case Types.DATE    : stmt.setObject(j, datum);break;               //setDate uses java.sql.Date.
				default            : stmt.setString(j, (String)datum);break;
			}
		}catch(SQLException sqle){
			Log.printt("SQL Exception !!!: "+j);
			sqle.printStackTrace();
		}catch(Exception e){
			Log.printt("Exception!: "+j);
			e.printStackTrace();
		}
		//	stmt.setObject(j, datum);
		
	}

	
	
	public boolean executeUpdate(String sql, List<Object> data){
		//log.ppln("SQL:"+sql);
		boolean rv = false;
		Object datum = null;
		int j = 0;
		try{
			PreparedStatement stmt = conn.prepareStatement(sql);
			for(int i = 0;i<data.size();i++){
				j = i+1;
				datum = data.get(i);
				stmt.setObject(j, datum);
			}
			stmt.executeUpdate();
			rv = true;
		}catch(SQLException sqle){
			sqle.printStackTrace();
			Log.printt("SQLException SQL:"+sql+" .");
			Log.cr();
			//log.pp("  Datum("+j+"):"+datum);
			Log.printt(data);
			Log.cr();
			Log.printt("SQLException:"+sqle.getMessage());
		}catch(Exception e){
			Log.exception(e);
		}
		return rv;
	}
	
	public Database getDatabase(){
		return this.database;
	}
	
	public int getResultColCount(){
		ResultSetMetaData rsmd;
		int rv = -1;
		try {
			rsmd = rs.getMetaData();
			rv = rsmd.getColumnCount();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return rv;
	}

	public ResultSet queryThrow(String sql) throws SQLException{
		PreparedStatement stmt = conn.prepareStatement(sql);
		rs = stmt.executeQuery();
			
		return rs;
	}	
	
	public ResultSet query(String sql){
		//log.ppln("SQL:"+sql);
		try{
			PreparedStatement stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			//log.pp("  Executed query:"+sql);
		}catch(Exception e){
			System.out.println("SQL:"+sql);
			e.printStackTrace();
			//Log.error(e);
		}
		return rs;
	}
	public ResultSet queryLarge(String sql){
		//log.ppln("SQL:"+sql);
		try{
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setFetchSize(1000);
			rs = stmt.executeQuery();
			//log.pp("  Executed query:"+sql);
		}catch(Exception e){
			System.out.println("SQL:"+sql);
			e.printStackTrace();
			//Log.error(e);
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
				log.pp(row+":");
				boolean first = true;
				for(int i=1;i<=colcount;i++){
					if(!first){
						log.pp(",");	
					}else{	
						first=false;
					}					
					String s = rs.getString(i);
					log.pp(s);
				}
				log.pp("\n");
				
			}	
		}catch(Exception e){
			Log.error(e);
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
				
				cd = new ColumnDefinition(colName,colType,colLen,colScal,i);
				defs.add(cd);
			}
		}catch(Exception e){
			Log.error(e);
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
					Log.printt("  PASS!");
				}else{
					Log.printt("  FAIL!");
				}
			}else{
				Log.printt("No connection was made for: "+db.name()+":"+db.url());
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
			Log.error(e);
		}
		return data;
	}


	
	public String createInsertQuery(List<ColumnDefinition> cds,String destTable) {
		log.pp("createInsertQuery("+destTable+")");
		//List<ColumnDefinition> cds = getColumnDefsFromDbMeta(destTable);
		
		/*
		 * 1. use database meta data to inspect the table.
		 * 2. Get columns
		 */
		return createInsertQuery(destTable,cds);
	}

	public List<ColumnDefinition> getColumnDefsFromDbMeta(String destTable) {
		List<ColumnDefinition> cds = new ArrayList<ColumnDefinition>();
		try {
			DatabaseMetaData dbmd = conn.getMetaData();
			String[] parts = destTable.split("\\.");
			
			ResultSet columns = dbmd.getColumns(null,parts[0],parts[1],null);
			
			final int COLUMN_NAME = 4;
			final int DATA_TYPE = 5;
			final int TYPE_NAME = 6;
			final int NULLABLE = 11;
			final int ORDINAL = 17;
			while(columns.next()){
				String colname = columns.getString(COLUMN_NAME);
				int coltype = columns.getInt(DATA_TYPE);
				String coltypeName = columns.getString(TYPE_NAME);
				int colnullable = columns.getInt(NULLABLE);
				boolean notNull = (colnullable==DatabaseMetaData.columnNoNulls);
				int ordinal = columns.getInt(ORDINAL);
				ColumnDefinition cd = new ColumnDefinition(colname,coltypeName,coltype,notNull,ordinal);
				//log.pp("Column Definition:"+cd.toString());
				cds.add(cd);
			}
		} catch (SQLException e) {
			Log.error(e);
		}
		return cds;
	}
	
	private String createInsertQuery(String tableName, List<ColumnDefinition> defs){
		String query = "INSERT INTO "+tableName+" (";
		String[] parts = tableName.split("\\.");
		
		boolean first = true;
		for(ColumnDefinition def:defs){
			if(!first){
				query+=", ";	
			}else{	
				first=false;
			}
			String colname = def.getColName();
			if("group".equalsIgnoreCase(colname.trim().toLowerCase())){
				colname = "\""+colname+"\"";
			}
			query+=colname;
		}
		query += ") VALUES (";
		first = true;
		
		for(ColumnDefinition def:defs){
			if(!first){
				query+=", ";	
			}else{	
				first=false;
			}
			query+="?";
			if(def.getColType()==1111 && def.getColTypeName()!=null){
				query+="::"+parts[0]+"."+def.getColTypeName();
			}
		}
		query += ")";

		log.pp("INSERT:"+query);
		return query;		
	}


	public String getDDL(String existingSchema, String fqTableName, String newTable) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("CREATE TABLE "+newTable+ " ( ");
		List<ColumnDefinition> defs = getColumnDefsFromDbMeta(fqTableName);
		boolean first = true;
		for(ColumnDefinition def:defs){
			if(!first){
				sb.append(", ");	
			}else{	
				first=false;
			}	
			String nullable = (def.getNullable())?"":"NOT NULL";
			String coltype = ((def.getColType()==1111)?(existingSchema+"."+def.getColTypeName()):def.getColTypeName());//FIXME create types
			
			sb.append("\""+def.getColName()+"\" "+coltype+" "+nullable);
		}
		sb.append(")");
		
		return sb.toString();
	}
	
	public String getDDLOwnerAlter(String existingSchema, String fqTableName, String newTable, String owner) {
		StringBuilder sb = new StringBuilder();
		sb.append("ALTER TABLE "+newTable+ " OWNER TO "+owner+" ");
		return sb.toString();
	}
	public String getDDLOwnerGrant(String existingSchema, String fqTableName, String newTable, String owner) {
		StringBuilder sb = new StringBuilder();
		sb.append("GRANT ALL ON TABLE "+newTable+ " TO "+owner+" ");
		return sb.toString();
	}
	public String getDDLViewerGrant(String existingSchema, String fqTableName, String newTable, String viewer) {
		StringBuilder sb = new StringBuilder();
		sb.append("GRANT SELECT ON TABLE "+newTable+ " TO "+viewer+" ");
		return sb.toString();
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
