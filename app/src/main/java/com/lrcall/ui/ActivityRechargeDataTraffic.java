/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.DataTrafficInfo;
import com.lrcall.appbst.models.PayTypeInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.UserBalanceInfo;
import com.lrcall.appbst.models.UserInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.DataTrafficOrderService;
import com.lrcall.appbst.services.DataTrafficService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UserService;
import com.lrcall.enums.PayType;
import com.lrcall.ui.adapter.DataTrafficAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.dialog.DialogCommon;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.DateTimeTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;
import com.lrcall.utils.apptools.AppFactory;

import java.util.ArrayList;
import java.util.List;

public class ActivityRechargeDataTraffic extends MyBasePageActivity implements IAjaxDataResponse, View.OnClickListener
{
	private View layoutRechargeOnline, layoutRechargeCard, layoutBtnOnline, layoutBtnCard, ivOnline, ivCard;
	private TextView tvOnline, tvCard, tvValidate;
	private EditText etNumber, etCardId, etCardPwd;
	private DataTrafficAdapter dataTrafficAdapter;
	private DataTrafficService mDataTrafficService;
	private DataTrafficOrderService mDataTrafficOrderService;
	private final List<DataTrafficInfo> mDataTrafficInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge_data_traffic);
		viewInit();
		mDataTrafficService = new DataTrafficService(this);
		mDataTrafficService.addDataResponse(this);
		mDataTrafficOrderService = new DataTrafficOrderService(this);
		mDataTrafficOrderService.addDataResponse(this);
		refreshData();
		UserService userService = new UserService(this);
		userService.addDataResponse(this);
		userService.getUserInfo("请稍后...", false);
		userService.getUserBalanceInfo(null, false);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		layoutRechargeOnline = findViewById(R.id.xlist);
		layoutRechargeCard = findViewById(R.id.layout_recharge_card);
		layoutBtnOnline = findViewById(R.id.layout_btn_online);
		layoutBtnCard = findViewById(R.id.layout_btn_card);
		ivOnline = findViewById(R.id.iv_online);
		ivCard = findViewById(R.id.iv_card);
		tvValidate = (TextView) findViewById(R.id.tv_validate);
		tvOnline = (TextView) findViewById(R.id.tv_online);
		tvCard = (TextView) findViewById(R.id.tv_card);
		etNumber = (EditText) findViewById(R.id.et_number);
		etCardId = (EditText) findViewById(R.id.et_card_id);
		etCardPwd = (EditText) findViewById(R.id.et_card_pwd);
		xListView = (XListView) findViewById(R.id.xlist);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
		etNumber.setText(AppFactory.getInstance().getPhoneNumber());
		layoutBtnOnline.setOnClickListener(this);
		layoutBtnCard.setOnClickListener(this);
		findViewById(R.id.btn_recharge_submit).setOnClickListener(this);
		findViewById(R.id.btn_get).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_get:
			{
				mDataTrafficService.getPackageDataTraffic("正在申请，请稍后...", false);
				break;
			}
			case R.id.layout_btn_online:
			{
				tvOnline.setTextColor(getResources().getColor(R.color.red));
				tvCard.setTextColor(getResources().getColor(R.color.black));
				ivOnline.setVisibility(View.VISIBLE);
				ivCard.setVisibility(View.INVISIBLE);
				layoutRechargeOnline.setVisibility(View.VISIBLE);
				layoutRechargeCard.setVisibility(View.GONE);
				break;
			}
			case R.id.layout_btn_card:
			{
				tvOnline.setTextColor(getResources().getColor(R.color.black));
				tvCard.setTextColor(getResources().getColor(R.color.red));
				ivOnline.setVisibility(View.INVISIBLE);
				ivCard.setVisibility(View.VISIBLE);
				layoutRechargeOnline.setVisibility(View.GONE);
				layoutRechargeCard.setVisibility(View.VISIBLE);
				break;
			}
			case R.id.btn_recharge_submit:
			{
				String number = etNumber.getText().toString();
				String cardId = etCardId.getText().toString();
				String cardPwd = etCardPwd.getText().toString();
				if (StringTools.isNull(number))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "请输入要充值的手机号码！");
					etNumber.requestFocus();
					return;
				}
				if (StringTools.isNull(cardId))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "请输入卡号！");
					etCardId.requestFocus();
					return;
				}
				if (StringTools.isNull(cardPwd))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "请输入密码！");
					etCardPwd.requestFocus();
					return;
				}
				mDataTrafficOrderService.addOrderByCard(number, cardId, cardPwd, "正在充值，请稍后...", false);
				break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_recharge_data_traffic, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_recharges)
		{
			startActivity(new Intent(this, ActivityRechargeDataTrafficList.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void refreshData()
	{
		mDataTrafficInfoList.clear();
		dataTrafficAdapter = null;
		loadMoreData();
	}

	@Override
	public void loadMoreData()
	{
		mDataTrafficService.getDataTrafficInfoList(mDataStart, getPageSize(), "请稍后...", true);
	}

	//刷新数据
	synchronized private void refreshDataTraffics(List<DataTrafficInfo> dataTrafficInfoList)
	{
		if (dataTrafficInfoList == null || dataTrafficInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			return;
		}
		if (dataTrafficInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (DataTrafficInfo dataTrafficInfo : dataTrafficInfoList)
		{
			mDataTrafficInfoList.add(dataTrafficInfo);
		}
		if (dataTrafficAdapter == null)
		{
			dataTrafficAdapter = new DataTrafficAdapter(this, mDataTrafficInfoList, new DataTrafficAdapter.IDataTrafficAdapterItemClicked()
			{
				@Override
				public void onDataTrafficClicked(final DataTrafficInfo dataTrafficInfo)
				{
					final String number = etNumber.getText().toString();
					if (StringTools.isNull(number))
					{
						ToastView.showCenterToast(ActivityRechargeDataTraffic.this, R.drawable.ic_do_fail, "号码不能为空！");
						return;
					}
					new DialogCommon(ActivityRechargeDataTraffic.this, new DialogCommon.LibitDialogListener()
					{
						@Override
						public void onOkClick()
						{
							DataTrafficOrderService dataTrafficOrderService = new DataTrafficOrderService(ActivityRechargeDataTraffic.this);
							dataTrafficOrderService.addDataResponse(new IAjaxDataResponse()
							{
								@Override
								public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
								{
									if (url.endsWith(ApiConfig.ADD_DATA_TRAFFIC_ORDER))
									{
										ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
										if (ReturnInfo.isSuccess(returnInfo))
										{
											Intent intent = new Intent(ActivityRechargeDataTraffic.this, ActivityPayList.class);
											intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, GsonTools.toJson(new PayTypeInfo(PayType.PAY_DATA_TRAFFIC_ORDER, dataTrafficInfo.getPrice(), "流量订单" + returnInfo.getErrmsg() + "支付", returnInfo.getErrmsg())));
											startActivity(intent);
										}
										else
										{
											String msg = result;
											if (returnInfo != null)
											{
												msg = returnInfo.getErrmsg();
											}
											ToastView.showCenterToast(ActivityRechargeDataTraffic.this, R.drawable.ic_do_fail, "下单失败，错误信息：" + msg);
										}
										return true;
									}
									return false;
								}
							});
							dataTrafficOrderService.addOrder(dataTrafficInfo.getProductId(), 1, number, null, null, "正在下单，请稍后...", false);
							finish();
						}

						@Override
						public void onCancelClick()
						{
							finish();
						}
					}, "提示", "确定要为" + number + "充值吗？", true, false, true).show();
				}
			});
			xListView.setAdapter(dataTrafficAdapter);
		}
		else
		{
			dataTrafficAdapter.notifyDataSetChanged();
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
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_DATA_TRAFFIC_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<DataTrafficInfo> dataTrafficInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<DataTrafficInfo>>()
				{
				}.getType());
				refreshDataTraffics(dataTrafficInfoList);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_USER_INFO))
		{
			UserInfo userInfo = GsonTools.getReturnObject(result, UserInfo.class);
			if (userInfo != null)
			{
				etNumber.setText(userInfo.getNumber());
				etNumber.setEnabled(false);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_USER_BALANCE_INFO))
		{
			UserBalanceInfo userBalanceInfo = GsonTools.getReturnObject(result, UserBalanceInfo.class);
			if (userBalanceInfo != null)
			{
				long tm = userBalanceInfo.getDataTrafficValidateLong();
				long current = DateTimeTools.getTodayYearMonthLong();
				if (tm < current)
				{
					tm = current;
				}
				tvValidate.setText(String.format("%04d-%02d", tm / 100, tm % 100));
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.DATA_TRAFFIC_RECHARGE_BY_CARD))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				//"充值成功，请等待管理员处理！"
				ToastView.showCenterToast(this, R.drawable.ic_done, returnInfo.getErrmsg());
				etCardId.setText("");
				etCardPwd.setText("");
			}
			else
			{
				String msg = "充值失败！";
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, msg);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_PACKAGE_DATA_TRAFFIC))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				ToastView.showCenterToast(this, R.drawable.ic_done, returnInfo.getErrmsg());
			}
			else
			{
				String msg = "申领失败！";
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, msg);
			}
			return true;
		}
		return false;
	}
}
