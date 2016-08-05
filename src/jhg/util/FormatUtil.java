package jhg.util;

@SuppressWarnings({"boxing","unused"})
public class FormatUtil {

	public static final String MONEY = "$%,.2f";
	public static final String PERCENT = "%.1f%%";
	public static final String CRLF = "%n";
	public static final String LPAD = "%05d";
	public static final String RESULT = "Result: %s  Expected:'%s' Actual:'%s' ";
	public static final String TIME = " %d:%d:%d %d ";//escaping dot?
	public static final String TIME_SPELLED = " %dh %dm %ds %dms";
	
	public static enum TimeConversions{
		HOUR_24_PAD("H"),
		HOUR_12_PAD("I"),
		HOUR_24("k"),
		HOUR_12("l"),
		MIN("M"),
		SEC("S"),
		MILLI("L"),
		NANO("N"),
		AMPM("p"),
		TZ_NUM("z"),
		TZ_ABBR("Z"),
		SEC_EPOCH("s"),
		MILLIS_EPOCH("Q")
		;
		private String val;
		private TimeConversions(String s){
			this.val = s;
		}
		public String getVal(){
			return val;
		}		
	}
	public static enum DateConversions{
		MONTH_FULL("B"),
		MONTH_ABBR("b"),
		DOW_FULL("A"),
		DOW_ABBREV("a"),
		Y4("Y"),
		Y2("y"),
		DOY("j"),
		MONTH_PAD("m"),
		DOM_PAD("d"),
		DOM("e")
		;
		private String val;
		private DateConversions(String s){
			this.val = s;
		}
		public String getVal(){
			return val;
		}			
	}
	public static enum CommonDateTime{
		CLOCK("R"),
		CLOCK_SEC("T"),
		CLOCK12("r"),
		DATE_MDY("D"),
		DATE_ISO("F"),//ymd
		DATETIME("c")
		;
		private String val;
		private CommonDateTime(String s){
			this.val = s;
		}
		public String getVal(){
			return val;
		}		
	}
	
	public static enum Flag{
		LEFT_JUSTIFY("-"),
		SIGNED("+"),
		ZERO_PAD("0"),
		COMMA_ZEROGROUP(","),
		SIGNED_NEG(" "),
		NEG_PARENS("(");  		//minus sign if neg, space if positive number
		/*
		 * not used: # conversion dependent alternat form. 
		 */
		private String val;
		private Flag(String s){
			this.val = s;
		}
		public String getVal(){
			return val;
		}
	}
	
	public static enum Conversion {
		DECIMAL("d"),
		FLOATING("f"),
		CHARACTER("c"),
		STRING("s"),
		TIME("t"),
		HASHCODE("h"),
		NEWLINE("n");
		private String val;
		private Conversion(String s){
			val = s;
		}
		public String getVal(){
			return val;
		}
	}
	public static String createList(int q){
		StringBuilder sb = new StringBuilder("");
		for(int i=0;i<q;i++){
			sb.append("%s");
			if(i<q-1){
				sb.append(", ");
			}
		}
		return sb.toString();
	}
	public static String createQuotedList(int q){
		StringBuilder sb = new StringBuilder("");
		for(int i=0;i<q;i++){
			sb.append("\"%s");
			if(i<q-1){
				sb.append("\", ");
			}else{
				sb.append("\"");
			}
		}
		return sb.toString();
	}
	
	//%1$(,5.2f
	public static String createFormatString(int argIndex, Flag[] flags, int width, int precision, Conversion conversion){
		StringBuilder sb = new StringBuilder("%");
		if(argIndex!=-1){
			sb.append(argIndex+"$");
		}
		if(flags!=null){
			for(Flag f:flags){
				sb.append(f.getVal());
			}
		}
		if(width!=-1){
			sb.append(width);
		}
		if(Conversion.FLOATING.equals(conversion)||Conversion.STRING.equals(conversion)){

			if(precision!=-1){
				sb.append("."+precision);
			}
		}
		sb.append(conversion.getVal());
		return sb.toString();
	}
	//%(,5.2f
	public static String createFormatString(Flag[] flags, int width, Conversion conversion){
		return createFormatString(-1,flags,width,-1,conversion);
	}
	//%1$(f
	public static String createFormatString(int argIndex,Flag[] flags, Conversion conversion){
		return createFormatString(argIndex,flags,-1,-1,conversion);
	}
	//%1$s
	public static String createFormatString(int argIndex, Conversion conversion){
		Flag[] flags = null;
		return createFormatString(argIndex,flags,-1,-1,conversion);
	}
	//%1$(50d
	public static String createFormatString(int argIndex,Flag[] flags, int width, Conversion conversion){
		return createFormatString(argIndex,flags,width,-1,conversion);
	}
	//%(50.2f
	public static String createFormatString(Flag[] flags, int width, int precision, Conversion conversion){
		return createFormatString(-1,flags,width,precision,conversion);
	}
	//%$1(50.2f (single flag)
	public static String createFormatString(int argIndex,Flag flag, int width, int precision, Conversion conversion){
		return createFormatString(argIndex,new Flag[]{flag},width,precision,conversion);
	}	
	//%20s
	public static String createFormatString(Conversion conversion,int width){
		Flag[] flags = null;
		return createFormatString(-1,flags,width,-1,conversion);
	}	
	//%s
	public static String createFormatString(Conversion conversion){
		Flag[] flags = null;
		return createFormatString(-1,flags,-1,-1,conversion);
	}
	public static void main(String[] args){
		/*
		 * Be able to create strings
		 * 
		 * % [flags] [width] [.precision] conversion-character ( square brackets denote optional parameters )
		 */
		//test1();
		//test2();
		
		System.out.format("Hello %s, %s, %s and %s!\n","John",250.9,"Joe","Sue");
		System.out.format("Your total is $%,.2f: ",250.32988);
		
	}
	private static void test2(){
		
		Double money = -53529392945.23;
		System.out.format(MONEY+CRLF,money);
		String[] variables = {"car","boat","house","tax refund"};
		System.out.format("I want to buy a "+createQuotedList(3)+" with my %s.%n",(Object[])variables);
		System.out.format("He always gave "+PERCENT+CRLF,101.9);
		System.out.format("%20.5s\n", "hello world");
		System.out.format(LPAD,234);
		//TEMPLATE System.out.format("xxx",x);
	}
	
	
	private static void test1(){
		String format1 = createFormatString(-1,new Flag[]{Flag.COMMA_ZEROGROUP},5,2,Conversion.DECIMAL);
		String hr = createFormatString(Conversion.NEWLINE);
		System.out.format("Total is: $"+format1+hr,50000000);
		System.out.println("Format 1:'"+format1+"'");
		System.out.println("Format 2:'"+hr+"'");		
	}
}
/*
 * Notes on precision: e E f g G
 * 
 * b,B boolean
 * h,H Hex
 * o,O Octal
 * x,X hex int
 * e,E comp sci note
 * g,G (scientific notation but depending on precision after rounding.)
 * a,A hex floating
 * 
 * 
 * 
 * 
 */
/*
The following conversion characters are used for formatting times:

'H'	'\u0048'	Hour of the day for the 24-hour clock, formatted as two digits with a leading zero as necessary i.e. 00 - 23. 00 corresponds to midnight.
'I'	'\u0049'	Hour for the 12-hour clock, formatted as two digits with a leading zero as necessary, i.e. 01 - 12. 01 corresponds to one o'clock (either morning or afternoon).
'k'	'\u006b'	Hour of the day for the 24-hour clock, i.e. 0 - 23. 0 corresponds to midnight.
'l'	'\u006c'	Hour for the 12-hour clock, i.e. 1 - 12. 1 corresponds to one o'clock (either morning or afternoon).
'M'	'\u004d'	Minute within the hour formatted as two digits with a leading zero as necessary, i.e. 00 - 59.
'S'	'\u0053'	Seconds within the minute, formatted as two digits with a leading zero as necessary, i.e. 00 - 60 ("60" is a special value required to support leap seconds).
'L'	'\u004c'	Millisecond within the second formatted as three digits with leading zeros as necessary, i.e. 000 - 999.
'N'	'\u004e'	Nanosecond within the second, formatted as nine digits with leading zeros as necessary, i.e. 000000000 - 999999999. The precision of this value is limited by the resolution of the underlying operating system or hardware.
'p'	'\u0070'	Locale-specific morning or afternoon marker in lower case, e.g."am" or "pm". Use of the conversion prefix 'T' forces this output to upper case. (Note that 'p' produces lower-case output. This is different from GNU date and POSIX strftime(3c) which produce upper-case output.)
'z'	'\u007a'	RFC 822 style numeric time zone offset from GMT, e.g. -0800. This value will be adjusted as necessary for Daylight Saving Time. For long, Long, and Date the time zone used is the default time zone for this instance of the Java virtual machine.
'Z'	'\u005a'	A string representing the abbreviation for the time zone. This value will be adjusted as necessary for Daylight Saving Time. For long, Long, and Date the time zone used is the default time zone for this instance of the Java virtual machine. The Formatter's locale will supersede the locale of the argument (if any).
's'	'\u0073'	Seconds since the beginning of the epoch starting at 1 January 1970 00:00:00 UTC, i.e. Long.MIN_VALUE/1000 to Long.MAX_VALUE/1000.
'Q'	'\u004f'	Milliseconds since the beginning of the epoch starting at 1 January 1970 00:00:00 UTC, i.e. Long.MIN_VALUE to Long.MAX_VALUE. The precision of this value is limited by the resolution of the underlying operating system or hardware.
The following conversion characters are used for formatting dates:

'B'	'\u0042'	Locale-specific full month name, e.g. "January", "February".
'b'	'\u0062'	Locale-specific abbreviated month name, e.g. "Jan", "Feb".
'h'	'\u0068'	Same as 'b'.
'A'	'\u0041'	Locale-specific full name of the day of the week, e.g. "Sunday", "Monday"
'a'	'\u0061'	Locale-specific short name of the day of the week, e.g. "Sun", "Mon"
'C'	'\u0043'	Four-digit year divided by 100, formatted as two digits with leading zero as necessary, i.e. 00 - 99
'Y'	'\u0059'	Year, formatted to at least four digits with leading zeros as necessary, e.g. 0092 equals 92 CE for the Gregorian calendar.
'y'	'\u0079'	Last two digits of the year, formatted with leading zeros as necessary, i.e. 00 - 99.
'j'	'\u006a'	Day of year, formatted as three digits with leading zeros as necessary, e.g. 001 - 366 for the Gregorian calendar. 001 corresponds to the first day of the year.
'm'	'\u006d'	Month, formatted as two digits with leading zeros as necessary, i.e. 01 - 13, where "01" is the first month of the year and ("13" is a special value required to support lunar calendars).
'd'	'\u0064'	Day of month, formatted as two digits with leading zeros as necessary, i.e. 01 - 31, where "01" is the first day of the month.
'e'	'\u0065'	Day of month, formatted as two digits, i.e. 1 - 31 where "1" is the first day of the month.
The following conversion characters are used for formatting common date/time compositions.

'R'	'\u0052'	Time formatted for the 24-hour clock as "%tH:%tM"
'T'	'\u0054'	Time formatted for the 24-hour clock as "%tH:%tM:%tS".
'r'	'\u0072'	Time formatted for the 12-hour clock as "%tI:%tM:%tS %Tp". The location of the morning or afternoon marker ('%Tp') may be locale-dependent.
'D'	'\u0044'	Date formatted as "%tm/%td/%ty".
'F'	'\u0046'	ISO 8601 complete date formatted as "%tY-%tm-%td".
'c'	'\u0063'	Date and time formatted as "%ta %tb %td %tT %tZ %tY", e.g. "Sun Jul 20 16:17:00 EDT 1969".
*/