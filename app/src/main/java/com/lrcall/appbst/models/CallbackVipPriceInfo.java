//
// CallbackVipPriceInfo.h
//
// 作者：libit 创建于 2017年2月21日.
// Copyright © 2017年 LR. All rights reserved.
//
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class CallbackVipPriceInfo
{
	@SerializedName("priceId")
	private String priceId;
	@SerializedName("name")
	private String name;
	@SerializedName("day")
	private int day;
	@SerializedName("price")
	private int price;
	@SerializedName("remark")
	private String remark;

	public String getPriceId()
	{
		return priceId;
	}

	public void setPriceId(String priceId)
	{
		this.priceId = priceId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getDay()
	{
		return day;
	}

	public void setDay(int day)
	{
		this.day = day;
	}

	public int getPrice()
	{
		return price;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}
}