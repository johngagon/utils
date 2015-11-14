package jhg.util.book;

public enum Issue {
	DIALOGUE_ANACHRONISM(""),
	SENTENCE_TOO_LONG("");
	
	private String message;
	private Issue(String m){
		this.message = m;
	}
	public String getMessage(){
		return this.message;
	}
}
