package jhg.storywriter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Setting extends FilePersisted {

	public static enum Type{
		HOME,
		BOAT,
		CASTLE,
		PARK,
		YARD,
		RESTAURANT,
		COURTROOM;
	}
	
	private Period period;
	private Locale locale;
	private String name;
	private String address;
	private String city;
	private String provinceState;
	private String country;
	private Type type;
	
	private Character owner;
	
	public Setting(String startYYYYMMDD, String endYYYYMMDD, String aName, Type aType){
		DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;//yyyymmdd
		LocalDate start = LocalDate.parse(startYYYYMMDD,formatter);
		LocalDate end = LocalDate.parse(endYYYYMMDD,formatter);
		period = Period.between(start, end);
		this.name = aName;
		this.type = aType;
	}
	
	public void setLocale(Locale aLocale){
		this.locale = aLocale;
	}
	
	public void setAddress(String anAddress){
		this.address = anAddress;
	}
	
	public void setOwner(Character anOwner){
		this.owner = anOwner;
	}
	
	public void setLocations(String city, String provinceState, String country){
		this.city = city;
		this.provinceState = provinceState;
		this.country = country;
	}

	public Period getPeriod() {
		return period;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public String getProvinceState() {
		return provinceState;
	}

	public String getCountry() {
		return country;
	}

	public Character getOwner() {
		return owner;
	}
	
	public Type getType(){
		return type;
	}
	
}
