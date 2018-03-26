/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

/**
 * 商家级别
 *
 * @author libit
 */
public enum ShopLevel
{
	L2((byte) 10, "推广员"), L3((byte) 20, "掌柜"), L4((byte) 30, "渠道商"), L5((byte) 40, "供应商");
	private byte level;
	private String desc;

	 ShopLevel(byte level, String desc)
	{
		this.level = level;
		this.desc = desc;
	}

	public byte getLevel()
	{
		return level;
	}

	public void setLevel(byte level)
	{
		this.level = level;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public static String getLevelDesc(int level)
	{
		ShopLevel[] list = ShopLevel.values();
		for (ShopLevel shopLevel : list)
		{
			if (shopLevel.getLevel() == level)
			{
				return shopLevel.getDesc();
			}
		}
		return "";
	}
}
