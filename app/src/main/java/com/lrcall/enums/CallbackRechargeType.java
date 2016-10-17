/*
 * Libit保留所有版权，如有疑问联系QQ：30806205
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

/**
 * 回拨充值类型
 *
 * @author libit
 */
public enum CallbackRechargeType
{
	CARD("card", "充值卡"), ALIPAY("alipay", "支付宝"), WECHART("wechart", "微信支付"), BALANCE("balance", "余额支付");
	private String type;
	private String desc;

	private CallbackRechargeType(String type, String desc)
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
		CallbackRechargeType[] list = CallbackRechargeType.values();
		for (CallbackRechargeType callbackRechargeType : list)
		{
			if (callbackRechargeType.getType().equals(type))
			{
				return callbackRechargeType.getDesc();
			}
		}
		return type;
	}
}
