/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

public class CallbackCallLogInfo
{
	private String userId;
	private String number;
	private String platform;
	private Integer versionCode;
	private int duration;
	private long callDateLong;
	private byte status;
	private String remark;

	public CallbackCallLogInfo()
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

	public String getNumber()
	{
		return this.number;
	}

	public void setNumber(String number)
	{
		this.number = number;
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

	public int getDuration()
	{
		return this.duration;
	}

	public void setDuration(int duration)
	{
		this.duration = duration;
	}

	public long getCallDateLong()
	{
		return this.callDateLong;
	}

	public void setCallDateLong(long callDateLong)
	{
		this.callDateLong = callDateLong;
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
