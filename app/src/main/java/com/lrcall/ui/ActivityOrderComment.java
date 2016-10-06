/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.OrderInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.OrderProductCommentService;
import com.lrcall.appbst.services.OrderService;
import com.lrcall.ui.adapter.OrderCommentAdapter;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

public class ActivityOrderComment extends MyBaseActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityOrderComment.class.getSimpleName();
	private String mOrderId;
	private OrderService mOrderService;
	private ListView lvProductComment;
	private OrderCommentAdapter mOrderCommentAdapter;
	private OrderProductCommentService mOrderProductCommentService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_comment);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			mOrderId = bundle.getString(ConstValues.DATA_ORDER_ID);
		}
		if (StringTools.isNull(mOrderId))
		{
			finish();
			Toast.makeText(this, "没有选择订单！", Toast.LENGTH_LONG).show();
		}
		mOrderService = new OrderService(this);
		mOrderService.addDataResponse(this);
		mOrderProductCommentService = new OrderProductCommentService(this);
		mOrderProductCommentService.addDataResponse(this);
		viewInit();
		initData();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		lvProductComment = (ListView) findViewById(R.id.list_products_comment);
	}

	//初始化数据
	private void initData()
	{
		mOrderService.getOrderInfo(mOrderId, "请稍后...", true);
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_ORDER_INFO))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				final OrderInfo orderInfo = GsonTools.getReturnObject(result, OrderInfo.class);
				if (orderInfo != null)
				{
					mOrderCommentAdapter = new OrderCommentAdapter(this, orderInfo.getOrderProductInfoList(), new OrderCommentAdapter.IOrderCommentAdapter()
					{
						@Override
						public void onSubmit(String productId, int rate, String content)
						{
							if (StringTools.isNull(content))
							{
								content = "用户未发言，系统默认好评！";
							}
							mOrderProductCommentService.addProductCommentInfo(orderInfo.getOrderId(), productId, content, (byte) rate, rate, "请稍后...", true);
						}
					});
					lvProductComment.setAdapter(mOrderCommentAdapter);
				}
			}
			else
			{
				String msg = result;
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				Toast.makeText(this, "查询订单失败：" + msg, Toast.LENGTH_LONG).show();
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.ADD_PRODUCT_COMMENT))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				Toast.makeText(this, "评价成功！", Toast.LENGTH_LONG).show();
				finish();
			}
			else
			{
				String msg = result;
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				Toast.makeText(this, "评价失败：" + msg, Toast.LENGTH_LONG).show();
			}
			return true;
		}
		return false;
	}
}
