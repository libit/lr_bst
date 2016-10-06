/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.models.CallLogInfo;
import com.lrcall.models.ContactInfo;
import com.lrcall.ui.customer.LoadImageTask;
import com.lrcall.ui.customer.LocalTools;
import com.lrcall.utils.DateTimeTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class ContactsAndCallLogsAdapter extends BaseAdapter
{
	protected final Context context;
	protected final List<ContactInfo> contactInfoList;
	protected final List<CallLogInfo> callLogInfoList;
	protected final IContactsAdapterItemClick contactsAdapterItemClicked;
	protected final ICallLogsAdapterItemClicked callLogsAdapterItemClicked;

	public ContactsAndCallLogsAdapter(Context context, List<ContactInfo> contactInfoList, List<CallLogInfo> callLogInfoList, IContactsAdapterItemClick contactsAdapterItemClicked, ICallLogsAdapterItemClicked callLogsAdapterItemClicked)
	{
		this.context = context;
		this.contactInfoList = contactInfoList;
		this.callLogInfoList = callLogInfoList;
		this.contactsAdapterItemClicked = contactsAdapterItemClicked;
		this.callLogsAdapterItemClicked = callLogsAdapterItemClicked;
	}

	@Override
	public int getCount()
	{
		int count1 = contactInfoList != null ? contactInfoList.size() : 0;
		int count2 = callLogInfoList != null ? callLogInfoList.size() : 0;
		return count1 + count2;
	}

	@Override
	public Object getItem(int position)
	{
		int count1 = contactInfoList != null ? contactInfoList.size() : 0;
		int count2 = callLogInfoList != null ? callLogInfoList.size() : 0;
		if (position < count1)
		{
			return contactInfoList.get(position);
		}
		else if (position < count1 + count2)
		{
			return contactInfoList.get(position);
		}
		else
		{
			return null;
		}
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		int count1 = contactInfoList != null ? contactInfoList.size() : 0;
		int count2 = callLogInfoList != null ? callLogInfoList.size() : 0;
		if (position < count1)
		{
			final ContactViewHolder holder = new ContactViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_contact, null);
			holder.ivHeader = (ImageView) convertView.findViewById(R.id.contact_picture);
			holder.tvName = (TextView) convertView.findViewById(R.id.contact_name);
			holder.tvNumber = (TextView) convertView.findViewById(R.id.contact_number);
			convertView.setTag(holder);
			final ContactInfo contactInfo = contactInfoList.get(position);
			holder.tvName.setText(contactInfo.getName());
			if (contactInfo != null && contactInfo.getPhoneInfoList() != null && contactInfo.getPhoneInfoList().size() > 0)
			{
				String number = contactInfo.getPhoneInfoList().get(0).getNumber();
				holder.tvNumber.setText(number);
				LocalTools.setLocal(context, holder.tvNumber, number, true);
			}
			LoadImageTask.loadBitmap(contactInfo.getContactId(), holder.ivHeader);
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (contactsAdapterItemClicked != null)
					{
						contactsAdapterItemClicked.onItemClicked(contactInfo);
					}
				}
			});
		}
		else if (position < count1 + count2)
		{
			CallLogsAdapter.CallLogViewHolder callLogHolder = null;
			//			if (convertView != null)
			//			{
			//				callLogHolder = (CallLogsAdapter.CallLogHolder) convertView.getTag();
			//			}
			if (callLogHolder == null)
			{
				callLogHolder = new CallLogsAdapter.CallLogViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_calllog, null);
				callLogHolder.ivCallLogType = (ImageView) convertView.findViewById(R.id.call_log_type_icon);
				callLogHolder.tvName = (TextView) convertView.findViewById(R.id.call_log_name);
				callLogHolder.tvNumber = (TextView) convertView.findViewById(R.id.call_log_number);
				callLogHolder.tvLocal = (TextView) convertView.findViewById(R.id.tv_local);
				callLogHolder.tvDuration = (TextView) convertView.findViewById(R.id.call_log_duration);
				callLogHolder.tvTime = (TextView) convertView.findViewById(R.id.call_log_date);
				convertView.setTag(callLogHolder);
				final CallLogInfo callLogInfo = callLogInfoList.get(position - count1);
				callLogHolder.ivCallLogType.setImageResource(CallLogInfo.getTypeRes(callLogInfo.getType()));
				callLogHolder.tvName.setText(callLogInfo.getName());
				callLogHolder.tvNumber.setText(callLogInfo.getNumber());
				callLogHolder.tvDuration.setText(CallLogInfo.getDurationString(callLogInfo.getDuration()));
				callLogHolder.tvTime.setText(DateTimeTools.getRelativeTimeSpanString(callLogInfo.getDate()));
				convertView.findViewById(R.id.v_call).setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if (callLogsAdapterItemClicked != null)
						{
							callLogsAdapterItemClicked.onCallClicked(callLogInfo);
						}
					}
				});
				convertView.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if (callLogsAdapterItemClicked != null)
						{
							callLogsAdapterItemClicked.onItemClicked(callLogInfo);
						}
					}
				});
				LocalTools.setLocal(context, callLogHolder.tvLocal, callLogInfo.getNumber(), true);
			}
		}
		return convertView;
	}

	public interface IContactsAdapterItemClick
	{
		void onItemClicked(ContactInfo contactInfo);
	}

	public interface ICallLogsAdapterItemClicked
	{
		void onItemClicked(CallLogInfo callLogInfo);

		void onCallClicked(CallLogInfo callLogInfo);
	}

	public static class ContactViewHolder
	{
		public Long contactId;
		public ImageView ivHeader;
		public TextView tvName;
		public TextView tvNumber;
	}
}
