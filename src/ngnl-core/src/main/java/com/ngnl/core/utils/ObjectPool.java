package com.ngnl.core.utils;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author capt.
 * 对象池    L 
 */
public abstract class ObjectPool<O> {
	
	boolean isDebug = false;
	
	Hashtable<O, Long> lockedPool;
	
	Hashtable<O, Long> unlockedPool;
	/**
	 * 池中对象长时间未使用的回收时间间隔 (毫秒),用于防止某些特殊时刻, 池中对象数量出现高峰值,之后一直占用着大量内存.
	 * 释放对象的线程也将按这个时间间隔检测并销毁那些对象
	 * 默认可以最长30秒未被使用
	 */
	long distory_time_span = 30000L;
	
	int lockedPoolSize = 0;
	int unlockedPoolSize = 0;
	
	CleanPoolThread cleanPoolThread;
	
	/**
	 * 默认构造方法.
	 * 注: 池中对象默认30s内未被使用过, 将从池中删除销毁.
	 */
	protected ObjectPool() {
		init();
	}
	
	/**
	 * 设置自定义的自动销毁超时.
	 * @param distoryTimeSpan 池内对象超时自动销毁 (毫秒)
	 */
	protected ObjectPool(long distoryTimeSpan) {
		this.distory_time_span = distoryTimeSpan;
		init();
	}
	
	void init() {
		lockedPool = new Hashtable<>();
		unlockedPool = new Hashtable<>();

		cleanPoolThread = new CleanPoolThread(this, distory_time_span);
		cleanPoolThread.setDaemon(true); //设置为守护进程
		cleanPoolThread.start();
	}
	
	/**
	 * @return  创建一个对象池内对象的实例
	 */
	abstract protected O createPoolObject();
	
	/**
	 * 销毁一个对象池内的对象
	 * @return
	 */
	abstract protected void distoryPoolObject(O obj);
	
	/**
	 * 验证当前池中循环到的这个对象是否可用
	 */
	boolean validate(O obj) {
		if (obj == null)
			return false;
		return true;
	}
	
	/**
	 * 从对象池中借出. 
	 * @return
	 */
	public O loanOut() {
		O obj = null;
		long currentTimeMills = System.currentTimeMillis();
		
		if (isDebug)
			System.out.println("loanOut");
		
		synchronized (unlockedPool) {
			if (unlockedPool.size() > 0) {
				Enumeration<O> keys = unlockedPool.keys();
				
				while (keys.hasMoreElements()) {
					obj = keys.nextElement();
					if (validate(obj) == true) {
						unlockedPool.remove(obj);
						unlockedPoolSize --;
						
						synchronized (lockedPool) {
							lockedPool.put(obj, new Long(currentTimeMills));
							lockedPoolSize ++;
						}
						
						break;
					}else {
						unlockedPool.remove(obj);
						unlockedPoolSize --;
						distoryPoolObject(obj);
						obj = null;
					}
				}
				
			}
			if (obj == null) {
				obj = createPoolObject();
				synchronized (lockedPool) {
					lockedPool.put(obj, new Long(currentTimeMills));
					lockedPoolSize ++;
				}
			}
		}
		
		return obj;
	}
	
	/**
	 * 取回一个对象池 -> 象到池;
	 * @param o
	 */
	public void reclaim (O obj) {
		
		if (isDebug)
			System.out.println("reclaim");

		((Reusable)obj).erase();
		
		synchronized (lockedPool) {
			lockedPool.remove(obj);
			lockedPoolSize --;
		}
		synchronized (unlockedPool) {
			unlockedPool.put(obj, new Long(System.currentTimeMillis()));
			unlockedPoolSize ++;
		}

	}
	
	/**
	 * 清理unlockPool 
	 * 移除未使用时间超过distory_time_span的对象池对象
	 */
	int distory = 0;
	void cleanPool () {
		long currentTimeMills = System.currentTimeMillis();
		
		if (isDebug)
			System.out.println("cleanPool");
		synchronized (unlockedPool) {
			Enumeration<O> keys = unlockedPool.keys();
			O obj;
			boolean isDistory= false;
			while (keys.hasMoreElements()) {
				obj = keys.nextElement();
				if (currentTimeMills - unlockedPool.get(obj).longValue() > distory_time_span) {
					unlockedPool.remove(obj);
					unlockedPoolSize --;
					distoryPoolObject(obj);
					distory ++;
					isDistory = true;
				}
			}
			
			if (isDistory && distory > 0 )
				System.out.println("--销毁 " + distory +"个");
		}
		
	}
	
	public int getLockedPoolSize() {
		return lockedPoolSize;
	}
	
	public int getUnlockedPoolSize () {
		return unlockedPoolSize;
	}
}

class CleanPoolThread extends Thread{
	
	@SuppressWarnings("rawtypes")
	ObjectPool objectPool = null;
	long sleepTimeMills;
	
	@SuppressWarnings("rawtypes")
	CleanPoolThread(ObjectPool pool, long sleepTimeMills) {
		this.objectPool = pool;
		this.sleepTimeMills = sleepTimeMills;
	}
	
	@Override
	public void run() {
		super.run();
		while (true) {
			try {
				sleep(sleepTimeMills);
				
				objectPool.cleanPool();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
