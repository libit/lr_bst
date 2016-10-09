/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.OrderInfo;
import com.lrcall.appbst.models.PayTypeInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.OrderService;
import com.lrcall.enums.OrderStatus;
import com.lrcall.enums.PayType;
import com.lrcall.ui.adapter.OrderAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.dialog.DialogCommon;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;

import java.util.ArrayList;
import java.util.List;

public class FragmentOrderList extends MyBasePageFragment implements IAjaxDataResponse
{
	private static final String TAG = FragmentOrderList.class.getSimpleName();
	private static final String ARG_ORDER_TYPE = "orderType";
	private View layoutNoOrder;
	private OrderService mOrderService;
	private final List<OrderInfo> mOrderInfoList = new ArrayList<>();
	private byte orderType;
	private OrderAdapter mOrderAdapter;

	public FragmentOrderList()
	{
	}

	public static FragmentOrderList newInstance(byte orderType)
	{
		FragmentOrderList fragment = new FragmentOrderList();
		Bundle args = new Bundle();
		args.putByte(ARG_ORDER_TYPE, orderType);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
		{
			orderType = getArguments().getByte(ARG_ORDER_TYPE);
		}
		mOrderService = new OrderService(this.getContext());
		mOrderService.addDataResponse(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_order_list, container, false);
		viewInit(rootView);
		refreshData();
		return rootView;
	}

	@Override
	protected void viewInit(View rootView)
	{
		xListView = (XListView) rootView.findViewById(R.id.xlist);
		layoutNoOrder = rootView.findViewById(R.id.layout_no_order);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setAdapter(null);
		xListView.setXListViewListener(this);
		super.viewInit(rootView);
	}

	@Override
	public void fragmentShow()
	{
		super.fragmentShow();
		refreshData();
	}

	/**
	 * 刷新数据
	 */
	@Override
	public void refreshData()
	{
		mOrderAdapter = null;
		mOrderInfoList.clear();
		loadMoreData();
	}

	/**
	 * 加载更多数据
	 */
	@Override
	public void loadMoreData()
	{
		mOrderService.getOrderInfoList(orderType, mDataStart, getPageSize(), null, false);
	}

	//设置List
	synchronized private void setOrderInfoList(List<OrderInfo> orderInfoList)
	{
		if (orderInfoList == null || orderInfoList.size() < 1)
		{
			if (mOrderInfoList.size() < 1)
			{
				layoutNoOrder.setVisibility(View.VISIBLE);
				xListView.setVisibility(View.GONE);
			}
			return;
		}
		if (orderInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		layoutNoOrder.setVisibility(View.GONE);
		xListView.setVisibility(View.VISIBLE);
		mOrderInfoList.clear();
		for (OrderInfo orderInfo : orderInfoList)
		{
			mOrderInfoList.add(orderInfo);
		}
		if (mOrderAdapter == null)
		{
			mOrderAdapter = new OrderAdapter(getContext(), mOrderInfoList, new OrderAdapter.IOrderAdapter()
			{
				@Override
				public void onOrderClicked(OrderInfo orderInfo)
				{
					Intent intent = new Intent(FragmentOrderList.this.getContext(), ActivityOrderDetail.class);
					intent.putExtra(ConstValues.DATA_ORDER_ID, orderInfo.getOrderId());
					startActivity(intent);
				}

				@Override
				public void onOrderPayClicked(OrderInfo orderInfo)
				{
					Intent intent = new Intent(FragmentOrderList.this.getContext(), ActivityPayList.class);
					//					intent.putExtra(ConstValues.DATA_ORDER_ID, orderInfo.getOrderId());
					intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, GsonTools.toJson(new PayTypeInfo(PayType.PAY_ORDER, orderInfo.getTotalPrice(), "订单" + orderInfo.getOrderId() + "支付", orderInfo.getOrderId())));
					startActivity(intent);
				}

				@Override
				public void onOrderCancelClicked(final OrderInfo orderInfo)
				{
					new DialogCommon(FragmentOrderList.this.getContext(), new DialogCommon.LibitDialogListener()
					{
						@Override
						public void onOkClick()
						{
							if (orderInfo.getStatus() == OrderStatus.WAIT_PAY.getStatus())
							{
								mOrderService.deleteOrder(orderInfo.getOrderId(), "正在取消订单，请稍后...", false);
							}
							else
							{
								ToastView.showCenterToast(FragmentOrderList.this.getContext(), R.drawable.ic_do_fail, "订单已生效，不能取消！");
							}
						}

						@Override
						public void onCancelClick()
						{
						}
					}, getString(R.string.title_warning), "确定要取消订单吗？", true, false, true).show();
				}

				@Override
				public void onOrderConfirmClicked(final OrderInfo orderInfo)
				{
					new DialogCommon(FragmentOrderList.this.getContext(), new DialogCommon.LibitDialogListener()
					{
						@Override
						public void onOkClick()
						{
							mOrderService.orderFinish(orderInfo.getOrderId(), "请稍后...", false);
						}

						@Override
						public void onCancelClick()
						{
						}
					}, getString(R.string.title_warning), "确定已收到货吗？", true, false, true).show();
				}

				@Override
				public void onOrderCommentClicked(OrderInfo orderInfo)
				{
					Intent intent = new Intent(FragmentOrderList.this.getContext(), ActivityOrderComment.class);
					intent.putExtra(ConstValues.DATA_ORDER_ID, orderInfo.getOrderId());
					startActivity(intent);
				}
			});
			xListView.setAdapter(mOrderAdapter);
		}
		else
		{
			mOrderAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		LogcatTools.debug(TAG, "onAjaxDataResponse服务器返回数据了");
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_ORDER_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<OrderInfo> orderInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<OrderInfo>>()
				{
				}.getType());
				setOrderInfoList(orderInfoList);
			}
			else
			{
				setOrderInfoList(null);
			}
		}
		else if (url.endsWith(ApiConfig.DELETE_ORDER))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				ToastView.showCenterToast(this.getContext(), R.drawable.ic_done, "取消订单成功！");
				for (OrderInfo orderInfo : mOrderInfoList)
				{
					if (orderInfo.getOrderId().equals(returnInfo.getErrmsg()))
					{
						mOrderInfoList.remove(orderInfo);
						mOrderAdapter.notifyDataSetChanged();
						break;
					}
				}
			}
			else
			{
				String msg = result;
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(this.getContext(), R.drawable.ic_do_fail, "取消订单失败：" + msg);
			}
		}
		else if (url.endsWith(ApiConfig.ORDER_FINISH))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				ToastView.showCenterToast(this.getContext(), R.drawable.ic_done, "确认收货成功！");
				for (OrderInfo orderInfo : mOrderInfoList)
				{
					if (orderInfo.getOrderId().equals(returnInfo.getErrmsg()))
					{
						mOrderInfoList.remove(orderInfo);
						mOrderAdapter.notifyDataSetChanged();
						break;
					}
				}
			}
			else
			{
				String msg = result;
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(this.getContext(), R.drawable.ic_do_fail, "确认收货失败：" + msg);
			}
		}
		return false;
	}
}
