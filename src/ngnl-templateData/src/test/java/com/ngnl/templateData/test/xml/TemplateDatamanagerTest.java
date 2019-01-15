package com.ngnl.templateData.test.xml;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.LoggerFactory;

import com.ngnl.templateData.TemplateDataManager;
import com.ngnl.templateData.xml.DefaultXMLTemplateDataLoader;

/**
 * @author 47
 * 测试xml模板数据加载/映射  与 获取
 */
public class TemplateDatamanagerTest {
	
	@BeforeClass
	public static void beforeClass() throws Exception{
		Collection<URL> urls = ClasspathHelper.forPackage("com.ngnl.templateData.test");
		Reflections reflections = new Reflections(new ConfigurationBuilder()
						                .setUrls(urls)
						                .setScanners(new SubTypesScanner(false),
						                			 new TypeAnnotationsScanner()));
		LoggerFactory.getLogger(TemplateDatamanagerTest.class).info("Project Root: {}", new File("").getAbsolutePath());
		
		TemplateDataManager.scanTemplateDataMap(reflections);
		
		DefaultXMLTemplateDataLoader templateDataLoader = new DefaultXMLTemplateDataLoader();
		templateDataLoader.setRootFile("com/ngnl/templateData/test/xml/");
		TemplateDataManager.loadTemplateData(T23TemplateDataMap.class, templateDataLoader);
	}
	
	@Test
	public void testGetData () {
		T23TemplateDataMap t23TemplateDataMap = TemplateDataManager.getTemplateDataMap(T23TemplateDataMap.class);
		T23TemplateData data = t23TemplateDataMap.get(101);
		LoggerFactory.getLogger(TemplateDatamanagerTest.class).info(data.toString());
	}
}
