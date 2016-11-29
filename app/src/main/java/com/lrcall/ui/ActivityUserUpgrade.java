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
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.pay.demo.PayResult;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.AlipayConfigInfo;
import com.lrcall.appbst.models.BannerInfo;
import com.lrcall.appbst.models.PayInfo;
import com.lrcall.appbst.models.PayTypeInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.UserInfo;
import com.lrcall.appbst.models.WxPayInfo;
import com.lrcall.appbst.services.AlipayService;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.BannerService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PayService;
import com.lrcall.appbst.services.UserService;
import com.lrcall.appbst.services.WxPayService;
import com.lrcall.enums.ClientBannerType;
import com.lrcall.enums.PayType;
import com.lrcall.enums.UserLevel;
import com.lrcall.ui.adapter.PayAdapter;
import com.lrcall.ui.adapter.SectionsPagerAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.customer.ViewHeightCalTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

import static com.lrcall.ui.ActivityPayList.api;

public class ActivityUserUpgrade extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityUserUpgrade.class.getSimpleName();
	public static final int REQ_PAY = 2002;
	private static final String PAY_BY_SHOP_CARD_ID = "shop_card_id";
	private TextView tvCurrentLevel, tvNextLevel, tvPrice, tvTips;
	private GridView xListView;
	private final List<PayInfo> mPayInfoList = new ArrayList<>();
	private UserService mUserService;
	private AlipayService alipayService;
	private WxPayService wxPayService;
	private PayService mPayService;
	private String params;
	private PayTypeInfo payTypeInfo;
	private ViewPager viewPager;
	private final List<Fragment> mFragmentRecommendList = new ArrayList<>();
	private SmartTabLayout viewPagerTab;
	private SectionsPagerAdapter sectionsPagerAdapter;
	private BannerService mBannerService;
	private final Handler mHandler = new Handler()
	{
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
						Toast.makeText(ActivityUserUpgrade.this, "支付成功", Toast.LENGTH_SHORT).show();
						finish();
					}
					else
					{
						// 判断resultStatus 为非"9000"则代表可能支付失败
						// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000"))
						{
							Toast.makeText(ActivityUserUpgrade.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
						}
						else
						{
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							Toast.makeText(ActivityUserUpgrade.this, "支付失败", Toast.LENGTH_SHORT).show();
						}
					}
					break;
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_upgrade);
		//需要支付的类型
		payTypeInfo = new PayTypeInfo(PayType.PAY_UPGRADE, 0, "用户升级", PreferenceUtils.getInstance().getUserId());
		params = GsonTools.toJson(payTypeInfo);
		alipayService = new AlipayService(this);
		alipayService.addDataResponse(this);
		wxPayService = new WxPayService(this);
		wxPayService.addDataResponse(this);
		mPayService = new PayService(this);
		mPayService.addDataResponse(this);
		mUserService = new UserService(this);
		mUserService.addDataResponse(this);
		mBannerService = new BannerService(this);
		mBannerService.addDataResponse(this);
		viewInit();
		mUserService.upgradeTips(null, false);
		mBannerService.getBannerInfoList(ClientBannerType.USER_UPGRADE.getType(), 0, -1, null, true);
		mUserService.getUserInfo("请稍后...", false);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		tvCurrentLevel = (TextView) findViewById(R.id.tv_current_level);
		tvNextLevel = (TextView) findViewById(R.id.tv_next_level);
		tvPrice = (TextView) findViewById(R.id.tv_price);
		tvTips = (TextView) findViewById(R.id.tv_tips);
		xListView = (GridView) findViewById(R.id.xlist);
		findViewById(R.id.btn_upgrade).setOnClickListener(this);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		//设置图片的长宽，这里便于制作图片
		ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
		layoutParams.width = DisplayTools.getWindowWidth(this);
		layoutParams.height = DisplayTools.getWindowWidth(this) * 1 / 2;
		viewPager.setLayoutParams(layoutParams);
		viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
		viewPager.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE)
				{
				}
				else
				{
				}
				return false;
			}
		});
	}

	//设置图片适配器
	private void setViewPagerAdapter(List<BannerInfo> bannerInfoList)
	{
		if (bannerInfoList != null && bannerInfoList.size() > 0)
		{
			mFragmentRecommendList.clear();
			for (BannerInfo bannerInfo : bannerInfoList)
			{
				mFragmentRecommendList.add(FragmentServerImage.newInstance(ApiConfig.getServerPicUrl(bannerInfo.getPicUrl()), DisplayTools.getWindowWidth(this), bannerInfo.getContent()));
			}
			sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mFragmentRecommendList);
			viewPager.setAdapter(sectionsPagerAdapter);
			viewPagerTab.setViewPager(viewPager);
		}
	}

	private void initData()
	{
		PayAdapter payAdapter = new PayAdapter(this, mPayInfoList, new PayAdapter.IItemClick()
		{
			@Override
			public void onPayClicked(View v, PayInfo payInfo)
			{
				if (payInfo != null)
				{
					if (payInfo.getName().indexOf("商城卡") > -1)
					{
						Intent intent = new Intent(ActivityUserUpgrade.this, ActivityPayByShopCard.class);
						intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, params);
						startActivity(intent);
					}
					else if (payInfo.getName().indexOf("支付宝") > -1)
					{
						alipayService.getAlipayConfigInfo("正在调用支付宝，请稍后...", false);
					}
					else if (payInfo.getName().indexOf("微信") > -1)
					{
						wxPayService.getWxPayInfo("正在调用微信支付，请稍后...", true);
					}
					else if (payInfo.getName().indexOf("余额") > -1)
					{
						Intent intent = new Intent(ActivityUserUpgrade.this, ActivityPayByBalance.class);
						intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, params);
						startActivityForResult(intent, REQ_PAY);
					}
				}
			}
		});
		xListView.setAdapter(payAdapter);
		ViewHeightCalTools.setGridViewHeight(xListView, 2, true);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_upgrade:
			{
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_USER_INFO))
		{
			UserInfo userInfo = GsonTools.getReturnObject(result, UserInfo.class);
			if (userInfo != null)
			{
				tvCurrentLevel.setText(UserLevel.getLevelDesc(userInfo.getUserLevel()));
				tvNextLevel.setText(UserLevel.getNextLevelDesc(userInfo.getUserLevel()));
			}
			mPayService.getUserUpgradePrice("请稍后...", false);
			return true;
		}
		else if (url.endsWith(ApiConfig.USER_UPGRADE_TIPS))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				String tips = returnInfo.getErrmsg();
				tips = tips.replace("<br>", "\r\n");
				tips = tips.replace("<br/>", "\r\n");
				tips = tips.replace("<br />", "\r\n");
				tvTips.setText(tips);
				findViewById(R.id.layout_tips).setVisibility(View.VISIBLE);
			}
			else
			{
				tvTips.setText("");
				findViewById(R.id.layout_tips).setVisibility(View.GONE);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.USER_UPGRADE_PRICE))
		{
			Integer amount = GsonTools.getReturnObject(result, Integer.class);
			if (amount != null)
			{
				tvPrice.setText(StringTools.getPrice(amount) + "元");
				payTypeInfo.setPrice(amount);
			}
			else
			{
				showServerMsg(result, null);
			}
			mPayService.getPayTypeList("请稍后...", true);
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_PAY_LIST))
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
					mPayInfoList.add(new PayInfo(PAY_BY_SHOP_CARD_ID, "商城卡支付", ""));
					String picUrl = "";
					for (PayInfo payInfo : payInfoList)
					{
						if (payInfo.getName().contains("余额"))
						{
							picUrl = payInfo.getPicUrl();
						}
						mPayInfoList.add(payInfo);
					}
					mPayInfoList.get(0).setPicUrl(picUrl);
				}
			}
			initData();
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_ALIPAY_CONIFG))
		{
			AlipayConfigInfo alipayConfigInfo = GsonTools.getReturnObject(result, AlipayConfigInfo.class);
			if (alipayConfigInfo != null)
			{
				alipayService.pay(ActivityUserUpgrade.this, mHandler, alipayConfigInfo, payTypeInfo.getSubject(), params, StringTools.getPrice(payTypeInfo.getPrice()));
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.WX_PRE_PAY))
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
				Toast.makeText(ActivityUserUpgrade.this, "正常调起支付", Toast.LENGTH_SHORT).show();
				// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
				boolean res = api.sendReq(req);
				if (res)
				{
					Toast.makeText(ActivityUserUpgrade.this, "请求支付成功！", Toast.LENGTH_SHORT).show();
				}
				else
				{
					ToastView.showCenterToast(ActivityUserUpgrade.this, R.drawable.ic_do_fail, "请求支付失败！");
				}
				finish();
			}
			else
			{
				ToastView.showCenterToast(ActivityUserUpgrade.this, R.drawable.ic_do_fail, "服务器返回信息不正确！");
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
					api = WXAPIFactory.createWXAPI(ActivityUserUpgrade.this, WxPayService.getWeixinAppId());
					api.registerApp(WxPayService.getWeixinAppId());
				}
				wxPayService.wxPrePay(params, "正在调用微信支付，请稍后...", false);
			}
		}
		else if (url.endsWith(ApiConfig.GET_BANNER_LIST))
		{
			List<BannerInfo> bannerInfoList = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				bannerInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<BannerInfo>>()
				{
				}.getType());
			}
			setViewPagerAdapter(bannerInfoList);
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == REQ_PAY)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				setResult(RESULT_OK);
				finish();
			}
		}
	}
}
