package jhg.diviner;

public enum Duality {
	SUN("\u25CB"),
	MOON("\u25CF")
	;
	private String sym;
	private Duality(String s){
		this.sym = s;
	}
	public String getSym(){
		return sym;
	}
}
