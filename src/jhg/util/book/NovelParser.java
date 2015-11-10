package jhg.util.book;


import jhg.util.Log;
import jhg.util.TextFile;

import java.text.BreakIterator;
import java.util.*;

@SuppressWarnings("unused")
public class NovelParser {

	
	private Book book;
	
	public NovelParser(String s) {
		book = new Book(s);
	}
	
	public void processChapter(int chapterNo, String title, String[] chapterText){
		Log.println("\n\nProcessing Ch. "+chapterNo+" with "+chapterText.length+" paragraphs.");
		Chapter c = new Chapter(chapterNo,title);
		List<String> sceneParagraphs = new ArrayList<String>();
		int paranum = 0;
		int qpara = chapterText.length;
		for(String para : chapterText){
			//Log.println("PARA:"+para);
			if(para.trim().contains("***") || paranum==(qpara-1) ){
				Scene newScene = new Scene(c);
				newScene.addParagraphs(sceneParagraphs);
				sceneParagraphs = new ArrayList<String>();
				c.addScene(newScene);
			}else{
				sceneParagraphs.add(para.trim());

			}
			
			paranum++;
			/*
			 * go through until "***" is encountered. with a buffer, pass to new scene.
			 */
		}
		book.addChapter(c);
		
	}

	public void debugBook(){
		book.debug();
	}
	

	

	private void writeData(String filename) {
		if(TextFile.write(filename, book.writeData())){
			Log.println("\n\nWrote book out to "+filename);
		}else{
			Log.println("\n\nBook write data failed.");
		}
	}
	
	private void writeDialogue(String filename) {
		if(TextFile.write(filename, book.getAllDialogue())){
			Log.println("\n\nWrote dialogue out to "+filename);
		}else{
			Log.println("\n\nBook write dialogue failed.");
		}
	}

	
	public Sentence getLongestSentence(){
		return book.getLongestSentence();
	}
	
	public void printStats(){
		Log.println("\n\n");
		book.printStats();
	}
	
	public static void main(String[] args){
		Log.println("Start");
		//testParse();
		
		NovelParser np = new NovelParser("The Masks of Salem");
		for(int i=1;i<=9;i++){
			String fileName = "data/chapters/ch0"+i+".txt";
			String[] content = new TextFile(fileName).getLines();
			//debugContent()
			np.processChapter(i, ""+i, content);
		}
		String fileName = "data/chapters/ch10.txt";
		String[] content = new TextFile(fileName).getLines();
		np.processChapter(10, ""+10, content);
		/*
		Print to console.
		np.debugBook();

		Write data
		np.writeData("data/chapters/book.dat");
		
		Get longest sentence
		Log.println("\nLongest Sentence"+np.getLongestSentence().writeData());
		
		np.printStats();
		*/
		
		//FIXME broken np.writeDialogue("data/chapters/dialogue.txt");
		
		
		/*
		 * Novel parser = looks for "***" for scenes.
		 * Stores data on each sentence.
		 * identifies dialogue
		 * 
		 * TASKS
		 * 
		 * 1. Length analysis: 
		 *   a. Book 
		 *   b. Chapter
		 *   c. Scene
		 *   d. Para
		 *   e. Sentence
		 *   
		 * 2. Dialogue analysis
		 * 3. Unused words
		 * 4. Blah color finder
		 * 5. Blah words, blah patterns.
		 * 
		 * !Compile - List paragraphs with sentence spacing.
		 * !Generate - Produce a record list of sentences. allow marking some as document type. 
		 *  
		 *  Top longest sentences
		 */
		
		Log.println("End");
	}
	
	
	
	
	public static void testParse(){
		BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
		String source = "This is a test. This is a T.L.A. test.  Now with a Dr. in it. \"Now with quotest in it,\" he said. ";
		iterator.setText(source);
		int start = iterator.first();
		for (int end = iterator.next();
		    end != BreakIterator.DONE;
		    start = end, end = iterator.next()) {
		  System.out.println("|"+source.substring(start,end)+"|");
		}		
	}

}
/* debugContent
for(String s:content){
	Log.print(s);
}
*/