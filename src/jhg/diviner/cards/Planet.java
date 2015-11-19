package jhg.diviner.cards;

public enum Planet {
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
	private Planet(String s){this.sym=s;}
	public String getSym(){return sym;}	 
	/*
	 Mining 2692
	 Nautical
	 Military
	 Medical
	 Legal
	 Chemical
	 Botanical
	 Technical
	 Commerical
	 */
}
