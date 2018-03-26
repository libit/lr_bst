/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.PayTypeInfo;
import com.lrcall.appbst.models.WxPayInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.WxPayService;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import static com.lrcall.wxapi.WXPayEntryActivity.api;

public class ActivityPayByWxPay extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityPayByWxPay.class.getSimpleName();
	private TextView tvPrice;
	private WxPayService wxPayService;
	private String params;
	private PayTypeInfo payTypeInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_by_wxpay);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			params = bundle.getString(ConstValues.DATA_PAY_TYPE_INFO);
			payTypeInfo = GsonTools.getObject(params, PayTypeInfo.class);
			if (payTypeInfo == null)
			{
				finish();
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, "支付信息为空！");
				return;
			}
		}
		wxPayService = new WxPayService(this);
		wxPayService.addDataResponse(this);
		viewInit();
		initData();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		tvPrice = (TextView) findViewById(R.id.tv_pay_total);
		findViewById(R.id.btn_pay).setOnClickListener(this);
		tvPrice.setText(String.format("%s元", StringTools.getPrice(payTypeInfo.getPrice())));
	}

	private void initData()
	{
		pay();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_pay:
			{
				pay();
				break;
			}
		}
	}

	private void pay()
	{
		//		wxPayService.getWxPayInfo("正在调用微信支付，请稍后...", true);
		wxPayService.wxPrePay(params, "正在调用微信支付，请稍后...", false);
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.WX_PRE_PAY))
		{
			WxPayInfo wxPayInfo = GsonTools.getReturnObject(result, WxPayInfo.class);
			if (wxPayInfo != null)
			{
				//				LogcatTools.debug("wxPayInfo", "wxPayInfo:" + GsonTools.toJson(wxPayInfo));
				PayReq req = new PayReq();
				req.appId = wxPayInfo.getAppId();
				req.partnerId = wxPayInfo.getPartnerId();
				req.prepayId = wxPayInfo.getPrepayId();
				req.nonceStr = wxPayInfo.getNonceStr();
				req.timeStamp = wxPayInfo.getTimeStamp();
				req.packageValue = wxPayInfo.getPackageValue();
				req.sign = wxPayInfo.getSign();
				//			req.extData = "app data"; // optional
				Toast.makeText(ActivityPayByWxPay.this, "正常调起支付", Toast.LENGTH_SHORT).show();
				// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
				PreferenceUtils.getInstance().setStringValue(PreferenceUtils.WX_APP_ID, wxPayInfo.getAppId());
				if (api == null)
				{
					api = WXAPIFactory.createWXAPI(ActivityPayByWxPay.this, WxPayService.getWeixinAppId());
					api.registerApp(WxPayService.getWeixinAppId());
				}
				boolean res = api.sendReq(req);
				if (res)
				{
					Toast.makeText(ActivityPayByWxPay.this, "请求支付成功！", Toast.LENGTH_SHORT).show();
					setResult(RESULT_OK);
					finish();
				}
				else
				{
					ToastView.showCenterToast(ActivityPayByWxPay.this, R.drawable.ic_do_fail, "请求支付失败！");
				}
			}
			else
			{
				ToastView.showCenterToast(ActivityPayByWxPay.this, R.drawable.ic_do_fail, "服务器返回信息不正确！");
			}
			return true;
		}
//		else if (url.endsWith(ApiConfig.GET_WX_PAY_INFO))
//		{
//			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
//			if (ReturnInfo.isSuccess(returnInfo))
//			{
//				String wxAppId = returnInfo.getMsg();
//				PreferenceUtils.getInstance().setStringValue(PreferenceUtils.WX_APP_ID, wxAppId);
//				if (api == null)
//				{
//					api = WXAPIFactory.createWXAPI(ActivityPayByWxPay.this, WxPayService.getWeixinAppId());
//					api.registerApp(WxPayService.getWeixinAppId());
//				}
//				wxPayService.wxPrePay(params, "正在调用微信支付，请稍后...", false);
//			}
//		}
		return false;
	}
}
