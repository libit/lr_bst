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

import com.lrcall.appbst.MyApplication;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ProductSortInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.PicService;
import com.lrcall.ui.customer.DisplayTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class CategorySortAdapter extends BaseUserAdapter<ProductSortInfo>
{
	protected final IProductSortAdapterItemClicked productSortAdapterItemClicked;

	public CategorySortAdapter(Context context, List<ProductSortInfo> list, IProductSortAdapterItemClicked productSortAdapterItemClicked)
	{
		super(context, list);
		this.productSortAdapterItemClicked = productSortAdapterItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		SortViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (SortViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			viewHolder = new SortViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_category_sort, null);
			viewHolder.ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_label);
			convertView.setTag(viewHolder);
			ViewGroup.LayoutParams layoutParams = viewHolder.ivHead.getLayoutParams();
			layoutParams.width = DisplayTools.getWindowWidth(context) / 3;
			layoutParams.height = layoutParams.width * 2 / 3;
		}
		else
		{
			viewHolder.clear();
		}
		final ProductSortInfo productSortInfo = list.get(position);
		String url = ApiConfig.getServerPicUrl(productSortInfo.getPicId());
		PicService.ajaxGetPic(viewHolder.ivHead, url, DisplayTools.getWindowWidth(MyApplication.getContext()) / 4);
		viewHolder.tvName.setText(productSortInfo.getName());
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (productSortAdapterItemClicked != null)
				{
					productSortAdapterItemClicked.onProductSortClicked(productSortInfo);
				}
			}
		});
		return convertView;
	}

	public interface IProductSortAdapterItemClicked
	{
		void onProductSortClicked(ProductSortInfo productSortInfo);
	}

	public class SortViewHolder
	{
		public ImageView ivHead;
		public TextView tvName;

		public void clear()
		{
			ivHead.setImageBitmap(null);
			tvName.setText("");
		}
	}
}
