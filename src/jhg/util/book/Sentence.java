package jhg.util.book;

import jhg.util.Log;

public class Sentence {

	public static enum Type{
		NARRATIVE,
		DIALOG,
		DOCUMENT
	}
	
	private int sentenceNumber;
	private Paragraph paragraph;
	private String text;
	private Type type;
	
	public Sentence(int n, Paragraph p, Type t, String s) {
		this.sentenceNumber = n;
		this.text = s;
		this.paragraph = p;
		this.type = t;
	}

	public Paragraph getParagraph() {
		return paragraph;
	}

	public String getText() {
		return text;
	}

	public Type getType() {
		return type;
	}

	public int length(){
		return this.text.length();
	}

	public int getSentenceNumber() {
		return sentenceNumber;
	}
	public int getParagraphNumber() {
		return this.paragraph.getParaNumber();
	}
	public int getSceneNumber(){
		return this.paragraph.getScene().getSceneNumber();
	}
	public int getChapterNumber(){
		return this.paragraph.getScene().getChapter().getChapterNumber();
	}
	private String getSentenceID(){
		return this.getChapterNumber()+":"+this.getSceneNumber()+":"+this.getParagraphNumber()+":"+this.getSentenceNumber();
	}
	public void debug() {
		Log.println(getSentenceID()+":"+this.text);
		
	}
	
}
