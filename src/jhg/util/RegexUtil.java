package jhg.util;

import java.util.regex.*;

public class RegexUtil {

	private RegexUtil() {
		super();
	}
	
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
	public static final String HEX = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

	/**
	 * with firstname: Alphanum,underscore,dash, a single dot, lastname same as firstname. at sign. another pattern and dot 2 or more alpha upper lower. 
	 */
	public static final String EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
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
	
	
	/*
	 * lowercase a-z
	 * uppercase A-Z
	 * numbers 0-9
	 * keyboardSpecChar 
	 */
	
	
	public static Pattern getPattern(String s){
		return Pattern.compile(s);
	}
	
	public static boolean match(Pattern pattern, String toTest){
		Matcher matcher = pattern.matcher(toTest);
		return matcher.matches();
	}
	public static boolean match(String patternString, String toTest){
		Pattern pattern = getPattern(patternString);
		Matcher matcher = pattern.matcher(toTest);
		return matcher.matches();
	}
	
	public static void test(){
		String bad = "abcdefghijklmonpqrs";
		String good = "mkyong_2002";                      //no uppercase
		boolean pass1= (match(USERNAME,bad) == false);
		boolean pass2= (match(USERNAME,good)== true);
		Log.println(" Pass1:"+pass1);
		Log.println(" Pass2:"+pass2);
	}
	
	public static void main(String[] args){
		test();
	}
}
