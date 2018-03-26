/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.CallbackService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

public class ActivityCallbackRecharge extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityCallbackRecharge.class.getSimpleName();
	private EditText etCardId, etCardPwd;
	private CallbackService mCallbackService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callback_recharge);
		mCallbackService = new CallbackService(this);
		mCallbackService.addDataResponse(this);
		viewInit();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		etCardId = (EditText) findViewById(R.id.et_card_id);
		etCardPwd = (EditText) findViewById(R.id.et_card_pwd);
		findViewById(R.id.btn_recharge).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_recharge:
			{
				String cardId = etCardId.getText().toString();
				String cardPwd = etCardPwd.getText().toString();
				if (StringTools.isNull(cardId))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "充值卡号不能为空！");
					etCardId.requestFocus();
					return;
				}
				if (StringTools.isNull(cardPwd))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "充值密码不能为空！");
					etCardPwd.requestFocus();
					return;
				}
				mCallbackService.recharge(cardId, cardPwd, "正在充值,请稍后...", false);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.CALLBACK_RECHARGE))
		{
			showServerMsg(result, "充值成功!");
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				finish();
			}
			return true;
		}
		return false;
	}
}
