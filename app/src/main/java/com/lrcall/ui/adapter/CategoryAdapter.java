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
	private final ICategoryProductSortAdapterItemClicked categoryProductSortAdapterItemClicked;

	public CategoryAdapter(Context context, List<ProductSortInfo> list, ICategoryProductSortAdapterItemClicked categoryProductSortAdapterItemClicked)
	{
		super(context, list);
		this.categoryProductSortAdapterItemClicked = categoryProductSortAdapterItemClicked;
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
			viewHolder = new CategoryViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_category, null);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_label);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final ProductSortInfo productSortInfo = list.get(position);
		viewHolder.tvName.setText(productSortInfo.getName());
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (categoryProductSortAdapterItemClicked != null)
				{
					categoryProductSortAdapterItemClicked.onProductSortClicked(v, productSortInfo);
				}
			}
		});
		//		if (position == 0)
		//		{
		//			LogcatTools.debug("getView", "convertView:" + convertView);
		//		}
		return convertView;
	}

	public interface ICategoryProductSortAdapterItemClicked
	{
		void onProductSortClicked(View view, ProductSortInfo productSortInfo);
	}

	public class CategoryViewHolder
	{
		public TextView tvName;

		public void clear()
		{
			tvName.setText("");
		}
	}
}
