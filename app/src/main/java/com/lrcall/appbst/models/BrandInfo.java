/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class BrandInfo implements java.io.Serializable
{
	private static final long serialVersionUID = -5154368931522681427L;
	@SerializedName("brandId")
	private String brandId;
	@SerializedName("name")
	private String name;
	@SerializedName("picId")
	private String picUrl;
	@SerializedName("description")
	private String description;
	@SerializedName("content")
	private String content;

	public BrandInfo()
	{
	}

	public BrandInfo(String brandId, String name, String picUrl, String description, String content)
	{
		this.brandId = brandId;
		this.name = name;
		this.picUrl = picUrl;
		this.description = description;
		this.content = content;
	}

	public String getBrandId()
	{
		return brandId;
	}

	public void setBrandId(String brandId)
	{
		this.brandId = brandId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPicUrl()
	{
		return picUrl;
	}

	public void setPicUrl(String picUrl)
	{
		this.picUrl = picUrl;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
}
