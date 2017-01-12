package chp.dbreplicator.etl;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;




import org.apache.commons.cli.*;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import chp.dbreplicator.ColumnDefinition;
/*
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
*/
import chp.dbreplicator.Database;
import chp.dbreplicator.DatabaseManager;
import chp.dbreplicator.Index;
import chp.dbreplicator.ProgressReporter;
import chp.dbreplicator.Rdbms;
import chp.dbreplicator.SimpleProgressListener;
import chp.dbreplicator.ProgressReporter.Marker;
import static java.lang.System.out;

public class MarketReportsDeploy {

	
	private static final String EXIT_FAIL_MSG = "Exit due to premature failure.";
	private static final String D = "\t";
	private static final String noq = "";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private static String Q = noq;	
	

	
	public static final String LOG_DIR = "C:\\deploy\\logs\\";//"K:\\Foundation\\ETLAndDeploymentLogs\\";

	public static final String CONFIG = "mr_table_config.txt";

	public static void main(String[] args){
		Cli cli = new Cli(args).parse();
		
		transfer(!cli.append,cli.from,cli.to,cli.year,cli.upload);
		/*
		 * FIXME create the file if it doesn't exist.
		 * FIXME the database manager needs to have the log set.
		 * FIXME the progress reporter should use System.out.println
		 */
	}
	public static String getFilename(String source, String dest, int year, int upload){
		String U="_";
		SimpleDateFormat ymdFormatter = new SimpleDateFormat("yyyyMMdd");
		Date now = new Date();
		String ymd = ymdFormatter.format(now);
		return "MarketReports" + U + ymd + U + source + "to" + dest + "_"+year+"u"+upload+".txt";
	}
	
	private static void transfer(boolean cleanTarget,Database source, Database target, int year, int upload){
		
		String filename = LOG_DIR + getFilename(source.name(),target.name(),year,upload);
		TextLog log = new TextLog(filename);
		String s = new String("Starting Copy of "+source.name()+" to "+target.name()+" with "+CONFIG+" on "+new java.util.Date());
		log.print(s);
		System.out.println(s);
		log.print("java.lib.path -- Be sure to copy lib/sqljdbc_auth.dll here: "+System.getProperty("java.library.path"));
		DatabaseManager sourceDatabase = new DatabaseManager(source,log);
		DatabaseManager targetDatabase = new DatabaseManager(target,log);	//	
		sourceDatabase.connect();
		boolean connected = sourceDatabase.test();
		log.print("Connected to "+source.name()+" is connected: "+connected);
	
		
		if(!connected){
			log.print(EXIT_FAIL_MSG);
			targetDatabase.close();
			return;
		}
		
		targetDatabase.connect();
		connected = targetDatabase.test();
		log.print("Connected to "+target.name()+" is connected: "+connected);		
		if(!connected){
			log.print(EXIT_FAIL_MSG);
			targetDatabase.close();
			return;
		}
		
		//DEBUG-TESTME
		boolean schemaExists= targetDatabase.doesSchemaExist(getSchema(year, upload));
		if(!schemaExists){
			String createPartitionDDL = getPartitionFunction(year, upload);
			log.print("DDL: "+createPartitionDDL);
			targetDatabase.execute(createPartitionDDL);
			String insertDataSetTx = getDataSetInsert(year, upload);
			log.print("SQL: "+insertDataSetTx);
			targetDatabase.execute(insertDataSetTx);
		}
		
		TextFile f = new TextFile(CONFIG);
		Map<String,String> viewTableMapping = f.getMapping();

		List<String> l = new ArrayList<String>(viewTableMapping.keySet());
		Collections.reverse(l);
		
		//Work estimation for progress and clean
		int totalCount = 0;
		for(String sourceRelation:l){
			String sourceTable = "";
			if( Rdbms.SQLSERVER.equals(source.rdbms())){
				sourceTable = "market_reports."+sourceRelation;
			}else{
				sourceTable = getSchema(year, upload)+"."+sourceRelation;
			}			
			String destTable = getSchema(year, upload)+"."+viewTableMapping.get(sourceRelation);
			totalCount += getTableCount(log, sourceDatabase,sourceTable, year, upload);
			if(cleanTarget){
				cleanTable(targetDatabase,destTable);
			}	
		}
		log.print("\n ");
		
		ProgressReporter pr = new ProgressReporter(totalCount,Marker.TICKS);
		pr.addListener(new SimpleProgressListener());
		
		//Perform transfer
		for(String sourceRelation:viewTableMapping.keySet()){
			String sourceTable = "";
			if( Rdbms.SQLSERVER.equals(source.rdbms())){
				sourceTable = "market_reports."+sourceRelation;
			}else{
				sourceTable = getSchema(year, upload)+"."+sourceRelation;
			}
			String destTable = getSchema(year, upload)+"."+viewTableMapping.get(sourceRelation);

			String schema = destTable.substring(0,destTable.indexOf('.')); 
			String table = "";
			log.print("Schema: "+schema+"  , Table:"+table+" ");

			//Capure indexes and drop
			List<Index> indexes = targetDatabase.getIndexes(schema, table);
			for(Index idx:indexes){
				log.print("Saved Index: "+idx);
				String idxName = idx.getIndexName();
				if(!idxName.endsWith("_pkey")){
					String ddl = "DROP INDEX "+schema+"."+idxName;
					log.print("Temporary remove index by drop :  '"+ddl+"'");
					targetDatabase.execute(ddl);
				}
			}				
			
			
			performCopyByUpload(log, pr, sourceDatabase, targetDatabase, sourceTable, destTable, year, upload);
			
			//Restore indexes
			boolean indexesExist = targetDatabase.doesAnyIndexExist(schema, table);
			if(!indexesExist){
				for(Index idx:indexes){
					String idxName = idx.getIndexName();
					if(!idxName.endsWith("_pkey")){
					
						String ddl = "CREATE INDEX "+idxName+" ON "+idx.getTableSchema()+"."+idx.getTableName()+" USING btree ("+idx.getColumnName()+")";
						targetDatabase.execute(ddl);
					}
						//boolean indexesExist = sourceDatabase.doesAnyIndexExist(schema, table);
				}
			}//if indexes exist
			String analyze = "ANALYZE "+destTable+" ";
			log.print("SQL: "+analyze);
			targetDatabase.execute(analyze);
			
		}//for each sourceRelation
		
		List<String> updatePatches = getMarketDataSetUpdateStatements();
		for(String updateTx:updatePatches){
			log.print("SQL: "+updateTx);
			targetDatabase.execute(updateTx);
		}
		
		sourceDatabase.close();
		targetDatabase.close();
		
		log.print("\n\nFinished on "+new java.util.Date()+"!");			
	}		
	
	
	
	private static int getTableCount(TextLog log, DatabaseManager database,String sourceRelation, int year, int upload){
		log.print("\n\nStarting Count of "+database.getDatabase().name()+" for table:"+sourceRelation);
		int count = 0;
		String sql = "select count(*) from "+sourceRelation+" where cq_year="+year+" and upload="+upload;
		database.query(sql);
		count = database.getCountResult();
		log.print("\n\nFinished Count of "+database.getDatabase().name()+"-"+sourceRelation+":"+count+"\n");
		return count;
	}	
	
	
	private static void cleanTable(DatabaseManager targetDatabase,	String destTable) {
		targetDatabase.clearTable(destTable);
	}
	
	
	private static void performCopyByUpload(TextLog log, ProgressReporter pr, DatabaseManager sourceDatabase, DatabaseManager targetDatabase,
			String sourceRelation, String destTable, 
			int year, int upload) {
		
		
		List<ColumnDefinition> cds = sourceDatabase.getColumnDefsFromDbMeta(sourceRelation);

		String countQuery = "select count(*) from "+sourceRelation;
		if(sourceRelation.startsWith("market_reports.")){
			countQuery += " where cq_year="+year+" and upload="+upload;
		}
		
		sourceDatabase.query(countQuery);
		int countResult = sourceDatabase.getCountResult();
		log.print("Source Result Size:"+countResult);
		int max = getMagnitude(countResult);
		String query = "select * from "+sourceRelation+" where cq_year="+year+" and upload="+upload;
		log.print("Source query: "+query);

		int count=0;

		ResultSet rs = sourceDatabase.queryLarge(query);
		try{
			StringBuilder sb = new StringBuilder();
			while(rs.next()){
				
				boolean first = true;
				for(ColumnDefinition cd:cds){
					if(!first){sb.append(D);}else{first=false;}
					extractResultIntoStringBuilder(rs, sb, cd);
				}//for(ColumnDefinition cd:cds)
				sb.append("\n");
				
				if(count%max==0){
					log.print(new Date()+" Copying data to target: "+destTable+" at count: "+count);
					
					copyPostgres(log, targetDatabase,destTable, sb.toString());
					sb = new StringBuilder();
				}
				count++;
				pr.completeWork();
				
			}//while(rs.next())
			
			copyPostgres(log, targetDatabase, destTable, sb.toString());
			sb = new StringBuilder();				
			
		}catch(Exception e){
			e.printStackTrace();
		}//trycatch
		log.print("Finished copying "+destTable+".\n\n");
	}
	
	private static void copyPostgres(TextLog log, DatabaseManager targetDatabase,String destTable, String s) throws SQLException {
		CopyIn cpIN=null;
		CopyManager cm = new CopyManager((BaseConnection) targetDatabase.getConnection());
		String sql = "COPY "+destTable+" FROM STDIN  WITH DELIMITER '"+D+"' NULL 'null' ";
		log.print("Copy statement:"+sql);
		cpIN = cm.copyIn(sql);
		byte[] bytes = s.getBytes();
		cpIN.writeToCopy(bytes, 0, bytes.length);
		cpIN.endCopy();
		
	}	

	private static void extractResultIntoStringBuilder(ResultSet rs,
			StringBuilder sb, ColumnDefinition cd) throws SQLException {
		int cdcoltype = cd.getColType();
		
		switch (cdcoltype){
			
			//from DW: case Types.VARCHAR:sb.append("\""+rs.getString(cd.getColName())+"\"");break;
			case Types.VARCHAR:
			case Types.NVARCHAR:sb.append(Q+rs.getString(cd.getColName())+Q);break;
			
			case Types.INTEGER:
				int ival = rs.getInt(cd.getColName());
				if(rs.wasNull()){
					sb.append("null");
				}else{
					sb.append(ival);
				}
				break;
			case Types.BIGINT:
				long lval = rs.getLong(cd.getColName());
				if(rs.wasNull()){
					sb.append("null");
				}else{
					sb.append(lval);
				}
				break;
			case Types.DOUBLE:
			case Types.NUMERIC: 
			case Types.DECIMAL:
				double dval = rs.getDouble(cd.getColName());
				if(rs.wasNull()){
					sb.append("null");
				}else{
					sb.append(dval);
				}
				break;
			case Types.DATE:sb.append(Q+formatDate(rs.getDate(cd.getColName()))+"\"");break;
			
			
			case Types.ARRAY:sb.append(""+rs.getArray(cd.getColName())+"");break;
			case 1111: sb.append(""+rs.getString(cd.getColName())+"");break;
			
			//case Types.ARRAY Types.BIGINT, BINARY, BIT, BLOB, BOOLEAN, CHAR CLOB
			//DATALINK, DISTINCT,DOUBLE,FLOAT,JAVA_OBJECT,LONGVARCHAR,LONGNVARCHAR,LONGVARBINARY
			//NCHAR,NCLOB,NULL,OTHER,REAL,REF,REF_CURSOR,ROWID,SMALLINT,SQLXML,STRUCT,TIME,TIME_WITH_TIMEZONE,TIMESTAMP,TIMESTAMP_WITH_TIMEZONE
			//TINYINT,VARBINARY,VARCHAR
			default : sb.append("\""+rs.getString(cd.getColName())+"\"");break;
		}
	}			
	

	private static String formatDate(java.sql.Date inDate){
		String rv = "";
		if(inDate==null){
			rv = "null";
		}else{
			rv = sdf.format(inDate);
		}
		return rv;
	}	
	
	private static int getMagnitude(int countResult) {
		final int X = 20;//affects buffer/speed. 10 fast and hoggish, 100 slow and birdish
		final int F = 10;
		final int Y = X*F;
		
		int rv = X;
		if(countResult>Y){
			rv = (int)countResult/X;
		}
		return rv;
	}		
	
	public static String getPartitionFunction(int year, int upload){
		return "select \"valuequest\".\"create_partition_namespace\"("+year+","+upload+")";
	}
	
	public static String getDataSetInsert(int year, int upload){
		String rv = "";
		switch(upload){
			case 1: 
				rv = "insert into valuequest_"+year+"u1.data_set (cq_year,upload,incurred_start,incurred_end,paid,type)values("+year+",1,'"+year+"-01-01','"+year+"-12-31','"+(year+1)+"-02-28','CY')";
				break;
			case 2:
				rv = "insert into valuequest_"+year+"u2.data_set (cq_year,upload,incurred_start,incurred_end,paid,type)values("+year+",2,'"+year+"-07-01','"+(year+1)+"-06-30','"+(year+1)+"-08-31','MY')";
				break;
			default:
		}
		return rv;
	}
	
	public static List<String> getMarketDataSetUpdateStatements(){
		List<String> rv = new ArrayList<String>();
		rv.add("update valuequest.market m set m.data_type = 'A' where m.data_type = '\"A    \"';");
		rv.add("update valuequest.market m set m.data_type = 'H' where m.data_type = '\"H\"';");
		rv.add("update valuequest.market m set m.data_type = 'AP' where m.data_type = '\"AP   \"';");
		rv.add("update valuequest.market m set m.data_type = 'HP' where m.data_type = '\"HP   \"';");
		rv.add("update valuequest.market m set m.data_type = 'A' where m.data_type = '\"A\"';");
		rv.add("update valuequest.market m set m.data_type = 'H' where m.data_type = '\"H\"';");
		rv.add("update valuequest.market m set m.data_type = 'AP' where m.data_type = '\"AP\"';");
		rv.add("update valuequest.market m set m.data_type = 'HP' where m.data_type = '\"HP\"'      ;");
		return rv;
	}
	
	public static String getSchema(int year, int upload){
		return "valuequest_"+year+"u"+upload;
	}



	

	
	public static class Cli {
		
		@Override
		public String toString() {
			return "Cli [year=" + year + ", upload=" + upload + ", from="
					+ from + ", to=" + to + "]";
		}
		private static final String HELP = "help";
		private static final String TO = "to";
		private static final String FROM = "from";
		private static final String UPLOAD = "upload";
		private static final String YEAR = "year";
		
		private int year = 0;
		private int upload = 0;
		private Database from = Database.DW;
		private Database to = Database.DMFDEV;
		private boolean append = false;
		//private boolean all = false; TODO, use file or just do all tables flag.
		
		private String[] args = null;
		private Options options = new Options();
		public Cli(String[] args){
			this.args = args;
			options.addOption(HELP,HELP,false,"Show help.");
			options.addOption(YEAR,YEAR,true,"CQ year");
			options.addOption(UPLOAD,UPLOAD,true,"Upload no.");
			options.addOption(FROM,FROM,true,"From Source Server.");
			options.addOption(TO,TO,true,"To Target Server.");
			options.addOption("append","append",false,"Append, if present will not overwrite or clean.");
		}
		public Cli parse() {
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = null;
			boolean needshelp = false;
			try{
				cmd = parser.parse(options,args);
				needshelp = extractYearArg(cmd, needshelp);
				needshelp = extractUploadArg(cmd, needshelp);				
				needshelp = extractFromArg(cmd, needshelp);				
				needshelp = extractToArg(cmd, needshelp);
				if(cmd.hasOption("append")){
					append = true;
				}
				if(cmd.hasOption(HELP) || needshelp){
					help();
				}
			}catch(Exception e){
				help();
			}
			return this;
		}
		private boolean extractToArg(CommandLine cmd, boolean needshelp) {
			if(cmd.hasOption(TO)){
				String toName = cmd.getOptionValue(TO);
				if(Database.from(toName)==null){
					out.println("Command invalid. To database '"+toName+"' must be listed: "+Database.list());
					needshelp = true;
				}else{
					this.to = Database.from(toName);
				}
			}else{
				out.println("Command invalid. Database 't'/to option is required.");
				needshelp = true;				
			}
			return needshelp;
		}
		private boolean extractFromArg(CommandLine cmd, boolean needshelp) {
			if(cmd.hasOption(FROM)){
				String fromName = cmd.getOptionValue(FROM);
				if(Database.from(fromName)==null){
					out.println("Command invalid. From database '"+fromName+"' must be listed: "+Database.list());
					needshelp = true;;
				}else{
					this.from = Database.from(fromName);
				}
				
			}else{
				out.println("Command invalid. Database 'f'/from option is required.");
				needshelp = true;					
			}
			return needshelp;
		}
		private boolean extractUploadArg(CommandLine cmd, boolean needshelp) {
			if(cmd.hasOption(UPLOAD)){
				try{
					this.upload = Integer.parseInt(cmd.getOptionValue(UPLOAD));
					if(this.upload!=1 && this.upload!=2){
						out.println("Command invalid. Upload '"+this.upload+"' must be either 1 or 2. ");
						needshelp = true;
					}
				}catch(NumberFormatException nfe){
					out.println("Command invalid. Upload required to be a number.");
					needshelp = true;
				}
			}else{
				out.println("Command invalid. Upload is required.");
				needshelp = true;					
			}
			return needshelp;
		}
		private boolean extractYearArg(CommandLine cmd, boolean needshelp) {
			if(cmd.hasOption(YEAR)){
				try{
					this.year = Integer.parseInt(cmd.getOptionValue(YEAR));
					if(this.year<2000 || this.year >2999){
						out.println("Command invalid. Year '"+year+"' must be in the 2000 millenia.");
						needshelp = true;
					}
				}catch(NumberFormatException nfe){
					out.println("Command invalid. Year required to be a number.");
					needshelp = true;
				}
			}else{
				out.println("Command invalid. Year is required.");
				needshelp = true;				
			}
			return needshelp;
		}
		private void help(){
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("mrdeploy", options);
			System.exit(0);
		}
	}//Cli	
	
}




/*
 * Requirements:
 * 
 * Execute from command line. CLI with options.
 * 
 * Note: should be safe. 
 *   If the source is DW, then the schema convention is not prefixed. If the source is a PG one, then it is.
 *   If the partition doesn't exist, fail nicely.
 *   If the destination tables do not exist, fail nicely. 
 * 
 * Reindex
 * 
 * Perform both ETL and Deploy intelligently. 
 * 
 * 
 * Opt to be able to copy from DW to DM each and every time? (but only if the IDSProd is guaranteed to be locked down)
 * 		Every update should be tagged.
 * 
 * Progress.
 * 
 * Format: MRyyyymmdd_E1toE2.txt
 * 
 * Progress tracked.
 * Readable and Minimal Logging
 * 
 * Table and data set granularity. 
 * 
 * Automate manual steps/verify issues.
 * 
 * Automate partition creation
 * 
 */