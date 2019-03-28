package com.ngnl.quartz;

import java.net.URL;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MemberUsageScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterNamesScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ngnl.core.utils.Assert;
import com.ngnl.quartz.annotations.QuartzJob;

public class QuartzManager {
	
	static AtomicInteger count = new AtomicInteger(0);
	
	static Scheduler scheduler = null;

	/**
	 * @param packageStr   "com.ngnl.module.test"
	 */
	public static void startQuartz(String packageStr) {

		try{
			synchronized (QuartzManager.class) {
				if (scheduler != null)
					return;
				scheduler = StdSchedulerFactory.getDefaultScheduler();
				scheduler.start();
				
				scanQuartzJob(packageStr);
			}
		}catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	static void scanQuartzJob (String packageStr){
		Collection<URL> urls = ClasspathHelper.forPackage(packageStr);
		Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(urls)
                .setScanners(
                        new SubTypesScanner(false),
                        new TypeAnnotationsScanner(),
                        new FieldAnnotationsScanner(),
                        new MethodAnnotationsScanner(),
                        new MethodParameterScanner(),
                        new MethodParameterNamesScanner(),
                        new MemberUsageScanner()));
		scanQuartzJob(reflections);
	}
	
	static void scanQuartzJob (Reflections reflections){
		Assert.notEmpty(reflections.getConfiguration().getUrls(), "Scan @URL can not be empty .");
		doScanQuartzJob(reflections);
	}
	
	@SuppressWarnings("unchecked")
	static void doScanQuartzJob (Reflections reflections){
		Logger logger = LoggerFactory.getLogger(QuartzManager.class);
		
		Set<Class<?>> clazzes = reflections.getTypesAnnotatedWith(QuartzJob.class);
		for (Class<?> clazz : clazzes){
			logger.debug("Quartz Job: " + clazz.toString() + " loaded.");
			QuartzJob quartzJob = clazz.getAnnotation(QuartzJob.class);
			addSchedulerTask(quartzJob.corn().toString(), clazz.getName());
		}
	}
	
	static void addSchedulerTask(String cron, String className){
		count.incrementAndGet();
		JobDetail job = JobBuilder.newJob(SchedulerTask.class)
				.withIdentity("job" + count.get(), "SchedulerTaskGroup")
				.usingJobData("className", className)
				.build();
		
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("trigger" + count.get(), "SchedulerTaskGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(cron))
				.forJob("job" + count, "SchedulerTaskGroup")
				.build();
		synchronized (QuartzManager.class) {
			try{
				scheduler.scheduleJob(job, trigger);
			}catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop(boolean flag){
		try{
			if (scheduler != null){
				scheduler.shutdown(flag);
			}
		}catch (SchedulerException e) {
			LoggerFactory.getLogger(QuartzManager.class).error(e.getMessage());
		}
	}
}
