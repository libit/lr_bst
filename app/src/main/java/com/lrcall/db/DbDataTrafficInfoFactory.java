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
import com.lrcall.appbst.models.DataTrafficInfo;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by libit on 16/7/8.
 */
public class DbDataTrafficInfoFactory
{
	private static final String TABLE_NAME = DbConstant.TABLE_NAME_DATA_TRAFFIC;
	private static DbDataTrafficInfoFactory instance;

	synchronized public static DbDataTrafficInfoFactory getInstance()
	{
		if (instance == null)
		{
			instance = new DbDataTrafficInfoFactory();
		}
		return instance;
	}

	/**
	 * 增加或更新流量充值信息
	 *
	 * @param dataTrafficInfo
	 * @return
	 */
	public boolean addOrUpdateDataTrafficInfo(DataTrafficInfo dataTrafficInfo)
	{
		if (dataTrafficInfo == null || StringTools.isNull(dataTrafficInfo.getProductId()))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		ContentValues values = dataTrafficInfo.getObjectContentValues();
		int row = contentResolver.update(tableUri, values, DataTrafficInfo.FIELD_PRODUCT_ID + " = ?", new String[]{dataTrafficInfo.getProductId()});
		if (row < 1)
		{
			return contentResolver.insert(tableUri, values) != null;
		}
		return true;
	}

	/**
	 * 删除流量充值信息
	 *
	 * @param productId 商品ID
	 * @return
	 */
	public boolean deleteDataTrafficInfo(String productId)
	{
		if (StringTools.isNull(productId))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		int row = contentResolver.delete(tableUri, DataTrafficInfo.FIELD_PRODUCT_ID + " = ?", new String[]{productId});
		return (row > 0);
	}

	/**
	 * 清空流量充值信息
	 *
	 * @return
	 */
	public boolean clearDataTrafficInfo()
	{
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		int row = contentResolver.delete(tableUri, null, null);
		return row > 0;
	}

	/**
	 * 获取流量充值信息
	 *
	 * @param productId 商品ID
	 * @return
	 */
	public DataTrafficInfo getDataTrafficInfo(String productId)
	{
		if (StringTools.isNull(productId))
		{
			return null;
		}
		DataTrafficInfo dataTrafficInfo = null;
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, DataTrafficInfo.FIELD_PRODUCT_ID + " = ?", new String[]{productId}, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				dataTrafficInfo = DataTrafficInfo.getObjectFromDb(cursor);
			}
			cursor.close();
		}
		return dataTrafficInfo;
	}

	/**
	 * 获取流量充值列表
	 *
	 * @return
	 */
	public List<DataTrafficInfo> getDataTrafficInfoList()
	{
		List<DataTrafficInfo> list = new ArrayList<>();
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, null, null, null);
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				DataTrafficInfo dataTrafficInfo = DataTrafficInfo.getObjectFromDb(cursor);
				list.add(dataTrafficInfo);
			}
			cursor.close();
		}
		return list;
	}
}
