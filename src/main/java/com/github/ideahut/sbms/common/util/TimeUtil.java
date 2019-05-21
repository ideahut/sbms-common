package com.github.ideahut.sbms.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class TimeUtil {

	private TimeUtil() {}	
	
	public static Long getGMTTimeMillis() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String value = format.format(now);
		TimeZone gmt = TimeZone.getTimeZone("GMT");
		format.setTimeZone(gmt);
		Long result = 0L;
		try {
			result = format.parse(value).getTime();
		} catch (Exception e) {}
		return result;
	}
	
}
