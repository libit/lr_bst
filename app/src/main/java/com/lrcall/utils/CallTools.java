/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ErrorInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.services.UserService;
import com.lrcall.ui.ActivityDialWaiting;
import com.lrcall.ui.ActivityLogin;
import com.lrcall.ui.customer.ToastView;

import java.util.ArrayList;

/**
 * Created by libit on 16/5/1.
 */
public class CallTools
{
	private static final String CHINA_CODE = "+86";
	private static final String[] ipPrefix = new String[]{"12593", "17911", "17909"};

	/**
	 * 处理数据库查询的电话号码
	 *
	 * @param number
	 * @return
	 */
	public static String[] parseSqlNumber(String number)
	{
		LogcatTools.debug("deleteNumberCallLogs", "parseSqlNumber number=" + number);
		if (StringTools.isNull(number))
		{
			return new String[]{"-"};
		}
		else if (number.startsWith("-"))
		{
			return new String[]{"-"};
		}
		number = convertToCallPhoneNumber(number);
		int len = number.length();
		if (len > 8)
		{
			String[] nums = new String[3];
			nums[0] = number.substring(0, len - 8);
			nums[1] = number.substring(len - 8, len - 4);
			nums[2] = number.substring(len - 4, len);
			return new String[]{number, String.format("%s %s %s", nums[0], nums[1], nums[2]), String.format("%s-%s-%s", nums[0], nums[1], nums[2])};
		}
		else if (len == 8)
		{
			String[] nums = new String[2];
			nums[0] = number.substring(0, len - 4);
			nums[1] = number.substring(len - 4, len);
			return new String[]{number, String.format("%s %s", nums[0], nums[1]), String.format("%s-%s", nums[0], nums[1])};
		}
		else if (len == 8)
		{
			return new String[]{number};
		}
		else
		{
			return new String[]{number};
		}
	}

	/**
	 * 判断是不是未知来电号码
	 *
	 * @param number 要判断的号码
	 * @return
	 */
	public static boolean isUnKnownPhoneNumber(String number)
	{
		String regExp = "-\\d*";
		return StringTools.isMatch(regExp, number);
	}

	/**
	 * 判断是不是中国手机号码，包括手机和座机
	 *
	 * @param number 要判断的号码
	 * @return
	 */
	public static boolean isChinaPhoneNumber(String number)
	{
		// 正则表达式
		String regExp = "(\\+86)?((1\\d{10})|((0\\d{2,3})?\\d{8}))";
		return StringTools.isMatch(regExp, number);
	}

	/**
	 * 判断是不是中国手机号码
	 *
	 * @param number 要判断的手机号码
	 * @return
	 */
	public static boolean isChinaMobilePhoneNumber(String number)
	{
		String regExp = "(\\+86)?1\\d{10}";
		return StringTools.isMatch(regExp, number);
	}

	/**
	 * 判断是不是中国手机号码并带前缀
	 *
	 * @param number 要判断的号码
	 * @param prefix 前缀
	 * @return
	 */
	public static boolean isChinaMobilePhoneNumberWithPrefix(String number, String prefix)
	{
		if (StringTools.isNull(prefix))
		{
			return isChinaMobilePhoneNumber(number);
		}
		String regExp = "(\\+86)?" + prefix + "1\\d{10}";
		return StringTools.isMatch(regExp, number);
	}

	/**
	 * 判断是不是带区号的座机号码
	 *
	 * @param number 要判断的号码
	 * @return
	 */
	public static boolean isChinaTelePhoneNumberWithAreaCode(String number)
	{
		String regExp = "(\\+86)?0\\d{10,11}";
		return StringTools.isMatch(regExp, number);
	}

	/**
	 * 判断是不是不带区号的座机号码
	 *
	 * @param number 要判断的号码
	 * @return
	 */
	public static boolean isChinaTelePhoneNumberWithoutAreaCode(String number)
	{
		String regExp = "\\d{8}";
		return StringTools.isMatch(regExp, number);
	}

	/**
	 * 去除国家代号
	 *
	 * @param number 要去掉的号码
	 * @return
	 */
	public static String removeChinaCodeInChinaPhoneNumber(String number)
	{
		if (StringTools.isNull(number))
		{
			return "";
		}
		if (number.startsWith(CHINA_CODE))
		{
			return number.substring(CHINA_CODE.length());
		}
		return number;
	}

	/**
	 * 转换成中国的号码，去掉国家代码，添加前缀，如果是座机则添加区号
	 *
	 * @param number   号码
	 * @param prefix   要添加的前缀
	 * @param areaCode 区号
	 * @return
	 */
	public static String convertToChinaPhoneNumber(String number, String prefix, String areaCode)
	{
		if (StringTools.isNull(number))
		{
			return "";
		}
		if (isChinaPhoneNumber(number))
		{
			number = removeChinaCodeInChinaPhoneNumber(number);
			if (isChinaMobilePhoneNumber(number))
			{
				number = prefix + number;
			}
			else if (isChinaTelePhoneNumberWithoutAreaCode(number))
			{
				if (areaCode != null && !number.startsWith("0"))
				{
					number = areaCode + number;
				}
			}
		}
		return number;
	}

	/**
	 * 去掉号码的IP前缀
	 *
	 * @param number    被去掉的号码
	 * @param ipPrefixs IP前缀的集合
	 * @return 处理后的号码
	 */
	public static String trimIpPrefix(String number, ArrayList<String> ipPrefixs)
	{
		if (StringTools.isNull(number))
		{
			return "";
		}
		number = removeChinaCodeInChinaPhoneNumber(number);
		int count = ipPrefix.length;
		for (int i = 0; i < count; i++)
		{
			if (number.startsWith(ipPrefix[i]))
			{
				number = number.substring(ipPrefix[i].length());
				return number;
			}
		}
		if (ipPrefixs != null && ipPrefixs.size() > 0)
		{
			for (String prefix : ipPrefixs)
			{
				if (number.startsWith(prefix))
				{
					number = number.substring(prefix.length());
					return number;
				}
			}
		}
		return number;
	}

	/**
	 * 转换成可以呼叫的号码
	 *
	 * @param originalCallNumber 原始号码
	 * @return
	 */
	public static String convertToCallPhoneNumber(String originalCallNumber)
	{
		if (StringTools.isNull(originalCallNumber))
		{
			return "";
		}
		originalCallNumber = removeChinaCodeInChinaPhoneNumber(originalCallNumber);
		String num = "";
		for (int i = 0; i < originalCallNumber.length(); i++)
		{
			char ch = originalCallNumber.charAt(i);
			if (ch >= '0' && ch <= '9')
			{
				num += ch;
			}
		}
		return num;
	}

	//呼叫
	public static ReturnInfo makeCall(final Context context, final String number)
	{
		if (StringTools.isNull(number))
		{
			return new ReturnInfo(ErrorInfo.getForbiddenErrorId(), "呼叫号码不能为空！");
		}
		if (!UserService.isLogin())
		{
			context.startActivity(new Intent(context, ActivityLogin.class));
			return new ReturnInfo(ErrorInfo.getSuccessId(), "用户未登录！");
		}
		Intent intent = new Intent(context, ActivityDialWaiting.class);
		intent.putExtra(ConstValues.DATA_NUMBER, number);
		context.startActivity(intent);
		return new ReturnInfo(ErrorInfo.getSuccessId(), "呼叫号码成功！");
	}

	//呼叫
	public static ReturnInfo makeLocalCall(final Context context, final String number)
	{
		if (StringTools.isNull(number))
		{
			return new ReturnInfo(ErrorInfo.getForbiddenErrorId(), "呼叫号码不能为空！");
		}
		try
		{
			Intent i = new Intent(Intent.ACTION_CALL);
			i.setData(Uri.fromParts("tel", number, null));
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			//			PendingIntent.getActivity(context, 0, i, 0).send();
		}
		//		catch (PendingIntent.CanceledException e)
		//		{
		//			e.printStackTrace();
		//		}
		catch (SecurityException e)
		{
			e.printStackTrace();
			ToastView.showCenterToast(context, R.drawable.ic_do_fail, "您拒绝了软件拨打电话的权限！");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new ReturnInfo(ErrorInfo.getSuccessId(), "呼叫号码成功！");
	}
}
