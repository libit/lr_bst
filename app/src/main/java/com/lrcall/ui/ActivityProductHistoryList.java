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
import com.lrcall.appbst.models.ProductHistoryInfo;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ProductHistoryService;
import com.lrcall.events.ProductEvent;
import com.lrcall.ui.adapter.ProductHistoryAdapter;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ActivityProductHistoryList extends MyBasePageActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityProductHistoryList.class.getSimpleName();
	private View layoutHistoryList, layoutNoHistory;
	private ProductHistoryService mProductHistoryService;
	private final List<ProductHistoryInfo> mProductHistoryInfoList = new ArrayList<>();
	private ProductHistoryAdapter mProductHistoryAdapter;

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_product_history_list, menu);
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
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_history_list);
		mProductHistoryService = new ProductHistoryService(this);
		mProductHistoryService.addDataResponse(this);
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
	public void onEventMainThread(ProductEvent productEvent)
	{
		if (productEvent != null)
		{
			if (productEvent.getEvent().equals(ProductEvent.EVENT_HISTORY_ADD))
			{
				onRefresh();
			}
		}
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		layoutHistoryList = findViewById(R.id.layout_history_list);
		layoutNoHistory = findViewById(R.id.layout_no_history);
		xListView = (XListView) findViewById(R.id.xlist);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
	}

	synchronized private void refreshProductHistoryList(List<ProductHistoryInfo> productHistoryInfoList)
	{
		if (productHistoryInfoList == null || productHistoryInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mProductHistoryInfoList.size() < 1)
			{
				layoutHistoryList.setVisibility(View.GONE);
				layoutNoHistory.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutHistoryList.setVisibility(View.VISIBLE);
		layoutNoHistory.setVisibility(View.GONE);
		if (productHistoryInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (ProductHistoryInfo productHistoryInfo : productHistoryInfoList)
		{
			mProductHistoryInfoList.add(productHistoryInfo);
		}
		if (mProductHistoryAdapter == null)
		{
			mProductHistoryAdapter = new ProductHistoryAdapter(this, mProductHistoryInfoList, new ProductHistoryAdapter.IItemClicked()
			{
				@Override
				public void onProductClicked(ProductInfo productInfo)
				{
					if (productInfo != null)
					{
						Intent intent = new Intent(ActivityProductHistoryList.this, ActivityProduct.class);
						intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
						startActivity(intent);
					}
				}
			});
			xListView.setAdapter(mProductHistoryAdapter);
		}
		else
		{
			mProductHistoryAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void refreshData()
	{
		mProductHistoryInfoList.clear();
		mProductHistoryAdapter = null;
		loadMoreData();
	}

	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		mProductHistoryService.getProductHistoryInfoList(null, mDataStart, getPageSize(), null, null, tips, true);
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_PRODUCT_HISTORY_LIST))
		{
			List<ProductHistoryInfo> productHistoryInfoList = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				productHistoryInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductHistoryInfo>>()
				{
				}.getType());
			}
			refreshProductHistoryList(productHistoryInfoList);
			return true;
		}
		return false;
	}
}
