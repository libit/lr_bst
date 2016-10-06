/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;

import com.lrcall.models.FuncInfo;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public abstract class BaseFuncsAdapter extends BaseUserAdapter<FuncInfo>
{
	protected final IFuncsAdapterItemClicked funcsAdapterItemClicked;

	public BaseFuncsAdapter(Context context, List<FuncInfo> list, IFuncsAdapterItemClicked funcsAdapterItemClicked)
	{
		super(context, list);
		this.funcsAdapterItemClicked = funcsAdapterItemClicked;
	}

	public interface IFuncsAdapterItemClicked
	{
		void onFuncClicked(FuncInfo funcInfo);
	}
}
