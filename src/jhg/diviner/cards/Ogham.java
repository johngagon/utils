package jhg.diviner.cards;

public enum Ogham {
	BEITH("\u1681"),
	LUIS("\u1682"),
	FEARN("\u1683"),
	SAIL("\u1684"),
	NION("\u1685"),
	UATH("\u1686"),
	DAIR("\u1687"),
	TINNE("\u1688"),
	COLL("\u1689"),
	CEIRT("\u168A");
	private String sym;
	private Ogham(String s){sym=s;}
	public String getSym(){return sym;}
}
