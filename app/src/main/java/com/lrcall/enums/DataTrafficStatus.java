package com.lrcall.enums;

/**
 * 流量充值商品状态
 *
 * @author libit
 */
public enum DataTrafficStatus
{
	ENABLED((byte) 0, "已启用"), DISABLED((byte) 1, "已禁用"), HOT((byte) 2, "火爆");
	private byte status;
	private String desc;

	private DataTrafficStatus(byte status, String desc)
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
}
