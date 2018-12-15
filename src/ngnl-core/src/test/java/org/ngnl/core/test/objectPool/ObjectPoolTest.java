package org.ngnl.core.test.objectPool;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectPoolTest {

	public static int onWorkSeed = 200;
	public static int offWorkSeed = 200;
	
	
	public static void main(String[] args) {

		AntObjectPool antObjectPool = new AntObjectPool();
		ConcurrentHashMap<Integer, Ant> workingAnts = new ConcurrentHashMap<>(); //工作中的蚂蚁
		
		//从池中借出蚂蚁
		for (int i = 0 ; i != 100; i++) {
			Thread thread = new Thread(new LetAntGoWork(antObjectPool, workingAnts));
			thread.start();
		}
		//归还蚂蚁到池
		for (int j = 0 ; j != 100; j++) {
			Thread thread = new Thread(new LetAntOffWork(antObjectPool, workingAnts));
			thread.start();
		}
		
		//输出线程
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					
					try {
						Thread.sleep( 100L );
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					System.out.println("当前共创建 " + antObjectPool.id_index + " 只. 池当前可用[" + antObjectPool.getUnlockedPoolSize() + "],当前外派[" + antObjectPool.getLockedPoolSize() + "], working["+workingAnts.size()+"]");
				}
			}
			
		}).start();
		
	}
}

class LetAntGoWork implements Runnable{

	AntObjectPool antObjectPool;
	ConcurrentHashMap<Integer, Ant> workingAnts;
	
	long interval = 1000L;
	
	public LetAntGoWork(AntObjectPool antObjectPool, ConcurrentHashMap<Integer, Ant> workingAnts) {
		this.antObjectPool = antObjectPool;
		this.workingAnts = workingAnts;
		interval = (long) (Math.random() * 1000L);
	}
	
	@Override
	public void run() {
		
		while(true) {
			
			int needNum = (int) (Math.random() * ObjectPoolTest.onWorkSeed);
			for (int i = 0; i != needNum; i++) {
				goWork();
			}
			
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	void goWork () {
		Ant ant = antObjectPool.loanOut();
		
		workingAnts.put(ant.getId(), ant);
		
		ant.startWork();
	}
}

class LetAntOffWork implements Runnable{
	
	AntObjectPool antObjectPool;
	ConcurrentHashMap<Integer, Ant> workingAnts;
	long interval = 1000L;
	
	public LetAntOffWork(AntObjectPool antObjectPool, ConcurrentHashMap<Integer, Ant> workingAnts) {
		this.antObjectPool = antObjectPool;
		this.workingAnts = workingAnts;
		interval = (long) (Math.random() * 1000L);
	}
	
	@Override
	public void run() {
		while (true) {
			
			int needNum = (int) (Math.random() * ObjectPoolTest.offWorkSeed);
			for (int i = 0; i != needNum; i++) {
				offWork();
			}
			
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	void offWork() {

		synchronized (workingAnts) {
			Iterator<Entry<Integer, Ant>> iterator = workingAnts.entrySet().iterator();
			
			while (iterator.hasNext()) {
				Entry<Integer, Ant> entry = iterator.next();
				
				Ant ant = entry.getValue();
				ant.endWork();
				
				iterator.remove();
				antObjectPool.reclaim(ant);
				
				break;
			}
		}
	}
	
}