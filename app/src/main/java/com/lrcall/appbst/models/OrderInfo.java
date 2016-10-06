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
public class OrderInfo
{
	@SerializedName("orderId")
	private String orderId;//ID
	@SerializedName("userId")
	private String userId;//用户ID
	@SerializedName("totalPrice")
	private int totalPrice;//总价
	@SerializedName("comment")
	private String comment;//备注
	@SerializedName("status")
	private byte status;//状态
	@SerializedName("addDateLong")
	private long addDateLong;//下单时间
	@SerializedName("updateDateLong")
	private long updateDateLong;//最后更新状态时间
	@SerializedName("orderProductInfoList")
	private List<OrderProductInfo> orderProductInfoList;

	public OrderInfo()
	{
	}

	public OrderInfo(String orderId, String userId, int totalPrice, String comment, byte status, long addDateLong, long updateDateLong)
	{
		this.orderId = orderId;
		this.userId = userId;
		this.totalPrice = totalPrice;
		this.comment = comment;
		this.status = status;
		this.addDateLong = addDateLong;
		this.updateDateLong = updateDateLong;
	}

	public OrderInfo(String orderId, String userId, int totalPrice, String comment, byte status, long addDateLong, long updateDateLong, List<OrderProductInfo> orderProductInfoList)
	{
		this.orderId = orderId;
		this.userId = userId;
		this.totalPrice = totalPrice;
		this.comment = comment;
		this.status = status;
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

	public int getTotalPrice()
	{
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice)
	{
		this.totalPrice = totalPrice;
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
