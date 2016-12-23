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
public enum ClientFuncActivityType
{
	SHOP_PRODUCTS("ActivityShopProducts", "微店"), RECHARGE_DATA_TRAFFIC("ActivityRechargeDataTraffic", "流量充值"), POINT_PRODUCT_SHOP("ActivityPointProductShop", "积分商城"), PRODUCTS("ActivityProducts", "分类商品");
	private String type;
	private String desc;

	ClientFuncActivityType(String type, String desc)
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
		ClientFuncActivityType[] list = ClientFuncActivityType.values();
		for (ClientFuncActivityType clientClientFuncType : list)
		{
			if (clientClientFuncType.getType().equals(type))
			{
				return clientClientFuncType.getDesc();
			}
		}
		return "";
	}

	public static Map<String, Object> getMap()
	{
		ClientFuncActivityType[] list = ClientFuncActivityType.values();
		Map<String, Object> map = new HashMap<>();
		for (ClientFuncActivityType clientClientFuncType : list)
		{
			map.put(clientClientFuncType.getType(), clientClientFuncType.getDesc());
		}
		return map;
	}
}
