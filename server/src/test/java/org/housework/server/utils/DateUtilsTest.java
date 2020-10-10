package org.housework.server.utils;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DateUtilsTest {

	@Test
	public void testDateMillisClear() {
		Date date = DateUtils.buildDate(2020, 7, 7);
		Assertions.assertEquals(0, date.getTime()%1000);
	}
	
	@Test
	public void testDate() {
		Date date = DateUtils.buildDate(2020, 7, 7);
		Calendar cal =  Calendar.getInstance();
		cal.setTime(date);
		Assertions.assertEquals(cal.get(Calendar.DAY_OF_MONTH), 7);
		Assertions.assertEquals(cal.get(Calendar.MONTH), 6);
		Assertions.assertEquals(cal.get(Calendar.YEAR), 2020);
	}
}
