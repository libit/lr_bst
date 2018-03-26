/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.UserInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UserAgentService;
import com.lrcall.ui.adapter.ReferrerUserAdapter;
import com.lrcall.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityAgentUserList extends MyBasePageActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityAgentUserList.class.getSimpleName();
	private View layoutUserList, layoutNoUser;
	private TextView tvInfo;
	private ReferrerUserAdapter mReferrerUserAdapter;
	private UserAgentService mUserAgentService;
	private final List<UserInfo> mUserInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_user_list);
		mUserAgentService = new UserAgentService(this);
		mUserAgentService.addDataResponse(this);
		viewInit();
		onRefresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_referrer_user_list, menu);
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
		layoutUserList = findViewById(R.id.layout_user_list);
		layoutNoUser = findViewById(R.id.layout_no_agent);
		tvInfo = (TextView) findViewById(R.id.tv_info);
		xListView = (XListView) findViewById(R.id.xlist);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
	}

	//刷新数据
	@Override
	public void refreshData()
	{
		mUserInfoList.clear();
		if (mReferrerUserAdapter != null)
		{
			mReferrerUserAdapter.notifyDataSetChanged();
		}
		mReferrerUserAdapter = null;
		loadMoreData();
	}

	//加载更多
	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		mUserAgentService.getUserAgentList(mDataStart, getPageSize(), tips, true);
	}

	synchronized private void refreshUserInfos(List<UserInfo> userInfoList)
	{
		if (userInfoList == null || userInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mUserInfoList.size() < 1)
			{
				layoutUserList.setVisibility(View.GONE);
				layoutNoUser.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutUserList.setVisibility(View.VISIBLE);
		layoutNoUser.setVisibility(View.GONE);
		xListView.setPullLoadEnable(userInfoList.size() >= getPageSize());
		for (UserInfo userInfo : userInfoList)
		{
			mUserInfoList.add(userInfo);
		}
		if (mReferrerUserAdapter == null)
		{
			mReferrerUserAdapter = new ReferrerUserAdapter(this, mUserInfoList, new ReferrerUserAdapter.IItemClick()
			{
				@Override
				public void onItemClicked(UserInfo userInfo)
				{
				}
			});
			xListView.setAdapter(mReferrerUserAdapter);
		}
		else
		{
			mReferrerUserAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_USER_AGENT_LIST))
		{
			List<UserInfo> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<UserInfo>>()
				{
				}.getType());
				tvInfo.setText(String.format("您的下级代理人数：%d人。", tableData.getRecordsTotal()));
			}
			refreshUserInfos(list);
		}
		return false;
	}
}
