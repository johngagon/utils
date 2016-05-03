package jhg.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PreprocessResult {

	private boolean isSuccessful;
	private List<String> messages;
	
	private String dataName;
	private int count;
	private Set<String> fields;
	private String[][] resultData;
	private int colcount;
	
	
	public PreprocessResult(){
		isSuccessful = true;
		messages = new ArrayList<String>();
		dataName = "";
		count = 0;
		fields = new TreeSet<String>();
		String[][] d = {{}};
		resultData = d;
		
	}

	public void initResultData(String dataName, int count, Set<String> fields, String[][] resultData){
		this.dataName = dataName;
		this.count = count;
		this.fields = fields;
		this.resultData = resultData;
		colcount = fields.size();
	}
	
	public boolean isSuccessful(){
		return this.isSuccessful;
	}
	
	public void addError(String string) {
		this.isSuccessful = false;
		messages.add(string);
	}

	public List<String> getMessages() {
		return messages;
	}

	public String getDataName() {
		return dataName;
	}

	public int getCount() {
		return count;
	}

	public Set<String> getFields() {
		return fields;
	}

	public String[][] getResultData() {
		return resultData;
	}

	public int getColcount() {
		return colcount;
	}
	


}
