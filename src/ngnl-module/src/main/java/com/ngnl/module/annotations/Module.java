package com.ngnl.module.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME) 
public @interface Module {
	
	/**
	 * log: 扫描注解时可以获取, 不再传入 --2018.09.12
	 */
//	public Class<? extends Module> clazz();
	
	/**
	 * 扫描注解{@code ModuleAnno}后, 实例化Module的顺序, sort值越高越靠后.
	 * <p>实际使用时, 希望使用枚举的自增性质来管理顺序. 
	 * <p>例如:  
	 * 	<p>public enum ModuleSort {
		<p>		BAG,
		<p>		SHOP,
		<p>		DEFAULT;
		<p>}
		
		<p> 注意: 在实例化{@code Module} 实例时, {@code Module} 之间不要根据{@code initSort}顺序互相调用. 
				举例: 两个Module, a和b在{@link Module#init()}时互相引用对方, 将总是会报NPE错误. 所以应避免在初始化{@code Module}时做类似的处理.
				
		log : 鉴于以上理由, 暂不使用initSort参数 --2018.09.12
	 */
//	public int initSort() default Integer.MAX_VALUE;
}
