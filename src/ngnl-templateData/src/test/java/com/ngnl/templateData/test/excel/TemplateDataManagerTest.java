package com.ngnl.templateData.test.excel;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.LoggerFactory;

import com.ngnl.templateData.TemplateDataManager;
import com.ngnl.templateData.excel.DefaultExcelTemplateDataLoader;

public class TemplateDataManagerTest {

	@Test
	public void testLoadExcel () throws Exception{

		Collection<URL> urls = ClasspathHelper.forPackage("com.ngnl.templateData.test");
		Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(urls)
                .setScanners(
                        new SubTypesScanner(false),
                        new TypeAnnotationsScanner()));
		LoggerFactory.getLogger(TemplateDataManagerTest.class).debug("Root file URL : {}", new File("").getAbsolutePath());
		
		TemplateDataManager.scanTemplateDataMap(reflections);
		ItemTemplateDataMap itemTemplateDataMap = TemplateDataManager.getTemplateDataMap(ItemTemplateDataMap.class);
		TemplateDataManager.loadTemplateData(ItemTemplateDataMap.class, new DefaultExcelTemplateDataLoader());
		
		LoggerFactory.getLogger(TemplateDataManagerTest.class).debug(itemTemplateDataMap.toString());
	}
}
