package jhg.util;

public class TextUtil {

	public TextUtil() {
		
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
	
	public static void main(String[] args){
		String test1 = "Sally's";
		String test2 = "puppies'";
		
		String expected1 = "Sally";
		String expected2 = "puppies";
		String actual1 = truncPossessive(test1);
		String actual2 = truncPossessive(test2);
		Log.test(actual1.equals(expected1),"Fail 1:"+actual1,"Pass 1:"+actual1);
		Log.test(actual2.equals(expected2),"Fail 2:"+actual2,"Pass 2:"+actual2);
		
	
	}
}
