package jhg.util;

import java.util.*;
import java.util.regex.*;


public class QuoteExtractor {
	private Pattern pattern;
	
	private String input;
	private Set<String> output;
	
	public QuoteExtractor() {
		input = "";
		output = new TreeSet<String>();
		this.pattern = Pattern.compile("[\\w']+");	
	}
	public void setText(String text){
		this.input = text;
	}
	/*
	public void extract(){
		String regex = "\"([^\"]*)\"";
		Pattern pat = Pattern.compile(regex);
		Matcher m = pat.matcher(input);
		while(m.find()) {
		    output.add(m.group(1));
		}		
		
	}
	*/
	public void extractWords(){
		String regex = "\"([^\"]*)\"";
		StringBuffer buff = new StringBuffer();
		Pattern pat = Pattern.compile(regex);
		Matcher m = pat.matcher(input);
		String q = "";
		while(m.find()) {
			q = m.group(1);
			Log.println("QUOTE:'"+q+"'");
		    buff.append(q);
		}
		String quoteText = buff.toString();
		
		Matcher mq = pattern.matcher(quoteText);
		String word = "";
		while ( mq.find() ) {
			word = quoteText.substring(mq.start(),mq.end());
			//word = TextUtil.truncPossessive(word);
			output.add(word.toLowerCase().trim());
			
	
			
			//if it's a proper name, put in proper names.
			//anything not in a spelling dictionary is either archaic or bad scan - have to add words to special dictionary for archaic.
			
		}		
		
		/*
		String regex = "\"([^\"]*)\"";
		Pattern pat = Pattern.compile(regex);
		Matcher m = pat.matcher(input);
		while(m.find()) {
		    String quote = m.group(1);
		    String[] words = quote.split(" ");
		    for(String w:words){
		    	output.add(w);
		    }
		}	
		*/		
	}
	
	public Set<String> results(){
		return output;
	}
	
	
	
	public static void main(String[] args){
	
		String testString = "The quick brown fox jumps over the lazy dog but \"Now is the time.\" for all good men to \"come to their party\". If you know what I mean.";
		
		//List<String> expected = new ArrayList<String>();
		//expected.add("Now is the time.");
		//expected.add("come to their party");  TODO add a compare
		
		QuoteExtractor qe = new QuoteExtractor();
		qe.setText(testString);
		qe.extractWords();
		Set<String> actual = qe.results();
		for(String s:actual){
			Log.quoteln(s);
		}
		
	}

}
