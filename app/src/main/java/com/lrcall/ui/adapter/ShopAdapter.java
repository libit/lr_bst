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
import com.lrcall.appbst.models.ShopInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.PicService;
import com.lrcall.ui.customer.DisplayTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class ShopAdapter extends BaseUserAdapter<ShopInfo>
{
	protected final IItemClicked iItemClicked;

	public ShopAdapter(Context context, List<ShopInfo> list, IItemClicked iItemClicked)
	{
		super(context, list);
		this.iItemClicked = iItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ProductStarViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (ProductStarViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_shop, null);
			viewHolder = new ProductStarViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final ShopInfo shopInfo = list.get(position);
		String picUrl = ApiConfig.getServerPicUrl(shopInfo.getPicUrl());
		PicService.ajaxGetPic(viewHolder.ivHead, picUrl, DisplayTools.getWindowWidth(MyApplication.getContext()) / 6);
		viewHolder.tvName.setText(shopInfo.getName());
		viewHolder.tvNumber.setText(shopInfo.getShopId());
		if (iItemClicked != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClicked.onShopClicked(shopInfo);
				}
			});
		}
		return convertView;
	}

	public interface IItemClicked
	{
		void onShopClicked(ShopInfo shopInfo);
	}

	public static class ProductStarViewHolder
	{
		public ImageView ivHead;
		public TextView tvName;
		public TextView tvNumber;

		public void viewInit(View convertView)
		{
			ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
			tvName = (TextView) convertView.findViewById(R.id.tv_name);
			tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
		}

		public void clear()
		{
			ivHead.setImageBitmap(null);
			tvName.setText("");
			tvNumber.setText("");
		}
	}
}
