package jhg.util;

public interface Constants {

	/*
	 * \.[]{}()*+-?^$|    regex escape
	 * 
	 * \\ \. \[ \] \{ \} \( \) \* \+ \- \? \^ \$ \|
	 * 
	 * Cross site scripting security tool
	 * SQL injection tool
	 * 
	 * HTML, javascript, css, java
	 * csv, json
	 * sql :     % || ? _  single quotes '' 
	 * 
	 * see commons string escape utils
	 * 
	 * tabular data converter: 
	 * 
	 * <table><tr><td>
	 * | padded data    |   323|   2|      |
	 * csv delimited, quoted
	 * json
	 * xml flat (identified)
	 * fixed width char[][]
	 * resultset ->
	 * 
	 * validation, data conversion, data formatting, search/filter, sort
	 * cross tab, count groups, export, import ->object
	 * (but not graphs / analysis)
	 *  
	 * 
	 * 
	 */
	
	public static final String LB = "{";			//left brace
	public static final String RB = "}";			//right brace
	public static final String LP = "(";			//left paren
	public static final String RP = ")";			//right paren
	public static final String LS = "[";			//left square
	public static final String RS = "]";			//right square
	public static final String LA = "<"; 			//left angle
	public static final String RA = ">"; 			//right angle
	  
	public static final String A = "&";				//ampersand
	public static final String B = "\b";			//backspace
	public static final String C = ",";				//comma
	public static final String D = "\"";			//double quote
	public static final String E = "";				//empty
	public static final String F = "\f";			//formfeed
	public static final String L = "\n";			//line end
	
	public static final Integer N = -1;				
	public static final Integer O = 1;				
	public static final Integer Z = 0;
	
	
	public static final String M = "$";				//money
	public static final String P = ".";				//period
	public static final String Q = "'";				//single-quote
	public static final String R = "\r";				//carriage return
	public static final String S = " ";				//space
	public static final String T = "\t";			//tab
	public static final String Y = "*";				//star
	public static final Object X = null;			//null
	
	/*
	 * Punctuation not seen:
	 * ~ ` ! @ # % ^ - _ + = | / ; : ?
	 */
	
	public static final String G = "";
	public static final String H = "";
	public static final String I = "";
	public static final String J = "";
	public static final String U = "";
	public static final String V = "";
	public static final String W = "";
	
	
	public static final boolean PASS = true;
	public static final boolean FAIL = false;
	public static final boolean SUCCESS = true;
	public static final boolean FAILURE = false;
	
	public static final boolean DEBUG = false;
	public static final boolean TEST = false;
	
	public static final int NOT_FOUND = N;
	
	/*
	 * Unused: GHIJK UVW
	 * 
	 * 
	 */
	
	/*
	 * most common enumerations in business
	 */
	
}
