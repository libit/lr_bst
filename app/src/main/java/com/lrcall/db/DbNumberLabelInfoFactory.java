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
import com.lrcall.appbst.models.NumberLabelInfo;
import com.lrcall.utils.StringTools;

/**
 * Created by libit on 16/7/8.
 */
public class DbNumberLabelInfoFactory
{
	private static final String TABLE_NAME = DbConstant.TABLE_NAME_NUMBER_LABEL;
	private static DbNumberLabelInfoFactory instance;

	synchronized public static DbNumberLabelInfoFactory getInstance()
	{
		if (instance == null)
		{
			instance = new DbNumberLabelInfoFactory();
		}
		return instance;
	}

	/**
	 * 增加或更新号码标记信息
	 *
	 * @param number 电话号码
	 * @param label  标记
	 * @param count  标记人数
	 * @return
	 */
	public boolean addOrUpdateNumberLabelInfo(String number, String label, int count)
	{
		if (StringTools.isNull(number) || StringTools.isNull(label))
		{
			return false;
		}
		NumberLabelInfo numberLabelInfo = new NumberLabelInfo(number, label, count);
		return addOrUpdateNumberLabelInfo(numberLabelInfo);
	}

	/**
	 * 增加或更新号码标记信息
	 *
	 * @param numberLabelInfo
	 * @return
	 */
	public boolean addOrUpdateNumberLabelInfo(NumberLabelInfo numberLabelInfo)
	{
		if (numberLabelInfo == null || StringTools.isNull(numberLabelInfo.getNumber()) || StringTools.isNull(numberLabelInfo.getLabel()))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		ContentValues values = numberLabelInfo.getObjectContentValues();
		int row = contentResolver.update(tableUri, values, NumberLabelInfo.FIELD_NUMBER + " = ?", new String[]{numberLabelInfo.getNumber()});
		if (row < 1)
		{
			return contentResolver.insert(tableUri, values) != null;
		}
		return true;
	}

	/**
	 * 获取电话号码的标记信息
	 *
	 * @param number 电话号码
	 * @return
	 */
	public NumberLabelInfo getNumberLabelInfo(String number)
	{
		if (StringTools.isNull(number))
		{
			return null;
		}
		NumberLabelInfo numberLabelInfo = null;
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, NumberLabelInfo.FIELD_NUMBER + " = ?", new String[]{number}, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				numberLabelInfo = NumberLabelInfo.getObjectFromDb(cursor);
			}
			cursor.close();
		}
		return numberLabelInfo;
	}
}
