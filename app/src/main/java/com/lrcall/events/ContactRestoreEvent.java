/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.events;

/**
 * Created by libit on 16/7/7.
 */
public class ContactRestoreEvent
{
	private int current;
	private int total;

	public ContactRestoreEvent(int current, int total)
	{
		this.current = current;
		this.total = total;
	}

	public int getCurrent()
	{
		return current;
	}

	public int getTotal()
	{
		return total;
	}
}
