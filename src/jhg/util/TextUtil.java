package jhg.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class TextUtil {

	public TextUtil() {
		
	}

	/*
	 * Characters that match the regex are the ones left alone in the string. 
	 */
	public static final String filter(String in, String regex){
		Pattern pt = Pattern.compile(regex);
		Matcher match= pt.matcher(in);
		while(match.find()){
			String s= match.group();
	        in = in.replaceAll("\\"+s, "");
		}
		return in;
	}
	
	
	/*
	 * Characters that match the regex are taken out.
	 */
	public static final String filterOut(String in, String regex){
		//FIXME finish
		return in;
	}


	public static final String mask(String s, String r){
		return s.replaceAll("(?s).",r);		
	}
	
	public static String truncPossessive(String word){
		String out = word;
		String possessive1 = "'s";
		String possessive2 = "'";
		
		if(word.endsWith("'s")){
			StringBuilder b = new StringBuilder(word);
			b.replace(word.lastIndexOf("'s"), word.lastIndexOf("'s") + 2, "" );
			out = b.toString();			
			//out = word.replaceAll("'s","");
		}
		if(word.endsWith("'")){
			StringBuilder b = new StringBuilder(word);
			b.replace(word.lastIndexOf("'"), word.lastIndexOf("'") + 1, "" );
			out = b.toString();		
		}
		
		return out;
	}
	
	//@SuppressWarnings("unused")
	private static void testFilterInString(){
		Log.println("Testing filter.");
		String funny = "~@$#%$#@Mary's Funny Farm Pa((((((ncakes+__+++____ Inc.;;;";
		String expected = "Mary's Funny Farm Pancakes Inc.";
		String out = filter(funny,"[^a-zA-Z0-9 '.]");
		System.out.println(expected);
		System.out.println(out);
	}
	
	private static void testFilterOutString(){
		Log.println("Testing filter.");
		String funny = "~@$#%$#@Mary's Funny Farm Pa((((((ncakes+__+++____ Inc.;;;";
		String expected = "Mary's Funny Farm Pancakes Inc.";
		String out = filter(funny,"[~@$#%()_+;]");
		System.out.println(expected);
		System.out.println(out);
	}	
	
	public static void main(String[] args){
		//Log.println(mask("A supersecret 1234*password","*"));
		
		//testFilterInString();
		testFilterOutString();
	}
}
/*
String test1 = "Sally's";
String test2 = "puppies'";

String expected1 = "Sally";
String expected2 = "puppies";
String actual1 = truncPossessive(test1);
String actual2 = truncPossessive(test2);
Log.test(actual1.equals(expected1),"Fail 1:"+actual1,"Pass 1:"+actual1);
Log.test(actual2.equals(expected2),"Fail 2:"+actual2,"Pass 2:"+actual2);
*/
