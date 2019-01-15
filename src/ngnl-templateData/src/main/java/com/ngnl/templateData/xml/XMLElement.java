package com.ngnl.templateData.xml;

import java.io.Serializable;

public class XMLElement<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8140833400503881481L;

	/**
	 * value of xml's element.
	 */
	private T evalue;

	public T getEvalue() {
		return evalue;
	}

	public void setEvalue(T evalue) {
		this.evalue = evalue;
	}
	
	
}
