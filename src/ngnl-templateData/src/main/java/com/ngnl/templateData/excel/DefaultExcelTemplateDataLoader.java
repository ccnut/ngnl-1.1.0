package com.ngnl.templateData.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.common.io.Resources;
import com.ngnl.core.annotations.Nullable;
import com.ngnl.core.utils.Assert;
import com.ngnl.templateData.AbstractTemplateData;
import com.ngnl.templateData.TemplateDataLoader;
import com.ngnl.templateData.utils.POIUtils;

/**
 * @author 47.
 *
 */
public class DefaultExcelTemplateDataLoader extends TemplateDataLoader {
	
	final String metaSheetName = "metaSheet";
	
	public DefaultExcelTemplateDataLoader() {
		
	}
	
	public DefaultExcelTemplateDataLoader(String rootFileURL) {
		this.setRootURL(rootFileURL);
	}

	public <T extends AbstractTemplateData> Collection<T> loadTemplateData (String fileName, Class<T> templateDataClazz){
		Collection<T> templateDatas = new ArrayList<T>();
		try {
			String url = Resources.getResource(getRootFileURL() + fileName).getPath();
			Workbook workBook = WorkbookFactory.create(new FileInputStream(url));
			Collection<T> loadedTemplateDatas = (Collection<T>) this.doLoadTemplateData(workBook, templateDataClazz);
			templateDatas.addAll(loadedTemplateDatas);
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return templateDatas;
	}

	public <T extends AbstractTemplateData> Collection<T> doLoadTemplateData (Workbook workBook, Class<T> templateDataClazz){
		Sheet metaSheet =workBook.getSheet(metaSheetName);
		Assert.notNull(metaSheet, "Excel " + workBook.getName("") +  "must contains a sheet named 'metaSheet' .");
		int firstMetaRowNum = metaSheet.getFirstRowNum();
		Row fieldRow = metaSheet.getRow(firstMetaRowNum + 2);
		
		String dataSheetName = metaSheet.getRow(0).getCell(0).getStringCellValue();
		Sheet dataSheet = workBook.getSheet(dataSheetName);
		Assert.notNull(dataSheet, "Excel must contains a sheet named 'metaSheet' .");
		
		Collection<T> templateDatas = new ArrayList<T>();
		int firstDataRowNum = dataSheet.getFirstRowNum();
		int lastDataRowNum = dataSheet.getLastRowNum();
		for (int dataRowIndex = firstDataRowNum + 1; dataRowIndex != lastDataRowNum; dataRowIndex ++){
			Row dataRow = dataSheet.getRow(dataRowIndex);
			if (dataRow == null)
				continue;
			T templateData = doLoadTemplateData(dataRow, fieldRow, templateDataClazz);
			
			if (templateData != null)
				templateDatas.add(templateData);
		}
		
		return templateDatas;
	}

	public @Nullable <T extends AbstractTemplateData>  T doLoadTemplateData(Row dataRow, Row fieldRow, Class<T> templateDataClazz) {
		short firstFieldCellNum = fieldRow.getFirstCellNum();
		short lastFieldCellNum = fieldRow.getLastCellNum(); 
		
		if (POIUtils.isEmptyRow(dataRow) == true)
			return null;
		
		T newTemplateData = null;
		try {
			newTemplateData = templateDataClazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new NullPointerException("Create new " + templateDataClazz.getName() + " instance error." + e.getMessage());
		}
		
		//Find all fields, but do not include Object fields.
		Collection<Field> allField = new ArrayList<>();
		Class<?> iteratorClazz = newTemplateData.getClass();
		while (iteratorClazz !=null && !iteratorClazz.getName().toLowerCase().equals("java.lang.object") ) {
			
			allField.addAll(Arrays.asList(iteratorClazz.getDeclaredFields()));
			iteratorClazz = iteratorClazz.getSuperclass();
		}
		
		for (int fieldCellIndex = firstFieldCellNum; fieldCellIndex != lastFieldCellNum; fieldCellIndex ++){
			Cell fieldCell = fieldRow.getCell(fieldCellIndex);
			 if (fieldCell==null || fieldCell.getCellType() == CellType.BLANK)
				 continue;
			String fieldName = fieldCell.getStringCellValue();
			
			Cell dataCell = dataRow.getCell(fieldCellIndex);
			 if (dataCell==null || dataCell.getCellType() == CellType.BLANK)
				 continue;

			try {
				for (Field field : allField){
					if (fieldName.equals(field.getName()) == true){
						field.setAccessible(true);
						field.set(newTemplateData, POIUtils.getValue(dataCell, field.getType()));
						break;
					}
				}
			} catch (SecurityException|IllegalArgumentException|IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(newTemplateData.toString());

		return newTemplateData;
	}
	
}
