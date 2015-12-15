package jhg.util.types.locale;

import java.util.*;

public class Earth {

	
	private List<Country> countries;
	
	public Earth(){
		this.countries = Earth.loadCountries();
	}

	
	
	public List<Country> getCountries() {
		return countries;
	}



	private static List<Country> loadCountries() {
		List<Country> rv = new ArrayList<Country>();
		String[] locales = Locale.getISOCountries();
		for(String countryCode:locales){
			Locale locale = new Locale("",countryCode);
			Country c = new Country(locale);
			rv.add(c);
		}
		return rv;
	}
}
