/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

public class CallbackRechargeLogInfo
{
	private String userId;
	private String rechargeType;
	private String platform;
	private Integer versionCode;
	private int amount;
	private long rechargeDateLong;
	private byte status;
	private String remark;

	public CallbackRechargeLogInfo()
	{
	}

	public String getUserId()
	{
		return this.userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getRechargeType()
	{
		return this.rechargeType;
	}

	public void setRechargeType(String rechargeType)
	{
		this.rechargeType = rechargeType;
	}

	public String getPlatform()
	{
		return this.platform;
	}

	public void setPlatform(String platform)
	{
		this.platform = platform;
	}

	public Integer getVersionCode()
	{
		return this.versionCode;
	}

	public void setVersionCode(Integer versionCode)
	{
		this.versionCode = versionCode;
	}

	public int getAmount()
	{
		return this.amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	public long getRechargeDateLong()
	{
		return this.rechargeDateLong;
	}

	public void setRechargeDateLong(long rechargeDateLong)
	{
		this.rechargeDateLong = rechargeDateLong;
	}

	public byte getStatus()
	{
		return this.status;
	}

	public void setStatus(byte status)
	{
		this.status = status;
	}

	public String getRemark()
	{
		return this.remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}
}
