package jhg.util.book;

import java.util.ArrayList;
import java.util.List;

import chp.dbutil.Log;

public class Chapter {

	private int chapterNumber;
	private String title;
	private List<Scene> scenes;

	public Chapter(int i, String s) {
		Log.println("  Creating Chapter "+i);
		this.scenes = new ArrayList<Scene>();
		this.chapterNumber = i;
		this.title = s;
	}
	
	public List<Scene> getScenes() {
		return scenes;
	}

	public void addScene(Scene scene) {
		Log.println("    Adding scene "+scene.getSceneNumber());
		this.scenes.add(scene);
	}
	
	public int getSceneCount(){
		return this.scenes.size();
	}
	
	public int getChapterNumber() {
		return chapterNumber;
	}

	public String getTitle() {
		return title;
	}

	public void debug() {
		Log.println("\n\nCHAPTER "+title);
		for(Scene s:scenes){
			s.debug();
		}		
		
	}	
	
	public Sentence getLongestSentence(){
		Sentence longest = null;
		int currentLongest = 0;
		for(Scene scene:scenes){
			int candidateLongest = scene.getLongestSentence().length();
			if(candidateLongest > currentLongest){
				currentLongest = candidateLongest;
				longest = scene.getLongestSentence();
			}
		}
		return longest;
	}

	public String writeData() {
		StringBuffer buff = new StringBuffer();
		for(Scene s:scenes){
			buff.append(s.writeData());
		}	
		return buff.toString();
	}

	public int getParagraphCount() {
		int total = 0;
		List<Scene> scenes = getScenes();
		for(Scene s:scenes){
			total+=s.getParagraphCount();
		}
		return total;
	}
	
	public int getSentenceCount(){
		int total = 0;
		List<Scene> scenes = getScenes();
		for(Scene s:scenes){
			total+=s.getSentenceCount();
		}
		return total;		
	}
}
