package com.lrcall.enums;

/**
 * 是否需要快递
 *
 * @author libit
 */
public enum NeedExpress
{
	NEED((byte) 0), NOT_NEED((byte) 1);
	private byte status;

	NeedExpress(byte status)
	{
		this.setStatus(status);
	}

	public byte getStatus()
	{
		return status;
	}

	public void setStatus(byte status)
	{
		this.status = status;
	}
}
