package com.ngnl.log4j.test;

import org.apache.logging.log4j.LogManager;
import org.junit.Test;

import com.ngnl.log4j.Log4j2Manager;

public class Log4j2ManagerTest {
	
		@Test
		public void testLog4j2Manager (){
			//initialize log4j2 system.
			Log4j2Manager.getInstance().init("\\src\\test\\resources\\log4j2.xml");
			
			LogManager.getRootLogger().debug("root logger");
			LogManager.getLogger("errorLogger").error("errorLogger");
		} 
}
