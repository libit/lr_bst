/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

public class WxPayInfo
{
	private String appId;
	private String partnerId;
	private String prepayId;
	private String nonceStr;
	private String timeStamp;
	private String packageValue;
	private String sign;

	public WxPayInfo()
	{
		super();
	}

	public WxPayInfo(String appId, String partnerId, String prepayId, String nonceStr, String timeStamp, String packageValue, String sign)
	{
		this.appId = appId;
		this.partnerId = partnerId;
		this.prepayId = prepayId;
		this.nonceStr = nonceStr;
		this.timeStamp = timeStamp;
		this.packageValue = packageValue;
		this.sign = sign;
	}

	public String getAppId()
	{
		return appId;
	}

	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	public String getPartnerId()
	{
		return partnerId;
	}

	public void setPartnerId(String partnerId)
	{
		this.partnerId = partnerId;
	}

	public String getPrepayId()
	{
		return prepayId;
	}

	public void setPrepayId(String prepayId)
	{
		this.prepayId = prepayId;
	}

	public String getNonceStr()
	{
		return nonceStr;
	}

	public void setNonceStr(String nonceStr)
	{
		this.nonceStr = nonceStr;
	}

	public String getTimeStamp()
	{
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	public String getPackageValue()
	{
		return packageValue;
	}

	public void setPackageValue(String packageValue)
	{
		this.packageValue = packageValue;
	}

	public String getSign()
	{
		return sign;
	}

	public void setSign(String sign)
	{
		this.sign = sign;
	}
}
