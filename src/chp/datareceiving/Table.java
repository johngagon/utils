package chp.datareceiving;


public class Table {
	
	/*
	 * Variety of data formats for "tabular" data.
	 * 
	 * Pretty prints. This pads string1, int1, and string2 to 32, 10, and 16 characters, respectively.
	 * e.g.: System.out.format("%32s%10d%16s", string1, int1, string2);
	 * 
	 * List<String[]>
	 * List<Map<String,String>>
	 * String[][]
	 * ResultSet
	 * CSV parser
	 * JSON
	 * XML
	 * etc.
	 * 
	 * Figures out the max lengths.
	 * Gives basic stats.
	 * printable as a report with "header" etc.
	 * 
	 * 
	 */
	private String[][] data;
	
	public Table(String[][] d){
		if(!isValid(d)){
			throw new IllegalArgumentException(" String[][] d value was not valid.");
		}
		this.data = d;
	}
	
	private boolean isValid(String[][] d){
		if(d==null){
			return false;
		}
		if(d.length<1){
			return false;
		}
		int colLen = d[0].length;
		if(colLen<1){
			return false;
		}
		
		for(int i=0;i<d.length;i++){
			if(d[i].length!=colLen){
				return false;
			}
		}
	
		return true;
	}
	
	public void print(){
		System.out.print(formatted());
	}
	
	public String toHtmlTable(){
		StringBuilder sb = new StringBuilder();
		sb.append("<br/>");
		sb.append("<h3>Current File Upload Directory and Status</h3><br/>");
		sb.append("<table border=1>");

		for(int i=0;i<data.length;i++){
			sb.append("<tr>");
			String tag = (i==0)?"th":"td";
			String first = (i==1)?" bgcolor=\"#eeeeff\"":"";
			for(int j=0;j<data[i].length;j++){
				boolean hasLink = (j==0)&&(i!=0);
				String firstcolS = (hasLink)?"<a href=#>":"";//FIXME make generic to other than first.
				String firstcolE = (hasLink)?"</a>":"";
				sb.append("<"+tag+first+">");
				sb.append(firstcolS+(data[i][j])+firstcolE);
				sb.append("</"+tag+">");
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();		
	}
	
	public String formatted(){
		/*
		 * 1. validate (nulls, dimensions)
		 * 2. determine max length on each column
		 * 3. create format 
		 */
		StringBuilder sb = new StringBuilder();
		int[] colwidths = new int[data[0].length];
		for(int i=0;i<colwidths.length;i++){
			colwidths[i]=0;
		}
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[i].length;j++){
				if(data[i][j].length()>colwidths[j]){
					colwidths[j] = data[i][j].length();
				}
			}
		}
		
		String formatString = getFormatString(colwidths);
		for(int i=0;i<data.length;i++){
			sb.append(String.format(formatString,(Object[])data[i]));
		}		
		return sb.toString();
	}
	
	private static String getFormatString(int[] colwidths) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<colwidths.length;i++){
			//if(i==0){
			//	sb.append("|");
			//}
			sb.append("%"+colwidths[i]+"s");//+"|"
			if(i!=colwidths.length-1){
				sb.append("|");
			}
		}
		sb.append("\n");
		return sb.toString();
	}

	public static void main(String[] args){
		String[] header = {"id","name","address","city","state","zip","yearly_income","phone"};
		String[] row1 = {"1","John","123 Main St.","New Orleans","LA","22111","$200,000.00","(800)212-3343"};
		String[] row2 = {"2","Frank","423 Main St.","St. Louis","MO","33111","$150,000.00","(899)292-3343"};
		String[] row3 = {"3","Mark","523 Main St.","New Orleans","LA","44111","$250,000.00","(909)232-4333"};
		
		String[][] data = {header,row1,row2,row3};
		Table t = new Table(data);
		t.print();
	}
}
/*
 * 
Made table: 4r 8c

//for(int i=0;i<colwidths.length;i++){			Log.println(i+":"+colwidths[i]);		}
0:2
1:5
2:12
3:11
4:5
5:5
6:13
7:13

//Log.println("Format String: '"+formatString+"'");   
Format String: '|%2s|%5s|%12s|%11s|%5s|%5s|%13s|%13s|'

 */
