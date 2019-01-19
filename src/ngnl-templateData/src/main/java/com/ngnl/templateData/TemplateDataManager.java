package com.ngnl.templateData;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ngnl.core.utils.Assert;
import com.ngnl.templateData.annotations.TemplateDataMap;
import com.ngnl.templateData.test.xml.TemplateDatamanagerTest;

public class TemplateDataManager {
	
	static Logger logger = LoggerFactory.getLogger(TemplateDataManager.class);

	static HashMap<Class<? extends AbstractTemplateDataMap<? extends AbstractTemplateData>>, AbstractTemplateDataMap<? extends AbstractTemplateData>> templateDataMaps = new HashMap<>();
	
	
	/**
	 * note: No matter what package path you pass in, 'Reflections' will always return the root-url to your root directory.
	 * such as, If you call it like this {@code ClasspathHelper.forPackage("com.ngnl.templateData.test.xml")} it returns [file:/D:/workspace/ngnl-1.1.0/src/ngnl-templateData/target/test-classes/], 
	 * not that what you except [file:/D:/workspace/ngnl-1.1.0/src/ngnl-templateData/target/test-classes/com/ngnl/templateData/test/xml].
	 * @param packageName
	 */
	public static void scanTempalteDataMapBy (String packageName) throws Exception{
		Collection<URL> urls = ClasspathHelper.forPackage(packageName.replace(".", "\\."));
		Collection<URL> fullUrls = new ArrayList<>();
		for (URL url : urls) {
			String fullURL = url.toExternalForm() + packageName.replace(".", "/");
			fullUrls.add(new URL(fullURL));
		}
		
		Reflections reflections = new Reflections(new ConfigurationBuilder()
						                .setUrls(fullUrls)
						                .setScanners(new SubTypesScanner(false),
						                			 new TypeAnnotationsScanner()));
		LoggerFactory.getLogger(TemplateDatamanagerTest.class).info("Project Root: {}", new File("").getAbsolutePath());
		
		TemplateDataManager.scanTemplateDataMap(reflections);
	}
	/**
	 * on server start. use this method to scan {@code AbstractTemplateDataMap}.
	 * @param reflections
	 */
	static void scanTemplateDataMap (Reflections reflections){
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
				
				LoggerFactory.getLogger(TemplateDataManager.class).info("New class={}, has been scanned and instantiated . ", clazz);
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
	public static  <M extends AbstractTemplateDataMap<? extends AbstractTemplateData>> M getTemplateDataMap (Class<M> templateDataMapClazz){
		Assert.notNull(templateDataMapClazz, "clazz can't be null. ");
		
		if (templateDataMaps.containsKey(templateDataMapClazz) == false)
			throw new IllegalArgumentException("TemplateDataMap" + templateDataMapClazz + "singleton has not initialized.");
		
		M templateDatamap = (M)templateDataMaps.get(templateDataMapClazz);
		
		return templateDatamap;
	}
	
	/**
	 * Load all template data that has been scanned<br>
	 * note: You should call this method after {@code TemplateDataManager#scanTempalteDataMapBy (String packageName)}
	 * @param templateDataLoader
	 * @throws Exception
	 */
	public static void loadAllScannedTemplateDatas(TemplateDataLoader templateDataLoader)throws Exception{
		Assert.notNull(templateDataLoader, "templateDataLoader can't be null.");
		
		Iterator<Entry<Class<? extends AbstractTemplateDataMap<? extends AbstractTemplateData>>, AbstractTemplateDataMap<? extends AbstractTemplateData>>> iterator = templateDataMaps.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Class<? extends AbstractTemplateDataMap<? extends AbstractTemplateData>>, AbstractTemplateDataMap<? extends AbstractTemplateData>> entry = iterator.next();
			
			Class<? extends AbstractTemplateDataMap<?>> key = entry.getKey();

			loadTemplateData(key, templateDataLoader);
		}
	}
	
	/**
	 * Loading template data from the file.
	 * Before loading data, the cached data will be cleared first. 
	 * @param templateDataMapClazz
	 * @param templateDataLoader
	 */
	@SuppressWarnings("unchecked")
	public static void loadTemplateData(Class<? extends AbstractTemplateDataMap<? extends AbstractTemplateData>>  templateDataMapClazz, TemplateDataLoader templateDataLoader)throws Exception{
		Assert.notNull(templateDataMapClazz, "templateDataMapClazz can't be null.");
		Assert.notNull(templateDataLoader, "templateDataLoader can't be null.");

		clearAllTemplateData(templateDataMapClazz);
		TemplateDataMap annotation = templateDataMapClazz.getAnnotation(TemplateDataMap.class);
		Collection<? extends AbstractTemplateData> collection = templateDataLoader.loadTemplateData(annotation.fileName(), annotation.templateDataClazz());
		addAllTemplateData(templateDataMapClazz, collection);
	}
	
	/**
	 * Add all template data to target template data map. 
	 * If it already exists, it will cover. 
	 * @param templateDataMapClazz
	 * @param templateDatas
	 */
	@SuppressWarnings("unchecked")
	public static <T extends AbstractTemplateData, A extends AbstractTemplateData> 
				  void addAllTemplateData (Class<? extends AbstractTemplateDataMap<? extends AbstractTemplateData>> templateDataMapClazz, Collection<T> templateDatas){
		Assert.notNull(templateDataMapClazz, "templateDataMapClazz can't be null.");

		AbstractTemplateDataMap<A> map  = (AbstractTemplateDataMap<A>)templateDataMaps.get(templateDataMapClazz);
		if (map == null) {
			logger.info("class: " + templateDataMapClazz.toString() + " is not loaded yet.");
			return;
		}
		for (T item : templateDatas) {
			A itemData = (A)item;
			if(itemData == null){
				logger.error("BaseLibary: " + templateDataMapClazz.getSimpleName() + " data --> " + item.getClass().getName() + " cast error!");
				return;
			}
			map.put(itemData.getKey(), itemData);
		}
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
		
		 TemplateDataManager.getTemplateDataMap(templateDataMapClazz).put(templateData.getKey(), templateData);
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
