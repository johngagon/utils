package jhg.calendar;

import java.text.*;
import java.util.*;

import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;

/**
 * Utilitity class for calendar date calculations.
 * 
 * @author jgagon
 *
 */
public class CalendarCalculator {

	/**
	 * Simple class for holding dates without years.
	 * @author jgagon
	 *
	 */
	public static class D{
		int month;
		int day;
		public D(int m, int d){
			this.month = m;
			this.day = d;
		}
		public int getMonth(){
			return month;
		}
		public int getDay(){
			return day;
		}
	}
	
	public static enum Annual{
		NEW_YEAR(1,1),
		GROUNDHOG(2,2),
		VALENTINES(2,14),
		ST_PATRICK(3,17),
		INDEPENDENCE_DAY(7,4),
		HALLOWEEN(10,31),
		VETERANS(11,11),
		CHRISTMAS(12,25),
		NY_EVE(12,31)
		;
		private int month;
		private int day;
		private Annual(int m, int d){
			month = m; day = d;
		}
	}
	
	public static LocalDate getSuperBowlSunday(int year){
		return getNDayOfMonth(DateTimeConstants.SUNDAY, 1, 2, year);
	}
	public static LocalDate getMothersDay(int year){
		return getNDayOfMonth(DateTimeConstants.SUNDAY, 2, 5, year);
	}
	public static LocalDate getFathersDay(int year){
		return getNDayOfMonth(DateTimeConstants.SUNDAY, 3, 6, year);
	}
	public static LocalDate getThanksgiving(int year){
		return getNDayOfMonth(DateTimeConstants.THURSDAY, 4, 11, year);
	}	
	public static LocalDate getLaborDay(int year){
		return getNDayOfMonth( DateTimeConstants.MONDAY, 1, 9, year);//1st monday in sep
	}
	public static LocalDate getMemorialDay(int year){
		return getLastWeekdayOfMonth( DateTimeConstants.MONDAY, 5, year);//last monday in may
	}
	public static LocalDate getMlkDay(int year){
		return getNDayOfMonth(DateTimeConstants.MONDAY, 3, 1, year);
	}
	public static LocalDate getPresidentsDay(int year){
		return getNDayOfMonth(DateTimeConstants.MONDAY, 3, 2, year);
	}
	public static LocalDate getDaylightSavingsSpringForward(int year){
		return getNDayOfMonth(DateTimeConstants.SUNDAY, 2, 3, year);
	}
	public static LocalDate getDaylightSavingsFallBack(int year){
		return getNDayOfMonth(DateTimeConstants.SUNDAY, 1, 11, year);
	}		
	public static LocalDate getColumbusDay(int year){
		return getNDayOfMonth(DateTimeConstants.MONDAY, 2, 10, year);
	}

	//Gauss 1800s calculations is only good for 18th and 19th century and gets less accurate for 20th century was correct for 2016
	public static LocalDate getEasterSunday(int year)   {
		int a = year % 19,
            b = year / 100,
            c = year % 100,
            d = b / 4,
            e = b % 4,
            g = (8 * b + 13) / 25,
            h = (19 * a + b - d - g + 15) % 30,
            j = c / 4,
            k = c % 4,
            m = (a + 11 * h) / 319,
            r = (2 * e + 2 * j - k - h + m + 32) % 7,
            n = (h - m + r + 90) / 25,                   //month
            p = (h - m + r + n + 19) % 32;               //day
		LocalDate ld = new LocalDate(year,n,p);
		return ld;
	}	
	
	public static void printFederalCalendar(int year){
		String[] holidayTitles = {"New Year's Day",
				"Birthday of Martin Luther King, Jr.",
				"Washington's Birthday",
				"Daylight Savings Off",
				"Memorial Day",
				"Independence Day",
				"Labor Day",
				"Columbus Day",
				"Daylight Savings On",
				"Veterans Day",
				"Thanksgiving Day",
				"Christmas Day"};//10
		LocalDate[] dates = {
				from(Annual.NEW_YEAR,2016),                       
				getMlkDay(2016),
				getPresidentsDay(2016),
				getDaylightSavingsSpringForward(2016),
				getMemorialDay(2016),
				from(Annual.INDEPENDENCE_DAY,2016),
				getLaborDay(2016),
				getColumbusDay(2016),
				getDaylightSavingsFallBack(2016),
				from(Annual.VETERANS,2016),
				getThanksgiving(2016),
				from(Annual.CHRISTMAS,2016)
				};
		int i=0;
		for(LocalDate d:dates){
			System.out.println(holidayTitles[i]+" :    "+d);
			i++;
		}
	}	

	public static int goldenNumber(int y){
		return y%19+1;
	}
	public static LocalDate from(D d, int year){
		return new LocalDate(year,d.month,d.day);
	}
	public static LocalDate from(Annual s, int year){
		LocalDate d = new LocalDate(year,s.month,s.day);
		return d;
	}
	
	public static LocalDate getNDayOfMonth(int dayweek,int nthweek,int month,int year)  {
		LocalDate d = new LocalDate(year, month, 1).withDayOfWeek(dayweek);
		if(d.getMonthOfYear() != month) d = d.plusWeeks(1);
		return d.plusWeeks(nthweek-1);
	}

	public static LocalDate getLastWeekdayOfMonth(int dayweek,int month,int year) {
		LocalDate d = new LocalDate(year, month, 1).plusMonths(1).withDayOfWeek(dayweek);
		if(d.getMonthOfYear() != month) d = d.minusWeeks(1);
		return d;
	}
	
	public static D d(int a,int b){
		return new D(a,b);
	}
	
	public static LocalDate[] from(D[] dates, int year){
		LocalDate[] rv = new LocalDate[dates.length];
		int i=0;
		for(D d:dates){
			rv[i]=new LocalDate(year,d.month,d.day);
			i++;
		}
		return rv;
	}
	
	/**
	 * Lunar Calendar table.
	 * For a given year, get a list of full moon dates.
	 */
	public static LocalDate[] getFullMoons(int year){
		D[] d1  = {d(1,16) ,d(2,14) ,d(3,16) ,d(4,15) ,d(5,14) ,d(6,13) ,d(7,12) ,d(8,10) ,d(9,9)  ,d(10,8)  ,d(11,6)  ,d(12,6)           };
		D[] d2  = {d(1,5)  ,d(2,3)  ,d(3,5)  ,d(4,4)  ,d(5, 4) ,d(6,2)  ,d(7,2)  ,d(7,31) ,d(8,29) ,d(9,28)  ,d(10,27) ,d(11,25) ,d(12,25)};
		D[] d3  = {d(1,23) ,d(2,22) ,d(3,24) ,d(4,22) ,d(5,22) ,d(6,20) ,d(7,20) ,d(8,18) ,d(9,16) ,d(10,16) ,d(11,14) ,d(12,14)          };
		D[] d4  = {d(1,12) ,d(2,11) ,d(3,13) ,d(4,11) ,d(5,11) ,d(6,10) ,d(7, 9) ,d(8, 8) ,d(9, 6) ,d(10, 5) ,d(11, 4) ,d(12, 3)          };
		D[] d5  = {d(1, 1) ,d(1,31) ,d(3, 1) ,d(3,31) ,d(4,30) ,d(5,30) ,d(6,28) ,d(7,28) ,d(8,26) ,d(9,25)  ,d(10,24) ,d(11,23) ,d(12,22)};
		D[] d6  = {d(1,21) ,d(2,19) ,d(3,20) ,d(4,18) ,d(5,18) ,d(6,16) ,d(7,16) ,d(8,15) ,d(9,13) ,d(10,13) ,d(11,11) ,d(12,11)          };
		D[] d7  = {d(1,9)  ,d(2, 8) ,d(3, 9) ,d(4, 8) ,d(5,07) ,d(6, 6) ,d(7, 5) ,d(8, 4) ,d(9, 2) ,d(10, 2) ,d(11, 1) ,d(11,30) ,d(12,30)};
		D[] d8  = {d(1,28) ,d(2,27) ,d(3,28) ,d(4,27) ,d(5,26) ,d(6,24) ,d(7,24) ,d(8,22) ,d(9,21) ,d(10,21) ,d(11,20) ,d(12,19)          };
		D[] d9  = {d(1,18) ,d(2,16) ,d(3,18) ,d(4,16) ,d(5,16) ,d(6,14) ,d(7,13) ,d(8,12) ,d(9,10) ,d(10,10) ,d(11, 9) ,d(12, 8)          };
		D[] d10 = {d(1, 7) ,d(2, 6) ,d(3, 6) ,d(4, 5) ,d(5,04) ,d(6, 3) ,d(7, 2) ,d(7,31) ,d(8,30) ,d( 9,28) ,d(10,28) ,d(11,26) ,d(12,26)};
		D[] d11 = {d(1,25) ,d(2,24) ,d(3,25) ,d(4,24) ,d(5,23) ,d(6,22) ,d(7,21) ,d(8,19) ,d(9,18) ,d(10,17) ,d(11,16) ,d(12,15)          };
		D[] d12 = {d(1,14) ,d(2,13) ,d(3,14) ,d(4,13) ,d(5,13) ,d(6,11) ,d(7,11) ,d(8, 9) ,d(9, 7) ,d(10, 7) ,d(11, 5) ,d(12, 5)          };
		D[] d13 = {d(1, 3) ,d(2, 2) ,d(3, 3) ,d(4, 2) ,d(5, 2) ,d(6, 1) ,d(6,30) ,d(7,30) ,d(8,28) ,d(9,26)  ,d(10,26) ,d(11,24) ,d(12,24)};
		D[] d14 = {d(1,22) ,d(2,21) ,d(3,21) ,d(4,20) ,d(5,20) ,d(6,18) ,d(7,18) ,d(8,16) ,d(9,15) ,d(10,14) ,d(11,13) ,d(12,12)          };
		D[] d15 = {d(1,11) ,d(2, 9) ,d(3,11) ,d(4, 9) ,d(5, 9) ,d(6, 7) ,d(7, 7) ,d(8, 6) ,d(9, 4) ,d(10, 4) ,d(11, 2) ,d(12, 2), d(12,31)};
		D[] d16 = {d(1,30) ,d(2,28) ,d(3,30) ,d(4,28) ,d(5,27) ,d(6,26) ,d(7,26) ,d(8,24) ,d(9,23) ,d(10,23) ,d(11,21) ,d(12,21)          };
		D[] d17 = {d(1,19) ,d(2,18) ,d(3,19) ,d(4,18) ,d(5,17) ,d(6,15) ,d(7,15) ,d(8,13) ,d(9,12) ,d(10,12) ,d(11,10) ,d(12,10)          };
		D[] d18 = {d(1, 9) ,d(2, 7) ,d(3, 8) ,d(4, 6) ,d(5, 6) ,d(6, 4) ,d(7, 3) ,d(8, 2) ,d(8,31) ,d(9,30)  ,d(10,29) ,d(11,28) ,d(12,28)};
		D[] d19 = {d(1,27) ,d(2,25) ,d(3,27) ,d(4,25) ,d(5,25) ,d(6,23) ,d(7,22) ,d(8,21) ,d(9,19) ,d(10,18) ,d(11,17) ,d(12,17)          };
		
		
		D[][] dates = {d1,d2,d3,d4,d5,d6,d7,d8,d9,d10,d11,d12,d13,d14,d15,d16,d17,d18,d19};
		D[] d = dates[goldenNumber(year)-1];
		//1995 Jan 16
		//float cycle = 29.5306;//synodic lunar month
		LocalDate[] rv = from(d,year); 
		
		return rv;
	}
	
	


	

	@SuppressWarnings("boxing")
	public static void main(String[] args){
		
		try{
			final int TESTS = 2;
			int numFail = 0;
			
			numFail += testEqual(5,getLaborDay(2016).getDayOfMonth());
			numFail += testEqual(30,getMemorialDay(2016).getDayOfMonth());
			System.out.println(from(Annual.CHRISTMAS,2016));
			System.out.format("\n\nTests Ran: %s  Fail: %s  Pass: %s",TESTS,numFail,(TESTS-numFail));
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void testPrintFederal(){printFederalCalendar(2016);}
	public static void testGoldenNumber(){System.out.println("Golden Number 2016: "+goldenNumber(2016));  }
	public static void testEaster(){System.out.println("Easter Sunday: "+getEasterSunday(2016)); }
	public static void testMoonsThisYear(){
		LocalDate[] months = getFullMoons(2016);
		for(LocalDate ld:months){
			System.out.println(ld);
		}		
	}
	public static void testSynodicMonthsInMetonicCycle(){
		final int lo = 27;
		final int hi = 31;
		LocalDate lastMonth = null;
		//go through each year.
		int n = 1;//235 synodic months (lunar phases) = 6,939.688 days (Metonic period by definition).
		//https://en.wikipedia.org/wiki/Metonic_cycle
		for(int i=2014;i<=2032;i++){
			LocalDate[] months = getFullMoons(i);
			for(LocalDate ld:months){
				if(lastMonth==null){
					lastMonth = ld;
					System.out.format("%s This full moon: %s    \n",String.valueOf(n),ld);
				}else{
					int daysbetween = Days.daysBetween(lastMonth, ld).getDays();
					boolean inLimits = daysbetween <=hi && daysbetween>=lo;
					System.out.format("%s Last full moon: %s  This full moon: %s   between : %s  inLimits: %s \n",String.valueOf(n),lastMonth,ld,String.valueOf(daysbetween),String.valueOf(inLimits) );
					lastMonth = ld;
				}
				//System.out.println(ld);
				n++;
			}
			
		}
	}


	public static int testEqual(int expected, int actual){
		int failCount = 0;
		if(expected!=actual){
			System.out.format("Fail :  Date actual: '%s' != expected: '%s' \n",actual+"",expected+"");
			failCount = 1;
		}else{
			System.out.format("Pass :  Date actual: '%s' == expected: '%s' \n",actual+"",expected+"");
		}
		return failCount;		
	}
	public static int testDateEquals(Date expected, Date actual)  {
		int failCount = 0;
		if(!expected.equals(actual)){
			System.out.format("Fail :  Date actual: '%s1' != expected: '%s2' ",actual,expected);
			failCount = 1;
		}else{
			System.out.format("Pass :  Date actual: '%s1' == expected: '%s2' ",actual,expected);
		}
		return failCount;
	}
	public static int testDateEquals(String expected, String actual) {
		Date d1 = new Date(); 
		Date d2 = new Date();
		try {
			d1 = parseDate(expected);
			d2 = parseDate(actual);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return testDateEquals(d1,d2);
	}
	
	private static final String parseStr = "yyyyMMdd";
	public static String formatDate(Date d){
		return new SimpleDateFormat(parseStr).format(d);
	}
	public static Date parseDate(String date) throws ParseException{
		
		return new SimpleDateFormat(parseStr).parse(date);
	}
	
	
	/*
	 * FIXME implement all this
	 */
	public static LocalDate adjustObservance(LocalDate d){
		return null;//TODO apply Saturday and Sunday rules for annual observance for fixed federal holidays like Veterans and Christmas
		//If a holiday is on a Saturday, the Friday is observed, if the holiday is on a Sunday, Monday is observed
	}
	public static void printCalendar(int year){
		
	}		
	
}

/**
 * 
 * @param year
 * @return
 
@SuppressWarnings("deprecation")
public static Date thanksgivingObserved(int nYear)
{
    int nX;
    int nMonth = 10; // November
    Date dtD;
    dtD = new Date(nYear, nMonth, 1);
    nX = dtD.getDay();
    switch(nX)
        {
        case 0 : // Sunday
        return new Date(nYear, nMonth, 26);
        case 1 : // Monday
        return new Date(nYear, nMonth, 25);
        case 2 : // Tuesday
        return new Date(nYear, nMonth, 24);
        case 3 : // Wednesday
        return new Date(nYear, nMonth, 23);
        case 4 : // Thursday
        return new Date(nYear, nMonth, 22);
        case 5 : // Friday
        return new Date(nYear, nMonth, 28);
        default : // Saturday
        return new Date(nYear, nMonth, 27);
        }
} 
*/
/*
@SuppressWarnings("deprecation")
public static Date martinLutherKingObserved (int nYear)
{
    // Third Monday in January
    int nX;
    int nMonth = 0; // January
    Date dtD;
    dtD = new Date(nYear, nMonth, 1);
    nX = dtD.getDay();
    switch(nX)
        {
        case 0 : // Sunday
        return new Date(nYear, nMonth, 16);
        case 1 : // Monday
        return new Date(nYear, nMonth, 15);
        case 2 : // Tuesday
        return new Date(nYear, nMonth, 21);
        case 3 : // Wednesday
        return new Date(nYear, nMonth, 20);
        case 4 : // Thursday
        return new Date(nYear, nMonth, 19);
        case 5 : // Friday
        return new Date(nYear, nMonth, 18);
        default : // Saturday
        return new Date(nYear, nMonth, 17);
        }
}	
*/
/*
 	private static Calendar cacheCalendar = Calendar.getInstance();
	
	public static int getFirstMonday(int year, int month) {
		
	    cacheCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	    cacheCalendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
	    cacheCalendar.set(Calendar.MONTH, month);
	    cacheCalendar.set(Calendar.YEAR, year);
	    return cacheCalendar.get(Calendar.DATE);
	}

	public static int getFirstMonday(int year) {
	    return getFirstMonday(year, Calendar.JANUARY);
	}	
	

	public static int getLaborDay(int year){
		Calendar c = getLaborDay();
		c.set(Calendar.YEAR, year);
		return c.get(Calendar.DATE);
	}
	private static Calendar getLaborDay(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
		c.set(Calendar.MONTH, Calendar.SEPTEMBER);
		return c;
	}	
	
	
	
		B_MOM(1,8),
		B_JOHN(5,31),
		B_RAY(11,15);	
	
	
 */
