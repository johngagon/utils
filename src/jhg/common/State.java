package jhg.common;

public enum State {

	AL("Alabama"),
	AK("Alaska"),
	AZ("Arizona"),
	AR("Arkansas"),
	CA("California"),
	CO("Colorado"),
	CT("Connecticut"),
	DE("Delaware"),
	FL("Florida"),
	GA("Georgia"),
	HI("Hawaii"),
	ID("Idaho"),
	IL("Illinois"),
	IN("Indiana"),
	IA("Iowa"),
	KS("Kansas"),
	KY("Kentucky"),
	LA("Lousianna"),
	ME("Maine"),
	MD("Maryland"),
	MA("Massachusetts"),
	MI("Michigan"),
	MN("Minnesota"),
	MS("Mississippi"),
	MO("Missouri"),
	MT("Montana"),
	NE("Nebraska"),
	NV("Nevada"),
	NH("New Hampshire"),
	NJ("New Jersey"),
	NM("New Mexico"),
	NY("New York"),
	NC("North Carolina"),
	ND("North Dakota"),
	OH("Ohio"),
	OK("Oklahoma"),
	OR("Oregon"),
	PA("Pennsylvania"),
	RI("Rhode Island"),
	SC("South Carolina"),
	SD("South Dakota"),
	TN("Tennessee"),
	TX("Texas"),
	UT("Utah"),
	VT("Vermont"),
	VA("Virginia"),
	WA("Washington"),
	WV("West Virginia"),
	WI("Wisconsin"),
	WY("Wyoming"),
	PR("Puerto Rico"),
	DC("District of Columbia");
	
	private String longName;
	private State(String aName){
		this.longName = aName;
	}
	public String getLongName(){
		return this.longName;
	}
	public static State valueOfLongName(String _longName){
		State rv = null;
		State[] states = State.values();
		for(State state:states){
			if(state.longName.equals(_longName)){
				rv = state;
			}
		}
		return rv;
	}
	public static String longToShort(String longName){
		String rv = null;
		State s = State.valueOfLongName(longName);
		if(s!=null){
			rv = s.name();
		}
		return rv;
	}	
	public static String shortToLong(String shortName){
		String rv = null;
		State s = State.valueOf(shortName);
		if(s!=null){
			rv = s.getLongName();
		}
		return rv;
	}	
}