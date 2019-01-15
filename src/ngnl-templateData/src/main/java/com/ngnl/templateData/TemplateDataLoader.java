package com.ngnl.templateData;

import java.util.Collection;

/**
 * @author 47
 * Template data file loader.Map template data to entity classes.
 */
public abstract class TemplateDataLoader {

	private String rootFile = "";
	
	abstract public <T extends AbstractTemplateData> Collection<T> loadTemplateData (String fileName, Class<T> templateDataClazz) throws Exception;
	
	/**
	 * relative path of target file.(The relative path of the file name has been removed)<br>
	 * <i>If the relative path of the file is "com/rac/core/templateData/T23_Function.xml", </i><br>
	 * <i>then this value is "com/rac/core/templateData/".</i><br>
	 * default is empty string.
	 */
	public String getRootFile() {
		return rootFile;
	}

	/**
	 * relative path of target file.(The relative path of the file name has been removed)<br>
	 * <i>If the relative path of the file is "com/rac/core/templateData/T23_Function.xml", </i><br>
	 * <i>then this value is "com/rac/core/templateData/".</i><br>
	 * default is empty string.
	 */
	public void setRootURL(String rootFileURL) {
		this.rootFile = rootFile;
	}
}
