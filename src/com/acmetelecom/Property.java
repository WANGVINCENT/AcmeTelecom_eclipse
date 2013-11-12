package com.acmetelecom;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Property {
	
	private static Properties properties = new Properties();
	private final static String filePath = "resources/config.properties";
	
	public static Properties getInstance() {
		
		InputStream stream = Property.class.getClassLoader().getResourceAsStream(filePath);
		
		try {
			properties.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return properties;
	}
}
