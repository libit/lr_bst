/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.alipay.sdk.pay.demo.PayResult;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.AlipayConfigInfo;
import com.lrcall.appbst.models.OrderInfo;
import com.lrcall.appbst.models.OrderProductInfo;
import com.lrcall.appbst.models.PayInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.WxPayInfo;
import com.lrcall.appbst.services.AlipayService;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.OrderService;
import com.lrcall.appbst.services.PayService;
import com.lrcall.appbst.services.WxPayService;
import com.lrcall.ui.adapter.PayAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

public class ActivityPayList extends MyBaseActivity implements IAjaxDataResponse, PayAdapter.IPayAdapterItemClicked
{
	public static final int REQ_PAY = 1002;
	private static final String TAG = ActivityPayList.class.getSimpleName();
	private ListView xListView;
	private PayService mPayService;
	private OrderService mOrderService;
	private final List<PayInfo> mPayInfoList = new ArrayList<>();
	private String orderId = "";
	private OrderInfo orderInfo;
	public static IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_list);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			orderId = bundle.getString(ConstValues.DATA_ORDER_ID);
			if (StringTools.isNull(orderId))
			{
				finish();
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, "订单号不能为空！");
			}
		}
		mPayService = new PayService(this);
		mPayService.addDataResponse(this);
		mOrderService = new OrderService(this);
		mOrderService.addDataResponse(this);
		viewInit();
		initData();
		mOrderService.getOrderInfo(orderId, "请稍后...", false);
		mPayService.getPayTypeList(null, true);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		xListView = (ListView) findViewById(R.id.xlist);
	}

	private void initData()
	{
		PayAdapter payAdapter = new PayAdapter(this, mPayInfoList, this);
		xListView.setAdapter(payAdapter);
	}

	@Override
	public void onPayClicked(View v, PayInfo payInfo)
	{
		if (orderInfo == null)
		{
			ToastView.showCenterToast(this, R.drawable.ic_do_fail, "获取订单信息失败！");
			return;
		}
		if (payInfo != null)
		{
			if (payInfo.getName().indexOf("余额") > -1)
			{
				Intent intent = new Intent(this, ActivityPayByBalance.class);
				intent.putExtra(ConstValues.DATA_ORDER_ID, orderId);
				startActivityForResult(intent, REQ_PAY);
			}
			else if (payInfo.getName().indexOf("支付宝") > -1)
			{
				final AlipayService alipayService = new AlipayService(this);
				alipayService.addDataResponse(new IAjaxDataResponse()
				{
					@Override
					public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
					{
						if (url.endsWith(ApiConfig.GET_ALIPAY_CONIFG))
						{
							AlipayConfigInfo alipayConfigInfo = GsonTools.getReturnObject(result, AlipayConfigInfo.class);
							if (alipayConfigInfo != null)
							{
								String subject = "";
								if (orderInfo.getOrderProductInfoList() != null)
								{
									for (OrderProductInfo orderProductInfo : orderInfo.getOrderProductInfoList())
									{
										subject += orderProductInfo.getProductInfo().getName() + ",";
									}
								}
								if (subject.length() > 0)
								{
									subject = subject.substring(0, subject.length() - 1);
								}
								alipayService.pay(ActivityPayList.this, new Handler()
								{
									@SuppressWarnings("unused")
									public void handleMessage(Message msg)
									{
										switch (msg.what)
										{
											case AlipayService.SDK_PAY_FLAG:
											{
												PayResult payResult = new PayResult((String) msg.obj);
												/**
												 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
												 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
												 * docType=1) 建议商户依赖异步通知
												 */
												String resultInfo = payResult.getResult();// 同步返回需要验证的信息
												String resultStatus = payResult.getResultStatus();
												// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
												if (TextUtils.equals(resultStatus, "9000"))
												{
													Toast.makeText(ActivityPayList.this, "支付成功", Toast.LENGTH_SHORT).show();
													finish();
												}
												else
												{
													// 判断resultStatus 为非"9000"则代表可能支付失败
													// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
													if (TextUtils.equals(resultStatus, "8000"))
													{
														Toast.makeText(ActivityPayList.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
													}
													else
													{
														// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
														Toast.makeText(ActivityPayList.this, "支付失败", Toast.LENGTH_SHORT).show();
													}
												}
												break;
											}
											default:
												break;
										}
									}
								}, alipayConfigInfo, subject, orderId, String.format("%.2f", (double) orderInfo.getTotalPrice() / 100));//orderInfo.getTotalPrice() / 100 + ""
							}
							return true;
						}
						return false;
					}
				});
				alipayService.getAlipayConfigInfo("正在调用支付宝，请稍后...", false);
			}
			else if (payInfo.getName().indexOf("微信") > -1)
			{
				final WxPayService wxPayService = new WxPayService(this);
				wxPayService.addDataResponse(new IAjaxDataResponse()
				{
					@Override
					public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
					{
						if (url.endsWith(ApiConfig.WX_PRE_PAY))
						{
							WxPayInfo wxPayInfo = GsonTools.getReturnObject(result, WxPayInfo.class);
							if (wxPayInfo != null)
							{
								LogcatTools.debug("wxPayInfo", "wxPayInfo:" + GsonTools.toJson(wxPayInfo));
								PayReq req = new PayReq();
								req.appId = wxPayInfo.getAppId();
								req.partnerId = wxPayInfo.getPartnerId();
								req.prepayId = wxPayInfo.getPrepayId();
								req.nonceStr = wxPayInfo.getNonceStr();
								req.timeStamp = wxPayInfo.getTimeStamp();
								req.packageValue = wxPayInfo.getPackageValue();
								req.sign = wxPayInfo.getSign();
								//			req.extData = "app data"; // optional
								Toast.makeText(ActivityPayList.this, "正常调起支付", Toast.LENGTH_SHORT).show();
								// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
								boolean res = api.sendReq(req);
								if (res)
								{
									Toast.makeText(ActivityPayList.this, "请求支付成功！", Toast.LENGTH_SHORT).show();
								}
								else
								{
									ToastView.showCenterToast(ActivityPayList.this, R.drawable.ic_do_fail, "请求支付失败！");
								}
								finish();
							}
							else
							{
								ToastView.showCenterToast(ActivityPayList.this, R.drawable.ic_do_fail, "服务器返回信息不正确！");
							}
							return true;
						}
						else if (url.endsWith(ApiConfig.GET_WX_PAY_INFO))
						{
							ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
							if (ReturnInfo.isSuccess(returnInfo))
							{
								String wxAppId = returnInfo.getErrmsg();
								PreferenceUtils.getInstance().setStringValue(PreferenceUtils.WX_APP_ID, wxAppId);
								if (api == null)
								{
									api = WXAPIFactory.createWXAPI(ActivityPayList.this, WxPayService.getWeixinAppId());
									api.registerApp(WxPayService.getWeixinAppId());
								}
								wxPayService.wxPrePay(orderId, "正在调用微信支付，请稍后...", false);
							}
						}
						return false;
					}
				});
				wxPayService.getWxPayInfo("正在调用微信支付，请稍后...", true);
				//				wxPayService.wxPrePay(orderId, "正在调用微信支付，请稍后...", false);
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_PAY_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<PayInfo> payInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<PayInfo>>()
				{
				}.getType());
				if (payInfoList != null && payInfoList.size() > 0)
				{
					mPayInfoList.clear();
					//					mPayInfoList = DbUserAddressInfoFactory.getInstance().getUserAddressInfoList(PreferenceUtils.getInstance().getUsername());
					for (PayInfo payInfo : payInfoList)
					{
						mPayInfoList.add(payInfo);
					}
				}
			}
			initData();
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_ORDER_INFO))
		{
			orderInfo = GsonTools.getReturnObject(result, OrderInfo.class);
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_PAY)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				finish();
			}
		}
	}
}