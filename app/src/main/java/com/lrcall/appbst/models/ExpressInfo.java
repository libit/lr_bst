/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by libit on 16/7/28.
 */
public class ExpressInfo
{
	@SerializedName("id")
	private Integer id;
	@SerializedName("orderExpressId")
	private String orderExpressId;
	@SerializedName("orderSubId")
	private String orderId;
	@SerializedName("addressId")
	private String addressId;
	@SerializedName("expressName")
	private String expressName;
	@SerializedName("expressId")
	private String expressId;
	@SerializedName("price")
	private int price;
	@SerializedName("status")
	private byte status;
	@SerializedName("addDateLong")
	private long addDateLong;
	@SerializedName("updateDateLong")
	private long updateDateLong;

	public ExpressInfo()
	{
	}

	public ExpressInfo(String orderExpressId, String orderId, String addressId, String expressName, String expressId, int price, byte status, long addDateLong, long updateDateLong)
	{
		this.orderExpressId = orderExpressId;
		this.orderId = orderId;
		this.addressId = addressId;
		this.expressName = expressName;
		this.expressId = expressId;
		this.price = price;
		this.status = status;
		this.addDateLong = addDateLong;
		this.updateDateLong = updateDateLong;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getOrderExpressId()
	{
		return orderExpressId;
	}

	public void setOrderExpressId(String orderExpressId)
	{
		this.orderExpressId = orderExpressId;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public String getAddressId()
	{
		return addressId;
	}

	public void setAddressId(String addressId)
	{
		this.addressId = addressId;
	}

	public String getExpressName()
	{
		return expressName;
	}

	public void setExpressName(String expressName)
	{
		this.expressName = expressName;
	}

	public String getExpressId()
	{
		return expressId;
	}

	public void setExpressId(String expressId)
	{
		this.expressId = expressId;
	}

	public int getPrice()
	{
		return price;
	}

	public void setPrice(int price)
	{
		this.price = price;
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
}
