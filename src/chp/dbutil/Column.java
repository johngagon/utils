package chp.dbutil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jhg.data.analysis.IncrementingInt;
import jhg.data.analysis.MapUtil;
import jhg.data.analysis.Type;
//import jhg.data.analysis.Column.Group;
import jhg.util.Log;

@SuppressWarnings("boxing")
public class Column implements Comparable<Column> {

	public static enum Group {
		UNIQUE("0-No Grouping. All unique"),
		NONE("1-Uniform single value/singleton group."),                   //Uniform, no pattern. 
		BISET("2-Two group."), 					//Two values
		SHORTSET("3-Small Number of Groups(4)."), 				//A good set splitter 4 in a group
		GROUP("4-Distributable count rank group set."), 					//A fair splittler
		FLAT("5-Flat group distribution."), 
		MYRIAD("6-No group pattern/likely near unique or quantity."), 				
		WEAK("7-Weak group pattern."),					//the most frequent, top count group is below 5%
		SMALL_SAMPLE("8-Short sample of less than 100 records. Groupings small.") 
		;
		private String desc;
		private Group(String d){
			this.desc = d;
		}
		public String description(){
			return this.desc;
		}
	}
	private Analysis analysis;
	private String name;
	private Integer index;
	private Type type;
	private Set<String> uniques;
	private List<String> values;
	private int min;
	private int max;
	private int textLength;
	private Group groupType;
	Map<String,IncrementingInt> uniqueCounts;
	private int nullCount;
	private int emptyCount;
	private Integer countMax;
	
	public Column(String n, int i){
		super();
		this.name = n;
		this.index = i;
		this.type = Type.NOT_CHECKED;
		this.min = -1;
		this.max = -1;
		this.textLength = -1;
		this.uniqueCounts = new HashMap<String,IncrementingInt>();
		this.nullCount = 0;
		this.emptyCount = 0;
		this.countMax = -1;
	}

	public String getName() {
		return name;
	}
	public int getCountMax(){
		return this.countMax;
	}
	public int getNullCount() {
		return this.nullCount;
	}
	
	public int getEmptyCount() {
		return this.emptyCount;
	}	

	public Integer getIndex() {
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
		
		for(String s:uniqueCounts.keySet()){
			IncrementingInt z = uniqueCounts.get(s);
			if(z.get()>countMax){
				countMax = z.get();
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

	public static String dataHeader(){
		return "columnName|index|type|uniqueCount|duplicateCount|textLength|min|max|nullCount|zeroOrEmptyCount|groupType|topRankedValue|topRankedCount\n";
	}
	public String dataRow(){
		final String D="|";
		StringBuilder sb = new StringBuilder();
		sb.append(this.name+D);
		sb.append(index+D);
		sb.append(type.name().toLowerCase()+D);
		sb.append(this.uniques.size()+D);
		sb.append((analysis.getRecordCount()-this.uniques.size())+D);
		sb.append(((this.textLength==-1)?"n/a":this.textLength)+D);
		sb.append(((this.min==-1)?"n/a":this.min)+D);
		sb.append(((this.max==-1)?"n/a":this.max)+D);
		sb.append(this.nullCount +D);
		sb.append(this.emptyCount+D);
		
		sb.append(this.groupType.description()+D);
		
		if(!Group.MYRIAD.equals(groupType) && !Group.WEAK.equals(groupType)){
			if(uniqueCounts.size()==0){
				this.generateCounts();
			}
			Map<String,IncrementingInt> sortedUnique = MapUtil.reverseSortByValue(uniqueCounts);
			boolean first = true;
			for(String s:sortedUnique.keySet()){
				if(first){
					sb.append(s+D);
					sb.append(this.countMax+D);
					first=false;
				}else{
					break;
				}
			}
		}
		sb.append("\n");
		return sb.toString();	
	}
	
	public String report() {
		StringBuilder sb = new StringBuilder();
		sb.append("Column["+index+"]: '"+this.name+"', ");
		sb.append("type: "+type.name().toLowerCase()+", ");
		sb.append("uniqueCount: "+this.uniques.size()+", ");
		sb.append("duplicateCount: "+(analysis.getRecordCount()-this.uniques.size())+", ");
		sb.append("textLength: "+((this.textLength==-1)?"n/a":this.textLength)+", ");
		sb.append("min: "+((this.min==-1)?"n/a":this.min)+", ");
		sb.append("max: "+((this.max==-1)?"n/a":this.max)+", ");
		sb.append("nullCount: "+this.nullCount +", ");
		sb.append("zeroOrEmptyCount: "+this.emptyCount+", ");
		
		sb.append("groupType:  "+this.groupType.description()+", ");
		final int PRINT_LIMIT = 10;
		final boolean PRINT_DETAIL = false;
		
		if(!Group.MYRIAD.equals(groupType) && !Group.WEAK.equals(groupType)){
			if(PRINT_DETAIL){
				sb.append("\nGroup Counts (Top "+PRINT_LIMIT+"):\n");
			}
			if(uniqueCounts.size()==0){
				this.generateCounts();
			}
			Map<String,IncrementingInt> sortedUnique = MapUtil.reverseSortByValue(uniqueCounts);
			
			int count = 1;
			for(String s:sortedUnique.keySet()){
				if(count==1 && !PRINT_DETAIL){
					
					sb.append("topRankedValue: "+s+"| ");
					sb.append("topRankedCount:  "+this.countMax+"| ");
				}
				if(PRINT_DETAIL){
					sb.append("\t#"+count+" \t"+s+":"+sortedUnique.get(s)+"\n");
				}
			
				if(count>=PRINT_LIMIT){
					break;
				}
				count++;
			}
		}
		return sb.toString();
		
	}

	public void incrementNullCount() {
		nullCount++;
	}
	public void incrementEmptyCount() {
		emptyCount++;
	}

	@Override
	public int compareTo(Column arg0) {
		return this.index.compareTo(arg0.index);
	}

	public void setAnalysis(Analysis a) {
		this.analysis = a;
		
	}
	
}
