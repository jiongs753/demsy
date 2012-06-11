package com.kmetop.demsy.lang;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class Dates {

	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	public static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance();
		Date trialTime = new Date();
		calendar.setTime(trialTime);
		return calendar;
	}

	public static int getAMPM() {
		return getCalendar().get(9);
	}

	public static String getCurrentDate(String format) {
		Date date = new Date();
		return (new SimpleDateFormat(format)).format(date);
	}

	public static String getCurrentDate() {
		Date date = new Date();
		return (new SimpleDateFormat(DEFAULT_DATE_PATTERN)).format(date);
	}

	public static Date parse(String s) {
		if (s == null) {
			return null;
		}
		s = s.trim();
		int len = s.length();
		try {
			if (len >= 19) {
				return new SimpleDateFormat(DEFAULT_DATE_TIME_PATTERN).parse(s.trim());
			} else if (len >= 16) {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(s.trim());
			} else if (len >= 13) {
				return new SimpleDateFormat("yyyy-MM-dd HH").parse(s.trim());
			}
			return new SimpleDateFormat(DEFAULT_DATE_PATTERN).parse(s.trim());
		} catch (ParseException e) {
			throw Ex.throwEx(e);
		}
	}

	public static Date parse(String s, String format) {
		try {
			return new SimpleDateFormat(format).parse(s);
		} catch (ParseException e) {
			throw Ex.throwEx(e);
		}
	}

	public static String formatDate(Date date) {
		if (date == null)
			return "";

		return (new SimpleDateFormat(DEFAULT_DATE_PATTERN)).format(date);
	}

	public static String formatDate(Date date, String format) {
		if (date == null) {
			return "";
		}
		return (new SimpleDateFormat(format)).format(date);
	}

	public static String formatDateTime(Date date) {
		if (date == null) {
			return "";
		}
		return (new SimpleDateFormat(DEFAULT_DATE_TIME_PATTERN)).format(date);
	}

	public static String getCurrentDateTime() {
		Date date = new Date();
		return (new SimpleDateFormat(DEFAULT_DATE_TIME_PATTERN)).format(date);
	}

	public static int getDSTOffset() {
		return getCalendar().get(16) / 0x36ee80;
	}

	public static String getDate() {
		return getYear() + "-" + getMonthInt() + "-" + getDayOfMonth();
	}

	public static String getDateWeek() {
		return getYear() + " 年 " + getMonthInt() + " 月 " + getDayOfMonth() + " 日 星期" + getDay();
	}

	@SuppressWarnings("unused")
	public static int getDay(String s) {
		s = s.trim();
		if (s.length() <= 10)
			s = s + " 00:00:00.0";
		String ret = null;
		int start = 0;
		int _index1 = s.indexOf(45, 0);
		int _index2 = s.indexOf(45, _index1 + 1);
		int spaceIndex = s.indexOf(32, _index2 + 1);
		int colonIndex1 = s.indexOf(58, spaceIndex + 1);
		int colonIndex2 = s.indexOf(58, colonIndex1 + 1);
		int end = s.length();
		int point = s.indexOf(46, colonIndex2 + 1);
		int dd = Integer.parseInt(s.substring(_index2 + 1, spaceIndex));
		return dd;
	}

	public static String getDay() {
		int x = getDayOfWeek();
		String days[] = { "日", "一", "二", "三", "四", "五", "六" };
		if (x > 7)
			return "Unknown to Man";
		else
			return days[x];
	}

	public static int getDayOfMonth() {
		return getCalendar().get(5);
	}

	public static int getDayOfWeek() {
		return getCalendar().get(7) - 1;
	}

	public static int getDayOfYear() {
		return getCalendar().get(6);
	}

	public static int getEra() {
		return getCalendar().get(0);
	}

	public static int getHour() {
		return getCalendar().get(11);
	}

	public static int getMinute() {
		return getCalendar().get(12);
	}

	@SuppressWarnings("unused")
	public static int getMonth(String s) {
		s = s.trim();
		if (s.length() <= 10)
			s = s + " 00:00:00.0";
		String ret = null;
		int start = 0;
		int _index1 = s.indexOf(45, 0);
		int _index2 = s.indexOf(45, _index1 + 1);
		int spaceIndex = s.indexOf(32, _index2 + 1);
		int colonIndex1 = s.indexOf(58, spaceIndex + 1);
		int colonIndex2 = s.indexOf(58, colonIndex1 + 1);
		int end = s.length();
		int point = s.indexOf(46, colonIndex2 + 1);
		int MM = Integer.parseInt(s.substring(_index1 + 1, _index2));
		return MM;
	}

	public static String getMonth() {
		int m = getMonthInt();
		String months[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
		if (m > 12)
			return "Unknown to Man";
		else
			return months[m - 1];
	}

	public static int getMonthInt() {
		return 1 + getCalendar().get(2);
	}

	public static Date getToday() {
		return parse(getCurrentDate());
	}

	public static Date getNext(int day) {
		return getNext(new Date(), day);
	}

	public static Date getNext(Date d, int day) {
		return new Date(d.getTime() + day * 24 * 60 * 60 * 1000);
	}

	public static Date getNextDay(Date d) {
		return parse(getNextDay(formatDate(d)));
	}

	@SuppressWarnings("unused")
	public static String getNextDay(String s) {
		s = s.trim();
		if (s.length() <= 10)
			s = s + " 00:00:00.0";
		else
			s = s + ".0";
		String ret = null;
		int start = 0;
		int _index1 = s.indexOf(45, 0);
		int _index2 = s.indexOf(45, _index1 + 1);
		int spaceIndex = s.indexOf(32, _index2 + 1);
		int colonIndex1 = s.indexOf(58, spaceIndex + 1);
		int colonIndex2 = s.indexOf(58, colonIndex1 + 1);
		int end = s.length();
		int point = s.indexOf(46, colonIndex2 + 1);
		int yyyy = Integer.parseInt(s.substring(start, _index1));
		int MM = Integer.parseInt(s.substring(_index1 + 1, _index2));
		int dd = Integer.parseInt(s.substring(_index2 + 1, spaceIndex));
		int hh = Integer.parseInt(s.substring(spaceIndex + 1, colonIndex1));
		int mm = Integer.parseInt(s.substring(colonIndex1 + 1, colonIndex2));
		int ss = Integer.parseInt(s.substring(colonIndex2 + 1, point));
		int day = 0;
		if (MM == 1 || MM == 3 || MM == 5 || MM == 7 || MM == 8 || MM == 10 || MM == 12)
			day = 31;
		else if (MM == 2)
			day = (yyyy % 4 != 0 || yyyy % 100 == 0) && (yyyy % 100 != 0 || yyyy % 400 != 0) ? 28 : 29;
		else
			day = 30;
		if (dd < day) {
			dd++;
		} else {
			if (MM < 12) {
				MM++;
			} else {
				yyyy++;
				MM = 1;
			}
			dd = 1;
		}
		ret = "" + yyyy + "-" + (MM >= 10 ? "" + MM : "0" + MM) + "-" + (dd >= 10 ? "" + dd : "0" + dd);
		return ret;
	}

	public static int getSecond() {
		return getCalendar().get(13);
	}

	public static String getTime() {
		return getHour() + ":" + getMinute() + ":" + getSecond();
	}

	public static String getUSTimeZone() {
		String zones[] = { "Hawaii", "Alaskan", "Pacific", "Mountain", "Central", "Eastern" };
		return zones[10 + getZoneOffset()];
	}

	public static int getWeekOfMonth() {
		return getCalendar().get(4);
	}

	public static int getWeekOfYear() {
		return getCalendar().get(3);
	}

	@SuppressWarnings("unused")
	public static int getYear(String s) {
		s = s.trim();
		if (s.length() <= 10)
			s = s + " 00:00:00.0";
		String ret = null;
		int start = 0;
		int _index1 = s.indexOf(45, 0);
		int _index2 = s.indexOf(45, _index1 + 1);
		int spaceIndex = s.indexOf(32, _index2 + 1);
		int colonIndex1 = s.indexOf(58, spaceIndex + 1);
		int colonIndex2 = s.indexOf(58, colonIndex1 + 1);
		int end = s.length();
		int point = s.indexOf(46, colonIndex2 + 1);
		int yyyy = Integer.parseInt(s.substring(start, _index1));
		return yyyy;
	}

	public static int getYear() {
		return getCalendar().get(1);
	}

	public static int getZoneOffset() {
		return getCalendar().get(15) / 0x36ee80;
	}

	@SuppressWarnings("unused")
	public static String validateDate(String s) {
		s = s.trim();
		if (s.length() <= 10)
			s = s + " 00:00:00.0";
		String ret = null;
		int start = 0;
		int _index1 = s.indexOf(45, 0);
		int _index2 = s.indexOf(45, _index1 + 1);
		int spaceIndex = s.indexOf(32, _index2 + 1);
		int colonIndex1 = s.indexOf(58, spaceIndex + 1);
		int colonIndex2 = s.indexOf(58, colonIndex1 + 1);
		int end = s.length();
		int point = s.indexOf(46, colonIndex2 + 1);
		int yyyy = Integer.parseInt(s.substring(start, _index1));
		int MM = Integer.parseInt(s.substring(_index1 + 1, _index2));
		int dd = Integer.parseInt(s.substring(_index2 + 1, spaceIndex));
		int hh = Integer.parseInt(s.substring(spaceIndex + 1, colonIndex1));
		int mm = Integer.parseInt(s.substring(colonIndex1 + 1, colonIndex2));
		int ss = Integer.parseInt(s.substring(colonIndex2 + 1, point));
		return s;
	}
}
