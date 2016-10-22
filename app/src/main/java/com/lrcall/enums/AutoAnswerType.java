/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

/**
 * Created by libit on 16/7/29.
 */
public enum AutoAnswerType
{
	ANSWER_USER(0, "手动接听"), AUTO_ANSWER1(1, "自动接听1"), AUTO_ANSWER2(2, "自动接听2");
	private int type;
	private String desc;

	AutoAnswerType(int type, String desc)
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
}
