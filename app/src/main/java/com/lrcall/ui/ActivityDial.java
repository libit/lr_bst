/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.models.TabInfo;
import com.lrcall.utils.ConstValues;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

public class ActivityDial extends MyBaseActivity
{
	private final List<TabInfo> mTabInfoList = new ArrayList<>();
	private ViewPager viewPager;
	private Menu menu;
	private Boolean bShowPad = true;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dial);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			bShowPad = bundle.getBoolean(ConstValues.DATA_SHOW_PAD, true);
		}
		viewInit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_dial, menu);
		this.menu = menu;
		setMenu();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_search)
		{
			startActivity(new Intent(this, ActivitySearchContacts.class));
			return true;
		}
		else if (id == R.id.action_add_contact)
		{
			ContactsFactory.getInstance().toSystemAddContactActivity(this, "");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	synchronized protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		//初始化Tab
		if (bShowPad)
		{
			mTabInfoList.add(new TabInfo(0, "拨号", R.drawable.ic_tab_dialer_normal, FragmentDialer2.class));
		}
		mTabInfoList.add(new TabInfo(1, "联系人", R.drawable.ic_tab_contacts_normal, FragmentContacts.class));
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
						setTitle(tabInfo.getLabel());
					}
					else
					{
						tabInfo.getTvLabel().setTextColor(getResources().getColor(R.color.tab_text_disabled));
					}
				}
				setMenu();
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
			}
		});
		FragmentPagerItems pages = new FragmentPagerItems(this);
		for (TabInfo tabInfo : mTabInfoList)
		{
			pages.add(FragmentPagerItem.of(tabInfo.getLabel(), tabInfo.getLoadClass()));
		}
		FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);
		viewPager.setAdapter(adapter);
		viewPagerTab.setViewPager(viewPager);
		setTitle(mTabInfoList.get(0).getLabel());
		mTabInfoList.get(0).getTvLabel().setTextColor(getResources().getColor(R.color.tab_text_enabled));
		if (!bShowPad)
		{
			//			setTitle("联系人");
			findViewById(R.id.tab).setVisibility(View.GONE);
		}
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

	/**
	 * 设置菜单
	 */
	private void setMenu()
	{
		if (menu == null)
		{
			return;
		}
		int index = viewPager.getCurrentItem();
		if (index == 0)
		{
			menu.findItem(R.id.action_add_contact).setVisible(false);
		}
		else if (index == 1)
		{
			menu.findItem(R.id.action_add_contact).setVisible(true);
		}
	}
}
