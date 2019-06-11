package com.github.ideahut.sbms.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class StringUtil {
	
	/*
	 * SPLIT
	 */
	public static String[] split(String text, String delim, int max, boolean allowEmpty, boolean isTrim) {
		if (delim == null || "".equals(delim)) {
			return new String[] { text };
		}
		List<String> list = new ArrayList<String>();
		String str = new String(text), txt;
		int idx = str.indexOf(delim);
		int len = delim.length();
		while (idx != -1) {
			if (max > -1 && list.size() == max - 1) {
				break;
			}
			txt = isTrim ? str.substring(0, idx).trim() : str.substring(0, idx);
			str = str.substring(idx + len, str.length());
			idx = str.indexOf(delim);
			if (allowEmpty) {
				list.add(txt);
			} else {
				if (!"".equals(txt)) {
					list.add(txt);
				}
			}			
		}
		if (str.length() > 0) {
			list.add(str);
		}
		return list.toArray(new String[0]);
	}
	
	public static String[] split(String text, String delim, int max, boolean allowEmpty) {
		return split(text, delim, max, true, false);
	}
	
	public static String[] split(String text, String delim, boolean allowEmpty, boolean isTrim) {
		return split(text, delim, -1, allowEmpty, isTrim);
	}
	
	public static String[] split(String text, String delim, int max) {
		return split(text, delim, max, true);
	}
	
	public static String[] split(String text, String delim, boolean allowEmpty) {
		return split(text, delim, -1, allowEmpty);
	}
	
	public static String[] split(String text, String delim) {
		return split(text, delim, -1, true);
	}
	
	
	/*
	 * PARSE TO MAP
	 */
	public static Map<String, String> parseToMap(String text, String delim, String sep, boolean isTrim) {
		if (text == null || delim == null || sep == null || "".equals(delim) || "".equals(sep)) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		String[] ss = split(text, delim);
		for (String s : ss) {
			if (isTrim) {
				s = s.trim();
			}
			String[] dd = split(s, sep, 2);
			if (dd.length == 2) {
				if (isTrim) {
					map.put(dd[0].trim(), dd[1].trim());
				} else {
					map.put(dd[0], dd[1]);
				}
			}
		}
		return map;
	}
	
	public static Map<String, String> parseToMap(String text, String delim, String sep) {
		return parseToMap(text, delim, sep, false);
	}
	
	
	/*
	 * UNIQUE NUMBER
	 */
	public static String getUniqueNumber() {
		return getUniqueNumber(0);
	}
	
	public static String getUniqueNumber(int len) {
		if (len > 0) {
			long div = (long)Math.pow(10, len);
			long number = (System.nanoTime() / len) % div;
			return String.format("%0" + len + "d", number);
		}
		return System.nanoTime() + "";
	}
	
	
	/*
	 * PADDING LEFT
	 */
	public static String padLeft(String s, int len, char c) {
		if (s.length() > len) {
			return s.substring(s.length() - len, s.length());
		}
		StringBuilder d = new StringBuilder(len);
		int fill = len - s.length();
		while (fill-- > 0) {
			d.append(c);
		}
		d.append(s);
		return d.toString();
	}

	
	/*
	 * PADDING RIGHT
	 */
	public static String padRight(String s, int len, char c) {
		if (s.length() > len) {
			return s.substring(0, len);
		}
		StringBuilder d = new StringBuilder(len);
		int fill = len - s.length();
		d.append(s);
		while (fill-- > 0) {
			d.append(c);
		}
		return d.toString();
	}
	
	
}
