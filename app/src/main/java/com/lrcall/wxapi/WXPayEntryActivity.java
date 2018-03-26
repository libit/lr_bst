/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.lrcall.appbst.R;
import com.lrcall.appbst.services.WxPayService;
import com.lrcall.ui.customer.ToastView;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler
{
	private static final String TAG = WXPayEntryActivity.class.getSimpleName();
	public static final int WX_PAY_FLAG = 4;
	public static IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (api == null)
		{
			api = WXAPIFactory.createWXAPI(this, WxPayService.getWeixinAppId());
			api.registerApp(WxPayService.getWeixinAppId());
		}
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req)
	{
	}

	@Override
	public void onResp(BaseResp resp)
	{
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
		{
			if (resp.errCode == 0)
			{
				ToastView.showCenterToast(this, R.drawable.ic_done, getString(R.string.pay_success));
				Message msg = new Message();
				msg.what = WX_PAY_FLAG;
				EventBus.getDefault().post(msg);
				this.finish();
			}
			else if (resp.errCode == -1)
			{
				this.finish();
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, "支付失败，错误信息：" + resp.errStr);
			}
			else
			{
				this.finish();
			}
		}
		else
		{
			this.finish();
		}
	}
}