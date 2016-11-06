/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.events;

/**
 * Created by libit on 16/7/7.
 */
public class CallLogEvent
{
	public static final String EVENT_CALLLOG_ADD = "event_calllog_add";
	public static final String EVENT_CALLLOG_UPDATE = "event_calllog_update";
	public static final String EVENT_CALLLOG_DELETE = "event_calllog_delete";
	private final String event;

	public CallLogEvent(String event)
	{
		this.event = event;
	}

	public String getEvent()
	{
		return event;
	}
}
