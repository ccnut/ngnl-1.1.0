package com.ngnl.log4j;

import java.io.FileInputStream;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

public class Log4j2Manager {
	
	private static Log4j2Manager instance = new Log4j2Manager();
	
	public static Log4j2Manager getInstance (){
		return instance;
	}
	
	/**
	 * Initialize log4j system.
	 * @param configFileRelativePath  Relative path of {@code log4j2.xml}, perhaps "\\config\\log4j2.xml"
	 */
	public void init(String configFileRelativePath){
		try {
			String config=System.getProperty("user.dir");
			ConfigurationSource source = new ConfigurationSource(new FileInputStream(config+ configFileRelativePath));
			Configurator.initialize(null, source);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
