package com.ngnl.templateData.test;

import com.ngnl.templateData.annotations.TemplateData;
import com.ngnl.templateData.excel.ExcelTemplateData;

@TemplateData
public class ItemTemplateData extends ExcelTemplateData {

	private static final long serialVersionUID = 3177385568984361891L;

	private String itemName;
	
	private String dateStr;
	
	private long dateLong;
	
	private short maxCount;
	
	private int price;
	
	private boolean canSell;
	
	private float sellRate;
	
	public String getItemName (){
		return itemName;
	}
	
	@Override
	public String toString() {
		return "id: " + id
					+ " itemName: " + itemName 
					+ " dateStr: " + dateStr 
					+ " dateLong: " + dateLong
					+ " maxCount: " + maxCount
					+ " price: " + price
					+ " canSell: " + canSell
					+ " sellRate: " + sellRate;
	}
}
