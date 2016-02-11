package jhg.validation;

import java.util.*;

/**
 * TODO : Use validation library.
 * 
 * @author jgagon
 *
 */
public class ContactPreprocessor extends Preprocessor {

	private static final String CONTACT = "contact";
	private static final String DEST_NAME = CONTACT;

	public ContactPreprocessor() {
		super();
	}

	private String dataName;
	private int count;
	private String[] fields;
	private String[][] data;
	
	private  Set<String> uniqueFields;
	private String[][] copy;
	
	private int colcount;
	
	public ContactPreprocessor(String dataName, int count, String[] fields,
			String[][] data) {
		this.dataName = dataName;
		this.count = count;
		this.fields = fields;
		this.colcount = fields.length;
		this.data = data;
		uniqueFields = new TreeSet<String>();
		
	}

	@Override
	public void execute() {
		validateInput();		//validate the basic input first.
		transformFields();
		copyData();				//always work off a copy
		transformData();
	}		
	

	protected void validateInput() {
		if(dataName==null){		report.addError("Data name is null.");		return;								}
		if(fields==null){		report.addError("Fields is null.");			return;								}
		if(data==null){			report.addError("Data is null.");			return;								}
		if(count<1){			report.addError("No record count.");		return;								}
		
		if(!CONTACT.equals(dataName)){
			report.addError("The name of the data assigned to this processor does not match.");
		}
		
		if(data.length!=count){	report.addError("Record count doesn't match actual count.");					}
		
		int firstRowLength = data[0].length;
		if(firstRowLength==0){
			report.addError("First row has no data.");
			return;
		}
		if(firstRowLength!=colcount){
			report.addError("First row number of fields does not match number of field names.");
		}
		for(int i=0;i<data.length;i++){
			String[] row = data[i];
			if(row.length!=firstRowLength){
				report.addError("Rows have uneven field counts. First row: "+firstRowLength+ "  current row ("+i+") : "+row.length+" .");
				break;
			}
		}
		List<String> temp = new ArrayList<String>();
		for(String f:fields){
			if(temp.contains(f)){
				report.addError("Field name "+f+" is duplicate.");
				break;
			}else{
				temp.add(f);
			}
		}
	}
	
	protected void transformFields() {
		for(String f:fields){
			uniqueFields.add(f);
		}
	}
	
	
	private void copyData(){
		this.copy = new String[count][colcount];
		for(int i=0; i<data.length; i++){
			for(int j=0; j<data[i].length; j++){
				copy[i][j]=data[i][j];
			}
		}
	}

	protected void cleanData(){
		//Cleans and converts formats, types etc.
		final String DEFAULT = "UNK";
		for(String[] sa:copy){
			for(String s:sa){
				if("".equals(s)){
					s = DEFAULT;
				}
			}
		}
	}

	protected void validateData(){
		for(int i=0;i<copy.length;i++){
			validateRow(i,copy[i]);
		}
	}
	
	
	protected void transformData(){
		//TODO implement more complex transformData....this is just a simple copy.
		String[][] newData = copy;
		int newcolcount = colcount;
		report.initResultData(DEST_NAME, newcolcount, uniqueFields, newData);
	}
	
	

	private void validateRow(int i, String[] row) {
		
		validateName(i,row[0]);
		validateBirthday(i,row[1]);
	}

	private void validateName(int i, String string) {
		if(string.length()>100){
			report.addError("("+i+"): Name too long (>100) - '"+string.substring(0,99)+"'");
		}
	}
	
	private void validateBirthday(int i, String string) {
		if(string.length()>100){
			report.addError("("+i+"): Bday too long (>100) - '"+string.substring(0,99)+"'");
		}
	}
	
	 
}


/*
 * 1. Create String[][] for data. String for type, integer for count, String[] for fieldnames.
 * 
 * 
 * 2. Initial search and replace routines (regex with replacement expressions), macros
 * 
 * 4. Validation task - (bad file) - types of issues:
 * 		a. field count mismatch/missing field. 
 *      b. bad record delimiter
 *      c. Try to deal with empty string as null for integer or required data.
 *      d. Try to parse these: double, integer, long, String, date, boolean.
 *      e. Try regex rules
 *      f. complex rules - one date comes before another (field comparison rule). (inter field comparison), if one field blank, another row full.
 *      g. complex rule - number in current row must be > number in previous row (inter row comparison)
 *      
 * 5. Transformation - mapping. 
 * 		
 *      new field - defaults, calculation, copy
 *      
 *      
 *      rename field - source name, target name    
 *      
 *      
 *      value mapping with rules, lookups
 *      (complex mapping - fields based on relationships.)
 *      
 *      ignore field
 *      
 *      Map<String,TransformRule> sourceFields, transformation rule.
 *      --new structure.
 *      
 *       
 *   Feature: 
 *     be able to validate, convert, transform in any order and any number of times.
 *     use field validator assists
 *     be able to make a variety of these. 
 *     
 *   Out of scope: 
 *     where the file comes from (e.g.: upload).
 *     	be able to identify file "data" type based on file naming convention or use of 2 header rows 
 *     csv parsing.   (see jhg.data.analysis).
 *     be able to do more than just csv.  
 */