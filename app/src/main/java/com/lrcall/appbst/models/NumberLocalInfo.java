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
 * Created by libit on 15/8/19.
 */
public class NumberLocalInfo extends DbObject implements Comparator<NumberLocalInfo>
{
	public static final String FIELD_NUMBER = "number";
	public static final String FIELD_LOCAL_INFO = "local_info";
	@SerializedName("id")
	private String id;// 主键
	@SerializedName("number")
	private String number;//电话号码
	@SerializedName("localInfo")
	private String localInfo;//归属地信息

	public NumberLocalInfo()
	{
	}

	public NumberLocalInfo(String number, String localInfo)
	{
		this.number = number;
		this.localInfo = localInfo;
	}

	/**
	 * 获取创建表的SQL语句
	 *
	 * @return SQL语句
	 */
	public static final String getCreateTableSQL()
	{
		// 归属地信息表
		return String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL);", DbConstant.TABLE_NAME_LOCAL_INFO, FIELD_ID, FIELD_NUMBER, FIELD_LOCAL_INFO);
	}

	/**
	 * 从数据库中取出对象
	 *
	 * @param cursor
	 * @return
	 */
	public static NumberLocalInfo getObjectFromDb(Cursor cursor)
	{
		NumberLocalInfo numberLocalInfo = new NumberLocalInfo();
		numberLocalInfo.setId(cursor.getString(cursor.getColumnIndex(FIELD_ID)));
		numberLocalInfo.setNumber(cursor.getString(cursor.getColumnIndex(FIELD_NUMBER)));
		numberLocalInfo.setLocalInfo(cursor.getString(cursor.getColumnIndex(FIELD_LOCAL_INFO)));
		return numberLocalInfo;
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
		contentValues.put(FIELD_NUMBER, number);
		contentValues.put(FIELD_LOCAL_INFO, localInfo);
		return contentValues;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getLocalInfo()
	{
		return localInfo;
	}

	public void setLocalInfo(String localInfo)
	{
		this.localInfo = localInfo;
	}

	@Override
	public String toString()
	{
		return String.format("[id:%s],[number:%s],[localInfo:%s].", id, number, localInfo);
	}

	@Override
	public int compare(NumberLocalInfo lhs, NumberLocalInfo rhs)
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
		String lName = lhs.getNumber();
		String rName = rhs.getNumber();
		return lName.compareTo(rName);
	}
}
