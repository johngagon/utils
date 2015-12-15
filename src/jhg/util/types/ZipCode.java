package jhg.util.types;

import jhg.util.Log;

public class ZipCode extends Text {

	public static ZipCode defaultInstance = new ZipCode("00000-0000");
	
	public static boolean isValid(String toTest) {
		return isValid(defaultInstance,toTest);
	}
	
	
	public ZipCode(String v) {
		super(v,"^\\d{5}(?:[-\\s]\\d{4})?$",5,10);
		
	}
	public static void main(String[] args){
		ZipCode zip = new ZipCode("84111-4444");
		//ZipCode zip2 = new ZipCode("111");
		Log.println("Zip:"+zip);
		String testCode = "3333";
		boolean isValid = ZipCode.isValid(testCode);
		Log.println("Valid: '"+testCode+"' :"+isValid);
	}
	
	
	
}
/*
 * Time, Place, zip,census,
 * Book id, URL
 * EIN
 * 
 * 
 */
