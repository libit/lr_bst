/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.CallbackCallLogInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.CallbackService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.events.CallLogEvent;
import com.lrcall.ui.adapter.CallbackCallLogsAdapter;
import com.lrcall.utils.CallTools;
import com.lrcall.utils.GsonTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ActivityCallbackCallLogList extends MyBasePageActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityCallbackCallLogList.class.getSimpleName();
	private View layoutCalllogList, layoutNoCalllog;
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
		EventBus.getDefault().register(this);
		onRefresh();
	}

	@Override
	protected void onDestroy()
	{
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Subscribe
	public void onEventMainThread(CallLogEvent callLogEvent)
	{
		if (callLogEvent != null)
		{
			if (callLogEvent.getEvent().equals(CallLogEvent.EVENT_CALLLOG_ADD) || callLogEvent.getEvent().equals(CallLogEvent.EVENT_CALLLOG_DELETE))
			{
				onRefresh();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_calllog_list, menu);
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
		layoutCalllogList = findViewById(R.id.layout_calllog_list);
		layoutNoCalllog = findViewById(R.id.layout_no_calllog);
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
		if (mCallbackCallLogsAdapter != null)
		{
			mCallbackCallLogsAdapter.notifyDataSetChanged();
		}
		mCallbackCallLogsAdapter = null;
		loadMoreData();
	}

	//加载更多商品
	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		mCallbackService.getCallLogList(mDataStart, getPageSize(), null, null, tips, true);
	}

	synchronized private void refreshCallLogs(List<CallbackCallLogInfo> callbackCallLogInfoList)
	{
		if (callbackCallLogInfoList == null || callbackCallLogInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mCallbackCallLogInfoList.size() < 1)
			{
				layoutCalllogList.setVisibility(View.GONE);
				layoutNoCalllog.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutCalllogList.setVisibility(View.VISIBLE);
		layoutNoCalllog.setVisibility(View.GONE);
		xListView.setPullLoadEnable(callbackCallLogInfoList.size() >= getPageSize());
		for (CallbackCallLogInfo callbackCallLogInfo : callbackCallLogInfoList)
		{
			mCallbackCallLogInfoList.add(callbackCallLogInfo);
		}
		if (mCallbackCallLogsAdapter == null)
		{
			mCallbackCallLogsAdapter = new CallbackCallLogsAdapter(this, mCallbackCallLogInfoList, new CallbackCallLogsAdapter.IItemClick()
			{
				@Override
				public void onItemClicked(CallbackCallLogInfo callLogInfo)
				{
				}

				@Override
				public void onCallClicked(CallbackCallLogInfo callLogInfo)
				{
					if (callLogInfo != null)
					{
						ReturnInfo returnInfo = CallTools.makeCall(ActivityCallbackCallLogList.this, callLogInfo.getNumber());
						if (!ReturnInfo.isSuccess(returnInfo))
						{
							Toast.makeText(ActivityCallbackCallLogList.this, returnInfo.getMsg(), Toast.LENGTH_LONG).show();
						}
					}
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
			List<CallbackCallLogInfo> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<CallbackCallLogInfo>>()
				{
				}.getType());
			}
			refreshCallLogs(list);
		}
		return false;
	}
}
