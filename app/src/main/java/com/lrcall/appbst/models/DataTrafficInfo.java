/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;
import com.lrcall.db.DbConstant;

// Generated 2016-9-5 14:47:54 by Hibernate Tools 4.3.1
public class DataTrafficInfo extends DbObject
{
	public static final String FIELD_PRODUCT_ID = "product_id";
	public static final String FIELD_SORT_NAME = "sort_name";
	public static final String FIELD_DATA_TYPE = "data_type";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_PIC_URL = "pic_url";
	public static final String FIELD_PRICE = "price";
	public static final String FIELD_MARKET_PRICE = "market_price";
	public static final String FIELD_COUNT = "count";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_VALIDATE_DATE = "validate_date";
	public static final String FIELD_STATUS = "status";
	@SerializedName("productId")
	private String productId;
	@SerializedName("sortId")
	private String sortName;
	@SerializedName("dataType")
	private String dataType;
	@SerializedName("name")
	private String name;
	@SerializedName("picId")
	private String picUrl;
	@SerializedName("price")
	private int price;
	@SerializedName("marketPrice")
	private int marketPrice;
	@SerializedName("count")
	private int count;
	@SerializedName("description")
	private String description;
	@SerializedName("validateDate")
	private Integer validateDate;
	@SerializedName("status")
	private int status;

	/**
	 * 获取创建表的SQL语句
	 *
	 * @return SQL语句
	 */
	public static final String getCreateTableSQL()
	{
		return String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL,%s INT DEFAULT 0,%s INT DEFAULT 0,%s INT DEFAULT 0,%s TEXT,%s INT,%s INT DEFAULT 0);", DbConstant.TABLE_NAME_DATA_TRAFFIC, FIELD_ID, FIELD_PRODUCT_ID, FIELD_SORT_NAME, FIELD_DATA_TYPE, FIELD_NAME, FIELD_PIC_URL, FIELD_PRICE, FIELD_MARKET_PRICE, FIELD_COUNT, FIELD_DESCRIPTION, FIELD_VALIDATE_DATE, FIELD_STATUS);
	}

	/**
	 * 从数据库中取出对象
	 *
	 * @param cursor
	 * @return
	 */
	public static DataTrafficInfo getObjectFromDb(Cursor cursor)
	{
		DataTrafficInfo dataTrafficInfo = new DataTrafficInfo();
		dataTrafficInfo.setProductId(cursor.getString(cursor.getColumnIndex(FIELD_PRODUCT_ID)));
		dataTrafficInfo.setSortName(cursor.getString(cursor.getColumnIndex(FIELD_SORT_NAME)));
		dataTrafficInfo.setDataType(cursor.getString(cursor.getColumnIndex(FIELD_DATA_TYPE)));
		dataTrafficInfo.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
		dataTrafficInfo.setPicUrl(cursor.getString(cursor.getColumnIndex(FIELD_PIC_URL)));
		dataTrafficInfo.setPrice(cursor.getInt(cursor.getColumnIndex(FIELD_PRICE)));
		dataTrafficInfo.setMarketPrice(cursor.getInt(cursor.getColumnIndex(FIELD_MARKET_PRICE)));
		dataTrafficInfo.setCount(cursor.getInt(cursor.getColumnIndex(FIELD_COUNT)));
		dataTrafficInfo.setDescription(cursor.getString(cursor.getColumnIndex(FIELD_DESCRIPTION)));
		dataTrafficInfo.setValidateDate(cursor.getInt(cursor.getColumnIndex(FIELD_VALIDATE_DATE)));
		dataTrafficInfo.setStatus(cursor.getInt(cursor.getColumnIndex(FIELD_STATUS)));
		return dataTrafficInfo;
	}

	/**
	 * 转换成数据库存储的数据
	 *
	 * @return ContentValues
	 */
	public ContentValues getObjectContentValues()
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(FIELD_PRODUCT_ID, productId);
		contentValues.put(FIELD_SORT_NAME, sortName);
		contentValues.put(FIELD_DATA_TYPE, dataType);
		contentValues.put(FIELD_NAME, name);
		contentValues.put(FIELD_PIC_URL, picUrl);
		contentValues.put(FIELD_PRICE, price);
		contentValues.put(FIELD_MARKET_PRICE, marketPrice);
		contentValues.put(FIELD_COUNT, count);
		contentValues.put(FIELD_DESCRIPTION, description);
		contentValues.put(FIELD_VALIDATE_DATE, validateDate);
		contentValues.put(FIELD_STATUS, status);
		return contentValues;
	}

	public DataTrafficInfo()
	{
	}

	public DataTrafficInfo(String productId, String sortName, String name, String picUrl, int price, int marketPrice, int count, String description, Integer validateDate, int status)
	{
		this.productId = productId;
		this.sortName = sortName;
		this.name = name;
		this.picUrl = picUrl;
		this.price = price;
		this.marketPrice = marketPrice;
		this.count = count;
		this.description = description;
		this.validateDate = validateDate;
		this.status = status;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public String getSortName()
	{
		return sortName;
	}

	public void setSortName(String sortName)
	{
		this.sortName = sortName;
	}

	public String getDataType()
	{
		return dataType;
	}

	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPicUrl()
	{
		return picUrl;
	}

	public void setPicUrl(String picUrl)
	{
		this.picUrl = picUrl;
	}

	public int getPrice()
	{
		return price;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}

	public int getMarketPrice()
	{
		return marketPrice;
	}

	public void setMarketPrice(int marketPrice)
	{
		this.marketPrice = marketPrice;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Integer getValidateDate()
	{
		return validateDate;
	}

	public void setValidateDate(Integer validateDate)
	{
		this.validateDate = validateDate;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}
}
