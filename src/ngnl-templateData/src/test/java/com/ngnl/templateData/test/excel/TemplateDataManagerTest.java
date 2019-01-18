package com.ngnl.templateData.test.excel;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.ngnl.templateData.TemplateDataLoader;
import com.ngnl.templateData.TemplateDataManager;
import com.ngnl.templateData.excel.DefaultExcelTemplateDataLoader;

public class TemplateDataManagerTest {

	@Test
	public void testLoadExcel () throws Exception{

		TemplateDataManager.scanTempalteDataMapBy("com.ngnl.templateData.test.excel");
		
		//批量扫描加载
		TemplateDataLoader loader = new DefaultExcelTemplateDataLoader("com/ngnl/templateData/test/excel/");
		TemplateDataManager.loadAllScannedTemplateDatas(loader);
		
		//指定扫描加载
		ItemTemplateDataMap itemTemplateDataMap = TemplateDataManager.getTemplateDataMap(ItemTemplateDataMap.class);
		TemplateDataManager.loadTemplateData(ItemTemplateDataMap.class, loader);
		
		LoggerFactory.getLogger(TemplateDataManagerTest.class).debug(itemTemplateDataMap.toString());
	}
}
