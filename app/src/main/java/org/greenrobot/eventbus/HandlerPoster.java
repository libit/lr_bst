/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package org.greenrobot.eventbus;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

final class HandlerPoster extends Handler
{
	private final PendingPostQueue queue;
	private final int maxMillisInsideHandleMessage;
	private final EventBus eventBus;
	private boolean handlerActive;

	HandlerPoster(EventBus eventBus, Looper looper, int maxMillisInsideHandleMessage)
	{
		super(looper);
		this.eventBus = eventBus;
		this.maxMillisInsideHandleMessage = maxMillisInsideHandleMessage;
		queue = new PendingPostQueue();
	}

	void enqueue(Subscription subscription, Object event)
	{
		PendingPost pendingPost = PendingPost.obtainPendingPost(subscription, event);
		synchronized (this)
		{
			queue.enqueue(pendingPost);
			if (!handlerActive)
			{
				handlerActive = true;
				if (!sendMessage(obtainMessage()))
				{
					throw new EventBusException("Could not send handler message");
				}
			}
		}
	}

	@Override
	public void handleMessage(Message msg)
	{
		boolean rescheduled = false;
		try
		{
			long started = SystemClock.uptimeMillis();
			while (true)
			{
				PendingPost pendingPost = queue.poll();
				if (pendingPost == null)
				{
					synchronized (this)
					{
						// Check again, this time in synchronized
						pendingPost = queue.poll();
						if (pendingPost == null)
						{
							handlerActive = false;
							return;
						}
					}
				}
				eventBus.invokeSubscriber(pendingPost);
				long timeInMethod = SystemClock.uptimeMillis() - started;
				if (timeInMethod >= maxMillisInsideHandleMessage)
				{
					if (!sendMessage(obtainMessage()))
					{
						throw new EventBusException("Could not send handler message");
					}
					rescheduled = true;
					return;
				}
			}
		}
		finally
		{
			handlerActive = rescheduled;
		}
	}
}