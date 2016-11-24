/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.MyApplication;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ProductHistoryInfo;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PicService;
import com.lrcall.appbst.services.ProductService;
import com.lrcall.db.DbProductInfoFactory;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class ProductHistoryAdapter extends BaseUserAdapter<ProductHistoryInfo>
{
	protected final IItemClicked iItemClicked;

	public ProductHistoryAdapter(Context context, List<ProductHistoryInfo> list, IItemClicked iItemClicked)
	{
		super(context, list);
		this.iItemClicked = iItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ProductStarViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (ProductStarViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_product_history, null);
			viewHolder = new ProductStarViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final ProductHistoryInfo productHistoryInfo = list.get(position);
		viewHolder.id = productHistoryInfo.getProductId();
		ProductInfo productInfo = DbProductInfoFactory.getInstance().getProductInfo(productHistoryInfo.getProductId());
		if (productInfo != null)
		{
			setProduct(productInfo, viewHolder, productHistoryInfo, convertView);
		}
		else
		{
			final ProductStarViewHolder holder = viewHolder;
			final View v = convertView;
			ProductService productService = new ProductService(context);
			productService.addDataResponse(new IAjaxDataResponse()
			{
				@Override
				public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
				{
					final ProductInfo productInfo = GsonTools.getReturnObject(result, ProductInfo.class);
					if (productInfo != null && holder.id.equals(productHistoryInfo.getProductId()))
					{
						setProduct(productInfo, holder, productHistoryInfo, v);
					}
					return false;
				}
			});
			productService.getProductInfo(productHistoryInfo.getProductId(), null, true);
		}
		return convertView;
	}

	private void setProduct(final ProductInfo productInfo, ProductStarViewHolder holder, ProductHistoryInfo productStarInfo, View v)
	{
		if (productInfo != null && holder.id.equals(productStarInfo.getProductId()))
		{
			String picUrl = ApiConfig.getServerPicUrl(productInfo.getPicId());
			PicService.ajaxGetPic(holder.ivHead, picUrl, DisplayTools.getWindowWidth(MyApplication.getContext()) / 6);
			holder.tvName.setText(productInfo.getName());
			holder.tvPrice.setText("￥" + StringTools.getPrice(productInfo.getPrice()));
			holder.tvMarketPrice.setText("￥" + StringTools.getPrice(productInfo.getMarketPrice()));
			if (iItemClicked != null)
			{
				v.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						iItemClicked.onProductClicked(productInfo);
					}
				});
			}
		}
	}

	public interface IItemClicked
	{
		void onProductClicked(ProductInfo productInfo);
	}

	public static class ProductStarViewHolder
	{
		public String id;
		public ImageView ivHead;
		public TextView tvName;
		public TextView tvPrice;
		public TextView tvMarketPrice;

		public void viewInit(View convertView)
		{
			ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
			tvName = (TextView) convertView.findViewById(R.id.tv_label);
			tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
			tvMarketPrice = (TextView) convertView.findViewById(R.id.tv_market_price);
			tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}

		public void clear()
		{
			ivHead.setImageBitmap(null);
			tvName.setText("");
			tvPrice.setText("");
			tvMarketPrice.setText("");
		}
	}
}
