package jhg.util;

public class Util {

	/*
	 * Most commonly repeated code. (groovy like things)
	 * 
	 * join, split string
	 * 
	 * 
	 * 
	 */
	
	
	
	
	
	
	public static boolean func(String x, String y){
		return false;
	}
	
	public static void test(){
		String bad = "abcdefghijklmonpqrs";
		String good = "jgagon_2015";                      //no uppercase
		boolean pass1= (func("",bad) == false);
		boolean pass2= (func("",good)== true);
		Log.println(" Pass1:"+pass1);
		Log.println(" Pass2:"+pass2);
	}
	
	public static void main(String[] args){
		test();
	}		
	

}
