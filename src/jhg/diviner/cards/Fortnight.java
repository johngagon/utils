package jhg.diviner.cards;

public enum Fortnight {
	  	A01, //("\u2600"), //26a2-26af
		A02, // ("\u2601"),
		A03, // ("\u2602"),
		A04, // ("\u2603"),
		A05, // ("\u2604"),
		A06, // ("\u2605"),
		A07, // ("\u2606"),
		A08, // ("\u2607"),
		A09, // ("\u2608"),
		A10, // ("\u2609"),
		A11, // ("\u260A"),
		A12, // ("\u260B"),
		A13, // ("\u260C"),
		A14,
		A15// ("\u260D")           Black phone
	;
	//private String sym;
	private Fortnight(){}//String s){sym=s;}
	public String getSym(){//return sym;}
		int val = 9728+this.ordinal();
		char c = (char)val;
		return "" + c;
	}
}
/*
 * 	LESB    ("\u26A2"), //26a2-26af
	GAY     ("\u26A3"),
	STRAIGHT("\u26A4"), 
	HERM    ("\u26A5"), //entomology
	TRANS   ("\u26A6"),
	MULTI   ("\u26A7"),
	IRON    ("\u26A8"), //sulfate
	MAG     ("\u26A9"),
	ASEX    ("\u26AA"),
	NOTDISCL("\u26AB"),
	ENGAGED ("\u26AC"),
	MARRIED ("\u26AD"),
	DIVORCED("\u26AE"),
	PARTNER ("\u26AF")
 */
