/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.MyApplication;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.ShopCartInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PicService;
import com.lrcall.appbst.services.ProductService;
import com.lrcall.db.DbProductInfoFactory;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.List;
import java.util.Map;

/**
 * Created by libit on 16/4/30.
 */
public class ShopCartProductsAdapter extends BaseUserAdapter<ShopCartInfo>
{
	protected final IItemClicked iItemClicked;
	private Map<ShopCartInfo, CheckBox> checkBoxMap;

	public ShopCartProductsAdapter(Context context, List<ShopCartInfo> list, Map<ShopCartInfo, CheckBox> checkBoxMap, IItemClicked iItemClicked)
	{
		super(context, list);
		this.checkBoxMap = checkBoxMap;
		this.iItemClicked = iItemClicked;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_cart_product, null);
			viewHolder = new ProductViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		//设置控件的值
		final ShopCartInfo shopCartInfo = list.get(position);
		checkBoxMap.put(shopCartInfo, viewHolder.cbSelect);
		ProductInfo productInfo = DbProductInfoFactory.getInstance().getProductInfo(shopCartInfo.getProductId());
		if (productInfo != null)
		{
			setValue(viewHolder, convertView, shopCartInfo, productInfo);
		}
		else
		{
			final ProductViewHolder productViewHolder = viewHolder;
			final View v = convertView;
			ProductService productService = new ProductService(context);
			productService.addDataResponse(new IAjaxDataResponse()
			{
				@Override
				public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
				{
					if (url.endsWith(ApiConfig.GET_PRODUCT_INFO))
					{
						ProductInfo productInfo1 = GsonTools.getObject(result, ProductInfo.class);
						if (productInfo1 != null)
						{
							DbProductInfoFactory.getInstance().addOrUpdateProductInfo(productInfo1);
							setValue(productViewHolder, v, shopCartInfo, productInfo1);
						}
					}
					return true;
				}
			});
			productService.getProductInfo(shopCartInfo.getProductId(), null, false);
		}
		return convertView;
	}

	private void setValue(ProductViewHolder viewHolder, final View convertView, final ShopCartInfo shopCartInfo, final ProductInfo productInfo)
	{
		PicService.ajaxGetPic(viewHolder.ivHead, ApiConfig.getServerPicUrl(productInfo.getPicId()), DisplayTools.getWindowWidth(MyApplication.getContext()) / 3);
		viewHolder.tvName.setText(productInfo.getName());
		viewHolder.tvCount.setText("X " + shopCartInfo.getAmount());
		viewHolder.tvPrice.setText("￥" + StringTools.getPrice(productInfo.getPrice()));
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//				if (iItemClicked != null)
				//				{
				//					iItemClicked.onProductClicked(productInfo);
				//				}
			}
		});
		if (iItemClicked != null)
		{
			convertView.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					iItemClicked.onDeleteClicked(shopCartInfo);
				}
			});
			viewHolder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					iItemClicked.onCheckClicked(shopCartInfo, productInfo, isChecked);
				}
			});
		}
	}

	public interface IItemClicked
	{
		void onProductClicked(ProductInfo productInfo);

		void onDeleteClicked(ShopCartInfo shopCartInfo);

		void onCheckClicked(ShopCartInfo shopCartInfo, ProductInfo productInfo, boolean isChecked);
	}

	public static class ProductViewHolder
	{
		public CheckBox cbSelect;
		public ImageView ivHead;
		public TextView tvName;
		public TextView tvCount;
		public TextView tvPrice;

		public void viewInit(View convertView)
		{
			cbSelect = (CheckBox) convertView.findViewById(R.id.cb_select);
			ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
			tvName = (TextView) convertView.findViewById(R.id.tv_label);
			tvCount = (TextView) convertView.findViewById(R.id.tv_total);
			tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
		}

		public void clear()
		{
			cbSelect.setChecked(true);
			ivHead.setImageBitmap(null);
			tvName.setText("");
			tvCount.setText("");
			tvPrice.setText("");
		}
	}
}
