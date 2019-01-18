package com.ngnl.module;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MemberUsageScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterNamesScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.LoggerFactory;

import com.ngnl.core.utils.Assert;
import com.ngnl.module.annotations.Module;

public class ModuleManager {
	
	static HashMap<Class<? extends BaseModule>, BaseModule> moduleInstances = new HashMap<>();
	
	
	/**
	 * @param packageStr   "com.ngnl.module.test"
	 */
	public static void scanModule (String packageStr) {
		Collection<URL> urls = ClasspathHelper.forPackage(packageStr);
		Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(urls)
                .setScanners(
                        new SubTypesScanner(false),
                        new TypeAnnotationsScanner(),
                        new FieldAnnotationsScanner(),
                        new MethodAnnotationsScanner(),
                        new MethodParameterScanner(),
                        new MethodParameterNamesScanner(),
                        new MemberUsageScanner()));
		scanModule(reflections);
	}
	
	public static void scanModule (Reflections reflections){
		Assert.notEmpty(reflections.getConfiguration().getUrls(), "Scan @URL can not be empty .");
		doScanModule(reflections);
	}
	
	@SuppressWarnings("unchecked")
	static void doScanModule (Reflections reflections){
		Set<Class<?>> clazzes = reflections.getTypesAnnotatedWith(Module.class);
		for (Class<?> clazz : clazzes){

			if (BaseModule.class.isAssignableFrom(clazz) == false)
				throw new IllegalArgumentException(clazz + " must be a subtype of @Module class.");
			
			try {
				moduleInstances.put(((Class<? extends BaseModule>)clazz), (BaseModule) clazz.newInstance());
				
				LoggerFactory.getLogger(ModuleManager.class).debug("New class={}, has been scanned and instantiated . ", clazz);
				
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Get a {@code Module} that had scanned.
	 *  Get a registered {@code Module}.
	 */
	@SuppressWarnings("unchecked")
	public static <M extends BaseModule> M getModule (Class<M> clazz){
		Assert.notNull(clazz, "clazz can't be null. ");

		if (moduleInstances.containsKey(clazz) == false)
			throw new IllegalArgumentException("Module " + clazz + " singleton has not initialized.");
		
		return (M)moduleInstances.get(clazz);
	}
}
