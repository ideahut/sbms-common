package com.github.ideahut.sbms.common.parser;

import java.math.BigDecimal;
import java.math.BigInteger;

public class StringParser {
	
	private String text = "";
	
	private String buffer = "";
	
	public StringParser() {}
	
	public StringParser(String text) throws Exception {
		setText(text);
		first();
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) throws Exception {
		if (text == null) {
			throw new Exception("text is null");
		}
		this.text = text;		
	}

	public void first(){
		buffer = new String(text);
	}
	
	public void before(int p) throws Exception {
		if (text.length() < p) {
			throw new Exception(text.length() + "<" + p);
		}
		buffer = new String(text.substring(0, p));
	}
	
	public void after(int p) throws Exception {
		if (text.length() < p) {
			throw new Exception(text.length() + "<" + p);
		}
		buffer = new String(text.substring(p));
	}
	
	public String getNextString(int len) throws Exception {
		if (buffer.length() < len) {
			throw new Exception("Invalid length: " + buffer.length() + "<" + len);
		}
		String res = buffer.substring(0, len);
		buffer = buffer.substring(len);
		return res;
	}
	
	public Long getNextLong(int len) throws Exception {
		String s = getNextString(len);
		return new Long(s.trim());
	}
	
	public Integer getNextInt(int len) throws Exception {
		String s = getNextString(len);
		return new Integer(s.trim());
	}
	
	public Float getNextFloat(int len) throws Exception {
		String s = getNextString(len);
		return new Float(s.trim());
	}
	
	public Double getNextDouble(int len) throws Exception {
		String s = getNextString(len);
		return new Double(s.trim());
	}
	
	public BigInteger getNextBigInteger(int len) throws Exception {
		String s = getNextString(len);
		return new BigInteger(s.trim());
	}
	
	public BigDecimal getNextBigDecimal(int len) throws Exception {
		String s = getNextString(len);
		return new BigDecimal(s.trim());
	}
	
	
	public String getString(int start, int end) throws Exception {
		if (end < start) {
			throw new Exception("end < start");
		}
		if (text.length() < start) {
			throw new Exception("length < start");
		}
		if (text.length() < end) {
			throw new Exception("length < end");
		}
		return text.substring(start, end);
	}
	
	public Long getLong(int start, int end) throws Exception {
		String s = getString(start, end);
		return new Long(s.trim());
	}
	
	public Integer getInt(int start, int end) throws Exception {
		String s = getString(start, end);
		return new Integer(s.trim());
	}
	
	public Float getNextFloat(int start, int end) throws Exception {
		String s = getString(start, end);
		return new Float(s.trim());
	}
	
	public Double getNextDouble(int start, int end) throws Exception {
		String s = getString(start, end);
		return new Double(s.trim());
	}
	
	public BigInteger getNextBigInteger(int start, int end) throws Exception {
		String s = getString(start, end);
		return new BigInteger(s.trim());
	}
	
	public BigDecimal getNextBigDecimal(int start, int end) throws Exception {
		String s = getString(start, end);
		return new BigDecimal(s.trim());
	}
}
