/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class UserWithdrawInfo
{
	@SerializedName("withdrawId")
	private String withdrawId;
	@SerializedName("userId")
	private String userId;
	@SerializedName("amount")
	private int amount;
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

	public UserWithdrawInfo()
	{
		super();
	}

	public UserWithdrawInfo(String withdrawId, long addDateLong, String userId, int amount, String bankName, String cardName, String cardId, byte status)
	{
		this.withdrawId = withdrawId;
		this.addDateLong = addDateLong;
		this.userId = userId;
		this.amount = amount;
		this.bankName = bankName;
		this.cardName = cardName;
		this.cardId = cardId;
		this.status = status;
	}

	public String getWithdrawId()
	{
		return withdrawId;
	}

	public void setWithdrawId(String withdrawId)
	{
		this.withdrawId = withdrawId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public int getAmount()
	{
		return amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
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
