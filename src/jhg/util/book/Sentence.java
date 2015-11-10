package jhg.util.book;

import jhg.util.Log;

public class Sentence {

	private static final String DELIM="\t";
	private static final String EOL = "";
	public static enum Type{
		NARRATIVE,
		DIALOG,
		DOCUMENT,
		UNANALYZED
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
		return this.getChapterNumber()+DELIM+this.getSceneNumber()+DELIM+this.getParagraphNumber()+DELIM+this.getSentenceNumber();
	}
	public void debug() {
		Log.println(getSentenceID()+DELIM+this.text+DELIM+this.text.length()+EOL);
		
	}

	public String writeData() {
		return getSentenceID()+DELIM+this.text+DELIM+this.text.length()+"\n";
	}
	
}
