package com.ngnl.core.utils;

import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;

import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * Used to load properties-file and get values that defined in file.
 * @author 47
 *
 */
public class ConfigPropertiesUtils {
	
	static Configuration config = null;
	
	static {
		init();
	}
	
	public static void init () {
		CompositeConfiguration compositeConfig = new CompositeConfiguration();
		try {
			Configurations configs = new Configurations();
			Configuration baseConfig = configs.properties(new File("config.properties"));
			compositeConfig.addConfiguration(baseConfig);
			
			String parentFolder = compositeConfig.getString("properties.folder", "config");
			String[] fileNames = compositeConfig.getStringArray("properties.fileName");
			
			Arrays.asList(fileNames).forEach((fileName)->{
				try {
					Configuration exConfig = configs.properties(new File(parentFolder + fileName));
					compositeConfig.addConfiguration(exConfig);
				} catch (ConfigurationException e) {
					e.printStackTrace();
				}
			});
			
			ConfigPropertiesUtils.config = compositeConfig;
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static String getString(String key) {
		return config.getString(key);
	}

	public static String getString(String key, String defaultValue) {
		return config.getString(key, defaultValue);
	}

	/**
	 * equate to: MessageFormat.format("congratulations, {0}.", name); 
	 * @param key
	 * @param arguments
	 * @return
	 */
	public static String getFormatString(String key, Object... arguments) {
		return MessageFormat.format(config.getString(key), arguments);
	}

	/**
	 * get formatted string with default value. compare to {@code getFormatString(String key, Object... arguments)}
	 * @param key
	 * @param defaultValue
	 * @param parse
	 * @return
	 */
	public static String getFormatString(String key, String defaultValue, Object... parse) {
		String s = config.getString(key, defaultValue);
		if (s != null && !"".equals(s)) {
			return MessageFormat.format(s, parse);
		}
		return s;
	}

	public static String[] getStringArray(String key) {
		return config.getStringArray(key);
	}

	public static boolean getBoolean(String key) {
		return config.getBoolean(key);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return config.getBoolean(key, defaultValue);
	}

	public static int getInt(String key) {
		return config.getInt(key);
	}

	public static int getInt(String key, int defaultValue) {
		return config.getInt(key, defaultValue);
	}

	public static long getLong(String key) {
		return config.getLong(key);
	}

	public static long getLong(String key, long defaultValue) {
		return config.getLong(key, defaultValue);
	}

	public static float getFloat(String key) {
		return config.getFloat(key);
	}

	public static float getFloat(String key, float defaultValue) {
		return config.getFloat(key, defaultValue);
	}
}
