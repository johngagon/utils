package jhg.util.book;

import java.util.ArrayList;
import java.util.List;

import chp.dbutil.Log;

public class Book {

	private List<Chapter> chapters;
	private String title;
	
	public Book(String s) {
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
		Log.println("BOOK:"+title);
		for(Chapter c:chapters){
			c.debug();
		}
	}

	

}
