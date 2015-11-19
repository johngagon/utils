package jhg.diviner;

public enum Color {
	RED("R","#F00"),
	YELLOW("Y","#FF0"),
	GREEN("G","#0F0"), 
	CYAN("C","#0FF"),  
	BLUE("B","#00F"),  
	MAGENTA("M","#F0F");
	private String sym; //property String prop   "property" implies public getter and constructor, private name ,eval and json.
	private String hex;
	private Color(String s,String t){this.sym=s;this.hex=t;}
	public String getSym(){return sym;}	
	public String getHex(){return hex;}
	
}
