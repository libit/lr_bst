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
import com.lrcall.appbst.models.UserBankInfo;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class UserBankAdapter extends BaseUserAdapter<UserBankInfo>
{
	protected final IItemClicked iItemClicked;

	public UserBankAdapter(Context context, List<UserBankInfo> list, IItemClicked iItemClicked)
	{
		super(context, list);
		this.iItemClicked = iItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		UserBankViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (UserBankViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_user_bank, null);
			viewHolder = new UserBankViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final UserBankInfo userBankInfo = list.get(position);
		viewHolder.tvBankName.setText(userBankInfo.getBankName());
		viewHolder.tvName.setText(userBankInfo.getCardName());
		viewHolder.tvCardId.setText(userBankInfo.getCardId());
		if (iItemClicked != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClicked.onUserBankClicked(userBankInfo);
				}
			});
		}
		return convertView;
	}

	public interface IItemClicked
	{
		void onUserBankClicked(UserBankInfo userBankInfo);
	}

	public static class UserBankViewHolder
	{
		public TextView tvBankName;
		public TextView tvName;
		public TextView tvCardId;

		public void viewInit(View convertView)
		{
			tvBankName = (TextView) convertView.findViewById(R.id.tv_bankname);
			tvName = (TextView) convertView.findViewById(R.id.tv_name);
			tvCardId = (TextView) convertView.findViewById(R.id.tv_cardid);
		}

		public void clear()
		{
			tvBankName.setText("");
			tvName.setText("");
			tvCardId.setText("");
		}
	}
}
