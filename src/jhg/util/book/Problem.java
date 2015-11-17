package jhg.util.book;

public class Problem {

	private Sentence sentence;
	private Issue issue;
	private int dialogue_id;
	private String message;
	
	public Problem(Sentence s, Issue i, String _message){
		this.sentence = s;
		this.issue = i;
		this.dialogue_id = -1;
		this.message = _message;
	}
	public Problem(Sentence s, Issue i, int _dialogue_id, String _message){
		this.sentence = s;
		this.issue = i;
		this.dialogue_id = _dialogue_id;
		this.message = _message;
	}

	
	
	public String getMessage() {
		return message;
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

	public String toDisplayString(){
		StringBuffer buff = new StringBuffer();
		buff.append("Problem: "+issue.getMessage());
		if(!message.isEmpty()){
			buff.append(" Detail: '"+message+"'");
		}
		buff.append(" ID: "+sentence.getSentenceID());
		if(dialogue_id!=-1){
			buff.append(Sentence.DELIM+dialogue_id);
		}
		buff.append("  S:'"+sentence.getText()+"'");

		
		//buff.append("\n");
		return buff.toString();
	}
	
}
