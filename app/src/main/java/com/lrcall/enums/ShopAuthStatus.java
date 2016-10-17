/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

/**
 * 商户认证状态
 *
 * @author libit
 */
public enum ShopAuthStatus
{
	UNAUTH((byte) 10, "未认证"), AUTHED((byte) 20, "已认证");
	private byte status;
	private String desc;

	private ShopAuthStatus(byte status, String desc)
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

	public static String getAuthDesc(int status)
	{
		ShopAuthStatus[] list = ShopAuthStatus.values();
		for (ShopAuthStatus shopAuthStatus : list)
		{
			if (shopAuthStatus.getStatus() == status)
			{
				return shopAuthStatus.getDesc();
			}
		}
		return "";
	}
}
