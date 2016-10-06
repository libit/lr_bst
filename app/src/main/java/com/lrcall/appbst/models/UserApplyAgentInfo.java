/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class UserApplyAgentInfo
{
	@SerializedName("userId")
	private String userId;
	@SerializedName("applyUserType")
	private byte applyUserType;
	@SerializedName("provinceId")
	private String provinceId;
	@SerializedName("cityId")
	private String cityId;
	@SerializedName("countryId")
	private String countryId;
	@SerializedName("status")
	private byte status;
	@SerializedName("addDateLong")
	private long addDateLong;
	@SerializedName("updateDateLong")
	private long updateDateLong;

	public UserApplyAgentInfo()
	{
	}

	public UserApplyAgentInfo(String userId, byte applyUserType, byte status, long addDateLong, long updateDateLong)
	{
		this.userId = userId;
		this.applyUserType = applyUserType;
		this.status = status;
		this.addDateLong = addDateLong;
		this.updateDateLong = updateDateLong;
	}

	public UserApplyAgentInfo(String userId, byte applyUserType, String provinceId, String cityId, String countryId, byte status, long addDateLong, long updateDateLong)
	{
		this.userId = userId;
		this.applyUserType = applyUserType;
		this.provinceId = provinceId;
		this.cityId = cityId;
		this.countryId = countryId;
		this.status = status;
		this.addDateLong = addDateLong;
		this.updateDateLong = updateDateLong;
	}

	public String getUserId()
	{
		return this.userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public byte getApplyUserType()
	{
		return this.applyUserType;
	}

	public void setApplyUserType(byte applyUserType)
	{
		this.applyUserType = applyUserType;
	}

	public String getProvinceId()
	{
		return this.provinceId;
	}

	public void setProvinceId(String provinceId)
	{
		this.provinceId = provinceId;
	}

	public String getCityId()
	{
		return this.cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getCountryId()
	{
		return this.countryId;
	}

	public void setCountryId(String countryId)
	{
		this.countryId = countryId;
	}

	public byte getStatus()
	{
		return this.status;
	}

	public void setStatus(byte status)
	{
		this.status = status;
	}

	public long getAddDateLong()
	{
		return this.addDateLong;
	}

	public void setAddDateLong(long addDateLong)
	{
		this.addDateLong = addDateLong;
	}

	public long getUpdateDateLong()
	{
		return this.updateDateLong;
	}

	public void setUpdateDateLong(long updateDateLong)
	{
		this.updateDateLong = updateDateLong;
	}
}
