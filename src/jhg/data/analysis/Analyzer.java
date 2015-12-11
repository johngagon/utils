package jhg.data.analysis;

import java.util.*;

import jhg.util.Log;

public class Analyzer {

	public static final String[] TRUE_VALUES = {"true","TRUE","T","1"};
	public static final String[] FALSE_VALUES = {"false","FALSE","F","0"};
	
	private Analysis analysis;
	private List<String> errors;
	
	private Analyzer(String name){
		super();
		this.analysis = new Analysis(name);
		this.errors = new ArrayList<String>();
	}

	public void setHeader(Map<String,Integer> headerMap){
		for(String n:headerMap.keySet()){
			Column c = new Column(n,headerMap.get(n));
			analysis.addColumn(c);
		}
		//analysis.setHeader(headerMap);
	}
	
	public Analysis getAnalysis(){
		return this.analysis;
	}

	public static Analyzer defaultInstance(String name) {
		return new Analyzer(name);
	}

	public void addRecord(String[] record) {
		analysis.addRecord(record);
		
	}

	public boolean analyze() {
		
		int colCount = analysis.getColumnCount();
		for(int i=0;i<colCount;i++){
			analyzeColumnType(i);
			analyzeColumnExtremes(i);
		}
		/*
		 * TODO impl
		 * 
		 * 1. Analyze each column for data type.
		 * 	  first column
		 * 
		 * 
		 * Questions:
		 * 
		 * a. What are the likely data types and data lengths? max and min?   DONE
		 * 
		 * Validity of data - are there some values that are outliers in terms of type?
		 * 
		 * b. Which columns repeat? which are likely flags or lookups?  
		 *              which have the fewest unique values/most repetitions.
		 *              are the rows unique on their own?
		 *              e.g.:
		 *               lgID/GP tie for 2 value, gameNum:3
		 *               then startingPos-11, teamID-48, yearID-82, gameID-85.
		 *               finally, playerID-1708 out of 4993.
		 *               
		 *     are there "values" here? no.
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
		 * 3. Create sorted lists for indexing on groups.
		 * 4. Create a unique sorted set for the key if possible. - determine if there is a key and which columns are minimally required.
		 *  
		 * STATISTICS. 
		 *
		 * group questions.
		 * 
		 * 
		 * top counts in each group starting with the group that has the fewest unique values.
		 * 
		 * exclude two var and bool?
		 * 
		 * declare or determine a cut off for the count of uniques or. (10, no restrection except, not unique records, below square root). 
		 * 
		 * how many games did each team play? who played the most? the top counts.(also determine cutoff). outliers, std dev
		 * 
		 *  
		 * 
		 * 
		 */
		return true;
	}


	
	private void analyzeColumnType(int i) {

		List<String> colValues = analysis.getColumnValues(i);
		Set<String> uniqueValues = new HashSet<String>();
		Column col = analysis.getColumn(i);
		col.setUniques(uniqueValues);
		col.setValues(colValues);
		int countEmptyNull = 0;
		for(String s:colValues){
			if(s==null || s.isEmpty()){
				countEmptyNull ++;
			}else{
				uniqueValues.add(s);
			}
		}
		if(countEmptyNull==analysis.getRecordCount()){
			col.setType(Type.ALL_NULL);
		}else if(uniqueValues.size()==1){
			col.setType(Type.UNIFORM);
		}else if(uniqueValues.size()==2){
			String[] vals = (String[])uniqueValues.toArray(new String[uniqueValues.size()]);
			boolean isBool = false;
			for(int f=0;f<TRUE_VALUES.length;f++){
				String flag = TRUE_VALUES[f];
				if(vals[0].equals(flag) && vals[1].equals(FALSE_VALUES[f])){
					isBool = true;
					break;
				}else if(vals[1].equals(flag) && vals[0].equals(FALSE_VALUES[f])){
					isBool = true;
					break;
				}
			}
			if(isBool){
				col.setType(Type.BOOLVAL);
			}else{
				col.setType(Type.TWO_VALUE);
			}
			
		}else{
			String[] vals = uniqueValues.toArray(new String[uniqueValues.size()]);
			
			if(isAllInteger(vals)){
				col.setType(Type.NUMERICAL);
			}else if(isAllDecimal(vals)){  //TODO add support for a variety of formats.
				col.setType(Type.DECIMAL);
			}else if(isAllDate(vals)){
				col.setType(Type.DATE);//TODO add support for a variety of formats.
			}else{
				col.setType(Type.TEXT);
			}
			
		}
		
	}
	
	private void analyzeColumnExtremes(int index) {	
		Column col = analysis.getColumn(index);
		Type t = col.getType();
		Set<String> uniques = col.getUniques();
		
		if(Type.TEXT.equals(t)){
			
			int maxLength=1;
			Iterator<String> i = uniques.iterator();	
			String s = "";
			while(i.hasNext()){
				s = i.next();
				if(s.length()>maxLength){
					maxLength = s.length();
				}
			}
			col.setTextLength(maxLength);
		}else if(Type.NUMERICAL.equals(t)){
			Integer min = Integer.MAX_VALUE;
			Integer max = Integer.MIN_VALUE;
			Iterator<String> i = uniques.iterator();
			String s = "";
			while(i.hasNext()){
				s = i.next();
				Integer x = Integer.parseInt(s);
				if(x>max){
					max = x;
				}if(x<min){
					min = x;
				}
			}
			col.setMax(max);
			col.setMin(min);
		}
	}	

	public static boolean isAllDate(String[] vals){
		int dateCount = 0;
		for(String s:vals){
			if(isDate(s)){
				dateCount++;
			}
		}
		return dateCount==vals.length;
	}	
	
	
	public static boolean isAllDecimal(String[] vals){
		int decimalCount = 0;
		for(String s:vals){
			if(isDecimal(s)){
				decimalCount++;
			}
		}
		return decimalCount==vals.length;
	}	
	
	public static boolean isAllInteger(String[] vals){
		int integerCount = 0;
		for(String s:vals){
			if(isInteger(s)){
				integerCount++;
			}
		}
		return integerCount==vals.length;
	}
	
	public static boolean isInteger(String s){
		try{
			Integer.parseInt(s);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	public static boolean isDecimal(String s){
		try{
			Double.parseDouble(s);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	public static boolean isDate(String s){
		return false;//FIXME impl
	}
	
	public void reportErrors() {
		for(String s:errors){
			Log.println(s);
		}
	}
	
	public static void main(String[] args){
		Test.execute();
	}
	
	
}
