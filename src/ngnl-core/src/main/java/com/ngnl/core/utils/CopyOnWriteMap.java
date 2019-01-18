
package com.ngnl.core.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CopyOnWriteMap<K, V> implements Map<K, V>, Cloneable {

	 private volatile Map<K, V> internalMap;
	 
	 public CopyOnWriteMap (){
			this.internalMap = new HashMap<>();
	 }

	@Override
	public boolean containsKey(Object arg0) {
		return this.internalMap.containsKey(arg0);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.internalMap.containsValue(value);
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return this.internalMap.entrySet();
	}

	@Override
	public V get(Object key) {
		return this.internalMap.get(key);
	}

	@Override
	public boolean isEmpty() {
		return this.internalMap.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return this.internalMap.keySet();
	}

	@Override
	public V put(K key, V value) {
		synchronized (this) {
            Map<K, V> newMap = new HashMap<K, V>(internalMap);
            V val = newMap.put(key, value);
            internalMap = newMap;
            return val;
        }
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> values) {
		synchronized (this) {
            Map<K, V> newMap = new HashMap<K, V>(internalMap);
            newMap.putAll(values);
            internalMap = newMap;
        }
	}

	@Override
	public V remove(Object key) {
		synchronized (this) {
            Map<K, V> newMap = new HashMap<K, V>(internalMap);
            V val = newMap.remove(key);
            internalMap = newMap;
            return val;
        }
	}

	@Override
	public int size() {
		return this.internalMap.size();
	}

	@Override
	public Collection<V> values() {
		return this.internalMap.values();
	}

	@Override
	public void clear() {
		if(this.internalMap != null && this.internalMap.size() == 0)
			return;
		
		this.internalMap = new HashMap<>();
	}
}
