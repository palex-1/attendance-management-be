package it.palex.attendanceManagement.library.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;

/**
 *
 * @author Alessandro Pagliaro
 *
 */
public class DateUtility {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DateUtility.class);
	
	/**
	 * Note the date is converted to UTC but the TimeZone remains the default. 
	 * So if you call more time this method you will get a conversion error.<br>
	 * Example you have date <b>2020-02-17T14:24:34.338 CET</b> if you call this method you will get 
	 * a new date <b>2020-02-17T13:24:34.338+0000 CET</b> and if you call again you will get 
	 * <b>2020-02-17T12:24:34.338+0000 CET</b> cause the time zone is CET on your computer.
	 * So only first call will give you the date <b>value</b> in UTC with the time zone of JVM.
	 * 
	 * @param date
	 * @return
	 */
	public static Date dateToUTC(Date date) {
		if(date==null) {
			throw new NullPointerException();
		}
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d;
		try {
			d = dateFormatLocal.parse( dateFormatGmt.format(date) );
		} catch (ParseException e) {
			LOGGER.error("ParseException parsing date. broken jre");
			throw new RuntimeException("ParseException parsing date. broken jre");
		}
		return d;
	}
	
	/**
	 * Note the current date is converted to UTC but the TimeZone remains the default of jvm. 
	 * TimeZone.setDefault(TimeZone.getTimeZone(timezone));
	 * 
	 * @return Date in UTC 
	 */
	public static Date getCurrentDateInUTC(){
		Date date = new Date();
		
		return dateToUTC(date);
	}
	
	public static Calendar getCurrentCalendarInUTC(){
		Date date = getCurrentDateInUTC();
		return dateToCalendar(date);
	}
		
	public static Calendar dateToCalendar(Date date){
		if(date==null){
			throw new NullPointerException();
		}
		Calendar park = Calendar.getInstance();
		park.setTime(date);
		return park;
	}
	
	public static Date calendarToDate(Calendar cal){
		if(cal==null){
			throw new NullPointerException();
		}
		return cal.getTime();
	}
	
	
	public static Date getFirstDayOfMonth(Date date) {
		if(date==null) {
			throw new NullPointerException();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		
		return startOfDayOfDate(calendar.getTime());
	}
	
	public static Date getLastDayOfMonth(Date date) {
		if(date==null) {
			throw new NullPointerException();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		return endOfDayOfDate(calendar.getTime());
	}
	
	public static Date copy(Date date) {
		if(date==null){
			throw new NullPointerException();
		}
		Calendar park = dateToCalendar(date);
		Calendar cloned = (Calendar) park.clone();
		
		return cloned.getTime();
	}
	
	public static Calendar copy(Calendar dateOfDailyResume) {
		if(dateOfDailyResume==null){
			throw new NullPointerException();
		}
		Calendar cloned = (Calendar) dateOfDailyResume.clone();
		
		return cloned;
	}
	
	public static Date startOfDayOfDate(Date date) {
		if(date==null){
			throw new NullPointerException();
		}
		Calendar park = dateToCalendar(date);
		Calendar cloned = (Calendar) park.clone();
		
		return DateUtils.truncate(cloned.getTime(), Calendar.DATE);
	}

	public static Date endOfDayOfDate(Date date) {
		if(date==null){
			throw new NullPointerException();
		}
		Calendar park = dateToCalendar(date);
		Calendar cloned = (Calendar) park.clone();
		
		return DateUtils.addMilliseconds(DateUtils.ceiling(cloned.getTime(), Calendar.DATE), - 1);
	}
	
	/**
	 * 
	 * @param date
	 * @return the <strong>date</strong> in a string format <strong>yyyy-MM-dd HH:mm:ss</strong>
	 */
	public static String dateToStandardTimestampString(Date date){
		if(date==null){
			throw new NullPointerException();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
    
	/**
	 * 
	 * @param date
	 * @return the date in a string format <strong>yyyy-MM-dd</strong>
	 * @throws NullPointerException if <strong>date</strong> is null
	 */
	public static String dateToStandardDateString(Date date){
		if(date==null){
			throw new NullPointerException();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
	
	
	
	
	/**
	 * 
	 * @param date
	 * @return the <strong>date</strong> in a string format <strong>yyyy-MM-dd HH:mm:ss</strong>
	 */
	public static String dateToStandardTimestampString(Calendar date){
		if(date==null){
			throw new NullPointerException();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
    
	/**
	 * 
	 * @param date
	 * @return the date in a string format <strong>yyyy-MM-dd</strong>
	 * @throws NullPointerException if <strong>date</strong> is null
	 */
	public static String dateToStandardDateString(Calendar date){
		if(date==null){
			throw new NullPointerException();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param data
	 * @return the data formatted with format: dd-MM-yyyy
	 * if the data is null will be returned a empty string
	 */
	public static String getFormattedDataDDMMYYYY(Calendar data){
		if(data==null){
			return "";
		}
		return getFormattedDataDDMMYYYY(data.getTime());
	}
	
	/**
	 * 
	 * @param data
	 * @return the data formatted with format: dd-MM-yyyy
	 * if the data is null will be returned a empty string
	 */
	public static String getFormattedDataDDMMYYYY(Date data){
		if(data==null){
			return "";
		}
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		formatter.setLenient(false);
	    String res = formatter.format(data);
		return res;
	}

	/**
	 * 
	 * @param data with the format dd-MM-yyyy HH:mm:ss
	 * @return the data
	 * @throws NullPointerException if the data is null
	 * @throws ParseException if the format is wrong
	 */
	public static Date getDateFromStringDDMMYYYYHHMMSS(String date) throws ParseException{
		if(date==null){
			throw new NullPointerException();
		}
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		format.setLenient(false);
		return format.parse(date.trim());
	}
	
	/**
	 * 
	 * @param data with the format dd-MM-yyyy HH:mm:ss
	 * @return the data
	 * @throws NullPointerException if the data is null
	 * @throws ParseException if the format is wrong
	 */
	public static Calendar getCalendarFromStringDDMMYYYYHHMMSS(String date) throws ParseException{
		if(date==null){
			throw new NullPointerException();
		}
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		format.setLenient(false);
		Date park = format.parse(date.trim());
		Calendar cal = Calendar.getInstance();
		cal.setTime(park);
		return cal;
	}
	
	
	
	
	
	/**
	  * @return the Calendar of the easter day, Example: findHolyDay(2017) will return a Calendar : Sun Apr 16 00:00:00 CEST 2017
	  * @throws IllegalArgumentexception If the year is before 1582 (since the algorithm only works on the Gregorian calendar).
	  * 
	   * Compute the day of the year that Easter falls on. Step names E1 E2 etc.,
	   * are direct references to Knuth, Vol 1, p 155. 
	   */
	  public static final Calendar findHolyDay(int year) {
	    if (year <= 1582) {
	      throw new IllegalArgumentException("Algorithm invalid before April 1583");
	    }
	    int golden, century, x, z, d, epact, n;

	    golden = (year % 19) + 1; /* E1: metonic cycle */
	    century = (year / 100) + 1; /* E2: e.g. 1984 was in 20th C */
	    x = (3 * century / 4) - 12; /* E3: leap year correction */
	    z = ((8 * century + 5) / 25) - 5; /* E3: sync with moon's orbit */
	    d = (5 * year / 4) - x - 10;
	    epact = (11 * golden + 20 + z - x) % 30; /* E5: epact */
	    if ((epact == 25 && golden > 11) || epact == 24)
	      epact++;
	    n = 44 - epact;
	    n += 30 * (n < 21 ? 1 : 0); /* E6: */
	    n += 7 - ((d + n) % 7);
	    if (n > 31){ /* E7: */
	      return new GregorianCalendar(year, 4 - 1, n - 31); /* April */
	    }
	    else{
	      return new GregorianCalendar(year, 3 - 1, n); /* March */
	    }
	  }


  /**
	 * Add negative value to subtract
	 * @param currentDateInUTC
	 * @param years
	 * @return
	 */
	public static Date addYearsToDate(Date date, int years) {
		return addTimeUnitToDate(date, Calendar.YEAR, years);
	}

	/**
	 * Add negative value to subtract
	 * @param currentDateInUTC
	 * @param days
	 * @return
	 */
	public static Date addDaysToDate(Date date, int days) {
		return addTimeUnitToDate(date, Calendar.DATE, days);
	}

	/**
	 * Add negative value to subtract
	 * @param cal
	 * @param days
	 * @return
	 */
	public static Calendar addDaysToCalendar(Calendar cal, int days){
		return addTimeUnitToCalendar(cal, Calendar.DATE, days);
	}

	/**
	 * Add negative value to subtract
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static Date addMinutesToDate(Date date, int minutes) {
		return addTimeUnitToDate(date, Calendar.MINUTE, minutes);
	}
	
	/**
	 * Add negative value to subtract
	 * @param cal
	 * @param minutes
	 * @return
	 */
	public static Calendar addMinutesToCalendar(Calendar cal, int minutes){
		return addTimeUnitToCalendar(cal, Calendar.MINUTE, minutes);
	}
	
	/**
	 * Add negative value to subtract
	 * @param creationDate
	 * @param seconds
	 * @return
	 */
	public static Date addSecondsToDate(Date date, int seconds) {
		return addTimeUnitToDate(date, Calendar.SECOND, seconds);
	}
	
	/**
	 * Add negative value to subtract
	 * @param cal
	 * @param seconds
	 * @return
	 */
	public static Calendar addSecondsToCalendar(Calendar cal, int seconds){
		return addTimeUnitToCalendar(cal, Calendar.SECOND, seconds);
	}
	
	/**
	 * 
	 * @param date
	 * @param hours
	 * @return 
	 */
	public static Date addHoursToDate(Date date, int hours) {
		return addTimeUnitToDate(date, Calendar.HOUR, hours);
	}
	
	/**
	 * 
	 * @param cal
	 * @param hours
	 * @return
	 */
	public static Calendar addHoursToDate(Calendar cal, int hours) {
		return addTimeUnitToCalendar(cal, Calendar.HOUR, hours);
	}

    /**
	 * 
	 * @param date
	 * @param months
	 * @return
	 */
	public static Date addMonthToDate(Date date, int months) {
		return addTimeUnitToDate(date, Calendar.MONTH, months);
	}
	
	/**
	 * 
	 * @param cal
	 * @param months
	 * @return
	 */
	public static Calendar addMonthToCalendar(Calendar cal, int months) {
		return addTimeUnitToCalendar(cal, Calendar.MONTH, months);
	}
	
	/**
	 * Add negative value to subtract
	 * @param date
	 * @param seconds
	 * @return
	 */
	public static Date addSecondsToCalendar(Date date, int seconds){
		if(date==null){
			throw new NullPointerException();
		}
		return calendarToDate(addTimeUnitToCalendar(dateToCalendar(date), Calendar.SECOND, seconds));
	}
	
	private static Date addTimeUnitToDate(Date date, int timeUnit, int value){
		if(date==null){
			throw new NullPointerException();
		}
		Calendar park = dateToCalendar(date);
		
		return calendarToDate(addTimeUnitToCalendar(park, timeUnit, value));
	}
	
	private static Calendar addTimeUnitToCalendar(Calendar cal, int timeUnit, int value){
		if(cal==null){
			throw new NullPointerException();
		}
		Calendar park = (Calendar) cal.clone();
		park.add(timeUnit, value);
		
		return park;
	}

	/**
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @throws NullPointerException if date1 or date2 is null
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		if(date1==null || date2==null) {
			throw new NullPointerException();
		}
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		
		boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
		                  cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		
		return sameDay;
	}

	
	
	
}

