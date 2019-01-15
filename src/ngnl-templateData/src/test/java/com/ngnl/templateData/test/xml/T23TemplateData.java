package com.ngnl.templateData.test.xml;

import java.util.List;

import com.ngnl.templateData.annotations.TemplateData;
import com.ngnl.templateData.xml.XMLElement;
import com.ngnl.templateData.xml.XMLTemplateData;

@TemplateData
public class T23TemplateData extends XMLTemplateData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3957662732291656689L;

	private int uid;
	
	private List<XMLElement<Integer>> ID;
	
	private List<XMLElement<Integer>> Type;
	
	private List<XMLElement<Integer>> Value;
	
	private List<XMLElement<String>> Desc;

	@Override
	public int getKey() {
		return this.getID();
	}

	public int getID() {
		return ID.get(0).getEvalue();
	}

	public void setID(List<XMLElement<Integer>> iD) {
		ID = iD;
	}
	
	//element property.
	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getType() {
		return Type.get(0).getEvalue();
	}

	public void setType(List<XMLElement<Integer>> type) {
		Type = type;
	}

	public int getValue() {
		return Value.get(0).getEvalue();
	}

	public void setValue(List<XMLElement<Integer>> value) {
		Value = value;
	}

	public String getDesc() {
		return Desc.get(0).getEvalue();
	}

	public void setDesc(List<XMLElement<String>> desc) {
		Desc = desc;
	}
	
	
	@Override
	public String toString() {
		return "uid=" + uid
				+ "ID=" + getID()
				+ "Type=" + getType()
				+ "Value=" + getValue()
				+ "Desc=" + getDesc();
	}
}
