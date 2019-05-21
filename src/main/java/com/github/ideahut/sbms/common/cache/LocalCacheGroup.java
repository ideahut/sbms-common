package com.github.ideahut.sbms.common.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LocalCacheGroup implements CacheGroup {
	
	private final Map<String, Cache<?, ?>> map = Collections.synchronizedMap(new HashMap<String, Cache<?, ?>>());
	
	@Override
	public<KEY, VALUE> CacheGroup register(
		String group, int limit, long ageInMillis, boolean nullable, CacheValueColletor<KEY, VALUE> valueColletor
	) {
		if (!map.containsKey(group)) {
			Cache<KEY, VALUE> cache = new LocalCache<KEY, VALUE>(limit, ageInMillis, nullable, valueColletor);
			map.put(group, cache);
		}
		return this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public<KEY, VALUE> CacheGroup unregister(String group) {
		Cache<KEY, VALUE> cache = (Cache<KEY, VALUE>)map.remove(group);
		if (cache != null) {
			cache.clear();
		}
		return this;
	}
	

	@Override
	public<KEY, VALUE> VALUE get(String group, KEY key, Object...args) {
		Cache<KEY, VALUE> cache = getCache(group);
		return cache.get(key, args);
	}

	@Override
	public<KEY, VALUE> VALUE put(String group, KEY key, VALUE value) {
		Cache<KEY, VALUE> cache = getCache(group);
		return cache.put(key, value);
	}

	@Override
	public<KEY, VALUE> VALUE remove(String group, KEY key) {
		Cache<KEY, VALUE> cache = getCache(group);
		return cache.remove(key);
	}

	@Override
	public<KEY, VALUE> void clear(String group) {
		Cache<KEY, VALUE> cache = getCache(group);
		cache.clear();
	}

	@Override
	public boolean exists(String group) {
		return map.containsKey(group);
	}

	@Override
	public<KEY, VALUE> boolean exists(String group, KEY key) {
		Cache<KEY, VALUE> cache = getCache(group);
		return cache.exists(key);
	}

	@Override
	public Iterator<String> groups() {
		return map.keySet().iterator();
	}

	@Override
	public<KEY, VALUE> Iterator<KEY> keys(String group) {
		Cache<KEY, VALUE> cache = getCache(group);
		return cache.keys();
	}

	@Override
	public<KEY, VALUE> int size(String group) {
		Cache<KEY, VALUE> cache = getCache(group);
		return cache.size();
	}

	@Override
	public<KEY, VALUE> long age(String group) {
		Cache<KEY, VALUE> cache = getCache(group);
		return cache.age();
	}

	@Override
	public<KEY, VALUE> int limit(String group) {
		Cache<KEY, VALUE> cache = getCache(group);
		return cache.limit();
	}
	
	@SuppressWarnings("unchecked")
	public<KEY, VALUE> Cache<KEY, VALUE> getCache(String group) {
		Cache<KEY, VALUE> cache = (Cache<KEY, VALUE>)map.get(group);
		if (cache == null) {
			throw new RuntimeException("Group is not registered: " + group);
		}
		return cache;
	}

}
