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
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.UserBankInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UserBankService;
import com.lrcall.ui.adapter.UserBankAdapter;
import com.lrcall.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityUserBankList extends MyBasePageActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityUserBankList.class.getSimpleName();
	private View layoutLogList, layoutNoLog;
	private UserBankAdapter mUserBankAdapter;
	private UserBankService mUserBankService;
	private final List<UserBankInfo> mUserBankInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_bank_list);
		mUserBankService = new UserBankService(this);
		mUserBankService.addDataResponse(this);
		viewInit();
		onRefresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_user_bank_list, menu);
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
		else if (id == R.id.action_add)
		{
			startActivity(new Intent(this, ActivityUserBankAdd.class));
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
		mUserBankInfoList.clear();
		mUserBankAdapter = null;
		loadMoreData();
	}

	//加载更多
	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		mUserBankService.getUserBankInfoList(mDataStart, getPageSize(), null, null, tips, true);
	}

	synchronized private void refreshUserBankInfos(List<UserBankInfo> userBankInfoList)
	{
		if (userBankInfoList == null || userBankInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mUserBankInfoList.size() < 1)
			{
				layoutLogList.setVisibility(View.GONE);
				layoutNoLog.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutLogList.setVisibility(View.VISIBLE);
		layoutNoLog.setVisibility(View.GONE);
		if (userBankInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (UserBankInfo userBankInfo : userBankInfoList)
		{
			mUserBankInfoList.add(userBankInfo);
		}
		if (mUserBankAdapter == null)
		{
			mUserBankAdapter = new UserBankAdapter(this, mUserBankInfoList, new UserBankAdapter.IItemClicked()
			{
				@Override
				public void onUserBankClicked(UserBankInfo userBankInfo)
				{
				}
			});
			xListView.setAdapter(mUserBankAdapter);
		}
		else
		{
			mUserBankAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.USER_USER_BANK_LIST))
		{
			List<UserBankInfo> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<UserBankInfo>>()
				{
				}.getType());
			}
			refreshUserBankInfos(list);
		}
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK)
		{
			onRefresh();
		}
	}
}
