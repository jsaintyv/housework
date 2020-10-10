package org.housework.server.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	private DateUtils() {		
	}
	
	/**
	 * 
	 */
	public static Calendar buildCalendar(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month-1, day);		
		return cal;
	}
	
	/**
	 * 
	 */
	public static Date buildDate(int year, int month, int day) {
		Calendar cal = buildCalendar(year, month, day);			
		return cal.getTime();
	}
}
