/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import com.lrcall.appbst.services.ProductService;
import com.lrcall.ui.adapter.SearchProductsAdapter;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class ActivitySearchProducts extends MyBasePageActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivitySearchProducts.class.getSimpleName();
	private View layoutProductList, layoutNoProduct;
	private EditText etSearch;
	private SearchProductsAdapter searchProductsAdapter;
	private String mSortId;
	private ProductService mProductService;
	private final List<ProductInfo> mProductInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_products);
		mSortId = getIntent().getStringExtra(ConstValues.DATA_PRODUCT_SORT_ID);
		viewInit();
		mProductService = new ProductService(this);
		mProductService.addDataResponse(this);
		onRefresh();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		layoutProductList = findViewById(R.id.layout_search_result);
		layoutNoProduct = findViewById(R.id.layout_no_product);
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
				if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND || (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
				{
					onRefresh();
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
		if (!StringTools.isNull(mSortId))
		{
			mProductService.getProductList(mSortId, mDataStart, getPageSize(), null, null, null, true);
		}
		else
		{
			mProductService.getProductList(condition, mDataStart, getPageSize(), null, null, null, true);
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.search_icon:
			{
				onRefresh();
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

	// 隐藏软键盘
	private void hideSoftPad()
	{
		etSearch.clearFocus();
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
	}

	synchronized private void refreshProducts(List<ProductInfo> productInfoList)
	{
		if (productInfoList == null || productInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mProductInfoList.size() < 1)
			{
				layoutProductList.setVisibility(View.GONE);
				layoutNoProduct.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutProductList.setVisibility(View.VISIBLE);
		layoutNoProduct.setVisibility(View.GONE);
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
			searchProductsAdapter = new SearchProductsAdapter(this, mProductInfoList, new SearchProductsAdapter.IItemClick()
			{
				@Override
				public void onProductClicked(ProductInfo productInfo)
				{
					if (productInfo != null)
					{
						Intent intent = new Intent(ActivitySearchProducts.this, ActivityProduct.class);
						intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
						startActivity(intent);
					}
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
		if (url.endsWith(ApiConfig.GET_PRODUCT_LIST))
		{
			List<ProductInfo> productInfoList = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				productInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductInfo>>()
				{
				}.getType());
			}
			refreshProducts(productInfoList);
		}
		return false;
	}
}
