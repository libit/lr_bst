/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package org.greenrobot.eventbus;

final class Subscription
{
	final Object subscriber;
	final SubscriberMethod subscriberMethod;
	/**
	 * Becomes false as soon as {@link EventBus#unregister(Object)} is called, which is checked by queued event delivery
	 * {@link EventBus#invokeSubscriber(PendingPost)} to prevent race conditions.
	 */
	volatile boolean active;

	Subscription(Object subscriber, SubscriberMethod subscriberMethod)
	{
		this.subscriber = subscriber;
		this.subscriberMethod = subscriberMethod;
		active = true;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof Subscription)
		{
			Subscription otherSubscription = (Subscription) other;
			return subscriber == otherSubscription.subscriber && subscriberMethod.equals(otherSubscription.subscriberMethod);
		}
		else
		{
			return false;
		}
	}

	@Override
	public int hashCode()
	{
		return subscriber.hashCode() + subscriberMethod.methodString.hashCode();
	}
}