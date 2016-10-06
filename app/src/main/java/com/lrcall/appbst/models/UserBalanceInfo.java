/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by libit on 16/8/23.
 */
public class UserBalanceInfo
{
	@SerializedName("userId")
	private String userId;
	@SerializedName("balance")
	private int balance;
	@SerializedName("point")
	private int point;
	@SerializedName("freezeBalance")
	private int freezeBalance;
	@SerializedName("freezePoint")
	private int freezePoint;
	@SerializedName("dataTrafficType")
	private String dataTrafficType;
	@SerializedName("dataTrafficValidateLong")
	private long dataTrafficValidateLong;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public int getBalance()
	{
		return balance;
	}

	public void setBalance(int balance)
	{
		this.balance = balance;
	}

	public int getPoint()
	{
		return point;
	}

	public void setPoint(int point)
	{
		this.point = point;
	}

	public int getFreezeBalance()
	{
		return freezeBalance;
	}

	public void setFreezeBalance(int freezeBalance)
	{
		this.freezeBalance = freezeBalance;
	}

	public int getFreezePoint()
	{
		return freezePoint;
	}

	public void setFreezePoint(int freezePoint)
	{
		this.freezePoint = freezePoint;
	}

	public String getDataTrafficType()
	{
		return dataTrafficType;
	}

	public void setDataTrafficType(String dataTrafficType)
	{
		this.dataTrafficType = dataTrafficType;
	}

	public long getDataTrafficValidateLong()
	{
		return dataTrafficValidateLong;
	}

	public void setDataTrafficValidateLong(long dataTrafficValidateLong)
	{
		this.dataTrafficValidateLong = dataTrafficValidateLong;
	}
}
