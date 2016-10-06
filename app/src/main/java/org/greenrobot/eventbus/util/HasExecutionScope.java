/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package org.greenrobot.eventbus.util;

public interface HasExecutionScope
{
	Object getExecutionScope();

	void setExecutionScope(Object executionScope);
}
