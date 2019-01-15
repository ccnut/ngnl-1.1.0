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
	 * Template data file's URL string.<br>
	 * e.x.: If the file URL is "templateData/item.xml",  use the file name of "item.excel".
	 * @return Template data file's URL string.
	 */
	String fileName() default "";
}
