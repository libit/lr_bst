/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lrcall.appbst.MyApplication;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.PicService;
import com.lrcall.utils.DisplayTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class IndexRecommendProducts4Adapter extends BaseAdapter
{
	protected final Context context;
	protected final List<ProductInfo> list;
	protected final IItemClick iItemClick;

	public IndexRecommendProducts4Adapter(Context context, List<ProductInfo> list, IItemClick iItemClick)
	{
		this.context = context;
		this.list = list;
		this.iItemClick = iItemClick;
	}

	@Override
	public int getCount()
	{
		//		return list != null ? Math.round(list.size() / 4 + 0.5f) : 0;
		return 1;
	}

	@Override
	public Object getItem(int position)
	{
		return list != null ? list.get(position * 4) : null;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_recommend_product_4, null);
			viewHolder = new ProductViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
			//重设图片宽高
			ViewGroup.LayoutParams layoutParams = viewHolder.ivProduct1.getLayoutParams();
			layoutParams.width = DisplayTools.getWindowWidth(context) - DisplayTools.dip2px(context, 20);
			layoutParams.height = layoutParams.width * 2 / 5;
			viewHolder.ivProduct1.setLayoutParams(layoutParams);
			viewHolder.ivProduct1.setPadding(DisplayTools.dip2px(context, 10), 0, DisplayTools.dip2px(context, 10), 0);
			ViewGroup.LayoutParams layoutParams1 = viewHolder.ivProduct2.getLayoutParams();
			layoutParams1.width = (DisplayTools.getWindowWidth(context) - DisplayTools.dip2px(context, 40)) / 3;
			layoutParams1.height = layoutParams1.width;
			viewHolder.ivProduct2.setLayoutParams(layoutParams1);
			viewHolder.ivProduct3.setLayoutParams(layoutParams1);
			viewHolder.ivProduct4.setLayoutParams(layoutParams1);
			viewHolder.ivProduct2.setPadding(DisplayTools.dip2px(context, 0), 0, DisplayTools.dip2px(context, 5), 0);
			viewHolder.ivProduct3.setPadding(DisplayTools.dip2px(context, 5), 0, DisplayTools.dip2px(context, 5), 0);
			viewHolder.ivProduct4.setPadding(DisplayTools.dip2px(context, 5), 0, DisplayTools.dip2px(context, 0), 0);
		}
		else
		{
			viewHolder.clear();
		}
		try
		{
			//设置控件的值
			final ProductInfo productInfo1 = list.get(position);
			if (productInfo1 != null)
			{
				PicService.ajaxGetRoundPic(viewHolder.ivProduct1, ApiConfig.getServerPicUrl(productInfo1.getPicId()), DisplayTools.getWindowWidth(context) - DisplayTools.dip2px(context, 20), DisplayTools.getWindowWidth(context) - DisplayTools.dip2px(context, 20) * 2 / 5, 20, 50);
				if (iItemClick != null)
				{
					viewHolder.ivProduct1.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							iItemClick.onProduct1Clicked(productInfo1);
						}
					});
				}
			}
			final ProductInfo productInfo2 = list.get(position + 1);
			if (productInfo2 != null)
			{
				PicService.ajaxGetRoundPic(viewHolder.ivProduct2, ApiConfig.getServerPicUrl(productInfo2.getPicId()), DisplayTools.getWindowWidth(MyApplication.getContext()) / 3);
				if (iItemClick != null)
				{
					viewHolder.ivProduct2.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							iItemClick.onProduct2Clicked(productInfo2);
						}
					});
				}
			}
			final ProductInfo productInfo3 = list.get(position + 2);
			if (productInfo3 != null)
			{
				PicService.ajaxGetRoundPic(viewHolder.ivProduct3, ApiConfig.getServerPicUrl(productInfo3.getPicId()), DisplayTools.getWindowWidth(MyApplication.getContext()) / 3);
				if (iItemClick != null)
				{
					viewHolder.ivProduct3.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							iItemClick.onProduct3Clicked(productInfo3);
						}
					});
				}
			}
			final ProductInfo productInfo4 = list.get(position + 3);
			if (productInfo4 != null)
			{
				PicService.ajaxGetRoundPic(viewHolder.ivProduct4, ApiConfig.getServerPicUrl(productInfo4.getPicId()), DisplayTools.getWindowWidth(MyApplication.getContext()) / 3);
				if (iItemClick != null)
				{
					viewHolder.ivProduct4.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							iItemClick.onProduct4Clicked(productInfo4);
						}
					});
				}
			}
		}
		catch (Exception e)
		{
		}
		return convertView;
	}

	public interface IItemClick
	{
		void onProduct1Clicked(ProductInfo productInfo);

		void onProduct2Clicked(ProductInfo productInfo);

		void onProduct3Clicked(ProductInfo productInfo);

		void onProduct4Clicked(ProductInfo productInfo);
	}

	public static class ProductViewHolder
	{
		public ImageView ivProduct1, ivProduct2, ivProduct3, ivProduct4;

		public void viewInit(View convertView)
		{
			ivProduct1 = (ImageView) convertView.findViewById(R.id.iv_product1);
			ivProduct2 = (ImageView) convertView.findViewById(R.id.iv_product2);
			ivProduct3 = (ImageView) convertView.findViewById(R.id.iv_product3);
			ivProduct4 = (ImageView) convertView.findViewById(R.id.iv_product4);
		}

		public void clear()
		{
			ivProduct1.setImageBitmap(null);
			ivProduct2.setImageBitmap(null);
			ivProduct3.setImageBitmap(null);
			ivProduct4.setImageBitmap(null);
		}
	}
}
