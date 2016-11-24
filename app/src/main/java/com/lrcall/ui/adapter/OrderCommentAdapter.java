/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.OrderProductInfo;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PicService;
import com.lrcall.appbst.services.ProductService;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.GsonTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class OrderCommentAdapter extends BaseUserAdapter<OrderProductInfo>
{
	protected final IItemClick iItemClick;

	public OrderCommentAdapter(Context context, List<OrderProductInfo> list, IItemClick iItemClick)
	{
		super(context, list);
		this.iItemClick = iItemClick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		OrderCommentViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (OrderCommentViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_order_product_comment, null);
			viewHolder = new OrderCommentViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		//设置控件的值
		final OrderProductInfo orderProductInfo = list.get(position);
		final OrderCommentViewHolder holder = viewHolder;
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
						PicService.ajaxGetPic(holder.ivHead, ApiConfig.getServerPicUrl(productInfo.getPicId()), DisplayTools.getWindowWidth(context) / 6);
					}
				}
				return false;
			}
		});
		productService.getProductInfo(orderProductInfo.getProductId(), null, true);
		final RatingBar ratingBar = viewHolder.ratingBar;
		final EditText etContent = viewHolder.etContent;
		if (iItemClick != null)
		{
			convertView.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClick.onSubmit(orderProductInfo.getProductId(), ratingBar.getNumStars(), etContent.getText().toString());
				}
			});
		}
		return convertView;
	}

	public interface IItemClick
	{
		void onSubmit(String productId, int rate, String content);
	}

	public static class OrderCommentViewHolder
	{
		public ImageView ivHead;
		public RatingBar ratingBar;
		public EditText etContent;

		public void viewInit(View convertView)
		{
			ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
			ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
			etContent = (EditText) convertView.findViewById(R.id.et_content);
		}

		public void clear()
		{
			ivHead.setImageBitmap(null);
			ratingBar.setRating(5);
			etContent.setText("");
		}
	}
}
