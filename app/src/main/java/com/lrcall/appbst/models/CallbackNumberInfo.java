/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

/**
 * Created by libit on 2016/11/16.
 */
public class CallbackNumberInfo
{
	private String number;
	private String name;
	private long addDateLong;

	public CallbackNumberInfo()
	{
	}

	public CallbackNumberInfo(String number, String name, long addDateLong)
	{
		this.number = number;
		this.name = name;
		this.addDateLong = addDateLong;
	}

	public String getNumber()
	{
		return this.number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getAddDateLong()
	{
		return this.addDateLong;
	}

	public void setAddDateLong(long addDateLong)
	{
		this.addDateLong = addDateLong;
	}
}
