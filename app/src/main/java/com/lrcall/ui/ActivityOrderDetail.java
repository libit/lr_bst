/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ExpressInfo;
import com.lrcall.appbst.models.OrderInfo;
import com.lrcall.appbst.models.OrderProductInfo;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.UserAddressInfo;
import com.lrcall.appbst.services.AddressService;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.OrderService;
import com.lrcall.appbst.services.ProductService;
import com.lrcall.db.DbProductInfoFactory;
import com.lrcall.enums.NeedExpress;
import com.lrcall.ui.adapter.OrderProductsAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.customer.ViewHeightCalTools;
import com.lrcall.ui.dialog.DialogInputComment;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.List;

public class ActivityOrderDetail extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityOrderDetail.class.getSimpleName();
	private TextView tvName, tvNumber, tvAddress, tvExpress, tvExpressId, tvRemark, tvProductsPrice, tvExpressPrice, tvTotalPrice;
	private ListView lvProducts;
	private AddressService mAddressService;
	private ProductService mProductService;
	private int expressPrice = 0, productsPrice = 0, totalPrice = 0;
	private OrderService mOrderService;
	private String mOrderId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			mOrderId = bundle.getString(ConstValues.DATA_ORDER_ID);
		}
		if (StringTools.isNull(mOrderId))
		{
			finish();
			Toast.makeText(this, "订单不能为空！", Toast.LENGTH_LONG).show();
		}
		mAddressService = new AddressService(this);
		mAddressService.addDataResponse(this);
		mProductService = new ProductService(this);
		mProductService.addDataResponse(this);
		mOrderService = new OrderService(this);
		mOrderService.addDataResponse(this);
		viewInit();
		mOrderService.getOrderInfo(mOrderId, "请稍后...", false);
		mOrderService.getOrderExpressList(mOrderId, null, false);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		tvName = (TextView) findViewById(R.id.tv_name);
		tvNumber = (TextView) findViewById(R.id.tv_number);
		tvAddress = (TextView) findViewById(R.id.tv_address);
		tvExpress = (TextView) findViewById(R.id.tv_express);
		tvExpressId = (TextView) findViewById(R.id.tv_express_id);
		tvRemark = (TextView) findViewById(R.id.tv_remark);
		tvProductsPrice = (TextView) findViewById(R.id.tv_products_price);
		tvExpressPrice = (TextView) findViewById(R.id.tv_express_price);
		tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
		lvProducts = (ListView) findViewById(R.id.list_products);
		findViewById(R.id.layout_remark).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.layout_remark:
			{
				DialogInputComment dialogInputComment = new DialogInputComment(this, new DialogInputComment.OnInputListenser()
				{
					@Override
					public void onOkClick(String content)
					{
						if (content.length() > 10)
						{
							content = content.substring(0, 10) + "...";
						}
						tvRemark.setText(content);
					}

					@Override
					public void onCancelClick()
					{
					}
				});
				dialogInputComment.show();
				dialogInputComment.setDefaultContent(tvRemark.getText().toString());
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_ORDER_INFO))
		{
			OrderInfo orderInfo = GsonTools.getReturnObject(result, OrderInfo.class);
			if (orderInfo != null)
			{
				boolean isNeedExpress = false;
				if (orderInfo.getOrderProductInfoList() != null && orderInfo.getOrderProductInfoList().size() > 0)
				{
					for (OrderProductInfo orderProductInfo : orderInfo.getOrderProductInfoList())
					{
						ProductInfo productInfo = DbProductInfoFactory.getInstance().getProductInfo(orderProductInfo.getProductId());
						productsPrice += productInfo.getPrice() * orderProductInfo.getCount();
						expressPrice += productInfo.getExpressPrice() * orderProductInfo.getCount();
						if (productInfo.getNeedExpress() == NeedExpress.NEED.getStatus())
						{
							isNeedExpress = true;
						}
					}
				}
				if (!isNeedExpress)
				{
					findViewById(R.id.layout_address).setVisibility(View.GONE);
					findViewById(R.id.layout_select_express).setVisibility(View.GONE);
					findViewById(R.id.layout_express_id).setVisibility(View.GONE);
				}
				tvProductsPrice.setText("￥" + StringTools.getPrice(productsPrice));
				tvExpressPrice.setText(String.format("￥%s", StringTools.getPrice(expressPrice)));
				totalPrice = expressPrice + productsPrice;
				tvTotalPrice.setText(String.format("￥%s", StringTools.getPrice(totalPrice)));
				tvRemark.setText(orderInfo.getComment());
				OrderProductsAdapter orderProductsAdapter = new OrderProductsAdapter(this, orderInfo.getOrderProductInfoList(), new OrderProductsAdapter.IItemClick()
				{
					@Override
					public void onProductClicked(ProductInfo productInfo)
					{
					}
				});
				lvProducts.setAdapter(orderProductsAdapter);
				ViewHeightCalTools.setListViewHeight(lvProducts, true);
			}
			else
			{
				ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
				String msg = "";
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, msg);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_ADDRESS_INFO))
		{
			UserAddressInfo userAddressInfo = GsonTools.getReturnObject(result, UserAddressInfo.class);
			if (userAddressInfo != null)
			{
				tvName.setText(userAddressInfo.getName());
				tvNumber.setText(userAddressInfo.getNumber());
				tvAddress.setText(userAddressInfo.getProvince() + " " + userAddressInfo.getCity() + " " + userAddressInfo.getDistrict() + " " + userAddressInfo.getAddress());
			}
			else
			{
				tvName.setText("");
				tvNumber.setText("");
				tvAddress.setText("");
			}
		}
		else if (url.endsWith(ApiConfig.GET_ORDER_EXPRESS_LIST))
		{
			List<ExpressInfo> expressInfoList = GsonTools.getReturnListObjects(result, new TypeToken<List<ExpressInfo>>()
			{
			}.getType());
			if (expressInfoList != null && expressInfoList.size() > 0)
			{
				ExpressInfo expressInfo = expressInfoList.get(0);
				tvExpress.setText(expressInfo.getExpressName());
				tvExpressId.setText(expressInfo.getExpressId());
				mAddressService.getUserAddressInfo(expressInfo.getAddressId(), null, false);
			}
			else
			{
				tvName.setText("");
				tvNumber.setText("");
				tvAddress.setText("");
			}
		}
		return false;
	}
}
