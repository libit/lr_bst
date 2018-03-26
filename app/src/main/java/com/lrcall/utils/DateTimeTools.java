/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.utils;

import android.text.format.DateUtils;

import com.lrcall.appbst.MyApplication;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by libit on 16/7/17.
 */
public class DateTimeTools
{
	/**
	 * 根据时间显示相对于当前时间的逝去值
	 *
	 * @param time 要显示的时间
	 * @return 相对于当前时间的逝去值
	 */
	public static String getRelativeTimeSpanString(long time)
	{
		long current = System.currentTimeMillis();
		long dx = current - time;
		//		LogcatTools.debug("DateTimeTools", "current:" + current + ",time:" + time + ",dx:" + dx);
		if (dx < 0)
		{
			return DateUtils.formatDateTime(MyApplication.getContext(), time, DateUtils.FORMAT_SHOW_DATE);
		}
		else if (dx < 24 * 60 * 60 * 1000)
		{
			//			LogcatTools.debug("DateTimeTools", "小于24小时");
			return DateUtils.formatDateTime(MyApplication.getContext(), time, DateUtils.FORMAT_SHOW_TIME);
		}
		else
		{
			//			LogcatTools.debug("DateTimeTools", "大于24小时");
			return DateUtils.formatDateTime(MyApplication.getContext(), time, DateUtils.FORMAT_SHOW_DATE);
		}
	}

	/**
	 * 获取当前时间，格式为yyyyMMddHHmmss
	 *
	 * @return
	 */
	public static String getCurrentTimeNum()
	{
		return getTimeNum(System.currentTimeMillis());
	}

	/**
	 * 获取当前时间，格式为yyyyMMddHHmmss
	 *
	 * @param tm 时间long类型
	 * @return
	 */
	public static String getTimeNum(long tm)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(new Date(tm));
	}

	/**
	 * 获取当前时间，格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @return
	 */
	public static String getCurrentTime()
	{
		return getTime(System.currentTimeMillis());
	}

	/**
	 * 获取当前时间，格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @param tm 时间long类型
	 * @return
	 */
	public static String getTime(long tm)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(tm));
	}

	/**
	 * 获取指定时间，格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @param time
	 * @return
	 */
	public static Timestamp getDateTimestamp(long time)
	{
		Date tm = new Date(time);
		SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return Timestamp.valueOf(from.format(tm));
	}

	/**
	 * 获取指定时间，格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @param time
	 * @return
	 */
	public static String getDateTimeString(long time)
	{
		Date tm = new Date(time);
		SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timestamp = Timestamp.valueOf(from.format(tm)).toString();
		if (timestamp.lastIndexOf(".") > -1)
		{
			timestamp = timestamp.substring(0, timestamp.lastIndexOf("."));
		}
		return timestamp;
	}

	/**
	 * 获取指定时间，格式为yyyy-MM-dd
	 *
	 * @param time
	 * @return
	 */
	public static String getDateString(long time)
	{
		Date tm = new Date(time);
		SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timestamp = Timestamp.valueOf(from.format(tm)).toString();
		if (timestamp.lastIndexOf(" ") > -1)
		{
			timestamp = timestamp.substring(0, timestamp.lastIndexOf(" "));
		}
		return timestamp;
	}

	/**
	 * 获取当前时间，格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @return 获取当前时间，格式为yyyy-MM-dd HH:mm:ss
	 */
	public static Timestamp getCurrentDateTime()
	{
		return getDateTimestamp(System.currentTimeMillis());
	}

	/**
	 * 获取指定格式的时间 long类型
	 *
	 * @param tmp String类型时间
	 * @return
	 */
	public static long getTime(String tmp)
	{
		Timestamp t = Timestamp.valueOf(tmp);
		return t.getTime();
	}

	/**
	 * 计算从某年1月1日0点0分0秒到现在的时间差
	 *
	 * @param year 年份
	 * @return
	 */
	public static long getFromMillis(int year)
	{
		long current = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, 1, 1, 0, 0, 0);
		return current - calendar.getTimeInMillis();
	}

	/**
	 * 计算从2016年1月1日0点0分0秒到现在的时间差
	 *
	 * @return
	 */
	public static long getFromMillis()
	{
		return getFromMillis(2016);
	}

	/**
	 * 返回当前日期，比如20160901
	 *
	 * @return
	 */
	public static long getTodayDateLong()
	{
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR) * 10000L + (calendar.get(Calendar.MONTH) + 1) * 100L + calendar.get(Calendar.DAY_OF_MONTH) * 1L;
	}

	/**
	 * 返回当前月份，比如201609
	 *
	 * @return
	 */
	public static long getTodayYearMonthLong()
	{
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR) * 100L + (calendar.get(Calendar.MONTH) + 1);
	}

	/**
	 * 返回当前日期最初的第一秒时间
	 *
	 * @return
	 */
	public static long getTodayBeginDateTimeLong()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		// 这样设置相差12小时
		// calendar.set(Calendar.HOUR, 0);
		// calendar.set(Calendar.MINUTE, 0);
		// calendar.set(Calendar.SECOND, 0);
		// calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}

	/**
	 * 返回当前日期最后的第一秒时间
	 *
	 * @return
	 */
	public static long getTodayEndDateTimeLong()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		// 这样设置相差12小时
		// calendar.set(Calendar.HOUR, 23);
		// calendar.set(Calendar.MINUTE, 59);
		// calendar.set(Calendar.SECOND, 59);
		// calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime().getTime();
	}

	public static long getDateTimeLong(int year, int month, int day)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, 0, 0, 0);
		return calendar.getTime().getTime();
	}
}
