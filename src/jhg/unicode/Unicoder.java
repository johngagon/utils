package jhg.unicode;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import jhg.util.Log;
import jhg.util.TextFile;

public class Unicoder {

	
	public static final String printUnicodeFromText(String s){
		StringBuilder sb = new StringBuilder();
		char[] chars = s.toCharArray();
		for(char c:chars){
			 sb.append("\\u" + Integer.toHexString(c | 0x10000).substring(1));
		}
		return sb.toString();
	}
	public static final String printTextFromUnicode(String s){
		Properties p = new Properties();//property hack
		try {
			p.load(new StringReader("key="+s));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p.getProperty("key");		
	}
	
	public static final void testWithFile(){
		String fileName = "H:\\git\\market_reports\\grails-app\\services\\com\\chpinfo\\services\\ReportDataService.groovy";
		TextFile f = new TextFile(fileName);
		String content = f.getText();
		//String encoded = printUnicodeFromText(content);
		String decoded = printTextFromUnicode(content);
		System.out.println(decoded);
	}
	
	public static final void test(){
		String program = "public class Ugly { public static void main(String[] args){ System.out.println(\"Hello w\"+\"orld\");}}";
		String encoded = printUnicodeFromText(program);
		
		//console - use escape to prevent program "literal unicode".
		//String encoded = "\\u0070\\u0075\\u0062\\u006c\\u0069\\u0063\\u0020\\u0063\\u006c\\u0061\\u0073\\u0073\\u0020\\u0055\\u0067\\u006c\\u0079\\u0020\\u007b\\u0020\\u0070\\u0075\\u0062\\u006c\\u0069\\u0063\\u0020\\u0073\\u0074\\u0061\\u0074\\u0069\\u0063\\u0020\\u0076\\u006f\\u0069\\u0064\\u0020\\u006d\\u0061\\u0069\\u006e\\u0028\\u0053\\u0074\\u0072\\u0069\\u006e\\u0067\\u005b\\u005d\\u0020\\u0061\\u0072\\u0067\\u0073\\u0029\\u007b\\u0020\\u0053\\u0079\\u0073\\u0074\\u0065\\u006d\\u002e\\u006f\\u0075\\u0074\\u002e\\u0070\\u0072\\u0069\\u006e\\u0074\\u006c\\u006e\\u0028\\u0022\\u0048\\u0065\\u006c\\u006c\\u006f\\u0020\\u0077\\u0022\\u002b\\u0022\\u006f\\u0072\\u006c\\u0064\\u0022\\u0029\\u003b\\u007d\\u007d";
		
		System.out.println("Encoded:\n"+encoded);
		System.out.println(new String(new char[80]).replace("\0", "_"));
		System.out.println("Decoded:\n"+printTextFromUnicode(encoded));
	}
	
	public static void main(String[] args) {
		testWithFile();

	}

}
