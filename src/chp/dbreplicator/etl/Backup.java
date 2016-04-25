package chp.dbreplicator.etl;

import java.util.List;

import chp.dbreplicator.ColumnDefinition;
import chp.dbreplicator.Database;
import chp.dbreplicator.DatabaseManager;
import chp.dbreplicator.Log;

public class Backup {

	public static void main(String[] args){
		backup(Database.DMDEVNEW,"benchmarking");
	}
	
	public static void backup(Database db, String schema){
		/*
		 *  existing schema
		 *  new schema to create defaults to <name>_backup
		 *  
		 *  create new schema.
		 *  get list of tables,  make ddl from existing.
		 *  
		 */
		
		String newSchema = schema+"_backup";
		
		Log.pl("Starting Backup of "+schema+" to "+newSchema+" on "+new java.util.Date());
		
		DatabaseManager database = new DatabaseManager(db);
			
		database.connect();
		Log.pl("Connection to "+db.name()+" is connected: "+database.test());
	
		String sql = "create schema "+newSchema+" ";
		database.execute(sql);
		
		List<String> tables = database.getTables(schema);
		
		for(String table:tables){
			String fqSourceTable = schema+"."+table;
			String fqNewTable = newSchema +"."+ table;
			String ddl = database.getDDL(schema,fqSourceTable,fqNewTable);
			Log.pl("DDL for "+fqSourceTable+" :\n"+ddl);
		}
		
		Log.pl("Finished Backup of "+schema+" to "+newSchema+" at "+new java.util.Date());
	}

	
	
	
}





