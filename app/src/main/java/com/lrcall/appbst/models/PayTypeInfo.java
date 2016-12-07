/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;
import com.lrcall.enums.PayType;

import java.io.Serializable;

/**
 * Created by libit on 2016/10/8.
 */
public class PayTypeInfo implements Serializable
{
	private static final long serialVersionUID = -7404611634463340895L;
	@SerializedName("payType")
	private PayType payType;//需付款的类型
	@SerializedName("price")
	private int price;//付款金额,单位:分
	@SerializedName("subject")
	private String subject;//标题
	@SerializedName("comment")
	private String comment;//备注信息,可以是订单号或充值的账号

	public PayTypeInfo()
	{
	}

	public PayTypeInfo(PayType payType, int price, String subject, String comment)
	{
		this.payType = payType;
		this.price = price;
		this.subject = subject;
		this.comment = comment;
	}

	public PayType getPayType()
	{
		return payType;
	}

	public void setPayType(PayType payType)
	{
		this.payType = payType;
	}

	public int getPrice()
	{
		return price;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}
}
