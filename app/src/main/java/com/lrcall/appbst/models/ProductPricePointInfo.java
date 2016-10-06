/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class ProductPricePointInfo
{
	@SerializedName("productId")
	private String productId;
	@SerializedName("maxPoint")
	private int maxPoint;

	public ProductPricePointInfo()
	{
	}

	public ProductPricePointInfo(String productId, int maxPoint)
	{
		this.productId = productId;
		this.maxPoint = maxPoint;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public int getMaxPoint()
	{
		return maxPoint;
	}

	public void setMaxPoint(int maxPoint)
	{
		this.maxPoint = maxPoint;
	}
}
