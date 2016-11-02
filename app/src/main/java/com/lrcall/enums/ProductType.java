/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

/**
 * Created by libit on 16/7/29.
 */
public enum ProductType
{
	PRODUCT(0, "普通商品"), POINT_PRODUCT(1, "积分商品");
	private int type;
	private String desc;

	ProductType(int type, String desc)
	{
		this.type = type;
		this.desc = desc;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
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
