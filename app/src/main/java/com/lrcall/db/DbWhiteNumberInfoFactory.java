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
import com.lrcall.appbst.models.WhiteNumberInfo;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by libit on 16/7/8.
 */
public class DbWhiteNumberInfoFactory
{
	private static final String TABLE_NAME = DbConstant.TABLE_NAME_WHITE_NUMBER;
	private static DbWhiteNumberInfoFactory instance;

	synchronized public static DbWhiteNumberInfoFactory getInstance()
	{
		if (instance == null)
		{
			instance = new DbWhiteNumberInfoFactory();
		}
		return instance;
	}

	/**
	 * 增加或更新白名单信息
	 *
	 * @param number 电话号码
	 * @param name   名字
	 * @param remark 备注
	 * @return
	 */
	public boolean addOrUpdateWhiteNumberInfo(String number, String name, String remark)
	{
		if (StringTools.isNull(number))
		{
			return false;
		}
		WhiteNumberInfo whiteNumberInfo = new WhiteNumberInfo(number, name, remark);
		return addOrUpdateWhiteNumberInfo(whiteNumberInfo);
	}

	/**
	 * 增加或更新白名单信息
	 *
	 * @param whiteNumberInfo
	 * @return
	 */
	public boolean addOrUpdateWhiteNumberInfo(WhiteNumberInfo whiteNumberInfo)
	{
		if (whiteNumberInfo == null || StringTools.isNull(whiteNumberInfo.getNumber()))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		ContentValues values = whiteNumberInfo.getObjectContentValues();
		int row = contentResolver.update(tableUri, values, WhiteNumberInfo.FIELD_NUMBER + " = ?", new String[]{whiteNumberInfo.getNumber()});
		if (row < 1)
		{
			return contentResolver.insert(tableUri, values) != null;
		}
		return true;
	}

	/**
	 * 删除白名单信息
	 *
	 * @param number 电话号码
	 * @return
	 */
	public boolean deleteWhiteNumberInfo(String number)
	{
		if (StringTools.isNull(number))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		int row = contentResolver.delete(tableUri, WhiteNumberInfo.FIELD_NUMBER + " = ?", new String[]{number});
		return (row > 0);
	}

	/**
	 * 获取电话号码的白名单信息
	 *
	 * @param number 电话号码
	 * @return
	 */
	public WhiteNumberInfo getWhiteNumberInfo(String number)
	{
		if (StringTools.isNull(number))
		{
			return null;
		}
		WhiteNumberInfo whiteNumberInfo = null;
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, WhiteNumberInfo.FIELD_NUMBER + " = ?", new String[]{number}, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				whiteNumberInfo = WhiteNumberInfo.getObjectFromDb(cursor);
			}
			cursor.close();
		}
		return whiteNumberInfo;
	}

	/**
	 * 获取白名单列表
	 *
	 * @return
	 */
	public List<WhiteNumberInfo> getWhiteNumberInfoList()
	{
		List<WhiteNumberInfo> list = new ArrayList<>();
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, null, null, null);
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				WhiteNumberInfo whiteNumberInfo = WhiteNumberInfo.getObjectFromDb(cursor);
				list.add(whiteNumberInfo);
			}
			cursor.close();
		}
		return list;
	}
}
