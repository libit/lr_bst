/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.utils;

import android.os.Build;

import com.lrcall.appbst.MyApplication;
import com.lrcall.utils.apptools.AppFactory;

public class AppConfig
{
	/**
	 * 代理ID
	 */
	public static final String AGENT_ID = "libit";
	/**
	 * 平台类型
	 */
	public static final String PLATFORM = "android";
	/**
	 * 程序存放数据的目录
	 */
	public static final String APP_FOLDER = "bst";
	/**
	 * 数据分享的名字
	 */
	public static final String AUTHORITY_NAME = "lr_bst";

	public static final String getBackupFileName()
	{
		return Build.MODEL + "_" + Build.DISPLAY + "_" + Build.VERSION.RELEASE + "_" + AppFactory.getInstance().getVersionName() + "_" + DateTimeTools.getCurrentTimeNum() + ".bak";
	}

	/**
	 * 调试模式
	 * 用于显示LOG，签名验证等
	 *
	 * @return
	 */
	public static boolean isDebug()
	{
		return PreferenceUtils.getInstance().getBooleanValue(PreferenceUtils.IS_DEBUG);
	}

	/**
	 * 获取代理ID
	 *
	 * @return
	 */
	public static String getAgent()
	{
		return AGENT_ID;
	}

	/**
	 * 获取程序存放数据的目录
	 *
	 * @return
	 */
	public static String getSDCardFolder()
	{
		return AGENT_ID + "/" + APP_FOLDER;
	}

	/**
	 * 数据备份目录
	 *
	 * @return
	 */
	public static String getBackupFolder()
	{
		//        return "backup/data/" + Build.MODEL + "_" + Build.DISPLAY + "_" + Build.VERSION.RELEASE;
		return "backup";
	}

	/**
	 * 日志记录的文件夹
	 *
	 * @return
	 */
	public static String getLogcatFolder()
	{
		return "logcat";
	}

	/**
	 * 获取缓存目录
	 *
	 * @return
	 */
	public static String getCacheDir()
	{
		return MyApplication.getInstance().getCacheDir().getAbsolutePath();
	}

	/**
	 * 获取用户头像缓存路径
	 *
	 * @param userId
	 * @return
	 */
	public static String getUserPicCacheDir(String userId)
	{
		return getCacheDir() + "/head/" + userId + "_temp.jpg";
	}

	/**
	 * 获取商家头像缓存路径
	 *
	 * @param userId
	 * @return
	 */
	public static String getShopPicCacheDir(String userId)
	{
		return getCacheDir() + "/head/shop_" + userId + "_temp.jpg";
	}

	/**
	 * 升级程序的文件夹
	 *
	 * @return
	 */
	public static String getUpdateFolder()
	{
		return "update";
	}
}
