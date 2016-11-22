/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

/**
 * Created by libit on 2016/11/22.
 */
public enum PointLogType
{
	ORDER_ADD(0, "订单下单，积分抵扣"), ORDER_FINISH(1, "订单完成赠送"), REGISTER_POINT(2, "注册赠送"), SIGN_POINT(3, "签到赠送"), REFERRER_POINT(4, "推荐赠送");
	private int type;
	private String desc;

	private PointLogType(int type, String desc)
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

	public static String getLogString(int logType)
	{
		PointLogType[] orderLogTypes = new PointLogType[]{ORDER_ADD, ORDER_FINISH, REGISTER_POINT, SIGN_POINT, REFERRER_POINT};
		for (PointLogType orderLogType : orderLogTypes)
		{
			if (logType == orderLogType.type)
			{
				return orderLogType.desc;
			}
		}
		return "";
	}
}
