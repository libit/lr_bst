/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ExpressInfo;
import com.lrcall.appbst.models.OrderProductInfo;
import com.lrcall.appbst.models.PayTypeInfo;
import com.lrcall.appbst.models.PointProductInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.UserAddressInfo;
import com.lrcall.appbst.models.UserBalanceInfo;
import com.lrcall.appbst.services.AddressService;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PointOrderService;
import com.lrcall.appbst.services.PointProductService;
import com.lrcall.appbst.services.UserService;
import com.lrcall.db.DbUserAddressInfoFactory;
import com.lrcall.enums.NeedExpress;
import com.lrcall.enums.PayType;
import com.lrcall.models.FuncInfo;
import com.lrcall.ui.adapter.BaseFuncsAdapter;
import com.lrcall.ui.adapter.FuncsHorizontalAdapter;
import com.lrcall.ui.adapter.PointOrderProductsAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.customer.ViewHeightCalTools;
import com.lrcall.ui.dialog.DialogInputComment;
import com.lrcall.ui.dialog.DialogList;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityPointOrderAdd extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityPointOrderAdd.class.getSimpleName();
	private static final int EXPRESS_EMS = 1;
	private static final int EXPRESS_SF = 2;
	private static final int REQ_SELECT_ADDRESS = 100;
	private TextView tvName, tvNumber, tvAddress, tvExpress, tvRemark, tvProductsPoint, tvExpressPrice, tvTotalPrice, tvPoint;
	private ListView lvProducts;
	private View layoutAddAddress, layoutShowAddress;
	private int expressPrice = 0, productsPoint = 0, totalPrice = 0;
	private String addressId = "";
	private String comment = "";
	private int expressType = 0;
	private ArrayList<OrderProductInfo> mOrderProductInfoArrayList = new ArrayList<>();
	private final List<FuncInfo> funcExpressInfoList = new ArrayList<>();
	private boolean isNeedExpress = false;
	private AddressService mAddressService;
	private PointProductService mPointProductService;
	private PointOrderService mPointOrderService;
	private UserService mUserService;
	private int mMaxPoint = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_point_order_add);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			String data = bundle.getString(ConstValues.DATA_ORDER_PRODUCT_LIST);
			if (!StringTools.isNull(data))
			{
				mOrderProductInfoArrayList = GsonTools.getObjects(data, new TypeToken<ArrayList<OrderProductInfo>>()
				{
				}.getType());
			}
		}
		if (mOrderProductInfoArrayList == null || mOrderProductInfoArrayList.size() < 1)
		{
			finish();
			Toast.makeText(this, "没有选择商品！", Toast.LENGTH_LONG).show();
		}
		mAddressService = new AddressService(this);
		mAddressService.addDataResponse(this);
		mPointProductService = new PointProductService(this);
		mPointProductService.addDataResponse(this);
		mPointOrderService = new PointOrderService(this);
		mPointOrderService.addDataResponse(this);
		mUserService = new UserService(this);
		mUserService.addDataResponse(this);
		funcExpressInfoList.add(new FuncInfo(EXPRESS_EMS, R.drawable.camera, "EMS"));
		funcExpressInfoList.add(new FuncInfo(EXPRESS_SF, R.drawable.camera, "顺丰"));
		viewInit();
		initData();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		layoutAddAddress = findViewById(R.id.layout_add_address);
		layoutShowAddress = findViewById(R.id.layout_show_address);
		tvName = (TextView) findViewById(R.id.tv_name);
		tvNumber = (TextView) findViewById(R.id.tv_number);
		tvAddress = (TextView) findViewById(R.id.tv_address);
		tvExpress = (TextView) findViewById(R.id.tv_express);
		tvRemark = (TextView) findViewById(R.id.tv_remark);
		tvPoint = (TextView) findViewById(R.id.tv_available_point);
		tvProductsPoint = (TextView) findViewById(R.id.tv_point);
		tvExpressPrice = (TextView) findViewById(R.id.tv_express_price);
		tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
		lvProducts = (ListView) findViewById(R.id.list_products);
		findViewById(R.id.layout_address).setOnClickListener(this);
		findViewById(R.id.layout_select_express).setOnClickListener(this);
		findViewById(R.id.layout_remark).setOnClickListener(this);
		findViewById(R.id.btn_submit).setOnClickListener(this);
	}

	int index = 0;

	//初始化数据
	private void initData()
	{
		final int size = mOrderProductInfoArrayList.size();
		index = 0;
		for (int i = 0; i < size; i++)
		{
			final OrderProductInfo orderProductInfo = mOrderProductInfoArrayList.get(i);
			LogcatTools.debug(TAG, "数量：" + orderProductInfo.getCount());
			PointProductService pointProductService = new PointProductService(this);
			pointProductService.addDataResponse(new IAjaxDataResponse()
			{
				@Override
				public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
				{
					index++;
					PointProductInfo pointProductInfo = GsonTools.getReturnObject(result, PointProductInfo.class);
					if (pointProductInfo != null)
					{
						productsPoint += pointProductInfo.getPoint() * orderProductInfo.getCount();
						expressPrice += pointProductInfo.getExpressPrice() * orderProductInfo.getCount();
						if (pointProductInfo.getNeedExpress() == NeedExpress.NEED.getStatus())
						{
							isNeedExpress = true;
						}
					}
					if (index >= size)//如果是最后一个了
					{
						if (!isNeedExpress)
						{
							expressPrice = 0;
							findViewById(R.id.layout_address).setVisibility(View.GONE);
							findViewById(R.id.layout_select_express).setVisibility(View.GONE);
							findViewById(R.id.layout_select_express_line).setVisibility(View.GONE);
							findViewById(R.id.layout_express_price).setVisibility(View.GONE);
							findViewById(R.id.layout_express_price_line).setVisibility(View.GONE);
						}
						tvProductsPoint.setText("" + productsPoint);
						tvExpressPrice.setText(String.format("￥%s", StringTools.getPrice(expressPrice)));
						totalPrice = expressPrice;
						tvTotalPrice.setText(String.format("￥%s", StringTools.getPrice(totalPrice)));
						PointOrderProductsAdapter orderProductsAdapter = new PointOrderProductsAdapter(ActivityPointOrderAdd.this, mOrderProductInfoArrayList, new PointOrderProductsAdapter.IItemClick()
						{
							@Override
							public void onProductClicked(PointProductInfo pointProductInfo)
							{
							}
						});
						lvProducts.setAdapter(orderProductsAdapter);
						ViewHeightCalTools.setListViewHeight(lvProducts, true);
						mUserService.getUserBalanceInfo("请稍后...", false);
					}
					return false;
				}
			});
			pointProductService.getPointProduct(orderProductInfo.getProductId(), null, true);
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.layout_address:
			{
				Intent intent = new Intent(this, ActivityAddressList.class);
				startActivityForResult(intent, REQ_SELECT_ADDRESS);
				break;
			}
			case R.id.layout_select_express:
			{
				final DialogList dialogList = new DialogList(this);
				FuncsHorizontalAdapter funcsHorizontalAdapter = new FuncsHorizontalAdapter(this, funcExpressInfoList, new BaseFuncsAdapter.IFuncsAdapterItemClicked()
				{
					@Override
					public void onFuncClicked(FuncInfo funcInfo)
					{
						if (dialogList.isShowing())
						{
							dialogList.dismiss();
						}
						expressType = funcInfo.getId();
						tvExpress.setText(funcInfo.getLabel());
						tvExpress.setTag(funcInfo.getId());
						//expressPrice = 0;
						tvExpressPrice.setText(String.format("￥%s", StringTools.getPrice(expressPrice)));
						totalPrice = expressPrice;
						tvTotalPrice.setText(String.format("￥%s", StringTools.getPrice(totalPrice)));
					}
				});
				dialogList.setAdapter(funcsHorizontalAdapter);
				dialogList.show();
				break;
			}
			case R.id.layout_remark:
			{
				DialogInputComment dialogInputComment = new DialogInputComment(this, new DialogInputComment.OnInputListenser()
				{
					@Override
					public void onOkClick(String content)
					{
						comment = content;
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
			case R.id.btn_submit:
			{
				String expressName = "";
				String expressJson = "";
				String point = tvPoint.getText().toString();
				int pointAmount = 0;
				try
				{
					pointAmount = Integer.parseInt(point);
				}
				catch (Exception e)
				{
				}
				if (isNeedExpress)
				{
					if (StringTools.isNull(addressId))
					{
						ToastView.showCenterToast(this, R.drawable.ic_do_fail, "请选择或添加收货人地址！");
						return;
					}
					if (expressType == 0)
					{
						ToastView.showCenterToast(this, R.drawable.ic_do_fail, "请选择快递方式！");
						return;
					}
					for (FuncInfo funcInfo : funcExpressInfoList)
					{
						if (funcInfo.getId() == expressType)
						{
							expressName = funcInfo.getLabel();
						}
					}
					expressJson = GsonTools.toJson(new ExpressInfo("", "", addressId, expressName, "", expressPrice, (byte) 0, 0l, 0l));
				}
				String productsJson = GsonTools.toJson(mOrderProductInfoArrayList);
				mPointOrderService.addOrder(comment, "测试", productsJson, expressJson, pointAmount, null, false);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.ADD_POINT_ORDER))
		{
			showServerMsg(result, "下单成功！");
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				finish();
				if (isNeedExpress && expressPrice > 0)
				{
					//这里转到支付界面
					Intent intent = new Intent(this, ActivityPayList.class);
					intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, GsonTools.toJson(new PayTypeInfo(PayType.PAY_POINT_ORDER, totalPrice, "订单" + returnInfo.getErrmsg() + "支付", returnInfo.getErrmsg())));
					startActivity(intent);
				}
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_USER_BALANCE_INFO))
		{
			UserBalanceInfo userBalanceInfo = GsonTools.getReturnObject(result, UserBalanceInfo.class);
			if (userBalanceInfo != null)
			{
				mMaxPoint = userBalanceInfo.getPoint();
				tvPoint.setText(mMaxPoint + "积分可用");
			}
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_SELECT_ADDRESS)
		{
			if (resultCode == RESULT_OK)
			{
				addressId = data.getStringExtra(ConstValues.DATA_ADDRESS_ID);
				if (!StringTools.isNull(addressId))
				{
					UserAddressInfo userAddressInfo = DbUserAddressInfoFactory.getInstance().getUserAddressInfo(addressId);
					if (userAddressInfo != null)
					{
						layoutAddAddress.setVisibility(View.GONE);
						layoutShowAddress.setVisibility(View.VISIBLE);
						tvName.setText(userAddressInfo.getName());
						tvNumber.setText(userAddressInfo.getNumber());
						tvAddress.setText(userAddressInfo.getCountry() + " " + userAddressInfo.getProvince() + " " + userAddressInfo.getCity() + "" + userAddressInfo.getDistrict() + " " + userAddressInfo.getAddress());
						return;
					}
					else
					{
						addressId = "";
					}
				}
			}
		}
	}
}
