package chp.dbreplicator.etl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.cli.*;

/*
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
*/
import chp.dbreplicator.Database;
import static java.lang.System.out;

public class MarketReportsDeploy {

	
	private static final String EXIT_FAIL_MSG = "Exit due to premature failure.";
	private static final String D = "\t";
	private static final String noq = "";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private static String Q = noq;	
	
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
	
	public static final String LOG_DIR = "K:\\Foundation\\ETLAndDeploymentLogs\\";

	public static final String CONFIG = "config.txt";

	public static void main(String[] args){
		Cli cli = new Cli(args).parse();
		out.println("CLI: "+cli.toString());
	}

	public static String getSchema(int year, int upload){
		return "";
	}


	public static String getFilename(String source, String dest){
		String U="_";
		SimpleDateFormat ymdFormatter = new SimpleDateFormat("yyyyMMdd");
		Date now = new Date();
		String ymd = ymdFormatter.format(now);
		return "MarketReports" + U + ymd + U + source + "to" + dest + ".txt";
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
		private boolean all = false;
		
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
				if(cmd.hasOption("append")){
					append = true;
				}				
				
				// TODO restrict from/to combinations., restrict years not created yet.
								
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