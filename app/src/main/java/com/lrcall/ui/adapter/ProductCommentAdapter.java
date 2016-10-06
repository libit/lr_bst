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
import android.widget.RatingBar;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ProductCommentInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.PicService;
import com.lrcall.ui.customer.DisplayTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class ProductCommentAdapter extends BaseUserAdapter<ProductCommentInfo>
{
	protected final IProductCommentAdapter iProductCommentAdapter;

	public ProductCommentAdapter(Context context, List<ProductCommentInfo> list, IProductCommentAdapter iProductCommentAdapter)
	{
		super(context, list);
		this.iProductCommentAdapter = iProductCommentAdapter;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ProductCommentViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (ProductCommentViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			viewHolder = new ProductCommentViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_product_comment, null);
			viewHolder.ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
			viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		//设置控件的值
		final ProductCommentInfo orderProductInfo = list.get(position);
		if (orderProductInfo.getUserInfo() != null)
		{
			PicService.ajaxGetPic(viewHolder.ivHead, ApiConfig.getServerPicUrl(orderProductInfo.getUserInfo().getPicId()), DisplayTools.getWindowWidth(context) / 6);
		}
		viewHolder.ratingBar.setRating(orderProductInfo.getType());
		viewHolder.tvName.setText(orderProductInfo.getUserId());
		viewHolder.tvContent.setText(orderProductInfo.getContent());
		return convertView;
	}

	public interface IProductCommentAdapter
	{
	}

	public static class ProductCommentViewHolder
	{
		public ImageView ivHead;
		public RatingBar ratingBar;
		public TextView tvName;
		public TextView tvContent;

		public void clear()
		{
			ivHead.setImageBitmap(null);
			ratingBar.setRating(5);
			tvName.setText("");
			tvContent.setText("");
		}
	}
}
