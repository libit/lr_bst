/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by libit on 16/7/14.
 */
public class OrderProductInfo
{
	@SerializedName("orderProductId")
	private String orderProductId;//ID
	@SerializedName("orderId")
	private String orderId;//ID
	@SerializedName("count")
	private int count;//总数
	@SerializedName("price")
	private int price;//单价
	@SerializedName("totalPrice")
	private int totalPrice;//总价
	@SerializedName("updateDateLong")
	private long updateDateLong;//最后更新状态时间
	@SerializedName("productInfo")
	private ProductInfo productInfo;//商品信息

	public OrderProductInfo()
	{
	}

	public OrderProductInfo(String orderProductId, String orderId, int count, int price, int totalPrice, long updateDateLong)
	{
		this.orderProductId = orderProductId;
		this.orderId = orderId;
		this.count = count;
		this.price = price;
		this.totalPrice = totalPrice;
		this.updateDateLong = updateDateLong;
	}

	public OrderProductInfo(String orderProductId, String orderId, int count, int price, int totalPrice, long updateDateLong, ProductInfo productInfo)
	{
		this.orderProductId = orderProductId;
		this.orderId = orderId;
		this.count = count;
		this.price = price;
		this.totalPrice = totalPrice;
		this.updateDateLong = updateDateLong;
		this.productInfo = productInfo;
	}

	public String getOrderProductId()
	{
		return orderProductId;
	}

	public void setOrderProductId(String orderProductId)
	{
		this.orderProductId = orderProductId;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public int getPrice()
	{
		return price;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}

	public int getTotalPrice()
	{
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice)
	{
		this.totalPrice = totalPrice;
	}

	public long getUpdateDateLong()
	{
		return updateDateLong;
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
