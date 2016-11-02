package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by libit on 2016/10/31.
 */
public class ShopProductAgentInfo
{
	@SerializedName("productAgentId")
	private String productAgentId;
	@SerializedName("shopId")
	private String shopId;
	@SerializedName("productId")
	private String productId;
	@SerializedName("productInfo")
	private ProductInfo productInfo;

	public ShopProductAgentInfo()
	{
	}

	public ShopProductAgentInfo(String productAgentId, String shopId, String productId, ProductInfo productInfo)
	{
		this.productAgentId = productAgentId;
		this.shopId = shopId;
		this.productId = productId;
		this.productInfo = productInfo;
	}

	public String getProductAgentId()
	{
		return productAgentId;
	}

	public void setProductAgentId(String productAgentId)
	{
		this.productAgentId = productAgentId;
	}

	public String getShopId()
	{
		return shopId;
	}

	public void setShopId(String shopId)
	{
		this.shopId = shopId;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
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
