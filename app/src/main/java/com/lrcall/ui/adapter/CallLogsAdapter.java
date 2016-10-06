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
public class CallLogsAdapter extends BaseUserAdapter<CallLogInfo>
{
	protected final ICallLogsAdapterItemClicked callLogsAdapterItemClicked;

	public CallLogsAdapter(Context context, List<CallLogInfo> list, ICallLogsAdapterItemClicked callLogsAdapterItemClicked)
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_calllog, null);
			viewHolder = new CallLogViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final CallLogInfo callLogInfo = list.get(position);
		String number = callLogInfo.getNumber();
		viewHolder.ivCallLogType.setImageResource(CallLogInfo.getTypeRes(callLogInfo.getType()));
		viewHolder.tvName.setText(callLogInfo.getName());
		viewHolder.tvNumber.setText(number);
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
		LocalTools.setLocal(context, viewHolder.tvLocal, number, true);
		return convertView;
	}

	public interface ICallLogsAdapterItemClicked
	{
		void onItemClicked(CallLogInfo callLogInfo);

		void onCallClicked(CallLogInfo callLogInfo);
	}

	public static class CallLogViewHolder
	{
		public ImageView ivCallLogType;
		public TextView tvName;
		public TextView tvNumber;
		public TextView tvLocal;
		public TextView tvDuration;
		public TextView tvTime;

		public void viewInit(View convertView)
		{
			ivCallLogType = (ImageView) convertView.findViewById(R.id.call_log_type_icon);
			tvName = (TextView) convertView.findViewById(R.id.call_log_name);
			tvNumber = (TextView) convertView.findViewById(R.id.call_log_number);
			tvLocal = (TextView) convertView.findViewById(R.id.tv_local);
			tvDuration = (TextView) convertView.findViewById(R.id.call_log_duration);
			tvTime = (TextView) convertView.findViewById(R.id.call_log_date);
		}

		public void clear()
		{
			ivCallLogType.setImageBitmap(null);
			tvName.setText("");
			tvNumber.setText("");
			tvLocal.setText("");
			tvDuration.setText("");
			tvTime.setText("");
		}
	}
}
