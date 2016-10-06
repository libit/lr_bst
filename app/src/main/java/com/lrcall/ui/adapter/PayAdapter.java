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
import com.lrcall.appbst.models.PayInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.PicService;
import com.lrcall.ui.customer.DisplayTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class PayAdapter extends BaseUserAdapter<PayInfo>
{
	protected final IPayAdapterItemClicked iPayAdapterItemClicked;

	public PayAdapter(Context context, List<PayInfo> list, IPayAdapterItemClicked iPayAdapterItemClicked)
	{
		super(context, list);
		this.iPayAdapterItemClicked = iPayAdapterItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		PayViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (PayViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			viewHolder = new PayViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.ivLogo = (ImageView) convertView.findViewById(R.id.iv_logo);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final PayInfo payInfo = list.get(position);
		viewHolder.tvName.setText(payInfo.getName());
		PicService.ajaxGetPic(viewHolder.ivLogo, ApiConfig.getServerPicUrl(payInfo.getPicUrl()), DisplayTools.getWindowWidth(MyApplication.getContext()) / 5);
		if (iPayAdapterItemClicked != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iPayAdapterItemClicked.onPayClicked(v, payInfo);
				}
			});
		}
		return convertView;
	}

	public interface IPayAdapterItemClicked
	{
		void onPayClicked(View v, PayInfo payInfo);
	}

	public static class PayViewHolder
	{
		public ImageView ivLogo;
		public TextView tvName;

		public void clear()
		{
			ivLogo.setImageBitmap(null);
			tvName.setText("");
		}
	}
}
