package jhg.diviner.cards;

public enum Zodiac {
	ARIES("\u2648"),
	TAURUS("\u2649"),
	GEMINI("\u264A"),
	CANCER("\u264B"),
	LEO("\u264C"),
	VIRGO("\u264D"),
	LIBRA("\u264E"),
	SCORPIO("\u264F"),
	SAGITTARIUS("\u2650"),
	CAPRICORN("\u2651"),
	AQUARIUS("\u2652"),
	PISCES("\u2653")
	;
	
	private String sym;
	private Zodiac(String s){
		this.sym = s;
	}
	public String getSym(){
		return sym;
	}
}
