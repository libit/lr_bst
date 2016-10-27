/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ExpressInfo;
import com.lrcall.appbst.models.OrderProductInfo;
import com.lrcall.appbst.models.OrderSubInfo;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.UserAddressInfo;
import com.lrcall.appbst.services.AddressService;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ProductService;
import com.lrcall.appbst.services.ShopOrderService;
import com.lrcall.db.DbProductInfoFactory;
import com.lrcall.enums.NeedExpress;
import com.lrcall.enums.OrderStatus;
import com.lrcall.ui.adapter.OrderProductsAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.customer.ViewHeightCalTools;
import com.lrcall.ui.dialog.DialogInputComment;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

public class ActivityShopOrderInfo extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityShopOrderInfo.class.getSimpleName();
	private TextView tvName, tvNumber, tvAddress, tvExpress, tvRemark, tvProductsPrice, tvExpressPrice, tvTotalPrice;
	private ListView lvProducts;
	private Button btnSend;
	private AddressService mAddressService;
	private ProductService mProductService;
	private ShopOrderService mShopOrderService;
	private int expressPrice = 0, productsPrice = 0, totalPrice = 0;
	private String mOrderSubId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_order_info);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			mOrderSubId = bundle.getString(ConstValues.DATA_ORDER_ID);
		}
		if (StringTools.isNull(mOrderSubId))
		{
			finish();
			Toast.makeText(this, "订单号不能为空！", Toast.LENGTH_LONG).show();
		}
		mAddressService = new AddressService(this);
		mAddressService.addDataResponse(this);
		mProductService = new ProductService(this);
		mProductService.addDataResponse(this);
		mShopOrderService = new ShopOrderService(this);
		mShopOrderService.addDataResponse(this);
		viewInit();
		refreshData();
	}

	private void refreshData()
	{
		mShopOrderService.getOrderInfo(mOrderSubId, "请稍后...", false);
		mShopOrderService.getOrderExpressInfo(mOrderSubId, null, false);
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
		tvRemark = (TextView) findViewById(R.id.tv_remark);
		tvProductsPrice = (TextView) findViewById(R.id.tv_products_price);
		tvExpressPrice = (TextView) findViewById(R.id.tv_express_price);
		tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
		lvProducts = (ListView) findViewById(R.id.list_products);
		findViewById(R.id.layout_remark).setOnClickListener(this);
		btnSend = (Button) findViewById(R.id.btn_send_express);
		btnSend.setOnClickListener(this);
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
			case R.id.btn_send_express:
			{
				Intent intent = new Intent(this, ActivityOrderShip.class);
				intent.putExtra(ConstValues.DATA_ORDER_ID, mOrderSubId);
				startActivity(intent);
				finish();
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_SHOP_ORDER_INFO))
		{
			OrderSubInfo orderSubInfo = GsonTools.getReturnObject(result, OrderSubInfo.class);
			if (orderSubInfo != null)
			{
				boolean isNeedExpress = false;
				if (orderSubInfo.getOrderProductInfoList() != null && orderSubInfo.getOrderProductInfoList().size() > 0)
				{
					for (OrderProductInfo orderProductInfo : orderSubInfo.getOrderProductInfoList())
					{
						ProductInfo productInfo = DbProductInfoFactory.getInstance().getProductInfo(orderProductInfo.getProductInfo().getProductId());
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
					findViewById(R.id.layout_select_express_line).setVisibility(View.GONE);
				}
				tvProductsPrice.setText("￥" + StringTools.getPrice(productsPrice));
				tvExpressPrice.setText(String.format("￥%s", StringTools.getPrice(expressPrice)));
				totalPrice = expressPrice + productsPrice;
				tvTotalPrice.setText(String.format("￥%s", StringTools.getPrice(totalPrice)));
				tvRemark.setText(orderSubInfo.getComment());
				OrderProductsAdapter orderProductsAdapter = new OrderProductsAdapter(this, orderSubInfo.getOrderProductInfoList(), new OrderProductsAdapter.IOrderProductsAdapter()
				{
					@Override
					public void onProductClicked(ProductInfo productInfo)
					{
					}
				});
				lvProducts.setAdapter(orderProductsAdapter);
				if (orderSubInfo.getStatus() == OrderStatus.DELETED.getStatus())
				{
					btnSend.setEnabled(false);
					btnSend.setText("已关闭");
				}
				else if (orderSubInfo.getStatus() == OrderStatus.WAIT_PAY.getStatus())
				{
					btnSend.setEnabled(false);
					btnSend.setText("等待买家付款");
				}
				else if (orderSubInfo.getStatus() == OrderStatus.EXPRESS.getStatus())
				{
					btnSend.setEnabled(false);
					btnSend.setText("已发货");
				}
				else if (orderSubInfo.getStatus() == OrderStatus.FINISH.getStatus())
				{
					btnSend.setEnabled(false);
					btnSend.setText("交易已完成");
				}
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
				tvAddress.setText(userAddressInfo.getAddress());
			}
			else
			{
				tvName.setText("");
				tvNumber.setText("");
				tvAddress.setText("");
			}
		}
		else if (url.endsWith(ApiConfig.GET_SHOP_ORDER_EXPRESS_INFO))
		{
			ExpressInfo expressInfo = GsonTools.getReturnObject(result, ExpressInfo.class);
			if (expressInfo != null)
			{
				tvExpress.setText(expressInfo.getExpressName());
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
