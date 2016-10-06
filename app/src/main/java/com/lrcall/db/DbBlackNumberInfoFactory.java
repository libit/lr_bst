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
import com.lrcall.appbst.models.BlackNumberInfo;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by libit on 16/7/8.
 */
public class DbBlackNumberInfoFactory
{
	private static final String TABLE_NAME = DbConstant.TABLE_NAME_BLACK_NUMBER;
	private static DbBlackNumberInfoFactory instance;

	synchronized public static DbBlackNumberInfoFactory getInstance()
	{
		if (instance == null)
		{
			instance = new DbBlackNumberInfoFactory();
		}
		return instance;
	}

	/**
	 * 增加或更新黑名单信息
	 *
	 * @param number 电话号码
	 * @param name   名字
	 * @param remark 备注
	 * @return
	 */
	public boolean addOrUpdateBlackNumberInfo(String number, String name, String remark)
	{
		if (StringTools.isNull(number))
		{
			return false;
		}
		BlackNumberInfo blackNumberInfo = new BlackNumberInfo(number, name, remark);
		return addOrUpdateBlackNumberInfo(blackNumberInfo);
	}

	/**
	 * 增加或更新黑名单信息
	 *
	 * @param blackNumberInfo
	 * @return
	 */
	public boolean addOrUpdateBlackNumberInfo(BlackNumberInfo blackNumberInfo)
	{
		if (blackNumberInfo == null || StringTools.isNull(blackNumberInfo.getNumber()))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		ContentValues values = blackNumberInfo.getObjectContentValues();
		int row = contentResolver.update(tableUri, values, BlackNumberInfo.FIELD_NUMBER + " = ?", new String[]{blackNumberInfo.getNumber()});
		if (row < 1)
		{
			return contentResolver.insert(tableUri, values) != null;
		}
		return true;
	}

	/**
	 * 删除黑名单信息
	 *
	 * @param number 电话号码
	 * @return
	 */
	public boolean deleteBlackNumberInfo(String number)
	{
		if (StringTools.isNull(number))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		int row = contentResolver.delete(tableUri, BlackNumberInfo.FIELD_NUMBER + " = ?", new String[]{number});
		return (row > 0);
	}

	/**
	 * 获取电话号码的黑名单信息
	 *
	 * @param number 电话号码
	 * @return
	 */
	public BlackNumberInfo getBlackNumberInfo(String number)
	{
		if (StringTools.isNull(number))
		{
			return null;
		}
		BlackNumberInfo blackNumberInfo = null;
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, BlackNumberInfo.FIELD_NUMBER + " = ?", new String[]{number}, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				blackNumberInfo = BlackNumberInfo.getObjectFromDb(cursor);
			}
			cursor.close();
		}
		return blackNumberInfo;
	}

	/**
	 * 获取黑名单列表
	 *
	 * @return
	 */
	public List<BlackNumberInfo> getBlackNumberInfoList()
	{
		List<BlackNumberInfo> list = new ArrayList<>();
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, null, null, null);
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				BlackNumberInfo blackNumberInfo = BlackNumberInfo.getObjectFromDb(cursor);
				list.add(blackNumberInfo);
			}
			cursor.close();
		}
		return list;
	}
}
