package jhg.util.book;

public enum Issue {
	DIALOGUE_ANACHRONISM("'Dialogue Issue'"),
	SENTENCE_TOO_LONG("'Sentence too long'");
	
	private String message;
	private Issue(String m){
		this.message = m;
	}
	public String getMessage(){
		return this.message;
	}
}
