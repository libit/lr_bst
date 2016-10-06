/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志记录级别，级别越高记录的日志越多
 * Created by libit on 16/7/12.
 */
public enum LogLevel
{
	LEVEL_0(0, "不记录日志"), LEVEL_1(1, "仅记录出错日志"), LEVEL_2(2, "记录出错和警告日志"), LEVEL_3(3, "记录出错、警告和信息日志"), LEVEL_4(4, "记录出错、警告、信息和调试日志"), LEVEL_5(5, "记录所有日志");
	private final int level;
	private final String desc;

	LogLevel(int level, String desc)
	{
		this.level = level;
		this.desc = desc;
	}

	public int getLevel()
	{
		return level;
	}

	public String getDesc()
	{
		return desc;
	}

	public static List<LogLevel> getAllLevels()
	{
		List<LogLevel> levels = new ArrayList<>();
		levels.add(LEVEL_0);
		levels.add(LEVEL_1);
		levels.add(LEVEL_2);
		levels.add(LEVEL_3);
		levels.add(LEVEL_4);
		levels.add(LEVEL_5);
		return levels;
	}
}
