/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

/**
 * Created by libit on 16/7/18.
 */
public enum ProductPicType
{
	CLIENT_BANNER("客户端首页滚动展示图片", "c_banner")/* 客户端首页滚动展示图片 */, CLIENT_CONCESSION("客户端促销页展示图片", "c_concession")/* 客户端促销页展示图片 */, CLIENT_DETAIL_BIG("客户端详情页大图展示图片", "c_detail_big")/* 客户端详情页大图展示图片 */;
	private String name;
	private String type;

	ProductPicType(String name, String type)
	{
		this.name = name;
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
}
