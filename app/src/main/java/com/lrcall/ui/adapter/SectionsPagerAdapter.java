/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by libit on 16/7/11.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter
{
	private final List<Fragment> fragmentList;

	public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragmentList)
	{
		super(fm);
		this.fragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int position)
	{
		if (position < fragmentList.size())
		{
			return fragmentList.get(position);
		}
		else
		{
			return null;
		}
	}

	@Override
	public int getCount()
	{
		return fragmentList.size();
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return "";
	}
}
