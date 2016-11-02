/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by libit on 16/7/14.
 */
public class PointOrderInfo
{
	@SerializedName("orderId")
	private String orderId;
	@SerializedName("userId")
	private String userId;
	@SerializedName("productsPoint")
	private int productsPoint;
	@SerializedName("expressPrice")
	private int expressPrice;
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
	@SerializedName("orderProductInfoList")
	private List<OrderProductInfo> orderProductInfoList;

	public PointOrderInfo()
	{
	}

	public PointOrderInfo(String orderId, String userId, int productsPoint, int expressPrice, int totalPrice, int marketPrice, String comment, byte status, String remark, long addDateLong, long updateDateLong, List<OrderProductInfo> orderProductInfoList)
	{
		this.orderId = orderId;
		this.userId = userId;
		this.productsPoint = productsPoint;
		this.expressPrice = expressPrice;
		this.totalPrice = totalPrice;
		this.marketPrice = marketPrice;
		this.comment = comment;
		this.status = status;
		this.remark = remark;
		this.addDateLong = addDateLong;
		this.updateDateLong = updateDateLong;
		this.orderProductInfoList = orderProductInfoList;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public int getProductsPoint()
	{
		return productsPoint;
	}

	public void setProductsPoint(int productsPoint)
	{
		this.productsPoint = productsPoint;
	}

	public int getExpressPrice()
	{
		return expressPrice;
	}

	public void setExpressPrice(int expressPrice)
	{
		this.expressPrice = expressPrice;
	}

	public int getTotalPrice()
	{
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice)
	{
		this.totalPrice = totalPrice;
	}

	public int getMarketPrice()
	{
		return marketPrice;
	}

	public void setMarketPrice(int marketPrice)
	{
		this.marketPrice = marketPrice;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public byte getStatus()
	{
		return status;
	}

	public void setStatus(byte status)
	{
		this.status = status;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public long getAddDateLong()
	{
		return addDateLong;
	}

	public void setAddDateLong(long addDateLong)
	{
		this.addDateLong = addDateLong;
	}

	public long getUpdateDateLong()
	{
		return updateDateLong;
	}

	public void setUpdateDateLong(long updateDateLong)
	{
		this.updateDateLong = updateDateLong;
	}

	public List<OrderProductInfo> getOrderProductInfoList()
	{
		return orderProductInfoList;
	}

	public void setOrderProductInfoList(List<OrderProductInfo> orderProductInfoList)
	{
		this.orderProductInfoList = orderProductInfoList;
	}
}
