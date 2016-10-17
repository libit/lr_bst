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
import com.lrcall.appbst.models.CallbackBalanceInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.CallbackService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.BmpTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;

public class ActivityCallbackInfo extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityCallbackInfo.class.getSimpleName();
	private TextView tvInfo;
	private ImageView ivHead;
	private View btnRegister, btnRecharge;
	private CallbackService mCallbackService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callback_info);
		mCallbackService = new CallbackService(this);
		mCallbackService.addDataResponse(this);
		viewInit();
		Bitmap bitmap = BmpTools.getBmpFile(AppConfig.getUserPicCacheDir(PreferenceUtils.getInstance().getUsername()));
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
		mCallbackService.getBalanceInfo(true, "请稍后...", false);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		tvInfo = (TextView) findViewById(R.id.tv_info);
		ivHead = (ImageView) findViewById(R.id.iv_head);
		btnRegister = findViewById(R.id.btn_register);
		btnRecharge = findViewById(R.id.btn_recharge);
		btnRegister.setOnClickListener(this);
		btnRecharge.setOnClickListener(this);
		findViewById(R.id.layout_calllog).setOnClickListener(this);
		findViewById(R.id.layout_recharge_log).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_register:
			{
				mCallbackService.register("正在开通回拨服务,请稍后...", false);
				break;
			}
			case R.id.btn_recharge:
			{
				startActivity(new Intent(this, ActivityCallbackRecharge.class));
				break;
			}
			case R.id.layout_calllog:
			{
				startActivity(new Intent(this, ActivityCallbackCallLogList.class));
				break;
			}
			case R.id.layout_recharge_log:
			{
				startActivity(new Intent(this, ActivityCallbackRechargeLogList.class));
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.CALLBACK_GET_BALANCE_INFO))
		{
			CallbackBalanceInfo callbackBalanceInfo = GsonTools.getReturnObject(result, CallbackBalanceInfo.class);
			if (callbackBalanceInfo != null)
			{
				tvInfo.setText(String.format("当前余额:%.2f元\n有效期至:%s", callbackBalanceInfo.getBalance(), callbackBalanceInfo.getValidateDate()));
				btnRegister.setVisibility(View.GONE);
				btnRecharge.setVisibility(View.VISIBLE);
			}
			else
			{
				tvInfo.setText("您还没有开通回拨服务!");
				btnRegister.setVisibility(View.VISIBLE);
				btnRecharge.setVisibility(View.GONE);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.CALLBACK_REGISTER))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				ToastView.showCenterToast(this, R.drawable.ic_done, "开通回拨服务成功!");
				refreshData();
			}
			else
			{
				String msg = result;
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, "开通回拨服务失败:" + msg);
			}
			return true;
		}
		return false;
	}
}
