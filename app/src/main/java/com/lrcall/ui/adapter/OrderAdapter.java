/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.OrderInfo;
import com.lrcall.appbst.models.OrderProductInfo;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.OrderProductCommentService;
import com.lrcall.enums.OrderType;
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class OrderAdapter extends BaseUserAdapter<OrderInfo>
{
	protected final IOrderAdapter iOrderAdapter;

	public OrderAdapter(Context context, List<OrderInfo> list, IOrderAdapter iOrderAdapter)
	{
		super(context, list);
		this.iOrderAdapter = iOrderAdapter;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		OrderViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (OrderViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			viewHolder = new OrderViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_order, null);
			viewHolder.tvOrderId = (TextView) convertView.findViewById(R.id.tv_order_id);
			viewHolder.tvOrderStatus = (TextView) convertView.findViewById(R.id.tv_order_status);
			viewHolder.tvOrderProductCount = (TextView) convertView.findViewById(R.id.tv_products_count);
			viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_total_price);
			viewHolder.lvProducts = (ListView) convertView.findViewById(R.id.list_products);
			convertView.setTag(viewHolder);
		}
		else
		{
			convertView.findViewById(R.id.btn_order_pay).setVisibility(View.GONE);
			convertView.findViewById(R.id.btn_cancel_order).setVisibility(View.GONE);
			convertView.findViewById(R.id.btn_confirm_receive).setVisibility(View.GONE);
			convertView.findViewById(R.id.btn_order_comment).setVisibility(View.GONE);
			viewHolder.clear();
		}
		//设置控件的值
		final OrderInfo orderInfo = list.get(position);
		viewHolder.tvOrderId.setText(orderInfo.getOrderId());
		int count = 0;
		if (orderInfo.getOrderProductInfoList() != null)
		{
			for (OrderProductInfo orderProductInfo : orderInfo.getOrderProductInfoList())
			{
				count += orderProductInfo.getCount();
			}
		}
		viewHolder.tvOrderProductCount.setText(String.format("共%d件商品", count));
		viewHolder.tvPrice.setText("￥" + StringTools.getPrice(orderInfo.getTotalPrice()));
		viewHolder.lvProducts.setAdapter(new OrderProductsAdapter(context, orderInfo.getOrderProductInfoList(), null));
		if (orderInfo.getStatus() == OrderType.WAIT_PAY.getStatus())
		{
			viewHolder.tvOrderStatus.setText(OrderType.WAIT_PAY.getDesc());
			convertView.findViewById(R.id.btn_order_pay).setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.btn_order_pay).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (iOrderAdapter != null)
					{
						iOrderAdapter.onOrderPayClicked(orderInfo);
					}
				}
			});
			convertView.findViewById(R.id.btn_cancel_order).setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.btn_cancel_order).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (iOrderAdapter != null)
					{
						iOrderAdapter.onOrderCancelClicked(orderInfo);
					}
				}
			});
		}
		else if (orderInfo.getStatus() == OrderType.PAYED.getStatus())
		{
			viewHolder.tvOrderStatus.setText(OrderType.PAYED.getDesc());
		}
		else if (orderInfo.getStatus() == OrderType.EXPRESS.getStatus())
		{
			viewHolder.tvOrderStatus.setText(OrderType.EXPRESS.getDesc());
			convertView.findViewById(R.id.btn_confirm_receive).setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.btn_confirm_receive).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (iOrderAdapter != null)
					{
						iOrderAdapter.onOrderConfirmClicked(orderInfo);
					}
				}
			});
		}
		else if (orderInfo.getStatus() == OrderType.FINISH.getStatus())
		{
			viewHolder.tvOrderStatus.setText(OrderType.FINISH.getDesc());
			if (orderInfo.getOrderProductInfoList() != null && orderInfo.getOrderProductInfoList().size() > 0)
			{
				final Button btnComment = (Button) convertView.findViewById(R.id.btn_order_comment);
				btnComment.setVisibility(View.VISIBLE);
				OrderProductCommentService orderProductCommentService = new OrderProductCommentService(context);
				orderProductCommentService.addDataResponse(new IAjaxDataResponse()
				{
					@Override
					public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
					{
						int count = Integer.parseInt(result);
						if (count > 0)
						{
							btnComment.setText("已评价");
							btnComment.setEnabled(false);
						}
						else
						{
							btnComment.setText("我要评价");
							btnComment.setEnabled(true);
							btnComment.setOnClickListener(new View.OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									if (iOrderAdapter != null)
									{
										iOrderAdapter.onOrderCommentClicked(orderInfo);
									}
								}
							});
						}
						return false;
					}
				});
				orderProductCommentService.getProductCommentInfoCount(orderInfo.getOrderId(), orderInfo.getOrderProductInfoList().get(0).getProductInfo().getProductId(), null, false);
			}
		}
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (iOrderAdapter != null)
				{
					iOrderAdapter.onOrderClicked(orderInfo);
				}
			}
		});
		return convertView;
	}

	public interface IOrderAdapter
	{
		void onOrderClicked(OrderInfo orderInfo);

		void onOrderPayClicked(OrderInfo orderInfo);

		void onOrderCancelClicked(OrderInfo orderInfo);

		void onOrderConfirmClicked(OrderInfo orderInfo);

		void onOrderCommentClicked(OrderInfo orderInfo);
	}

	public static class OrderViewHolder
	{
		public TextView tvOrderId;
		public TextView tvOrderStatus;
		public TextView tvOrderProductCount;
		public TextView tvPrice;
		public ListView lvProducts;

		public void clear()
		{
			tvOrderId.setText("");
			tvOrderStatus.setText("");
			tvOrderProductCount.setText("");
			tvPrice.setText("");
			lvProducts.setAdapter(null);
		}
	}
}
