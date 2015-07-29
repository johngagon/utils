package jhg.util;

import java.util.*;
import java.util.regex.*;

public class QuoteExtractor {

	private String input;
	private List<String> output;
	
	public QuoteExtractor() {
		input = "";
		output = new ArrayList<String>();
	}
	public void setText(String text){
		this.input = text;
	}
	
	public void extract(){
		String regex = "\"([^\"]*)\"";
		Pattern pat = Pattern.compile(regex);
		Matcher m = pat.matcher(input);
		while(m.find()) {
		    output.add(m.group(1));
		}		
		
	}
	
	public List<String> results(){
		return output;
	}
	
	
	
	public static void main(String[] args){
	
		String testString = "The quick brown fox jumps over the lazy dog but \"Now is the time.\" for all good men to \"come to their party\". If you know what I mean.";
		
		//List<String> expected = new ArrayList<String>();
		//expected.add("Now is the time.");
		//expected.add("come to their party");  TODO add a compare
		
		QuoteExtractor qe = new QuoteExtractor();
		qe.setText(testString);
		qe.extract();
		List<String> actual = qe.results();
		for(String s:actual){
			Log.quoteln(s);
		}
		
	}

}
