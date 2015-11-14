package jhg.util.book;

public class Problem {

	private Sentence sentence;
	private Issue issue;
	private int dialogue_id;
	
	public Problem(Sentence s, Issue i){
		this.sentence = s;
		this.issue = i;
		this.dialogue_id = -1;
	}
	/**
	 * @return the dialogue_id
	 */
	public int getDialogue_id() {
		return dialogue_id;
	}
	/**
	 * @param dialogue_id the dialogue_id to set
	 */
	public void setDialogue_id(int dialogue_id) {
		this.dialogue_id = dialogue_id;
	}
	/**
	 * @return the sentence
	 */
	public Sentence getSentence() {
		return sentence;
	}
	/**
	 * @return the issue
	 */
	public Issue getIssue() {
		return issue;
	}

}
