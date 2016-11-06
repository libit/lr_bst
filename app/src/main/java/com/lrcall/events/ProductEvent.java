/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.events;

/**
 * Created by libit on 16/7/15.
 */
public class ProductEvent
{
	public static final String EVENT_STAR_ADD = "event_star_add";
	public static final String EVENT_STAR_CANCEL = "event_star_cancel";
	public static final String EVENT_HISTORY_ADD = "event_history_add";
	private String event;

	public ProductEvent(String event)
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
