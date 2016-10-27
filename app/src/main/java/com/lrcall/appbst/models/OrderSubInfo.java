/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderSubInfo
{
	@SerializedName("orderSubId")
	private String orderSubId;
	@SerializedName("orderId")
	private String orderId;
	@SerializedName("shopId")
	private String shopId;
	@SerializedName("userId")
	private String userId;
	@SerializedName("productsPrice")
	private int productsPrice;
	@SerializedName("expressPrice")
	private int expressPrice;
	@SerializedName("discountPrice")
	private int discountPrice;
	@SerializedName("totalPrice")
	private int totalPrice;
	@SerializedName("marketPrice")
	private int marketPrice;
	@SerializedName("comment")
	private String comment;
	@SerializedName("status")
	private byte status;
	@SerializedName("remark")
	private String remark;
	@SerializedName("addDateLong")
	private long addDateLong;
	@SerializedName("updateDateLong")
	private long updateDateLong;
	@SerializedName("userInfo")
	private UserInfo userInfo;
	@SerializedName("orderProductInfoList")
	private List<OrderProductInfo> orderProductInfoList;

	public UserInfo getUserInfo()
	{
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo)
	{
		this.userInfo = userInfo;
	}

	public List<OrderProductInfo> getOrderProductInfoList()
	{
		return orderProductInfoList;
	}

	public void setOrderProductInfoList(List<OrderProductInfo> orderProductInfoList)
	{
		this.orderProductInfoList = orderProductInfoList;
	}

	public OrderSubInfo()
	{
	}

	public OrderSubInfo(String orderSubId, String orderId, String shopId, String userId, int productsPrice, int expressPrice, int discountPrice, int totalPrice, int marketPrice, byte status, long addDateLong, long updateDateLong)
	{
		this.orderSubId = orderSubId;
		this.orderId = orderId;
		this.shopId = shopId;
		this.userId = userId;
		this.productsPrice = productsPrice;
		this.expressPrice = expressPrice;
		this.discountPrice = discountPrice;
		this.totalPrice = totalPrice;
		this.marketPrice = marketPrice;
		this.status = status;
		this.addDateLong = addDateLong;
		this.updateDateLong = updateDateLong;
	}

	public OrderSubInfo(String orderSubId, String orderId, String shopId, String userId, int productsPrice, int expressPrice, int discountPrice, int totalPrice, int marketPrice, String comment, byte status, String remark, long addDateLong, long updateDateLong)
	{
		this.orderSubId = orderSubId;
		this.orderId = orderId;
		this.shopId = shopId;
		this.userId = userId;
		this.productsPrice = productsPrice;
		this.expressPrice = expressPrice;
		this.discountPrice = discountPrice;
		this.totalPrice = totalPrice;
		this.marketPrice = marketPrice;
		this.comment = comment;
		this.status = status;
		this.remark = remark;
		this.addDateLong = addDateLong;
		this.updateDateLong = updateDateLong;
	}

	public String getOrderSubId()
	{
		return this.orderSubId;
	}

	public void setOrderSubId(String orderSubId)
	{
		this.orderSubId = orderSubId;
	}

	public String getOrderId()
	{
		return this.orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public String getShopId()
	{
		return this.shopId;
	}

	public void setShopId(String shopId)
	{
		this.shopId = shopId;
	}

	public String getUserId()
	{
		return this.userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public int getProductsPrice()
	{
		return this.productsPrice;
	}

	public void setProductsPrice(int productsPrice)
	{
		this.productsPrice = productsPrice;
	}

	public int getExpressPrice()
	{
		return this.expressPrice;
	}

	public void setExpressPrice(int expressPrice)
	{
		this.expressPrice = expressPrice;
	}

	public int getDiscountPrice()
	{
		return this.discountPrice;
	}

	public void setDiscountPrice(int discountPrice)
	{
		this.discountPrice = discountPrice;
	}

	public int getTotalPrice()
	{
		return this.totalPrice;
	}

	public void setTotalPrice(int totalPrice)
	{
		this.totalPrice = totalPrice;
	}

	public int getMarketPrice()
	{
		return this.marketPrice;
	}

	public void setMarketPrice(int marketPrice)
	{
		this.marketPrice = marketPrice;
	}

	public String getComment()
	{
		return this.comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public byte getStatus()
	{
		return this.status;
	}

	public void setStatus(byte status)
	{
		this.status = status;
	}

	public String getRemark()
	{
		return this.remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
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
}
