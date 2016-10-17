/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ShopInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ShopService;
import com.lrcall.enums.ShopAuthStatus;
import com.lrcall.enums.ShopLevel;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.BmpTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;

public class ActivityShopInfo extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityShopInfo.class.getSimpleName();
	private TextView tvType, tvApplyStatus;
	private ImageView ivHead;
	private View btnRegister;
	private ShopService mShopService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_info);
		mShopService = new ShopService(this);
		mShopService.addDataResponse(this);
		viewInit();
		Bitmap bitmap = BmpTools.getBmpFile(AppConfig.getShopPicCacheDir(PreferenceUtils.getInstance().getUsername()));
		if (bitmap != null)
		{
			ivHead.setImageBitmap(bitmap);
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mShopService.getShopInfo("请稍后...", false);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		tvType = (TextView) findViewById(R.id.tv_type);
		tvApplyStatus = (TextView) findViewById(R.id.tv_apply_status);
		ivHead = (ImageView) findViewById(R.id.iv_head);
		btnRegister = findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(this);
		findViewById(R.id.layout_products_manage).setOnClickListener(this);
		findViewById(R.id.layout_orders_manage).setOnClickListener(this);
		findViewById(R.id.layout_profits_list).setOnClickListener(this);
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
			case R.id.layout_products_manage:
			{
				startActivity(new Intent(this, ActivityShopProductsManage.class));
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
				tvApplyStatus.setText(ShopAuthStatus.getAuthDesc(shopInfo.getAuthStatus()));
			}
			else
			{
				tvType.setText("您还没申请开店!");
				btnRegister.setVisibility(View.VISIBLE);
			}
			return true;
		}
		return false;
	}
}
