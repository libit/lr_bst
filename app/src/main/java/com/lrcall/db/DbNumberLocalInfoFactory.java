/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.lrcall.appbst.MyApplication;
import com.lrcall.appbst.models.NumberLocalInfo;
import com.lrcall.utils.StringTools;

/**
 * Created by libit on 16/7/8.
 */
public class DbNumberLocalInfoFactory
{
	private static final String TABLE_NAME = DbConstant.TABLE_NAME_LOCAL_INFO;
	private static DbNumberLocalInfoFactory instance;

	synchronized public static DbNumberLocalInfoFactory getInstance()
	{
		if (instance == null)
		{
			instance = new DbNumberLocalInfoFactory();
		}
		return instance;
	}

	/**
	 * 增加号码归属地
	 *
	 * @param number    电话号码
	 * @param localInfo 归属地信息
	 * @return
	 */
	public boolean addOrUpdateNumberLocalInfo(String number, String localInfo)
	{
		if (StringTools.isNull(number) || StringTools.isNull(localInfo))
		{
			return false;
		}
		NumberLocalInfo numberLocalInfo = new NumberLocalInfo(number, localInfo);
		return addOrUpdateNumberLocalInfo(numberLocalInfo);
	}

	/**
	 * 增加号码归属地
	 *
	 * @param numberLocalInfo
	 * @return
	 */
	public boolean addOrUpdateNumberLocalInfo(NumberLocalInfo numberLocalInfo)
	{
		if (numberLocalInfo == null || StringTools.isNull(numberLocalInfo.getNumber()) || StringTools.isNull(numberLocalInfo.getLocalInfo()))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		ContentValues values = numberLocalInfo.getObjectContentValues();
		int row = contentResolver.update(tableUri, values, NumberLocalInfo.FIELD_NUMBER + " = ?", new String[]{numberLocalInfo.getNumber()});
		if (row < 1)
		{
			return contentResolver.insert(tableUri, values) != null;
		}
		return true;
	}

	/**
	 * 获取电话号码的归属地
	 *
	 * @param number 电话号码
	 * @return
	 */
	public String getLocalInfo(String number)
	{
		if (StringTools.isNull(number))
		{
			return "";
		}
		NumberLocalInfo numberLocalInfo = null;
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, NumberLocalInfo.FIELD_NUMBER + " = ?", new String[]{number}, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				numberLocalInfo = NumberLocalInfo.getObjectFromDb(cursor);
			}
			cursor.close();
		}
		if (numberLocalInfo != null)
		{
			return numberLocalInfo.getLocalInfo();
		}
		else
		{
			return "";
		}
	}
}
