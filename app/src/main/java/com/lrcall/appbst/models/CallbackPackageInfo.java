//
// CallbackPackageInfo.h
//
// 作者：libit 创建于 2017年2月25日.
// Copyright © 2017年 LR. All rights reserved.
//
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class CallbackPackageInfo
{
	@SerializedName("packageId")
	private String packageId;
	@SerializedName("name")
	private String name;
	@SerializedName("money")
	private int money;
	@SerializedName("payType")
	private String payType;
	@SerializedName("maxMinute")
	private int maxMinute;
	@SerializedName("userLevel")
	private byte userLevel;

	public String getPackageId()
	{
		return packageId;
	}

	public void setPackageId(String packageId)
	{
		this.packageId = packageId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getMoney()
	{
		return money;
	}

	public void setMoney(int money)
	{
		this.money = money;
	}

	public String getPayType()
	{
		return payType;
	}

	public void setPayType(String payType)
	{
		this.payType = payType;
	}

	public int getMaxMinute()
	{
		return maxMinute;
	}

	public void setMaxMinute(int maxMinute)
	{
		this.maxMinute = maxMinute;
	}

	public byte getUserLevel()
	{
		return userLevel;
	}

	public void setUserLevel(byte userLevel)
	{
		this.userLevel = userLevel;
	}
}