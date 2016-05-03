package jhg.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import chp.dbreplicator.Database;
import chp.dbreplicator.Log;

import java.util.*;

import jhg.sql.meta.*;

/**
 * Provides 
 * 
 * @author jgagon
 *
 */
@SuppressWarnings("unused")
public class DatabaseManager {

	private Database database;
	private Connection conn;
	
	public static void main(String[] args){
		Database test = Database.DMFDEV;
		DatabaseManager dm = new DatabaseManager(test);
		dm.connect();
		String testSchema = "employer";
		
		List<Table> tables = dm.getTables(testSchema);
		for(Table t:tables){
			Log.pl(t.toString());
		}
		/*
		List<Attribute> attributes = dm.getAttributes(testSchema);//String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern
		for(Attribute a:attributes){
			Log.pl(a.toString());
		}
		*/
		
	}
	
	public List<Table> getTables(String schema){
		List<Table> tables = new ArrayList<Table>();
		try {
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet rs = dbmd.getTables(null,schema,null,null); //aka get Relation
			while(rs.next()){
				Table table = new Table(rs);
				if("TABLE".equals(table.getTableType())){  //INDEX,SEQUENCE
					tables.add(table);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables;
	}
	
	//org.postgresql.util.PSQLException: Method org.postgresql.jdbc3.Jdbc3DatabaseMetaData.getAttributes(String,String,String,String) is not yet implemented.
	public List<Attribute> getAttributes(String schema) {
		List<Attribute> attributes = new ArrayList<Attribute>();
		try {
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet rs = dbmd.getAttributes(null,schema,null,null);
			while(rs.next()){
				Attribute attr = new Attribute(rs);
				attributes.add(attr);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return attributes;
	}
	/*
	 getSchemas()
	 getSchemas(String catalog, String schemaPattern)
	 */

	public DatabaseManager(Database db) {
		this.database = db;
	}	
	
	public void connect(){
		try{
			Class.forName(database.driver());
			Log.pl("Connecting with URL:'"+database.url()+"'");
			conn = DriverManager.getConnection(database.url(),database.user(),database.password());
			Log.pl("Connected to:'"+database.url()+"'");
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	public void disconnect(){
		try{
			if(conn!=null){
				conn.close();
			}
			Log.pl("Closed connection. - "+database.url());
		}catch(Exception e){
			Log.error(e);
		}
	}	
}
