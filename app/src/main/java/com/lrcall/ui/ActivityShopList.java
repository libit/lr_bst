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
import com.lrcall.appbst.models.ShopInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ShopService;
import com.lrcall.ui.adapter.ShopAdapter;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityShopList extends MyBasePageActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityShopList.class.getSimpleName();
	private View layoutShopList, layoutNoShop;
	private ShopService mShopService;
	private final List<ShopInfo> mShopInfoList = new ArrayList<>();
	private ShopAdapter mShopAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_list);
		mShopService = new ShopService(this);
		mShopService.addDataResponse(this);
		viewInit();
		onRefresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_shop_list, menu);
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
		layoutShopList = findViewById(R.id.layout_shop_list);
		layoutNoShop = findViewById(R.id.layout_no_shop);
		xListView = (XListView) findViewById(R.id.xlist);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
	}

	@Override
	public void refreshData()
	{
		mShopInfoList.clear();
		mShopAdapter = null;
		loadMoreData();
	}

	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		mShopService.getShopList(null, mDataStart, getPageSize(), null, null, tips, true);
	}

	synchronized private void refreshShopList(List<ShopInfo> shopInfoList)
	{
		if (shopInfoList == null || shopInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mShopInfoList.size() < 1)
			{
				layoutShopList.setVisibility(View.GONE);
				layoutNoShop.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutShopList.setVisibility(View.VISIBLE);
		layoutNoShop.setVisibility(View.GONE);
		if (shopInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (ShopInfo shopInfo : shopInfoList)
		{
			mShopInfoList.add(shopInfo);
		}
		if (mShopAdapter == null)
		{
			mShopAdapter = new ShopAdapter(this, mShopInfoList, new ShopAdapter.IItemClicked()
			{
				@Override
				public void onShopClicked(ShopInfo shopInfo)
				{
					if (shopInfo != null)
					{
						Intent intent = new Intent(ActivityShopList.this, ActivityShopProducts.class);
						intent.putExtra(ConstValues.DATA_SHOP_ID, shopInfo.getShopId());
						startActivity(intent);
						ActivityShopList.this.finish();
					}
				}
			});
			xListView.setAdapter(mShopAdapter);
		}
		else
		{
			mShopAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_SHOP_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			List<ShopInfo> list = null;
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ShopInfo>>()
				{
				}.getType());
			}
			refreshShopList(list);
			return true;
		}
		return false;
	}
}
