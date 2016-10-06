package com.lrcall.enums;

/**
 * 用户类型
 *
 * @author libit
 */
public enum UserType
{
	COMMON((byte) 0, "普通会员"), DISTRICT((byte) 10, "区代理"), CITY((byte) 20, "市代理"), PROVINCE((byte) 30, "省代理");
	private byte type;
	private String desc;

	UserType(byte type, String desc)
	{
		this.type = type;
		this.desc = desc;
	}

	public byte getType()
	{
		return type;
	}

	public void setType(byte type)
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

	public static String getDesc(byte type)
	{
		UserType[] list = new UserType[]{COMMON, DISTRICT, CITY, PROVINCE};
		{
			for (UserType userType : list)
			{
				if (userType.getType() == type)
				{
					return userType.getDesc();
				}
			}
		}
		return "未知类型";
	}

	public static byte getNextType(byte userType)
	{
		if (userType == UserType.COMMON.getType())
		{
			userType = UserType.DISTRICT.getType();
		}
		else if (userType == UserType.DISTRICT.getType())
		{
			userType = UserType.CITY.getType();
		}
		else if (userType == UserType.CITY.getType())
		{
			userType = UserType.PROVINCE.getType();
		}
		else
		{
			return (byte) -1;
		}
		return userType;
	}
}
