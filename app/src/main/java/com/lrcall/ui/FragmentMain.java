/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;
import com.lrcall.appbst.R;
import com.lrcall.appbst.services.ProductSortService;
import com.lrcall.models.TabInfo;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

public class FragmentMain extends MyBaseFragment implements View.OnClickListener
{
	private static final String TAG = FragmentMain.class.getSimpleName();
	private static final int REQ_SCAN_QR = 1200;
	private final List<TabInfo> mTabInfoList = new ArrayList<>();
	private EditText etSearch;
	private ViewPager viewPager;
	private SmartTabLayout viewPagerTab;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		viewInit(rootView);
		return rootView;
	}

	@Override
	protected void viewInit(View rootView)
	{
		rootView.findViewById(R.id.iv_menu).setOnClickListener(this);
		rootView.findViewById(R.id.btn_qr).setOnClickListener(this);
		//		View layoutSearch = rootView.findViewById(R.id.layout_search);
		etSearch = (EditText) rootView.findViewById(R.id.et_search);
		etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (v.getId() == etSearch.getId() && hasFocus)
				{
					startActivity(new Intent(FragmentMain.this.getContext(), ActivityProductsSearch.class));
					v.clearFocus();
				}
			}
		});
		ViewGroup tab = (ViewGroup) rootView.findViewById(R.id.tab);
		//加载tab布局
		tab.addView(LayoutInflater.from(this.getContext()).inflate(R.layout.layout_tab_text, tab, false));
		viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
		viewPagerTab = (SmartTabLayout) rootView.findViewById(R.id.viewpagertab);
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
		//初始化Tab
		mTabInfoList.clear();
		mTabInfoList.add(new TabInfo(0, "首页", FragmentIndex.class));
		mTabInfoList.add(new TabInfo(1, "海外购", FragmentProductList.class));
		mTabInfoList.add(new TabInfo(2, "手购直营", FragmentProductList.class));
		FragmentPagerItems pages = new FragmentPagerItems(this.getContext());
		for (TabInfo tabInfo : mTabInfoList)
		{
			Bundle bundle = new Bundle();
			if (tabInfo.getLabel().equalsIgnoreCase("海外购"))
			{
				bundle.putString(FragmentProductList.ARG_SORT_ID, ProductSortService.HAIWAIGOU);
			}
			else if (tabInfo.getLabel().equalsIgnoreCase("手购直营"))
			{
				bundle.putString(FragmentProductList.ARG_SORT_ID, ProductSortService.ZHIYING);
			}
			pages.add(FragmentPagerItem.of(tabInfo.getLabel(), tabInfo.getLoadClass(), bundle));
		}
		FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), pages);
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(mTabInfoList.size());
		viewPagerTab.setViewPager(viewPager);
		mTabInfoList.get(0).getTvLabel().setTextColor(getResources().getColor(R.color.tab_text_enabled));
		super.viewInit(rootView);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.iv_menu:
			{
				if (ActivityMain.getInstance() != null)
				{
					ActivityMain.getInstance().getmDrawerLayout().openDrawer(Gravity.LEFT);
				}
				break;
			}
			case R.id.btn_qr:
			{
				Intent intent = new Intent(this.getContext(), CaptureActivity.class);
				startActivityForResult(intent, REQ_SCAN_QR);
				break;
			}
		}
	}
}
