package jhg.puzzles;

import java.io.File;

public class MeToo {
	public static void test(){
		System.out.println(MeToo.class.getName().replaceAll("\\.",File.separator) + ".class");
		
		/*
		 * naive:			jhg\puzzles\MeToo.class
		 * expected & why:  ?	
		 * suggested fix:	?
		 * observed:		exception below						
		 * answer:			replaceAll second param is not literal string but a replacement text expression. the backslash is interpreted as escape.
		 *                  now guess fix: use double backslash. "\\"
		 * answer fix:      
		 * 
		 */
	}
}
/*
Exception in thread "main" java.lang.IllegalArgumentException: character to be escaped is missing
	at java.util.regex.Matcher.appendReplacement(Unknown Source)
	at java.util.regex.Matcher.replaceAll(Unknown Source)
	at java.lang.String.replaceAll(Unknown Source)
*/