package com.ngnl.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveThread<Entity extends BaseEntity> extends Thread {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	//Entity的UniqueKey队列. 用于标识存储顺序.
	Queue<Object> queue = new LinkedList<>();
	
	//Map<uniqueKey, entity>唯一键值映射
	Map<Object, Entity> uniqueMap = new ConcurrentHashMap<>();
	
	String threadName;
	
	boolean stop = false;
	
	LogicDAO dao;
	
	int maxSize = 8192;
	
	public SaveThread(String threadName, LogicDAO sqlDAO) {
		super(threadName);
		this.threadName = threadName;
		this.dao = sqlDAO;
	}
	
	@Override
	public void run() {
		while (!stop || queue.isEmpty() == false) {
			Entity entity = null;
			try {
				synchronized (this) {
					Object uniqueKey = queue.poll();
					if(uniqueKey != null)
						entity = uniqueMap.remove(uniqueKey);
					else
						this.wait();
				}
				
				update(entity);
				
				if (queue.size() > maxSize) {
					//用于防止缓存雪崩
					queue.clear();
					logger.error(getName() + "待处理命令行超过" + maxSize + ", 已经触发清空队列操作!");
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	void update(Entity entity) {
		if (entity == null)
			return;
		try {
			this.dao.updateSync(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 public void add(Entity entity) {
		if (entity == null)
			 return;
		 
		try {
			Entity saveEntity = (Entity) SaveThread.deepCopy(entity);// 拷贝一份数据
			if (saveEntity == null)
				return; //深拷贝失败或者其他原因, null则不处理
			
			synchronized (this) {
				if (uniqueMap.containsKey(entity.getUniqueKey()) == false) {
					queue.add(entity.getUniqueKey());
				}
				uniqueMap.put(entity.getUniqueKey(), entity);
				this.notifyAll();
			}
		} catch (Exception e) {
			logger.error("save Thread " + threadName + " Notify Exception:" + e.getMessage());
			e.printStackTrace();
		}
	}

	public void stop(boolean flag) {
		stop = flag;
		try {
			synchronized (this) {
				notifyAll();
			}
		} catch (Exception e) {
			logger.error("Role Thread " + threadName + " Notify Exception:" + e.getMessage());
		}
	}
	
	public boolean isClear () {
		logger.info(this.threadName + ", 队列剩余：" + this.queue.size());
		if(this.queue.size() == 0)
			return true;
		return false;
	}

	static Object deepCopy(Object entity) {
		if (entity == null) {
			return null;
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		Object object = null;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(entity);
			ByteArrayInputStream bis = new ByteArrayInputStream( bos.toByteArray());
			// 将流序列化成对象
			ObjectInputStream ois = new ObjectInputStream(bis);
			object = ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return object;
	}
}
