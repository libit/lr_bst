/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.utils.DisplayTools;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * 自定义ActionBar基类
 * Created by libit on 15/8/30.
 */
public class MyCustomerToolBarActivity extends SwipeBackActivity
{
	protected Toolbar mToolbar;
	protected View mBtnBack, mBtnMore;
	protected TextView tvTitle;
	protected View mMenuView;
	protected PopupWindow mPopupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	/**
	 * 设置标题
	 *
	 * @param title
	 */
	@Override
	public void setTitle(CharSequence title)
	{
		if (tvTitle != null)
		{
			tvTitle.setText(title);
		}
		else
		{
			super.setTitle(title);
		}
	}

	//初始化视图
	protected void viewInit()
	{
		mToolbar = (Toolbar) findViewById(R.id.toolbar_title);
		mBtnBack = findViewById(R.id.btn_back);
		mBtnMore = findViewById(R.id.btn_more);
		mBtnBack.setVisibility(View.GONE);
		mBtnMore.setVisibility(View.GONE);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		if (mToolbar != null)
		{
			setTitle(getTitle());
			setSupportActionBar(mToolbar);
		}
		//设置滑动返回区域
		getSwipeBackLayout().setEdgeSize(DisplayTools.getWindowWidth(this) / 4);
	}

	//后退按钮
	protected void setBackButton()
	{
		mBtnBack.setVisibility(View.VISIBLE);
		mBtnBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}

	//更多菜单按钮
	protected View setMoreButton(final int layoutResId)
	{
		mBtnMore.setVisibility(View.VISIBLE);
		mMenuView = getLayoutInflater().inflate(layoutResId, null);
		mMenuView.setPadding(0, 0, 0, 0);//不设置的话右边会空出一些
		mBtnMore.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mPopupWindow = new PopupWindow(mMenuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
				mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				//得到更多按钮在屏幕上的坐标
				int[] location = new int[2];
				v.getLocationOnScreen(location);
				mPopupWindow.showAtLocation(mToolbar, Gravity.RIGHT | Gravity.TOP, 5, location[1] + v.getHeight());
			}
		});
		return mMenuView;
	}
}
