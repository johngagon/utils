package cache;

import java.util.*;

public class Table {

	//partioning (e.g.: cq_year)
	
	private String name;
	private int count;
	
	private List<Field> fields;
	private List<Field> desiredFields;
	private List<String> indexFields;//the main one.

	
	public Table(String name) {
		this.name = name;
		//this.pkField = pkField;
		fields = new ArrayList<Field>();
		desiredFields = new ArrayList<Field>();
		indexFields = new ArrayList<String>();
	}
	
	public Table(String tableName, String[] strings) {
		this.name = tableName;
		fields = new ArrayList<Field>();
		desiredFields = new ArrayList<Field>();
		indexFields = Arrays.asList(strings);
	}

	public void addField(Field f){
		
		fields.add(f);
	}
	

	public void setDesiredFields(List<Field> desired){
		this.desiredFields = desired;
	}
	
	public List<Field> getDesiredFields(){
		return this.desiredFields;
	}
	
	public String getName(){
		return this.name;
	}

	
	public int getDesiredColCount(){
		return this.desiredFields.size();//note: this is entire select column list.
	}
	
	public int getColCount(){
		return fields.size();
	}

	@Override
	public String toString() {
		return "Table [name=" + name  + "]";
	}

	public static List<Table> from(String[] tableList,Map<String, String[]> indexes) {
		List<Table> tables = new ArrayList<Table>();
		
		for(String tableName:tableList){
			Table t = new Table(tableName,indexes.get(tableName));
			tables.add(t);
		}
		return tables;
	}
	
	public String getIndexesAsCommaString(){
		String result = "";
		boolean first = true;
		for(String string : indexFields) {
		    if(first) {
		        result+=string;
		        first=false;
		    } else {
		        result+=","+string;
		    }
		}
		return result;
	}

	public String getFieldName(int colIndex) {
		Field f = fields.get(colIndex);
		return f.getColname();
	}

	public void setCount(int count2) {
		this.count = count2;
	}
	public int getCount(){
		return this.count;
	}
	
}
