package com.ngnl.db;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author 47
 * DAO 业务异步操作逻辑基类
 */
public abstract class BaseDAO<UniqueKey, Entity extends BaseEntity> implements LogicDAO<UniqueKey, Entity> {
	
	//异步更新
	SaveThread<Entity> saveThread;
	
    private Class<Entity> poClass;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BaseDAO() {
        poClass = (Class<Entity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; //获取第一个泛型
		saveThread = new SaveThread(poClass.getName() + " Async-save Thread", this);
		saveThread.start();
	}
	
	/**
	 * Synchronous query.
	 */
	protected abstract Entity readSync(UniqueKey uniqueKey) throws Exception;

	/**
	 * Synchronous query.
	 */
	protected abstract List<Entity> readSync(List<UniqueKey> uniqueKeys) throws Exception;
	
	public void update(Entity entity){
		saveThread.add(entity);
	}
}
