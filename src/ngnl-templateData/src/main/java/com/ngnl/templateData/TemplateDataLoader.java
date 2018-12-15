package com.ngnl.templateData;

import java.util.Collection;

public interface TemplateDataLoader {

	<T extends AbstractTemplateData> Collection<T> loadTemplateData (String fileURL, Class<T> templateDataClazz);
	
}
