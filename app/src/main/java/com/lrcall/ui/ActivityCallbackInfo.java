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
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.UserCallbackInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.CallbackService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.enums.StatusType;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.BitmapTools;
import com.lrcall.utils.DateTimeTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;

public class ActivityCallbackInfo extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityCallbackInfo.class.getSimpleName();
	private TextView tvBalance, tvValidate, tvPackage, tvVip;
	private ImageView ivHead;
	private View btnRegister, layoutRecharge, layoutFuncs;
	private CallbackService mCallbackService;
	private boolean bFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callback_info);
		mCallbackService = new CallbackService(this);
		mCallbackService.addDataResponse(this);
		viewInit();
		Bitmap bitmap = BitmapTools.getBmpFile(AppConfig.getUserPicCacheDir(PreferenceUtils.getInstance().getUserId()));
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
		mCallbackService.getUserCallbackInfo(tips, false);
		//		mCallbackService.getBalanceInfo(true, tips, false);
		bFirst = false;
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		tvBalance = (TextView) findViewById(R.id.tv_balance);
		tvValidate = (TextView) findViewById(R.id.tv_validate);
		tvPackage = (TextView) findViewById(R.id.tv_package);
		tvVip = (TextView) findViewById(R.id.tv_vip);
		ivHead = (ImageView) findViewById(R.id.iv_head);
		btnRegister = findViewById(R.id.btn_register);
		layoutRecharge = findViewById(R.id.layout_recharge);
		layoutFuncs = findViewById(R.id.layout_funcs);
		btnRegister.setOnClickListener(this);
		layoutRecharge.setOnClickListener(this);
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
			case R.id.layout_recharge:
			{
				startActivity(new Intent(this, ActivityCallbackRechargeList.class));
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
		//		if (url.endsWith(ApiConfig.CALLBACK_GET_BALANCE_INFO))
		//		{
		//			CallbackBalanceInfo callbackBalanceInfo = GsonTools.getReturnObject(result, CallbackBalanceInfo.class);
		//			if (callbackBalanceInfo != null)
		//			{
		//				if (StringTools.isNull(callbackBalanceInfo.getValidateDate()))
		//				{
		//					tvBalance.setText(String.format("当前余额:%.2f元", callbackBalanceInfo.getBalance()));
		//				}
		//				else
		//				{
		//					tvBalance.setText(String.format("当前余额:%.2f元\n有效期至:%s", callbackBalanceInfo.getBalance(), callbackBalanceInfo.getValidateDate()));
		//				}
		//				btnRegister.setVisibility(View.GONE);
		//				btnRecharge.setVisibility(View.VISIBLE);
		//				layoutFuncs.setVisibility(View.VISIBLE);
		//			}
		//			else
		//			{
		//				tvBalance.setText("您还没有开通回拨服务!");
		//				btnRegister.setVisibility(View.VISIBLE);
		//				btnRecharge.setVisibility(View.GONE);
		//				layoutFuncs.setVisibility(View.GONE);
		//			}
		//			return true;
		//		}
		//		else
		if (url.endsWith(ApiConfig.GET_USER_CALLBACK_INFO))
		{
			UserCallbackInfo userCallbackInfo = GsonTools.getReturnObject(result, UserCallbackInfo.class);
			if (userCallbackInfo != null)
			{
				tvBalance.setText(String.format("当前余额:%s元", StringTools.getPrice(userCallbackInfo.getBalance())));
				tvValidate.setText(String.format("有效期至:%s", DateTimeTools.getDateString(userCallbackInfo.getValidateDateLong())));
				tvPackage.setText("当前套餐:" + userCallbackInfo.getPackageId() + ",已使用时长:" + userCallbackInfo.getUseMinute() + "分钟");
				if (userCallbackInfo.getIsVip() == StatusType.ENABLED.getStatus())
				{
					long current = System.currentTimeMillis();
					if (current > userCallbackInfo.getVipValidateDateLong())
					{
						tvVip.setText("VIP:已过期");
					}
					else
					{
						tvVip.setText("VIP有效期:" + DateTimeTools.getDateString(userCallbackInfo.getVipValidateDateLong()));
					}
				}
				else
				{
					tvVip.setText("VIP:否");
				}
				btnRegister.setVisibility(View.GONE);
				//				layoutRecharge.setVisibility(View.VISIBLE);
				layoutFuncs.setVisibility(View.VISIBLE);
			}
			else
			{
				tvValidate.setText("您还没有开通回拨服务!");
				btnRegister.setVisibility(View.VISIBLE);
				//				layoutRecharge.setVisibility(View.GONE);
				layoutFuncs.setVisibility(View.GONE);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.CALLBACK_REGISTER))
		{
			showServerMsg(result, "开通回拨服务成功!");
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				refreshData();
			}
			return true;
		}
		return false;
	}
}
