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

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.DataTrafficOrderInfo;
import com.lrcall.appbst.models.PayTypeInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.DataTrafficOrderService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.enums.OrderStatus;
import com.lrcall.enums.PayType;
import com.lrcall.ui.adapter.DataTrafficOrderAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.dialog.DialogCommon;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityRechargeDataTrafficList extends MyBasePageActivity implements IAjaxDataResponse
{
	public static final int REQ_PAY = 1005;
	private static final String TAG = ActivityRechargeDataTrafficList.class.getSimpleName();
	private View layoutRechargeDataTrafficList, layoutNoRechargeDataTraffic;
	private DataTrafficOrderService mDataTrafficOrderService;
	private List<DataTrafficOrderInfo> mDataTrafficOrderInfoList = new ArrayList<>();
	private DataTrafficOrderAdapter mDataTrafficOrderAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge_data_traffic_list);
		mDataTrafficOrderService = new DataTrafficOrderService(this);
		mDataTrafficOrderService.addDataResponse(this);
		viewInit();
		onRefresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_recharge_data_traffic_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_refresh)
		{
			onRefresh();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		layoutRechargeDataTrafficList = findViewById(R.id.layout_log_list);
		layoutNoRechargeDataTraffic = findViewById(R.id.layout_no_log);
		xListView = (XListView) findViewById(R.id.xlist);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
	}

	@Override
	public void refreshData()
	{
		mDataTrafficOrderInfoList.clear();
		if (mDataTrafficOrderAdapter != null)
		{
			mDataTrafficOrderAdapter.notifyDataSetChanged();
		}
		mDataTrafficOrderAdapter = null;
		loadMoreData();
	}

	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		mDataTrafficOrderService.getOrderInfoList(mDataStart, getPageSize(), tips, false);
	}

	//刷新数据
	synchronized private void refreshDataTraffics(List<DataTrafficOrderInfo> dataTrafficInfoList)
	{
		if (dataTrafficInfoList == null || dataTrafficInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mDataTrafficOrderInfoList.size() < 1)
			{
				layoutRechargeDataTrafficList.setVisibility(View.GONE);
				layoutNoRechargeDataTraffic.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutRechargeDataTrafficList.setVisibility(View.VISIBLE);
		layoutNoRechargeDataTraffic.setVisibility(View.GONE);
		xListView.setPullLoadEnable(dataTrafficInfoList.size() >= getPageSize());
		for (DataTrafficOrderInfo dataTrafficInfo : dataTrafficInfoList)
		{
			mDataTrafficOrderInfoList.add(dataTrafficInfo);
		}
		if (mDataTrafficOrderAdapter == null)
		{
			mDataTrafficOrderAdapter = new DataTrafficOrderAdapter(this, mDataTrafficOrderInfoList, new DataTrafficOrderAdapter.IItemClick()
			{
				@Override
				public void onOrderClicked(DataTrafficOrderInfo dataTrafficOrderInfo)
				{
				}

				@Override
				public void onOrderPayClicked(DataTrafficOrderInfo dataTrafficOrderInfo)
				{
					if (dataTrafficOrderInfo != null)
					{
						Intent intent = new Intent(ActivityRechargeDataTrafficList.this, ActivityPayList.class);
						intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, GsonTools.toJson(new PayTypeInfo(PayType.PAY_DATA_TRAFFIC_ORDER, dataTrafficOrderInfo.getTotalPrice(), "流量订单" + dataTrafficOrderInfo.getOrderId() + "支付", dataTrafficOrderInfo.getOrderId())));
						startActivityForResult(intent, REQ_PAY);
					}
				}

				@Override
				public void onOrderCancelClicked(final DataTrafficOrderInfo dataTrafficOrderInfo)
				{
					if (dataTrafficOrderInfo != null)
					{
						new DialogCommon(ActivityRechargeDataTrafficList.this, new DialogCommon.LibitDialogListener()
						{
							@Override
							public void onOkClick()
							{
								if (dataTrafficOrderInfo.getStatus() == OrderStatus.WAIT_PAY.getStatus())
								{
									mDataTrafficOrderService.deleteOrder(dataTrafficOrderInfo.getOrderId(), "正在取消订单，请稍后...", false);
								}
								else
								{
									ToastView.showCenterToast(ActivityRechargeDataTrafficList.this, R.drawable.ic_do_fail, "订单已生效，不能取消！");
								}
							}

							@Override
							public void onCancelClick()
							{
							}
						}, getString(R.string.title_warning), "确定要取消订单吗？", true, false, true).show();
					}
				}
			});
			xListView.setAdapter(mDataTrafficOrderAdapter);
		}
		else
		{
			mDataTrafficOrderAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_DATA_TRAFFIC_ORDER_LIST))
		{
			List<DataTrafficOrderInfo> dataTrafficOrderInfoList = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				dataTrafficOrderInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<DataTrafficOrderInfo>>()
				{
				}.getType());
			}
			refreshDataTraffics(dataTrafficOrderInfoList);
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		//		refreshData();
		finish();
	}
}
