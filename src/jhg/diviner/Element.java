package jhg.diviner;

import jhg.util.Log;

public enum Element {
	AIR("\u2660"),
	FIRE("\u2663"),
	EARTH("\u2666"),
	WATER("\u2665");
	
	private String sym;
	private Element(String s){
		this.sym = s;
	}
	public String getSym(){
		return sym;
	}
	

}
