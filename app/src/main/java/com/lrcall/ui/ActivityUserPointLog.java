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
import com.lrcall.appbst.models.PointLogInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PointLogService;
import com.lrcall.ui.adapter.UserPointLogAdapter;
import com.lrcall.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityUserPointLog extends MyBasePageActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityUserPointLog.class.getSimpleName();
	private View layoutLogList, layoutNoLog;
	private UserPointLogAdapter mUserPointLogAdapter;
	private PointLogService mPointLogService;
	private final List<PointLogInfo> mPointLogInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_point_log);
		mPointLogService = new PointLogService(this);
		mPointLogService.addDataResponse(this);
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
		mPointLogInfoList.clear();
		mUserPointLogAdapter = null;
		loadMoreData();
	}

	//加载更多商品
	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		mPointLogService.getPointLogInfoList(mDataStart, getPageSize(), tips, true);
	}

	synchronized private void refreshPointLogInfos(List<PointLogInfo> pointLogInfoList)
	{
		if (pointLogInfoList == null || pointLogInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mPointLogInfoList.size() < 1)
			{
				layoutLogList.setVisibility(View.GONE);
				layoutNoLog.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutLogList.setVisibility(View.VISIBLE);
		layoutNoLog.setVisibility(View.GONE);
		if (pointLogInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (PointLogInfo pointLogInfo : pointLogInfoList)
		{
			mPointLogInfoList.add(pointLogInfo);
		}
		if (mUserPointLogAdapter == null)
		{
			mUserPointLogAdapter = new UserPointLogAdapter(this, mPointLogInfoList, new UserPointLogAdapter.IItemClicked()
			{
				@Override
				public void onPointLogInfoClicked(PointLogInfo pointLogInfo)
				{
					if (pointLogInfo != null)
					{
					}
				}
			});
			xListView.setAdapter(mUserPointLogAdapter);
		}
		else
		{
			mUserPointLogAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_USER_POINT_LOG_LIST))
		{
			List<PointLogInfo> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<PointLogInfo>>()
				{
				}.getType());
			}
			refreshPointLogInfos(list);
		}
		return false;
	}
}
