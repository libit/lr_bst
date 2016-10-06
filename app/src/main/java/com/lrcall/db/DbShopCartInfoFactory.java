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
import com.lrcall.appbst.models.ShopCartInfo;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by libit on 16/7/8.
 */
public class DbShopCartInfoFactory
{
	private static final String TABLE_NAME = DbConstant.TABLE_NAME_SHOP_CART;
	private static DbShopCartInfoFactory instance;

	synchronized public static DbShopCartInfoFactory getInstance()
	{
		if (instance == null)
		{
			instance = new DbShopCartInfoFactory();
		}
		return instance;
	}

	/**
	 * 增加或更新购物车信息
	 *
	 * @param shopCartInfo
	 * @return
	 */
	public boolean addOrUpdateShopCartInfo(ShopCartInfo shopCartInfo)
	{
		if (shopCartInfo == null || StringTools.isNull(shopCartInfo.getCartId()) || StringTools.isNull(shopCartInfo.getUserId()) || StringTools.isNull(shopCartInfo.getProductId()))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		ContentValues values = shopCartInfo.getObjectContentValues();
		int row = contentResolver.update(tableUri, values, ShopCartInfo.FIELD_CART_ID + " = ?", new String[]{shopCartInfo.getCartId()});
		if (row < 1)
		{
			return contentResolver.insert(tableUri, values) != null;
		}
		return true;
	}

	/**
	 * 删除购物车信息
	 *
	 * @param cartId
	 * @return
	 */
	public int deleteShopCartInfo(String cartId)
	{
		if (StringTools.isNull(cartId))
		{
			return 0;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		return contentResolver.delete(tableUri, ShopCartInfo.FIELD_CART_ID + " = ?", new String[]{cartId});
	}

	/**
	 * 删除购物车信息
	 *
	 * @return
	 */
	public int clearShopCartInfo()
	{
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		return contentResolver.delete(tableUri, null, null);
	}

	/**
	 * 获取购物车信息
	 *
	 * @param cartId
	 * @return
	 */
	public ShopCartInfo getShopCartInfo(String cartId)
	{
		if (StringTools.isNull(cartId))
		{
			return null;
		}
		ShopCartInfo shopCartInfo = null;
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, ShopCartInfo.FIELD_CART_ID + " = ?", new String[]{cartId}, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				shopCartInfo = ShopCartInfo.getObjectFromDb(cursor);
			}
			cursor.close();
		}
		return shopCartInfo;
	}

	/**
	 * 获取购物车列表
	 *
	 * @return
	 */
	public List<ShopCartInfo> getShopCartInfoList(String userId)
	{
		List<ShopCartInfo> list = new ArrayList<>();
		if (StringTools.isNull(userId))
		{
			return list;
		}
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, ShopCartInfo.FIELD_USER_ID + " = ?", new String[]{userId}, null);
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				ShopCartInfo shopCartInfo = ShopCartInfo.getObjectFromDb(cursor);
				list.add(shopCartInfo);
			}
			cursor.close();
		}
		return list;
	}
}
