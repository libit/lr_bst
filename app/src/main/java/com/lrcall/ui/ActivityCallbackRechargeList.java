/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.PayInfo;
import com.lrcall.appbst.models.PayTypeInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PayService;
import com.lrcall.enums.PayType;
import com.lrcall.ui.adapter.PayAdapter;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import static com.lrcall.utils.ConstValues.REQ_PAY;
import static com.lrcall.utils.ConstValues.REQ_PAY_BY_ALIPAY;
import static com.lrcall.utils.ConstValues.REQ_PAY_BY_WXPAY;

public class ActivityCallbackRechargeList extends MyBaseActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityCallbackRechargeList.class.getSimpleName();
	private View layoutPayTypeList, layoutNoPayType;
	private ListView xListView;
	private final List<PayInfo> mPayInfoList = new ArrayList<>();
	private String params;
	//	private PayTypeInfo payTypeInfo;
	private PayService mPayService;
	private Spinner spAmount;
	List<Integer> amountList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callback_recharge_list);
		mPayService = new PayService(this);
		mPayService.addDataResponse(this);
		viewInit();
		amountList = new ArrayList<>();
		amountList.add(20);
		amountList.add(50);
		amountList.add(100);
		ArrayAdapter spAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, amountList);
		spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spAmount.setAdapter(spAdapter);
		onRefresh();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu)
//	{
//		getMenuInflater().inflate(R.menu.menu_activity_pay_list, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item)
//	{
//		int id = item.getItemId();
//		if (id == R.id.action_refresh)
//		{
//			onRefresh();
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		layoutPayTypeList = findViewById(R.id.layout_paytype_list);
		layoutNoPayType = findViewById(R.id.layout_no_paytype);
		xListView = (ListView) findViewById(R.id.xlist);
		spAmount = (Spinner) findViewById(R.id.sp_amount);
	}

	private void onRefresh()
	{
		//		mPayService.getPayTypeList("请稍后...", true);
		mPayInfoList.clear();
		mPayInfoList.add(new PayInfo("1", "充值卡充值", ""));
		mPayInfoList.add(new PayInfo("2", "支付宝充值", ""));
		initData();
	}

	private void initData()
	{
		if (mPayInfoList.size() < 1)
		{
			layoutPayTypeList.setVisibility(View.GONE);
			layoutNoPayType.setVisibility(View.VISIBLE);
		}
		else
		{
			layoutPayTypeList.setVisibility(View.VISIBLE);
			layoutNoPayType.setVisibility(View.GONE);
			PayAdapter payAdapter = new PayAdapter(this, mPayInfoList, new PayAdapter.IItemClick()
			{
				@Override
				public void onPayClicked(View v, PayInfo payInfo)
				{
					if (payInfo != null)
					{
						int amount = amountList.get(spAmount.getSelectedItemPosition()) * 100;
						PayTypeInfo payTypeInfo = new PayTypeInfo(PayType.PAY_CALLBACK_RECHARGE, amount, PayType.PAY_CALLBACK_RECHARGE.getDesc(), PreferenceUtils.getInstance().getUserId());
						params = GsonTools.toJson(payTypeInfo);
						if (payInfo.getName().contains("充值卡"))
						{
							Intent intent = new Intent(ActivityCallbackRechargeList.this, ActivityCallbackRecharge.class);
							intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, params);
							startActivityForResult(intent, REQ_PAY);
						}
						else if (payInfo.getName().contains("余额"))
						{
							Intent intent = new Intent(ActivityCallbackRechargeList.this, ActivityPayByBalance.class);
							intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, params);
							startActivityForResult(intent, REQ_PAY);
						}
						else if (payInfo.getName().contains("支付宝"))
						{
							Intent intent = new Intent(ActivityCallbackRechargeList.this, ActivityPayByAlipay.class);
							intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, params);
							startActivityForResult(intent, REQ_PAY_BY_ALIPAY);
						}
						else if (payInfo.getName().contains("微信"))
						{
							Intent intent = new Intent(ActivityCallbackRechargeList.this, ActivityPayByWxPay.class);
							intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, params);
							startActivityForResult(intent, REQ_PAY_BY_WXPAY);
						}
					}
				}
			});
			xListView.setAdapter(payAdapter);
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
					//					String type = payTypeInfo.getPayType().getType();
					//					for (PayInfo payInfo : payInfoList)
					//					{
					//						if (type.equals(PayType.PAY_BALANCE.getType()))
					//						{
					//							if (payInfo.getName().contains("余额"))
					//							{
					//								continue;
					//							}
					//						}
					//						mPayInfoList.add(payInfo);
					//					}
				}
			}
			initData();
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == Activity.RESULT_OK)
		{
			finish();
		}
	}
}
