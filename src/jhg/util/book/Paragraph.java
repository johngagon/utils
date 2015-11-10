package jhg.util.book;

import java.util.*;

import jhg.util.Log;

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
		Log.println("Paragraph "+paraNumber);
		for(Sentence sentence:sentences){
			sentence.debug();
		}
		
	}

	public int getParaNumber() {
		return paraNumber;
	}

	public Scene getScene() {
		return scene;
	}

	public List<Sentence> getSentences() {
		return sentences;
	}
	

}
