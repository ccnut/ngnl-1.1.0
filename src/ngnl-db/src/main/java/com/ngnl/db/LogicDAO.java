package com.ngnl.db;

import java.util.List;

/**
 * @author 47
 * 
 * 暴露给业务逻辑使用的接口. 
 *
 * @param <UniqueKey>
 * @param <Entity>
 */
public interface LogicDAO<UniqueKey, Entity> {
	
	/**
	 * 首先从缓存中查询, 命中则直接返回; 未命中则查询DB,并添加到缓存中并返回
	 * @param key 唯一key
	 * @return
	 */
	Entity get(UniqueKey key) throws Exception;
	
	/**
	 * 首先从缓存中查询, 命中则直接返回; 未命中则查询DB,并添加到缓存中并返回
	 * @param keys
	 * @return
	 */
	List<Entity> get(List<UniqueKey> keys) throws Exception;
	
	/**
	 * 首先从缓存中查询, 命中则直接返回; 未命中则查询DB,并添加到缓存中并返回
	 * @return
	 * @throws Exception
	 */
	List<Entity> getAll() throws Exception;
	
	/**
	 * Synchronous insert.
	 */
	void createSync(Entity entity) throws Exception;
	
	/**
	 *  Asynchronous update.
	 */
	void update(Entity entity) throws Exception;

	/**
	 * Synchronous update.
	 */
	void updateSync(Entity entity) throws Exception;
	
	/**
	 * Synchronous delete.
	 */
	int deleteSync(Entity entity) throws Exception;
}
