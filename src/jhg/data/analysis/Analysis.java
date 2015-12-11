package jhg.data.analysis;

import java.util.*;
import jhg.util.*;

public class Analysis {

	private Map<String,Integer> headerMap;
	private List<String[]> records;
	private String label;
	
	Analysis(String name){
		super();
		this.label = name;
		this.records = new ArrayList<String[]>();
		this.headerMap = new Hashtable<String,Integer>();
	}
	
	public void setHeader(Map<String, Integer> _headerMap) {
		this.headerMap = _headerMap;
	}
	public Map<String,Integer> getHeaderMap(){
		return this.headerMap;
	}

	public void addRecord(String[] record) {
		records.add(record);
	}

	public void report() {
		Log.println("Analysis:"+label);
		Log.println("Record Count:"+records.size());
		Log.println("Column Count:"+headerMap.size());
	}
	
	public static void main(String[] args){
		Test.execute();
	}
}
