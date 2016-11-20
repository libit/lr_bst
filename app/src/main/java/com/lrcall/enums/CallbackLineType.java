/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

/**
 * Created by libit on 16/7/29.
 */
public enum CallbackLineType
{
	LINE_1(1, "快速通道"), LINE_2(2, "高清通道");
	private int type;
	private String desc;

	CallbackLineType(int type, String desc)
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

	public static String getTypeDesc(int type)
	{
		CallbackLineType[] list = CallbackLineType.values();
		for (CallbackLineType callbackLineType : list)
		{
			if (callbackLineType.getType() == type)
			{
				return callbackLineType.getDesc();
			}
		}
		return "";
	}
}
