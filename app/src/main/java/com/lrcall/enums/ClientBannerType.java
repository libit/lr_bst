/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JC on 2016/11/29.
 */
public enum ClientBannerType
{
	PAGE_INDEX("PAGE_INDEX", "首页Banner"), USER_UPGRADE("USER_UPGRADE", "用户升级页面"), POINT_PRODUCT("POINT_PRODUCT", "积分商城页面");
	private String type;
	private String desc;

	ClientBannerType(String type, String desc)
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
		ClientBannerType[] list = ClientBannerType.values();
		for (ClientBannerType clientBannerType : list)
		{
			if (clientBannerType.getType().equals(type))
			{
				return clientBannerType.getDesc();
			}
		}
		return "";
	}

	public static Map<String, Object> getMap()
	{
		ClientBannerType[] list = ClientBannerType.values();
		Map<String, Object> map = new HashMap<>();
		for (ClientBannerType clientBannerType : list)
		{
			map.put(clientBannerType.getType(), clientBannerType.getDesc());
		}
		return map;
	}
}
