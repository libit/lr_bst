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

import com.lrcall.appbst.MyApplication;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.PicService;
import com.lrcall.ui.customer.DisplayTools;
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class SearchProductsAdapter extends BaseUserAdapter<ProductInfo>
{
	protected final IProductsAdapterItemClicked iProductsAdapterItemClicked;

	public SearchProductsAdapter(Context context, List<ProductInfo> list, IProductsAdapterItemClicked iProductsAdapterItemClicked)
	{
		super(context, list);
		this.iProductsAdapterItemClicked = iProductsAdapterItemClicked;
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
			viewHolder = new SearchProductViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_search_product, null);
			viewHolder.ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_label);
			viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
			viewHolder.tvMarketPrice = (TextView) convertView.findViewById(R.id.tv_market_price);
			viewHolder.tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final ProductInfo productInfo = list.get(position);
		String url = ApiConfig.getServerPicUrl(productInfo.getPicId());
		PicService.ajaxGetPic(viewHolder.ivHead, url, DisplayTools.getWindowWidth(MyApplication.getContext()) / 6);
		//			BitmapCacheTools.loadBitmap(context, searchProductViewHolder.ivHead, productInfo.getImgRes());
		//		searchProductViewHolder.ivHead.setImageResource(productInfo.getImgRes());
		viewHolder.tvName.setText(productInfo.getName());
		viewHolder.tvPrice.setText("￥" + StringTools.getPrice(productInfo.getPrice()));
		viewHolder.tvMarketPrice.setText("￥" + StringTools.getPrice(productInfo.getMarketPrice()));
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (iProductsAdapterItemClicked != null)
				{
					iProductsAdapterItemClicked.onProductClicked(productInfo);
				}
			}
		});
		return convertView;
	}

	public interface IProductsAdapterItemClicked
	{
		void onProductClicked(ProductInfo productInfo);
	}

	public static class SearchProductViewHolder
	{
		public ImageView ivHead;
		public TextView tvName;
		public TextView tvPrice;
		public TextView tvMarketPrice;

		public void clear()
		{
			ivHead.setImageBitmap(null);
			tvName.setText("");
			tvPrice.setText("");
			tvMarketPrice.setText("");
		}
	}
}
