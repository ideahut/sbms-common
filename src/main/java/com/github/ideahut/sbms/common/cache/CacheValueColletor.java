package com.github.ideahut.sbms.common.cache;

import java.io.Serializable;

public interface CacheValueColletor<KEY, VALUE> extends Serializable {
	
	VALUE collect(KEY key, Object...args) throws Exception;
	
}
