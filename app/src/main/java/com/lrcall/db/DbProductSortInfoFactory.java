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
import com.lrcall.appbst.models.ProductSortInfo;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by libit on 16/7/8.
 */
public class DbProductSortInfoFactory
{
	private static final String TABLE_NAME = DbConstant.TABLE_NAME_PRODUCT_SORT;
	private static DbProductSortInfoFactory instance;

	synchronized public static DbProductSortInfoFactory getInstance()
	{
		if (instance == null)
		{
			instance = new DbProductSortInfoFactory();
		}
		return instance;
	}

	/**
	 * 增加或更新商品分类信息
	 *
	 * @param sortId   分类ID
	 * @param name     分类名
	 * @param parentId 父分类ID
	 * @param levelId  层级
	 * @param picUrl   图片地址
	 * @return
	 */
	public boolean addOrUpdateProductSortInfo(String sortId, String name, String parentId, int levelId, String picUrl)
	{
		if (StringTools.isNull(sortId) || StringTools.isNull(name))
		{
			return false;
		}
		ProductSortInfo productSortInfo = new ProductSortInfo(sortId, name, parentId, levelId, picUrl);
		return addOrUpdateProductSortInfo(productSortInfo);
	}

	/**
	 * 增加或更新商品分类信息
	 *
	 * @param productSortInfo
	 * @return
	 */
	public boolean addOrUpdateProductSortInfo(ProductSortInfo productSortInfo)
	{
		if (productSortInfo == null || StringTools.isNull(productSortInfo.getSortId()) || StringTools.isNull(productSortInfo.getName()))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		ContentValues values = productSortInfo.getObjectContentValues();
		int row = contentResolver.update(tableUri, values, ProductSortInfo.FIELD_SORT_ID + " = ?", new String[]{productSortInfo.getSortId()});
		if (row < 1)
		{
			return contentResolver.insert(tableUri, values) != null;
		}
		return true;
	}

	/**
	 * 删除商品分类信息
	 *
	 * @param sortId 分类ID
	 * @return
	 */
	public int deleteProductSortInfo(String sortId)
	{
		if (StringTools.isNull(sortId))
		{
			return 0;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		return contentResolver.delete(tableUri, ProductSortInfo.FIELD_SORT_ID + " = ?", new String[]{sortId});
	}

	/**
	 * 删除商品分类信息
	 *
	 * @return
	 */
	public int clearProductSortInfo()
	{
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		return contentResolver.delete(tableUri, null, null);
	}

	/**
	 * 获取商品分类信息
	 *
	 * @param sortId 分类ID
	 * @return
	 */
	public ProductSortInfo getProductSortInfo(String sortId)
	{
		if (StringTools.isNull(sortId))
		{
			return null;
		}
		ProductSortInfo productSortInfo = null;
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, ProductSortInfo.FIELD_SORT_ID + " = ?", new String[]{sortId}, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				productSortInfo = ProductSortInfo.getObjectFromDb(cursor);
			}
			cursor.close();
		}
		return productSortInfo;
	}

	/**
	 * 获取商品分类列表
	 *
	 * @return
	 */
	public List<ProductSortInfo> getProductSortInfoList()
	{
		List<ProductSortInfo> list = new ArrayList<>();
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, null, null, null);
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				ProductSortInfo productSortInfo = ProductSortInfo.getObjectFromDb(cursor);
				list.add(productSortInfo);
			}
			cursor.close();
		}
		return list;
	}

	/**
	 * 获取商品分类列表
	 *
	 * @param levelId 分类级别
	 * @return
	 */
	public List<ProductSortInfo> getProductSortInfoListByLevelId(int levelId)
	{
		List<ProductSortInfo> list = new ArrayList<>();
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, ProductSortInfo.FIELD_LEVEL_ID + " = ?", new String[]{levelId + ""}, null);
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				ProductSortInfo productSortInfo = ProductSortInfo.getObjectFromDb(cursor);
				list.add(productSortInfo);
			}
			cursor.close();
		}
		return list;
	}

	/**
	 * 获取商品分类列表
	 *
	 * @param parentId 父分类ID
	 * @return
	 */
	public List<ProductSortInfo> getProductSortInfoListByParentId(String parentId)
	{
		List<ProductSortInfo> list = new ArrayList<>();
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, ProductSortInfo.FIELD_PARENT_ID + " = ?", new String[]{parentId}, null);
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				ProductSortInfo productSortInfo = ProductSortInfo.getObjectFromDb(cursor);
				list.add(productSortInfo);
			}
			cursor.close();
		}
		return list;
	}
}
