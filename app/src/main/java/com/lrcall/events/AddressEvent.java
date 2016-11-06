/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.events;

/**
 * Created by libit on 16/7/15.
 */
public class AddressEvent
{
	public static final String EVENT_ADD = "event_add";
	public static final String EVENT_UPDATE = "event_update";
	public static final String EVENT_DELETED = "event_deleted";
	private String event;

	public AddressEvent(String event)
	{
		this.event = event;
	}

	public String getEvent()
	{
		return event;
	}

	public void setEvent(String event)
	{
		this.event = event;
	}
}
