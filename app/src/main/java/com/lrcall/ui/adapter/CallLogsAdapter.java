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
import com.lrcall.utils.DateTimeTools;
import com.lrcall.utils.LocalTools;
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class CallLogsAdapter extends BaseUserAdapter<CallLogInfo>
{
	protected final IItemClick iItemClick;

	public CallLogsAdapter(Context context, List<CallLogInfo> list, IItemClick iItemClick)
	{
		super(context, list);
		this.iItemClick = iItemClick;
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
		try
		{
			final CallLogInfo callLogInfo = list.get(position);
			String name = callLogInfo.getName();
			if (StringTools.isNull(name))
			{
				name = "未知号码";
			}
			else if (name.startsWith("("))
			{
				name = "未知号码" + name;
			}
			String number = callLogInfo.getNumber();
			viewHolder.ivType.setImageResource(CallLogInfo.getTypeRes(callLogInfo.getType()));
			viewHolder.tvName.setText(name);
			viewHolder.tvNumber.setText(number);
			viewHolder.tvDuration.setText(CallLogInfo.getDurationString(callLogInfo.getDuration()));
			viewHolder.tvTime.setText(DateTimeTools.getRelativeTimeSpanString(callLogInfo.getDate()));
			if (iItemClick != null)
			{
				convertView.findViewById(R.id.v_call).setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						iItemClick.onCallClicked(callLogInfo);
					}
				});
				convertView.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						iItemClick.onItemClicked(callLogInfo);
					}
				});
			}
			LocalTools.setLocal(context, viewHolder.tvLocal, number, true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return convertView;
	}

	public interface IItemClick
	{
		void onItemClicked(CallLogInfo callLogInfo);

		void onCallClicked(CallLogInfo callLogInfo);
	}

	public static class CallLogViewHolder
	{
		public ImageView ivType;
		public TextView tvName;
		public TextView tvNumber;
		public TextView tvLocal;
		public TextView tvDuration;
		public TextView tvTime;

		public void viewInit(View convertView)
		{
			ivType = (ImageView) convertView.findViewById(R.id.call_log_type_icon);
			tvName = (TextView) convertView.findViewById(R.id.call_log_name);
			tvNumber = (TextView) convertView.findViewById(R.id.call_log_number);
			tvLocal = (TextView) convertView.findViewById(R.id.tv_local);
			tvDuration = (TextView) convertView.findViewById(R.id.call_log_duration);
			tvTime = (TextView) convertView.findViewById(R.id.call_log_date);
		}

		public void clear()
		{
			ivType.setImageBitmap(null);
			tvName.setText("");
			tvNumber.setText("");
			tvLocal.setText("");
			tvDuration.setText("");
			tvTime.setText("");
		}
	}
}
