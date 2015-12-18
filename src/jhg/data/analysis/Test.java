package jhg.data.analysis;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.collections4.MapUtils;

import java.nio.charset.Charset;

import jhg.util.*;

import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class Test {

	private static String readFile( File file ) throws IOException {
	    @SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader( new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }

	    return stringBuilder.toString();
	}
	

	public static void execute() {
		/*
		 * Give some csv data, introspect it. 
		 * 
		 * 
		 * Given a csv file with a header. (how it's parsed with or without quotes or delimited is up to parser) and records --
		 * 
		 * Questions:
		 * 
		 * a. What are the likely data types and data lengths? max and min?
		 * 
		 * b. Which columns repeat? which are likely flags or lookups?
		 *  
		 * c. Of the repeating groups, which sets are smallest(largest partitions)? What are the counts?
		 * d. Which ones are values/quanta?
		 * e. Which ones appear to be unique keys?
		 * f. Will it load into a particular table or particular object?
		 * 
		 * Correlations between groups and value fields.
		 * When one category is matrixed with another, does a third value field tend to change?
		 * If I want to limit my result, which combinations of criteria will fall within that limit?
		 * 
		 * What are particular aggregation sums, averages, counts, min and max values? std dev?
		 * What other statistical analysis and questions can be answered about this data?
		 * 
		 * Store all this information into an Analysis object that can be reported.
		 * 
		 * 
		 * 1. Read a CSV file.
		 * 2. Create a list of flexible business object.
		 * 3. Create sorted lists for indexing on groups.
		 * 4. Create a unique sorted set for the key if possible.
		 * 
		 */
		String[] files = {"AllstarFull",        "Appearances",  "AwardsManagers",  "AwardsPlayers",  "AwardsShareManagers",
				          "AwardsSharePlayers", "Batting",      "BattingPost",     "CollegePlaying", "Fielding",
				          "FieldingOF",         "FieldingPost", "HallOfFame",      "Managers",       "ManagersHalf",
				          "Master",             "Pitching",     "PitchingPost",    "Salaries",       "Schools",
				          "SeriesPost",         "Teams",        "TeamsFranchises", "TeamsHalf"
				          };
				//0-4, 5-9, 10-14, 15-19, 20,21,22,23
				//>1MB    [15]Master 2.4, [16]Pitching 3.7, [1]Appearances 5.8, [6]Batting 6.0, [9]Fielding 8.0
		String dir = "data/baseball/";
		String ext = ".csv";
		String nameFile = files[21];//"Batting";//"AllstarFull";
		String fileName = dir + nameFile + ext;
		File csvData = new File(fileName);
		
		CSVParser parser;
		try {
			//Log.println(readFile(csvData));
			CSVFormat format = CSVFormat.DEFAULT;
			format = format.withQuote(null);
			format = format.withHeader();
			parser = CSVParser.parse(csvData, Charset.defaultCharset(), format);
			
			//get headers and their positions.
			Map<String,Integer>headerMap = parser.getHeaderMap();
			//Log.println("headerMap null: "+(headerMap==null));
			//MapUtils.debugPrint(System.out, "allstar", headerMap);

			Analyzer analyzer = Analyzer.defaultInstance(nameFile);
			analyzer.setHeader(headerMap);
			for (CSVRecord csvRecord : parser) {
				String[] record = new String[headerMap.size()];
				for(Integer i:headerMap.values()){
					record[i] = csvRecord.get(i);
				}
				analyzer.addRecord(record);
			}
			if(analyzer.analyze()){
				Analysis a = analyzer.getAnalysis();
				a.report();
				//expect 4993 records, not 39944
			}else{
				analyzer.reportErrors();
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		execute();
	}
	/*
	 * everything should have basic logging and profiling and debugging enabled and progress reporting. 
	 * utilize basics.
	 */
	
}
