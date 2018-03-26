/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流量卡类型
 *
 * @author libit
 */
public enum DataTrafficType
{
	COMMON("10", "普通卡"), PACKAGE("20", "套餐卡");
	private String type;
	private String desc;

	 DataTrafficType(String type, String desc)
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

	public static String getDataTrafficTypeDesc(String type)
	{
		if (!StringTools.isNull(type))
		{
			List<DataTrafficType> list = new ArrayList<>();
			list.add(DataTrafficType.COMMON);
			list.add(DataTrafficType.PACKAGE);
			for (DataTrafficType dataTrafficType : list)
			{
				if (dataTrafficType.getType().equals(type))
				{
					return dataTrafficType.getDesc();
				}
			}
		}
		return "未知";
	}

	public static List<DataTrafficType> getAllType()
	{
		List<DataTrafficType> list = new ArrayList<>();
		list.add(DataTrafficType.COMMON);
		list.add(DataTrafficType.PACKAGE);
		return list;
	}

	public static Map<String, String> getMap()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put(DataTrafficType.COMMON.getType(), DataTrafficType.COMMON.getDesc());
		map.put(DataTrafficType.PACKAGE.getType(), DataTrafficType.PACKAGE.getDesc());
		return map;
	}
}
