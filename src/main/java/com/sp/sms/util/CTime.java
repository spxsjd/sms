package com.sp.sms.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author sunpeng
 * 
 */
public class CTime {

	public static final DateFormat MMddHHmmss = new SimpleDateFormat(
			"MMddHHmmss");
	public static final DateFormat YYMMddHHmmss = new SimpleDateFormat(
			"YYMMddHHmmss");
	public static final DateFormat YYMMddHHmmsssss = new SimpleDateFormat(
			"YYMMddHHmmsssss");

	public static String getTime(DateFormat format) {
		Date time = new Date();
		return format.format(time);
	}

}