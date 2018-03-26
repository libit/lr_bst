/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.CallbackRechargeLogInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.CallbackService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.ui.adapter.CallbackRechargeLogsAdapter;
import com.lrcall.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityCallbackRechargeLogList extends MyBasePageActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityCallbackRechargeLogList.class.getSimpleName();
	private View layoutRechargeLogList, layoutNoRechargeLog;
	private CallbackRechargeLogsAdapter mCallbackRechargeLogsAdapter;
	private CallbackService mCallbackService;
	private final List<CallbackRechargeLogInfo> mCallbackRechargeLogInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callback_recharge_log_list);
		mCallbackService = new CallbackService(this);
		mCallbackService.addDataResponse(this);
		viewInit();
		onRefresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_recharge_log_list, menu);
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
		layoutRechargeLogList = findViewById(R.id.layout_recharge_log_list);
		layoutNoRechargeLog = findViewById(R.id.layout_no_recharge_log);
		xListView = (XListView) findViewById(R.id.xlist);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
	}

	//刷新数据
	@Override
	public void refreshData()
	{
		mCallbackRechargeLogInfoList.clear();
		if (mCallbackRechargeLogsAdapter != null)
		{
			mCallbackRechargeLogsAdapter.notifyDataSetChanged();
		}
		mCallbackRechargeLogsAdapter = null;
		loadMoreData();
	}

	//加载更多商品
	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		mCallbackService.getRechargeLogList(mDataStart, getPageSize(), null, null, tips, true);
	}

	synchronized private void refreshRechargeLogs(List<CallbackRechargeLogInfo> callbackRechargeLogInfoList)
	{
		if (callbackRechargeLogInfoList == null || callbackRechargeLogInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mCallbackRechargeLogInfoList.size() < 1)
			{
				layoutRechargeLogList.setVisibility(View.GONE);
				layoutNoRechargeLog.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutRechargeLogList.setVisibility(View.VISIBLE);
		layoutNoRechargeLog.setVisibility(View.GONE);
		xListView.setPullLoadEnable(callbackRechargeLogInfoList.size() >= getPageSize());
		for (CallbackRechargeLogInfo callbackRechargeLogInfo : callbackRechargeLogInfoList)
		{
			mCallbackRechargeLogInfoList.add(callbackRechargeLogInfo);
		}
		if (mCallbackRechargeLogsAdapter == null)
		{
			mCallbackRechargeLogsAdapter = new CallbackRechargeLogsAdapter(this, mCallbackRechargeLogInfoList, new CallbackRechargeLogsAdapter.IItemClick()
			{
				@Override
				public void onItemClicked(CallbackRechargeLogInfo callbackRechargeLogInfo)
				{
				}
			});
			xListView.setAdapter(mCallbackRechargeLogsAdapter);
		}
		else
		{
			mCallbackRechargeLogsAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.CALLBACK_GET_RECHARGE_LOG_LIST))
		{
			List<CallbackRechargeLogInfo> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<CallbackRechargeLogInfo>>()
				{
				}.getType());
			}
			refreshRechargeLogs(list);
		}
		return false;
	}
}
