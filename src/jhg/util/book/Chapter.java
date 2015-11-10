package jhg.util.book;

import java.util.*;

import chp.dbutil.Log;

public class Chapter {

	private int chapterNumber;
	private String title;
	private List<Scene> scenes;

	public Chapter(int i, String s) {
		this.scenes = new ArrayList<Scene>();
		this.chapterNumber = i;
		this.title = s;
	}
	
	public List<Scene> getScenes() {
		return scenes;
	}

	public void addScene(Scene scene) {
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
}
