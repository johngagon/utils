package jhg.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

	private RegexUtil() {
		super();
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
		boolean rv = matcher.matches();
		return rv;
	}
	
	public static void test2(){
		String bad = "abcdefghijklmonpqrs";
		String good = "mkyong_2002";                      //no uppercase
		boolean pass1= (match(USERNAME,bad) == false);
		boolean pass2= (match(USERNAME,good)== true);
		Log.println(" Pass1:"+pass1);
		Log.println(" Pass2:"+pass2);
	}
	private static void doSimpleTest(String name,String passCase, String failCase, String regex){
		boolean r1 = passCase.matches(regex);
		boolean r2 = failCase.matches(regex);
		Log.println("Testing: '"+name+"' - Pass case '"+passCase+"' passed as expected?:"+r1+", and fail case '"+failCase+"' failed as expected?:"+(!r2)+"  with regex: '"+regex+"'");
		
	}
	public static void testISBN10(){
		String passCase = "ISBN 1-56389-668-0";
		String failCase = "ISBN 9-87654321-2";
		doSimpleTest("ISBN10", passCase,failCase,ISBN10);
	}
	public static void main(String[] args){
		testISBN10();
		
	}
}
/*
Decimals input

Positive Integers  ^\d+$

Negative Integers  ^-\d+$

Integer ^-?\d+$

Positive Number ^\d*\.?\d+$

Negative Number  ^-\d*\.?\d+$

Positive Number or Negative Number  ^-?\d*\.?\d+$

Phone number ^\+?[\d\s]{3,}$

Phone with code  ^\+?[\d\s]+\(?[\d\s]{10,}$

Year 1900-2099  ^(19|20)\d{2}$

Date (dd mm yyyy, d/m/yyyy, etc.)

^([1-9]|0[1-9]|[12][0-9]|3[01])\D([1-9]|0[1-9]|1[012])\D(19[0-9][0-9]|20[0-9][0-9])$ IP v4 --- ^(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]).(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]){3}$

Alphabetic input

Personal Name ^[\w.']{2,}(\s[\w.']{2,})+$
Username  ^[\w\d_.]{4,}$
Password at least 6 symbols  ^.{6,}$
Password or empty input  ^.{6,}$|^$
email ^[_]*([a-z0-9]+(\.|_*)?)+@([a-z][a-z0-9-]+(\.|-*\.))+[a-z]{2,6}$
domain ^([a-z][a-z0-9-]+(\.|-*\.))+[a-z]{2,6}$
Other regular expressions - Match no input  ^$ - Match blank input ^\s\t*$ - Match New line [\r\n]|$ - Match white Space  ^\s+$ - Match Url = ^http\:\/\/[a-zA-Z0-9.-]+\.[a-zA-Z]{2,3}$
 */
