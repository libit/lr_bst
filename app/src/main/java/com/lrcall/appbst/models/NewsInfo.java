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
public class NewsInfo extends DbObject implements Comparator<NewsInfo>
{
	public static final String FIELD_NEWS_ID = "news_id";
	public static final String FIELD_TITLE = "title";
	public static final String FIELD_SORT_ID = "sort_id";
	public static final String FIELD_AUTHOR = "author";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_CONTENT = "content";
	public static final String FIELD_DATE_LONG = "date";
	public static final String FIELD_VALIDATE_DATE_LONG = "valide_date";
	public static final String FIELD_IS_READ = "read";
	@SerializedName("newsId")
	private String newsId;//消息ID
	@SerializedName("title")
	private String title;
	@SerializedName("sortId")
	private String sortId;//分类ID--存储分类名
	@SerializedName("author")
	private String author;
	@SerializedName("descripition")
	private String descripition;
	@SerializedName("content")
	private String content;
	@SerializedName("updateDateLong")
	private long updateDateLong;
	@SerializedName("valideDateLong")
	private Long valideDateLong;
	@SerializedName("isRead")
	private int isRead;

	public NewsInfo()
	{
	}

	public NewsInfo(String newsId, String title, String sortId, String author, String descripition, String content, long updateDateLong)
	{
		this.newsId = newsId;
		this.title = title;
		this.sortId = sortId;
		this.author = author;
		this.descripition = descripition;
		this.content = content;
		this.updateDateLong = updateDateLong;
	}

	/**
	 * 获取创建表的SQL语句
	 *
	 * @return SQL语句
	 */
	public static final String getCreateTableSQL()
	{
		// 归属地信息表
		return String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT,%s TEXT NOT NULL,%s TEXT,%s BIGINT NOT NULL,%s BIGINT,%s INT NOT NULL DEFAULT 0);", DbConstant.TABLE_NAME_NEWS, FIELD_ID, FIELD_NEWS_ID, FIELD_TITLE, FIELD_SORT_ID, FIELD_AUTHOR, FIELD_DESCRIPTION, FIELD_CONTENT, FIELD_DATE_LONG, FIELD_VALIDATE_DATE_LONG, FIELD_IS_READ);
	}

	/**
	 * 从数据库中取出对象
	 *
	 * @param cursor
	 * @return
	 */
	public static NewsInfo getObjectFromDb(Cursor cursor)
	{
		NewsInfo newsInfo = new NewsInfo();
		newsInfo.setNewsId(cursor.getString(cursor.getColumnIndex(FIELD_NEWS_ID)));
		newsInfo.setTitle(cursor.getString(cursor.getColumnIndex(FIELD_TITLE)));
		newsInfo.setSortId(cursor.getString(cursor.getColumnIndex(FIELD_SORT_ID)));
		newsInfo.setAuthor(cursor.getString(cursor.getColumnIndex(FIELD_AUTHOR)));
		newsInfo.setDescripition(cursor.getString(cursor.getColumnIndex(FIELD_DESCRIPTION)));
		newsInfo.setContent(cursor.getString(cursor.getColumnIndex(FIELD_CONTENT)));
		newsInfo.setUpdateDateLong(cursor.getLong(cursor.getColumnIndex(FIELD_DATE_LONG)));
		newsInfo.setValideDateLong(cursor.getLong(cursor.getColumnIndex(FIELD_VALIDATE_DATE_LONG)));
		newsInfo.setIsRead(cursor.getInt(cursor.getColumnIndex(FIELD_IS_READ)));
		return newsInfo;
	}

	/**
	 * 转换成数据库存储的数据
	 *
	 * @return ContentValues
	 */
	public ContentValues getObjectContentValues()
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(FIELD_NEWS_ID, newsId);
		contentValues.put(FIELD_TITLE, title);
		contentValues.put(FIELD_SORT_ID, sortId);
		contentValues.put(FIELD_AUTHOR, author);
		contentValues.put(FIELD_DESCRIPTION, descripition);
		contentValues.put(FIELD_CONTENT, content);
		contentValues.put(FIELD_DATE_LONG, updateDateLong);
		contentValues.put(FIELD_VALIDATE_DATE_LONG, valideDateLong);
		//		contentValues.put(FIELD_IS_READ, isRead);
		return contentValues;
	}

	public String getNewsId()
	{
		return newsId;
	}

	public void setNewsId(String newsId)
	{
		this.newsId = newsId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getSortId()
	{
		return sortId;
	}

	public void setSortId(String sortId)
	{
		this.sortId = sortId;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getDescripition()
	{
		return descripition;
	}

	public void setDescripition(String descripition)
	{
		this.descripition = descripition;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public long getUpdateDateLong()
	{
		return updateDateLong;
	}

	public void setUpdateDateLong(long updateDateLong)
	{
		this.updateDateLong = updateDateLong;
	}

	public Long getValideDateLong()
	{
		return valideDateLong;
	}

	public void setValideDateLong(Long valideDateLong)
	{
		this.valideDateLong = valideDateLong;
	}

	public int getIsRead()
	{
		return isRead;
	}

	public void setIsRead(int isRead)
	{
		this.isRead = isRead;
	}

	@Override
	public int compare(NewsInfo lhs, NewsInfo rhs)
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
		long lDate = lhs.getUpdateDateLong();
		long rDate = rhs.getUpdateDateLong();
		if (lDate == rDate)
		{
			return 0;
		}
		return lDate < rDate ? -1 : 1;
	}
}
