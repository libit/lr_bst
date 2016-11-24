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
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class PayAdapter extends BaseUserAdapter<PayInfo>
{
	protected final IItemClick iItemClick;

	public PayAdapter(Context context, List<PayInfo> list, IItemClick iItemClick)
	{
		super(context, list);
		this.iItemClick = iItemClick;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
			viewHolder = new PayViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final PayInfo payInfo = list.get(position);
		viewHolder.tvName.setText(payInfo.getName());
		if (!StringTools.isNull(payInfo.getPicUrl()))
		{
			PicService.ajaxGetPic(viewHolder.ivLogo, ApiConfig.getServerPicUrl(payInfo.getPicUrl()), DisplayTools.getWindowWidth(MyApplication.getContext()) / 5);
		}
		else
		{
			viewHolder.ivLogo.setImageBitmap(null);
		}
		if (iItemClick != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClick.onPayClicked(v, payInfo);
				}
			});
		}
		return convertView;
	}

	public interface IItemClick
	{
		void onPayClicked(View v, PayInfo payInfo);
	}

	public static class PayViewHolder
	{
		public ImageView ivLogo;
		public TextView tvName;

		public void viewInit(View convertView)
		{
			tvName = (TextView) convertView.findViewById(R.id.tv_name);
			ivLogo = (ImageView) convertView.findViewById(R.id.iv_logo);
		}

		public void clear()
		{
			ivLogo.setImageBitmap(null);
			tvName.setText("");
		}
	}
}
