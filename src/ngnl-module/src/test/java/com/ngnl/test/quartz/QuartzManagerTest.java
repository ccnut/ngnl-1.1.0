package com.ngnl.test.quartz;

import com.ngnl.quartz.QuartzManager;

public class QuartzManagerTest {

	public static void main(String[] args) {
		String packageStr = "com.ngnl.test.quartz";
		QuartzManager.startQuartz(packageStr);
	}
}
