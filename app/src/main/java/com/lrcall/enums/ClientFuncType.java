/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by libit on 2016/11/30.
 */
public enum ClientFuncType
{
	OPEN_URL("open_url", "打开一个网址"), OPEN_ACTIIVTY("open_activity", "打开一个窗口");
	private String type;
	private String desc;

	ClientFuncType(String type, String desc)
	{
		this.type = type;
		this.desc = desc;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
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

	public static String getTypeDesc(String type)
	{
		ClientFuncType[] list = ClientFuncType.values();
		for (ClientFuncType clientFuncType : list)
		{
			if (clientFuncType.getType().equals(type))
			{
				return clientFuncType.getDesc();
			}
		}
		return "";
	}

	public static Map<String, Object> getMap()
	{
		ClientFuncType[] list = ClientFuncType.values();
		Map<String, Object> map = new HashMap<>();
		for (ClientFuncType clientFuncType : list)
		{
			map.put(clientFuncType.getType(), clientFuncType.getDesc());
		}
		return map;
	}
}
