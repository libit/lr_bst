/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String辅助类
 *
 * @author libit
 * @Date 2015-08-09
 */
public class StringTools
{
	/**
	 * 判断字符串是否符合正则表达式
	 *
	 * @param regExp 正则表达式
	 * @param string 要判断的字符串
	 * @return 匹配：true，不匹配：false;
	 */
	public static boolean isMatch(String regExp, String string)
	{
		// 如果正则表达式为空，则返回true
		if (isNull(regExp))
		{
			return true;
		}
		//如果判断的字符串为空，则返回fasle
		if (isNull(string))
		{
			return false;
		}
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(string);
		return m.matches();
	}

	/**
	 * 判断是否是Email
	 *
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str)
	{
		String regExp = "^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
		return isMatch(regExp, str);
	}

	/**
	 * 判断字符串的长度范围
	 *
	 * @param str 要判断的字符串
	 * @param min 字符串最小长度
	 * @param max 字符串最大长度
	 * @return
	 */
	public static boolean isStringLengthBetween(String str, int min, int max)
	{
		// 如果最小长度小于0，则将最小值设为0
		if (min < 0)
		{
			min = 0;
		}
		// 如果最大长度小于最小长度，则将最大长度设为最小长度
		if (max < min)
		{
			max = min;
		}
		String regExp = "\\d{" + min + "," + max + "}";
		return isMatch(regExp, str);
	}

	/**
	 * 判断是不是数字（包含证书和带小数的数）
	 *
	 * @param str 要判断的字符串
	 * @return
	 */
	public static boolean isFloat(String str)
	{
		String regExp = "\\d+(.\\d*)?";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 判断字符串是不是为空
	 *
	 * @param str 要判断的字符串
	 * @return
	 */
	public static boolean isNull(String str)
	{
		return (str == null || str.length() < 1);
	}

	/**
	 * 取字符串之间的值
	 *
	 * @param str   原始字符串
	 * @param start 开始匹配的字符串
	 * @param end   结束匹配的字符串
	 * @return
	 */
	public static String getValue(String str, String start, String end)
	{
		String result = "";
		if (isNull(str))
		{
			return result;
		}
		int index = -1;
		if (isNull(start))
		{
			result = str;
		}
		else
		{
			index = str.indexOf(start);
			if (index > -1)
			{
				result = str.substring(index + start.length());
			}
		}
		if (!isNull(end))
		{
			index = result.indexOf(end);
			if (index > -1)
			{
				result = result.substring(0, index);
			}
		}
		return result;
	}

	/**
	 * 将数据库的以分计算的价格转换成元
	 *
	 * @param pricce 以分为单位的价格
	 * @return
	 */
	public static String getPrice(long pricce)
	{
		return String.format("%.2f", (double) ((double) pricce / 100));
	}
}
