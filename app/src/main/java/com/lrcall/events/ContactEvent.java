/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.events;

import com.lrcall.models.ContactInfo;

/**
 * Created by libit on 16/7/7.
 */
public class ContactEvent
{
	public static final String EVENT_CONTACT_ADD = "event_contact_add";
	public static final String EVENT_CONTACT_UPDATE = "event_contact_update";
	public static final String EVENT_CONTACT_DELETE = "event_contact_delete";
	private final String eventType;
	private ContactInfo contactInfo;

	public ContactEvent(String eventType)
	{
		this.eventType = eventType;
	}

	public ContactEvent(String eventType, ContactInfo contactInfo)
	{
		this.eventType = eventType;
		this.contactInfo = contactInfo;
	}

	public String getEventType()
	{
		return eventType;
	}

	public ContactInfo getContactInfo()
	{
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo)
	{
		this.contactInfo = contactInfo;
	}
}
