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
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.easeui.EaseConstant;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ClientConfigInfo;
import com.lrcall.appbst.models.HXKefuInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UserService;
import com.lrcall.ui.adapter.HxKefuAdapter;
import com.lrcall.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityImKefuList extends MyBasePageActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityImKefuList.class.getSimpleName();
	private View layoutKefuList, layoutNoKefu;
	private HxKefuAdapter mHxKefuAdapter;
	private UserService mUserService;
	private final List<HXKefuInfo> mHXKefuInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_im_kefu_list);
		mUserService = new UserService(this);
		mUserService.addDataResponse(this);
		viewInit();
		onRefresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_im_kefu_list, menu);
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
		layoutKefuList = findViewById(R.id.layout_kefu_list);
		layoutNoKefu = findViewById(R.id.layout_no_kefu);
		xListView = (XListView) findViewById(R.id.xlist);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
	}

	//刷新数据
	@Override
	public void refreshData()
	{
		mHXKefuInfoList.clear();
		if (mHxKefuAdapter != null)
		{
			mHxKefuAdapter.notifyDataSetChanged();
		}
		mHxKefuAdapter = null;
		loadMoreData();
	}

	//加载更多商品
	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		mUserService.getClientConfig(tips, true);
	}

	synchronized private void refreshUserBalanceLogs(List<HXKefuInfo> hxKefuInfoList)
	{
		if (hxKefuInfoList == null || hxKefuInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mHXKefuInfoList.size() < 1)
			{
				layoutKefuList.setVisibility(View.GONE);
				layoutNoKefu.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutKefuList.setVisibility(View.VISIBLE);
		layoutNoKefu.setVisibility(View.GONE);
		xListView.setPullLoadEnable(hxKefuInfoList.size() >= getPageSize());
		for (HXKefuInfo hxKefuInfo : hxKefuInfoList)
		{
			mHXKefuInfoList.add(hxKefuInfo);
		}
		if (mHxKefuAdapter == null)
		{
			mHxKefuAdapter = new HxKefuAdapter(this, mHXKefuInfoList, new HxKefuAdapter.IItemClick()
			{
				@Override
				public void onHXKefuClicked(final HXKefuInfo kefuInfo)
				{
					if (kefuInfo != null)
					{
						startActivity(new Intent(ActivityImKefuList.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, kefuInfo.getUsername()).putExtra(EaseConstant.DATA_NICK_NAME, kefuInfo.getNickname()));
						finish();
					}
				}
			});
			xListView.setAdapter(mHxKefuAdapter);
		}
		else
		{
			mHxKefuAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_CLIENT_CONFIG))
		{
			List<HXKefuInfo> list = null;
			ClientConfigInfo clientConfigInfo = GsonTools.getReturnObject(result, ClientConfigInfo.class);
			if (clientConfigInfo != null)
			{
				list = GsonTools.getObjects(clientConfigInfo.getImKefus(), new TypeToken<List<HXKefuInfo>>()
				{
				}.getType());
			}
			refreshUserBalanceLogs(list);
		}
		return false;
	}
}
