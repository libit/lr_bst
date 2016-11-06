/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.events;

/**
 * Created by libit on 16/7/15.
 */
public class OrderEvent
{
	public static final String EVENT_ORDER_ADD = "event_order_add";
	public static final String EVENT_ORDER_STATUS_CHANGED = "event_order_status_changed";
	private String event;

	public OrderEvent(String event)
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
