package com.opl.junitme.alloy.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationReader {
	
	private static ConfigurationReader INSTANCE = null;
	private Properties properties;
	
	private ConfigurationReader() {
		properties = new Properties();
	}
	
	public static ConfigurationReader getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ConfigurationReader();
		}
		return INSTANCE;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public void loadFile(String filepath) throws IOException {
		properties.clear();
		final FileInputStream filepathInputStream = new FileInputStream(filepath);
		properties.load(filepathInputStream);
	}

}
