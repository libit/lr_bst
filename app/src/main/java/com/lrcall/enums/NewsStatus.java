/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

/**
 * Created by libit on 16/7/12.
 */
public enum NewsStatus
{
	UNREAD(0), READ(1);
	private final int status;

	NewsStatus(int status)
	{
		this.status = status;
	}

	public int getStatus()
	{
		return status;
	}
}
