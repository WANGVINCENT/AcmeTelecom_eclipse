package com.acmetelecom.Test;

import com.acmetelecom.Property;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class PropertyTest {

	@Test
	//Test if the properties can be read
	public void test() throws FileNotFoundException, IOException {
		System.out.println(Property.getInstance().getProperty("peakstarttime"));
		System.out.println(Property.getInstance().getProperty("peakendtime"));
	}

}
