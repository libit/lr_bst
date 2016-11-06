/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.appbst.models.UserAddressInfo;
import com.lrcall.enums.AddressStatus;
import com.lrcall.ui.ActivityAddressEdit;
import com.lrcall.utils.ConstValues;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class AddressAdapter extends BaseUserAdapter<UserAddressInfo>
{
	private final IItemClick iItemClick;

	public AddressAdapter(Context context, List<UserAddressInfo> list, IItemClick iItemClick)
	{
		super(context, list);
		this.iItemClick = iItemClick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		AddressViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (AddressViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_address, null);
			viewHolder = new AddressViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final UserAddressInfo userAddressInfo = list.get(position);
		viewHolder.tvName.setText(userAddressInfo.getName());
		viewHolder.tvNumber.setText(userAddressInfo.getNumber());
		viewHolder.tvAddress.setText(userAddressInfo.getProvince() + " " + userAddressInfo.getCity() + " " + userAddressInfo.getDistrict() + " " + userAddressInfo.getAddress());
		if (userAddressInfo.getStatus() == AddressStatus.DEFAULT.getStatus())
		{
			viewHolder.ivDefault.setVisibility(View.VISIBLE);
		}
		convertView.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(context, ActivityAddressEdit.class);
				intent.putExtra(ConstValues.DATA_ADDRESS_ID, userAddressInfo.getAddressId());
				context.startActivity(intent);
			}
		});
		if (iItemClick != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClick.onAddressClicked(v, userAddressInfo);
				}
			});
		}
		return convertView;
	}

	public interface IItemClick
	{
		void onAddressClicked(View v, UserAddressInfo userAddressInfo);
	}

	public static class AddressViewHolder
	{
		public ImageView ivDefault;
		public TextView tvName;
		public TextView tvNumber;
		public TextView tvAddress;

		public void viewInit(View convertView)
		{
			tvName = (TextView) convertView.findViewById(R.id.tv_name);
			tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
			tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
			ivDefault = (ImageView) convertView.findViewById(R.id.iv_default);
		}

		public void clear()
		{
			ivDefault.setVisibility(View.GONE);
			tvName.setText("");
			tvNumber.setText("");
			tvAddress.setText("");
		}
	}
}
