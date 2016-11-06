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
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.ProductStarInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ProductStarService;
import com.lrcall.events.ProductEvent;
import com.lrcall.ui.adapter.ProductStarAdapter;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ActivityProductStarList extends MyBasePageActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityProductStarList.class.getSimpleName();
	private View layoutStarList, layoutNoStar;
	private ProductStarService mProductStarService;
	//	private ProductService mProductService;
	private final List<ProductStarInfo> mProductStarInfoList = new ArrayList<>();
	private ProductStarAdapter mProductStarAdapter;
	//	@Override
	//	protected int getPageSize()
	//	{
	//		return 1;
	//	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_star_list);
		mProductStarService = new ProductStarService(this);
		mProductStarService.addDataResponse(this);
		//		mProductService = new ProductService(this);
		//		mProductService.addDataResponse(this);
		viewInit();
		//		List<ProductStarInfo> productStarInfoList = DbProductStarInfoFactory.getInstance().getProductStarInfoList(PreferenceUtils.getInstance().getUsername());
		//		if (productStarInfoList != null && productStarInfoList.size() > 0)
		//		{
		//			for (ProductStarInfo productStarInfo : productStarInfoList)
		//			{
		//				ProductInfo productInfo = DbProductInfoFactory.getInstance().getProductInfo(productStarInfo.getProductId());
		//				if (productInfo != null)
		//				{
		//					mProductStarInfoList.add(productInfo);
		//				}
		//			}
		//			refreshProductList(productStarInfoList);
		//		}
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
			if (productEvent.getEvent().equals(ProductEvent.EVENT_STAR_ADD) || productEvent.getEvent().equals(ProductEvent.EVENT_STAR_CANCEL))
			{
				onRefresh();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_product_star_list, menu);
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
		layoutStarList = findViewById(R.id.layout_star_list);
		layoutNoStar = findViewById(R.id.layout_no_star);
		xListView = (XListView) findViewById(R.id.xlist);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
	}

	@Override
	public void refreshData()
	{
		mProductStarInfoList.clear();
		mProductStarAdapter = null;
		loadMoreData();
	}

	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		mProductStarService.getProductStarInfoList(null, mDataStart, getPageSize(), null, null, tips, true);
	}

	synchronized private void refreshProductList(List<ProductStarInfo> productStarInfoList)
	{
		if (productStarInfoList == null || productStarInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mProductStarInfoList.size() < 1)
			{
				layoutStarList.setVisibility(View.GONE);
				layoutNoStar.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutStarList.setVisibility(View.VISIBLE);
		layoutNoStar.setVisibility(View.GONE);
		if (productStarInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (ProductStarInfo productStarInfo : productStarInfoList)
		{
			mProductStarInfoList.add(productStarInfo);
		}
		if (mProductStarAdapter == null)
		{
			mProductStarAdapter = new ProductStarAdapter(this, mProductStarInfoList, new ProductStarAdapter.IItemClicked()
			{
				@Override
				public void onProductClicked(ProductInfo productInfo)
				{
					if (productInfo != null)
					{
						Intent intent = new Intent(ActivityProductStarList.this, ActivityProduct.class);
						intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
						startActivity(intent);
					}
				}
			});
			xListView.setAdapter(mProductStarAdapter);
		}
		else
		{
			mProductStarAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_STAR_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			List<ProductStarInfo> productStarInfoList = null;
			if (tableData != null)
			{
				productStarInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductStarInfo>>()
				{
				}.getType());
			}
			refreshProductList(productStarInfoList);
			return true;
		}
		return false;
	}
}
