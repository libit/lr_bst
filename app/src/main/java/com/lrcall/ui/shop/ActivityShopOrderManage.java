/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.shop;

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
import com.lrcall.appbst.models.OrderSubInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ShopOrderService;
import com.lrcall.ui.ActivityOrderShip;
import com.lrcall.ui.MyBasePageActivity;
import com.lrcall.ui.adapter.ShopOrdersAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.dialog.DialogInputPrice;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityShopOrderManage extends MyBasePageActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityShopOrderManage.class.getSimpleName();
	private View layoutOrderList, layoutNoOrder;
	private EditText etSearch;
	private ShopOrdersAdapter mShopOrdersAdapter;
	private ShopOrderService mShopOrderService;
	private final List<OrderSubInfo> mOrderInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_order_manage);
		mShopOrderService = new ShopOrderService(this);
		mShopOrderService.addDataResponse(this);
		viewInit();
		onRefresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_shop_order_manage, menu);
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
		layoutOrderList = findViewById(R.id.layout_search_result);
		layoutNoOrder = findViewById(R.id.layout_no_order);
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
		mOrderInfoList.clear();
		if (mShopOrdersAdapter != null)
		{
			mShopOrdersAdapter.notifyDataSetChanged();
		}
		mShopOrdersAdapter = null;
		loadMoreData();
	}

	//加载更多商品
	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		String condition = etSearch.getText().toString();
		mShopOrderService.getOrderList(condition, mDataStart, getPageSize(), null, null, tips, true);
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
				break;
			}
		}
	}

	synchronized private void refreshOrders(List<OrderSubInfo> orderInfoList)
	{
		if (orderInfoList == null || orderInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mOrderInfoList.size() < 1)
			{
				layoutOrderList.setVisibility(View.GONE);
				layoutNoOrder.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutOrderList.setVisibility(View.VISIBLE);
		layoutNoOrder.setVisibility(View.GONE);
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
			mShopOrdersAdapter = new ShopOrdersAdapter(this, mOrderInfoList, new ShopOrdersAdapter.IItemClick()
			{
				@Override
				public void onOrderClicked(OrderSubInfo orderSubInfo)
				{
					if (orderSubInfo != null)
					{
						Intent intent = new Intent(ActivityShopOrderManage.this, ActivityShopOrderInfo.class);
						intent.putExtra(ConstValues.DATA_ORDER_ID, orderSubInfo.getOrderSubId());
						startActivity(intent);
					}
				}

				@Override
				public void onOrderChangeExpressClicked(final OrderSubInfo orderSubInfo)
				{
					if (orderSubInfo != null)
					{
						DialogInputPrice dialogInputPrice = new DialogInputPrice(ActivityShopOrderManage.this, new DialogInputPrice.OnInputListenser()
						{
							@Override
							public void onOkClick(String content)
							{
								try
								{
									int expressPrice = Integer.parseInt(content);
									mShopOrderService.orderChangeExpress(orderSubInfo.getOrderSubId(), expressPrice * 100, "请稍后...", true);
								}
								catch (NumberFormatException e)
								{
									ToastView.showCenterToast(ActivityShopOrderManage.this, R.drawable.ic_do_fail, "价格输入错误！");
								}
							}

							@Override
							public void onCancelClick()
							{
							}
						});
						dialogInputPrice.show();
					}
				}

				@Override
				public void onOrderSendExpressClicked(OrderSubInfo orderSubInfo)
				{
					if (orderSubInfo != null)
					{
						Intent intent = new Intent(ActivityShopOrderManage.this, ActivityOrderShip.class);
						intent.putExtra(ConstValues.DATA_ORDER_ID, orderSubInfo.getOrderSubId());
						startActivity(intent);
					}
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
			List<OrderSubInfo> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<OrderSubInfo>>()
				{
				}.getType());
			}
			refreshOrders(list);
		}
		else if (url.endsWith(ApiConfig.SHOP_ORDER_CHANGE_EXPRESS_PRICE))
		{
			showServerMsg(result, "修改运费成功！");
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				onRefresh();
			}
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK)
		{
			onRefresh();
		}
	}
}
