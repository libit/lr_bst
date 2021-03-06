package com.lrcall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 性别类型
 *
 * @author libit
 */
public enum SexType
{
	MALE((byte) 1, "男"), FEMALE((byte) 2, "女"), UNKNOWN((byte) 3, "未知");
	private byte sex;
	private String desc;

	 SexType(byte sex, String desc)
	{
		this.sex = sex;
		this.desc = desc;
	}

	public byte getSex()
	{
		return sex;
	}

	public void setSex(byte sex)
	{
		this.sex = sex;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public static Map<Byte, String> getMap()
	{
		Map<Byte, String> map = new HashMap<>();
		map.put(MALE.getSex(), MALE.getDesc());
		map.put(FEMALE.getSex(), FEMALE.getDesc());
		map.put(UNKNOWN.getSex(), UNKNOWN.getDesc());
		return map;
	}
}
