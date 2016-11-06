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
import com.lrcall.appbst.models.DataTrafficOrderInfo;
import com.lrcall.enums.DataTrafficOrderType;
import com.lrcall.utils.DateTimeTools;
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class DataTrafficOrderAdapter extends BaseUserAdapter<DataTrafficOrderInfo>
{
	protected final IItemClick iItemClick;

	public DataTrafficOrderAdapter(Context context, List<DataTrafficOrderInfo> list, IItemClick iItemClick)
	{
		super(context, list);
		this.iItemClick = iItemClick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		DataTrafficOrderViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (DataTrafficOrderViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_data_traffic_order, null);
			viewHolder = new DataTrafficOrderViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			convertView.findViewById(R.id.layout_pay_date).setVisibility(View.GONE);
			convertView.findViewById(R.id.layout_recharge_date).setVisibility(View.GONE);
			convertView.findViewById(R.id.layout_comment).setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.btn_order_pay).setVisibility(View.GONE);
			convertView.findViewById(R.id.btn_cancel_order).setVisibility(View.GONE);
			viewHolder.clear();
		}
		//设置控件的值
		final DataTrafficOrderInfo dataTrafficOrderInfo = list.get(position);
		viewHolder.tvOrderId.setText(dataTrafficOrderInfo.getOrderId());
		viewHolder.tvMoney.setText(StringTools.getPrice(dataTrafficOrderInfo.getTotalPrice()) + "元");
		viewHolder.tvNumber.setText(dataTrafficOrderInfo.getNumber());
		if (StringTools.isNull(dataTrafficOrderInfo.getComment()))
		{
			convertView.findViewById(R.id.layout_comment).setVisibility(View.GONE);
		}
		else
		{
			viewHolder.tvComment.setText(dataTrafficOrderInfo.getComment());
		}
		String validateDate = "";
		//		if (dataTrafficOrderInfo.getDataTrafficInfo().getValidateDate() != null)
		//		{
		//			validateDate = "  有效期" + dataTrafficOrderInfo.getDataTrafficInfo().getValidateDate() + "天";
		//		}
		viewHolder.tvName.setText(dataTrafficOrderInfo.getDataTrafficInfo().getName() + validateDate);
		if (dataTrafficOrderInfo.getStatus() == DataTrafficOrderType.WAIT_PAY.getStatus())
		{
			viewHolder.tvOrderStatus.setText(DataTrafficOrderType.WAIT_PAY.getDesc());
			convertView.findViewById(R.id.btn_order_pay).setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.btn_cancel_order).setVisibility(View.VISIBLE);
			if (iItemClick != null)
			{
				convertView.findViewById(R.id.btn_order_pay).setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						iItemClick.onOrderPayClicked(dataTrafficOrderInfo);
					}
				});
				convertView.findViewById(R.id.btn_cancel_order).setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						iItemClick.onOrderCancelClicked(dataTrafficOrderInfo);
					}
				});
			}
		}
		else if (dataTrafficOrderInfo.getStatus() == DataTrafficOrderType.PAYED.getStatus())
		{
			convertView.findViewById(R.id.layout_pay_date).setVisibility(View.VISIBLE);
			viewHolder.tvPayDate.setText(DateTimeTools.getDateTimeString(dataTrafficOrderInfo.getUpdateDateLong()));
			viewHolder.tvOrderStatus.setText(DataTrafficOrderType.PAYED.getDesc());
		}
		else if (dataTrafficOrderInfo.getStatus() == DataTrafficOrderType.FINISH.getStatus())
		{
			convertView.findViewById(R.id.layout_recharge_date).setVisibility(View.VISIBLE);
			viewHolder.tvRechargeDate.setText(DateTimeTools.getDateTimeString(dataTrafficOrderInfo.getUpdateDateLong()));
			viewHolder.tvOrderStatus.setText(DataTrafficOrderType.FINISH.getDesc());
		}
		else if (dataTrafficOrderInfo.getStatus() == DataTrafficOrderType.DELETED.getStatus())
		{
			viewHolder.tvOrderStatus.setText(DataTrafficOrderType.DELETED.getDesc());
		}
		if (iItemClick != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClick.onOrderClicked(dataTrafficOrderInfo);
				}
			});
		}
		return convertView;
	}

	public interface IItemClick
	{
		void onOrderClicked(DataTrafficOrderInfo dataTrafficOrderInfo);

		void onOrderPayClicked(DataTrafficOrderInfo dataTrafficOrderInfo);

		void onOrderCancelClicked(DataTrafficOrderInfo dataTrafficOrderInfo);
	}

	public static class DataTrafficOrderViewHolder
	{
		public TextView tvOrderId;
		public TextView tvName;
		public TextView tvMoney;
		public TextView tvNumber;
		public TextView tvComment;
		public TextView tvPayDate;
		public TextView tvRechargeDate;
		public TextView tvOrderStatus;

		public void viewInit(View convertView)
		{
			tvOrderId = (TextView) convertView.findViewById(R.id.tv_order_id);
			tvOrderStatus = (TextView) convertView.findViewById(R.id.tv_order_status);
			tvName = (TextView) convertView.findViewById(R.id.tv_name);
			tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
			tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
			tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
			tvPayDate = (TextView) convertView.findViewById(R.id.tv_pay_date);
			tvRechargeDate = (TextView) convertView.findViewById(R.id.tv_recharge_date);
		}

		public void clear()
		{
			tvOrderId.setText("");
			tvOrderStatus.setText("");
			tvName.setText("");
			tvMoney.setText("");
			tvNumber.setText("");
			tvComment.setText("");
			tvPayDate.setText("");
			tvRechargeDate.setText("");
		}
	}
}
