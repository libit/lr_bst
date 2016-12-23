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
import com.lrcall.appbst.models.UserWithdrawInfo;
import com.lrcall.enums.WithdrawStatus;
import com.lrcall.utils.DateTimeTools;
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class UserWithdrawAdapter extends BaseUserAdapter<UserWithdrawInfo>
{
	protected final IItemClicked iItemClicked;

	public UserWithdrawAdapter(Context context, List<UserWithdrawInfo> list, IItemClicked iItemClicked)
	{
		super(context, list);
		this.iItemClicked = iItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		WithdrawViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (WithdrawViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_withdraw, null);
			viewHolder = new WithdrawViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final UserWithdrawInfo userWithdrawInfo = list.get(position);
		viewHolder.tvBankName.setText(userWithdrawInfo.getBankName());
		viewHolder.tvName.setText(userWithdrawInfo.getCardName());
		viewHolder.tvCardId.setText(userWithdrawInfo.getCardId());
		viewHolder.tvStatus.setText(WithdrawStatus.getDesc(userWithdrawInfo.getStatus()));
		viewHolder.tvPrice.setText("￥" + StringTools.getPrice(userWithdrawInfo.getAmount()));
		viewHolder.tvDate.setText(DateTimeTools.getDateTimeString(userWithdrawInfo.getAddDateLong()));
		if (iItemClicked != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClicked.onWithdrawClicked(userWithdrawInfo);
				}
			});
		}
		return convertView;
	}

	public interface IItemClicked
	{
		void onWithdrawClicked(UserWithdrawInfo userWithdrawInfo);
	}

	public static class WithdrawViewHolder
	{
		public TextView tvBankName;
		public TextView tvName;
		public TextView tvCardId;
		public TextView tvPrice;
		public TextView tvStatus;
		public TextView tvDate;

		public void viewInit(View convertView)
		{
			tvBankName = (TextView) convertView.findViewById(R.id.tv_bankname);
			tvName = (TextView) convertView.findViewById(R.id.tv_name);
			tvCardId = (TextView) convertView.findViewById(R.id.tv_cardid);
			tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
			tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
			tvDate = (TextView) convertView.findViewById(R.id.tv_date);
		}

		public void clear()
		{
			tvBankName.setText("");
			tvName.setText("");
			tvCardId.setText("");
			tvPrice.setText("");
			tvStatus.setText("");
			tvDate.setText("");
		}
	}
}
