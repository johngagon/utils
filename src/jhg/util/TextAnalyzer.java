package jhg.util;

import static java.lang.System.out;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 1. Take a text file and create a vocabulary file and an exceptions file (words that don't fit a particular regex)
 * 2. Compare two vocabularies and create a diff of:  In A, not in B. (e.g.: )
 * 		a. A has words too modern based on vocabulary B
 * 		b. B isn't using all the vocabulary found in A.  (A is basic english list, B is instructional text), the reverse says words that are too complex.
 * 
 */

public class TextAnalyzer {

	private Pattern pattern;
	private String content;
	
	private Set<String> vocab;
	private Set<String> unreadable;
	
	private String[] archaicList = {"thou","thee"};
	
	public TextAnalyzer(Pattern _p) {
		this.pattern = _p;
		this.vocab = new TreeSet<String>();
		this.unreadable = new TreeSet<String>();
	}
	public void read(String _content){
		this.content = _content;
	}
	public void analyze(){
		out.println("Analyzing.");
		
		Matcher m = pattern.matcher(content);
		String word = "";
		while ( m.find() ) {
			word = content.substring(m.start(),m.end());
			//use another regex on the word. if it's a number, put in numbers, 
			//if it's a proper name, put in proper names.
			//anything not in a spelling dictionary is either archaic or bad scan - have to add words to special dictionary for archaic.
			vocab.add(word);
		}
		out.println("Analzyed.");
	}
	
	public static void main(String[] args){
		String wordRegex = "[\\w']+";
		Pattern p = Pattern.compile(wordRegex);

		TextAnalyzer ta = new TextAnalyzer(p);
		//https://archive.org/stream/winthropsjourna05hosmgoog/winthropsjourna05hosmgoog_djvu.txt
		String fileName = "data/document.txt";
		String content = "not read";
		
		try {
			content = new String(readAllBytes(get("data/document.txt")));
			ta.read(content);
			ta.analyze();
			Set<String> vocab = ta.vocab;
			Set<String> unreadable = ta.unreadable;
			out.println("Vocab count:"+vocab.size());
			out.println("Unreadable count:"+unreadable.size());
			for(String v:vocab){
				out.println(v);
			}
			
			//for(String u:unreadable){
			//	out.println(u);
			//}			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		//out.println(content);
	}

}

/*
JLanguageTool langTool = new JLanguageTool(new English());
List<RuleMatch> matches = langTool.check("A sentence with a error in the Hitchhiker's Guide tot he Galaxy");
langTool.check()
for (RuleMatch match : matches) {
  System.out.println("Potential error at line " +
      match.getLine() + ", column " +
      match.getColumn() + ": " + match.getMessage());
  System.out.println("Suggested correction: " +
      match.getSuggestedReplacements());
}
http://wiki.languagetool.org/java-api#toc2
https://www.languagetool.org/development/
*/			
