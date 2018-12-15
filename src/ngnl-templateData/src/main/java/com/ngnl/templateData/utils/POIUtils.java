package com.ngnl.templateData.utils;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

public class POIUtils {
	
	public static Object getValue (Cell cell, Class<?> targetType){
		Object targetTypeValue = null;
		if ("short".equals(targetType.getName())){
			targetTypeValue =  getShort(cell);
    	}else if ("int".equals(targetType.getName())){
    		targetTypeValue =  getInt(cell);
    	}else if ("float".equals(targetType.getName())){
    		targetTypeValue =  getFloat(cell);
    	}else if ("long".equals(targetType.getName())){
    		targetTypeValue =  getLong(cell);
    	}else if ("double".equals(targetType.getName())){
    		targetTypeValue =  getDouble(cell);
    	}else if ("boolean".equals(targetType.getName())){
    		targetTypeValue =  getBoolean(cell);
    	}else if ("java.lang.String".equals(targetType.getName())){
    		targetTypeValue =  getString(cell);
    	}else{
    		throw new IllegalArgumentException("Unknow cell type: " + targetType.getName());
    	}
		return targetTypeValue;
	}
	
	private static short getShort(Cell cell) {
		short value = new BigDecimal(cell.getNumericCellValue()).shortValue();
		return value;
	}

	private static int getInt(Cell cell) {
		int value = new BigDecimal(cell.getNumericCellValue()).intValue();
		return value;
	}

	private static float getFloat(Cell cell) {
		float value = new BigDecimal(cell.getNumericCellValue()).floatValue();
		return value;
	}

	private static long getLong(Cell cell) {
		long value;
		switch (cell.getCellType()) {
		case NUMERIC:
			if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				value = date.getTime();
			} else if (cell.getCellStyle().getDataFormat() == 58) { 
				double cellValue = cell.getNumericCellValue();
				Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cellValue);
				value = date.getTime();
			} else {
				value = new BigDecimal(cell.getNumericCellValue()).longValue();
			}
			break;
		case STRING:
			value = Long.valueOf(cell.getStringCellValue());
			break;
		default:
			throw new IllegalArgumentException("Unexpected cell type of boolean value. ");
		}
		return value;
	}

	private static double getDouble(Cell cell) {
		double value = new BigDecimal(cell.getNumericCellValue()).doubleValue();
		return value;
	}

	private static Object getBoolean(Cell cell) {
		boolean value = false;
		switch (cell.getCellType()) {  
        case NUMERIC:
        	value = cell.getNumericCellValue() > 0;
        	break;
        case BOOLEAN:  
        	value = cell.getBooleanCellValue();
            break;  
		default:
        	throw new IllegalArgumentException("Unexpected cell type of boolean value. ");
		}
		return value;
	}

	private static Object getString(Cell cell) {
		String value = null;
		switch (cell.getCellType()) {  
        case NUMERIC:
        	DataFormatter formatter = new DataFormatter();
			if (	org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)// 处理日期格式、时间格式
				 || cell.getCellStyle().getDataFormat() == 58) {// 自定义日期格式
				/**
				 * 注释: 
				 * 对于日期形式的String(例如:"2018/01/01"), 在Excel中会被默认转换为Excel的 '日期/时间类型'.
				 * 而在Excel中 '日期/时间类型' 本质上就是一个时间戳, 显示为一个由自动匹配的dateFormat格式化而成的String.
				 * 使用POI读取到的就是这个时间戳, 再根据这个时间戳反向dateFormat格式转换为String时, 却不能完全逆向还原(尤其是含有'年''月''日'时, 中文会被带上双引号. 可能是在Excel中 "yy/m/d" 这样的格式化占位符和java中 有区别).
				 * 
				 * 所以对于日期形式的String, 最好是手动设置该单元格为文本类型, 再重新将日期String输入进去 (直接转换 '日期/时间类型' 时, '日期/时间类型' 会被换算为一个数字).
				 * */
				value = formatter.formatCellValue(cell);
            } else { 
                value =  cell.getNumericCellValue() + "";
            }  
        	break;
        case STRING:
        	value = cell.getStringCellValue();
        	break;
    	default:
            	throw new IllegalArgumentException("Unexpected cell type of boolean value. ");
		}
		return value;
	}

	/**
	 * Check if there is any value in this row. 
	 * @param row
	 * @return true if row is empty.
	 */
	public static boolean isEmptyRow (Row row) {
		boolean isRowEmpty = true;
		int firstFieldCellNum = row.getFirstCellNum();
		int lastFieldCellNum = row.getLastCellNum();
		
		for (int fieldCellIndex = firstFieldCellNum; fieldCellIndex != lastFieldCellNum; fieldCellIndex++) {
			Cell dataCell = row.getCell(fieldCellIndex);
			if (dataCell != null && dataCell.getCellType() != CellType.BLANK){
				isRowEmpty = false;
				break;
			}
		}
		
		return isRowEmpty;
	}
	
}
