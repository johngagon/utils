package jhg.util.book;


import jhg.util.Log;
import jhg.util.TextFile;

import java.util.*;

@SuppressWarnings("unused")
public class NovelParser {

	
	private Book book;
	
	public NovelParser(String s) {
		book = new Book(s);
	}
	
	public void process(int chapterNo, String title, String[] chapterText){
		Log.println("Processing "+chapterNo);
		Chapter c = new Chapter(chapterNo,title);
		List<String> sceneParagraphs = new ArrayList<String>();
		
		for(String para : chapterText){
			if(para.trim().contains("***")){
				Scene newScene = new Scene(c);
				newScene.addText(sceneParagraphs);
				sceneParagraphs = new ArrayList<String>();
				c.addScene(newScene);
			}else{
				sceneParagraphs.add(para);
			}
			
			/*
			 * go through until "***" is encountered. with a buffer, pass to new scene.
			 */
		}
		
	}

	public void debugBook(){
		book.debug();
	}
	
	public static void main(String[] args){
		Log.println("Start");
		NovelParser np = new NovelParser("The Masks of Salem");
		
		String fileName = "data/document.txt";
		String[] content = new TextFile(fileName).getLines();
		
		np.process(10, "TEN", content);
		np.debugBook();
		/*
		 * Novel parser = looks for "***" for scenes.
		 * Stores data on each sentence.
		 */
		
		Log.println("End");
	}

}
