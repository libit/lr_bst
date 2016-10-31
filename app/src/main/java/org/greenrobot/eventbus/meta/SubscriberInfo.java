/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package org.greenrobot.eventbus.meta;

import org.greenrobot.eventbus.SubscriberMethod;

/** Base class for generated index classes created by annotation processing. */
public interface SubscriberInfo
{
	Class<?> getSubscriberClass();

	SubscriberMethod[] getSubscriberMethods();

	SubscriberInfo getSuperSubscriberInfo();

	boolean shouldCheckSuperclass();
}
