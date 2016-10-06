/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

/**
 * 声音/震动模式
 * Created by libit on 16/7/12.
 */
public enum ModType
{
	GENERIC_TYPE_AUTO(0), GENERIC_TYPE_FORCE(1), GENERIC_TYPE_PREVENT(2);
	private final int type;

	ModType(int type)
	{
		this.type = type;
	}

	public int getType()
	{
		return type;
	}
}
