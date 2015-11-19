package jhg.diviner;

public enum Furthark {
	FEHU("\u16A0"),
	
	OTHALAN("\u16DF");//natural (for now)
	private String sym;
	private Furthark(String s){sym=s;}
	public String getSym(){return sym;}
	/*
FEHU 16A0
URUZ 16A2
THURISAZ 16A6
ANSUZ 16A8
raido 16B1
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
