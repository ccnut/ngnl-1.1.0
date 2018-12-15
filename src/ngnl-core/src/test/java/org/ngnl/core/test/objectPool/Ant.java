package org.ngnl.core.test.objectPool;

import com.ngnl.core.utils.Reusable;

public class Ant implements Reusable {

	int id;
	
	public Ant() {
		
	}
	
	public Ant(int id) {
		this.id = id;
	}
	
	public void startWork() {
//		System.out.println("No. "+id+" -- start");
	}
	
	public void endWork() {
//		System.out.println("No. "+id+" -- 		end");
	}
	
	@Override
	public void erase() {
	}

	public int getId() {
		return id;
	}
}

