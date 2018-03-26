/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.BannerInfo;
import com.lrcall.appbst.models.PayInfo;
import com.lrcall.appbst.models.PayTypeInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.UserInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.BannerService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PayService;
import com.lrcall.appbst.services.UserService;
import com.lrcall.enums.ClientBannerType;
import com.lrcall.enums.PayType;
import com.lrcall.enums.UserLevel;
import com.lrcall.ui.adapter.PayAdapter;
import com.lrcall.ui.customer.ViewHeightCalTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import static com.lrcall.utils.ConstValues.REQ_PAY;
import static com.lrcall.utils.ConstValues.REQ_PAY_BY_ALIPAY;
import static com.lrcall.utils.ConstValues.REQ_PAY_BY_WXPAY;

public class ActivityUserUpgrade extends MyBaseBannerActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityUserUpgrade.class.getSimpleName();
	private static final String PAY_BY_SHOP_CARD_ID = "shop_card_id";
	private TextView tvCurrentLevel, tvNextLevel, tvPrice, tvTips;
	private GridView xListView;
	private final List<PayInfo> mPayInfoList = new ArrayList<>();
	private UserService mUserService;
	private PayService mPayService;
	private PayTypeInfo payTypeInfo;
	private BannerService mBannerService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_upgrade);
		//需要支付的类型
		payTypeInfo = new PayTypeInfo(PayType.PAY_UPGRADE, 0, "用户升级", PreferenceUtils.getInstance().getUserId());
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
		updateView();
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
		bannerViewPager = (ViewPager) findViewById(R.id.banner_viewpager);
		bannerViewPagerTab = (SmartTabLayout) findViewById(R.id.banner_viewpagertab);
		setBannerWidthAndHeight(DisplayTools.getWindowWidth(this), DisplayTools.getWindowWidth(this) * 1 / 2);
		initBannerView();
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
					String params = GsonTools.toJson(payTypeInfo);
					if (payInfo.getName().contains("商城卡"))
					{
						Intent intent = new Intent(ActivityUserUpgrade.this, ActivityPayByShopCard.class);
						intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, params);
						startActivity(intent);
					}
					else if (payInfo.getName().contains("支付宝"))
					{
						Intent intent = new Intent(ActivityUserUpgrade.this, ActivityPayByAlipay.class);
						intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, params);
						startActivityForResult(intent, REQ_PAY_BY_ALIPAY);
					}
					else if (payInfo.getName().contains("微信"))
					{
						Intent intent = new Intent(ActivityUserUpgrade.this, ActivityPayByWxPay.class);
						intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, params);
						startActivityForResult(intent, REQ_PAY_BY_WXPAY);
					}
					else if (payInfo.getName().contains("余额"))
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
				String tips = returnInfo.getMsg();
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
		//		if (requestCode == REQ_PAY)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				setResult(RESULT_OK);
				finish();
			}
		}
	}
}
