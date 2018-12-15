package com.ngnl.templateData.test;

import com.ngnl.templateData.annotations.TemplateDataMap;
import com.ngnl.templateData.excel.ExcelTemplateDataMap;

@TemplateDataMap(templateDataClazz=ItemTemplateData.class, fileURL="excels\\item.xlsx")
public final class ItemTemplateDataMap extends ExcelTemplateDataMap<ItemTemplateData> {

}
