/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

public class CallbackBalanceInfo
{
	private double balance;
	private String validateDate;

	public CallbackBalanceInfo()
	{
		super();
	}

	public CallbackBalanceInfo(double balance, String validateDate)
	{
		super();
		this.balance = balance;
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
}
