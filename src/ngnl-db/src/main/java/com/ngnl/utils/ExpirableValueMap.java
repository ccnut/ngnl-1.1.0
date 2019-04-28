package com.ngnl.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 47
 * 数据缓存容器. value 有过期属性, 如果被判断为已过期, 等效为map中不存在该值.
 */
@SuppressWarnings("rawtypes")
public class ExpirableValueMap <K, V>  {

	/**
	 * 触发清理缓存占用的阀值. 长度在到达maxSize时, 会触发一次release()
	 * 默认值为Integer.MAX_VALUE.
	 */
	private int maxSize = Integer.MAX_VALUE;
	
	/**
	 * map<key, 原始value的一个默认的可过期属性的装饰者>
	 */
	Map<K, ExpirableValue<V>> cacheMap = new ConcurrentHashMap<K, ExpirableValue<V>>();
	
	Logger logger = LoggerFactory.getLogger(getClass());

	public void put (K key, V value) {
		cacheMap.put(key, new ExpirableValue<V>(value));
	}

	/**
	 * 过期数据等同于没有记录, putIfAbsent 覆盖一个过期数据时,也返回null并且覆盖过期数据
	 * @param key
	 * @param value
	 * @return
	 */
	public V putIfAbsent(K key, V value) {
		ExpirableValue<V> oldValue = cacheMap.putIfAbsent(key, new ExpirableValue<V>(value));
		if (oldValue == null)
			return null;
		else {
			if (oldValue.isExpired() == true) {
				put(key, value);
				return null;
			}else {
				return oldValue.getValue();
			}
		}
	}
	
	public V get (K key) {
		ExpirableValue<V> data = cacheMap.get(key);
		if (data != null && data.isExpired()) {
			cacheMap.remove(key);
			data = null;//过期或失效,等同于没有缓存值
		}
		if (data == null)
			return null;
		else
			return data.getValue();
	}
	
	public List<V> get(Collection<K> keys){
		//TODO:
		return null;
	}
	
	public List<V> getAll (){
		//TODO:
		return null;
	}
	
	public V removeKey (K key) {
		ExpirableValue<V> value = cacheMap.remove(key);
		if (value == null)
			return null;
		else 
			return value.getValue();
	}
	
	public Iterator<Entry<K, ExpirableValue<V>>> getIterator (){
		return cacheMap.entrySet().iterator();
	}
	
	public void setExpired(K key) {
		ExpirableValue<V> data = cacheMap.get(key);
		if (data != null) {
			data.setExpired(true);
		}else
			logger.debug("Cache-aside 未找到对应key的记录, key:" + key.toString());
	}
	
	public void setExpiredTimestamp (K key, long expiredTimestamp) {
		ExpirableValue<V> data = cacheMap.get(key);
		if (data != null) {
			data.setExpiredTimestamp(expiredTimestamp);
		}else
			logger.debug("Cache-aside 未找到对应key的记录, key:" + key.toString());
	}
	
	public void forEach (BiConsumer<? super K, ExpirableValue> action) {
		cacheMap.forEach(action);
	}
	
	public void setMaxSize (int maxSize) {
		this.maxSize = maxSize;
	}
	
	public int getMaxSize () {
		return this.maxSize;
	}

	/**
	 * 释放掉 cache中过期的数据. 减少缓存占用的空间.
	 */
	public void release () {
		//TODO:
		
	}
}

