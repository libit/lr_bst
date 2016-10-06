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
import com.lrcall.appbst.models.ProductStarInfo;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by libit on 16/7/8.
 */
public class DbProductStarInfoFactory
{
	private static final String TABLE_NAME = DbConstant.TABLE_NAME_STAR;
	private static DbProductStarInfoFactory instance;

	synchronized public static DbProductStarInfoFactory getInstance()
	{
		if (instance == null)
		{
			instance = new DbProductStarInfoFactory();
		}
		return instance;
	}

	/**
	 * 增加或更新商品收藏信息
	 *
	 * @param starId    ID
	 * @param userId    用户ID
	 * @param productId 商品ID
	 * @param date      添加时间
	 * @return
	 */
	public boolean addOrUpdateProductStarInfo(String starId, String userId, String productId, long date)
	{
		if (StringTools.isNull(starId) || StringTools.isNull(userId) || StringTools.isNull(productId))
		{
			return false;
		}
		ProductStarInfo productStarInfo = new ProductStarInfo(starId, userId, productId, date);
		return addOrUpdateProductStarInfo(productStarInfo);
	}

	/**
	 * 增加或更新商品收藏信息
	 *
	 * @param productStarInfo
	 * @return
	 */
	public boolean addOrUpdateProductStarInfo(ProductStarInfo productStarInfo)
	{
		if (productStarInfo == null || StringTools.isNull(productStarInfo.getStarId()) || StringTools.isNull(productStarInfo.getUserId()) || StringTools.isNull(productStarInfo.getProductId()))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		ContentValues values = productStarInfo.getObjectContentValues();
		int row = contentResolver.update(tableUri, values, ProductStarInfo.FIELD_STAR_ID + " = ?", new String[]{productStarInfo.getStarId()});
		if (row < 1)
		{
			return contentResolver.insert(tableUri, values) != null;
		}
		return true;
	}

	/**
	 * 删除商品收藏信息
	 *
	 * @param starId ID
	 * @return
	 */
	public int deleteProductStarInfo(String starId)
	{
		if (StringTools.isNull(starId))
		{
			return 0;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		return contentResolver.delete(tableUri, ProductStarInfo.FIELD_STAR_ID + " = ?", new String[]{starId});
	}

	/**
	 * 删除商品收藏信息
	 *
	 * @return
	 */
	public int clearProductStarInfo()
	{
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		return contentResolver.delete(tableUri, null, null);
	}

	/**
	 * 获取商品收藏信息
	 *
	 * @param starId ID
	 * @return
	 */
	public ProductStarInfo getProductStarInfo(String starId)
	{
		if (StringTools.isNull(starId))
		{
			return null;
		}
		ProductStarInfo productStarInfo = null;
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, ProductStarInfo.FIELD_STAR_ID + " = ?", new String[]{starId}, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				productStarInfo = ProductStarInfo.getObjectFromDb(cursor);
			}
			cursor.close();
		}
		return productStarInfo;
	}

	/**
	 * 获取商品收藏信息
	 *
	 * @param userId   用户ID
	 * @param produtId 商品ID
	 * @return
	 */
	public ProductStarInfo getProductStarInfo(String userId, String produtId)
	{
		if (StringTools.isNull(userId) || StringTools.isNull(produtId))
		{
			return null;
		}
		ProductStarInfo productStarInfo = null;
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, ProductStarInfo.FIELD_USER_ID + " = ? and " + ProductStarInfo.FIELD_PRODUCT_ID + " = ?", new String[]{userId, produtId}, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				productStarInfo = ProductStarInfo.getObjectFromDb(cursor);
			}
			cursor.close();
		}
		return productStarInfo;
	}

	/**
	 * 获取商品收藏列表
	 *
	 * @param userId 用户ID
	 * @return
	 */
	public List<ProductStarInfo> getProductStarInfoList(String userId)
	{
		List<ProductStarInfo> list = new ArrayList<>();
		if (StringTools.isNull(userId))
		{
			return list;
		}
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, ProductStarInfo.FIELD_USER_ID + " = ?", new String[]{userId}, null);
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				ProductStarInfo productStarInfo = ProductStarInfo.getObjectFromDb(cursor);
				list.add(productStarInfo);
			}
			cursor.close();
		}
		return list;
	}
}
