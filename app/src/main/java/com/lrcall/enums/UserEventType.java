/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

/**
 * Created by libit on 16/7/15.
 */
public enum UserEventType
{
	LOGINED("logined");
	private final String type;

	UserEventType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}
}
