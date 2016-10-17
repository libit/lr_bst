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
import com.lrcall.appbst.models.CallbackCallLogInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.CallbackService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.ui.adapter.CallbackCallLogsAdapter;
import com.lrcall.ui.customer.DisplayTools;
import com.lrcall.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityCallbackCallLogList extends MyBasePageActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityCallbackCallLogList.class.getSimpleName();
	private CallbackCallLogsAdapter mCallbackCallLogsAdapter;
	private CallbackService mCallbackService;
	private final List<CallbackCallLogInfo> mCallbackCallLogInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callback_calllog_list);
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
		mCallbackCallLogInfoList.clear();
		mCallbackCallLogsAdapter = null;
		loadMoreData();
	}

	//加载更多商品
	@Override
	public void loadMoreData()
	{
		mCallbackService.getCallLogList(mDataStart, getPageSize(), null, true);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			//			case R.id.search_icon:
			//			{
			//				search();
			//				break;
			//			}
		}
	}

	synchronized private void refreshCallLogs(List<CallbackCallLogInfo> callbackCallLogInfoList)
	{
		if (callbackCallLogInfoList == null || callbackCallLogInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			return;
		}
		if (callbackCallLogInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (CallbackCallLogInfo callbackCallLogInfo : callbackCallLogInfoList)
		{
			mCallbackCallLogInfoList.add(callbackCallLogInfo);
		}
		if (mCallbackCallLogsAdapter == null)
		{
			mCallbackCallLogsAdapter = new CallbackCallLogsAdapter(this, mCallbackCallLogInfoList, new CallbackCallLogsAdapter.ICallbackCallLogsAdapterItemClicked()
			{
				@Override
				public void onItemClicked(CallbackCallLogInfo callLogInfo)
				{
				}

				@Override
				public void onCallClicked(CallbackCallLogInfo callLogInfo)
				{
				}
			});
			xListView.setAdapter(mCallbackCallLogsAdapter);
		}
		else
		{
			mCallbackCallLogsAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.CALLBACK_GET_CALL_LOG_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<CallbackCallLogInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<CallbackCallLogInfo>>()
				{
				}.getType());
				refreshCallLogs(list);
			}
		}
		return false;
	}
}
