/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.enums.OrderStatus;
import com.lrcall.models.TabInfo;
import com.lrcall.ui.adapter.SectionsPagerAdapter;
import com.lrcall.ui.customer.DisplayTools;
import com.lrcall.utils.ConstValues;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

public class ActivityOrders extends MyBaseActivity
{
	private byte orderType;
	private ViewPager viewPager;
	private final List<Fragment> fragmentList = new ArrayList<>();
	private final List<TabInfo> tabInfos = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders);
		orderType = getIntent().getByteExtra(ConstValues.DATA_ORDER_TYPE, OrderStatus.WAIT_PAY.getStatus());
		viewInit();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		//设置滑动返回区域
		getSwipeBackLayout().setEdgeSize(DisplayTools.getWindowWidth(this) / 4);
		setBackButton();
		tabInfos.add(new TabInfo(0, OrderStatus.WAIT_PAY.getDesc(), null));
		tabInfos.add(new TabInfo(1, OrderStatus.PAYED.getDesc(), null));
		tabInfos.add(new TabInfo(2, OrderStatus.EXPRESS.getDesc(), null));
		tabInfos.add(new TabInfo(3, OrderStatus.FINISH.getDesc(), null));
		fragmentList.add(FragmentOrderList.newInstance(OrderStatus.WAIT_PAY.getStatus()));
		fragmentList.add(FragmentOrderList.newInstance(OrderStatus.PAYED.getStatus()));
		fragmentList.add(FragmentOrderList.newInstance(OrderStatus.EXPRESS.getStatus()));
		fragmentList.add(FragmentOrderList.newInstance(OrderStatus.FINISH.getStatus()));
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
		final LayoutInflater inflater = LayoutInflater.from(viewPagerTab.getContext());
		viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider()
		{
			@Override
			public View createTabView(ViewGroup container, int position, PagerAdapter adapter)
			{
				TabInfo tabInfo = tabInfos.get(position);
				View view = inflater.inflate(R.layout.item_product_tab, container, false);
				TextView textView = (TextView) view.findViewById(R.id.tab_label);
				textView.setText(tabInfo.getLabel());
				tabInfo.setTvLabel(textView);
				return view;
			}
		});
		SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragmentList);
		viewPager.setAdapter(adapter);
		viewPagerTab.setViewPager(viewPager);
		if (orderType == OrderStatus.WAIT_PAY.getStatus())
		{
			viewPager.setCurrentItem(0);
		}
		else if (orderType == OrderStatus.PAYED.getStatus())
		{
			viewPager.setCurrentItem(1);
		}
		else if (orderType == OrderStatus.EXPRESS.getStatus())
		{
			viewPager.setCurrentItem(2);
		}
		else if (orderType == OrderStatus.FINISH.getStatus())
		{
			viewPager.setCurrentItem(3);
		}
	}
}
