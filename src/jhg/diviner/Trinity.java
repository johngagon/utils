package jhg.diviner;

public enum Trinity {
	MERCURY("\u266D"),//flat
	SULFUR("\u266F"),//sharp
	SALT("\u266E");//natural (for now)
	private String sym;
	private Trinity(String s){sym=s;}
	public String getSym(){return sym;}
}
