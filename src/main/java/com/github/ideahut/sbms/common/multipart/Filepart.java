package com.github.ideahut.sbms.common.multipart;

import java.io.Serializable;

public class Filepart implements Serializable {	
	/**
	 * 
	 */
	private static final long serialVersionUID = -428750300855407974L;
	
	private String fieldName;

	private String name;
	
	private byte[] data;
	
	private long size;
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
}
