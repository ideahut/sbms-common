package com.github.ideahut.sbms.common.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LocalCache<KEY, VALUE> implements Cache<KEY, VALUE> {

	private final List<KEY> list = Collections.synchronizedList(new ArrayList<KEY>());
	private final Map<KEY, Store<VALUE>> map = Collections.synchronizedMap(new HashMap<KEY, Store<VALUE>>());
	
	private int limit; // 0 = unlimited
	
	private long ageInMillis; // 0 = never expired
	
	private boolean nullable;
	
	private CacheValueColletor<KEY, VALUE> valueColletor;
	
	public LocalCache(int limit, long ageInMillis, boolean nullable, CacheValueColletor<KEY, VALUE> valueColletor) {
		setLimit(limit);
		setAgeInMillis(ageInMillis);
		setNullable(nullable);
		setValueColletor(valueColletor);
	}
	
	public LocalCache(int limit, long ageInMillis, CacheValueColletor<KEY, VALUE> valueColletor) {
		this(limit, ageInMillis, false, valueColletor);
	}
	
	public LocalCache(long ageInMillis, boolean nullable, CacheValueColletor<KEY, VALUE> valueColletor) {
		this(0, ageInMillis, nullable, valueColletor);
	}
	
	public LocalCache(int limit, long ageInMillis) {
		this(limit, ageInMillis, false, null);
	}
	
	public LocalCache(long ageInMillis, boolean nullable) {
		this(0, ageInMillis, nullable, null);
	}
	
	public LocalCache(CacheValueColletor<KEY, VALUE> valueColletor) {
		this(0, 0, false, valueColletor);
	}
	
	public LocalCache() {
		this(0, 0, false, null);
	}
	
	public LocalCache<KEY, VALUE> setLimit(int limit) {
		this.limit = limit > 0 ? limit : 0;
		return this;
	}

	public LocalCache<KEY, VALUE> setAgeInMillis(long ageInMillis) {
		this.ageInMillis = ageInMillis > 0 ? ageInMillis : 0;
		return this;
	}
	
	public boolean isNullable() {
		return nullable;
	}

	public LocalCache<KEY, VALUE> setNullable(boolean nullable) {
		this.nullable = nullable;
		return this;
	}

	public CacheValueColletor<KEY, VALUE> getValueColletor() {
		return valueColletor;
	}

	public LocalCache<KEY, VALUE> setValueColletor(CacheValueColletor<KEY, VALUE> valueColletor) {
		this.valueColletor = valueColletor;
		return this;
	}

	@Override
	public VALUE get(KEY key, Object...args) {
		Store<VALUE> store = map.get(key);
		if (store != null) {
			if (!store.hasExpired()) {
				return store.getValue();
			}
			list.remove(key);
			map.remove(key);
		}
		VALUE value = null;
		if (valueColletor != null) {
			try {
				value = valueColletor.collect(key, args);				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			value = put(key, value);
		}
		return value;
	}
	
	@Override
	public VALUE put(KEY key, VALUE value) {
		if (value == null && !nullable) {
			return null; 
		}
		if (!list.contains(key) && !list.add(key)) {
			return null;
		}
		Store<VALUE> store = new Store<VALUE>(value , ageInMillis);
		map.put(key, store);
		int size = list.size();
		if (limit != 0 && size > limit) {
			int diff = size - limit;
			for (int i = 0; i < diff; i++) { // FIFO
				KEY k = list.remove(i);
				map.remove(k);
			}
		}
		return store.getValue();
	}

	@Override
	public VALUE remove(KEY key) {
		list.remove(key);
		Store<VALUE> store = map.remove(key);
		if (store != null && !store.hasExpired()) {
			return store.getValue();
		}
		return null;
	}

	@Override
	public void clear() {
		list.clear();
		map.clear();
	}

	@Override
	public boolean exists(KEY key) {
		return list.contains(key);
	}

	@Override
	public Iterator<KEY> keys() {
		return list.iterator();
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public long age() {
		return ageInMillis;
	}

	@Override
	public int limit() {
		return limit;
	}		
	
}
