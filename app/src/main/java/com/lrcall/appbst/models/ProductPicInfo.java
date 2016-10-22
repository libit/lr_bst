/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by libit on 16/7/18.
 */
public class ProductPicInfo
{
	@SerializedName("productId")
	private String productId;
	@SerializedName("picId")
	private String picUrl;
	@SerializedName("picType")
	private String picType;
	@SerializedName("description")
	private String description;

	public ProductPicInfo()
	{
	}

	public ProductPicInfo(String productId, String picUrl, String picType, String description)
	{
		this.productId = productId;
		this.picUrl = picUrl;
		this.picType = picType;
		this.description = description;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public String getPicUrl()
	{
		return picUrl;
	}

	public void setPicUrl(String picUrl)
	{
		this.picUrl = picUrl;
	}

	public String getPicType()
	{
		return picType;
	}

	public void setPicType(String picType)
	{
		this.picType = picType;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
