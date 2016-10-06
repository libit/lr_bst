/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class ProductHistoryInfo implements java.io.Serializable
{
	private static final long serialVersionUID = 1591656758212920353L;
	@SerializedName("userId")
	private String userId;
	@SerializedName("productId")
	private String productId;
	@SerializedName("viewCount")
	private int viewCount;
	@SerializedName("addDateLong")
	private long addDateLong;
	@SerializedName("updateDateLong")
	private long updateDateLong;
	private ProductInfo productInfo;

	public ProductHistoryInfo()
	{
	}

	public ProductHistoryInfo(String userId, String productId, int viewCount, long addDateLong, long updateDateLong)
	{
		this.userId = userId;
		this.productId = productId;
		this.viewCount = viewCount;
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

	public String getProductId()
	{
		return this.productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public int getViewCount()
	{
		return this.viewCount;
	}

	public void setViewCount(int viewCount)
	{
		this.viewCount = viewCount;
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

	public ProductInfo getProductInfo()
	{
		return productInfo;
	}

	public void setProductInfo(ProductInfo productInfo)
	{
		this.productInfo = productInfo;
	}
}
