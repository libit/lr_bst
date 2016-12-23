/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class UserBankInfo
{
	@SerializedName("userBankId")
	private String userBankId;
	@SerializedName("userId")
	private String userId;
	@SerializedName("bankName")
	private String bankName;
	@SerializedName("cardName")
	private String cardName;
	@SerializedName("cardId")
	private String cardId;
	@SerializedName("status")
	private byte status;
	@SerializedName("addDateLong")
	private long addDateLong;

	public UserBankInfo()
	{
		super();
	}

	public String getUserBankId()
	{
		return userBankId;
	}

	public void setUserBankId(String userBankId)
	{
		this.userBankId = userBankId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getBankName()
	{
		return bankName;
	}

	public void setBankName(String bankName)
	{
		this.bankName = bankName;
	}

	public String getCardName()
	{
		return cardName;
	}

	public void setCardName(String cardName)
	{
		this.cardName = cardName;
	}

	public String getCardId()
	{
		return cardId;
	}

	public void setCardId(String cardId)
	{
		this.cardId = cardId;
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
}
