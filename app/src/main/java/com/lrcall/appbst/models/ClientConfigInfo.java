/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class ClientConfigInfo
{
	@SerializedName("kefuNumber")
	private String kefuNumber;// 客服电话
	@SerializedName("officalWeb")
	private String officalWeb;// 官方网站

	public ClientConfigInfo()
	{
		super();
	}

	public ClientConfigInfo(String kefuNumber, String officalWeb)
	{
		super();
		this.kefuNumber = kefuNumber;
		this.officalWeb = officalWeb;
	}

	public String getKefuNumber()
	{
		return kefuNumber;
	}

	public void setKefuNumber(String kefuNumber)
	{
		this.kefuNumber = kefuNumber;
	}

	public String getOfficalWeb()
	{
		return officalWeb;
	}

	public void setOfficalWeb(String officalWeb)
	{
		this.officalWeb = officalWeb;
	}
}
