package jhg.util.types.locale;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Country {

	private Locale locale;
	@SuppressWarnings("unused")
	private List<CountrySubdivision> subdivisions;
	
	public Country(Locale locale) {
		super();
		this.locale = locale;
		this.subdivisions = new ArrayList<CountrySubdivision>();
	}

	public Locale getLocale(){
		return this.locale;
	}
	
	public String getCountryCode(){
		return locale.getCountry();
	}
	public String getCountryName(){
		return locale.getDisplayCountry();
	}
	public String getLanguage(){
		return locale.getDisplayLanguage();
	}
	public String getLanguageCode(){
		return locale.getLanguage();
	}
}
