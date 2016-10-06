/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;
import com.lrcall.db.DbConstant;

/**
 * Created by libit on 16/7/18.
 */
public class ProductStarInfo extends DbObject
{
	public static final String FIELD_STAR_ID = "star_id";
	public static final String FIELD_USER_ID = "user_id";
	public static final String FIELD_PRODUCT_ID = "product_id";
	public static final String FIELD_DATE = "date";
	@SerializedName("id")
	private String id;
	@SerializedName("starId")
	private String starId;
	@SerializedName("userId")
	private String userId;
	@SerializedName("productId")
	private String productId;
	@SerializedName("addDateLong")
	private long date;

	/**
	 * 获取创建表的SQL语句
	 *
	 * @return SQL语句
	 */
	public static final String getCreateTableSQL()
	{
		return String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s TEXT NOT NULL,%s INTEGER);", DbConstant.TABLE_NAME_STAR, FIELD_ID, FIELD_STAR_ID, FIELD_USER_ID, FIELD_PRODUCT_ID, FIELD_DATE);
	}

	/**
	 * 从数据库中取出对象
	 *
	 * @param cursor
	 * @return
	 */
	public static ProductStarInfo getObjectFromDb(Cursor cursor)
	{
		ProductStarInfo productStarInfo = new ProductStarInfo();
		productStarInfo.setId(cursor.getString(cursor.getColumnIndex(FIELD_ID)));
		productStarInfo.setStarId(cursor.getString(cursor.getColumnIndex(FIELD_STAR_ID)));
		productStarInfo.setUserId(cursor.getString(cursor.getColumnIndex(FIELD_USER_ID)));
		productStarInfo.setProductId(cursor.getString(cursor.getColumnIndex(FIELD_PRODUCT_ID)));
		productStarInfo.setDate(cursor.getLong(cursor.getColumnIndex(FIELD_DATE)));
		return productStarInfo;
	}

	/**
	 * 转换成数据库存储的数据
	 *
	 * @return ContentValues
	 */
	public ContentValues getObjectContentValues()
	{
		ContentValues contentValues = new ContentValues();
		if (id != null)
		{
			contentValues.put(FIELD_ID, id);
		}
		contentValues.put(FIELD_STAR_ID, starId);
		contentValues.put(FIELD_USER_ID, userId);
		contentValues.put(FIELD_PRODUCT_ID, productId);
		contentValues.put(FIELD_DATE, date);
		return contentValues;
	}

	public ProductStarInfo()
	{
	}

	public ProductStarInfo(String starId, String userId, String productId, long date)
	{
		this.starId = starId;
		this.userId = userId;
		this.productId = productId;
		this.date = date;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getStarId()
	{
		return starId;
	}

	public void setStarId(String starId)
	{
		this.starId = starId;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public long getDate()
	{
		return date;
	}

	public void setDate(long date)
	{
		this.date = date;
	}
}
