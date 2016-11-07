package com.lrcall.enums;

/**
 * 用户余额变动记录类型
 *
 * @author libit
 */
public enum UserBalanceLogType
{
	BUY_PRODUCT(0, "购买商品"), BUY_DATA_TRAFFIC(1, "购买流量"), UPDATE_BALANCE(2, "更新余额"), SHARED_PROFIT(3, "分润"), BUY_POINT_PRODUCT(4, "购买积分商品"), USER_UPGRADE(5, "用户升级");
	private int type;
	private String desc;

	private UserBalanceLogType(int type, String desc)
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
		UserBalanceLogType[] orderLogTypes = UserBalanceLogType.values();
		for (UserBalanceLogType orderLogType : orderLogTypes)
		{
			if (logType == orderLogType.type)
			{
				return orderLogType.desc;
			}
		}
		return "";
	}
}
