//
// UserCallbackInfo.h
//
// 作者：libit 创建于 2017年2月25日.
// Copyright © 2017年 LR. All rights reserved.
//
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class UserCallbackInfo
{
	@SerializedName("userId")
	private String userId;
	@SerializedName("agentId")
	private String agentId;
	@SerializedName("balance")
	private int balance;
	@SerializedName("packageId")
	private String packageId;
	@SerializedName("startDateLong")
	private long startDateLong;
	@SerializedName("validateDateLong")
	private long validateDateLong;
	@SerializedName("useCount")
	private int useCount;
	@SerializedName("unuseCount")
	private int unuseCount;
	@SerializedName("isVip")
	private byte isVip;
	@SerializedName("vipValidateDateLong")
	private Long vipValidateDateLong;
	@SerializedName("lastPackageId")
	private String lastPackageId;
	@SerializedName("lastPackageEndDateLong")
	private Long lastPackageEndDateLong;
	@SerializedName("registerDateLong")
	private Long registerDateLong;
	@SerializedName("useMinute")
	private int useMinute;
	@SerializedName("status")
	private byte status;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getAgentId()
	{
		return agentId;
	}

	public void setAgentId(String agentId)
	{
		this.agentId = agentId;
	}

	public int getBalance()
	{
		return balance;
	}

	public void setBalance(int balance)
	{
		this.balance = balance;
	}

	public String getPackageId()
	{
		return packageId;
	}

	public void setPackageId(String packageId)
	{
		this.packageId = packageId;
	}

	public long getStartDateLong()
	{
		return startDateLong;
	}

	public void setStartDateLong(long startDateLong)
	{
		this.startDateLong = startDateLong;
	}

	public long getValidateDateLong()
	{
		return validateDateLong;
	}

	public void setValidateDateLong(long validateDateLong)
	{
		this.validateDateLong = validateDateLong;
	}

	public int getUseCount()
	{
		return useCount;
	}

	public void setUseCount(int useCount)
	{
		this.useCount = useCount;
	}

	public int getUnuseCount()
	{
		return unuseCount;
	}

	public void setUnuseCount(int unuseCount)
	{
		this.unuseCount = unuseCount;
	}

	public byte getIsVip()
	{
		return isVip;
	}

	public void setIsVip(byte isVip)
	{
		this.isVip = isVip;
	}

	public Long getVipValidateDateLong()
	{
		return vipValidateDateLong;
	}

	public void setVipValidateDateLong(Long vipValidateDateLong)
	{
		this.vipValidateDateLong = vipValidateDateLong;
	}

	public String getLastPackageId()
	{
		return lastPackageId;
	}

	public void setLastPackageId(String lastPackageId)
	{
		this.lastPackageId = lastPackageId;
	}

	public Long getLastPackageEndDateLong()
	{
		return lastPackageEndDateLong;
	}

	public void setLastPackageEndDateLong(Long lastPackageEndDateLong)
	{
		this.lastPackageEndDateLong = lastPackageEndDateLong;
	}

	public Long getRegisterDateLong()
	{
		return registerDateLong;
	}

	public void setRegisterDateLong(Long registerDateLong)
	{
		this.registerDateLong = registerDateLong;
	}

	public int getUseMinute()
	{
		return useMinute;
	}

	public void setUseMinute(int useMinute)
	{
		this.useMinute = useMinute;
	}

	public byte getStatus()
	{
		return status;
	}

	public void setStatus(byte status)
	{
		this.status = status;
	}
}