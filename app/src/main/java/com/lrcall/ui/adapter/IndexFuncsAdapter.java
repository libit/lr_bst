/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.models.FuncInfo;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class IndexFuncsAdapter extends BaseFuncsAdapter
{
	public IndexFuncsAdapter(Context context, List<FuncInfo> list, IFuncsAdapterItemClicked funcsAdapterItemClicked)
	{
		super(context, list, funcsAdapterItemClicked);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		FuncViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (FuncViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_func_index, null);
			viewHolder = new FuncViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final FuncInfo funcInfo = list.get(position);
		viewHolder.ivHead.setImageResource(funcInfo.getImgRes());
		//				BitmapCacheTools.loadBitmap(context, ivHead, funcInfo.getImgRes());
		viewHolder.tvName.setText(funcInfo.getLabel());
		if (funcsAdapterItemClicked != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					funcsAdapterItemClicked.onFuncClicked(funcInfo);
				}
			});
		}
		return convertView;
	}

	public static class FuncViewHolder
	{
		public ImageView ivHead;
		public TextView tvName;

		public void viewInit(View convertView)
		{
			ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
			tvName = (TextView) convertView.findViewById(R.id.tv_label);
		}

		public void clear()
		{
			ivHead.setImageBitmap(null);
			tvName.setText("");
		}
	}
}
