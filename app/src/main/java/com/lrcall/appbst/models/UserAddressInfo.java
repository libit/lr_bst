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
 * Created by libit on 16/7/25.
 */
public class UserAddressInfo extends DbObject
{
	public static final String FIELD_ADDRESS_ID = "address_id";
	public static final String FIELD_USER_ID = "user_id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_NUMBER = "number";
	public static final String FIELD_COUNTRY = "country";
	public static final String FIELD_PROVINCE = "province";
	public static final String FIELD_CITY = "city";
	public static final String FIELD_DISTRICT = "district";
	public static final String FIELD_ADDRESS = "address";
	public static final String FIELD_STATUS = "status";
	public static final String FIELD_DATE = "date";
	@SerializedName("addressId")
	private String addressId;
	@SerializedName("userId")
	private String userId;
	@SerializedName("name")
	private String name;
	@SerializedName("number")
	private String number;
	@SerializedName("country")
	private String country;
	@SerializedName("province")
	private String province;
	@SerializedName("city")
	private String city;
	@SerializedName("district")
	private String district;
	@SerializedName("address")
	private String address;
	@SerializedName("status")
	private int status;
	@SerializedName("updateDateLong")
	private long updateDateLong;

	/**
	 * 获取创建表的SQL语句
	 *
	 * @return SQL语句
	 */
	public static final String getCreateTableSQL()
	{
		return String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT NOT NULL,%s INT DEFAULT 0,%s INT);", DbConstant.TABLE_NAME_ADDRESS, FIELD_ID, FIELD_ADDRESS_ID, FIELD_USER_ID, FIELD_NAME, FIELD_NUMBER, FIELD_COUNTRY, FIELD_PROVINCE, FIELD_CITY, FIELD_DISTRICT, FIELD_ADDRESS, FIELD_STATUS, FIELD_DATE);
	}

	/**
	 * 从数据库中取出对象
	 *
	 * @param cursor
	 * @return
	 */
	public static UserAddressInfo getObjectFromDb(Cursor cursor)
	{
		UserAddressInfo userAddressInfo = new UserAddressInfo();
		userAddressInfo.setAddressId(cursor.getString(cursor.getColumnIndex(FIELD_ADDRESS_ID)));
		userAddressInfo.setUserId(cursor.getString(cursor.getColumnIndex(FIELD_USER_ID)));
		userAddressInfo.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
		userAddressInfo.setNumber(cursor.getString(cursor.getColumnIndex(FIELD_NUMBER)));
		userAddressInfo.setCountry(cursor.getString(cursor.getColumnIndex(FIELD_COUNTRY)));
		userAddressInfo.setProvince(cursor.getString(cursor.getColumnIndex(FIELD_PROVINCE)));
		userAddressInfo.setCity(cursor.getString(cursor.getColumnIndex(FIELD_CITY)));
		userAddressInfo.setDistrict(cursor.getString(cursor.getColumnIndex(FIELD_DISTRICT)));
		userAddressInfo.setAddress(cursor.getString(cursor.getColumnIndex(FIELD_ADDRESS)));
		userAddressInfo.setStatus(cursor.getInt(cursor.getColumnIndex(FIELD_STATUS)));
		userAddressInfo.setUpdateDateLong(cursor.getLong(cursor.getColumnIndex(FIELD_DATE)));
		return userAddressInfo;
	}

	/**
	 * 转换成数据库存储的数据
	 *
	 * @return ContentValues
	 */
	public ContentValues getObjectContentValues()
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(FIELD_ADDRESS_ID, addressId);
		contentValues.put(FIELD_USER_ID, userId);
		contentValues.put(FIELD_NAME, name);
		contentValues.put(FIELD_NUMBER, number);
		contentValues.put(FIELD_COUNTRY, country);
		contentValues.put(FIELD_PROVINCE, province);
		contentValues.put(FIELD_CITY, city);
		contentValues.put(FIELD_DISTRICT, district);
		contentValues.put(FIELD_ADDRESS, address);
		contentValues.put(FIELD_STATUS, status);
		contentValues.put(FIELD_DATE, updateDateLong);
		return contentValues;
	}

	public UserAddressInfo()
	{
	}

	public UserAddressInfo(String addressId, String userId, String name, String number, String country, String province, String city, String district, String address, byte status, long updateDateLong)
	{
		this.addressId = addressId;
		this.userId = userId;
		this.name = name;
		this.number = number;
		this.country = country;
		this.province = province;
		this.city = city;
		this.district = district;
		this.address = address;
		this.status = status;
		this.updateDateLong = updateDateLong;
	}

	public String getAddressId()
	{
		return addressId;
	}

	public void setAddressId(String addressId)
	{
		this.addressId = addressId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getProvince()
	{
		return province;
	}

	public void setProvince(String province)
	{
		this.province = province;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getDistrict()
	{
		return district;
	}

	public void setDistrict(String district)
	{
		this.district = district;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public long getUpdateDateLong()
	{
		return updateDateLong;
	}

	public void setUpdateDateLong(long updateDateLong)
	{
		this.updateDateLong = updateDateLong;
	}
}
