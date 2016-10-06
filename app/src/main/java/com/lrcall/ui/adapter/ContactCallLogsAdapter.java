/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.models.CallLogInfo;
import com.lrcall.ui.customer.LocalTools;
import com.lrcall.utils.DateTimeTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class ContactCallLogsAdapter extends CallLogsAdapter
{
	public ContactCallLogsAdapter(Context context, List<CallLogInfo> list, ICallLogsAdapterItemClicked callLogsAdapterItemClicked)
	{
		super(context, list, callLogsAdapterItemClicked);
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
			viewHolder = new CallLogViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_contact_calllog, null);
			viewHolder.ivCallLogType = (ImageView) convertView.findViewById(R.id.call_log_type_icon);
			viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.call_log_number);
			viewHolder.tvDuration = (TextView) convertView.findViewById(R.id.call_log_duration);
			viewHolder.tvTime = (TextView) convertView.findViewById(R.id.call_log_date);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final CallLogInfo callLogInfo = list.get(position);
		viewHolder.ivCallLogType.setImageResource(CallLogInfo.getTypeRes(callLogInfo.getType()));
		viewHolder.tvNumber.setText(callLogInfo.getNumber());
		viewHolder.tvDuration.setText(CallLogInfo.getDurationString(callLogInfo.getDuration()));
		viewHolder.tvTime.setText(DateTimeTools.getRelativeTimeSpanString(callLogInfo.getDate()));
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
		LocalTools.setLocal(context, viewHolder.tvDuration, callLogInfo.getNumber(), true);
		return convertView;
	}
}
