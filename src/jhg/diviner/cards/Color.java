package jhg.diviner.cards;

public enum Color {
	RED    ("\u2624","#F00"),    //RYGCBM   Dice: 2680-2685   2624-2629
	YELLOW ("\u2625","#FF0"),
	GREEN  ("\u2626","#0F0"), 
	CYAN   ("\u2627","#0FF"),  
	BLUE   ("\u2628","#00F"),  
	MAGENTA("\u2629","#F0F");
	private String sym; //property String prop   "property" implies public getter and constructor, private name ,eval and json.
	private String hex;
	private Color(String s,String t){this.sym=s;this.hex=t;}
	public String getSym(){return sym;}	
	public String getHex(){return hex;}
	
}
