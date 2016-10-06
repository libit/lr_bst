/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class ProvinceInfo implements java.io.Serializable
{
	private static final long serialVersionUID = 5946518273085537774L;
	@SerializedName("provinceId")
	private String provinceId;
	@SerializedName("name")
	private String name;

	public ProvinceInfo()
	{
	}

	public ProvinceInfo(String provinceId, String name)
	{
		this.provinceId = provinceId;
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

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
