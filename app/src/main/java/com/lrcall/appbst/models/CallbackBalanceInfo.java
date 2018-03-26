/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class CallbackBalanceInfo
{
	@SerializedName("number")
	private String number;
	@SerializedName("balance")
	private double balance;
	@SerializedName("registerDate")
	private String registerDate;
	@SerializedName("validateDate")
	private String validateDate;

	public CallbackBalanceInfo()
	{
		super();
	}

	public CallbackBalanceInfo(String number, double balance, String registerDate, String validateDate)
	{
		this.number = number;
		this.balance = balance;
		this.registerDate = registerDate;
		this.validateDate = validateDate;
	}

	public double getBalance()
	{
		return balance;
	}

	public void setBalance(double balance)
	{
		this.balance = balance;
	}

	public String getValidateDate()
	{
		return validateDate;
	}

	public void setValidateDate(String validateDate)
	{
		this.validateDate = validateDate;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getRegisterDate()
	{
		return registerDate;
	}

	public void setRegisterDate(String registerDate)
	{
		this.registerDate = registerDate;
	}
}
