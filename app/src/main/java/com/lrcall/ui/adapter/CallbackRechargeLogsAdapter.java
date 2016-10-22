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
import com.lrcall.appbst.models.CallbackRechargeLogInfo;
import com.lrcall.enums.CallbackRechargeType;
import com.lrcall.utils.DateTimeTools;

import java.util.List;

import static com.lrcall.appbst.R.id.tv_remark;

/**
 * Created by libit on 16/4/30.
 */
public class CallbackRechargeLogsAdapter extends BaseUserAdapter<CallbackRechargeLogInfo>
{
	protected final ICallbackRechargeLogsAdapterItemClicked iCallbackRechargeLogsAdapterItemClicked;

	public CallbackRechargeLogsAdapter(Context context, List<CallbackRechargeLogInfo> list, ICallbackRechargeLogsAdapterItemClicked callbackRechargeLogsAdapterItemClicked)
	{
		super(context, list);
		this.iCallbackRechargeLogsAdapterItemClicked = callbackRechargeLogsAdapterItemClicked;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_callback_recharge_log, null);
			viewHolder = new CallLogViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final CallbackRechargeLogInfo callbackRechargeLogInfo = list.get(position);
		viewHolder.tvType.setText("充值类型:" + CallbackRechargeType.getTypeDesc(callbackRechargeLogInfo.getRechargeType()));
		viewHolder.tvAmount.setText("充值金额:" + callbackRechargeLogInfo.getAmount() + "(充值卡充值默认为0)");
		viewHolder.tvRechargeDate.setText("充值时间:" + DateTimeTools.getRelativeTimeSpanString(callbackRechargeLogInfo.getRechargeDateLong()));
		viewHolder.tvRemark.setText("备注:" + callbackRechargeLogInfo.getRemark());
		if (iCallbackRechargeLogsAdapterItemClicked != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iCallbackRechargeLogsAdapterItemClicked.onItemClicked(callbackRechargeLogInfo);
				}
			});
		}
		return convertView;
	}

	public interface ICallbackRechargeLogsAdapterItemClicked
	{
		void onItemClicked(CallbackRechargeLogInfo callbackRechargeLogInfo);
	}

	public static class CallLogViewHolder
	{
		public TextView tvType;
		public TextView tvAmount;
		public TextView tvRechargeDate;
		public TextView tvRemark;

		public void viewInit(View convertView)
		{
			tvType = (TextView) convertView.findViewById(R.id.tv_type);
			tvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
			tvRechargeDate = (TextView) convertView.findViewById(R.id.tv_recharge_date);
			tvRemark = (TextView) convertView.findViewById(tv_remark);
		}

		public void clear()
		{
			tvType.setText("");
			tvAmount.setText("");
			tvRechargeDate.setText("");
			tvRemark.setText("");
		}
	}
}
