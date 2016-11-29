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
 * Created by libit on 16/7/14.
 */
public class BannerInfo extends DbObject
{
	public static final String FIELD_BANNER_ID = "banner_id";
	public static final String FIELD_BANNER_TYPE = "banner_type";
	public static final String FIELD_PIC_URL = "pic_url";
	public static final String FIELD_CLICK_TYPE = "click_type";
	public static final String FIELD_CONTENT = "content";
	public static final String FIELD_DATE = "date";
	@SerializedName("bannerId")
	private String bannerId;//ID
	@SerializedName("bannerType")
	private String bannerType;//客户端位置
	@SerializedName("picId")
	private String picUrl;//图片地址
	@SerializedName("clickType")
	private String clickType;//点击事件
	@SerializedName("content")
	private String content;//事件内容
	@SerializedName("updateDateLong")
	private long date;//更新时间

	public BannerInfo()
	{
	}

	public BannerInfo(String bannerId, String bannerType, String picUrl, String clickType, String content, long date)
	{
		this.bannerId = bannerId;
		this.bannerType = bannerType;
		this.picUrl = picUrl;
		this.clickType = clickType;
		this.content = content;
		this.date = date;
	}

	/**
	 * 获取创建表的SQL语句
	 *
	 * @return SQL语句
	 */
	public static final String getCreateTableSQL()
	{
		return String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL,%s INT DEFAULT 0);", DbConstant.TABLE_NAME_BANNER, FIELD_ID, FIELD_BANNER_ID, FIELD_BANNER_TYPE, FIELD_PIC_URL, FIELD_CLICK_TYPE, FIELD_CONTENT, FIELD_DATE);
	}

	/**
	 * 从数据库中取出对象
	 *
	 * @param cursor
	 * @return
	 */
	public static BannerInfo getObjectFromDb(Cursor cursor)
	{
		BannerInfo bannerInfo = new BannerInfo();
		bannerInfo.setBannerId(cursor.getString(cursor.getColumnIndex(FIELD_BANNER_ID)));
		bannerInfo.setBannerType(cursor.getString(cursor.getColumnIndex(FIELD_BANNER_TYPE)));
		bannerInfo.setPicUrl(cursor.getString(cursor.getColumnIndex(FIELD_PIC_URL)));
		bannerInfo.setClickType(cursor.getString(cursor.getColumnIndex(FIELD_CLICK_TYPE)));
		bannerInfo.setContent(cursor.getString(cursor.getColumnIndex(FIELD_CONTENT)));
		bannerInfo.setDate(cursor.getLong(cursor.getColumnIndex(FIELD_DATE)));
		return bannerInfo;
	}

	/**
	 * 转换成数据库存储的数据
	 *
	 * @return ContentValues
	 */
	public ContentValues getObjectContentValues()
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(FIELD_BANNER_ID, bannerId);
		contentValues.put(FIELD_BANNER_TYPE, bannerType);
		contentValues.put(FIELD_PIC_URL, picUrl);
		contentValues.put(FIELD_CLICK_TYPE, clickType);
		contentValues.put(FIELD_CONTENT, content);
		contentValues.put(FIELD_DATE, date);
		return contentValues;
	}

	public String getBannerId()
	{
		return bannerId;
	}

	public void setBannerId(String bannerId)
	{
		this.bannerId = bannerId;
	}

	public String getBannerType()
	{
		return bannerType;
	}

	public void setBannerType(String bannerType)
	{
		this.bannerType = bannerType;
	}

	public String getPicUrl()
	{
		return picUrl;
	}

	public void setPicUrl(String picUrl)
	{
		this.picUrl = picUrl;
	}

	public String getClickType()
	{
		return clickType;
	}

	public void setClickType(String clickType)
	{
		this.clickType = clickType;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
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
