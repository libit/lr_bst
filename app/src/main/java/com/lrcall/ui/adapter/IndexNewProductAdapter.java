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

import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.PicService;
import com.lrcall.enums.NeedExpress;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.StringTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class IndexNewProductAdapter extends BaseUserAdapter<ProductInfo>
{
	protected final IItemClick iItemClick;

	public IndexNewProductAdapter(Context context, List<ProductInfo> list, IItemClick iItemClick)
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_index_new_product, null);
			viewHolder = new ProductViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
			//重设图片宽高
			ViewGroup.LayoutParams layoutParams = viewHolder.ivHead.getLayoutParams();
			layoutParams.width = DisplayTools.getWindowWidth(context);
			layoutParams.height = layoutParams.width * 1 / 2;
			viewHolder.ivHead.setLayoutParams(layoutParams);
			convertView.setPadding(DisplayTools.dip2px(context, 5), DisplayTools.dip2px(context, 5), DisplayTools.dip2px(context, 5), DisplayTools.dip2px(context, 5));
		}
		else
		{
			viewHolder.clear();
		}
		//设置控件的值
		final ProductInfo productInfo = list.get(position);
		PicService.ajaxGetPic(viewHolder.ivHead, ApiConfig.getServerPicUrl(productInfo.getPicId()), DisplayTools.getWindowWidth(context) - DisplayTools.dip2px(context, 10));
		viewHolder.tvName.setText(productInfo.getName());
		viewHolder.tvPrice.setText(String.format("%s元", StringTools.getPrice(productInfo.getPrice())));
		viewHolder.tvMarketPrice.setText(String.format("%s元", StringTools.getPrice(productInfo.getMarketPrice())));
		viewHolder.tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		if (productInfo.getNeedExpress() == NeedExpress.NEED.getStatus())
		{
			if (productInfo.getExpressPrice() == 0)
			{
				viewHolder.tvExpress.setText("包邮");
			}
			else
			{
				viewHolder.tvExpress.setText(String.format("快递费%s元", StringTools.getPrice(productInfo.getExpressPrice())));
			}
		}
		else
		{
			viewHolder.tvExpress.setText("");
		}
		if (productInfo.getCount() <= 0)
		{
			viewHolder.tvAmount.setText("库存不足");
		}
		else
		{
			viewHolder.tvAmount.setText("剩余：" + productInfo.getCount());
		}
		if (StringTools.isNull(productInfo.getShopId()))
		{
			viewHolder.tvShop.setText("自营商品");
		}
		else
		{
			viewHolder.tvShop.setText("来自商家" + productInfo.getShopId());
		}
		if (iItemClick != null)
		{
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClick.onProductClicked(productInfo);
				}
			});
		}
		return convertView;
	}

	public interface IItemClick
	{
		void onProductClicked(ProductInfo productInfo);
	}

	public static class ProductViewHolder
	{
		public ImageView ivHead;
		public TextView tvName;
		public TextView tvPrice;
		public TextView tvMarketPrice;
		public TextView tvExpress;
		public TextView tvAmount;
		public TextView tvShop;

		public void viewInit(View convertView)
		{
			ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
			tvName = (TextView) convertView.findViewById(R.id.tv_label);
			tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
			tvMarketPrice = (TextView) convertView.findViewById(R.id.tv_market_press);
			tvExpress = (TextView) convertView.findViewById(R.id.tv_express);
			tvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
			tvShop = (TextView) convertView.findViewById(R.id.tv_shop);
		}

		public void clear()
		{
			ivHead.setImageBitmap(null);
			tvName.setText("");
			tvPrice.setText("");
			tvMarketPrice.setText("");
			tvExpress.setText("");
			tvAmount.setText("");
			tvShop.setText("");
		}
	}
}
