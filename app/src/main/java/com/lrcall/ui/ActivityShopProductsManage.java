/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ShopProductService;
import com.lrcall.ui.adapter.SearchProductsAdapter;
import com.lrcall.ui.customer.DisplayTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityShopProductsManage extends MyBasePageActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityShopProductsManage.class.getSimpleName();
	public static final int REQ_EDIT = 300;
	public static final int REQ_ADD = 301;
	private EditText etSearch;
	private SearchProductsAdapter searchProductsAdapter;
	private ShopProductService mProductService;
	private final List<ProductInfo> mProductInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_products);
		viewInit();
		mProductService = new ShopProductService(this);
		mProductService.addDataResponse(this);
		mProductService.getProductList(null, mDataStart, getPageSize(), null, null, false, null, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_shop_product_manage, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_add_product)
		{
			startActivityForResult(new Intent(this, ActivityProductAdd.class), REQ_ADD);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		//设置滑动返回区域
		getSwipeBackLayout().setEdgeSize(DisplayTools.getWindowWidth(this) / 4);
		setBackButton();
		xListView = (XListView) findViewById(R.id.xlist);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
		etSearch = (EditText) findViewById(R.id.et_search);
		etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
				{
					search();
					return true;
				}
				return false;
			}
		});
		findViewById(R.id.search_icon).setOnClickListener(this);
		findViewById(R.id.search_del).setOnClickListener(this);
	}

	//刷新数据
	@Override
	public void refreshData()
	{
		mProductInfoList.clear();
		searchProductsAdapter = null;
		loadMoreData();
	}

	//加载更多商品
	@Override
	public void loadMoreData()
	{
		String condition = etSearch.getText().toString();
		mProductService.getProductList(condition, mDataStart, getPageSize(), null, null, false, null, true);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.search_icon:
			{
				search();
				break;
			}
			case R.id.search_del:
			{
				String number = etSearch.getText().toString();
				if (!StringTools.isNull(number))
				{
					etSearch.setTextKeepState(number.substring(0, number.length() - 1));
				}
				//				search();
				break;
			}
		}
	}

	//开始搜索
	private void search()
	{
		String condition = etSearch.getText().toString();
		if (!StringTools.isNull(condition))
		{
			onRefresh();
		}
		else
		{
			xListView.setAdapter(null);
		}
	}

	synchronized private void refreshProducts(List<ProductInfo> productInfoList)
	{
		if (productInfoList == null || productInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			return;
		}
		if (productInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (ProductInfo productInfo : productInfoList)
		{
			mProductInfoList.add(productInfo);
		}
		if (searchProductsAdapter == null)
		{
			searchProductsAdapter = new SearchProductsAdapter(this, mProductInfoList, new SearchProductsAdapter.IProductsAdapterItemClicked()
			{
				@Override
				public void onProductClicked(ProductInfo productInfo)
				{
					Intent intent = new Intent(ActivityShopProductsManage.this, ActivityProduct.class);
					intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
					startActivity(intent);
				}
			});
			xListView.setAdapter(searchProductsAdapter);
		}
		else
		{
			searchProductsAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_SHOP_PRODUCT_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<ProductInfo> productInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductInfo>>()
				{
				}.getType());
				refreshProducts(productInfoList);
			}
		}
		return false;
	}
}
