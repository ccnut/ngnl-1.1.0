package com.ngnl.templateData;

import java.io.Serializable;

public abstract class AbstractTemplateData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7354644453365616261L;

	protected int id = 0; 
	
	public int getId(){
		return id;
	}
}
