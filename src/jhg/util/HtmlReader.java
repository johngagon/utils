package jhg.util;

import java.util.regex.*;
import java.util.*;

public class HtmlReader {

	public HtmlReader() {
		
	}
	private static final Pattern TAG_REGEX = Pattern.compile("<em>(.+?)</em>");
	
	public static void main(String[] args){
		String fileName = "data/dictionary_old.txt";
		String content = new TextFile(fileName).getText();
		List<String> contents = getTagValues(content);
		for(String s:contents){
			System.out.println(s);
		}
		//System.out.println(Arrays.toString(getTagValues(content).toArray()));
	}
	private static List<String> getTagValues(final String str) {
	    final List<String> tagValues = new ArrayList<String>();
	    final Matcher matcher = TAG_REGEX.matcher(str);
	    while (matcher.find()) {
	        tagValues.add(matcher.group(1));
	    }
	    return tagValues;
	}	
}
