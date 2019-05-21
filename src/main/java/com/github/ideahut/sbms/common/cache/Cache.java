package com.github.ideahut.sbms.common.cache;

import java.util.Iterator;

public interface Cache<KEY, VALUE> {
	
	public VALUE get(KEY key, Object...args);
	
	public VALUE put(KEY key, VALUE value);
	
	public VALUE remove(KEY key);
	
	public void clear();
	
	public boolean exists(KEY key);
	
	public Iterator<KEY> keys();
	
	public int size();
	
	public long age();
	
	public int limit();
	
}
