package chp.dbutil;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Analysis {

	//private Map<String,Integer> headerMap;
	private List<String[]> records;
	private String label;
	private SortedSet<Column> columns;
	Analysis(String name){
		super();
		this.label = name;
		this.records = new ArrayList<String[]>();
		//this.headerMap = new Hashtable<String,Integer>();
		this.columns = new TreeSet<Column>();
	}
	/*
	public void setHeader(Map<String, Integer> _headerMap) {
		this.headerMap = _headerMap;
	}
	public Map<String,Integer> getHeaderMap(){
		return this.headerMap;
	}
	*/
	public Collection<Column> getColumns(){
		return this.columns;
	}
	public void addRecord(String[] record) {
		records.add(record);
	}
	public String[] getRow(int i){
		return records.get(i);
	}
	
	public List<String> getColumnValues(int i){
		if(i<1 || i>(columns.size())){
			throw new IllegalArgumentException("i is out of index.");
		}
		List<String> col = new ArrayList<String>();
		for(String[] record:records){
			col.add(record[i-1]);
		}
		return col;
	}
	
	public int getRecordCount(){
		return records.size();
	}
	public int getColumnCount(){
		return columns.size();
	}
	public String export(){
		StringBuilder sb = new StringBuilder();
		sb.append(Column.dataHeader());
		for(Column col:columns){
			sb.append(col.dataRow());
		}		
		return sb.toString();
	}
	
	
	public void report() {
		Log.println("Analysis:"+label);
		Log.println("Record Count:"+this.getRecordCount());
		Log.println("Column Count:"+this.getColumnCount());
		
		for(Column col:columns){
			Log.println(col.report());
		}
	}

	public void addColumn(Column c) {
		columns.add(c);
		c.setAnalysis(this);
	}

	public Column getColumn(Integer idx) {
		for(Column col:columns){
			if(col.getIndex().equals(idx)){
				return col;
			}
		}
		return null;
	}
}
