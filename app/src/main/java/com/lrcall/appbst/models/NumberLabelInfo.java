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
 * Created by libit on 16/7/8.
 */
public class NumberLabelInfo extends DbObject
{
	public static final String FIELD_NUMBER = "number";
	public static final String FIELD_LABEL = "label";
	public static final String FIELD_COUNT = "count";
	@SerializedName("number")
	private String number;//电话号码
	@SerializedName("label")
	private String label;//标签
	@SerializedName("count")
	private int count;//被标记次数

	public NumberLabelInfo()
	{
	}

	public NumberLabelInfo(String number, String label, int count)
	{
		this.number = number;
		this.label = label;
		this.count = count;
	}

	/**
	 * 获取创建表的SQL语句
	 *
	 * @return SQL语句
	 */
	public static final String getCreateTableSQL()
	{
		// 归属地信息表
		return String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s INTEGER NOT NULL);", DbConstant.TABLE_NAME_NUMBER_LABEL, FIELD_ID, FIELD_NUMBER, FIELD_LABEL, FIELD_COUNT);
	}

	/**
	 * 从数据库中取出对象
	 *
	 * @param cursor
	 * @return
	 */
	public static NumberLabelInfo getObjectFromDb(Cursor cursor)
	{
		NumberLabelInfo numberLabelInfo = new NumberLabelInfo();
		numberLabelInfo.setNumber(cursor.getString(cursor.getColumnIndex(FIELD_NUMBER)));
		numberLabelInfo.setLabel(cursor.getString(cursor.getColumnIndex(FIELD_LABEL)));
		numberLabelInfo.setCount(cursor.getInt(cursor.getColumnIndex(FIELD_COUNT)));
		return numberLabelInfo;
	}

	/**
	 * 转换成数据库存储的数据
	 *
	 * @return ContentValues
	 */
	public ContentValues getObjectContentValues()
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(FIELD_NUMBER, number);
		contentValues.put(FIELD_LABEL, label);
		contentValues.put(FIELD_COUNT, count);
		return contentValues;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}
}
