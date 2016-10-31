/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package org.greenrobot.eventbus;

/**
 * Posts events in background.
 *
 * @author Markus
 */
class AsyncPoster implements Runnable
{
	private final PendingPostQueue queue;
	private final EventBus eventBus;

	AsyncPoster(EventBus eventBus)
	{
		this.eventBus = eventBus;
		queue = new PendingPostQueue();
	}

	public void enqueue(Subscription subscription, Object event)
	{
		PendingPost pendingPost = PendingPost.obtainPendingPost(subscription, event);
		queue.enqueue(pendingPost);
		eventBus.getExecutorService().execute(this);
	}

	@Override
	public void run()
	{
		PendingPost pendingPost = queue.poll();
		if (pendingPost == null)
		{
			throw new IllegalStateException("No pending post available");
		}
		eventBus.invokeSubscriber(pendingPost);
	}
}
