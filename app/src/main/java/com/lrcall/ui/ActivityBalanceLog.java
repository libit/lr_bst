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
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.UserBalanceLogInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UserBalanceLogService;
import com.lrcall.ui.adapter.UserBalanceLogAdapter;
import com.lrcall.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityBalanceLog extends MyBasePageActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityBalanceLog.class.getSimpleName();
	private View layoutLogList, layoutNoLog;
	private UserBalanceLogAdapter mUserBalanceLogAdapter;
	private UserBalanceLogService mUserBalanceLogService;
	private final List<UserBalanceLogInfo> mUserBalanceLogInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_balance_log);
		mUserBalanceLogService = new UserBalanceLogService(this);
		mUserBalanceLogService.addDataResponse(this);
		viewInit();
		onRefresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_balance_log, menu);
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
		layoutLogList = findViewById(R.id.layout_log_list);
		layoutNoLog = findViewById(R.id.layout_no_log);
		xListView = (XListView) findViewById(R.id.xlist);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
	}

	//刷新数据
	@Override
	public void refreshData()
	{
		mUserBalanceLogInfoList.clear();
		mUserBalanceLogAdapter = null;
		loadMoreData();
	}

	//加载更多商品
	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		mUserBalanceLogService.getUserBalanceLogList(mDataStart, getPageSize(), tips, true);
	}

	synchronized private void refreshUserBalanceLogs(List<UserBalanceLogInfo> userBalanceLogInfoList)
	{
		if (userBalanceLogInfoList == null || userBalanceLogInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mUserBalanceLogInfoList.size() < 1)
			{
				layoutLogList.setVisibility(View.GONE);
				layoutNoLog.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutLogList.setVisibility(View.VISIBLE);
		layoutNoLog.setVisibility(View.GONE);
		if (userBalanceLogInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (UserBalanceLogInfo userBalanceLogInfo : userBalanceLogInfoList)
		{
			mUserBalanceLogInfoList.add(userBalanceLogInfo);
		}
		if (mUserBalanceLogAdapter == null)
		{
			mUserBalanceLogAdapter = new UserBalanceLogAdapter(this, mUserBalanceLogInfoList, new UserBalanceLogAdapter.IItemClicked()
			{
				@Override
				public void onUserBalanceLogClicked(UserBalanceLogInfo userBalanceLogInfo)
				{
					if (userBalanceLogInfo != null)
					{
					}
				}
			});
			xListView.setAdapter(mUserBalanceLogAdapter);
		}
		else
		{
			mUserBalanceLogAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_USER_BALANCE_LOG_LIST))
		{
			List<UserBalanceLogInfo> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<UserBalanceLogInfo>>()
				{
				}.getType());
			}
			refreshUserBalanceLogs(list);
		}
		return false;
	}
}
