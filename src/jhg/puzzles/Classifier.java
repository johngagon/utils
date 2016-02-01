package jhg.puzzles;

public class Classifier {
	/*
	 * Naive: LETTERUNKNOWNNUMERAL 
	 * Expected: ditto  
	 * Observed:LETTERUNKNOWNNUMERAL 
	 * Answer:
	 * 
	 */

	public static void test(){
		System.out.println(
				classify('n')+classify('+')+classify('2'));
	}
	static String classify(char ch){
		if("0123456789".indexOf(ch)>=0)
			return "NUMERAL";
		if("abcdefghijklmnopqrstuvwxyz".indexOf(ch)>=0)
			return "LETTER";
/*
     (Operators not supported yet)
      	if("/+-*&|!=".indexOf(ch) >=0)
      		return "OPERATOR";
 */
		return "UNKNOWN";
	}
	
}
