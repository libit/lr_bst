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

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.PointOrderInfo;
import com.lrcall.appbst.models.PointProductInfo;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PointProductService;
import com.lrcall.enums.DataTrafficOrderType;
import com.lrcall.utils.DateTimeTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class PointOrderAdapter extends BaseUserAdapter<PointOrderInfo>
{
	protected final IPointOrderAdapter iPointOrderAdapter;

	public PointOrderAdapter(Context context, List<PointOrderInfo> list, IPointOrderAdapter iPointOrderAdapter)
	{
		super(context, list);
		this.iPointOrderAdapter = iPointOrderAdapter;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		PointOrderViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (PointOrderViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			viewHolder = new PointOrderViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_point_order, null);
			viewHolder.tvOrderId = (TextView) convertView.findViewById(R.id.tv_order_id);
			viewHolder.tvOrderStatus = (TextView) convertView.findViewById(R.id.tv_order_status);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.tvPoint = (TextView) convertView.findViewById(R.id.tv_point);
			viewHolder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
			//			viewHolder.tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
			viewHolder.tvPayDate = (TextView) convertView.findViewById(R.id.tv_pay_date);
			convertView.setTag(viewHolder);
		}
		else
		{
			convertView.findViewById(R.id.layout_pay_date).setVisibility(View.GONE);
			convertView.findViewById(R.id.layout_comment).setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.btn_order_pay).setVisibility(View.GONE);
			convertView.findViewById(R.id.btn_cancel_order).setVisibility(View.GONE);
			viewHolder.clear();
		}
		//设置控件的值
		final PointOrderInfo pointOrderInfo = list.get(position);
		viewHolder.tvOrderId.setText(pointOrderInfo.getOrderId());
		viewHolder.tvPoint.setText("" + pointOrderInfo.getProductsPoint());
		viewHolder.tvMoney.setText(StringTools.getPrice(pointOrderInfo.getTotalPrice()) + "元");
		//		if (StringTools.isNull(pointOrderInfo.getComment()))
		//		{
		//			convertView.findViewById(R.id.layout_comment).setVisibility(View.GONE);
		//		}
		//		else
		//		{
		//			viewHolder.tvComment.setText(pointOrderInfo.getComment());
		//		}
		final PointOrderViewHolder holder = viewHolder;
		PointProductService pointProductService = new PointProductService(context);
		pointProductService.addDataResponse(new IAjaxDataResponse()
		{
			@Override
			public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
			{
				PointProductInfo pointProductInfo = GsonTools.getReturnObject(result, PointProductInfo.class);
				holder.tvName.setText(pointProductInfo.getName());
				return false;
			}
		});
		pointProductService.getPointProduct(pointOrderInfo.getOrderProductInfoList().get(0).getProductId(), null, false);
		if (pointOrderInfo.getStatus() == DataTrafficOrderType.WAIT_PAY.getStatus())
		{
			viewHolder.tvOrderStatus.setText(DataTrafficOrderType.WAIT_PAY.getDesc());
			convertView.findViewById(R.id.btn_order_pay).setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.btn_order_pay).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (iPointOrderAdapter != null)
					{
						iPointOrderAdapter.onOrderPayClicked(pointOrderInfo);
					}
				}
			});
			convertView.findViewById(R.id.btn_cancel_order).setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.btn_cancel_order).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (iPointOrderAdapter != null)
					{
						iPointOrderAdapter.onOrderCancelClicked(pointOrderInfo);
					}
				}
			});
		}
		else if (pointOrderInfo.getStatus() == DataTrafficOrderType.PAYED.getStatus())
		{
			convertView.findViewById(R.id.layout_pay_date).setVisibility(View.VISIBLE);
			viewHolder.tvPayDate.setText(DateTimeTools.getDateTimeString(pointOrderInfo.getUpdateDateLong()));
			viewHolder.tvOrderStatus.setText(DataTrafficOrderType.PAYED.getDesc());
		}
		else if (pointOrderInfo.getStatus() == DataTrafficOrderType.FINISH.getStatus())
		{
			viewHolder.tvOrderStatus.setText(DataTrafficOrderType.FINISH.getDesc());
		}
		else if (pointOrderInfo.getStatus() == DataTrafficOrderType.DELETED.getStatus())
		{
			viewHolder.tvOrderStatus.setText(DataTrafficOrderType.DELETED.getDesc());
		}
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (iPointOrderAdapter != null)
				{
					iPointOrderAdapter.onOrderClicked(pointOrderInfo);
				}
			}
		});
		return convertView;
	}

	public interface IPointOrderAdapter
	{
		void onOrderClicked(PointOrderInfo pointOrderInfo);

		void onOrderPayClicked(PointOrderInfo pointOrderInfo);

		void onOrderCancelClicked(PointOrderInfo pointOrderInfo);
	}

	public static class PointOrderViewHolder
	{
		public TextView tvOrderId;
		public TextView tvName;
		public TextView tvPoint;
		public TextView tvMoney;
		//		public TextView tvComment;
		public TextView tvPayDate;
		public TextView tvOrderStatus;

		public void clear()
		{
			tvOrderId.setText("");
			tvOrderStatus.setText("");
			tvName.setText("");
			tvPoint.setText("");
			tvMoney.setText("");
			//			tvComment.setText("");
			tvPayDate.setText("");
		}
	}
}
