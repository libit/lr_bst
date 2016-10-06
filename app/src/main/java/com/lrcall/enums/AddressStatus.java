/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

/**
 * 地址状态
 *
 * @author libit
 */
public enum AddressStatus
{
	ENABLED((byte) 0, "已启用"), DISABLED((byte) 1, "已禁用"), DEFAULT((byte) 2, "默认地址"), DELETED((byte) 3, "已删除");
	private byte status;
	private String desc;

	AddressStatus(byte status, String desc)
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
