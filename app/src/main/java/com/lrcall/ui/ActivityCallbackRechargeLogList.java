/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
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
import com.lrcall.ui.customer.DisplayTools;
import com.lrcall.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityCallbackRechargeLogList extends MyBasePageActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityCallbackRechargeLogList.class.getSimpleName();
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
		refreshData();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		//设置滑动返回区域
		getSwipeBackLayout().setEdgeSize(DisplayTools.getWindowWidth(this) / 4);
		setBackButton();
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
		mCallbackRechargeLogsAdapter = null;
		loadMoreData();
	}

	//加载更多商品
	@Override
	public void loadMoreData()
	{
		mCallbackService.getRechargeLogList(mDataStart, getPageSize(), null, true);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.search_icon:
			{
				break;
			}
		}
	}

	synchronized private void refreshRechargeLogs(List<CallbackRechargeLogInfo> callbackRechargeLogInfoList)
	{
		if (callbackRechargeLogInfoList == null || callbackRechargeLogInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			return;
		}
		if (callbackRechargeLogInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (CallbackRechargeLogInfo callbackRechargeLogInfo : callbackRechargeLogInfoList)
		{
			mCallbackRechargeLogInfoList.add(callbackRechargeLogInfo);
		}
		if (mCallbackRechargeLogsAdapter == null)
		{
			mCallbackRechargeLogsAdapter = new CallbackRechargeLogsAdapter(this, mCallbackRechargeLogInfoList, new CallbackRechargeLogsAdapter.ICallbackRechargeLogsAdapterItemClicked()
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
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<CallbackRechargeLogInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<CallbackRechargeLogInfo>>()
				{
				}.getType());
				refreshRechargeLogs(list);
			}
		}
		return false;
	}
}
