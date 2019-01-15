package com.ngnl.templateData;

import java.io.Serializable;

public abstract class AbstractTemplateData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7354644453365616261L;

	/**
	 * Unique identifier for a template data.
	 * Usually use the 'id' field to represent.
	 * @return
	 */
	abstract public int getKey();
	
}
