/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package org.greenrobot.eventbus;

import java.util.ArrayList;
import java.util.List;

final class PendingPost
{
	private final static List<PendingPost> pendingPostPool = new ArrayList<PendingPost>();
	Object event;
	Subscription subscription;
	PendingPost next;

	private PendingPost(Object event, Subscription subscription)
	{
		this.event = event;
		this.subscription = subscription;
	}

	static PendingPost obtainPendingPost(Subscription subscription, Object event)
	{
		synchronized (pendingPostPool)
		{
			int size = pendingPostPool.size();
			if (size > 0)
			{
				PendingPost pendingPost = pendingPostPool.remove(size - 1);
				pendingPost.event = event;
				pendingPost.subscription = subscription;
				pendingPost.next = null;
				return pendingPost;
			}
		}
		return new PendingPost(event, subscription);
	}

	static void releasePendingPost(PendingPost pendingPost)
	{
		pendingPost.event = null;
		pendingPost.subscription = null;
		pendingPost.next = null;
		synchronized (pendingPostPool)
		{
			// Don't let the pool grow indefinitely
			if (pendingPostPool.size() < 10000)
			{
				pendingPostPool.add(pendingPost);
			}
		}
	}
}