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
import com.lrcall.appbst.models.UserWithdrawInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.WithdrawService;
import com.lrcall.ui.adapter.UserWithdrawAdapter;
import com.lrcall.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityWithdrawList extends MyBasePageActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityWithdrawList.class.getSimpleName();
	private View layoutLogList, layoutNoLog;
	private UserWithdrawAdapter mUserWithdrawAdapter;
	private WithdrawService mWithdrawService;
	private final List<UserWithdrawInfo> mUserWithdrawInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw_list);
		mWithdrawService = new WithdrawService(this);
		mWithdrawService.addDataResponse(this);
		viewInit();
		onRefresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_withdraw_list, menu);
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
		mUserWithdrawInfoList.clear();
		mUserWithdrawAdapter = null;
		loadMoreData();
	}

	//加载更多
	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		mWithdrawService.getUserWithdrawInfoList(mDataStart, getPageSize(), null, null, tips, true);
	}

	synchronized private void refreshUserWithdrawInfos(List<UserWithdrawInfo> userWithdrawInfoList)
	{
		if (userWithdrawInfoList == null || userWithdrawInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mUserWithdrawInfoList.size() < 1)
			{
				layoutLogList.setVisibility(View.GONE);
				layoutNoLog.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutLogList.setVisibility(View.VISIBLE);
		layoutNoLog.setVisibility(View.GONE);
		if (userWithdrawInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (UserWithdrawInfo userWithdrawInfo : userWithdrawInfoList)
		{
			mUserWithdrawInfoList.add(userWithdrawInfo);
		}
		if (mUserWithdrawAdapter == null)
		{
			mUserWithdrawAdapter = new UserWithdrawAdapter(this, mUserWithdrawInfoList, new UserWithdrawAdapter.IItemClicked()
			{
				@Override
				public void onWithdrawClicked(UserWithdrawInfo userWithdrawInfo)
				{
				}
			});
			xListView.setAdapter(mUserWithdrawAdapter);
		}
		else
		{
			mUserWithdrawAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.USER_WITHDRAW_LIST))
		{
			List<UserWithdrawInfo> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<UserWithdrawInfo>>()
				{
				}.getType());
			}
			refreshUserWithdrawInfos(list);
		}
		return false;
	}
}
