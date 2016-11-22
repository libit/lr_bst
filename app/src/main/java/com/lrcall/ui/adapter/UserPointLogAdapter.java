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
import com.lrcall.appbst.models.PointLogInfo;
import com.lrcall.utils.DateTimeTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class UserPointLogAdapter extends BaseUserAdapter<PointLogInfo>
{
	protected final IItemClicked iItemClicked;

	public UserPointLogAdapter(Context context, List<PointLogInfo> list, IItemClicked iItemClicked)
	{
		super(context, list);
		this.iItemClicked = iItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		PointLogViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (PointLogViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_user_point_log, null);
			viewHolder = new PointLogViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final PointLogInfo pointLogInfo = list.get(position);
		viewHolder.tvType.setText(pointLogInfo.getLogType());
		viewHolder.tvContent.setText(pointLogInfo.getContent());
		viewHolder.tvAmount.setText("" + pointLogInfo.getAmount());
		viewHolder.tvDate.setText(DateTimeTools.getDateTimeString(pointLogInfo.getAddDateLong()));
		if (iItemClicked != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClicked.onPointLogInfoClicked(pointLogInfo);
				}
			});
		}
		return convertView;
	}

	public interface IItemClicked
	{
		void onPointLogInfoClicked(PointLogInfo pointLogInfo);
	}

	public static class PointLogViewHolder
	{
		public TextView tvType;
		public TextView tvContent;
		public TextView tvAmount;
		public TextView tvDate;

		public void viewInit(View convertView)
		{
			tvType = (TextView) convertView.findViewById(R.id.tv_type);
			tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			tvAmount = (TextView) convertView.findViewById(R.id.tv_price);
			tvDate = (TextView) convertView.findViewById(R.id.tv_date);
		}

		public void clear()
		{
			tvType.setText("");
			tvContent.setText("");
			tvAmount.setText("");
			tvDate.setText("");
		}
	}
}
