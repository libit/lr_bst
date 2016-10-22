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

import static android.R.attr.id;

/**
 * Created by libit on 15/8/19.
 */
public class WhiteNumberInfo extends DbObject implements Comparator<WhiteNumberInfo>
{
	public static final String FIELD_NUMBER = "number";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_REMARK = "remark";
	@SerializedName("number")
	private String number;//电话号码
	@SerializedName("name")
	private String name;//名字
	@SerializedName("remark")
	private String remark;//备注

	public WhiteNumberInfo()
	{
	}

	public WhiteNumberInfo(String number, String name, String remark)
	{
		this.number = number;
		this.name = name;
		this.remark = remark;
	}

	/**
	 * 获取创建表的SQL语句
	 *
	 * @return SQL语句
	 */
	public static final String getCreateTableSQL()
	{
		// 归属地信息表
		return String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s TEXT);", DbConstant.TABLE_NAME_WHITE_NUMBER, FIELD_ID, FIELD_NUMBER, FIELD_NAME, FIELD_REMARK);
	}

	/**
	 * 从数据库中取出对象
	 *
	 * @param cursor
	 * @return
	 */
	public static WhiteNumberInfo getObjectFromDb(Cursor cursor)
	{
		WhiteNumberInfo whiteNumberInfo = new WhiteNumberInfo();
		whiteNumberInfo.setNumber(cursor.getString(cursor.getColumnIndex(FIELD_NUMBER)));
		whiteNumberInfo.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
		whiteNumberInfo.setRemark(cursor.getString(cursor.getColumnIndex(FIELD_REMARK)));
		return whiteNumberInfo;
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
		contentValues.put(FIELD_NAME, name);
		contentValues.put(FIELD_REMARK, remark);
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	@Override
	public String toString()
	{
		return String.format("[id:%s],[number:%s],[name:%s],[remakr:%s].", id, number, name, remark);
	}

	@Override
	public int compare(WhiteNumberInfo lhs, WhiteNumberInfo rhs)
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
