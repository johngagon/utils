package jhg.util;

public class StringUtil {



	
	/*
	 * http://docs.oracle.com/javase/7/docs/api/java/lang/String.html
	 * 
	 * Idea: Go through api, come up with 
	 * Charsets:
	 * US-ASCII
	 * ISO-8859-1 
	 * UTF-8
	 * UTF-16BE
	 * UTF-16LE
	 * UTF-16
	 */
	
	public static String[] duplicateString(String s, int length){
		String[] rv = new String[length];
		for(int i=0;i<length;i++){
			rv[i]=s;
		}
		return rv;
	}
	
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
