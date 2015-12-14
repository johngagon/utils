package jhg.data.analysis;

import java.util.*;


import jhg.util.Log;

public class Column {

	public static enum Group {
		NONE,BISET,SHORTSET,GROUP,FLAT, SMALL_SAMPLE, MYRIAD
	}
	
	private String name;
	private int index;
	private Type type;
	private Set<String> uniques;
	private List<String> values;
	private int min;
	private int max;
	private int textLength;
	private Group groupType;
	Map<String,IncrementingInt> uniqueCounts;
	
	public Column(String n, int i){
		super();
		this.name = n;
		this.index = i;
		this.type = Type.NOT_CHECKED;
		this.min = -1;
		this.max = -1;
		this.textLength = -1;
		this.uniqueCounts = new HashMap<String,IncrementingInt>();
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

	public Group getGroupType() {
		return groupType;
	}

	public void setGroupType(Group groupType) {
		this.groupType = groupType;
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

	void generateCounts(){
		String[] vals = this.uniques.toArray(new String[uniques.size()]);
		for(String s:vals){
			uniqueCounts.put(s,new IncrementingInt());
		}
		for(String s:values){
			if(s!=null && !s.isEmpty()){
				IncrementingInt i = uniqueCounts.get(s);
				if(i==null){
					Log.println("Got null:'"+this.name+"'    '"+s+"'");
				}else{
					i.increment();
				}
			}
		}
	}
	
	public Map<String,IncrementingInt> getUniqueCounts(){
		return this.uniqueCounts;
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
		sb.append("unique values:"+this.uniques.size()+" ");
		sb.append("length: "+this.textLength+",  ");
		sb.append("min:  "+this.min+",  ");
		sb.append("max:  "+this.max+"  ");
		sb.append("Group Type:  "+this.groupType.name()+".  ");
		if(Group.GROUP.equals(groupType)){
			sb.append("\nGroup Counts:\n");
			//if(uniqueCounts.size()==0){
			//	this.generateCounts();
			//}
			Map<String,IncrementingInt> sortedUnique = MapUtil.reverseSortByValue(uniqueCounts);
			for(String s:sortedUnique.keySet()){
				sb.append("\t"+s+":"+sortedUnique.get(s)+"\n");	
			}
		}
		return sb.toString();
		
	}
	
	public static void main(String[] args){
		Test.execute();
	}	
}
