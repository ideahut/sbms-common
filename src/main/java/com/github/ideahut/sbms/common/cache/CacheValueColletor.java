package com.github.ideahut.sbms.common.cache;

public interface CacheValueColletor<KEY, VALUE> {
	
	VALUE collect(KEY key, Object...args) throws Exception;
	
}
