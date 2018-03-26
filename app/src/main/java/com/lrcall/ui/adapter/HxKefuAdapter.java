/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.appbst.models.HXKefuInfo;
import com.lrcall.utils.DisplayTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class HxKefuAdapter extends BaseUserAdapter<HXKefuInfo>
{
	protected final IItemClick iItemClick;

	public HxKefuAdapter(Context context, List<HXKefuInfo> list, IItemClick iItemClick)
	{
		super(context, list);
		this.iItemClick = iItemClick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ProductViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (ProductViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_hx_kefu, null);
			viewHolder = new ProductViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
			convertView.setPadding(DisplayTools.dip2px(context, 5), DisplayTools.dip2px(context, 5), DisplayTools.dip2px(context, 5), DisplayTools.dip2px(context, 5));
		}
		else
		{
			viewHolder.clear();
		}
		//设置控件的值
		final HXKefuInfo hxKefuInfo = list.get(position);
		viewHolder.tvName.setText(hxKefuInfo.getNickname());
		if (iItemClick != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClick.onHXKefuClicked(hxKefuInfo);
				}
			});
		}
		return convertView;
	}

	public interface IItemClick
	{
		void onHXKefuClicked(HXKefuInfo kefuInfo);
	}

	public static class ProductViewHolder
	{
		public TextView tvName;

		public void viewInit(View convertView)
		{
			tvName = (TextView) convertView.findViewById(R.id.tv_name);
		}

		public void clear()
		{
			tvName.setText("");
		}
	}
}
