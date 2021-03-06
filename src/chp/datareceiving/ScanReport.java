package chp.datareceiving;

public class ScanReport {

	private boolean pass;
	private StringBuilder sb;
	private int totalErrors;
	private int errorMax;
	private boolean init;
	private boolean identified;
	
	//TODO add progress reporting, add a total lines scanned number.
	public ScanReport(){
		super();
		this.sb = new StringBuilder();
		this.pass = true;
		this.totalErrors = 0;
		this.errorMax = 100;//TODO finish - (default, use overload to set different later)
		this.identified = false;
	}
	public void identify(){
		this.identified = true;
	}
	
	public void recordFail(){
		pass = false;
		init = true;
		totalErrors ++;
	}
	
	public void recordFail(int lineNo, ScanRule rule) {
		if(totalErrors < errorMax){
			sb.append("Line "+lineNo+" failed to abide rule: "+rule.describe()+"\n");
		}
		pass = false;
		init = true;
		this.totalErrors++;
		
	}
	public void recordPass(){
		init = true;
	}
			
	public void recordFail(int lineNo, int i, String fieldName, ScanRule rule) {
		if(totalErrors < errorMax){
			String msg = "  Line "+lineNo+" and column "+fieldName+"("+i+") failed to abide rule: "+rule.describe()+"\n"; 
			System.out.print(msg);
			sb.append(msg);
		}
		pass = false;
		init = true;
		this.totalErrors++;
	}	
	
	
	public int getTotalErrors(){
		return this.totalErrors;
	}
	
	public boolean pass() {
		return pass;
	}

	public String detailString(){
		return "\n  Scan Report\n"+sb.toString()+"\n  Total Errors: "+this.totalErrors+"\n  End Scan";
	}
	
	public void print(){
		System.out.println("\n  Scan Report");
		String z = sb.toString();
		if(z.trim().length()>0){
			System.out.println(sb.toString());
		}
		
		System.out.println("  Total Errors: "+this.totalErrors);
		System.out.println("  End Scan");
	}
	public void printSummary(){
		System.out.println("File Scan Report -   Total Errors: "+this.totalErrors);
	}	
	/*
	 * How many rows
	 * How many fields?
	 * 
	 * Field count errors
	 * Blank line errors   ignored.   Ignore|Warn|Error|Fail
	 * 
	 * Nulls found in which columns (two delimiters next to each other or as first character in line or last character.)
	 */

	public String shortString() {
		if(init){
			if(identified){
				return (pass)?"ScanReport-PASS":"ScanReport-FAIL("+this.totalErrors+")";
			}else{
				return "Waiting for identification.";
			}
		}else{
			if(identified){
				return "Waiting for scan.";
			}else{
				return "Waiting for identification.";//FIXME promote branch out/unnest
			}
			
		}
	}

}
