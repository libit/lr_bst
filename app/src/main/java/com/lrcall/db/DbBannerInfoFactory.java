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
import com.lrcall.appbst.models.BannerInfo;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by libit on 16/7/8.
 */
public class DbBannerInfoFactory
{
	private static final String TABLE_NAME = DbConstant.TABLE_NAME_BANNER;
	private static DbBannerInfoFactory instance;

	synchronized public static DbBannerInfoFactory getInstance()
	{
		if (instance == null)
		{
			instance = new DbBannerInfoFactory();
		}
		return instance;
	}

	/**
	 * 增加或更新Banner信息
	 *
	 * @param bannerInfo
	 * @return
	 */
	public boolean addOrUpdateBannerInfo(BannerInfo bannerInfo)
	{
		if (bannerInfo == null || StringTools.isNull(bannerInfo.getBannerId()) || StringTools.isNull(bannerInfo.getBannerType()) || StringTools.isNull(bannerInfo.getPicUrl()))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		ContentValues values = bannerInfo.getObjectContentValues();
		int row = contentResolver.update(tableUri, values, BannerInfo.FIELD_BANNER_ID + " = ?", new String[]{bannerInfo.getBannerId()});
		if (row < 1)
		{
			return contentResolver.insert(tableUri, values) != null;
		}
		return true;
	}

	/**
	 * 删除Banner信息
	 *
	 * @param bannerId
	 * @return
	 */
	public int deleteBannerInfo(String bannerId)
	{
		if (StringTools.isNull(bannerId))
		{
			return 0;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		return contentResolver.delete(tableUri, BannerInfo.FIELD_BANNER_ID + " = ?", new String[]{bannerId});
	}

	/**
	 * 删除Banner信息
	 *
	 * @return
	 */
	public int clearBannerInfo()
	{
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		return contentResolver.delete(tableUri, null, null);
	}

	/**
	 * 获取Banner信息
	 *
	 * @param bannerId
	 * @return
	 */
	public BannerInfo getBannerInfo(String bannerId)
	{
		if (StringTools.isNull(bannerId))
		{
			return null;
		}
		BannerInfo bannerInfo = null;
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, BannerInfo.FIELD_BANNER_ID + " = ?", new String[]{bannerId}, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				bannerInfo = BannerInfo.getObjectFromDb(cursor);
			}
			cursor.close();
		}
		return bannerInfo;
	}

	/**
	 * 获取Banner列表
	 *
	 * @return
	 */
	public List<BannerInfo> getBannerInfoList(String bannerType)
	{
		List<BannerInfo> list = new ArrayList<>();
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, BannerInfo.FIELD_BANNER_TYPE + " = ?", new String[]{bannerType}, null);
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				BannerInfo bannerInfo = BannerInfo.getObjectFromDb(cursor);
				list.add(bannerInfo);
			}
			cursor.close();
		}
		return list;
	}
}
