package jhg.diviner.cards;

public enum Dunon {
	//Religious/Cultural: 2613, 12:(2624-262F), 2638 2670,2671, 269C,269D   18.
	A01 ("\u05D0"), 
	A02 ("\u05D1"),
	A03 ("\u05D2"),
	A04 ("\u05D3"),
	A05 ("\u05D4"),
	A06 ("\u05D5"),
	A07 ("\u05D6"),
	A08 ("\u05D7"),
	A09 ("\u05D8"),
	
	A10 ("\u05D9"),
	A11 ("\u05DA"),
	A12 ("\u05DC"),
	A13 ("\u05DE"),
	A14 ("\u05E0"),
	A15 ("\u05E1"),
	A16 ("\u05E2"),
	A17 ("\u05E4"),
	A18 ("\u05E6")  //7,8,9,A 
	;
	private String sym;
	private Dunon(String s){sym=s;}
	public String getSym(){return sym;}
}
/*

1 05D0 Alef
2 05D1 Beit
3 05D2 Ghimel
4 05D3 Dalet
5 05D4 Hey
6 05D5 Vav
7 05D6 Zain
8 05D7 Cheit
9 05D8 Tet
10 05D9 Yud
11 05DA Khaf

12 05DC Lamed
13 05DE Mem
14 05E0 Nun
15 05E1 Samekh
16 05E2 'Ain
17 05E4 Peh
18 05E6 Tzadde
19 05E7 Quf
20 05E8 Resh
21 05E9 Shin
22 05EA Tav

Weather: 2600,2601,2602,2603,2614 2607,2608, 2668  5(26C4-26C8)  13
Astral:  2604,2605,2606                                             3
Astrol:  2609,             4: 260A,260B,260C,260D  4:(26B9-26BC)   9
Phone:   260E,260F        2
Alchemy: 2609:gold +2(gender), +mars/venus,earth.           6
Voting:  2610,2611,2612                                   3
Crosses: 2613,
Cautions: 2620,2621,2622,2623   269B  26A1,26A2               7
Fun: 2615,2618,2619,263C,267F,26BD,26BE
Handarrows: 6:261A-261F                                                   6
Religious/Cultural: 2613, 12:(2624-262F), 2638 2670,2671, 269C,269D   18.
Recycling: 13 (2672-267E)               13
Terminology: 9(2692-269A)                9
Gender: 15
Stellar: 6 26B3-26B8 incl.

	SALTIR   ("\u2613"), 
	CADUCEUS ("\u2624"),
	ANKH     ("\u2625"),
	RUSSIAN  ("\u2626"),
	CHIRHO   ("\u2627"),
	LORRAINE ("\u2628"),
	JERUSALEM("\u2629"),
	ISLAM    ("\u262A"),
	FARSI    ("\u262B"),
	
	ADISHAKTI("\u262C"),
	HAMMER   ("\u262D"),
	PEACE    ("\u262E"),
	YINYANG  ("\u262F"),
	DHARMA   ("\u2638"),
	WSYRIAC  ("\u2670"),
	ESYRIAC  ("\u2671"),
	FRANCE   ("\u269C"),
	MOROCCO  ("\u269D"),
*/