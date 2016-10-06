/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package me.imid.swipebacklayout.lib.app;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

/**
 * @author Yrom
 */
public interface SwipeBackActivityBase
{
	/**
	 * @return the SwipeBackLayout associated with this activity.
	 */
	public abstract SwipeBackLayout getSwipeBackLayout();

	public abstract void setSwipeBackEnable(boolean enable);

	/**
	 * Scroll out contentView and finish the activity
	 */
	public abstract void scrollToFinishActivity();
}
