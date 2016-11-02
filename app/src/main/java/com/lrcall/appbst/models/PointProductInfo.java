/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by libit on 2016/11/1.
 */
public class PointProductInfo
{
	@SerializedName("productId")
	private String productId;
	@SerializedName("sortId")
	private String sortName;
	@SerializedName("brandId")
	private String brandName;
	@SerializedName("name")
	private String name;
	@SerializedName("picId")
	private String picUrl;
	@SerializedName("point")
	private int point;
	@SerializedName("marketPrice")
	private int marketPrice;
	@SerializedName("expressPrice")
	private int expressPrice;
	@SerializedName("amount")
	private int amount;
	@SerializedName("description")
	private String description;
	@SerializedName("config")
	private String config;
	@SerializedName("content")
	private String content;
	@SerializedName("needExpress")
	private byte needExpress;
	@SerializedName("sortIndex")
	private int sortIndex;

	public PointProductInfo()
	{
	}

	public PointProductInfo(String productId, String sortName, String brandName, String name, String picUrl, int point, int marketPrice, int expressPrice, int amount, String description, String config, String content, byte needExpress, int sortIndex)
	{
		this.productId = productId;
		this.sortName = sortName;
		this.brandName = brandName;
		this.name = name;
		this.picUrl = picUrl;
		this.point = point;
		this.marketPrice = marketPrice;
		this.expressPrice = expressPrice;
		this.amount = amount;
		this.description = description;
		this.config = config;
		this.content = content;
		this.needExpress = needExpress;
		this.sortIndex = sortIndex;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public String getSortName()
	{
		return sortName;
	}

	public void setSortName(String sortName)
	{
		this.sortName = sortName;
	}

	public String getBrandName()
	{
		return brandName;
	}

	public void setBrandName(String brandName)
	{
		this.brandName = brandName;
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

	public int getPoint()
	{
		return point;
	}

	public void setPoint(int point)
	{
		this.point = point;
	}

	public int getMarketPrice()
	{
		return marketPrice;
	}

	public void setMarketPrice(int marketPrice)
	{
		this.marketPrice = marketPrice;
	}

	public int getExpressPrice()
	{
		return expressPrice;
	}

	public void setExpressPrice(int expressPrice)
	{
		this.expressPrice = expressPrice;
	}

	public int getAmount()
	{
		return amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getConfig()
	{
		return config;
	}

	public void setConfig(String config)
	{
		this.config = config;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public byte getNeedExpress()
	{
		return needExpress;
	}

	public void setNeedExpress(byte needExpress)
	{
		this.needExpress = needExpress;
	}

	public int getSortIndex()
	{
		return sortIndex;
	}

	public void setSortIndex(int sortIndex)
	{
		this.sortIndex = sortIndex;
	}
}
