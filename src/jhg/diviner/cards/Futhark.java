package jhg.diviner.cards;

public enum Futhark {  //younger has only 16
	FEHU("\u16A0"),
	URUZ("\u16A2"),
	THURISAZ("\u16A6"),
	ANSUZ("\u16A8"),
	RAIDO("\u16B1"),
	KENAZ("\u16B2"),
	GEBO("\u16B7"),
	WUNJO("\u16B9"),
	HAGLAZ("\u16BA"),
	NAUTHIZ("\u16BE"),
	ISA("\u16C1"),
	JERA("\u16C3"),
	IWAZ("\u16C7"),
	PERTHO("\u16C8"),
	ALGIZ("\u16C9"),
	SOWULO("\u16CA"),
	TEIWAZ("\u16CF"),
	BERKANA("\u16D2"),
	EHWAZ("\u16D6"),
	MANNAZ("\u16D7"),
	LAGUZ("\u16DA"),
	INGUZ("\u16DC"),
	DAGAZ("\u16DE"),
	OTHILA("\u16DF");//natural (for now)
	private String sym;
	private Futhark(String s){sym=s;}
	public String getSym(){return sym;}
	/*
FEHU 16A0
URUZ 16A2
THURISAZ 16A6
ANSUZ 16A8
RAIDO 16B1
KAUNA 16B2
GEBO 16B7
WUNJO 16B9

HAGLAZ 16BA
NAUDIZ 16BE
ISAZ 16C1
JERAN 16C3
IWAZ 16C7
PERTHO 16C8
ALGIZ 16C9
SOWILO 16CA

TIWAZ 16CF
BERKANAN 16D2
EHWAZ 16D6
MANNAZ 16D7
LAUKAZ 16DA
INGWAZ 16DC
DAGAZ 16DE
OTHALAN 16DF
	 */
}
