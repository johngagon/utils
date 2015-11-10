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

	public String getAllDialogue() {
		StringBuffer buff = new StringBuffer();
		for(Chapter c:chapters){
			List<Scene> scenes = c.getScenes();
			for(Scene s:scenes){
				List<Paragraph> paragraphs = s.getParagraphs();
				for(Paragraph p:paragraphs){
					List<Sentence> sentences = p.getSentences();
					for(Sentence e:sentences){
						if(e.getDialogues().size()>0){
							buff.append(e.getSentenceID()+"\n");
						}
						List<Dialogue> dialogues = e.getDialogues();
						for(Dialogue d:dialogues){
							buff.append("  "+d.getText()+"\n");
						}
					}
				}
			}
			
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
	public int getSceneCount(){
		int total = 0;
		for(Chapter c:chapters){
			total += c.getSceneCount();
		}
		return total;
	}
	public int getParagraphCount(){
		int total = 0;
		for(Chapter c:chapters){
			List<Scene> scenes = c.getScenes();
			for(Scene s:scenes){
				total+=s.getParagraphCount();
			}
		}
		return total;
	}
	public int getSentenceCount(){
		int total = 0;
		for(Chapter c:chapters){
			List<Scene> scenes = c.getScenes();
			for(Scene s:scenes){
				total+=s.getSentenceCount();
			}
		}
		return total;
		
	}
	
	public void printStats(){
		Log.println(                "Book ["+this.title+"] :: Chapters : "+chapters.size()+"  Scenes : "+getSceneCount()+"  Paragraphs : "+getParagraphCount()+"  Sentences : "+getSentenceCount()
				);
		for(Chapter c:chapters){
			Log.println(            "    Chapter["+c.getChapterNumber()+"] :: Scenes : "+c.getSceneCount()+"  Paragraphs : "+c.getParagraphCount()+"  Sentences : "+c.getSentenceCount());
			
			List<Scene> scenes = c.getScenes();
			for(Scene scene:scenes){
				Log.println(        "        Scene["+scene.getSceneNumber()+"] :: Paragraphs : "+scene.getParagraphCount()+"  Sentences : "+scene.getSentenceCount());
				
				//for(Paragraph p:scene.getParagraphs()){
				//	Log.println(    "            Paragraph["+p.getParaNumber()+"] :: Sentences : "+p.getSentences().size());	
				//}
			}
			
		}
	}

	
}
