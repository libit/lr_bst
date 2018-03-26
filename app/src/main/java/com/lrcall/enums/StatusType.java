/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2017.
 */
package com.lrcall.enums;

/**
 * Created by libit on 2017/2/26.
 */
public enum StatusType
{
	ENABLED((byte) 0), DISABLED((byte) 1), UNKNOWN((byte) 3);
	byte status;

	StatusType(byte status)
	{
		this.setStatus(status);
	}

	public byte getStatus()
	{
		return status;
	}

	public void setStatus(byte status)
	{
		this.status = status;
	}
}
