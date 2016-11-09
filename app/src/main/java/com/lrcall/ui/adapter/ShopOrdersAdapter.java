/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.appbst.models.OrderProductInfo;
import com.lrcall.appbst.models.OrderSubInfo;
import com.lrcall.enums.OrderStatus;
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class ShopOrdersAdapter extends BaseUserAdapter<OrderSubInfo>
{
	protected final IItemClick iItemClick;

	public ShopOrdersAdapter(Context context, List<OrderSubInfo> list, IItemClick iItemClick)
	{
		super(context, list);
		this.iItemClick = iItemClick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		SearchProductViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (SearchProductViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_order, null);
			viewHolder = new SearchProductViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			convertView.findViewById(R.id.btn_change_express).setVisibility(View.GONE);
			convertView.findViewById(R.id.btn_send_express).setVisibility(View.GONE);
			viewHolder.clear();
		}
		//设置控件的值
		final OrderSubInfo orderSubInfo = list.get(position);
		viewHolder.tvOrderId.setText(orderSubInfo.getOrderId());
		int count = 0;
		if (orderSubInfo.getOrderProductInfoList() != null)
		{
			for (OrderProductInfo orderProductInfo : orderSubInfo.getOrderProductInfoList())
			{
				count += orderProductInfo.getCount();
			}
		}
		viewHolder.tvOrderProductCount.setText(String.format("共%d件商品", count));
		viewHolder.tvPrice.setText("￥" + StringTools.getPrice(orderSubInfo.getTotalPrice()));
		viewHolder.lvProducts.setAdapter(new OrderProductsAdapter(context, orderSubInfo.getOrderProductInfoList(), null));
		if (orderSubInfo.getStatus() == OrderStatus.WAIT_PAY.getStatus())
		{
			viewHolder.tvOrderStatus.setText(OrderStatus.WAIT_PAY.getDesc());
			if (iItemClick != null)
			{
				if (orderSubInfo.getExpressPrice() > 0)
				{
					convertView.findViewById(R.id.btn_change_express).setVisibility(View.VISIBLE);
					convertView.findViewById(R.id.btn_change_express).setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							iItemClick.onOrderChangeExpressClicked(orderSubInfo);
						}
					});
				}
			}
		}
		else if (orderSubInfo.getStatus() == OrderStatus.PAYED.getStatus())
		{
			viewHolder.tvOrderStatus.setText(OrderStatus.PAYED.getDesc());
			convertView.findViewById(R.id.btn_send_express).setVisibility(View.VISIBLE);
			if (iItemClick != null)
			{
				convertView.findViewById(R.id.btn_send_express).setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						iItemClick.onOrderSendExpressClicked(orderSubInfo);
					}
				});
			}
		}
		else if (orderSubInfo.getStatus() == OrderStatus.EXPRESS.getStatus())
		{
			viewHolder.tvOrderStatus.setText(OrderStatus.EXPRESS.getDesc());
		}
		else if (orderSubInfo.getStatus() == OrderStatus.FINISH.getStatus())
		{
			viewHolder.tvOrderStatus.setText(OrderStatus.FINISH.getDesc());
		}
		if (iItemClick != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClick.onOrderClicked(orderSubInfo);
				}
			});
		}
		return convertView;
	}

	public interface IItemClick
	{
		void onOrderClicked(OrderSubInfo orderSubInfo);

		void onOrderChangeExpressClicked(OrderSubInfo orderSubInfo);

		void onOrderSendExpressClicked(OrderSubInfo orderSubInfo);
	}

	public static class SearchProductViewHolder
	{
		public TextView tvOrderId;
		public TextView tvOrderStatus;
		public TextView tvOrderProductCount;
		public TextView tvPrice;
		public ListView lvProducts;

		public void viewInit(View rootView)
		{
			tvOrderId = (TextView) rootView.findViewById(R.id.tv_order_id);
			tvOrderStatus = (TextView) rootView.findViewById(R.id.tv_order_status);
			tvOrderProductCount = (TextView) rootView.findViewById(R.id.tv_products_count);
			tvPrice = (TextView) rootView.findViewById(R.id.tv_total_price);
			lvProducts = (ListView) rootView.findViewById(R.id.list_products);
		}

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
