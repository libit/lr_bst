/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class CityInfo implements java.io.Serializable
{
	private static final long serialVersionUID = -2866743769218290002L;
	@SerializedName("cityId")
	private String cityId;
	@SerializedName("name")
	private String name;
	@SerializedName("provinceId")
	private String provinceId;

	public CityInfo()
	{
	}

	public CityInfo(String cityId, String name)
	{
		this.cityId = cityId;
		this.name = name;
	}

	public CityInfo(String cityId, String name, String provinceId)
	{
		this.cityId = cityId;
		this.name = name;
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
}
