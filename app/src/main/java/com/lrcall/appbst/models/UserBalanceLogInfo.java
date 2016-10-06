/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class UserBalanceLogInfo implements java.io.Serializable
{
	private static final long serialVersionUID = 7645904399398228626L;
	@SerializedName("userId")
	private String userId;
	@SerializedName("amount")
	private int amount;
	@SerializedName("logType")
	private String logType;
	@SerializedName("content")
	private String content;
	@SerializedName("addDateLong")
	private long addDateLong;

	public UserBalanceLogInfo()
	{
	}

	public UserBalanceLogInfo(String userId, int amount, String logType, long addDateLong)
	{
		this.userId = userId;
		this.amount = amount;
		this.logType = logType;
		this.addDateLong = addDateLong;
	}

	public UserBalanceLogInfo(String userId, int amount, String logType, String content, long addDateLong)
	{
		this.userId = userId;
		this.amount = amount;
		this.logType = logType;
		this.content = content;
		this.addDateLong = addDateLong;
	}

	public String getUserId()
	{
		return this.userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public int getAmount()
	{
		return this.amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	public String getLogType()
	{
		return this.logType;
	}

	public void setLogType(String logType)
	{
		this.logType = logType;
	}

	public String getContent()
	{
		return this.content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public long getAddDateLong()
	{
		return this.addDateLong;
	}

	public void setAddDateLong(long addDateLong)
	{
		this.addDateLong = addDateLong;
	}
}
