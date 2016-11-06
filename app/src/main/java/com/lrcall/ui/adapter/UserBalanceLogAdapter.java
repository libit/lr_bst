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
import com.lrcall.appbst.models.UserBalanceLogInfo;
import com.lrcall.utils.DateTimeTools;
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class UserBalanceLogAdapter extends BaseUserAdapter<UserBalanceLogInfo>
{
	protected final IItemClicked iItemClicked;

	public UserBalanceLogAdapter(Context context, List<UserBalanceLogInfo> list, IItemClicked iItemClicked)
	{
		super(context, list);
		this.iItemClicked = iItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		UserBalanceLogViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (UserBalanceLogViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_user_balance_log, null);
			viewHolder = new UserBalanceLogViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final UserBalanceLogInfo userBalanceLogInfo = list.get(position);
		viewHolder.tvName.setText(userBalanceLogInfo.getContent());
		viewHolder.tvPrice.setText("￥" + StringTools.getPrice(userBalanceLogInfo.getAmount()));
		viewHolder.tvDate.setText(DateTimeTools.getDateTimeString(userBalanceLogInfo.getAddDateLong()));
		if (iItemClicked != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClicked.onUserBalanceLogClicked(userBalanceLogInfo);
				}
			});
		}
		return convertView;
	}

	public interface IItemClicked
	{
		void onUserBalanceLogClicked(UserBalanceLogInfo userBalanceLogInfo);
	}

	public static class UserBalanceLogViewHolder
	{
		public TextView tvName;
		public TextView tvPrice;
		public TextView tvStatus;
		public TextView tvDate;

		public void viewInit(View convertView)
		{
			tvName = (TextView) convertView.findViewById(R.id.tv_content);
			tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
			tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
			tvDate = (TextView) convertView.findViewById(R.id.tv_date);
		}

		public void clear()
		{
			tvName.setText("");
			tvPrice.setText("");
			tvStatus.setText("");
			tvDate.setText("");
		}
	}
}
