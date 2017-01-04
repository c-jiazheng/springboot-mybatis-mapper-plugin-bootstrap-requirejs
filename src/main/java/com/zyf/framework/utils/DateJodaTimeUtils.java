package com.zyf.framework.utils;

import com.zyf.common.enums.TimeType;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.*;

public class DateJodaTimeUtils {

	public static final int WEEK_DAYS = 7;
	private static final String format_default = "yyyy-MM-dd HH:mm:ss";
	private static final String format_yMd = "yyyy-MM-dd";

	public static String getNowTime(String... format) {
		String fm = format_default;
		if (format != null && format.length > 0) {
			fm = format[0];
		}
		return new DateTime().toString(fm);
	}

	public static DateTime calendarToDateTime(Calendar c) {
		return new DateTime(c);
	}

	public static String calendarToString(Calendar c, String... format) {
		String pattern = format_default;
		if (isNotEmpty(format)) {
			pattern = format[0];
		}
		return calendarToDateTime(c).toString(pattern);
	}

	public static DateTime dateToDateTime(Date date) {
		return new DateTime(date);
	}

	public static String dateToString(Date date, String... format) {
		String pattern = format_default;
		if (isNotEmpty(format)) {
			pattern = format[0];
		}
		return dateToDateTime(date).toString(pattern);
	}

	public static DateTime stringToDateTime(String time) {
		return DateTime.parse(time, DateTimeFormat.forPattern(format_default));
	}

	public static Calendar stringToCalendar(String time) {
		return stringToDateTime(time).toCalendar(Locale.getDefault());
	}

	public static Calendar stringToCalendarYMD(String time) {
		return DateTime.parse(time, DateTimeFormat.forPattern(format_yMd))
				.toCalendar(Locale.getDefault());
	}

	/**
	 * 二时间差值
	 *
	 * @param startCal 2014-12-26到2015-01-02  差值为6 (不包含2015－01－02)
	 * @param endCal
	 * @return
	 * @see TimeType
	 */

	public static long compareCalendarByType(Calendar startCal, Calendar endCal, TimeType type) {
		if (type == null) {
			type = TimeType.day;
		}
		long diffNum = 0;
		DateTime begin = setDateTimeToStart(calendarToDateTime(startCal), type);
		DateTime end = setDateTimeToStart(calendarToDateTime(endCal), type);

		Duration duration = new Duration(begin, end);
		switch (type) {
			case year:
				diffNum = duration.getStandardDays() / 365;
				break;
			case day:
				diffNum = duration.getStandardDays();
				break;
			case hour:
				diffNum = duration.getStandardHours();
				break;
			case minute:
				diffNum = duration.getStandardMinutes();
				break;
			case second:
				diffNum = duration.getStandardSeconds();
				break;
			case millis:
				diffNum = duration.getMillis();
				break;
			default:
				diffNum = duration.getStandardSeconds();
				break;
		}
		return diffNum;
	}

	public static DateTime setDateTimeToStart(DateTime dateTime, TimeType key) {
		if (dateTime == null) {
			dateTime = DateTime.now();
		}

		switch (key) {
			case year:
				dateTime = dateTime.withMonthOfYear(1).withDayOfMonth(1)
						.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
				break;
			case month:
				dateTime = dateTime.withDayOfMonth(1).withHourOfDay(0)
						.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
				break;
			case day:
				dateTime = dateTime.withHourOfDay(0).withMinuteOfHour(0)
						.withSecondOfMinute(0).withMillisOfSecond(0);
				break;
			case hour:
				dateTime = dateTime.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
				break;
			case minute:
				dateTime = dateTime.withSecondOfMinute(0).withMillisOfSecond(0);
				break;
			case second:
				dateTime = dateTime.withMillisOfSecond(0).withMillisOfSecond(0);
				break;
			default:// TimeType.day
				dateTime = dateTime.withHourOfDay(0).withMinuteOfHour(0)
						.withSecondOfMinute(0).withMillisOfSecond(0);
				break;
		}
		return dateTime;
	}

	/**
	 * 加时间
	 *
	 * @param num
	 * @param key y M w d h m s
	 * @param c
	 * @return
	 */
	public static Calendar plusNumToCalendar(int num, TimeType key,
	                                         Calendar... c) {
		Calendar calendar = null;
		if (isNotEmpty(c)) {
			calendar = c[0];
		} else {
			calendar = Calendar.getInstance();
		}
		return plusNumToDateTime(num, key, calendarToDateTime(calendar))
				.toCalendar(Locale.getDefault());
	}

	public static DateTime plusNumToDateTime(int num, TimeType key, DateTime... dt) {
		DateTime dateTime = DateTime.now();
		if (isNotEmpty(dt)) {
			dateTime = dt[0];
		}
		switch (key) {
			case year:
				dateTime = dateTime.plusYears(num);
				break;
			case month:
				dateTime = dateTime.plusMonths(num);
				break;
			case week:
				dateTime = dateTime.plusWeeks(num);
				break;
			case day:
				dateTime = dateTime.plusDays(num);
				break;
			case hour:
				dateTime = dateTime.plusHours(num);
				break;
			case minute:
				dateTime = dateTime.plusMinutes(num);
				break;
			case second:
				dateTime = dateTime.plusSeconds(num);
				break;
			default:
				dateTime = dateTime.plusHours(num);
				break;
		}
		return dateTime;
	}

	/**
	 * 返回00:00:00
	 *
	 * @param c
	 * @return
	 */
	public static Calendar getCalendarMorning(Calendar c) {
		return getDateTimeMorning(c).toCalendar(Locale.getDefault());
	}

	public static DateTime getDateTimeMorning(Calendar c) {
		DateTime dateTime = calendarToDateTime(c);
		dateTime = dateTime.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		return dateTime;
	}

	/**
	 * 返回23:59:59
	 *
	 * @param c
	 * @return
	 */
	public static Calendar getCalendarMidnight(Calendar c) {
		return getDateTimeMidnight(c).toCalendar(Locale.getDefault());
	}

	public static DateTime getDateTimeMidnight(Calendar c) {
		DateTime dateTime = calendarToDateTime(c);
		dateTime = dateTime.withHourOfDay(23).withMinuteOfHour(59)
				.withSecondOfMinute(59);
		return dateTime;
	}

	/**
	 * 一周中的第几天。周一：１　周二：２ ...
	 *
	 * @param c
	 * @return
	 */
	public static int getDayInWeek(Calendar c) {
		if (c == null) {
			c = Calendar.getInstance();
		}
		return new DateTime(c).getDayOfWeek();
	}

	/**
	 * 时间段内　日期列表
	 *
	 * @param c1
	 * @param c2
	 * @param format
	 * @return
	 */
	public static List<String> getDayBetweenStr(Calendar c1, Calendar c2, String... format) {
		List<String> list = new ArrayList<String>(15);
		String pattern = format_yMd;
		if (isNotEmpty(format)) {
			pattern = format[0];
		}
		long days = compareCalendarByType(c1, c2, TimeType.day);
		DateTime start = calendarToDateTime(c1);
		for (int i = 0; i <= days; i++) {
			list.add(plusNumToDateTime(i, TimeType.day, start).toString(pattern));
		}
		return list;
	}

	/**
	 * 日期在年中第几周
	 *
	 * @param c
	 * @return
	 */
	public static int getWeekInYear(Calendar c) {
		if (c == null) {
			c = Calendar.getInstance();
		}
		return new DateTime(c).getWeekOfWeekyear();
	}

	public static int getWeekOfYMD(int year, int monthOfYear, int dayOfMonth) {
		LocalDate date = new LocalDate(year, monthOfYear, dayOfMonth);
		return date.weekOfWeekyear().get();
	}

	/**
	 * 某年某周第一天
	 *
	 * @param year
	 * @param weekOfWeekyear
	 * @return
	 */
	public static DateTime getFirstDayOfYearWeek(int year, int weekOfWeekyear) {
		DateTime dateTime = DateTime.now();
		dateTime = dateTime.withYear(year).withWeekOfWeekyear(weekOfWeekyear).dayOfWeek().withMinimumValue();
		return dateTime;
	}

	public static DateTime getLastDayOfYearWeek(int year, int weekOfWeekyear) {
		return DateTime.now().withYear(year).withWeekOfWeekyear(weekOfWeekyear)
				.dayOfWeek().withMaximumValue();
	}

	public static DateTime getFirstDayInNowWeek() {
		return DateTime.now().dayOfWeek().withMinimumValue();
	}

	public static DateTime getLastDayInNowWeek() {
		return DateTime.now().dayOfWeek().withMaximumValue();
	}

	public static DateTime getLastDayOfYearMonth(int year, int monthOfYear) {
		return DateTime.now().withYear(year).withMonthOfYear(monthOfYear)
				.dayOfMonth().withMaximumValue();
	}

	public static DateTime getLastDayOfNowMonth() {
		return DateTime.now().dayOfMonth().withMaximumValue();
	}

	/**
	 * 某周的七天日期
	 *
	 * @param format
	 * @return
	 */
	public static Map<String, String> getDateOfWeekMap(String... format) {
		DateTime dateTime = DateTime.now();
		return getDateOfWeekMap(dateTime.getYear(),
				dateTime.getWeekOfWeekyear(), format);
	}

	public static Map<String, String> getDateOfWeekMap(int year, int week,
	                                                   String... format) {
		String pattern = format_yMd;
		if (isNotEmpty(format)) {
			pattern = format[0];
		}
		Map<String, String> map = new LinkedHashMap<String, String>();
		DateTime dateTime = DateTime.now().withYear(year)
				.withWeekOfWeekyear(week).dayOfWeek().withMinimumValue();
		for (int i = 0; i < WEEK_DAYS; i++) {
			map.put((i + 1) + "", dateTime.plusDays(i).toString(pattern));
		}
		return map;
	}

	public static Map<String, String> getWeekAssertDateMap(String... format) {
		return getWeekAssertDateMap(2, true, format);
	}

	public static Map<String, String> getWeekAssertDateMap(int type,
	                                                       String... format) {
		return getWeekAssertDateMap(type, true, format);
	}

	/**
	 * 今日起一周内对应日期
	 *
	 * @param type               　１：数字(1-7) 2:周几　default:星期几
	 * @param isChangeWeekToDate 默认true{key:type，value:日期}; false:{key:日期，value:type}
	 * @param format
	 * @return
	 */
	public static Map<String, String> getWeekAssertDateMap(int type,
	                                                       boolean isChangeWeekToDate, String... format) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		String pattern = "yyMMdd";
		if (isNotEmpty(format)) {
			pattern = format[0];
		}
		String[] weekArr = new String[]{};
		switch (type) {
			case 1:
				weekArr = new String[]{"1", "2", "3", "4", "5", "6", "7"};
				break;
			case 2:
				weekArr = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
				break;
			default:
				weekArr = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六",
						"星期日"};
				break;
		}
		DateTime dateTime = DateTime.now();
		for (int i = 0; i < weekArr.length; i++) {
			int day = dateTime.getDayOfWeek();
			if (isChangeWeekToDate) {
				map.put(weekArr[day - 1], dateTime.toString(pattern));
			} else {
				map.put(dateTime.toString(pattern), weekArr[day - 1]);
			}
			dateTime = dateTime.plusDays(1);
		}
		return map;
	}

	public static String getDayOfWeek(Calendar c) {
		return new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"}[c.get(Calendar.DAY_OF_WEEK) - 1];
	}

	public static <T> boolean isNotEmpty(@SuppressWarnings("unchecked") T... obj) {
		if (obj != null && obj.length > 0 && obj[0] != null) {
			if (obj[0] instanceof String) {
				if (StringUtils.isBlank(obj[0].toString())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}