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
	protected final IUserBalanceLogAdapterItemClicked iUserBalanceLogAdapterItemClicked;

	public UserBalanceLogAdapter(Context context, List<UserBalanceLogInfo> list, IUserBalanceLogAdapterItemClicked iUserBalanceLogAdapterItemClicked)
	{
		super(context, list);
		this.iUserBalanceLogAdapterItemClicked = iUserBalanceLogAdapterItemClicked;
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
			viewHolder = new UserBalanceLogViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_user_balance_log, null);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_content);
			viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
			viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
			viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
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
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (iUserBalanceLogAdapterItemClicked != null)
				{
					iUserBalanceLogAdapterItemClicked.onUserBalanceLogClicked(userBalanceLogInfo);
				}
			}
		});
		return convertView;
	}

	public interface IUserBalanceLogAdapterItemClicked
	{
		void onUserBalanceLogClicked(UserBalanceLogInfo userBalanceLogInfo);
	}

	public static class UserBalanceLogViewHolder
	{
		public TextView tvName;
		public TextView tvPrice;
		public TextView tvStatus;
		public TextView tvDate;

		public void clear()
		{
			tvName.setText("");
			tvPrice.setText("");
			tvStatus.setText("");
			tvDate.setText("");
		}
	}
}
