package com.github.ideahut.sbms.common.cache;

import java.util.Iterator;

public interface CacheGroup {
	
	public<KEY, VALUE> CacheGroup register(
			String group, int limit, long ageInMillis, boolean nullable, CacheValueColletor<KEY, VALUE> valueColletor);
	
	public<KEY, VALUE> CacheGroup unregister(String group);
	
	
	public<KEY, VALUE> VALUE get(String group, KEY key, Object...args);
	
	public<KEY, VALUE> VALUE put(String group, KEY key, VALUE value);
	
	public<KEY, VALUE> VALUE remove(String group, KEY key);
	
	public<KEY, VALUE> void clear(String group);
	
	public boolean exists(String group);
	
	public<KEY, VALUE> boolean exists(String group, KEY key);
	
	public Iterator<String> groups();
	
	public<KEY, VALUE> Iterator<KEY> keys(String group);
	
	public<KEY, VALUE> int size(String group);
	
	public<KEY, VALUE> long age(String group);
	
	public<KEY, VALUE> int limit(String group);
	
}
