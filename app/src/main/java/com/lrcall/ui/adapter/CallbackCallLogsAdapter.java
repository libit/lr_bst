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
import com.lrcall.appbst.models.CallbackCallLogInfo;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.models.CallLogInfo;
import com.lrcall.models.ContactInfo;
import com.lrcall.ui.customer.LocalTools;
import com.lrcall.utils.DateTimeTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class CallbackCallLogsAdapter extends BaseUserAdapter<CallbackCallLogInfo>
{
	protected final ICallbackCallLogsAdapterItemClicked callLogsAdapterItemClicked;

	public CallbackCallLogsAdapter(Context context, List<CallbackCallLogInfo> list, ICallbackCallLogsAdapterItemClicked callLogsAdapterItemClicked)
	{
		super(context, list);
		this.callLogsAdapterItemClicked = callLogsAdapterItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		CallLogViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (CallLogViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_callback_calllog, null);
			viewHolder = new CallLogViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final CallbackCallLogInfo callLogInfo = list.get(position);
		String name = callLogInfo.getNumber();
		List<ContactInfo> contactInfoList = ContactsFactory.getInstance().getContactInfosByNumber(context, callLogInfo.getNumber(), false);
		if (contactInfoList != null && contactInfoList.size() > 0)
		{
			name = contactInfoList.get(0).getName();
		}
		String number = callLogInfo.getNumber();
		viewHolder.tvName.setText(name);
		viewHolder.tvNumber.setText(number);
		viewHolder.tvDuration.setText(CallLogInfo.getDurationString(callLogInfo.getDuration()));
		viewHolder.tvTime.setText(DateTimeTools.getRelativeTimeSpanString(callLogInfo.getCallDateLong()));
		if (callLogsAdapterItemClicked != null)
		{
			convertView.findViewById(R.id.v_call).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					callLogsAdapterItemClicked.onCallClicked(callLogInfo);
				}
			});
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					callLogsAdapterItemClicked.onItemClicked(callLogInfo);
				}
			});
		}
		LocalTools.setLocal(context, viewHolder.tvLocal, number, true);
		return convertView;
	}

	public interface ICallbackCallLogsAdapterItemClicked
	{
		void onItemClicked(CallbackCallLogInfo callLogInfo);

		void onCallClicked(CallbackCallLogInfo callLogInfo);
	}

	public static class CallLogViewHolder
	{
		public TextView tvName;
		public TextView tvNumber;
		public TextView tvLocal;
		public TextView tvDuration;
		public TextView tvTime;

		public void viewInit(View convertView)
		{
			tvName = (TextView) convertView.findViewById(R.id.call_log_name);
			tvNumber = (TextView) convertView.findViewById(R.id.call_log_number);
			tvLocal = (TextView) convertView.findViewById(R.id.tv_local);
			tvDuration = (TextView) convertView.findViewById(R.id.call_log_duration);
			tvTime = (TextView) convertView.findViewById(R.id.call_log_date);
		}

		public void clear()
		{
			tvName.setText("");
			tvNumber.setText("");
			tvLocal.setText("");
			tvDuration.setText("");
			tvTime.setText("");
		}
	}
}
