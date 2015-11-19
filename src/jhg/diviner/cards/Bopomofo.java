package jhg.diviner.cards;

public enum Bopomofo {
	BO("\u3105"),
	PO("\u3106"),
	MO("\u3107"),
	FO("\u3108"),
	DE("\u3109"),
	TE("\u310A"),
	NE("\u310B"),
	LE("\u310C"),
	GE("\u310D"),
	KE("\u310E"),
	HE("\u310F"),
	JI("\u3110"),
	
	
	CI("\u3111"),
	XI("\u3112"),
	JR("\u3113"),
	CR("\u3114"),
	XR("\u3115"),
	RR("\u3116"),
	DZ("\u3117"),
	CZ("\u3118"),
	SZ("\u3119"),
	AH("\u311A"),
	OH("\u311B"),
	UH("\u311C"),
	
	EH("\u311D"),
	AY("\u311E"),
	EY("\u311F"),
	AU("\u3120"),
	OU("\u3121"),
	AN("\u3122"),
	UN("\u3123"),
	OK("\u3124"),
	UK("\u3125"),
	YI("\u1627"),
	WU("\u1628"),
	YU("\u3129");
	//ER("\u3126"),
	private String sym;
	private Bopomofo(String s){sym=s;}
	public String getSym(){return sym;}
	
	/* 
	 * 
	 * 31XX
	 * BO 05
	 * PO 06
	 * MO 07
	 * FO 08
	 * DE 09
	 * TE 0A
	 * NE 0B
	 * LE 0C
	 * GE 0D
	 * 
	 * KE 0E
	 * HE 0F
	 * JI 10
	 * CHI 11
	 * SHI 12
	 * JR 13
	 * CHR 14
	 * SHR 15
	 * R   16
	 * 
	 * DZ 17
	 * TZ 18
	 * SZ 19
	 * AH 1A
	 * OH 1B
	 * UH 1C
	 * EH 1D
	 * AI 1E
	 * EI 1F
	 * 
	 * AU 20
	 * OU 21
	 * AN 22
	 * EN 23
	 * ANG 24
	 * ENG 25
	 * ER 26
	 * YI 27
	 * WU 28 
	 * YU 29
	 *  V 2A
	 */
}
