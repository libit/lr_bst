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

import com.lrcall.appbst.R;
import com.lrcall.models.TabInfo;
import com.lrcall.utils.ConstValues;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

public class ActivityHuodong extends MyBaseActivity
{
	private final List<TabInfo> mTabInfoList = new ArrayList<>();
	public static final String NEWS = "news";
	public static final String PANIC = "panic";
	public static final String RECOMMEND = "recommend";
	private ViewPager viewPager;
	private int index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huodong);
		index = getIntent().getIntExtra(ConstValues.DATA_INDEX, 0);
		viewInit();
	}

	@Override
	synchronized protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		//初始化Tab
		mTabInfoList.add(new TabInfo(0, "今日上新", FragmentHuodongProductList.class));
		mTabInfoList.add(new TabInfo(1, "限时疯抢", FragmentHuodongProductList.class));
		mTabInfoList.add(new TabInfo(2, "热门推荐", FragmentHuodongProductList.class));
		ViewGroup tab = (ViewGroup) findViewById(R.id.tab);
		//加载tab布局
		tab.addView(LayoutInflater.from(this).inflate(R.layout.layout_tab_text, tab, false));
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
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
		FragmentPagerItems pages = new FragmentPagerItems(this);
		String[] sorts = new String[]{NEWS, PANIC, RECOMMEND};
		for (int i = 0; i < mTabInfoList.size(); i++)
		{
			TabInfo tabInfo = mTabInfoList.get(i);
			Bundle bundle = new Bundle();
			bundle.putString(FragmentHuodongProductList.ARG_SORT_ID, sorts[i]);
			pages.add(FragmentPagerItem.of(tabInfo.getLabel(), tabInfo.getLoadClass(), bundle));
		}
		FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);
		viewPager.setAdapter(adapter);
		viewPagerTab.setViewPager(viewPager);
		viewPager.setCurrentItem(index);
		//		mTabInfoList.get(index).getTvLabel().setTextColor(getResources().getColor(R.color.tab_text_enabled));
	}

	/**
	 * 设置标题
	 *
	 * @param title
	 */
	@Override
	public void setTitle(CharSequence title)
	{
		mToolbar.setTitle(title);
	}
}
