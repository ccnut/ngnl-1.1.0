package com.ngnl.templateData.test.excel;

import com.ngnl.templateData.annotations.TemplateData;
import com.ngnl.templateData.excel.ExcelTemplateData;

@TemplateData
public class ItemTemplateData extends ExcelTemplateData {

	private static final long serialVersionUID = 3177385568984361891L;
	protected int id = 0; 

	private String itemName;
	
	private String dateStr;
	
	private long dateLong;
	
	private short maxCount;
	
	private int price;
	
	private boolean canSell;
	
	private float sellRate;
	

	@Override
	public int getKey() {
		return this.id;
	}
	
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
