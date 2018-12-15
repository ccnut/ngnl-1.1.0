package com.ngnl.templateData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.slf4j.LoggerFactory;

import com.ngnl.core.utils.Assert;
import com.ngnl.templateData.annotations.TemplateDataMap;

public class TemplateDataManager {

	static HashMap<Class<? extends AbstractTemplateDataMap<?>>, AbstractTemplateDataMap<?>> templateDataMaps = new HashMap<>();
	
	/**
	 * on server start. use this method to scan {@code AbstractTemplateDataMap}.
	 * @param reflections
	 */
	public static void scanTemplateDataMap (Reflections reflections){
		Assert.notEmpty(reflections.getConfiguration().getUrls(), "Scan @URL can not be empty .");
		doScanTemplateDataMap(reflections);
	}
	
	@SuppressWarnings("unchecked")
	static void doScanTemplateDataMap (Reflections reflections){
		Set<Class<?>> clazzes = reflections.getTypesAnnotatedWith(TemplateDataMap.class);
		for (Class<?> clazz : clazzes){

			if (AbstractTemplateDataMap.class.isAssignableFrom(clazz) == false)
				throw new IllegalArgumentException(clazz + " must be a subtype of @AbstractTemplateDataSet class.");
			
			try {
				AbstractTemplateDataMap<?> newInstance = (AbstractTemplateDataMap<?>) clazz.newInstance();
				templateDataMaps.put(((Class<? extends AbstractTemplateDataMap<?>>)clazz), newInstance);
				
				LoggerFactory.getLogger(TemplateDataManager.class).debug("New class={}, has been scanned and instantiated . ", clazz);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Get a {@code TemplateDataSet} that had scanned.
	 *  Get a registered {@code TemplateDataSet}.
	 */
	@SuppressWarnings("unchecked")
	public static  <M extends AbstractTemplateDataMap<?>> M getTemplateDataMap (Class<M> templateDataMapClazz){
		Assert.notNull(templateDataMapClazz, "clazz can't be null. ");
		
		if (templateDataMaps.containsKey(templateDataMapClazz) == false)
			throw new IllegalArgumentException("TemplateDataMap" + templateDataMapClazz + "singleton has not initialized.");
		return (M)templateDataMaps.get(templateDataMapClazz);
	}
	
	/**
	 * Loading template data from the file.
	 * Before loading data, the cached data will be cleared first. 
	 * @param templateDataMapClazz
	 * @param templateDataLoader
	 */
	@SuppressWarnings("unchecked")
	public static <M extends AbstractTemplateDataMap<D>, L extends TemplateDataLoader, D extends AbstractTemplateData> 
						 void loadTemplateData(Class<M>  templateDataMapClazz, L templateDataLoader){
		Assert.notNull(templateDataMapClazz, "templateDataMapClazz can't be null.");
		Assert.notNull(templateDataLoader, "templateDataLoader can't be null.");

		clearAllTemplateData(templateDataMapClazz);
		TemplateDataMap annotation = templateDataMapClazz.getAnnotation(TemplateDataMap.class);
		addAllTemplateData(templateDataMapClazz, (Collection<D>)templateDataLoader.loadTemplateData(annotation.fileURL(), annotation.templateDataClazz()));
	}
	
	/**
	 * Add all template data to target template data map. 
	 * If it already exists, it will cover. 
	 * @param templateDataMapClazz
	 * @param templateDatas
	 */
	public static <M extends AbstractTemplateDataMap<D>, D extends AbstractTemplateData> 
					     void addAllTemplateData (Class<M>  templateDataMapClazz, Collection<D> templateDatas){
		Assert.notNull(templateDataMapClazz, "templateDataMapClazz can't be null.");

		 Map<Integer, D> templateDataMap = templateDatas.stream()
				 																			.collect( Collectors.toMap(AbstractTemplateData::getId, data -> data) );
		 TemplateDataManager.getTemplateDataMap(templateDataMapClazz).putAll(templateDataMap);
	}
	
	/**
	 * Add a new template data to target template data map.
	 * If it already exists, it will cover. 
	 * @param templateDataMapClazz
	 * @param templateData
	 */
	public static <M extends AbstractTemplateDataMap<D>, D extends AbstractTemplateData>
						 void addTemplateData (Class<M>  templateDataMapClazz, D templateData){
		Assert.notNull(templateDataMapClazz, "templateDataMapClazz can't be null.");
		Assert.notNull(templateData, "templateData can't be null.");
		
		 TemplateDataManager.getTemplateDataMap(templateDataMapClazz).put(templateData.getId(), templateData);
	}
	
	/**
	 * clear all cached template data.
	 * @param templateDataMapClazz
	 */
	public static void clearAllTemplateData (Class<? extends AbstractTemplateDataMap<?>>  templateDataMapClazz){
		Assert.notNull(templateDataMapClazz, "templateDataMapClazz can't be null.");
		
		 TemplateDataManager.getTemplateDataMap(templateDataMapClazz).clear();
	}
}
