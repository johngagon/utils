package chp.dbreplicator.etl;

import chp.dbreplicator.Database;

/**
 * This class will patch data based on set differences of primary keys between two identical schema tables.
 * 
 * This assumes that:
 * 
 * a) the tables are in fact about identical for the purpose of patching. This means same table name, column names and types.
 * 
 * b) the data dependent on the primary keys have not changed and do not need updating.
 * 
 * 
 * 
 * @author jgagon
 *
 */
public class PatchData {

	/*
	 * The data needs some standard columns
	 * 
	 * If the last updated date is later in the target, then the row is due to be updated
	 * If the target has a key the source doesn't have, it will be deleted if switched true.
	 * If the source has a key the target doesn't have, it will be inserted.
	 * 
	 */
	public static void patchLifeCycledData(Database source, Database target, String table, String lastUpdated, String[] uniqueKeys, boolean delete){
		
	}
	
	public static void patchSimpleData(){
		
	}
	
}
