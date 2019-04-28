package com.ngnl.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author 47
 * 可过期的 cache aside value.
 */
public class ExpirableValue<Value>{
	
	Value value = null;
	
	private boolean expired = false;
	
	private long expiredTimestamp = -1L;
	
	protected long outOfExpired = Long.MAX_VALUE;//过期间隔. 默认值不过期
	
	ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	
	public ExpirableValue(Value value) {
		this.value = value;
		this.setExpiredTimestamp(System.currentTimeMillis() + outOfExpired);
		init();
	}
	
	protected void init() {
	}
	
	public Value getValue () {
		return this.value;
	}
	
	/**
	 * 失效标记. 缓存中标记为失效后, DBCahceMap需要从DB中从新拉取.
	 * 默认为false.
	 * @param outOfDate
	 */
	public void setExpired(boolean outOfDate) {
		Lock lock = readWriteLock.writeLock();
		try {
			lock.lock();
			this.expired = outOfDate;
		} finally {
			lock.unlock();
		}
	}

	public boolean isExpired() {
		Lock lock = readWriteLock.readLock();
		try {
			lock.lock();
			if (expired ==true)
				return true;
			
			if (expiredTimestamp == -1L)
				return false;
			
			if (expiredTimestamp >= System.currentTimeMillis()) {
				expired = true;
			}
			return expired;
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * 设置强制失效时间戳. -1L表示永远不会因为超时而失效.
	 * 默认为 -1L;
	 * @param outOfDateTimestamp
	 */
	public void setExpiredTimestamp(long outOfDateTimestamp) {
		Lock lock = readWriteLock.writeLock();
		try {
			lock.lock();
			this.expiredTimestamp = outOfDateTimestamp;
		} finally {
			lock.unlock();
		}
	}
	
}
