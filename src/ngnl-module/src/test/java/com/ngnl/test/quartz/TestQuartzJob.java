package com.ngnl.test.quartz;

import com.ngnl.quartz.annotations.QuartzJob;

@QuartzJob(clazz=TestQuartzJob.class, corn="0/1 * * * * ? ")
public class TestQuartzJob implements Runnable {

	@Override
	public void run() {
		System.out.println("quartz job triggled.");
	}

}
