/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ShopProductService;
import com.lrcall.ui.adapter.SearchProductsAdapter;
import com.lrcall.ui.customer.DisplayTools;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityChooseAgentProduct extends MyBasePageActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityChooseAgentProduct.class.getSimpleName();
	private EditText etSearch;
	private ListView lvSearchHistory;
	private SearchProductsAdapter searchProductsAdapter;
	private ShopProductService mProductService;
	private final List<ProductInfo> mProductInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_agent_product);
		mProductService = new ShopProductService(this);
		mProductService.addDataResponse(this);
		viewInit();
		refreshData();
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
		lvSearchHistory = (ListView) findViewById(R.id.list_search_history);
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
		mProductService.getAdminProductList(condition, mDataStart, getPageSize(), null, null, false, null, true);
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
					mProductService.chooseAgetnProduct(productInfo.getProductId(), "请稍后...", true);
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
		if (url.endsWith(ApiConfig.GET_ADMIN_PRODUCT_LIST))
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
		else if (url.endsWith(ApiConfig.SHOP_ADD_PRODUCT_AGENT))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				ToastView.showCenterToast(this, R.drawable.ic_done, "代理商品成功！");
				setResult(RESULT_OK);
				finish();
			}
			else
			{
				String msg = "";
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, "代理失败：" + msg);
			}
		}
		return false;
	}
}
