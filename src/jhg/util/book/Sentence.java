package jhg.util.book;

import java.util.*;

import jhg.util.Log;
import jhg.util.QuoteExtractor;

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
	private List<Dialogue> dialogues;
	
	public Sentence(int n, Paragraph p, Type t, String s) {
		this.sentenceNumber = n;
		this.text = s;
		this.paragraph = p;
		this.type = t;
		dialogues = new ArrayList<Dialogue>(); 
		processDialogue();
		
	}
	
	public List<Dialogue> getDialogues(){
		return this.dialogues;
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
	public String getSentenceID(){
		return this.getChapterNumber()+DELIM+this.getSceneNumber()+DELIM+this.getParagraphNumber()+DELIM+this.getSentenceNumber();
	}
	public void debug() {
		Log.println(getSentenceID()+DELIM+this.text+DELIM+this.text.length()+EOL);
		
	}
	
	private void processDialogue(){
		int i = 1;
		List<String> dialogueStrings = QuoteExtractor.extractQuotedText(this.text);
		for(String s:dialogueStrings){
			dialogues.add(new Dialogue(i,this,s));
			i++;
		}
	}
	
	public String writeData() {
		return getSentenceID()+DELIM+this.text+DELIM+this.text.length()+"\n";
	}
	
}
