package jhg.util.types;

import java.util.*;

public class CurrencyUtil {

	public static Set<Currency> getAvailable(){
		return Currency.getAvailableCurrencies();
	}
	
}
