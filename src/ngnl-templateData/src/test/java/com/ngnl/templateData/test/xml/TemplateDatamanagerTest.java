package com.ngnl.templateData.test.xml;

import org.junit.BeforeClass;
import org.junit.Test;
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

		//1.扫描模板注解
		TemplateDataManager.scanTempalteDataMapBy("com.ngnl.templateData.test.xml");
		//2.创建xml加载映射loader
		DefaultXMLTemplateDataLoader templateDataLoader = new DefaultXMLTemplateDataLoader("com/ngnl/templateData/test/xml/");
		
		TemplateDataManager.loadAllScannedTemplateDatas(templateDataLoader);
//		//3.重新加载指定xml
//		TemplateDataManager.loadTemplateData(T23TemplateDataMap.class, templateDataLoader);
	}
	
	@Test
	public void testGetData () {
		T23TemplateDataMap t23TemplateDataMap = TemplateDataManager.getTemplateDataMap(T23TemplateDataMap.class);
		T23TemplateData data = t23TemplateDataMap.get(101);
		LoggerFactory.getLogger(TemplateDatamanagerTest.class).info(data.toString());
	}
}
