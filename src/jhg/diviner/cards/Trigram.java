package jhg.diviner.cards;

public enum Trigram {
	HEAVEN("\u2630"),
	LAKE("\u2631"),
	FIRE("\u2632"),
	THUNDER("\u2633"),
	WIND("\u2634"),
	WATER("\u2635"),
	MOUNTAIN("\u2636"),
	EARTH("\u2637");
	private String sym;
	private Trigram(String s){this.sym=s;}
	public String getSym(){return sym;}
}
