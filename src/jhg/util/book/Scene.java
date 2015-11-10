package jhg.util.book;

import java.util.*;

import chp.dbutil.Log;

public class Scene {

	private int sceneNumber;
	private Chapter chapter;
	private List<Paragraph> paragraphs;
	
	public Scene(Chapter c) {
		this.chapter = c;
		chapter.addScene(this);
		this.sceneNumber = chapter.getSceneCount() -1;
		this.paragraphs = new ArrayList<Paragraph>();
	}
	public Chapter getChapter() {
		return chapter;
	}
	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}
	public int getSceneNumber() {
		return sceneNumber;
	}
	public void addText(List<String> sceneParagraphs) {
		
	}
	public void debug() {
		Log.println("\n\nSCENE "+sceneNumber);
		for(Paragraph p:paragraphs){
			p.debug();
		}
	}
	public Sentence getLongestSentence() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
