package jhg.util.book;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jhg.util.Dictionary;
import jhg.util.Log;
import jhg.util.QuoteExtractor;

public class Sentence {

	
	public static String DELIM="\t";
	private static final String EOL = "";
	public static enum Type{
		NARRATIVE,
		DIALOG,
		DOCUMENT,
		UNANALYZED
	}
	
	private List<Problem> problems;
	private Set<String> exceptionWords;
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
		problems = new ArrayList<Problem>();
		exceptionWords  = new TreeSet<String>();
		processDialogue();
		checkForProblems();
	}
	private void checkForProblems(){
		checkLength();
		checkDialogue();
	}
	private void checkLength(){
		
		if(this.text.length()>NovelParser.MAX_LENGTH){
			String msg = " Length: "+this.text.length()+", Max: "+NovelParser.MAX_LENGTH+".";
			problems.add(new Problem(this,Issue.SENTENCE_TOO_LONG,msg));
		}		
	}
	
	private void checkDialogue(){
		Dictionary dict = NovelParser.dictionary;
		if(dict!=null){
			for(Dialogue d:dialogues){
				int dialogueId = d.getNumber();
				String dtext = d.getText();
				List<String> unknownWords = dict.findUnknownWords(dtext,true);//AsJoinedString
				exceptionWords.addAll(unknownWords);
				if(unknownWords.size()>0){
					StringBuffer buff = new StringBuffer();
					buff.append("Unknown Words:[");
					for(String uw:unknownWords){
						buff.append(uw+" ");
					}
					buff.append("]");
					problems.add( new Problem(this,Issue.DIALOGUE_ANACHRONISM,dialogueId,buff.toString()) );
				}
			}
		}
	}
	
	public List<Problem> getProblems(){
		return this.problems;
	}
	
	public Set<String> getExceptionWords(){
		return this.exceptionWords;
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
