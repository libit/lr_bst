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
import com.lrcall.appbst.models.ProductSortInfo;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class CategoryAdapter extends BaseUserAdapter<ProductSortInfo>
{
	private final IItemClick iItemClick;

	public CategoryAdapter(Context context, List<ProductSortInfo> list, IItemClick iItemClick)
	{
		super(context, list);
		this.iItemClick = iItemClick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		CategoryViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (CategoryViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_category, null);
			viewHolder = new CategoryViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final ProductSortInfo productSortInfo = list.get(position);
		viewHolder.tvName.setText(productSortInfo.getName());
		if (iItemClick != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClick.onProductSortClicked(v, productSortInfo);
				}
			});
		}
		return convertView;
	}

	public interface IItemClick
	{
		void onProductSortClicked(View view, ProductSortInfo productSortInfo);
	}

	public class CategoryViewHolder
	{
		public TextView tvName;

		public void viewInit(View convertView)
		{
			tvName = (TextView) convertView.findViewById(R.id.tv_label);
		}

		public void clear()
		{
			tvName.setText("");
		}
	}
}
