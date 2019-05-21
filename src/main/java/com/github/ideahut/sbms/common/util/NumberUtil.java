package com.github.ideahut.sbms.common.util;

import java.util.Arrays;

public final class NumberUtil {

	/*
	 * To convert (long -> alphanumeric) and (char -> alphanumeric) with spesific length
	 * For HEX use length = 16
	 */
	private static final char[] CHAR_ALPHA_NUMERIC = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
		'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
		'U', 'V', 'W', 'X', 'Y', 'Z',
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
		'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
		'u', 'v', 'w', 'x', 'y', 'z'
	};
	
	private static final char[] CHAR_ALPHA_NUMERIC_LOWER_CASE = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
		'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
		'u', 'v', 'w', 'x', 'y', 'z'
	};
	
	private static final char[] CHAR_ALPHA_NUMERIC_UPPER_CASE = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
		'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
		'U', 'V', 'W', 'X', 'Y', 'Z'
	};
	
	private static final char[] CHAR_HEXA = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
		'A', 'B', 'C', 'D', 'E', 'F'
	};
	
	
	/*
	 * LONG TO ALPHA NUMERIC
	 */
	public static String longToAlphaNumericAllCase(long number) {
		return longToAlphaNumeric(number, CHAR_ALPHA_NUMERIC);
	}
	
	public static String longToAlphaNumericLowerCase(long number) {
		return longToAlphaNumeric(number, CHAR_ALPHA_NUMERIC_LOWER_CASE);
	}
	
	public static String longToAlphaNumericUpperCase(long number) {
		return longToAlphaNumeric(number, CHAR_ALPHA_NUMERIC_UPPER_CASE);
	}
	
	private static String longToAlphaNumeric(long number, char[] array) {
		if (number < 0) {
			throw new NullPointerException("number < 0");
		}
		String str = "";
		long num = number;
		int idx;
		int len = array.length;
		while(num > 0) {
			idx = (int)(num % len);
			str = CHAR_ALPHA_NUMERIC[idx] + str; 
			num = num / len;
	    }
		return str;
	}
	
	
	
	/*
	 * ALPHA NUMERIC TO LONG
	 */
	public static long alphaNumericAllCaseToLong(String alphanumeric) {
		return alphaNumericToLong(alphanumeric, CHAR_ALPHA_NUMERIC);
	}
	
	public static long alphaNumericLowerCaseToLong(String alphanumeric) {
		return alphaNumericToLong(alphanumeric, CHAR_ALPHA_NUMERIC_LOWER_CASE);
	}
	
	public static long alphaNumericUpperCaseToLong(String alphanumeric) {
		return alphaNumericToLong(alphanumeric, CHAR_ALPHA_NUMERIC_UPPER_CASE);
	}
	
	private static long alphaNumericToLong(String alphanumeric, char[] array) {
		if (alphanumeric == null) {
			throw new NullPointerException("alphanumeric == null");
		}
		long num = 0;
		int size = alphanumeric.length();
		int len  = array.length;
		for(int i = size; i > 0; i--) {
			num += Math.pow(len, size - i) * Arrays.binarySearch(array, alphanumeric.charAt(i - 1));
		}
		return num;
	}
	
	
	/*
	 * LONG TO HEX
	 */
	public static String longToHex(long number) {
		return longToAlphaNumeric(number, CHAR_HEXA);
	}
	
	
	/*
	 * HEX TO LONG
	 */
	public static long hexToLong(String hex) {
		if (hex == null) {
			throw new NullPointerException("hex == null");
		}
		String txt = new String(hex).trim().toUpperCase();
		return alphaNumericToLong(txt, CHAR_HEXA);
	}
}
