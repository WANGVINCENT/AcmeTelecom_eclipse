package com.acmetelecom;

import java.util.Calendar;
import java.util.Date;


public class DateUtility {
	
	private static Calendar calendar = Calendar.getInstance();
	private static int PEAKSTARTTIME;
	private static int PEAKENDTIME;
	
	//Test if the time is during the peak period
	public static boolean isInPeakPeriod(Date time) {
		
		try {
			PEAKSTARTTIME = Integer.valueOf(Property.getInstance().getProperty("peakstarttime"));
			PEAKENDTIME = Integer.valueOf(Property.getInstance().getProperty("peakendtime"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
    	return getHour(time) >= PEAKSTARTTIME  && getHour(time) < PEAKENDTIME;
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

	public static int getPeakstarttime() {
		return PEAKSTARTTIME;
	}

	public static int getPeakendtime() {
		return PEAKENDTIME;
	}
}
