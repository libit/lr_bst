/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by libit on 16/8/26.
 */
public class PayInfo
{
	@SerializedName("payId")
	private String payId;
	@SerializedName("name")
	private String name;
	@SerializedName("picId")
	private String picUrl;

	public PayInfo()
	{
	}

	public PayInfo(String payId, String name, String picUrl)
	{
		this.payId = payId;
		this.name = name;
		this.picUrl = picUrl;
	}

	public String getPayId()
	{
		return payId;
	}

	public void setPayId(String payId)
	{
		this.payId = payId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPicUrl()
	{
		return picUrl;
	}

	public void setPicUrl(String picUrl)
	{
		this.picUrl = picUrl;
	}
}
