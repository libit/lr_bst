/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;
import com.lrcall.db.DbConstant;

import java.util.Comparator;

/**
 * Created by libit on 16/7/14.
 */
public class ProductSortInfo extends DbObject implements Comparator<ProductSortInfo>
{
	public static final String FIELD_SORT_ID = "sort_id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_PARENT_ID = "parent_id";
	public static final String FIELD_LEVEL_ID = "level_id";
	public static final String FIELD_PIC_URL = "pic_url";
	@SerializedName("sortId")
	private String sortId;//分类ID
	@SerializedName("name")
	private String name;//分类名称
	@SerializedName("parentId")
	private String parentId;//上级ID
	@SerializedName("levelId")
	private int levelId;//层级
	@SerializedName("picId")
	private String picId;//图片ID--存储的是图片地址
	//	private String picUrl;//图片地址

	public ProductSortInfo()
	{
	}

	public ProductSortInfo(String sortId, String name, String parentId, int levelId, String picUrl)
	{
		this.sortId = sortId;
		this.name = name;
		this.parentId = parentId;
		this.levelId = levelId;
		this.picId = picUrl;
	}

	/**
	 * 获取创建表的SQL语句
	 *
	 * @return SQL语句
	 */
	public static final String getCreateTableSQL()
	{
		// 归属地信息表
		return String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL UNIQUE,%s TEXT,%s INTEGER NOT NULL,%s TEXT);", DbConstant.TABLE_NAME_PRODUCT_SORT, FIELD_ID, FIELD_SORT_ID, FIELD_NAME, FIELD_PARENT_ID, FIELD_LEVEL_ID, FIELD_PIC_URL);
	}

	/**
	 * 从数据库中取出对象
	 *
	 * @param cursor
	 * @return
	 */
	public static ProductSortInfo getObjectFromDb(Cursor cursor)
	{
		ProductSortInfo productSortInfo = new ProductSortInfo();
		productSortInfo.setSortId(cursor.getString(cursor.getColumnIndex(FIELD_SORT_ID)));
		productSortInfo.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
		productSortInfo.setParentId(cursor.getString(cursor.getColumnIndex(FIELD_PARENT_ID)));
		productSortInfo.setLevelId((byte) cursor.getInt(cursor.getColumnIndex(FIELD_LEVEL_ID)));
		productSortInfo.setPicId(cursor.getString(cursor.getColumnIndex(FIELD_PIC_URL)));
		return productSortInfo;
	}

	/**
	 * 转换成数据库存储的数据
	 *
	 * @return ContentValues
	 */
	public ContentValues getObjectContentValues()
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(FIELD_SORT_ID, sortId);
		contentValues.put(FIELD_NAME, name);
		contentValues.put(FIELD_PARENT_ID, parentId);
		contentValues.put(FIELD_LEVEL_ID, levelId);
		contentValues.put(FIELD_PIC_URL, picId);
		return contentValues;
	}

	public String getSortId()
	{
		return sortId;
	}

	public void setSortId(String sortId)
	{
		this.sortId = sortId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	public int getLevelId()
	{
		return levelId;
	}

	public void setLevelId(int levelId)
	{
		this.levelId = levelId;
	}

	public String getPicId()
	{
		return picId;
	}

	public void setPicId(String picId)
	{
		this.picId = picId;
	}

	@Override
	public int compare(ProductSortInfo lhs, ProductSortInfo rhs)
	{
		if (lhs == null && rhs == null)
		{
			return 0;
		}
		if (lhs == null && rhs != null)
		{
			return -1;
		}
		if (rhs == null)
		{
			return -1;
		}
		if (lhs == rhs)
		{
			return 0;
		}
		String lName = lhs.getName();
		String rName = rhs.getName();
		return lName.compareTo(rName);
	}
}
