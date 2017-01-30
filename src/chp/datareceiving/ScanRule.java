package chp.datareceiving;

public abstract class ScanRule {

	public abstract boolean check(String s);
	
	public abstract boolean[] fieldCheck(String s);

	public abstract String describe();

	public abstract boolean doFieldCheck();
	
	public abstract int[] getIndexes();
}
