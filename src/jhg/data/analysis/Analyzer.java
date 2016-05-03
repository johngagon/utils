package jhg.data.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jhg.data.analysis.Column.Group;
import jhg.util.Log;

@SuppressWarnings("boxing")
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
			analyzeColumnGroups(i);
		}
		/*
		 * TODO impl
		 * 
		 * 1. Analyze each column for data type.
		 * 	  first column
		 * 
		 * 
		 * Statistics: groupings. pick a number: top 10 then a threshold on non-uniques.
		 * 
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
	


	
	private void analyzeColumnGroups(int i) {
		final int SHORTSET_THRESHOLD = 4;
		
		Column col = analysis.getColumn(i);
		if(Type.ALL_NULL.equals(col.getType())){
			col.setGroupType(Group.NONE);
		}else if(Type.UNIFORM.equals(col.getType())){
			col.setGroupType(Group.NONE);
		}else if(Type.BOOLVAL.equals(col.getType()) || Type.TWO_VALUE.equals(col.getType())){
			col.setGroupType(Group.BISET);
		}else{
			int groupCount = col.getUniques().size();
			if(groupCount<SHORTSET_THRESHOLD){                                             //TODO extract CONFIG RULE
				col.setGroupType(Group.SHORTSET);
			}else{
				if(analysis.getRecordCount()>=100){
					//final double root = Math.sqrt(analysis.getRecordCount()+0.0);               //TODO extract rule
					
					final double tenth = analysis.getRecordCount()/10;
					final double LIMIT = tenth;
					//Log.println("LIMIT:"+limit);
					if(groupCount<LIMIT){
						col.generateCounts();
						Map<String,IncrementingInt> groupCounts = col.getUniqueCounts();
						final double TOP = 0.05;//1; //10%
						//final double TAPER_POINT = 0.01;
						double halfway = groupCount/2;
						//Log.println("halfway: "+halfway);
						Map<String,IncrementingInt> sortedGroupCounts = MapUtil.reverseSortByValue(groupCounts);
						//not truly sorted.FIXME
						boolean notFlat = false;
						double topPercent = 0.0;
						for(String s:sortedGroupCounts.keySet()){

							IncrementingInt topCountII = sortedGroupCounts.get(s);
							Integer topCount = topCountII.get();
							topPercent = (double)topCount/analysis.getRecordCount();
							notFlat = topPercent>TOP;
							break;//we only want the first value.
						}//break
						int count=0;
						if(notFlat){
							for(String s:sortedGroupCounts.keySet()){
								if(count>halfway-2 && count < halfway+2){
									IncrementingInt medianGroupCountII = sortedGroupCounts.get(s);
									Integer medianGroupCount= medianGroupCountII.get();
									double medianPercent = (double)medianGroupCount/analysis.getRecordCount();
									notFlat = (topPercent - medianPercent) > (TOP/2);
									break;
								}
								count++;
							}
							if(notFlat){
								col.setGroupType(Group.GROUP);
							}else{
								col.setGroupType(Group.FLAT);
							}							
						}else{
							col.setGroupType(Group.WEAK);//the most frequent, top count group is below 5% of all.an issue with myriad groups (which may be possible data, hard to determine if integer. if value is decimal, can determine based on that
						}

/*
 * Output wrong: FIXME flat issue is it flat because top value is < 5% of all counts? (probably).
Column[1]:'yearID' type:numerical unique values:82 length: -1,  min:  1933,  max:  2014  Group Type:  FLAT.  
Group Counts:
	1960:122
	1959:114
	1962:111
	1961:107
 */
						
					}else{
						col.setGroupType(Group.MYRIAD);
					}
				}else{
					col.setGroupType(Group.SMALL_SAMPLE);
				}
			}
		}
	}
/*
Data can have groupings where the top percent is a factor greater (twice)
as much as the smallest or it might be relatively unvaried. Group counts 
form a labeled quantity set that can be represented as a pie or stack bar.
This double factor is only true when the max >=20 (twice 10). Otherwise, the 
counts are merely an even distribution instead of grouping.

Groups that number under a threshold 4000->200 of SQRT for sets >100 are proper
groups. Other groups could be real groups but are not significant in terms of data.
It should also be over the threshold of >=5. flags do not form groups, use 
something other than pie for those. (2 bars or a single stack bar is better)



Go through all sets. Label if group, shortset, distribution or binary.
Go through the groups and do "other".
For the binary case render one way
For the shortset, do a stackbar
For the group, do a pie.
For distributions, do average then curve.

 */

/*
OTHER:

	The "Other" group should always be both <20% when percents of the others total
AND smaller than the largest piece. If the largest piece is 10%, then Other should
be less than that. 

The smallest piece of the pie should be no less than 5%, otherwise, it's a distribution.

 */
/*

ResultSet

ResultSet -> get value based on the meta data for the type.

Option to cast the type.

 */
/*


When a count is > 9999, then use K and M abbreviations.	
 */
	
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
		//Test.execute();
	}
	
	
}
