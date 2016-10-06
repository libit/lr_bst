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
import com.lrcall.models.ContactInfo;
import com.lrcall.ui.customer.LocalTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class ContactNumbersAdapter extends BaseUserAdapter<ContactInfo.PhoneInfo>
{
	private final IContactNumberAdapterItemClicked contactNumberAdapterItemClicked;

	public ContactNumbersAdapter(Context context, List<ContactInfo.PhoneInfo> list, IContactNumberAdapterItemClicked contactNumberAdapterItemClicked)
	{
		super(context, list);
		this.contactNumberAdapterItemClicked = contactNumberAdapterItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ContactNumberViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (ContactNumberViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			viewHolder = new ContactNumberViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_contact_number, null);
			viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
			viewHolder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
			viewHolder.tvLocal = (TextView) convertView.findViewById(R.id.tv_local);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final ContactInfo.PhoneInfo phoneInfo = list.get(position);
		final String number = phoneInfo.getNumber();
		String type = phoneInfo.getType();
		viewHolder.tvNumber.setText(number);
		viewHolder.tvType.setText(type);
		convertView.findViewById(R.id.v_call).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (contactNumberAdapterItemClicked != null)
				{
					contactNumberAdapterItemClicked.onCallClicked(phoneInfo);
				}
			}
		});
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (contactNumberAdapterItemClicked != null)
				{
					contactNumberAdapterItemClicked.onItemClicked(phoneInfo);
				}
			}
		});
		LocalTools.setLocal(context, viewHolder.tvLocal, number, true);
		return convertView;
	}

	public interface IContactNumberAdapterItemClicked
	{
		void onItemClicked(ContactInfo.PhoneInfo phoneInfo);

		void onCallClicked(ContactInfo.PhoneInfo phoneInfo);
	}

	public static class ContactNumberViewHolder
	{
		public TextView tvNumber;
		public TextView tvType;
		public TextView tvLocal;

		public void clear()
		{
			tvNumber.setText("");
			tvType.setText("");
			tvLocal.setText("");
		}
	}
}