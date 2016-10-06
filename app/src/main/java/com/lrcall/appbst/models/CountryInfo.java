/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class CountryInfo implements java.io.Serializable
{
	private static final long serialVersionUID = 854079275306979668L;
	@SerializedName("countryId")
	private String countryId;
	@SerializedName("name")
	private String name;
	@SerializedName("provinceId")
	private String provinceId;
	@SerializedName("cityId")
	private String cityId;

	public CountryInfo()
	{
	}

	public CountryInfo(String countryId, String name)
	{
		this.countryId = countryId;
		this.name = name;
	}

	public CountryInfo(String countryId, String name, String provinceId, String cityId)
	{
		this.countryId = countryId;
		this.name = name;
		this.provinceId = provinceId;
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

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
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
}
