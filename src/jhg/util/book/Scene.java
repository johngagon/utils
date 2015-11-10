package jhg.util.book;

import java.util.*;

import chp.dbutil.Log;

public class Scene {

	private int sceneNumber;
	private Chapter chapter;
	private List<Paragraph> paragraphs;
	
	public Scene(Chapter c) {
		this.chapter = c;
		this.sceneNumber = chapter.getSceneCount() +1;
		this.paragraphs = new ArrayList<Paragraph>();
		Log.println("    Created scene "+sceneNumber);
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
	public void addParagraphs(List<String> sceneParagraphs) {
		Log.println("      Adding "+sceneParagraphs.size()+" paragraphs.");
		int i=1;
		for(String s:sceneParagraphs){
			Paragraph p = new Paragraph(i,this);//(int num, Scene s)
			
			p.addSentences(s);
			if(p!=null){
				paragraphs.add(p);
			}
			i++;
		}
		//Log.println("");
	}
	public void debug() {
		//Log.println("\n\nSCENE "+sceneNumber);
		for(Paragraph p:paragraphs){
			p.debug();
		}
	}
	public Sentence getLongestSentence() {
		int topLength = 0;
		Sentence topSentence = null;
		for(Paragraph p:paragraphs){
			if(topSentence==null){
				topSentence = p.getLongestSentence();
			}
			if(p.hasSentences()){
				Sentence candidateSentence = p.getLongestSentence();
				if(candidateSentence.length()>topLength){
					topLength = p.getLongestSentence().length();
					topSentence = p.getLongestSentence();
				}
			}
		}
		return topSentence;
	}
	
	public String writeData() {
		StringBuffer buff = new StringBuffer();
		for(Paragraph p:paragraphs){
			buff.append(p.writeData());
		}
		return buff.toString();
	}

	
	
}
