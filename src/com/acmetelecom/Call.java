package com.acmetelecom;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Call {
    private CallEvent start;
    private CallEvent end;
    private Type type;
    private final int SECONDS_IN_DAY = 24 * 60 * 60;
    private final int SECONDS_IN_HOUR = 60 * 60;
    
    public Call(CallEvent start, CallEvent end) {
        this.start = start;
        this.end = end;
    }

    public String callee() {
        return start.getCallee();
    }
	
	
    
    //Calculate the on peak duration time in second according to different types
    public int onPeakDurationSeconds(Type type) {
    	int time = 0;
    	
    	switch(type) {
    		case isBetweenSeven:
				time = (DateUtility.getHour(endTime()) - 7) * SECONDS_IN_HOUR 
						+ DateUtility.getMinute(endTime()) * 60 
						+ DateUtility.getSecond(endTime());
				break;
    		case isBetweenNineteen: 
				time = (DateUtility.getHour(endTime()) - 19) * SECONDS_IN_HOUR 
						+ DateUtility.getMinute(endTime()) * 60 
						+ DateUtility.getSecond(endTime());
				break;
    		case isOnPeak:
    			time = (int) (((end.time() - start.time()) / 1000));
    			break;
    		case isOffPeak:
    			time = 0;
    			break;
    		case isCoverPeak:
    			time = SECONDS_IN_DAY / 2;
    			break;
    		case isCoverOffPeak:
    			time = (19 - DateUtility.getHour(endTime()) + DateUtility.getHour(startTime()) - 7) * SECONDS_IN_HOUR;
    			break;
		}
    	
    	return time;
    }
    
    //Calculate the off peak duration time in second according to different types
    public int offPeakDurationSeconds(Type type) {
    	int time = 0;
    	
    	switch(type) {
    		case isBetweenSeven:
				time = (7 - DateUtility.getHour(startTime())) * SECONDS_IN_HOUR 
						+ DateUtility.getMinute(startTime()) * 60 
						+ DateUtility.getSecond(startTime());
				break;
    		case isBetweenNineteen: 
				time = (19 - DateUtility.getHour(startTime())) * SECONDS_IN_HOUR 
						+ DateUtility.getMinute(startTime()) * 60 
						+ DateUtility.getSecond(startTime());
				break;
    		case isOnPeak:
    			time = 0;
    			break;
    		case isOffPeak:
    			time = (int) (((end.time() - start.time()) / 1000));
    			break;
    		case isCoverPeak:
    			//Two ways of calculating this time
    			//time = (7 - DateUtility.getHour(startTime()) + DateUtility.getHour(endTime()) - 19) * SECONDS_IN_HOUR;
    			time = (int) (((end.time() - start.time()) / 1000) - SECONDS_IN_DAY / 2);
    			break;
    		case isCoverOffPeak:
    			time = SECONDS_IN_DAY / 2;
    	}
    	
    	return time;
    }
    
    //Get the total duration in second
    public int durationSeconds() {
    	return (int) (((end.time() - start.time()) / 1000));
    }

    public String date() {
        return SimpleDateFormat.getInstance().format(new Date(start.time()));
    }

    public Date startTime() {
        return new Date(start.time());
    }

    public Date endTime() {
        return new Date(end.time());
    }
    
    //Get the type of the call
    public Type getType() {
    	
    	//if start time is between 7H and 19H
    	boolean startTimeType = DateUtility.isInPeakPeriod(startTime());
    	//if end time is between 7H and 19H
    	boolean endTimeType = DateUtility.isInPeakPeriod(endTime());
    	
    	//Assume that all calls period are less than 24 hours
    	if(durationSeconds() < SECONDS_IN_DAY) {
    		if(!startTimeType && endTimeType) {
    	    	type = Type.isBetweenSeven; 	//the type that start time and end time is between 7H
    	    } else if(startTimeType && !endTimeType){
    	    	type = Type.isBetweenNineteen;	//the type that start time and end time is between 19H
    	    } else if(startTimeType && endTimeType && durationSeconds() < SECONDS_IN_DAY / 2) {
    	    	type = Type.isOnPeak;			//the type of both time are in peak period
    	    } else if (startTimeType && endTimeType && durationSeconds() > SECONDS_IN_DAY / 2) {
    	    	type = Type.isCoverOffPeak;		//the type of both time are in Peak period and call period covers the offPeak period
    	    } else if (!startTimeType && !endTimeType && durationSeconds() > SECONDS_IN_DAY / 2) {
    	    	type = Type.isCoverPeak;		//the type of both time are in offPeak period and call period covers the peak period
    	    } else if(!startTimeType && !endTimeType && durationSeconds() < SECONDS_IN_DAY / 2){								
    	    	type = Type.isOffPeak;			//the type of both time are in offPeak period and call period doesn't cover the peak period
    	    }
    	}
    	
		
    	return type;
    }
}
