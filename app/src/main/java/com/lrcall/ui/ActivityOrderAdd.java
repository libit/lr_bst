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
import com.lrcall.appbst.models.PayInfo;
import com.lrcall.appbst.models.PayTypeInfo;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.ProductPricePointInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.UserAddressInfo;
import com.lrcall.appbst.models.UserBalanceInfo;
import com.lrcall.appbst.services.AddressService;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.OrderService;
import com.lrcall.appbst.services.PayService;
import com.lrcall.appbst.services.ProductPricePointService;
import com.lrcall.appbst.services.ProductService;
import com.lrcall.appbst.services.UserService;
import com.lrcall.db.DbProductInfoFactory;
import com.lrcall.db.DbUserAddressInfoFactory;
import com.lrcall.enums.NeedExpress;
import com.lrcall.enums.PayType;
import com.lrcall.models.FuncInfo;
import com.lrcall.ui.adapter.BaseFuncsAdapter;
import com.lrcall.ui.adapter.FuncsHorizontalAdapter;
import com.lrcall.ui.adapter.OrderProductsAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.customer.ViewHeightCalTools;
import com.lrcall.ui.dialog.DialogInputComment;
import com.lrcall.ui.dialog.DialogInputPoint;
import com.lrcall.ui.dialog.DialogList;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityOrderAdd extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityOrderAdd.class.getSimpleName();
	private static final int EXPRESS_EMS = 1;
	private static final int EXPRESS_SF = 2;
	private static final int REQ_SELECT_ADDRESS = 100;
	private TextView tvName, tvNumber, tvAddress, tvPayName, tvExpress, tvRemark, tvProductsPrice, tvExpressPrice, tvTotalPrice, tvAvailablePoint, tvPoint;
	private ListView lvProducts;
	private View layoutAddAddress, layoutShowAddress;
	private int expressPrice = 0, productsPrice = 0, totalPrice = 0;
	private String addressId = "";
	private String comment = "";
	private int expressType = 0;
	private ArrayList<OrderProductInfo> mOrderProductInfoArrayList = new ArrayList<>();
	private final List<FuncInfo> funcExpressInfoList = new ArrayList<>();
	private boolean isNeedExpress = false;
	private AddressService mAddressService;
	private ProductService mProductService;
	private PayService mPayService;
	private OrderService mOrderService;
	private UserService mUserService;
	private ProductPricePointService mProductPricePointService;
	private int mMaxPoint = 0;
	private int mNeedPoint = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_add);
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
		mProductService = new ProductService(this);
		mProductService.addDataResponse(this);
		mOrderService = new OrderService(this);
		mOrderService.addDataResponse(this);
		mUserService = new UserService(this);
		mUserService.addDataResponse(this);
		mProductPricePointService = new ProductPricePointService(this);
		mProductPricePointService.addDataResponse(this);
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
		tvPayName = (TextView) findViewById(R.id.tv_pay_name);
		tvExpress = (TextView) findViewById(R.id.tv_express);
		tvRemark = (TextView) findViewById(R.id.tv_remark);
		tvAvailablePoint = (TextView) findViewById(R.id.tv_available_point);
		tvPoint = (TextView) findViewById(R.id.tv_point);
		tvProductsPrice = (TextView) findViewById(R.id.tv_products_price);
		tvExpressPrice = (TextView) findViewById(R.id.tv_express_price);
		tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
		lvProducts = (ListView) findViewById(R.id.list_products);
		findViewById(R.id.layout_address).setOnClickListener(this);
		findViewById(R.id.layout_select_pay).setOnClickListener(this);
		findViewById(R.id.layout_select_express).setOnClickListener(this);
		findViewById(R.id.layout_remark).setOnClickListener(this);
		findViewById(R.id.layout_point).setOnClickListener(this);
		findViewById(R.id.btn_submit).setOnClickListener(this);
	}

	//初始化数据
	private void initData()
	{
		String productIds = "";
		for (OrderProductInfo orderProductInfo : mOrderProductInfoArrayList)
		{
			//			LogcatTools.debug(TAG, "数量：" + orderProductInfo.getCount());
			ProductInfo productInfo = DbProductInfoFactory.getInstance().getProductInfo(orderProductInfo.getProductId());
			productIds += "," + orderProductInfo.getProductId();
			productsPrice += productInfo.getPrice() * orderProductInfo.getCount();
			expressPrice += productInfo.getExpressPrice() * orderProductInfo.getCount();
			if (productInfo.getNeedExpress() == NeedExpress.NEED.getStatus())
			{
				isNeedExpress = true;
			}
			if (!isNeedExpress)
			{
				expressPrice = 0;
				findViewById(R.id.layout_address).setVisibility(View.GONE);
				findViewById(R.id.layout_select_express).setVisibility(View.GONE);
				findViewById(R.id.layout_select_express_line).setVisibility(View.GONE);
				findViewById(R.id.layout_express_price).setVisibility(View.GONE);
				findViewById(R.id.layout_express_price_line).setVisibility(View.GONE);
			}
			tvProductsPrice.setText("￥" + StringTools.getPrice(productsPrice));
			tvExpressPrice.setText(String.format("￥%s", StringTools.getPrice(expressPrice)));
			totalPrice = expressPrice + productsPrice;
			tvTotalPrice.setText(String.format("￥%s", StringTools.getPrice(totalPrice)));
			OrderProductsAdapter orderProductsAdapter = new OrderProductsAdapter(this, mOrderProductInfoArrayList, new OrderProductsAdapter.IItemClick()
			{
				@Override
				public void onProductClicked(ProductInfo productInfo)
				{
				}
			});
			lvProducts.setAdapter(orderProductsAdapter);
			ViewHeightCalTools.setListViewHeight(lvProducts, true);
			mUserService.getUserBalanceInfo("请稍后...", false);
			if (productIds.length() > 0)
			{
				productIds = productIds.substring(1);
			}
			mProductPricePointService.getProductPricePointInfoList(productIds, null, false);
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
			case R.id.layout_point:
			{
				final DialogInputPoint dialogInputPoint = new DialogInputPoint(this, new DialogInputPoint.OnInputListenser()
				{
					@Override
					public void onOkClick(String content)
					{
						int point = 0;
						try
						{
							point = Integer.parseInt(content);
							if (point <= mNeedPoint && point <= mMaxPoint)
							{
								tvPoint.setText(point + "");
								totalPrice -= point;
								tvTotalPrice.setText(String.format("￥%s", StringTools.getPrice(totalPrice)));
							}
							else
							{
								ToastView.showCenterToast(ActivityOrderAdd.this, R.drawable.ic_do_fail, "积分超限！");
							}
						}
						catch (Exception e)
						{
							ToastView.showCenterToast(ActivityOrderAdd.this, R.drawable.ic_do_fail, "积分输入错误！");
						}
					}

					@Override
					public void onCancelClick()
					{
					}
				});
				dialogInputPoint.show();
				break;
			}
			case R.id.layout_select_pay:
			{
				mPayService = new PayService(this);
				mPayService.addDataResponse(new IAjaxDataResponse()
				{
					@Override
					public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
					{
						if (url.endsWith(ApiConfig.GET_PAY_LIST))
						{
							TableData tableData = GsonTools.getObject(result, TableData.class);
							if (tableData != null)
							{
								List<PayInfo> payInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<PayInfo>>()
								{
								}.getType());
								List<FuncInfo> funcInfoList = new ArrayList<>();
								if (payInfoList != null && payInfoList.size() > 0)
								{
									for (PayInfo payInfo : payInfoList)
									{
										funcInfoList.add(new FuncInfo(R.drawable.camera, payInfo.getName()));
									}
								}
								final DialogList dialogList = new DialogList(ActivityOrderAdd.this);
								FuncsHorizontalAdapter funcsHorizontalAdapter = new FuncsHorizontalAdapter(ActivityOrderAdd.this, funcInfoList, new BaseFuncsAdapter.IFuncsAdapterItemClicked()
								{
									@Override
									public void onFuncClicked(FuncInfo funcInfo)
									{
										if (dialogList.isShowing())
										{
											dialogList.dismiss();
										}
										//										payType = funcInfo.getId();
										//										String payName = funcInfo.getLabel();
										//										if (payName.contains("支付宝"))
										//										{
										//											payType = PAY_ALIPAY;
										//										}
										//										else if (payName.contains("微信"))
										//										{
										//											payType = PAY_WX;
										//										}
										//										else if (payName.contains("余额"))
										//										{
										//											payType = DATA_PAY_AMOUNT;
										//										}
										tvPayName.setText(funcInfo.getLabel());
										//										tvPayName.setTag(funcInfo.getIndex());
									}
								});
								dialogList.setAdapter(funcsHorizontalAdapter);
								dialogList.show();
							}
							return true;
						}
						return false;
					}
				});
				mPayService.getPayTypeList(null, true);
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
						totalPrice = expressPrice + productsPrice;
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
				//				if (payType == 0)
				//				{
				//					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "请选择支付方式！");
				//					return;
				//				}
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
				mOrderService.addOrder(comment, "普通订单", productsJson, expressJson, pointAmount, null, true);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.ADD_ORDER))
		{
			showServerMsg(result, "下单成功！");
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				finish();
				//这里转到支付界面
				//				if (payType == PAY_ALIPAY)
				//				{
				//					Intent intent = new Intent(this, ActivityPayByBalance.class);
				//					intent.putExtra(ConstValues.DATA_ORDER_ID, returnInfo.getErrmsg());
				//					startActivity(intent);
				//				}
				//				else if (payType == PAY_WX)
				//				{
				//					Intent intent = new Intent(this, ActivityPayByBalance.class);
				//					intent.putExtra(ConstValues.DATA_ORDER_ID, returnInfo.getErrmsg());
				//					startActivity(intent);
				//				}
				//				else if (payType == DATA_PAY_AMOUNT)
				//				{
				//					Intent intent = new Intent(this, ActivityPayByBalance.class);
				//					intent.putExtra(ConstValues.DATA_ORDER_ID, returnInfo.getErrmsg());
				//					startActivity(intent);
				//				}
				//				else
				{
					Intent intent = new Intent(this, ActivityPayList.class);
					PayTypeInfo payTypeInfo = new PayTypeInfo(PayType.PAY_ORDER, totalPrice, "订单" + returnInfo.getErrmsg() + "支付", returnInfo.getErrmsg());
					intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, GsonTools.toJson(payTypeInfo));
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
			}
			tvAvailablePoint.setText(mMaxPoint + "积分可用，可消费" + mNeedPoint + "积分");
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_PRICE_POINT_INFO_LIST))
		{
			List<ProductPricePointInfo> productPricePointInfoList = GsonTools.getObjects(result, new TypeToken<List<ProductPricePointInfo>>()
			{
			}.getType());
			if (productPricePointInfoList != null && productPricePointInfoList.size() > 0)
			{
				for (ProductPricePointInfo productPricePointInfo : productPricePointInfoList)
				{
					mNeedPoint += productPricePointInfo.getMaxPoint();
				}
			}
			tvAvailablePoint.setText(mMaxPoint + "积分可用，可消费" + mNeedPoint + "积分");
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == REQ_SELECT_ADDRESS)
		{
			if (resultCode == RESULT_OK)
			{
				addressId = intent.getStringExtra(ConstValues.DATA_ADDRESS_ID);
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
		//		layoutAddAddress.setVisibility(View.VISIBLE);
		//		layoutShowAddress.setVisibility(View.GONE);
	}
}
