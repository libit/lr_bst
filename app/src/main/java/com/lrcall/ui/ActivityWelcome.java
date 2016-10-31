/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lrcall.appbst.R;
import com.lrcall.ui.adapter.SectionsPagerAdapter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

public class ActivityWelcome extends MyBaseActivity implements View.OnClickListener
{
	private ViewPager viewPager;
	private View btnStart;
	private final List<Fragment> fragmentWelcomeList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		viewInit();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setSwipeBackEnable(false); //禁止滑动返回
		fragmentWelcomeList.add(FragmentWelcome.newInstance(R.drawable.w1, ""));
		fragmentWelcomeList.add(FragmentWelcome.newInstance(R.drawable.w2, ""));
		fragmentWelcomeList.add(FragmentWelcome.newInstance(R.drawable.w3, ""));
		fragmentWelcomeList.add(FragmentWelcome.newInstance(R.drawable.w4, ""));
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
		SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragmentWelcomeList);
		viewPager.setAdapter(adapter);
		viewPagerTab.setViewPager(viewPager);
		btnStart = findViewById(R.id.btn_start);
		btnStart.setOnClickListener(this);
		viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{
			}

			@Override
			public void onPageSelected(int position)
			{
				if (position >= fragmentWelcomeList.size() - 1)
				{
					btnStart.setVisibility(View.VISIBLE);
					viewPagerTab.setVisibility(View.GONE);
				}
				else
				{
					btnStart.setVisibility(View.GONE);
					viewPagerTab.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_start:
			{
				finish();
				startActivity(new Intent(this, ActivityMain.class));
				break;
			}
		}
	}
}
