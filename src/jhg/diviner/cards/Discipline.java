package jhg.diviner.cards;

public enum Discipline {
	SUN("\u263C"),
	MOON("\u263D"),
	MERCURY("\u263F"),
	VENUS("\u2640"),
	MARS("\u2642"),
	JUPITER("\u2643"),
	SATURN("\u2644"),
	URANUS("\u2645"),
	NEPTUNE("\u2646"),
	PLUTO("\u2647");
	
	private String sym;
	private Discipline(String s){this.sym=s;}
	public String getSym(){return sym;}	 
}
