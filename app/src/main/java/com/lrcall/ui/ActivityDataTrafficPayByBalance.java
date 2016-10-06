/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.DataTrafficOrderInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.UserBalanceInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.DataTrafficOrderService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PayService;
import com.lrcall.appbst.services.UserService;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

public class ActivityDataTrafficPayByBalance extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityDataTrafficPayByBalance.class.getSimpleName();
	private TextView tvTotalPay, tvBalance;
	private EditText etPassword;
	private UserService mUserService;
	private PayService mPayService;
	private DataTrafficOrderService mDataTrafficOrderService;
	private String orderId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_by_balance);
		orderId = getIntent().getStringExtra(ConstValues.DATA_ORDER_ID);
		if (StringTools.isNull(orderId))
		{
			finish();
			Toast.makeText(this, "订单号为空！", Toast.LENGTH_LONG).show();
		}
		mUserService = new UserService(this);
		mUserService.addDataResponse(this);
		mPayService = new PayService(this);
		mPayService.addDataResponse(this);
		mDataTrafficOrderService = new DataTrafficOrderService(this);
		mDataTrafficOrderService.addDataResponse(this);
		viewInit();
		initData();
		mDataTrafficOrderService.getOrderInfo(orderId, "请稍后...", false);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		tvTotalPay = (TextView) findViewById(R.id.tv_pay_total);
		tvBalance = (TextView) findViewById(R.id.tv_balance);
		etPassword = (EditText) findViewById(R.id.et_pay_password);
		findViewById(R.id.btn_pay).setOnClickListener(this);
	}

	private void initData()
	{
		mUserService.getUserBalanceInfo(null, false);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_pay:
			{
				String password = etPassword.getText().toString();
				if (StringTools.isNull(password))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "支付密码不能为空！");
					etPassword.requestFocus();
					return;
				}
				mPayService.payDataTrafficOrderByBalance(orderId, password, "正在支付，请稍后...", true);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_USER_BALANCE_INFO))
		{
			UserBalanceInfo userBalanceInfo = GsonTools.getReturnObject(result, UserBalanceInfo.class);
			if (userBalanceInfo != null)
			{
				tvBalance.setText(StringTools.getPrice(userBalanceInfo.getBalance()) + "元");
			}
			else
			{
				String msg = result;
				ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				tvBalance.setText(msg);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.PAY_DATA_TRAFFIC_BY_BALANCE))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				ToastView.showCenterToast(this, R.drawable.ic_done, "支付成功！");
				setResult(RESULT_OK);
				finish();
			}
			else
			{
				String msg = result;
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, "支付失败：" + msg);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_DATA_TRAFFIC_ORDER_INFO))
		{
			DataTrafficOrderInfo dataTrafficOrderInfo = GsonTools.getReturnObject(result, DataTrafficOrderInfo.class);
			if (dataTrafficOrderInfo != null)
			{
				tvTotalPay.setText(StringTools.getPrice(dataTrafficOrderInfo.getTotalPrice()) + "元");
			}
			return true;
		}
		return false;
	}
}
