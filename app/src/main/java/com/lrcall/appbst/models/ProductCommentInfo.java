/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by libit on 16/7/29.
 */
public class ProductCommentInfo
{
	@SerializedName("id")
	private Integer id;
	@SerializedName("commentId")
	private String commentId;
	@SerializedName("orderId")
	private String orderId;
	@SerializedName("userId")
	private String userId;
	@SerializedName("productId")
	private String productId;
	@SerializedName("commentType")
	private Byte type;
	@SerializedName("commentLevel")
	private int level;
	@SerializedName("content")
	private String content;
	@SerializedName("addDateLong")
	private long date;
	@SerializedName("userInfo")
	private UserInfo userInfo;

	public ProductCommentInfo()
	{
	}

	public ProductCommentInfo(String commentId, String orderId, String userId, String productId, Byte type, int level, String content, long date)
	{
		this.commentId = commentId;
		this.orderId = orderId;
		this.userId = userId;
		this.productId = productId;
		this.type = type;
		this.level = level;
		this.content = content;
		this.date = date;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getCommentId()
	{
		return commentId;
	}

	public void setCommentId(String commentId)
	{
		this.commentId = commentId;
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

	public Byte getType()
	{
		return type;
	}

	public void setType(Byte type)
	{
		this.type = type;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public long getDate()
	{
		return date;
	}

	public void setDate(long date)
	{
		this.date = date;
	}

	public UserInfo getUserInfo()
	{
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo)
	{
		this.userInfo = userInfo;
	}
}
