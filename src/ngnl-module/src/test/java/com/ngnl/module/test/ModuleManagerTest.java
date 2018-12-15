package com.ngnl.module.test;

import java.net.URL;
import java.util.Collection;

import org.junit.Test;
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

import com.ngnl.core.annotations.Nullable;
import com.ngnl.module.BaseModule;
import com.ngnl.module.ModuleManager;
import com.ngnl.module.annotations.Module;

@Module()
public class ModuleManagerTest implements BaseModule {

	@Test
	public void testScanModule () {
		Collection<URL> urls = ClasspathHelper.forPackage("com.ngnl.module.test");
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
		
		ModuleManager.scanModule(reflections);
		
		ModuleManagerTest testModule = ModuleManager.getModule(ModuleManagerTest.class);
		LoggerFactory.getLogger(ModuleManagerTest.class).debug("scanned test module: {}", testModule.toString());
	}
	
	@Nullable
	public static String aaa() {
		return "";
	}
}
