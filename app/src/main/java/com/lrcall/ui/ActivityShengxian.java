/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.BannerInfo;
import com.lrcall.appbst.models.ProductSortInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.BannerService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ProductSortService;
import com.lrcall.enums.ClientBannerType;
import com.lrcall.models.TabInfo;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

public class ActivityShengxian extends MyBaseBannerActivity implements IAjaxDataResponse
{
	private final List<TabInfo> mTabInfoList = new ArrayList<>();
	private ViewPager viewPager;
	private SmartTabLayout viewPagerTab;
	private BannerService mBannerService;
	private ProductSortService mProductSortService;
	private String sortId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shengxian);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			sortId = bundle.getString(ConstValues.DATA_PRODUCT_SORT_ID);
		}
		if (StringTools.isNull(sortId))
		{
			finish();
			Toast.makeText(this, "商品分类不存在！", Toast.LENGTH_LONG).show();
		}
		mBannerService = new BannerService(this);
		mBannerService.addDataResponse(this);
		mProductSortService = new ProductSortService(this);
		mProductSortService.addDataResponse(this);
		viewInit();
		String bannerType = "";
		if (sortId.equalsIgnoreCase(ProductSortService.SHENGXIAN))
		{
			bannerType = ClientBannerType.SHENGXIAN.getType();
		}
		else if (sortId.equalsIgnoreCase(ProductSortService.MEISHI))
		{
			bannerType = ClientBannerType.MEISHI.getType();
		}
		else if (sortId.equalsIgnoreCase(ProductSortService.MEIZHUAN))
		{
			bannerType = ClientBannerType.MEIZHUAN.getType();
		}
		else if (sortId.equalsIgnoreCase(ProductSortService.HAIWAIGOU))
		{
			bannerType = ClientBannerType.HAIWAIGOU.getType();
		}
		else if (sortId.equalsIgnoreCase(ProductSortService.TECHAN))
		{
			bannerType = ClientBannerType.TECHAN.getType();
		}
		else if (sortId.equalsIgnoreCase(ProductSortService.FUZHUANG))
		{
			bannerType = ClientBannerType.FUZHUANG.getType();
		}
		else if (sortId.equalsIgnoreCase(ProductSortService.ZHIYING))
		{
			bannerType = ClientBannerType.ZHIYING.getType();
		}
		else
		{
			bannerType = ClientBannerType.PAGE_INDEX.getType();
		}
		mBannerService.getBannerInfoList(bannerType, 0, -1, null, true);
		mProductSortService.getProductSortList(sortId, null, true);
		updateView();
	}

	@Override
	synchronized protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		ViewGroup tab = (ViewGroup) findViewById(R.id.tab);
		//加载tab布局
		tab.addView(LayoutInflater.from(this).inflate(R.layout.layout_tab_text, tab, false));
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
		viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider()
		{
			@Override
			public View createTabView(ViewGroup container, int position, PagerAdapter adapter)
			{
				TabInfo tabInfo = mTabInfoList.get(position);
				View view = LayoutInflater.from(viewPagerTab.getContext()).inflate(R.layout.item_tab_text, container, false);
				TextView textView = (TextView) view.findViewById(R.id.tab_label);
				textView.setText(tabInfo.getLabel());
				tabInfo.setTvLabel(textView);
				return view;
			}
		});
		viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{
			}

			@Override
			public void onPageSelected(int position)
			{
				int size = mTabInfoList.size();
				for (int i = 0; i < size; i++)
				{
					TabInfo tabInfo = mTabInfoList.get(i);
					if (i == position)
					{
						tabInfo.getTvLabel().setTextColor(getResources().getColor(R.color.tab_text_enabled));
						//						mToolbar.setTitle(tabInfo.getLabel());
					}
					else
					{
						tabInfo.getTvLabel().setTextColor(getResources().getColor(R.color.tab_text_disabled));
					}
				}
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
			}
		});
		bannerViewPager = (ViewPager) findViewById(R.id.banner_viewpager);
		bannerViewPagerTab = (SmartTabLayout) findViewById(R.id.banner_viewpagertab);
		setBannerWidthAndHeight(DisplayTools.getWindowWidth(this), DisplayTools.getWindowWidth(this) * 1 / 2);
		initBannerView();
	}

	private void initPage(List<ProductSortInfo> productSortInfoList)
	{
		if (productSortInfoList == null || productSortInfoList.size() < 1)
		{
			return;
		}
		//初始化Tab
		mTabInfoList.clear();
		FragmentPagerItems pages = new FragmentPagerItems(this);
		for (int i = 0; i < productSortInfoList.size(); i++)
		{
			ProductSortInfo productSortInfo = productSortInfoList.get(i);
			TabInfo tabInfo = new TabInfo(i, productSortInfo.getName(), FragmentProductList.class);
			mTabInfoList.add(tabInfo);
			Bundle bundle = new Bundle();
			bundle.putString(FragmentProductList.ARG_SORT_ID, productSortInfo.getSortId());
			pages.add(FragmentPagerItem.of(tabInfo.getLabel(), tabInfo.getLoadClass(), bundle));
		}
		//		for (TabInfo tabInfo : mTabInfoList)
		//		{
		//			Bundle bundle = new Bundle();
		//			bundle.putString(FragmentProductList.ARG_SORT_ID, tabInfo.getLabel());
		//			pages.add(FragmentPagerItem.of(tabInfo.getLabel(), tabInfo.getLoadClass()));
		//		}
		FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);
		viewPager.setAdapter(adapter);
		viewPagerTab.setViewPager(viewPager);
		mTabInfoList.get(0).getTvLabel().setTextColor(getResources().getColor(R.color.tab_text_enabled));
	}

	/**
	 * 设置当前到第几页
	 *
	 * @param index
	 */
	public void setCurrentPage(int index)
	{
		if (index > -1 && index < mTabInfoList.size())
		{
			viewPager.setCurrentItem(index);
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_BANNER_LIST))
		{
			List<BannerInfo> bannerInfoList = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				bannerInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<BannerInfo>>()
				{
				}.getType());
			}
			setViewPagerAdapter(bannerInfoList);
		}
		else if (url.endsWith(ApiConfig.GET_PRODUCT_SORT_LIST))
		{
			List<ProductSortInfo> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductSortInfo>>()
				{
				}.getType());
			}
			initPage(list);
		}
		return false;
	}
}
