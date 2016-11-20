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
import com.lrcall.appbst.models.UserInfo;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class ReferrerUserAdapter extends BaseUserAdapter<UserInfo>
{
	protected final IItemClick iItemClick;

	public ReferrerUserAdapter(Context context, List<UserInfo> list, IItemClick iItemClick)
	{
		super(context, list);
		this.iItemClick = iItemClick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		UserViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (UserViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_referrer_user, null);
			viewHolder = new UserViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final UserInfo userInfo = list.get(position);
		String name = userInfo.getName();
		String userId = userInfo.getUserId();
		viewHolder.tvName.setText(name);
		viewHolder.tvUserId.setText(userId);
		if (iItemClick != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClick.onItemClicked(userInfo);
				}
			});
		}
		return convertView;
	}

	public interface IItemClick
	{
		void onItemClicked(UserInfo userInfo);
	}

	public static class UserViewHolder
	{
		public TextView tvName;
		public TextView tvUserId;

		public void viewInit(View convertView)
		{
			tvName = (TextView) convertView.findViewById(R.id.tv_name);
			tvUserId = (TextView) convertView.findViewById(R.id.tv_user_id);
		}

		public void clear()
		{
			tvName.setText("");
			tvUserId.setText("");
		}
	}
}
