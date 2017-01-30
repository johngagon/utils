package chp.datareceiving;

import java.util.Arrays;

public class RequiredRule extends ScanRule {

	int[] indexes;
	
	public RequiredRule(int[] iarr){
		super();
		this.indexes = iarr;
	}
	
	@Override
	public boolean check(String s) {

//		String[] fieldvalues = s.split("\\t");
//		for(int index:indexes){
//			String test = fieldvalues[index];
//			if(test.length()<1){
//				return false;
//			}
//		}
		return true;
	}
	

	@Override
	public String describe() {
		return "Required field cannot have zero length.";
				//+Arrays.toString(indexes);
	}
	

	@Override
	public boolean[] fieldCheck(String s) {
		String[] fieldvalues = s.split("\\t",-1);
		boolean[] rv = new boolean[indexes.length];
		int i=0;
		for(int index:indexes){
			if(index>=fieldvalues.length){
				System.out.println("!! Warning - split error "+fieldvalues.length+" with line '"+s+"'");
				rv[i] = false;
			}else{
				String test = fieldvalues[index];
				rv[i] = test.length()>0;
			}
			//if(!rv[i]){
			//	System.out.println("      Index("+i+"): ["+index+"] Test:'"+test+"' empty.");
			//}
			i++;
		}
		
		return rv;
	}

	@Override
	public boolean doFieldCheck() {
		return true;
	}

	@Override
	public int[] getIndexes() {
		return indexes;
	}

}
