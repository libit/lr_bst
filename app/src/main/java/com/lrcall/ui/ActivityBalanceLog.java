/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;

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
	private UserBalanceLogAdapter userBalanceLogAdapter;
	private UserBalanceLogService mUserBalanceLogService;
	private final List<UserBalanceLogInfo> mUserBalanceLogInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_balance_log);
		viewInit();
		mUserBalanceLogService = new UserBalanceLogService(this);
		mUserBalanceLogService.addDataResponse(this);
		refreshData();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
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
		mUserBalanceLogInfoList.clear();
		userBalanceLogAdapter = null;
		loadMoreData();
	}

	//加载更多商品
	@Override
	public void loadMoreData()
	{
		mUserBalanceLogService.getUserBalanceLogList(mDataStart, getPageSize(), null, true);
	}

	synchronized private void refreshUserBalanceLogs(List<UserBalanceLogInfo> userBalanceLogInfoList)
	{
		if (userBalanceLogInfoList == null || userBalanceLogInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			return;
		}
		if (userBalanceLogInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (UserBalanceLogInfo userBalanceLogInfo : userBalanceLogInfoList)
		{
			mUserBalanceLogInfoList.add(userBalanceLogInfo);
		}
		if (userBalanceLogAdapter == null)
		{
			userBalanceLogAdapter = new UserBalanceLogAdapter(this, mUserBalanceLogInfoList, new UserBalanceLogAdapter.IUserBalanceLogAdapterItemClicked()
			{
				@Override
				public void onUserBalanceLogClicked(UserBalanceLogInfo userBalanceLogInfo)
				{
					//					Intent intent = new Intent(ActivityBalanceLog.this, ActivityProduct.class);
					//					intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
					//					startActivity(intent);
				}
			});
			xListView.setAdapter(userBalanceLogAdapter);
		}
		else
		{
			userBalanceLogAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_USER_BALANCE_LOG_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<UserBalanceLogInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<UserBalanceLogInfo>>()
				{
				}.getType());
				refreshUserBalanceLogs(list);
			}
		}
		return false;
	}
}
