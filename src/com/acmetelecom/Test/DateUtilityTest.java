package com.acmetelecom.Test;

import java.util.Date;
import org.junit.Test;

import com.acmetelecom.DateUtility;

public class DateUtilityTest {

	@Test
	public void test() {
		Date date = new Date();
		DateUtility.isInPeakPeriod(date);
		System.out.println(DateUtility.getPeakstarttime());
		System.out.println(DateUtility.getPeakendtime());
	}

}
