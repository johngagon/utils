package chp.dbreplicator.pg2sql;

import chp.dbreplicator.ColumnDefinition;
import chp.dbreplicator.Database;
import chp.dbreplicator.DatabaseManager;
import chp.dbreplicator.Log;
import chp.dbreplicator.ProgressReporter;
import chp.dbreplicator.SimpleProgressListener;
import chp.dbreplicator.ProgressReporter.Marker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

import jhg.util.TextFile;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;

public class TransferJob {
	
	private static final int VARCHAR_DEFAULT = 255;
	private static final int VARCHAR_LIMIT = 2000;	

	public static class Table{
		public String name;
		public int rows;
		public List<ColumnDefinition> columns;
		public List<String> pkCols;
	}
	
	/*
carr_prod_ca_id_key UNIQUE(ca_id, pc_id, pdr_prod_id, state)
cm_benefit_category_pkey PRIMARY KEY(cm_code)
cost_model_pkey PRIMARY KEY(cm_code)
cpt_pkey PRIMARY KEY(cpt_code, cq_year, upload)

	 */
	
	//@SuppressWarnings("unused")
	public static void copyDmDevFoundationToDevSqlFoundation(){
		Log.pl("Starting Copy of DMDEV Foundation to DEVSQLSERVER Foundation MarketReports on "+new java.util.Date());
		Log.pl("java.lib.path -- Be sure to copy lib/sqljdbc_auth.dll here: "+System.getProperty("java.library.path"));
		DatabaseManager sourceDatabase = new DatabaseManager(Database.DM);
		DatabaseManager targetDatabase = new DatabaseManager(Database.DWF);		
		sourceDatabase.connect();
		Log.pl("Connected to "+Database.DMF.name()+" is connected: "+sourceDatabase.test());
		
		final String S = "valuequest";
		boolean doCount = false;
		List<Table> tables = getTables(sourceDatabase,S,doCount);
		
		int totalCount = countRows(tables);
		
		
		ProgressReporter progrpt = new ProgressReporter(116765952,Marker.PERCENT);
		progrpt.addListener(new SimpleProgressListener());
		
		Log.pl("Total counts:"+totalCount);
		
		
		targetDatabase.connect();
		Log.pl("Connected to "+Database.DWF.name()+" is connected: "+targetDatabase.test());
		
		dumpTables(sourceDatabase,S,tables,progrpt);
		//copyTables(sourceDatabase,targetDatabase,S,tables,progrpt);
		
		sourceDatabase.close();
		targetDatabase.close();
		
		Log.pl("Finished on "+new java.util.Date()+"!");
	}
	
	private static List<Table> getTables(DatabaseManager db, String schema, boolean doCount) {
		Log.pl("Getting tables for "+schema);
		List<Table> rv = new ArrayList<Table>();
		List<String> tableStrings = db.getTables(schema);
		String[] sa = {"carr_prod","cm_benefit_category","cost_model","detail_level","mm_benefit_category"};
		List<String> nonUploadTables = Arrays.asList(sa);
		for(String tableString:tableStrings){
			
			Table table = new Table();
			table.name = tableString;
			String criteria = "";
			if(!nonUploadTables.contains(tableString)){
				criteria = " where cq_year = 2011 or (cq_year=2012 and upload = 2)";
			}
			if(doCount){
				String countQuery = "select count(*) from "+schema+"."+tableString+criteria;
				db.query(countQuery);
				table.rows = db.getCountResult();
			}else{
				table.rows = 100;
			}
			
			table.columns = db.getColumns(schema, tableString);
			table.pkCols = db.getPKColumns(schema, tableString);
			if(table.pkCols.size()==0){
				List<String> pks = new ArrayList<String>();
				for(ColumnDefinition cd:table.columns){
					pks.add(cd.getColName());
				}
				table.pkCols = pks;
			}
			Log.pl("Schema: "+schema+"  Table: "+tableString+"  rows: "+table.rows+"  columns:"+table.columns.size()+" pks: ["+StringUtils.join(table.pkCols, ',')+"].");
			rv.add(table);
			
		}
		Log.pl("Found "+rv.size()+" tables for schema:"+schema+".");
		return rv;
	}	
	
	private static void dumpTables(DatabaseManager sourceDatabase, String schema, List<Table> tables, ProgressReporter progrpt) {
		Log.profile("Copying tables for schema: "+schema);
		String[] sa = {"carr_prod","cm_benefit_category","cost_model","detail_level","mm_benefit_category"};
		List<String> nonUploadTables = Arrays.asList(sa);
		
		//sort the tables by the row counts ascending
		
		//int breakCount = 4;
		int count = 0;
		for(Table table:tables){
			String fullTableName = schema+"."+table.name;
			String query = "select * from "+fullTableName;
			String criteria = "";
			if(!nonUploadTables.contains(table.name)){
				criteria = " where cq_year = 2011 or (cq_year=2012 and upload = 2)";
			}
			
			String filename = "data/pgout/"+fullTableName+".csv";
			
			TextFile.write(filename,makeHeader(table));
			Log.profile("    Query:"+query+criteria+"    current work count:"+count);
			ResultSet rs = sourceDatabase.queryLarge(query+criteria);
			//Log.profile("    Query finished");
			
			try{
				StringBuilder sb = new StringBuilder();
				while(rs.next()){
					
					List<ColumnDefinition> cds = table.columns;
					boolean first = true;
					for(ColumnDefinition cd:cds){
						
						if(!first){sb.append(",");}else{first=false;}
						
						switch (cd.getColType()){
							case Types.VARCHAR:sb.append("\""+rs.getString(cd.getColName())+"\"");break;
							case Types.INTEGER:sb.append(rs.getInt(cd.getColName()));break;
							case Types.BIGINT:sb.append(rs.getLong(cd.getColName()));break;
							case Types.NVARCHAR:sb.append("\""+rs.getString(cd.getColName())+"\"");break;
							case Types.DATE:sb.append("\""+rs.getDate(cd.getColName())+"\"");break;
							default : sb.append("\""+rs.getString(cd.getColName())+"\"");break;
						}
						
					}//for(ColumnDefinition cd:cds)
					sb.append("\n");
					//Log.profile("    Read Record "+count+"  appending to file:"+filename);
					if(count%100000==0){
						Log.profile("Appending file "+filename+" record count:"+count+" out of 116,765,952");
						TextFile.append(filename,sb.toString());//FIXME make this easier.
						sb = new StringBuilder();
					}
					//Log.profile("      Appended");
					
					
					count++;
					//progrpt.completeWork();
				}//while(rs.next())
				TextFile.append(filename,sb.toString());
				sb = new StringBuilder();				
				
			}catch(Exception e){
				e.printStackTrace();
			}//trycatch
			Log.profile("Finished dumping data for "+fullTableName+" to :"+filename+".");
			Log.pl(" ");
			
			//if(count>=breakCount){
			//	break;
			//}
		}//for(Table table:tables)
	}
	
	private static String makeHeader(Table table) {
		boolean first=true;
		StringBuilder sb = new StringBuilder();
		List<ColumnDefinition> cds = table.columns;
		for(ColumnDefinition cd:cds){
			if(!first){sb.append(",");}else{first=false;}
			sb.append(cd.getColName());
		}
		sb.append("\n");
		return sb.toString();
	}

	@SuppressWarnings("unused")
	private static void copyTables(DatabaseManager sourceDatabase, DatabaseManager targetDatabase, String schema, List<Table> tables, ProgressReporter progrpt) {
		Log.pl("Copying tables for schema: "+schema);
		for(Table table:tables){
					//Log.pl("Copying table :"+schema+"."+table.name);
			createTable(targetDatabase,schema,table);
		}	
		for(Table table:tables){
			String query = "select * from "+schema+"."+table+"";
			ResultSet rs = sourceDatabase.query(query);
			try {
				while(rs.next()){
					
					progrpt.completeWork();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			//XXXXXXList<Integer> pks = getPrimaryKeys(sourceDatabase,schema,table);
			//Log.pl("  Retrieving keys for table :"+schema+"."+table.name+" count: "+0);
			//Log.pl("  Copying data table :"+schema+"."+table.name+" count: "+0);
			//XXXXXfor(Integer pk:pks){
			//	copyRow(schema,table,pk,sourceDatabase,targetDatabase);
				
			//	progrpt.completeWork();
			//}
			//verify(targetDatabase,schema,table);
		
	}



	//@SuppressWarnings("unused")
	private static boolean createTable(DatabaseManager targetDatabase, String schema, Table table) {
		Log.pl("  Creating table: "+schema+"."+table.name+" ;");
		
		String ddl = "";
		//if(targetDatabase.doesTableExist(table.name)){
		//	ddl = "DROP TABLE "+table.name;
		//	targetDatabase.execute(ddl);
		//}
		List<ColumnDefinition> defs = table.columns;
		
		ddl = "create table "+schema+"."+table.name + "(";
		boolean first = true;
		boolean doPK = false;
		for(ColumnDefinition def:defs){
			String colname = def.getColName();
			if(!first){
				ddl +=",";
			}else{
				doPK = true;
				first = false;
			}
			
			if("group".equals(colname.toLowerCase())){
				colname = "\""+colname+"\"";
			}
			
			ddl += colname+" "+getSqlDataType(def.getColType());
			if(def.hasLength()){
				int collength = def.getColLen();
				if(collength>0 && collength<VARCHAR_LIMIT ){
					ddl+= "("+collength+")";
				}else{
					if("description".equals(colname)){
						ddl+= "("+VARCHAR_LIMIT+")";
					}else{
						ddl+= "("+VARCHAR_DEFAULT+")";
					}
				}
			}

		}
		if(doPK){
			ddl+=" ,PRIMARY KEY("+StringUtil.join(table.pkCols,",")+")";
		}		
		ddl+=")";
		Log.pl("  DDL:"+ddl);
		targetDatabase.execute(ddl);
		Log.pl("  Created "+schema+"."+table.name);
		
		return true;
	}
	
	private static String getSqlDataType(int type){
		switch (type){
			case Types.VARCHAR:return "VARCHAR";
			case Types.INTEGER:return "INTEGER";
			case Types.BIGINT:return "BIGINT";
			case Types.NVARCHAR:return "VARCHAR";
			case Types.DATE:return "DATE";
			default : return "VARCHAR";
		}
		//return null;
		
	}	
	
	@SuppressWarnings("unused")
	private static List<Integer> getPrimaryKeys(DatabaseManager sourceDatabase, String schema, Table table) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unused")
	private static void copyRow(String schema, String destSchema, Table table, Integer pk, DatabaseManager sourceDatabase, DatabaseManager targetDatabase) {
		// TODO Auto-generated method stub
		
	}	
	
	@SuppressWarnings("unused")
	private static boolean verify(DatabaseManager targetDatabase, String schema, Table table) {
		Log.pl("  Verifying data for table :"+schema+"."+table.name+" count: "+0);
		// TODO Auto-generated method stub
		return false;
	}	
	
	private static int countRows(List<Table> tables) {
		int rv = 0;
		for(Table t:tables){
			int r = t.rows;
			Log.pl("  Table : "+t.name+" has "+r+" rows.");
			rv += r;
		}
		
		return rv;
	}



	public static void main(String[] args){
		Log.pl("***TransferJob.main***");
		copyDmDevFoundationToDevSqlFoundation();
		Log.divider(80, "*");
	}
	
	/*
	 * x Connect to source PG database, test connections.
	 * x Connect to target SQL database
	 * 
	 * x 1. rows and columns count source for each table. 
	 * x 2. get list of tables in source.
	 * x 3. iterate tables in source.
	 * 	.a. create table in target
	 *  .b. select primary keys.
	 *  .c. select by pk into memory
	 *  .d. insert into target
	 *  .e. time in milliseconds the first row... multiply time by total work count. (total of all row counts)
	 *  e. show progress table count/total  row count/total  agg row count/total agg row count -> multiply by millis to get time estimate. 
	 *  f. verify row and columns - report.
	 * 4. 
	 * 
	 */
	
}
