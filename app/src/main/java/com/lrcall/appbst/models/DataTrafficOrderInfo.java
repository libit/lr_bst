/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

public class DataTrafficOrderInfo
{
	private Integer id;
	private String orderId;
	private String userId;
	private String productId;
	private int count;
	private String number;
	private int productsPrice;
	private int discountPrice;
	private int totalPrice;
	private int marketPrice;
	private String comment;
	private byte status;
	private String remark;
	private long addDateLong;
	private long updateDateLong;
	private DataTrafficInfo dataTrafficInfo;

	public DataTrafficOrderInfo()
	{
	}

	public DataTrafficOrderInfo(String orderId, String userId, String productId, int count, String number, int productsPrice, int discountPrice, int totalPrice, int marketPrice, String comment, byte status, String remark, long addDateLong, long updateDateLong, DataTrafficInfo dataTrafficInfo)
	{
		this.orderId = orderId;
		this.userId = userId;
		this.productId = productId;
		this.count = count;
		this.number = number;
		this.productsPrice = productsPrice;
		this.discountPrice = discountPrice;
		this.totalPrice = totalPrice;
		this.marketPrice = marketPrice;
		this.comment = comment;
		this.status = status;
		this.remark = remark;
		this.addDateLong = addDateLong;
		this.updateDateLong = updateDateLong;
		this.dataTrafficInfo = dataTrafficInfo;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
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

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public int getProductsPrice()
	{
		return productsPrice;
	}

	public void setProductsPrice(int productsPrice)
	{
		this.productsPrice = productsPrice;
	}

	public int getDiscountPrice()
	{
		return discountPrice;
	}

	public void setDiscountPrice(int discountPrice)
	{
		this.discountPrice = discountPrice;
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

	public DataTrafficInfo getDataTrafficInfo()
	{
		return dataTrafficInfo;
	}

	public void setDataTrafficInfo(DataTrafficInfo dataTrafficInfo)
	{
		this.dataTrafficInfo = dataTrafficInfo;
	}
}
