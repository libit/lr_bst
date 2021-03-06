/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.easeui.EaseConstant;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.OrderProductInfo;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.ProductStarInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ProductHistoryService;
import com.lrcall.appbst.services.ProductService;
import com.lrcall.appbst.services.ProductStarService;
import com.lrcall.appbst.services.ShopCartService;
import com.lrcall.appbst.services.UserService;
import com.lrcall.db.DbProductStarInfoFactory;
import com.lrcall.enums.ProductType;
import com.lrcall.events.UserEvent;
import com.lrcall.models.TabInfo;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;
import com.lrcall.utils.apptools.AppFactory;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.PermissionUtils;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ActivityProduct extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final int PAGE_COUNT = 3;
	private ViewPager viewPager;
	private ImageView ivAddStar;
	private final List<TabInfo> mTabInfos = new ArrayList<>();
	private ProductStarService mProductStarService;
	private ShopCartService mShopCartService;
	private ProductService mProductService;
	private ProductHistoryService mProductHistoryService;
	private boolean isStared = false;
	private String mShopId;
	private String productId;
	private String mReferrerId = "";
	private ProductInfo mProductInfo;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			productId = bundle.getString(ConstValues.DATA_PRODUCT_ID);
			mShopId = bundle.getString(ConstValues.DATA_SHOP_ID);
			mReferrerId = bundle.getString(ConstValues.DATA_REFERRER_ID);
		}
		if (StringTools.isNull(productId))
		{
			finish();
			Toast.makeText(this, "商品不存在！", Toast.LENGTH_LONG).show();
		}
		mProductStarService = new ProductStarService(this);
		mProductStarService.addDataResponse(this);
		mShopCartService = new ShopCartService(this);
		mShopCartService.addDataResponse(this);
		mProductHistoryService = new ProductHistoryService(this);
		mProductHistoryService.addDataResponse(this);
		mProductService = new ProductService(this);
		mProductService.addDataResponse(this);
		viewInit();
		EventBus.getDefault().register(this);
		if (AppFactory.isCompatible(23))
		{
			ActivityProductPermissionsDispatcher.initDataWithCheck(this);
		}
		else
		{
			initData();
		}
		mProductHistoryService.addProductHistoryInfo(productId, null, true);//添加到浏览历史
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		mTabInfos.clear();
		mTabInfos.add(new TabInfo(0, "商品", FragmentProduct.class));
		mTabInfos.add(new TabInfo(1, "详情", FragmentProductWeb.class));
		mTabInfos.add(new TabInfo(2, "评价", FragmentProductComments.class));
		ViewGroup tab = (ViewGroup) findViewById(R.id.tab);
		//加载tab布局
		tab.addView(LayoutInflater.from(this).inflate(R.layout.layout_distribute_evenly, tab, false));
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
		viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider()
		{
			@Override
			public View createTabView(ViewGroup container, int position, PagerAdapter adapter)
			{
				TabInfo tabInfo = mTabInfos.get(position);
				View view = LayoutInflater.from(viewPagerTab.getContext()).inflate(R.layout.item_tab_text, container, false);
				TextView textView = (TextView) view.findViewById(R.id.tab_label);
				textView.setText(tabInfo.getLabel());
				tabInfo.setTvLabel(textView);
				return view;
			}
		});
		viewPager.setOffscreenPageLimit(PAGE_COUNT);
		FragmentPagerItems pages = new FragmentPagerItems(this);
		//初始化Tab
		Bundle bundle = new Bundle();
		bundle.putString(ConstValues.DATA_PRODUCT_ID, productId);
		bundle.putString(ConstValues.DATA_SHOP_ID, mShopId);
		bundle.putString(ConstValues.DATA_PRODUCT_TYPE, ProductType.PRODUCT.getType() + "");
		for (TabInfo tabInfo : mTabInfos)
		{
			pages.add(FragmentPagerItem.of(tabInfo.getLabel(), tabInfo.getLoadClass(), bundle));
		}
		FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);
		viewPager.setAdapter(adapter);
		viewPagerTab.setViewPager(viewPager);
		ivAddStar = (ImageView) findViewById(R.id.iv_add_star);
		//		ivAddStar.setOnClickListener(this);
		findViewById(R.id.btn_kefu).setOnClickListener(this);
		findViewById(R.id.btn_buy).setOnClickListener(this);
		findViewById(R.id.btn_add_star).setOnClickListener(this);
		findViewById(R.id.btn_add_cart).setOnClickListener(this);
		findViewById(R.id.btn_go_cart).setOnClickListener(this);
		findViewById(R.id.iv_share).setOnClickListener(this);
	}

	public void setPage(int index)
	{
		viewPager.setCurrentItem(index);
	}

	@OnPermissionDenied({Manifest.permission.SYSTEM_ALERT_WINDOW})
	public void initDataDenied()
	{
		Toast.makeText(this, "您拒绝了应用所需的权限，应用将不能工作！", Toast.LENGTH_LONG).show();
		finish();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (!PermissionUtils.verifyPermissions(grantResults))
		{
			initDataDenied();
		}
		else
		{
			initData();
		}
	}

	@NeedsPermission({Manifest.permission.SYSTEM_ALERT_WINDOW})
	protected void initData()
	{
		ProductStarInfo productStarInfo = DbProductStarInfoFactory.getInstance().getProductStarInfo(PreferenceUtils.getInstance().getUserId(), productId);
		if (productStarInfo != null)
		{
			isStared = true;
			ivAddStar.setImageResource(R.drawable.item_info_collection_btn);
		}
		else
		{
			isStared = false;
			ivAddStar.setImageResource(R.drawable.item_info_collection_disabled_btn);
			mProductStarService.getProductStarInfo(productId, null, true);
		}
		mProductService.getProductInfo(productId, null, true);
	}

	@Override
	protected void onPause()
	{
		if (FragmentProduct.getInstance() != null)
		{
			FragmentProduct.getInstance().clearFloatWindow();
		}
		super.onPause();
	}

	@Override
	public void onDestroy()
	{
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Subscribe
	public void onEventMainThread(final UserEvent userEvent)
	{
		mHandler.post(new Thread()
		{
			@Override
			public void run()
			{
				super.run();
				if (userEvent != null)
				{
					if (userEvent.getEvent().equals(UserEvent.EVENT_LOGINED))
					{
						mProductService.getProductInfo(productId, null, true);
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_kefu:
			{
				if (UserService.isLogin())
				{
					if (mProductInfo != null)
					{
						if (StringTools.isNull(mProductInfo.getShopId()))
						{
							startActivity(new Intent(this, ActivityImKefuList.class));
						}
						else
						{
							startActivity(new Intent(this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, mProductInfo.getShopId()));
						}
					}
				}
				else
				{
					startActivity(new Intent(this, ActivityLogin.class));
				}
				break;
			}
			case R.id.btn_add_star:
			{
				if (UserService.isLogin())
				{
					if (!isStared)
					{
						mProductStarService.addProductStarInfo(productId, null, true);
					}
					else
					{
						mProductStarService.deleteProductStarInfo(productId, null, true);
					}
				}
				else
				{
					startActivity(new Intent(this, ActivityLogin.class));
				}
				break;
			}
			case R.id.btn_add_cart:
			{
				if (UserService.isLogin())
				{
					FragmentProduct fragmentProduct = FragmentProduct.getInstance();
					int amount = 1;
					if (fragmentProduct != null)
					{
						try
						{
							amount = Integer.parseInt(fragmentProduct.tvAmount.getText().toString());
						}
						catch (Exception e)
						{
							amount = 1;
						}
					}
					mShopCartService.addShopCartInfo(productId, amount, mShopId, mReferrerId, "请稍后...", true);
				}
				else
				{
					startActivity(new Intent(this, ActivityLogin.class));
				}
				break;
			}
			case R.id.btn_buy:
			{
				if (UserService.isLogin())
				{
					FragmentProduct fragmentProduct = FragmentProduct.getInstance();
					int amount = 1;
					if (fragmentProduct != null)
					{
						try
						{
							amount = Integer.parseInt(fragmentProduct.tvAmount.getText().toString());
						}
						catch (Exception e)
						{
						}
					}
					Intent intent = new Intent(this, ActivityOrderAdd.class);
					ArrayList<OrderProductInfo> orderProductInfos = new ArrayList<>();
					//					ProductInfo productInfo = new ProductInfo();
					//					productInfo.setProductId(productId);
					OrderProductInfo orderProductInfo = new OrderProductInfo();
					orderProductInfo.setCount(amount);
					orderProductInfo.setProductId(productId);
					orderProductInfo.setShopId(mShopId);
					orderProductInfo.setReferrerId(mReferrerId);
					//					orderProductInfo.setProductInfo(productInfo);
					orderProductInfos.add(orderProductInfo);
					intent.putExtra(ConstValues.DATA_ORDER_PRODUCT_LIST, GsonTools.toJson(orderProductInfos));
					//					intent.putExtra(ConstValues.DATA_SHOP_ID, mShopId);
					//					intent.putExtra(ConstValues.DATA_REFERRER_ID, mReferrerId);
					startActivity(intent);
				}
				else
				{
					startActivity(new Intent(this, ActivityLogin.class));
				}
				break;
			}
			case R.id.btn_go_cart:
			{
				if (UserService.isLogin())
				{
					Intent intent = new Intent(this, ActivityShopCart.class);
					startActivity(intent);
				}
				else
				{
					startActivity(new Intent(this, ActivityLogin.class));
				}
				break;
			}
			case R.id.iv_share:
			{
				Intent intent = new Intent(this, ActivityShare.class);
				//				String data = GsonTools.toJson(new ShareProductData(ApiConfig.getServerProductUrl(productId), productId, PreferenceUtils.getInstance().getUserId()));
				String data = ApiConfig.getServerProductUrl(productId);
				intent.putExtra(ConstValues.DATA_SHARE_DATA, data);
				startActivity(intent);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.ADD_STAR_INFO))
		{
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				isStared = true;
				ivAddStar.setImageResource(R.drawable.item_info_collection_btn);
			}
			else
			{
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, "收藏失败，请重试！");
			}
		}
		else if (url.endsWith(ApiConfig.GET_STAR_INFO))
		{
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				isStared = true;
				ivAddStar.setImageResource(R.drawable.item_info_collection_btn);
			}
			else
			{
				isStared = false;
				ivAddStar.setImageResource(R.drawable.item_info_collection_disabled_btn);
			}
		}
		else if (url.endsWith(ApiConfig.DELETE_STAR_INFO))
		{
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				isStared = false;
				ivAddStar.setImageResource(R.drawable.item_info_collection_disabled_btn);
			}
		}
		else if (url.endsWith(ApiConfig.ADD_SHOP_CART_INFO))
		{
			showServerMsg(result, "添加到购物车成功！");
		}
		else if (url.endsWith(ApiConfig.GET_PRODUCT_INFO))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				mProductInfo = GsonTools.getReturnObject(result, ProductInfo.class);
			}
			return true;
		}
		return false;
	}
}
