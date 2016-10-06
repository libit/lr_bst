/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.events;

/**
 * Created by libit on 16/7/15.
 */
public class UserEvent
{
	public static final String EVENT_LOGINED = "event_logined";
	public static final String EVENT_LOGOUT = "event_logout";
	private String event;

	public UserEvent(String event)
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
