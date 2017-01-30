package chp.datareceiving;

public class FieldCountRule extends ScanRule{

	private int fieldCount;
	
	public FieldCountRule(int i) {
		super();
		this.fieldCount = i;
	}

	@Override
	public boolean check(String s) {
		int actual = count(s,'\t');
		return (actual==fieldCount);
	}

	private static int count(String s, char token){
		int count = 0;
		for( int i=0; i<s.length(); i++ ) {
		    if( s.charAt(i) == token ) {
		        count++;
		    } 
		}
		return count+1;
	}

	@Override
	public String describe() {
		return "Field count expected to be "+fieldCount;
	}

	@Override
	public boolean[] fieldCheck(String s) {
		
		return new boolean[1];
	}	
	
	public boolean doFieldCheck(){
		return false;
	}

	@Override
	public int[] getIndexes() {
		return new int[1];
	}
	
}
