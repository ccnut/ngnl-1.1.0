package com.ngnl.quartz.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
* @author 47.
* Quartz定时job
*/
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.TYPE)
public @interface QuartzJob {
	//定时任务job类
	public Class<? extends Runnable> clazz();	
	//corn触发规则
	public String corn();
}
