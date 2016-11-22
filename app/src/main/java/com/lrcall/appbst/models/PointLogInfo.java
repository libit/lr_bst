/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

/**
 * Created by libit on 2016/11/22.
 */
public class PointLogInfo
{
	private String userId;
	private int amount;
	private String logType;
	private String content;
	private long addDateLong;

	public PointLogInfo()
	{
	}

	public PointLogInfo(String userId, int amount, String logType, String content, long addDateLong)
	{
		this.userId = userId;
		this.amount = amount;
		this.logType = logType;
		this.content = content;
		this.addDateLong = addDateLong;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public int getAmount()
	{
		return amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	public String getLogType()
	{
		return logType;
	}

	public void setLogType(String logType)
	{
		this.logType = logType;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public long getAddDateLong()
	{
		return addDateLong;
	}

	public void setAddDateLong(long addDateLong)
	{
		this.addDateLong = addDateLong;
	}
}
