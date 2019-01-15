package com.ngnl.templateData.test.excel;

import com.ngnl.templateData.annotations.TemplateDataMap;
import com.ngnl.templateData.excel.ExcelTemplateDataMap;

@TemplateDataMap(templateDataClazz=ItemTemplateData.class, fileName="excels\\item.xlsx")
public final class ItemTemplateDataMap extends ExcelTemplateDataMap<ItemTemplateData> {

}
