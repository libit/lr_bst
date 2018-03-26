/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.CallbackBalanceInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.CallbackService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.utils.GsonTools;

public class ActivityQueryUserBalance extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private EditText etNumber;
	private TextView tvNumber, tvBalance, tvRegDate, tvValidate;
	private CallbackService mCallbackService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_user_balance);
		mCallbackService = new CallbackService(this);
		mCallbackService.addDataResponse(this);
		viewInit();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		etNumber = (EditText) findViewById(R.id.et_number);
		tvNumber = (TextView) findViewById(R.id.tv_number);
		tvBalance = (TextView) findViewById(R.id.tv_balance);
		tvRegDate = (TextView) findViewById(R.id.tv_register_date);
		tvValidate = (TextView) findViewById(R.id.tv_validate);
		findViewById(R.id.btn_ok).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_ok:
			{
				String number = etNumber.getText().toString();
				mCallbackService.getBalanceInfo(number, true, "请稍后...", false);
				break;
			}
		}
	}

	/**
	 * ajax回调函数
	 *
	 * @param url
	 * @param result 返回结果
	 * @param status 状态
	 * @return
	 */
	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.CALLBACK_GET_BALANCE_INFO))
		{
			CallbackBalanceInfo callbackBalanceInfo = GsonTools.getReturnObject(result, CallbackBalanceInfo.class);
			if (callbackBalanceInfo != null)
			{
				tvNumber.setText(callbackBalanceInfo.getNumber());
				tvBalance.setText(String.format("%.2f元", callbackBalanceInfo.getBalance()));
				tvRegDate.setText(callbackBalanceInfo.getRegisterDate());
				tvValidate.setText(callbackBalanceInfo.getValidateDate());
			}
			else
			{
				showServerMsg(result, null);
				tvNumber.setText("");
				tvBalance.setText("");
				tvRegDate.setText("");
				tvValidate.setText("");
			}
			return true;
		}
		return false;
	}
}
