/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

/**
 * Created by libit on 16/7/12.
 */
public enum DataTrafficOrderType
{
	DELETED((byte) 0, "已取消"), WAIT_PAY((byte) 1, "待付款"), PAYED((byte) 10, "已付款"), FINISH((byte) 30, "已充值");
	private byte status;
	private String desc;

	DataTrafficOrderType(byte status, String desc)
	{
		this.status = status;
		this.desc = desc;
	}

	public byte getStatus()
	{
		return status;
	}

	public void setStatus(byte status)
	{
		this.status = status;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}
}
