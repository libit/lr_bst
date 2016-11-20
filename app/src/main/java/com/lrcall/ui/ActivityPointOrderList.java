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
import com.lrcall.appbst.models.PayTypeInfo;
import com.lrcall.appbst.models.PointOrderInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PointOrderService;
import com.lrcall.enums.OrderStatus;
import com.lrcall.enums.PayType;
import com.lrcall.ui.adapter.PointOrderAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.dialog.DialogCommon;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityPointOrderList extends MyBasePageActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityPointOrderList.class.getSimpleName();
	public static final int REQ_PAY = 1005;
	private View layoutOrderList, layoutNoOrder;
	private PointOrderService mPointOrderService;
	private List<PointOrderInfo> mPointOrderInfoList = new ArrayList<>();
	private PointOrderAdapter mPointOrderAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_point_order_list);
		mPointOrderService = new PointOrderService(this);
		mPointOrderService.addDataResponse(this);
		viewInit();
		onRefresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_point_order_list, menu);
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
		layoutOrderList = findViewById(R.id.layout_order_list);
		layoutNoOrder = findViewById(R.id.layout_no_order);
		xListView = (XListView) findViewById(R.id.xlist);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
	}

	@Override
	public void refreshData()
	{
		mPointOrderInfoList.clear();
		mPointOrderAdapter = null;
		loadMoreData();
	}

	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		mPointOrderService.getOrderInfoList(null, mDataStart, getPageSize(), tips, false);
	}

	//刷新数据
	synchronized private void refreshPointOrders(List<PointOrderInfo> pointOrderInfoList)
	{
		if (pointOrderInfoList == null || pointOrderInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mPointOrderInfoList.size() < 1)
			{
				layoutOrderList.setVisibility(View.GONE);
				layoutNoOrder.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutOrderList.setVisibility(View.VISIBLE);
		layoutNoOrder.setVisibility(View.GONE);
		if (pointOrderInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (PointOrderInfo pointOrderInfo : pointOrderInfoList)
		{
			mPointOrderInfoList.add(pointOrderInfo);
		}
		if (mPointOrderAdapter == null)
		{
			mPointOrderAdapter = new PointOrderAdapter(this, mPointOrderInfoList, new PointOrderAdapter.IItemClick()
			{
				@Override
				public void onOrderClicked(PointOrderInfo pointOrderInfo)
				{
				}

				@Override
				public void onOrderPayClicked(PointOrderInfo pointOrderInfo)
				{
					if (pointOrderInfo != null)
					{
						Intent intent = new Intent(ActivityPointOrderList.this, ActivityPayList.class);
						PayTypeInfo payTypeInfo = new PayTypeInfo(PayType.PAY_POINT_ORDER, pointOrderInfo.getTotalPrice(), "积分订单" + pointOrderInfo.getOrderId() + "支付", pointOrderInfo.getOrderId());
						intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, GsonTools.toJson(payTypeInfo));
						startActivityForResult(intent, REQ_PAY);
					}
				}

				@Override
				public void onOrderCancelClicked(final PointOrderInfo pointOrderInfo)
				{
					if (pointOrderInfo != null)
					{
						new DialogCommon(ActivityPointOrderList.this, new DialogCommon.LibitDialogListener()
						{
							@Override
							public void onOkClick()
							{
								if (pointOrderInfo.getStatus() == OrderStatus.WAIT_PAY.getStatus())
								{
									mPointOrderService.deleteOrder(pointOrderInfo.getOrderId(), "正在取消订单，请稍后...", true);
								}
								else
								{
									ToastView.showCenterToast(ActivityPointOrderList.this, R.drawable.ic_do_fail, "订单已生效，不能取消！");
								}
							}

							@Override
							public void onCancelClick()
							{
							}
						}, getString(R.string.title_warning), "确定要取消订单吗？", true, false, true).show();
					}
				}
			});
			xListView.setAdapter(mPointOrderAdapter);
		}
		else
		{
			mPointOrderAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_POINT_ORDER_LIST))
		{
			List<PointOrderInfo> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<PointOrderInfo>>()
				{
				}.getType());
			}
			refreshPointOrders(list);
			return true;
		}
		else if (url.endsWith(ApiConfig.DELETE_POINT_ORDER))
		{
			showServerMsg(result, "删除订单成功！");
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		//		onRefresh();
		finish();
	}
}
