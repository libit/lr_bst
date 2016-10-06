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
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by libit on 16/7/8.
 */
public class DbProductInfoFactory
{
	private static final String TABLE_NAME = DbConstant.TABLE_NAME_PRODUCT;
	private static DbProductInfoFactory instance;

	synchronized public static DbProductInfoFactory getInstance()
	{
		if (instance == null)
		{
			instance = new DbProductInfoFactory();
		}
		return instance;
	}

	/**
	 * 增加或更新商品信息
	 *
	 * @param productId   商品ID
	 * @param name        商品名称
	 * @param sortId      分类名
	 * @param brandId     品牌名
	 * @param picId       图片地址
	 * @param price       价格
	 * @param marketPrice 市场价
	 * @param count       库存
	 * @param description 描述
	 * @param config      参数
	 * @param content     内容
	 * @return
	 */
	public boolean addOrUpdateProductInfo(String productId, String name, String sortId, String brandId, String picId, int price, int marketPrice, int expressPrice, int count, String description, String config, String content, byte needExpress)
	{
		if (StringTools.isNull(productId) || StringTools.isNull(name) || StringTools.isNull(sortId) || StringTools.isNull(brandId) || StringTools.isNull(picId) || StringTools.isNull(description))
		{
			return false;
		}
		ProductInfo productInfo = new ProductInfo(productId, name, sortId, brandId, picId, price, marketPrice, expressPrice, count, description, config, content, needExpress);
		return addOrUpdateProductInfo(productInfo);
	}

	/**
	 * 增加或更新商品信息
	 *
	 * @param productInfo
	 * @return
	 */
	public boolean addOrUpdateProductInfo(ProductInfo productInfo)
	{
		if (productInfo == null || StringTools.isNull(productInfo.getSortId()) || StringTools.isNull(productInfo.getName()) || StringTools.isNull(productInfo.getDescription()))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		ContentValues values = productInfo.getObjectContentValues();
		int row = contentResolver.update(tableUri, values, ProductInfo.FIELD_PRODUCT_ID + " = ?", new String[]{productInfo.getProductId()});
		if (row < 1)
		{
			return contentResolver.insert(tableUri, values) != null;
		}
		return true;
	}

	/**
	 * 删除商品信息
	 *
	 * @param productId 商品ID
	 * @return
	 */
	public int deleteProductInfo(String productId)
	{
		if (StringTools.isNull(productId))
		{
			return 0;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		return contentResolver.delete(tableUri, ProductInfo.FIELD_PRODUCT_ID + " = ?", new String[]{productId});
	}

	/**
	 * 删除商品信息
	 *
	 * @return
	 */
	public int clearProductInfo()
	{
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		return contentResolver.delete(tableUri, null, null);
	}

	/**
	 * 获取商品信息
	 *
	 * @param productId 商品ID
	 * @return
	 */
	public ProductInfo getProductInfo(String productId)
	{
		if (StringTools.isNull(productId))
		{
			return null;
		}
		ProductInfo productInfo = null;
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, ProductInfo.FIELD_PRODUCT_ID + " = ?", new String[]{productId}, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				productInfo = ProductInfo.getObjectFromDb(cursor);
			}
			cursor.close();
		}
		return productInfo;
	}

	/**
	 * 获取商品列表
	 *
	 * @return
	 */
	public List<ProductInfo> getProductInfoList()
	{
		List<ProductInfo> list = new ArrayList<>();
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, null, null, null);
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				ProductInfo productInfo = ProductInfo.getObjectFromDb(cursor);
				list.add(productInfo);
			}
			cursor.close();
		}
		return list;
	}

	/**
	 * 获取商品列表
	 *
	 * @param sortId 分类
	 * @return
	 */
	public List<ProductInfo> getProductInfoListBySortId(String sortId)
	{
		List<ProductInfo> list = new ArrayList<>();
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, ProductInfo.FIELD_SORT_ID + " = ?", new String[]{sortId}, null);
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				ProductInfo productInfo = ProductInfo.getObjectFromDb(cursor);
				list.add(productInfo);
			}
			cursor.close();
		}
		return list;
	}
}
