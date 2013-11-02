package com.acmetelecom;

import java.util.Calendar;
import java.util.Date;

public class DateUtility {
	
	private static Calendar calendar = Calendar.getInstance();
	
	//Test if the call time is during the 7h-19h period
	public static boolean isInPeakPeriod(Date time) {
    	int hour = getHour(time);
        return hour >= 7 && hour < 19;
    }
	
	//Get the hour of the day for a given time
	public static int getHour(Date time) {
		calendar.setTime(time);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
	
	//Get the minutes of the hour for a given time
	public static int getMinute(Date time) {
		calendar.setTime(time);
        return calendar.get(Calendar.MINUTE);
    }
	
	//Get the seconds of the minute for a given time
	public static int getSecond(Date time) {
		calendar.setTime(time);
        return calendar.get(Calendar.SECOND);
    }
}
