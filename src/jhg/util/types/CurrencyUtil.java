package jhg.util.types;

import java.util.Currency;
import java.util.Set;

public class CurrencyUtil {

	public static Set<Currency> getAvailable(){
		return Currency.getAvailableCurrencies();
	}
	
}
