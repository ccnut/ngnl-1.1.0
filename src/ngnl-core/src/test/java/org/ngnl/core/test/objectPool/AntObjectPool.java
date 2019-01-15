package org.ngnl.core.test.objectPool;

import com.ngnl.core.utils.ObjectPool;

public class AntObjectPool extends ObjectPool<Ant> {

	int id_index = 0;

	public AntObjectPool() {
		super();
	}
	
	public AntObjectPool(long distoryTimeSpan) {
		super(distoryTimeSpan);
	}

	@Override
	protected Ant createPoolObject() {
		id_index ++;
		Ant ant = new Ant(id_index);
		
		return ant;
	}

	@Override
	protected void distoryPoolObject(Ant obj) {
		// 不做操作
	}

}
