/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.lrcall.ui.customer.LoadImageTask;

import java.util.List;

/**
 * Created by libit on 16/5/7.
 */
public abstract class BaseContactsAdapter<T> extends BaseUserAdapter<T>
{
	public BaseContactsAdapter(Context context, List<T> list)
	{
		super(context, list);
	}

	public void loadBitmap(Long cId, ImageView imageView)
	{
		LoadImageTask.loadBitmap(cId, imageView);
	}
}
