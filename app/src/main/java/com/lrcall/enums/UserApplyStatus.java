package com.lrcall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户申请状态
 *
 * @author libit
 */
public enum UserApplyStatus
{
	APPLY((byte) 0, "已申请"), VERIFY_SUCCESS((byte) 10, "审核通过"), VERIFY_FAIL((byte) 20, "审核不通过");
	private byte status;
	private String desc;

	private UserApplyStatus(byte status, String desc)
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

	public static String getDesc(byte status)
	{
		UserApplyStatus[] list = new UserApplyStatus[]{APPLY, VERIFY_SUCCESS, VERIFY_FAIL};
		{
			for (UserApplyStatus userApplyStatus : list)
			{
				if (userApplyStatus.getStatus() == status)
				{
					return userApplyStatus.getDesc();
				}
			}
		}
		return "未知状态";
	}

	public static Map<String, Byte> getMap()
	{
		Map<String, Byte> map = new HashMap<String, Byte>();
		map.put("APPLY", UserApplyStatus.APPLY.getStatus());
		map.put("VERIFY_SUCCESS", UserApplyStatus.VERIFY_SUCCESS.getStatus());
		map.put("VERIFY_FAIL", UserApplyStatus.VERIFY_FAIL.getStatus());
		return map;
	}
}
