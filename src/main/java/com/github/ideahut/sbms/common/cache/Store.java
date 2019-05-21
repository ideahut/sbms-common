package com.github.ideahut.sbms.common.cache;

class Store<VALUE> {

	private VALUE value;
	
	private long expiry;
	
	public Store(VALUE value, long ageInMillis) {
		this.value = value;
		if (ageInMillis > 0) {
			this.expiry = System.currentTimeMillis() + ageInMillis;
		} else {
			this.expiry = 0;
		}
	}
	
	public Store(VALUE value) {
		this(value, 0);
	}
	
	public boolean hasExpired() {
		if (expiry == 0) {
			return false;
		}
		return System.currentTimeMillis() > expiry;
	}

	public VALUE getValue() {
		return value;
	}

}
