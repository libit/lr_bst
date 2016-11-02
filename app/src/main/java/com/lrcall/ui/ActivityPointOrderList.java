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
import com.lrcall.appbst.models.PayTypeInfo;
import com.lrcall.appbst.models.PointOrderInfo;
import com.lrcall.appbst.models.ReturnInfo;
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
		refreshData();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
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
		mPointOrderService.getOrderInfoList(null, mDataStart, getPageSize(), "正在加载，请稍后...", false);
	}

	//刷新数据
	synchronized private void refreshPointOrders(List<PointOrderInfo> pointOrderInfoList)
	{
		if (pointOrderInfoList == null || pointOrderInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			return;
		}
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
			mPointOrderAdapter = new PointOrderAdapter(this, mPointOrderInfoList, new PointOrderAdapter.IPointOrderAdapter()
			{
				@Override
				public void onOrderClicked(PointOrderInfo pointOrderInfo)
				{
				}

				@Override
				public void onOrderPayClicked(PointOrderInfo pointOrderInfo)
				{
					Intent intent = new Intent(ActivityPointOrderList.this, ActivityPayList.class);
					intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, GsonTools.toJson(new PayTypeInfo(PayType.PAY_POINT_ORDER, pointOrderInfo.getTotalPrice(), "积分订单" + pointOrderInfo.getOrderId() + "支付", pointOrderInfo.getOrderId())));
					startActivityForResult(intent, REQ_PAY);
				}

				@Override
				public void onOrderCancelClicked(final PointOrderInfo pointOrderInfo)
				{
					new DialogCommon(ActivityPointOrderList.this, new DialogCommon.LibitDialogListener()
					{
						@Override
						public void onOkClick()
						{
							if (pointOrderInfo.getStatus() == OrderStatus.WAIT_PAY.getStatus())
							{
								mPointOrderService.deleteOrder(pointOrderInfo.getOrderId(), "正在取消订单，请稍后...", false);
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
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<PointOrderInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<PointOrderInfo>>()
				{
				}.getType());
				refreshPointOrders(list);
			}
			else
			{
				refreshPointOrders(null);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.DELETE_POINT_ORDER))
		{
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				ToastView.showCenterToast(this, R.drawable.ic_done, "删除订单成功！");
			}
			else
			{
				showServerMsg(result);
			}
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		//		refreshData();
		finish();
	}
}
