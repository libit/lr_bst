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
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class IndexConcessionProductsAdapter extends BaseUserAdapter<ProductInfo>
{
	protected final IConcessionProductsAdapter iConcessionProductsAdapter;

	public IndexConcessionProductsAdapter(Context context, List<ProductInfo> list, IConcessionProductsAdapter iConcessionProductsAdapter)
	{
		super(context, list);
		this.iConcessionProductsAdapter = iConcessionProductsAdapter;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_concession_product, null);
			viewHolder.ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_label);
			viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
			convertView.setTag(viewHolder);
			//重设图片宽高
			ViewGroup.LayoutParams layoutParams = viewHolder.ivHead.getLayoutParams();
			layoutParams.width = DisplayTools.getWindowWidth(context);
			layoutParams.height = layoutParams.width * 2 / 5;
			viewHolder.ivHead.setLayoutParams(layoutParams);
		}
		else
		{
			viewHolder.clear();
		}
		//设置控件的值
		final ProductInfo productInfo = list.get(position);
		PicService.ajaxGetPic(viewHolder.ivHead, ApiConfig.getServerPicUrl(productInfo.getPicId()), DisplayTools.getWindowWidth(MyApplication.getContext()));
		viewHolder.tvName.setText(productInfo.getName());
		viewHolder.tvPrice.setText("￥" + StringTools.getPrice(productInfo.getPrice()));
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (iConcessionProductsAdapter != null)
				{
					iConcessionProductsAdapter.onProductClicked(productInfo);
				}
			}
		});
		return convertView;
	}

	public interface IConcessionProductsAdapter
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
