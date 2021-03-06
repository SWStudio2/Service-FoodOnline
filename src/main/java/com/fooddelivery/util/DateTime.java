package com.fooddelivery.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTime {
	
	public static Date date; 
	
	public static Date getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
		dateFormat.setLenient(false);
		date = new Date();
		return date;
	}
	
	public static Date getDateTime(String dateTimeString) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		date = dateFormat.parse(dateTimeString.toString());
		return date;
	}
	
	public static Date getDateTime(Date dateTimeString) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		date = dateFormat.parse(dateTimeString.toString());
		return date;
	}
	public static String getCurrrentDateTimeString() {
		String dateTimeStr = "";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateTimeStr = dateFormat.format(getCurrentDateTime());

		return dateTimeStr;
	}

	
}