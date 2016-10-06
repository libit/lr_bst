/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;

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
import com.lrcall.appbst.services.ProductService;
import com.lrcall.ui.adapter.SearchProductsAdapter;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityProductHistoryList extends MyBaseActivity implements IAjaxDataResponse, XListView.IXListViewListener
{
	private static final String TAG = ActivityProductHistoryList.class.getSimpleName();
	private XListView xListView;
	private ProductHistoryService mProductHistoryService;
	private ProductService mProductService;
	private final List<ProductInfo> mProductInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_history_list);
		mProductHistoryService = new ProductHistoryService(this);
		mProductHistoryService.addDataResponse(this);
		mProductService = new ProductService(this);
		mProductService.addDataResponse(this);
		viewInit();
		initData();
		mProductHistoryService.getProductHistoryInfoList(null, true);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		xListView = (XListView) findViewById(R.id.xlist);
	}

	private void initData()
	{
		SearchProductsAdapter searchProductsAdapter = new SearchProductsAdapter(this, mProductInfoList, new SearchProductsAdapter.IProductsAdapterItemClicked()
		{
			@Override
			public void onProductClicked(ProductInfo productInfo)
			{
				Intent intent = new Intent(ActivityProductHistoryList.this, ActivityProduct.class);
				intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
				startActivity(intent);
			}
		});
		xListView.setAdapter(searchProductsAdapter);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
	}

	@Override
	public void onRefresh()
	{
		mProductHistoryService.getProductHistoryInfoList(null, true);
	}

	@Override
	public void onLoadMore()
	{
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_PRODUCT_HISTORY_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<ProductHistoryInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductHistoryInfo>>()
				{
				}.getType());
				if (list != null && list.size() > 0)
				{
					mProductInfoList.clear();
					for (ProductHistoryInfo productHistoryInfo : list)
					{
						ProductInfo productInfo = productHistoryInfo.getProductInfo();
						if (productInfo != null)
						{
							mProductInfoList.add(productInfo);
						}
						else
						{
							mProductService.getProductInfo(productHistoryInfo.getProductId(), null, true);
						}
					}
					initData();
				}
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_PRODUCT_INFO))
		{
			ProductInfo productInfo = GsonTools.getReturnObject(result, ProductInfo.class);
			if (productInfo != null)
			{
				mProductInfoList.add(productInfo);
			}
			return true;
		}
		return false;
	}
}
