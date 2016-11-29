/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.OrderProductInfo;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.ShopCartInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ProductService;
import com.lrcall.appbst.services.ShopCartService;
import com.lrcall.db.DbProductInfoFactory;
import com.lrcall.ui.adapter.ShopCartProductsAdapter;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityShopCart extends MyBasePageActivity implements View.OnClickListener, IAjaxDataResponse, CompoundButton.OnCheckedChangeListener
{
	private ShopCartService mShopCartService;
	private final List<ShopCartInfo> mShopCartInfoList = new ArrayList<>();
	private View layoutNoProducts, layoutProducts;
	private ShopCartProductsAdapter mShopCartProductsAdapter;
	private CheckBox cbSelectAll;
	private TextView tvTotalPrice;
	private int mTotalPrice;
	private final Map<ShopCartInfo, CheckBox> mProductCheckBoxMap = new HashMap<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_cart);
		viewInit();
		mShopCartService = new ShopCartService(this);
		mShopCartService.addDataResponse(this);
		onRefresh();
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
		layoutNoProducts = findViewById(R.id.layout_no_products);
		findViewById(R.id.btn_go_shopping).setOnClickListener(this);
		layoutProducts = findViewById(R.id.layout_products);
		findViewById(R.id.btn_submit).setOnClickListener(this);
		cbSelectAll = (CheckBox) findViewById(R.id.cb_select_all);
		tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
		mTotalPrice = 0;
		cbSelectAll.setOnCheckedChangeListener(this);
	}

	/**
	 * 刷新数据
	 */
	@Override
	public void refreshData()
	{
		mShopCartInfoList.clear();
		mShopCartProductsAdapter = null;
		loadMoreData();
	}

	/**
	 * 加载更多数据
	 */
	@Override
	public void loadMoreData()
	{
		mShopCartService.getShopCartInfoList(mDataStart, getPageSize(), null, true);
	}

	synchronized private void refreshProducts(List<ShopCartInfo> shopCartInfoList)
	{
		if (shopCartInfoList == null || shopCartInfoList.size() < 1)
		{
			if (mShopCartInfoList.size() < 1)
			{
				layoutNoProducts.setVisibility(View.VISIBLE);
				layoutProducts.setVisibility(View.GONE);
				//				if (AppFactory.isCompatible(11))
				//				{
				//					if (mMenu != null)
				//					{
				//						mMenu.findItem(R.id.action_shop_cart_edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
				//					}
				//				}
			}
			xListView.setPullLoadEnable(false);
			return;
		}
		layoutNoProducts.setVisibility(View.GONE);
		layoutProducts.setVisibility(View.VISIBLE);
		//		if (AppFactory.isCompatible(11))
		//		{
		//			if (mMenu != null)
		//			{
		//				mMenu.findItem(R.id.action_shop_cart_edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		//			}
		//		}
		if (shopCartInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		mTotalPrice = 0;
		for (ShopCartInfo shopCartInfo : shopCartInfoList)
		{
			mShopCartInfoList.add(shopCartInfo);
			ProductInfo productInfo = DbProductInfoFactory.getInstance().getProductInfo(shopCartInfo.getProductId());
			if (productInfo != null)
			{
				mTotalPrice += productInfo.getPrice() * shopCartInfo.getAmount();
			}
			else
			{
				final ShopCartInfo shopCart = shopCartInfo;
				ProductService productService = new ProductService(this);
				productService.addDataResponse(new IAjaxDataResponse()
				{
					@Override
					public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
					{
						if (url.endsWith(ApiConfig.GET_PRODUCT_INFO))
						{
							ProductInfo productInfo1 = GsonTools.getObject(result, ProductInfo.class);
							if (productInfo1 != null)
							{
								DbProductInfoFactory.getInstance().addOrUpdateProductInfo(productInfo1);
								mTotalPrice += productInfo1.getPrice() * shopCart.getAmount();
								tvTotalPrice.setText("￥" + StringTools.getPrice(mTotalPrice));
							}
						}
						return true;
					}
				});
				productService.getProductInfo(shopCartInfo.getProductId(), null, false);
			}
		}
		tvTotalPrice.setText("￥" + StringTools.getPrice(mTotalPrice));
		if (mShopCartProductsAdapter == null)
		{
			mShopCartProductsAdapter = new ShopCartProductsAdapter(this, mShopCartInfoList, mProductCheckBoxMap, new ShopCartProductsAdapter.IItemClicked()
			{
				@Override
				public void onProductClicked(ProductInfo productInfo)
				{
				}

				@Override
				public void onDeleteClicked(ShopCartInfo shopCartInfo)
				{
					mShopCartService.deleteShopCartInfo(shopCartInfo.getCartId(), null, true);
				}

				@Override
				public void onCheckClicked(ShopCartInfo shopCartInfo, ProductInfo productInfo, boolean isChecked)
				{
					if (isChecked)
					{
						mTotalPrice += productInfo.getPrice() * shopCartInfo.getAmount();
						boolean isSelectAll = true;
						for (CheckBox checkBox : mProductCheckBoxMap.values())
						{
							if (!checkBox.isChecked())
							{
								isSelectAll = false;
								break;
							}
						}
						if (isSelectAll)
						{
							cbSelectAll.setOnCheckedChangeListener(null);
							cbSelectAll.setChecked(true);
							cbSelectAll.setOnCheckedChangeListener(ActivityShopCart.this);
						}
					}
					else
					{
						mTotalPrice -= productInfo.getPrice() * shopCartInfo.getAmount();
						cbSelectAll.setOnCheckedChangeListener(null);
						cbSelectAll.setChecked(false);
						cbSelectAll.setOnCheckedChangeListener(ActivityShopCart.this);
					}
					tvTotalPrice.setText("￥" + StringTools.getPrice(mTotalPrice));
				}
			});
			xListView.setAdapter(mShopCartProductsAdapter);
		}
		else
		{
			mShopCartProductsAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_go_shopping:
			{
				finish();
				if (ActivityMain.getInstance() != null)
				{
					ActivityMain.getInstance().setCurrentPage(ActivityMain.INDEX);
				}
				break;
			}
			case R.id.btn_submit:
			{
				finish();
				Intent intent = new Intent(this, ActivityOrderAdd.class);
				ArrayList<OrderProductInfo> orderProductInfos = new ArrayList<>();
				for (ShopCartInfo shopCartInfo : mShopCartInfoList)
				{
					CheckBox checkBox = mProductCheckBoxMap.get(shopCartInfo);
					if (checkBox.isChecked())
					{
						//						ProductInfo productInfo = new ProductInfo();
						//						productInfo.setProductId(shopCartInfo.getProductId());
						OrderProductInfo orderProductInfo = new OrderProductInfo();
						orderProductInfo.setCount(shopCartInfo.getAmount());
						orderProductInfo.setProductId(shopCartInfo.getProductId());
						orderProductInfo.setShopId(shopCartInfo.getShopId());
						orderProductInfo.setReferrerId(shopCartInfo.getReferrerId());
						//						orderProductInfo.setProductInfo(productInfo);
						orderProductInfos.add(orderProductInfo);
					}
				}
				intent.putExtra(ConstValues.DATA_ORDER_PRODUCT_LIST, GsonTools.toJson(orderProductInfos));
				startActivity(intent);
				break;
			}
		}
	}

	/**
	 * ajax回调函数
	 *
	 * @param url
	 * @param result 返回结果
	 * @param status 状态
	 * @return
	 */
	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_SHOP_CART_LIST))
		{
			List<ShopCartInfo> shopCartInfoList = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				shopCartInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ShopCartInfo>>()
				{
				}.getType());
			}
			refreshProducts(shopCartInfoList);
		}
		else if (url.endsWith(ApiConfig.DELETE_SHOP_CART_INFO))
		{
			showServerMsg(result, "删除成功！");
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				onRefresh();
			}
		}
		return false;
	}
	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu)
	//	{
	//		getMenuInflater().inflate(R.menu.menu_activity_shop_cart, menu);
	//		mMenu = menu;
	//		return true;
	//	}
	//
	//	@Override
	//	public boolean onOptionsItemSelected(MenuItem item)
	//	{
	//		int id = item.getItemId();
	//		if (id == R.id.action_shop_cart_edit)
	//		{
	//			return true;
	//		}
	//		return super.onOptionsItemSelected(item);
	//	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		if (buttonView.getId() == R.id.cb_select_all)
		{
			for (CheckBox checkBox : mProductCheckBoxMap.values())
			{
				checkBox.setChecked(isChecked);
			}
			mTotalPrice = 0;
			if (isChecked)
			{
				for (ShopCartInfo shopCartInfo : mShopCartInfoList)
				{
					ProductInfo productInfo = DbProductInfoFactory.getInstance().getProductInfo(shopCartInfo.getProductId());
					if (productInfo != null)
					{
						mTotalPrice += productInfo.getPrice() * shopCartInfo.getAmount();
					}
					else
					{
						final ShopCartInfo shopCart = shopCartInfo;
						ProductService productService = new ProductService(ActivityShopCart.this);
						productService.addDataResponse(new IAjaxDataResponse()
						{
							@Override
							public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
							{
								if (url.endsWith(ApiConfig.GET_PRODUCT_INFO))
								{
									ProductInfo productInfo1 = GsonTools.getObject(result, ProductInfo.class);
									if (productInfo1 != null)
									{
										DbProductInfoFactory.getInstance().addOrUpdateProductInfo(productInfo1);
										mTotalPrice += productInfo1.getPrice() * shopCart.getAmount();
										tvTotalPrice.setText("￥" + StringTools.getPrice(mTotalPrice));
									}
								}
								return true;
							}
						});
						productService.getProductInfo(shopCartInfo.getProductId(), null, false);
					}
				}
			}
			tvTotalPrice.setText("￥" + StringTools.getPrice(mTotalPrice));
		}
	}
}
