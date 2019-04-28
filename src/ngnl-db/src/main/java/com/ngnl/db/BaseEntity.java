package com.ngnl.db;

import java.io.Serializable;

public interface BaseEntity extends Serializable{

	/**
	 * 用于标识这条记录唯一. 
	 * 可以是建表自动生成的id, 也可以是roleId.只要在db中能唯一映射到一条记录即可
	 * @return
	 */
	Object getUniqueKey();
}
