package jhg.diviner.cards;

public enum Ogham {
	A01,//("\u1681"),
	A02,//("\u1682"),
	A03,//("\u1683"),
	A04,//("\u1684"),
	A05,//("\u1685"),
	A06,//("\u1686"),
	A07,//("\u1687"),
	A08,//("\u1688"),
	A09,//("\u1689"),
	A10,
	A11;//("\u168A");
	//private String sym;
	private Ogham(){}//String s){sym=s;}
	public String getSym(){
		int val = 913+this.ordinal();
		char c = (char)val;
		return "" + c;		
		//return sym;
	}
}
/*
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
 */
