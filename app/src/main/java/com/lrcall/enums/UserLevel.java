package com.lrcall.enums;

/**
 * 用户级别
 *
 * @author libit
 */
public enum UserLevel
{
	L1((byte) 0, "注册会员"), L2((byte) 10, "铜牌会员"), L3((byte) 20, "银牌会员"), L4((byte) 30, "金牌会员"), L5((byte) 40, "白金会员");
	private byte level;
	private String desc;

	private UserLevel(byte level, String desc)
	{
		this.level = level;
		this.desc = desc;
	}

	public byte getLevel()
	{
		return level;
	}

	public void setLevel(byte level)
	{
		this.level = level;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public static String getLevelDesc(byte level)
	{
		UserLevel[] list = UserLevel.values();
		for (UserLevel userLevel : list)
		{
			if (userLevel.getLevel() == level)
			{
				return userLevel.getDesc();
			}
		}
		return "";
	}

	public static String getNextLevelDesc(byte level)
	{
		UserLevel[] list = UserLevel.values();
		for (UserLevel userLevel : list)
		{
			if (userLevel.getLevel() == level + 10)
			{
				return userLevel.getDesc();
			}
		}
		return "";
	}
}
