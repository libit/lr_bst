/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ShopProductService;
import com.lrcall.ui.adapter.SearchProductsAdapter;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LocationTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 微店商品列表
 */
public class ActivityShopProducts extends MyBasePageActivity implements View.OnClickListener, IAjaxDataResponse, AMapLocationListener
{
	private static final String TAG = ActivityShopProducts.class.getSimpleName();
	public static final int MSG_LOCATION_START = 113;//开始定位
	public static final int MSG_LOCATION_FINISH = 114;//定位完成
	private View layoutProductList, layoutNoProduct;
	private TextView tvAddress;
	private EditText etSearch;
	private SearchProductsAdapter searchProductsAdapter;
	private ShopProductService mProductService;
	private final List<ProductInfo> mProductInfoList = new ArrayList<>();
	String mLocation = "";
	String mShopId = "";
	private final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
				case MSG_LOCATION_START:
				{
					tvAddress.setText("正在定位...");
					break;
				}
				case MSG_LOCATION_FINISH:
				{
					AMapLocation loc = (AMapLocation) msg.obj;
					String location = LocationTools.getLocationDistrictStr(loc);
					if (!StringTools.isNull(location) && (StringTools.isNull(mLocation) || !mLocation.equals(location)))
					{
						mLocation = location;
						onRefresh();
					}
					String address = LocationTools.getLocationAddressStr(loc);
					if (!StringTools.isNull(address))
					{
						tvAddress.setText("您当前位置：" + address);
					}
					break;
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_products);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			mShopId = bundle.getString(ConstValues.DATA_SHOP_ID);
		}
		if (StringTools.isNull(mShopId))
		{
			mShopId = "";
		}
		mProductService = new ShopProductService(this);
		mProductService.addDataResponse(this);
		viewInit();
		beginLocation();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_shop_products, menu);
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
		else if (id == R.id.action_refresh_location)
		{
			beginLocation();
			return true;
		}
		else if (id == R.id.action_shop_list)
		{
			startActivity(new Intent(this, ActivityShopList.class));
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy()
	{
		if (null != locationClient)
		{
			/**
			 * 如果AMapLocationClient是在当前Activity实例化的，
			 * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
			 */
			locationClient.onDestroy();
			locationClient = null;
			locationOption = null;
		}
		super.onDestroy();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		layoutProductList = findViewById(R.id.layout_search_result);
		layoutNoProduct = findViewById(R.id.layout_no_product);
		tvAddress = (TextView) findViewById(R.id.tv_address);
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
		locationClient = new AMapLocationClient(this);
		locationOption = new AMapLocationClientOption();
		// 设置定位模式为低功耗模式
		locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
		// 设置定位监听
		locationClient.setLocationListener(this);
	}

	//刷新数据
	@Override
	public void refreshData()
	{
		mProductInfoList.clear();
		searchProductsAdapter = null;
		loadMoreData();
	}

	//加载更多商品
	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		String condition = etSearch.getText().toString();
		mProductService.getShopProductList(mShopId, mLocation, condition, mDataStart, getPageSize(), null, null, tips, true);
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

	synchronized private void refreshProducts(List<ProductInfo> productInfoList)
	{
		if (productInfoList == null || productInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mProductInfoList.size() < 1)
			{
				layoutProductList.setVisibility(View.GONE);
				layoutNoProduct.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutProductList.setVisibility(View.VISIBLE);
		layoutNoProduct.setVisibility(View.GONE);
		if (productInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (ProductInfo productInfo : productInfoList)
		{
			mProductInfoList.add(productInfo);
		}
		if (searchProductsAdapter == null)
		{
			searchProductsAdapter = new SearchProductsAdapter(this, mProductInfoList, new SearchProductsAdapter.IItemClick()
			{
				@Override
				public void onProductClicked(ProductInfo productInfo)
				{
					if (productInfo != null)
					{
						Intent intent = new Intent(ActivityShopProducts.this, ActivityProduct.class);
						intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
						startActivity(intent);
					}
				}
			});
			xListView.setAdapter(searchProductsAdapter);
		}
		else
		{
			searchProductsAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_LOCATION_SHOP_PRODUCT_LIST))
		{
			List<ProductInfo> productInfoList = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				productInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductInfo>>()
				{
				}.getType());
			}
			refreshProducts(productInfoList);
		}
		return false;
	}

	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;

	private void beginLocation()
	{
		initOption();
		// 设置定位参数
		locationClient.setLocationOption(locationOption);
		// 启动定位
		locationClient.startLocation();
		mHandler.sendEmptyMessage(MSG_LOCATION_START);
	}

	// 根据控件的选择，重新设置定位参数
	private void initOption()
	{
		// 设置是否需要显示地址信息
		locationOption.setNeedAddress(true);
		// 设置是否开启缓存
		locationOption.setLocationCacheEnable(true);
		//设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
		locationOption.setOnceLocationLatest(false);
		/**
		 *  设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
		 *  只有持续定位设置定位间隔才有效，单次定位无效
		 */
		locationOption.setInterval(1000);
	}

	// 定位监听
	@Override
	public void onLocationChanged(AMapLocation loc)
	{
		if (null != loc)
		{
			Message msg = mHandler.obtainMessage();
			msg.obj = loc;
			msg.what = MSG_LOCATION_FINISH;
			mHandler.sendMessage(msg);
		}
	}
}
