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

import com.lrcall.appbst.MyApplication;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.PicService;
import com.lrcall.ui.customer.DisplayTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class IndexNewProductsAdapter extends BaseUserAdapter<ProductInfo>
{
	protected final INewProductsAdapter iNewProductsAdapter;

	public IndexNewProductsAdapter(Context context, List<ProductInfo> list, INewProductsAdapter iNewProductsAdapter)
	{
		super(context, list);
		this.iNewProductsAdapter = iNewProductsAdapter;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_new_product, null);
			viewHolder.ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_label);
			viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
			convertView.setTag(viewHolder);
			//重设图片宽高
			ViewGroup.LayoutParams layoutParams = viewHolder.ivHead.getLayoutParams();
			layoutParams.width = (DisplayTools.getWindowWidth(context) - DisplayTools.dip2px(context, 31.4f)) / 2;
			layoutParams.height = layoutParams.width;
			viewHolder.ivHead.setLayoutParams(layoutParams);
			if (position % 2 == 0)
			{
				LogcatTools.debug("position", "position:在左边");
				convertView.setPadding(DisplayTools.dip2px(context, 10), DisplayTools.dip2px(context, 5), DisplayTools.dip2px(context, 5), DisplayTools.dip2px(context, 5));
			}
			else
			{
				LogcatTools.debug("position", "position:在右边");
				convertView.setPadding(DisplayTools.dip2px(context, 5), DisplayTools.dip2px(context, 5), DisplayTools.dip2px(context, 10), DisplayTools.dip2px(context, 5));
			}
		}
		else
		{
			viewHolder.clear();
		}
		//设置控件的值
		final ProductInfo productInfo = list.get(position);
		PicService.ajaxGetRoundTopPic(viewHolder.ivHead, ApiConfig.getServerPicUrl(productInfo.getPicId()), DisplayTools.getWindowWidth(MyApplication.getContext()) / 4, 15);
		viewHolder.tvName.setText(productInfo.getName());
		viewHolder.tvPrice.setText("￥" + StringTools.getPrice(productInfo.getPrice()));
		if (iNewProductsAdapter != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iNewProductsAdapter.onProductClicked(productInfo);
				}
			});
		}
		return convertView;
	}

	public interface INewProductsAdapter
	{
		void onProductClicked(ProductInfo productInfo);
	}

	public static class ProductViewHolder
	{
		public ImageView ivHead;
		public TextView tvName;
		public TextView tvPrice;

		public void clear()
		{
			ivHead.setImageBitmap(null);
			tvName.setText("");
			tvPrice.setText("");
		}
	}
}
