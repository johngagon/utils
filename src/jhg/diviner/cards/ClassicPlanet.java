package jhg.diviner.cards;

public enum ClassicPlanet {
	SUN("\u263C"),
	MOON("\u263D"),
	MERCURY("\u263F"),
	VENUS("\u2640"),
	MARS("\u2642"),
	JUPITER("\u2643"),
	SATURN("\u2644"),
	EARTH("\u2641");
	
	private String sym;
	private ClassicPlanet(String s){this.sym=s;}
	public String getSym(){return sym;}	  //uranus:2645, neptune:2646, pluto:2647	
}
