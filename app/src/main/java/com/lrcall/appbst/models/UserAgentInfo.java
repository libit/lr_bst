/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class UserAgentInfo implements java.io.Serializable
{
	private static final long serialVersionUID = 3085822517009071551L;
	@SerializedName("userId")
	private String userId;
	@SerializedName("userType")
	private byte userType;
	@SerializedName("provinceId")
	private String provinceId;
	@SerializedName("cityId")
	private String cityId;
	@SerializedName("countryId")
	private String countryId;
	private ProvinceInfo provinceInfo;
	private CityInfo cityInfo;
	private CountryInfo countryInfo;

	public UserAgentInfo()
	{
	}

	public UserAgentInfo(String userId, byte userType, String provinceId, String cityId, String countryId)
	{
		this.userId = userId;
		this.userType = userType;
		this.provinceId = provinceId;
		this.cityId = cityId;
		this.countryId = countryId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public byte getUserType()
	{
		return userType;
	}

	public void setUserType(byte userType)
	{
		this.userType = userType;
	}

	public String getProvinceId()
	{
		return provinceId;
	}

	public void setProvinceId(String provinceId)
	{
		this.provinceId = provinceId;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getCountryId()
	{
		return countryId;
	}

	public void setCountryId(String countryId)
	{
		this.countryId = countryId;
	}

	public ProvinceInfo getProvinceInfo()
	{
		return provinceInfo;
	}

	public void setProvinceInfo(ProvinceInfo provinceInfo)
	{
		this.provinceInfo = provinceInfo;
	}

	public CityInfo getCityInfo()
	{
		return cityInfo;
	}

	public void setCityInfo(CityInfo cityInfo)
	{
		this.cityInfo = cityInfo;
	}

	public CountryInfo getCountryInfo()
	{
		return countryInfo;
	}

	public void setCountryInfo(CountryInfo countryInfo)
	{
		this.countryInfo = countryInfo;
	}
}
