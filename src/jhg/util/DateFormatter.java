package jhg.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@SuppressWarnings("unused")
public class DateFormatter {

	public static final String ERA = "G";//AD,BC
	public static final String YR_4 = "yyyy";
	public static final String YR_2 = "yy";
	public static final String WEEK_YEAR = "Y";//For a date where the day is part of next year's week, it's next year.
	public static final String MO_1 = "M";   //1 digit  1
	public static final String MO_2 = "MM";   //2 digit  01
	public static final String MO_3 = "MMM";  //3 letter Jan
	public static final String MO_FULL = "MMMM";  //3 letter Jan
	public static final String DAY_NO_LEADING = "d";  //Jan 4
	public static final String DAY_LEADING = "dd";  //Jan 04
	public static final String DOW_3 = "E";  //Wed
	public static final String DOW_FULL = "EEEE";  //Wed
	public static final String DOW_NUM = "u";  //Mon=1
	public static final String DOW_IN_MO = "F";  //2nd Tues of month.
	public static final String HR12AMPM = "a";  //am pm
	public static final String HOUR = "H";//0-23 based
	public static final String HOURz = "k";//1-24 based.
	public static final String HOUR12 = "h";//1-12
	public static final String HOUR12z = "K";//0-11
	public static final String MINUTE = "m";
	public static final String SECOND = "s";
	public static final String MILLISECOND = "S";
	public static final String TIMEZONE_FULL = "zzzz";//Pacific Standard Time
	public static final String TIMEZONE_ABBREV = "zzz";//PST
	public static final String TIMEZONE_R = "Z";//RFC 822    +0000
	public static final String TIMEZONE_I = "XX";//ISO 8601 -08: -0800  -08:00
	
	public static final String MMDDYYY = "MM/dd/yyyy";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String LETTER = "EEEE, MMMM dd, yyyy";
	public static final String TIME24GMT = "HH:mm:ss.SXXX";
	public static final String TIME24TZ = "HH:mm:ss zzz";
	public static final String TIME12 = "h:mm a";
	
	
	public static void main(String[] args) {
		Date date = new Date();
		String allComponents = "XXX X ':' ZZZZ  ZZ Z ':' zzzz zzz z ':'   S s mm kk k HH H a u EEEE(F EEE 'of month') 'day:'d(D) 'week:'W(w) MMMM Y y G";
		SimpleDateFormat df = new SimpleDateFormat(allComponents);
		df.setTimeZone(TimeZone.getTimeZone("EST"));
		Log.println(df.format(date));
		
	}
	
}
