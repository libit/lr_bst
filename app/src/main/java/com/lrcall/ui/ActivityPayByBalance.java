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
import com.lrcall.appbst.models.DataTrafficOrderInfo;
import com.lrcall.appbst.models.OrderInfo;
import com.lrcall.appbst.models.PayTypeInfo;
import com.lrcall.appbst.models.PointOrderInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.UserBalanceInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.DataTrafficOrderService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.OrderService;
import com.lrcall.appbst.services.PayService;
import com.lrcall.appbst.services.PointOrderService;
import com.lrcall.appbst.services.UserService;
import com.lrcall.enums.PayType;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

public class ActivityPayByBalance extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityPayByBalance.class.getSimpleName();
	private TextView tvTotalPay, tvBalance;
	private EditText etPassword;
	private UserService mUserService;
	private PayService mPayService;
	private OrderService mOrderService;
	private DataTrafficOrderService mDataTrafficOrderService;
	private PointOrderService mPointOrderService;
	private String orderId;
	private PayTypeInfo payTypeInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_by_balance);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			String params = bundle.getString(ConstValues.DATA_PAY_TYPE_INFO);
			payTypeInfo = GsonTools.getObject(params, PayTypeInfo.class);
			if (payTypeInfo == null)
			{
				finish();
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, "支付信息为空！");
				return;
			}
			String type = payTypeInfo.getPayType().getType();
			if (type.equals(PayType.PAY_ORDER.getType()) || type.equals(PayType.PAY_DATA_TRAFFIC_ORDER.getType()) || type.equals(PayType.PAY_POINT_ORDER.getType()))
			{
				orderId = payTypeInfo.getComment();
				if (StringTools.isNull(orderId))
				{
					finish();
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "订单号不能为空！");
					return;
				}
			}
		}
		mUserService = new UserService(this);
		mUserService.addDataResponse(this);
		mPayService = new PayService(this);
		mPayService.addDataResponse(this);
		mOrderService = new OrderService(this);
		mOrderService.addDataResponse(this);
		mDataTrafficOrderService = new DataTrafficOrderService(this);
		mDataTrafficOrderService.addDataResponse(this);
		mPointOrderService = new PointOrderService(this);
		mPointOrderService.addDataResponse(this);
		viewInit();
		initData();
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
		if (payTypeInfo.getPayType().getType().equals(PayType.PAY_ORDER.getType()))
		{
			mOrderService.getOrderInfo(orderId, "请稍后...", false);
		}
		else if (payTypeInfo.getPayType().getType().equals(PayType.PAY_DATA_TRAFFIC_ORDER.getType()))
		{
			mDataTrafficOrderService.getOrderInfo(orderId, "请稍后...", false);
		}
		else if (payTypeInfo.getPayType().getType().equals(PayType.PAY_POINT_ORDER.getType()))
		{
			mPointOrderService.getOrderInfo(orderId, "请稍后...", false);
		}
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
				if (payTypeInfo.getPayType().getType().equals(PayType.PAY_ORDER.getType()))
				{
					mPayService.payOrderByBalance(orderId, password, "正在支付，请稍后...", true);
				}
				else if (payTypeInfo.getPayType().getType().equals(PayType.PAY_DATA_TRAFFIC_ORDER.getType()))
				{
					mPayService.payDataTrafficOrderByBalance(orderId, password, "正在支付，请稍后...", true);
				}
				else if (payTypeInfo.getPayType().getType().equals(PayType.PAY_POINT_ORDER.getType()))
				{
					mPointOrderService.payPointOrderByBalance(orderId, password, "正在支付，请稍后...", true);
				}
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
		else if (url.endsWith(ApiConfig.PAY_BY_BALANCE))
		{
			showServerMsg(result, "支付成功！");
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				setResult(RESULT_OK);
				finish();
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_ORDER_INFO))
		{
			OrderInfo orderInfo = GsonTools.getReturnObject(result, OrderInfo.class);
			if (orderInfo != null)
			{
				tvTotalPay.setText(StringTools.getPrice(orderInfo.getTotalPrice()) + "元");
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.PAY_DATA_TRAFFIC_BY_BALANCE))
		{
			showServerMsg(result, "支付成功！");
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				setResult(RESULT_OK);
				finish();
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
		else if (url.endsWith(ApiConfig.GET_POINT_ORDER))
		{
			PointOrderInfo pointOrderInfo = GsonTools.getReturnObject(result, PointOrderInfo.class);
			if (pointOrderInfo != null)
			{
				tvTotalPay.setText(StringTools.getPrice(pointOrderInfo.getTotalPrice()) + "元");
			}
			else
			{
				showServerMsg(result, null);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.PAY_POINT_ORDER_BY_BALANCE))
		{
			showServerMsg(result, "支付成功！");
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				setResult(RESULT_OK);
				finish();
			}
			return true;
		}
		return false;
	}
}
