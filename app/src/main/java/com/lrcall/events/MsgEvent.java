/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.events;

/**
 * Created by libit on 16/7/15.
 */
public class MsgEvent
{
	public static final String EVENT_MSG_RECEIVED = "event_msg_received";//收到普通消息
	public static final String EVENT_REFRESH_GROUP_RED_PACKET_ACTION = "EVENT_REFRESH_GROUP_RED_PACKET_ACTION";//红包回执透传消息
	public static final String EVENT_CONVERSATION_REFRESH = "EVENT_CONVERSATION_REFRESH";//刷新会话
	public static final String EVENT_CONTACT_REFRESH = "EVENT_CONTACT_REFRESH";//刷新联系人
	private String event;

	public MsgEvent(String event)
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
