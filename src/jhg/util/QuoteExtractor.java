package jhg.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
	public static List<String> extractQuotedText(String str){
		List<String> quotes = new ArrayList<String>();
		String regex = "\"([^\"]*)\"";
		
		Pattern pat = Pattern.compile(regex);
		Matcher m = pat.matcher(str);
		String q = "";
		while(m.find()) {
			q = m.group(1);
			quotes.add(q);
		}
		return quotes;
		
	}
	
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
	
	@SuppressWarnings("unused")
	private static void test1(){
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
	
	public static void main(String[] args){
	
		@SuppressWarnings("unused")
		String testString = "The quick brown fox jumps over the lazy dog but \"Now is the time.\" for all good men to \"come to their party\". If you know what I mean.";
		
		String testString2 = "\"The quick brown fox jumps over the lazy dog.\" he said \"Now is the time. for all good men to come to their party. If you know what I mean.\"";
		
		//QuoteExtractor qe = new QuoteExtractor();
		//qe.setText(testString);
		List<String> quotes = QuoteExtractor.extractQuotedText(testString2);
		for(String s: quotes){
			Log.println(s);
		}
		
	}

}
