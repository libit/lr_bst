/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.UserBalanceInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UserService;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

public class ActivityUserWallet extends MyBaseActivity implements IAjaxDataResponse, View.OnClickListener
{
	private UserService mUserService;
	private TextView tvBalance, tvFreezeBalance, tvPoint;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_wallet);
		mUserService = new UserService(this);
		mUserService.addDataResponse(this);
		viewInit();
		mUserService.getUserBalanceInfo("请稍后...", false);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		tvBalance = (TextView) findViewById(R.id.tv_balance);
		tvFreezeBalance = (TextView) findViewById(R.id.tv_freeze_balance);
		tvPoint = (TextView) findViewById(R.id.tv_point);
		findViewById(R.id.layout_add_balance).setOnClickListener(this);
		findViewById(R.id.btn_duihuan).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_user_wallet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_balance_log)
		{
			startActivity(new Intent(this, ActivityBalanceLog.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_USER_BALANCE_INFO))
		{
			UserBalanceInfo userBalanceInfo = GsonTools.getReturnObject(result, UserBalanceInfo.class);
			if (userBalanceInfo != null)
			{
				tvBalance.setText("￥" + StringTools.getPrice(userBalanceInfo.getBalance()));
				tvFreezeBalance.setText("￥" + StringTools.getPrice(userBalanceInfo.getFreezeBalance()));
				tvPoint.setText("" + userBalanceInfo.getPoint());
			}
			else
			{
				tvBalance.setText("获取失败");
				tvFreezeBalance.setText("获取失败");
				tvPoint.setText("获取失败");
			}
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.layout_add_balance:
			{
				Intent intent = new Intent(this, ActivityBalanceRecharge.class);
				startActivity(intent);
				break;
			}
			case R.id.btn_duihuan:
			{
				Intent intent = new Intent(this, ActivityPointProductShop.class);
				startActivity(intent);
				break;
			}
		}
	}
}
