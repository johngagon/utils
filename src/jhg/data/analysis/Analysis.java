package jhg.data.analysis;

import java.util.ArrayList;
import java.util.List;

import jhg.util.Log;

public class Analysis {

	//private Map<String,Integer> headerMap;
	private List<String[]> records;
	private String label;
	private List<Column> columns;
	Analysis(String name){
		super();
		this.label = name;
		this.records = new ArrayList<String[]>();
		//this.headerMap = new Hashtable<String,Integer>();
		this.columns = new ArrayList<Column>();
	}
	/*
	public void setHeader(Map<String, Integer> _headerMap) {
		this.headerMap = _headerMap;
	}
	public Map<String,Integer> getHeaderMap(){
		return this.headerMap;
	}
	*/

	public void addRecord(String[] record) {
		records.add(record);
	}
	public String[] getRow(int i){
		return records.get(i);
	}
	public List<String> getColumnValues(int i){
		if(i<0 || i>(columns.size()-1)){
			throw new IllegalArgumentException("i is out of index.");
		}
		List<String> col = new ArrayList<String>();
		for(String[] record:records){
			col.add(record[i]);
		}
		return col;
	}
	public int getRecordCount(){
		return records.size();
	}
	public int getColumnCount(){
		return columns.size();
	}
	public void report() {
		Log.println("Analysis:"+label);
		Log.println("Record Count:"+this.getRecordCount());
		Log.println("Column Count:"+this.getColumnCount());
		for(Column col:columns){
			Log.println(col.report());
		}
	}
	
	public static void main(String[] args){
		//Test.execute();
	}

	public void addColumn(Column c) {
		columns.add(c);
	}

	public Column getColumn(int i) {
		return columns.get(i);
	}
}
