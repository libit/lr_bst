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
import com.lrcall.appbst.models.UserAddressInfo;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by libit on 16/7/8.
 */
public class DbUserAddressInfoFactory
{
	private static final String TABLE_NAME = DbConstant.TABLE_NAME_ADDRESS;
	private static DbUserAddressInfoFactory instance;

	synchronized public static DbUserAddressInfoFactory getInstance()
	{
		if (instance == null)
		{
			instance = new DbUserAddressInfoFactory();
		}
		return instance;
	}

	/**
	 * 增加或更新地址信息
	 *
	 * @param addressId      ID
	 * @param userId         用户ID
	 * @param name           姓名
	 * @param number         电话号码
	 * @param country        国家
	 * @param province       省
	 * @param city           市
	 * @param district       区
	 * @param address        详细地址
	 * @param status         状态
	 * @param updateDateLong 日期
	 * @return
	 */
	public boolean addOrUpdateUserAddressInfo(String addressId, String userId, String name, String number, String country, String province, String city, String district, String address, byte status, long updateDateLong)
	{
		if (StringTools.isNull(addressId) || StringTools.isNull(userId) || StringTools.isNull(name) || StringTools.isNull(number) || StringTools.isNull(address))
		{
			return false;
		}
		UserAddressInfo userAddressInfo = new UserAddressInfo(addressId, userId, name, number, country, province, city, district, address, status, updateDateLong);
		return addOrUpdateUserAddressInfo(userAddressInfo);
	}

	/**
	 * 增加或更新地址信息
	 *
	 * @param userAddressInfo
	 * @return
	 */
	public boolean addOrUpdateUserAddressInfo(UserAddressInfo userAddressInfo)
	{
		if (userAddressInfo == null || StringTools.isNull(userAddressInfo.getAddressId()) || StringTools.isNull(userAddressInfo.getUserId()) || StringTools.isNull(userAddressInfo.getName()) || StringTools.isNull(userAddressInfo.getNumber()) || StringTools.isNull(userAddressInfo.getAddress()))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		ContentValues values = userAddressInfo.getObjectContentValues();
		int row = contentResolver.update(tableUri, values, UserAddressInfo.FIELD_ADDRESS_ID + " = ?", new String[]{userAddressInfo.getAddressId()});
		if (row < 1)
		{
			return contentResolver.insert(tableUri, values) != null;
		}
		return true;
	}

	/**
	 * 更新地址状态
	 *
	 * @param addressId ID
	 * @param status    状态
	 * @return
	 */
	public boolean updateUserAddressInfoStatus(String addressId, int status)
	{
		if (StringTools.isNull(addressId))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		ContentValues values = new ContentValues();
		values.put(UserAddressInfo.FIELD_STATUS, status);
		int row = contentResolver.update(tableUri, values, UserAddressInfo.FIELD_ADDRESS_ID + " = ?", new String[]{addressId});
		return row > 0;
	}

	/**
	 * 删除地址信息
	 *
	 * @param addressId ID
	 * @return
	 */
	public int deleteUserAddressInfo(String addressId)
	{
		if (StringTools.isNull(addressId))
		{
			return 0;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		return contentResolver.delete(tableUri, UserAddressInfo.FIELD_ADDRESS_ID + " = ?", new String[]{addressId});
	}

	/**
	 * 删除地址信息
	 *
	 * @return
	 */
	public int clearUserAddressInfo()
	{
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		return contentResolver.delete(tableUri, null, null);
	}

	/**
	 * 获取地址信息
	 *
	 * @param addressId ID
	 * @return
	 */
	public UserAddressInfo getUserAddressInfo(String addressId)
	{
		if (StringTools.isNull(addressId))
		{
			return null;
		}
		UserAddressInfo userAddressInfo = null;
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, UserAddressInfo.FIELD_ADDRESS_ID + " = ?", new String[]{addressId}, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				userAddressInfo = UserAddressInfo.getObjectFromDb(cursor);
			}
			cursor.close();
		}
		return userAddressInfo;
	}

	/**
	 * 获取地址列表
	 *
	 * @param userId 用户ID
	 * @return
	 */
	public List<UserAddressInfo> getUserAddressInfoList(String userId)
	{
		List<UserAddressInfo> list = new ArrayList<>();
		if (StringTools.isNull(userId))
		{
			return list;
		}
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, UserAddressInfo.FIELD_USER_ID + " = ?", new String[]{userId}, null);
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				UserAddressInfo userAddressInfo = UserAddressInfo.getObjectFromDb(cursor);
				list.add(userAddressInfo);
			}
			cursor.close();
		}
		return list;
	}
}
