package johng_map;


public enum State {
	UD("Undefined"),
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
	LA("Louisiana"),
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
	public static State fromLongName(String longName){
		State rv = null;
		for(State s:State.values()){
			if(s.longName.equals(longName)){
				rv = s;
				break;
			}
		}
		return rv;
	}
}
