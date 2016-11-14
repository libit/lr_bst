/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.models;

/**
 * Created by JC on 2016/11/13.
 */
public class ShareProductData extends ShareData
{
	private String productId;

	public ShareProductData()
	{
		super();
	}

	public ShareProductData(String url, String productId, String shareUserId)
	{
		super(url, shareUserId);
		this.productId = productId;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}
}
