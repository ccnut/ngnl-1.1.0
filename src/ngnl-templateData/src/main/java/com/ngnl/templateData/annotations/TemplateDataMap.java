package com.ngnl.templateData.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ngnl.templateData.AbstractTemplateData;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME) 
public @interface TemplateDataMap {

	Class<? extends AbstractTemplateData> templateDataClazz();
	
	/**
	 * Template data file's URL string.
	 * e.x.: "templateData/item.xml",  "templateData/item.excel" 
	 * @return Template data file's URL string.
	 */
	String fileURL() default "";
}
