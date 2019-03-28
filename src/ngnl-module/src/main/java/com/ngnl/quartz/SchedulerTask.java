package com.ngnl.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerTask implements Job {
	//日志
	protected Logger logger = LoggerFactory.getLogger(SchedulerTask.class);
	public SchedulerTask(){}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getMergedJobDataMap();
		String className = data.getString("className");
		
		try{
			Class<?> c = Class.forName(className);
			Runnable job = (Runnable)c.newInstance();
			job.run();
		}catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
		}catch (ClassCastException e) {
			logger.error(e.getMessage());
		}catch (IllegalAccessException e) {
			logger.error(e.getMessage());
		}catch (InstantiationException e) {
			logger.error(e.getMessage());
		}
	}
}
