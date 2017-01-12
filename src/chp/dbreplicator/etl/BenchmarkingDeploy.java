package chp.dbreplicator.etl;

import static java.lang.System.out;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import chp.dbreplicator.ColumnDefinition;
import chp.dbreplicator.Database;
import chp.dbreplicator.DatabaseManager;
import chp.dbreplicator.Index;
import chp.dbreplicator.ProgressReporter;
import chp.dbreplicator.Rdbms;
import chp.dbreplicator.SimpleProgressListener;
import chp.dbreplicator.ProgressReporter.Marker;


public class BenchmarkingDeploy {
	
	private static final String EXIT_FAIL_MSG = "Exit due to premature failure.";
	private static final String D = "\t";
	private static final String noq = "";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private static String Q = noq;	
	

	
	public static final String LOG_DIR = "C:\\deploy\\logs\\";//"K:\\Foundation\\ETLAndDeploymentLogs\\";

	public static final String CONFIG = "bm_table_config.txt";

	public static void main(String[] args){
		Cli cli = new Cli(args).parse();
		transfer(!cli.append,cli.from,cli.to,cli.dataset);
	}
	public static String getFilename(String source, String dest, int dataset){
		String U="_";
		SimpleDateFormat ymdFormatter = new SimpleDateFormat("yyyyMMdd");
		Date now = new Date();
		String ymd = ymdFormatter.format(now);
		return "Benchmarking" + U + ymd + U + source + "to" + dest + "_"+dataset+".txt";
	}
	
	private static void transfer(boolean cleanTarget,Database source, Database target, int dataset){
		String filename = LOG_DIR + getFilename(source.name(),target.name(),dataset);
		TextLog log = new TextLog(filename);
		log.print("Starting Copy of "+source.name()+" to "+target.name()+" with "+CONFIG+" on "+new java.util.Date());
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
		
		TextFile f = new TextFile(CONFIG);
		Map<String,String> viewTableMapping = f.getMapping();

		List<String> l = new ArrayList<String>(viewTableMapping.keySet());

		Collections.reverse(l);
		final String SCHEMA = "benchmarking.";
		//Work estimation for progress and clean
		int totalCount = 0;
		for(String sourceRelation:l){
			//System.out.println("SourceRelation:"+sourceRelation);
			String sourceTable = "";
			
			sourceTable = SCHEMA+sourceRelation;
			
			String destTable = SCHEMA+viewTableMapping.get(sourceRelation);
			
			if(!"benchmarking.request_log".equals(destTable)){
				String deleteByDataSetDML = "";
				if("benchmarking.data_sets".equals(destTable)){
					if(dataset!=0){
						deleteByDataSetDML = "delete from "+destTable+" where id = "+dataset;
					}else{
						deleteByDataSetDML = "delete from "+destTable+"";
					}
				}else{
					 deleteByDataSetDML = "delete from "+destTable+getDataSetWhere(dataset);
				}
				log.print("Delete:"+deleteByDataSetDML);
				targetDatabase.execute(deleteByDataSetDML);					
			}
			
			totalCount += getTableCount(log, sourceDatabase,sourceTable, dataset);

		}
		log.print("\n ");
		
		ProgressReporter pr = new ProgressReporter(totalCount,Marker.TICKS);
		pr.addListener(new SimpleProgressListener());
		
		//Perform transfer
		for(String sourceRelation:viewTableMapping.keySet()){
			String sourceTable = "";
			sourceTable = SCHEMA+sourceRelation;
			String destTable = SCHEMA+viewTableMapping.get(sourceRelation);

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

			

			if(!"benchmarking.request_log".equals(sourceRelation)){
				performCopy(log, pr, sourceDatabase, targetDatabase, sourceTable, destTable, dataset);
			}
			
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
		

		sourceDatabase.close();
		targetDatabase.close();
		
		log.print("\n\nFinished on "+new java.util.Date()+"!");			
	}		
	
	public static String getDataSetWhere(int dataset){
		return ( (dataset==0)?"":(" where data_set = "+dataset) );
	}
	
	private static int getTableCount(TextLog log, DatabaseManager database,String sourceRelation, int dataset){
		log.print("\n\nStarting Count of "+database.getDatabase().name()+" for table:"+sourceRelation);
		int count = 0;
		
		if(  sourceRelation.indexOf("data")!=-1  &&  (!"benchmarking.data_sets".equals(sourceRelation))  &&  (!"benchmarking.request_log".equals(sourceRelation))  ){
			String sql = "select count(*) from "+sourceRelation+getDataSetWhere(dataset);
			database.query(sql);
			count = database.getCountResult();
			log.print("\n\nFinished Count of "+database.getDatabase().name()+"-"+sourceRelation+":"+count+"\n");
		}else{
			log.print("\n\nNot Counting records of "+database.getDatabase().name()+"-"+sourceRelation+"\n");
		}

		return count;
	}	
	

	
	
	private static void performCopy(TextLog log, ProgressReporter pr, DatabaseManager sourceDatabase, DatabaseManager targetDatabase,
			String sourceRelation, String destTable, int dataset) {
		
		
		List<ColumnDefinition> cds = sourceDatabase.getColumnDefsFromDbMeta(sourceRelation);

		String countQuery = "";
		
		if("benchmarking.data_sets".equals(sourceRelation)){
			countQuery = "select count(*) from "+sourceRelation + " where id = "+dataset;
		}else{
			countQuery = "select count(*) from "+sourceRelation + getDataSetWhere(dataset);
		}	
		
		
		sourceDatabase.query(countQuery);
		int countResult = sourceDatabase.getCountResult();
		log.print("Source Result Size:"+countResult);
		int max = getMagnitude(countResult);
		
		String query = "";
		if("benchmarking.data_sets".equals(sourceRelation)){
			query = "select * from "+sourceRelation + " where id = "+dataset;
		}else{
			query = "select * from "+sourceRelation + getDataSetWhere(dataset);
		}		
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
			System.out.println("\nSource query: "+query);
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
	

	

	




	

	
	public static class Cli {
		
		@Override
		public String toString() {
			return "Cli [dataset=" + dataset  + ", from="
					+ from + ", to=" + to + "]";
		}
		private static final String HELP = "help";
		private static final String TO = "to";
		private static final String FROM = "from";
		private static final String DATASET = "dataset";
		private static final String YEAR = "year";
		
		private int dataset = 0;
		
		private Database from = Database.DW;
		private Database to = Database.DMDEVNEW;
		private boolean append = false;
		//private boolean all = false; TODO, use file or just do all tables flag.
		
		private String[] args = null;
		private Options options = new Options();
		public Cli(String[] args){
			this.args = args;
			options.addOption(HELP,HELP,false,"Show help.");
			options.addOption(DATASET,DATASET,true,"Data Set");
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
				needshelp = extractDatasetArg(cmd, needshelp);
								
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

		private boolean extractDatasetArg(CommandLine cmd, boolean needshelp) {
			if(cmd.hasOption(DATASET)){
				try{
					this.dataset = Integer.parseInt(cmd.getOptionValue(DATASET));
				}catch(NumberFormatException nfe){
					out.println("Command invalid. dataset required to be a number.");
					needshelp = true;
				}
			}else{
				out.println("Command invalid. Dataset is required.");
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