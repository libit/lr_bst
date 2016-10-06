/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lrcall.appbst.R;
import com.lrcall.ui.customer.DisplayTools;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * 自定义ActionBar基类
 * Created by libit on 15/8/30.
 */
public abstract class MyBaseActivity extends SwipeBackActivity
{
	protected Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//		setOverflowShowingAlways();
	}

	/**
	 * 设置标题
	 *
	 * @param title
	 */
	@Override
	public void setTitle(CharSequence title)
	{
		if (mToolbar != null)
		{
			mToolbar.setTitle(title);
		}
		else
		{
			super.setTitle(title);
		}
	}

	protected void viewInit()
	{
		mToolbar = (Toolbar) findViewById(R.id.toolbar_title);
		if (mToolbar != null)
		{
			mToolbar.setTitle(getTitle().toString());// 标题的文字需在setSupportActionBar之前，不然会无效
			// mToolbar.setSubtitle("副标题");
			setSupportActionBar(mToolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		//		setBackButton();
		//设置滑动返回区域
		getSwipeBackLayout().setEdgeSize(DisplayTools.getWindowWidth(this) / 2);
	}

	//后退按钮
	protected void setBackButton()
	{
		//		mToolbar.setNavigationIcon(R.drawable.back);
		if (mToolbar != null)
		{
			mToolbar.setNavigationOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					finish();
				}
			});
		}
	}
}
