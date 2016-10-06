/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

/**
 * Created by libit on 16/7/12.
 */
public enum PicQuality
{
	NO(0), LOW(1), MID(5), HIGH(10);
	private final int level;

	PicQuality(int level)
	{
		this.level = level;
	}

	public int getLevel()
	{
		return level;
	}
}
