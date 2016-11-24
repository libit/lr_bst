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

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.MyApplication;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.OrderProductInfo;
import com.lrcall.appbst.models.PointProductInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PicService;
import com.lrcall.appbst.services.PointProductService;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.GsonTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class PointOrderProductsAdapter extends BaseUserAdapter<OrderProductInfo>
{
	protected final IItemClick iItemClick;

	public PointOrderProductsAdapter(Context context, List<OrderProductInfo> list, IItemClick iItemClick)
	{
		super(context, list);
		this.iItemClick = iItemClick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ProductViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (ProductViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_order_product, null);
			viewHolder = new ProductViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		//设置控件的值
		final OrderProductInfo orderProductInfo = list.get(position);
		final PointProductInfo pointProductInfo = null;// DbProductInfoFactory.getInstance().getProductInfo(orderProductInfo.getProductInfo().getProductId());
		if (pointProductInfo != null)
		{
			setPointProductInfo(viewHolder, orderProductInfo, pointProductInfo, convertView);
		}
		else
		{
			final View v = convertView;
			final ProductViewHolder vHolder = viewHolder;
			PointProductService pointProductService = new PointProductService(context);
			pointProductService.addDataResponse(new IAjaxDataResponse()
			{
				@Override
				public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
				{
					if (url.endsWith(ApiConfig.GET_POINT_PRODUCT))
					{
						PointProductInfo pointProductInfo1 = GsonTools.getReturnObject(result, PointProductInfo.class);
						//						LogcatTools.debug("onAjaxDataResponse", "pointProductInfo1:" + result);
						if (pointProductInfo1 != null)
						{
							setPointProductInfo(vHolder, orderProductInfo, pointProductInfo1, v);
						}
					}
					return false;
				}
			});
			pointProductService.getPointProduct(orderProductInfo.getProductId(), null, true);
		}
		return convertView;
	}

	private void setPointProductInfo(final ProductViewHolder viewHolder, OrderProductInfo orderProductInfo, final PointProductInfo pointProductInfo, View convertView)
	{
		PicService.ajaxGetPic(viewHolder.ivHead, ApiConfig.getServerPicUrl(pointProductInfo.getPicUrl()), DisplayTools.getWindowWidth(MyApplication.getContext()) / 4);
		viewHolder.tvName.setText(pointProductInfo.getName());
		viewHolder.tvPrice.setText("" + pointProductInfo.getPoint());
		viewHolder.tvAmount.setText("X " + orderProductInfo.getCount());
		if (iItemClick != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClick.onProductClicked(pointProductInfo);
				}
			});
		}
	}

	public interface IItemClick
	{
		/**
		 * 点击商品事件
		 *
		 * @param pointProductInfo 商品信息
		 */
		void onProductClicked(PointProductInfo pointProductInfo);
		/**
		 * 从服务器获取商品信息并显示到控件上
		 *
		 * @param viewHolder 控件
		 */
		//		void onGetProductFromServer(ProductViewHolder viewHolder);
	}

	public static class ProductViewHolder
	{
		public ImageView ivHead;
		public TextView tvName;
		public TextView tvPrice;
		public TextView tvAmount;

		public void viewInit(View convertView)
		{
			ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
			tvName = (TextView) convertView.findViewById(R.id.tv_label);
			tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
			tvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
		}

		public void clear()
		{
			ivHead.setImageBitmap(null);
			tvName.setText("");
			tvPrice.setText("");
			tvAmount.setText("");
		}
	}
}
