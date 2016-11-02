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
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PicService;
import com.lrcall.appbst.services.ProductService;
import com.lrcall.db.DbProductInfoFactory;
import com.lrcall.ui.customer.DisplayTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class OrderProductsAdapter extends BaseUserAdapter<OrderProductInfo>
{
	protected final IOrderProductsAdapter iOrderProductsAdapter;

	public OrderProductsAdapter(Context context, List<OrderProductInfo> list, IOrderProductsAdapter iOrderProductsAdapter)
	{
		super(context, list);
		this.iOrderProductsAdapter = iOrderProductsAdapter;
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
			viewHolder = new ProductViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_order_product, null);
			viewHolder.ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_label);
			viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
			viewHolder.tvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		//设置控件的值
		final OrderProductInfo orderProductInfo = list.get(position);
		final ProductInfo productInfo = DbProductInfoFactory.getInstance().getProductInfo(orderProductInfo.getProductId());
		if (productInfo != null)
		{
			setProductInfo(viewHolder, orderProductInfo, productInfo, convertView);
		}
		else
		{
			final View v = convertView;
			final ProductViewHolder vHolder = viewHolder;
			ProductService productService = new ProductService(context);
			productService.addDataResponse(new IAjaxDataResponse()
			{
				@Override
				public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
				{
					if (url.endsWith(ApiConfig.GET_PRODUCT_INFO))
					{
						ProductInfo productInfo = GsonTools.getReturnObject(result, ProductInfo.class);
						if (productInfo != null)
						{
							setProductInfo(vHolder, orderProductInfo, productInfo, v);
						}
					}
					return false;
				}
			});
			productService.getProductInfo(orderProductInfo.getProductId(), null, true);
		}
		return convertView;
	}

	private void setProductInfo(final ProductViewHolder viewHolder, OrderProductInfo orderProductInfo, final ProductInfo productInfo, View convertView)
	{
		PicService.ajaxGetPic(viewHolder.ivHead, ApiConfig.getServerPicUrl(productInfo.getPicId()), DisplayTools.getWindowWidth(MyApplication.getContext()) / 4);
		viewHolder.tvName.setText(productInfo.getName());
		viewHolder.tvPrice.setText("￥" + StringTools.getPrice(productInfo.getPrice()));
		viewHolder.tvAmount.setText("X " + orderProductInfo.getCount());
		if (iOrderProductsAdapter != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iOrderProductsAdapter.onProductClicked(productInfo);
				}
			});
		}
	}

	public interface IOrderProductsAdapter
	{
		/**
		 * 点击商品事件
		 *
		 * @param productInfo 商品信息
		 */
		void onProductClicked(ProductInfo productInfo);
	}

	public static class ProductViewHolder
	{
		public ImageView ivHead;
		public TextView tvName;
		public TextView tvPrice;
		public TextView tvAmount;

		public void clear()
		{
			ivHead.setImageBitmap(null);
			tvName.setText("");
			tvPrice.setText("");
			tvAmount.setText("");
		}
	}
}
