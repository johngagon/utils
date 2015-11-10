package jhg.util.book;



public class Dialogue  {

	/*
	 * character
	 */
	
	private int number;
	private Sentence sentence;
	private String text;
	
	public Dialogue(int num, Sentence _sentence, String s) {
		this.number = num;
		this.sentence = _sentence;
		this.text = s;
	}

	public int getNumber() {
		return number;
	}

	public Sentence getSentence() {
		return sentence;
	}

	public String getText() {
		return text;
	}	
	
}
