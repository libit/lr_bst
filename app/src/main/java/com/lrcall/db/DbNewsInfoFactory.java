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
import com.lrcall.appbst.models.NewsInfo;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by libit on 16/7/8.
 */
public class DbNewsInfoFactory
{
	private static final String TABLE_NAME = DbConstant.TABLE_NAME_NEWS;
	private static DbNewsInfoFactory instance;

	synchronized public static DbNewsInfoFactory getInstance()
	{
		if (instance == null)
		{
			instance = new DbNewsInfoFactory();
		}
		return instance;
	}

	/**
	 * 增加或更新消息信息
	 *
	 * @param newsId         消息ID
	 * @param title          标题
	 * @param sortId         分类
	 * @param author         发布者
	 * @param descripition   简述
	 * @param content        内容
	 * @param updateDateLong 发布日期
	 * @return
	 */
	public boolean addOrUpdateNewsInfo(String newsId, String title, String sortId, String author, String descripition, String content, long updateDateLong)
	{
		if (StringTools.isNull(newsId) || StringTools.isNull(title) || StringTools.isNull(sortId))
		{
			return false;
		}
		NewsInfo newsInfo = new NewsInfo(newsId, title, sortId, author, descripition, content, updateDateLong);
		return addOrUpdateNewsInfo(newsInfo);
	}

	/**
	 * 增加或更新消息信息
	 *
	 * @param newsInfo
	 * @return
	 */
	public boolean addOrUpdateNewsInfo(NewsInfo newsInfo)
	{
		if (newsInfo == null || StringTools.isNull(newsInfo.getNewsId()) || StringTools.isNull(newsInfo.getTitle()) || StringTools.isNull(newsInfo.getSortId()))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		ContentValues values = newsInfo.getObjectContentValues();
		int row = contentResolver.update(tableUri, values, NewsInfo.FIELD_NEWS_ID + " = ?", new String[]{newsInfo.getNewsId()});
		if (row < 1)
		{
			return contentResolver.insert(tableUri, values) != null;
		}
		return true;
	}

	/**
	 * 更新消息状态
	 *
	 * @param newsId 消息ID
	 * @param status 状态
	 * @return
	 */
	public boolean updateNewsInfoStatus(String newsId, int status)
	{
		if (StringTools.isNull(newsId))
		{
			return false;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		ContentValues values = new ContentValues();
		values.put(NewsInfo.FIELD_IS_READ, status);
		int row = contentResolver.update(tableUri, values, NewsInfo.FIELD_NEWS_ID + " = ?", new String[]{newsId});
		return row > 0;
	}

	/**
	 * 删除消息信息
	 *
	 * @param newsId 消息ID
	 * @return
	 */
	public int deleteNewsInfo(String newsId)
	{
		if (StringTools.isNull(newsId))
		{
			return 0;
		}
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		return contentResolver.delete(tableUri, NewsInfo.FIELD_NEWS_ID + " = ?", new String[]{newsId});
	}

	/**
	 * 删除消息信息
	 *
	 * @return
	 */
	public int clearNewsInfo()
	{
		final ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
		final Uri tableUri = DbConstant.getTableUri(TABLE_NAME);
		return contentResolver.delete(tableUri, null, null);
	}

	/**
	 * 获取消息信息
	 *
	 * @param newsId 消息ID
	 * @return
	 */
	public NewsInfo getNewsInfo(String newsId)
	{
		if (StringTools.isNull(newsId))
		{
			return null;
		}
		NewsInfo newsInfo = null;
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, NewsInfo.FIELD_NEWS_ID + " = ?", new String[]{newsId}, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				newsInfo = NewsInfo.getObjectFromDb(cursor);
			}
			cursor.close();
		}
		return newsInfo;
	}

	/**
	 * 获取消息列表
	 *
	 * @return
	 */
	public List<NewsInfo> getNewsInfoList()
	{
		List<NewsInfo> list = new ArrayList<>();
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, null, null, NewsInfo.FIELD_DATE_LONG + " desc");
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				NewsInfo newsInfo = NewsInfo.getObjectFromDb(cursor);
				list.add(newsInfo);
			}
			cursor.close();
		}
		return list;
	}

	/**
	 * 获取消息列表
	 *
	 * @param isRead 是否已读
	 * @return
	 */
	public List<NewsInfo> getNewsInfoListByStatus(int isRead)
	{
		List<NewsInfo> list = new ArrayList<>();
		Cursor cursor = MyApplication.getContext().getContentResolver().query(DbConstant.getTableUri(TABLE_NAME), null, NewsInfo.FIELD_IS_READ + " = ?", new String[]{isRead + ""}, NewsInfo.FIELD_DATE_LONG + " desc");
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				NewsInfo newsInfo = NewsInfo.getObjectFromDb(cursor);
				list.add(newsInfo);
			}
			cursor.close();
		}
		return list;
	}
}
