package chp.dbreplicator;

import java.util.List;
import java.util.Vector;

public class Index {

	private String indexName;
	private String table;
	private String fieldList;
	private static final List<String> names;
	static{
		names = new Vector<String>();
		names.clear();
	}
	private static boolean isUnique(String name){
		return !names.contains(name);
	}
	public Index(String _indexName,String _table, String _fieldList) {
		this.indexName = _indexName;
		if(!isUnique(_indexName)){
			throw new IllegalArgumentException("Index "+_indexName+" is not unique.");
		}else{
			names.add(_indexName);
		}
		this.table = _table;
		this.fieldList = _fieldList;
	}
	public String sql(){
		String s = "CREATE INDEX "+indexName+" ON "+table+" ("+fieldList+")";
		return s;
	}
	public String getIndexName() {
		return indexName;
	}
	public String getTable() {
		return table;
	}
	public String getFieldList() {
		return fieldList;
	}
	
	@Override
	public String toString() {
		return "Index [indexName=" + indexName + ", table=" + table
				+ ", fieldList=" + fieldList + "]";
	}
	public static void main(String[] args){
		//test unique
		Log.pl("Start");
		Index a = new Index("a","a","a");
		Log.pl("Created "+a);
		Index b = new Index("b","a","a");
		Log.pl("Created "+b);
		Index c = new Index("c","a","a");
		Log.pl("Created "+c);
		Index d = new Index("c","a","a");//error here
		Log.pl("Created "+d);		
		Log.pl("End");
	}

}
