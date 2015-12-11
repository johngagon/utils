package jhg.data.analysis;

import java.util.List;
import java.util.Set;

import jhg.util.Log;

public class Column {

	private String name;
	private int index;
	private Type type;
	private Set<String> uniques;
	private List<String> values;
	private int min;
	private int max;
	private int textLength;
	
	public Column(String n, int i){
		super();
		this.name = n;
		this.index = i;
		this.type = Type.NOT_CHECKED;
		this.min = -1;
		this.max = -1;
		this.textLength = -1;
	}

	public String getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type t) {
		this.type = t;
	}

	public Set<String> getUniques() {
		return uniques;
	}

	public void setUniques(Set<String> uniqueValues) {
		this.uniques = uniqueValues;
	}

	public void setValues(List<String> _colValues) {
		this.values = _colValues;
	}
	public boolean isUnique(){
		if(this.uniques!=null && this.values!=null){
			return this.uniques.size()==this.values.size();
		}else{
			throw new IllegalStateException("Values and Uniques not set.");
		}
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getTextLength() {
		return textLength;
	}

	public void setTextLength(int textLength) {
		this.textLength = textLength;
	}

	public String report() {
		StringBuilder sb = new StringBuilder();
		sb.append("Column["+index+"]:'"+this.name+"' ");
		sb.append("type:"+type.name().toLowerCase()+" ");
		sb.append("unique:"+this.uniques.size()+" ");
		sb.append("length: "+this.textLength+",  ");
		sb.append("min:  "+this.min+",  ");
		sb.append("max:  "+this.max+".  ");
		return sb.toString();
		
	}
	
	public static void main(String[] args){
		Test.execute();
	}	
}
