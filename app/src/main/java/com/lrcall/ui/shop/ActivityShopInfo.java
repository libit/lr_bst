/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.shop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ShopInfo;
import com.lrcall.appbst.models.ShopSaleData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ShopService;
import com.lrcall.enums.ShopAuthStatus;
import com.lrcall.enums.ShopLevel;
import com.lrcall.ui.MyBaseActivity;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.BitmapTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;

public class ActivityShopInfo extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityShopInfo.class.getSimpleName();
	private TextView tvType, tvApplyStatus, tvAmount, tvRecentPeople, tvRecentOrderCount, tvRecentSaleAmount;
	private ImageView ivHead;
	private View btnRegister, btnAuth, layoutFuncs;
	private ShopService mShopService;
	private boolean bFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_info);
		mShopService = new ShopService(this);
		mShopService.addDataResponse(this);
		viewInit();
		Bitmap bitmap = BitmapTools.getBmpFile(AppConfig.getShopPicCacheDir(PreferenceUtils.getInstance().getUsername()));
		if (bitmap != null)
		{
			ivHead.setImageBitmap(bitmap);
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		refreshData();
	}

	private void refreshData()
	{
		String tips = null;
		if (bFirst)
		{
			tips = "请稍后...";
		}
		bFirst = false;
		mShopService.getShopInfo(tips, false);
		mShopService.getSaleData(null, false);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		tvType = (TextView) findViewById(R.id.tv_type);
		tvApplyStatus = (TextView) findViewById(R.id.tv_apply_status);
		tvAmount = (TextView) findViewById(R.id.tv_amount);
		tvRecentPeople = (TextView) findViewById(R.id.tv_people_count);
		tvRecentOrderCount = (TextView) findViewById(R.id.tv_recent_order_count);
		tvRecentSaleAmount = (TextView) findViewById(R.id.tv_recent_sale_amount);
		ivHead = (ImageView) findViewById(R.id.iv_head);
		btnRegister = findViewById(R.id.btn_register);
		btnAuth = findViewById(R.id.btn_auth);
		layoutFuncs = findViewById(R.id.layout_funcs);
		btnRegister.setOnClickListener(this);
		btnAuth.setOnClickListener(this);
		findViewById(R.id.layout_products_manage).setOnClickListener(this);
		findViewById(R.id.layout_agent_products_manage).setOnClickListener(this);
		findViewById(R.id.layout_orders_manage).setOnClickListener(this);
		findViewById(R.id.layout_profits_list).setOnClickListener(this);
		findViewById(R.id.layout_add_product).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_register:
			{
				startActivity(new Intent(this, ActivityShopRegister.class));
				break;
			}
			case R.id.btn_auth:
			{
				startActivity(new Intent(this, ActivityShopAuth.class));
				break;
			}
			case R.id.layout_add_product:
			{
				startActivity(new Intent(this, ActivityShopProductAdd.class));
				break;
			}
			case R.id.layout_agent_products_manage:
			{
				startActivity(new Intent(this, ActivityShopAgentProductManage.class));
				break;
			}
			case R.id.layout_products_manage:
			{
				startActivity(new Intent(this, ActivityShopProductManage.class));
				break;
			}
			case R.id.layout_orders_manage:
			{
				startActivity(new Intent(this, ActivityShopOrderManage.class));
				break;
			}
			case R.id.layout_profits_list:
			{
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_SHOP_INFO))
		{
			ShopInfo shopInfo = GsonTools.getReturnObject(result, ShopInfo.class);
			if (shopInfo != null)
			{
				btnRegister.setVisibility(View.GONE);
				tvType.setText(ShopLevel.getLevelDesc(shopInfo.getLevelId()));
				tvApplyStatus.setVisibility(View.VISIBLE);
				tvApplyStatus.setText(ShopAuthStatus.getAuthDesc(shopInfo.getAuthStatus()));
				if (shopInfo.getAuthStatus() == ShopAuthStatus.UNAUTH.getStatus())
				{
					btnAuth.setVisibility(View.VISIBLE);
				}
				if (shopInfo.getAuthStatus() == ShopAuthStatus.AUTHED.getStatus())
				{
					layoutFuncs.setVisibility(View.VISIBLE);
				}
				else
				{
					layoutFuncs.setVisibility(View.GONE);
				}
			}
			else
			{
				tvType.setText("您还没申请开店!");
				btnRegister.setVisibility(View.VISIBLE);
				btnAuth.setVisibility(View.GONE);
				layoutFuncs.setVisibility(View.GONE);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_SHOP_SALE_DATA))
		{
			ShopSaleData shopSaleData = GsonTools.getReturnObject(result, ShopSaleData.class);
			if (shopSaleData != null)
			{
				tvAmount.setText(StringTools.getPrice(shopSaleData.getTotalSaleAmount()));
				tvRecentSaleAmount.setText(StringTools.getPrice(shopSaleData.getRecent7SaleAmont()));
				tvRecentOrderCount.setText(shopSaleData.getRecent7OrderCount() + "");
			}
			return true;
		}
		return false;
	}
}
