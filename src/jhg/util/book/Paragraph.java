package jhg.util.book;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jhg.util.book.Sentence.Type;

public class Paragraph {

	private int paraNumber;
	private Scene scene;
	private List<Sentence> sentences;
	
	public Paragraph(int num, Scene s) {
		
		this.paraNumber=num;
		this.scene = s;
		this.sentences = new ArrayList<Sentence>();
	}

	public void debug() {
		//Log.println("Paragraph "+paraNumber);
		for(Sentence sentence:sentences){
			sentence.debug();
		}
		
	}
	public void addSentences(String source){
		BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
		//String source = "This is a test. This is a T.L.A. test. Now with a Dr. in it.";
		iterator.setText(source);
		int start = iterator.first();
		int i=1;
		for (int end = iterator.next();
			end != BreakIterator.DONE;
			start = end, end = iterator.next()) {
			String sentenceText = source.substring(start,end);
			Sentence sentence = new Sentence(i,this,Type.UNANALYZED,sentenceText); //(int n, Paragraph p, Type t, String s)
			sentences.add(sentence);
			i++;
		}	
	}

	public int getParaNumber() {
		return paraNumber;
	}
	
	public int getSceneNumber(){
		return this.getScene().getSceneNumber();
	}
	public int getChapterNumber(){
		return this.getScene().getChapter().getChapterNumber();
	}
	
	public Scene getScene() {
		return scene;
	}

	public List<Sentence> getSentences() {
		return sentences;
	}

	public String writeData() {
		StringBuffer buff = new StringBuffer();
		for(Sentence sentence:sentences){
			buff.append(sentence.writeData());
		}
		return buff.toString();
	}
	
	public boolean hasSentences(){
		return sentences.size()>0;
	}
	
	public Sentence getLongestSentence(){
		int topLength = 0;
		Sentence topSentence = null;
		for(Sentence sentence:sentences){
			if(topSentence==null){
				topSentence = sentence;
			}
			if(sentence.length()>topLength){
				topLength=sentence.length();
				topSentence = sentence;
			}
		}
		return topSentence;
	}
}
