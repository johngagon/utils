package chp.datareceiving;

import java.util.regex.*;

public class PatternRule extends ScanRule {

	int[] indexes;
	Pattern pattern;
	
	public PatternRule(int[] iarr, String aPattern){
		super();
		this.indexes = iarr;
		this.pattern = Pattern.compile(aPattern);
	}
	


	@Override
	public boolean[] fieldCheck(String s) {
		String[] fieldvalues = s.split("\\t",-1);
		boolean[] rv = new boolean[indexes.length];
		int i=0;
		for(int index:indexes){
			if(index>=fieldvalues.length){
				System.out.println("!! Warning - split error "+fieldvalues.length+" with line '"+s+"'");
				rv[i] = false;
			}else{
				String test = fieldvalues[index];
				Matcher m = pattern.matcher(test);
				rv[i] = m.matches();
			}
			i++;
		}
		return rv;
	}

	@Override
	public String describe() {
		return "Field should match rule: '"+pattern.pattern()+"'";
	}

	
	
	@Override
	public boolean doFieldCheck() {
		return true;
	}

	@Override
	public int[] getIndexes() {
		return indexes;
	}

	@Override
	public boolean check(String s) {
		return true;
	}	
	
	public static final String NUMBER = "^[0-9]+$";
	
	/**
	 * lowercase, digits, underscore, hyphen 
	 * length: 3-15
	 * single line.
	 */
	public static final String USERNAME = "^[a-z0-9_-]{3,15}$"; 
	
	/**
	 * must contain number, lowercase,uppercase, one of:@#$%   ?= (lookahead) 
	 * length: 6-20
	 */
	public static final String PASSWORD = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
	
	/**
	 * Color hex in particular, can be upper or lower A-F, 3 or 6 in length.
	 */
	public static final String HEXCOLOR = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

	/**
	 * with firstname: Alphanum,underscore,dash, a single dot, lastname same as firstname. at sign. another pattern and dot 2 or more alpha upper lower. 
	 */
	public static final String EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	//public static final String
	
	/**
	 * non space, dot, one of the extensions.
	 */
	public static final String IMAGE = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
	
	/**
	 * 
	 */
	public static final String IP_ADD = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	/**
	 * 
	 */
	public static final String TIME_12HR = "(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)";
	
	public static final String TIME_24HR = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
	
	public static final String DATE = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";//dd/mm/yyyy
	
	public static final String HTML_TAG = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";
	
	public static final String EIN = "^[1-9]\\d?-\\d{7}$";
	
	public static final String ISBN10 = "ISBN\\x20(?=.{13}$)\\d{1,5}([- ])\\d{1,7}\\1\\d{1,6}\\1(\\d|X)$";
	
	public static final String ISBN = "^ISBN\\s(?=[-0-9xX ]{13}$)(?:[0-9]+[- ]){3}[0-9]*[xX0-9]$";
	
	public static final String DECIMALCOPT = "^(((\\d{1,3})(,\\d{3})*)|(\\d+))(.\\d+)?$";//YES: 99999,9999.999 99,999,999.99999   NOT: 999. 9,99,9999.99  99.999.999
	
	public static final String EXP_NUM = "^[+-]?([0-9]*\\.?[0-9]+|[0-9]+\\.?[0-9]*)([eE][+-]?[0-9]+)?$";//23 -17.e23 +.23e+2

	public static final String HEX = "^[0-9A-F]+$";

	public static final String HTML_LINK_EXTRACT = "\\s*(?i)href\\s*=\\s*(\\\"([^\"]*\\\")|'[^']*'|([^'\">\\s]+));";
	
	public static final String WEBSITE = "";
	
	public static final String FILEPATH = "";
	
	public static final String URL = "/^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$/";
		
	
}
