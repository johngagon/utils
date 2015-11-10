package jhg.util.book;

import java.util.ArrayList;
import java.util.List;

import chp.dbutil.Log;

public class Book {

	private List<Chapter> chapters;
	private String title;
	
	public Book(String s) {
		Log.println("Creating Book.");
		chapters = new ArrayList<Chapter>();
		this.title = s;
	}
	public List<Chapter> getChapters() {
		return chapters;
	}
	public String getTitle() {
		return title;
	}
	public void debug() {
		Log.println("Debug...\n\nBOOK:"+title);
		for(Chapter c:chapters){
			c.debug();
		}
	}
	public void addChapter(Chapter c) {
		chapters.add(c);
	}
	
	public String writeData() {
		StringBuffer buff = new StringBuffer();
		for(Chapter c:chapters){
			buff.append(c.writeData());
		}
		return buff.toString();
	}

	public Sentence getLongestSentence(){
		Sentence longest = null;
		int currentLongest = 0;
		for(Chapter c:chapters){
			int candidateLongest = c.getLongestSentence().length();
			if(candidateLongest > currentLongest){
				currentLongest = candidateLongest;
				longest = c.getLongestSentence();
			}
		}
		return longest;
	}	

}
