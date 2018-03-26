/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JC on 2016/12/22.
 */
public enum WithdrawStatus
{
	SUBMIT((byte) 0, "已提交"), VERIFY_SUCCESS((byte) 10, "已受理"), VERIFY_FAIL((byte) 20, "审核失败"), PAYED((byte) 30, "已打款");
	private byte status;
	private String desc;

	 WithdrawStatus(byte status, String desc)
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

	public static String getDesc(byte status)
	{
		WithdrawStatus[] values = WithdrawStatus.values();
		for (WithdrawStatus withdrawStatus : values)
		{
			if (withdrawStatus.getStatus() == status)
			{
				return withdrawStatus.getDesc();
			}
		}
		return "";
	}

	public static Map<String, Byte> getMap()
	{
		Map<String, Byte> map = new HashMap<>();
		WithdrawStatus[] list = WithdrawStatus.values();
		for (WithdrawStatus withdrawStatus : list)
		{
			map.put(withdrawStatus.name(), withdrawStatus.getStatus());
		}
		return map;
	}
}
