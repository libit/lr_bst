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
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.OrderSubInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ShopOrderService;
import com.lrcall.ui.adapter.ShopOrdersAdapter;
import com.lrcall.ui.customer.DisplayTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityShopOrdersManage extends MyBasePageActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityShopOrdersManage.class.getSimpleName();
	private EditText etSearch;
	private ShopOrdersAdapter mShopOrdersAdapter;
	private ShopOrderService mShopOrderService;
	private final List<OrderSubInfo> mOrderInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_orders_manage);
		mShopOrderService = new ShopOrderService(this);
		mShopOrderService.addDataResponse(this);
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
		mOrderInfoList.clear();
		mShopOrdersAdapter = null;
		loadMoreData();
	}

	//加载更多商品
	@Override
	public void loadMoreData()
	{
		String condition = etSearch.getText().toString();
		mShopOrderService.getOrderList(condition, mDataStart, getPageSize(), null, null, false, null, true);
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

	synchronized private void refreshOrders(List<OrderSubInfo> orderInfoList)
	{
		if (orderInfoList == null || orderInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			return;
		}
		if (orderInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (OrderSubInfo orderInfo : orderInfoList)
		{
			mOrderInfoList.add(orderInfo);
		}
		if (mShopOrdersAdapter == null)
		{
			mShopOrdersAdapter = new ShopOrdersAdapter(this, mOrderInfoList, new ShopOrdersAdapter.IOrdersAdapterItemClicked()
			{
				@Override
				public void onOrderClicked(OrderSubInfo orderInfo)
				{
					Intent intent = new Intent(ActivityShopOrdersManage.this, ActivityShopOrderInfo.class);
					intent.putExtra(ConstValues.DATA_ORDER_ID, orderInfo.getOrderSubId());
					startActivity(intent);
				}

				@Override
				public void onOrderSendExpressClicked(OrderSubInfo orderInfo)
				{
				}
			});
			xListView.setAdapter(mShopOrdersAdapter);
		}
		else
		{
			mShopOrdersAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_SHOP_ORDER_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<OrderSubInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<OrderSubInfo>>()
				{
				}.getType());
				refreshOrders(list);
			}
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK)
		{
			refreshData();
		}
	}
}
