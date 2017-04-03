package com.fooddelivery.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTime {
	
	public static Date date; 
	
	public static Date getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
		dateFormat.setLenient(false);
		date = new Date();
		return date;
	}

}
