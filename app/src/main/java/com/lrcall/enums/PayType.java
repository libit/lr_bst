/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

/**
 * 需付款的类型
 * Created by libit on 16/7/12.
 */
public enum PayType
{
	PAY_ORDER("pay_order", "普通订单"), PAY_DATA_TRAFFIC_ORDER("pay_data_traffic_order", "流量订单"), PAY_BALANCE("pay_balance", "余额充值"), PAY_UPGRADE("pay_upgrade", "用户升级"), PAY_POINT_ORDER("pay_point_order", "积分商品订单");
	private String type;
	private String desc;

	PayType(String type, String desc)
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
}
