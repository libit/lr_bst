/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lrcall.appbst.R;
import com.lrcall.ui.adapter.SectionsPagerAdapter;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.ConstValues;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

public class ActivityPicView extends MyBaseActivity
{
	private ViewPager viewPager;
	private final List<Fragment> fragmentPicList = new ArrayList<>();
	private ArrayList<String> mPicUrlList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic_view);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			mPicUrlList = bundle.getStringArrayList(ConstValues.DATA_PRODUCT_SHOW_PIC_URLS);
			if (mPicUrlList == null || mPicUrlList.size() < 1)
			{
				finish();
			}
		}
		else
		{
			finish();
		}
		viewInit();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		for (String url : mPicUrlList)
		{
			fragmentPicList.add(FragmentServerImage.newInstance(url, DisplayTools.getWindowWidth(this), ""));
		}
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
		SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragmentPicList);
		viewPager.setAdapter(adapter);
		viewPagerTab.setViewPager(viewPager);
		viewPager.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
}
