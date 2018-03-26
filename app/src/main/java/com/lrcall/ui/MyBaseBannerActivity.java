/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.lrcall.appbst.models.BannerInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.ui.adapter.SectionsPagerAdapter;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.LogcatTools;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by libit on 16/4/27.
 */
public abstract class MyBaseBannerActivity extends MyBaseActivity
{
	private static final String TAG = MyBaseBannerActivity.class.getSimpleName();
	protected static final int SCROLL_VIEW_PAGE = 111;
	protected static final long SCROLL_TIME = 5;
	protected ViewPager bannerViewPager;
	protected SmartTabLayout bannerViewPagerTab;
	protected SectionsPagerAdapter bannerSectionsPagerAdapter;
	protected final List<Fragment> mFragmentBannerList = new ArrayList<>();
	private ScheduledExecutorService scheduledExecutorService = null;
	private ScheduledFuture scheduledFuture = null;
	protected final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
				case SCROLL_VIEW_PAGE:
				{
					int index = bannerViewPager.getCurrentItem();
					if (index < mFragmentBannerList.size() - 1)
					{
						index++;
					}
					else
					{
						index = 0;
					}
					bannerViewPager.setCurrentItem(index);
					break;
				}
			}
		}
	};

	synchronized protected void updateView()
	{
		if (scheduledExecutorService == null)
		{
			scheduledExecutorService = Executors.newScheduledThreadPool(1);
		}
		else
		{
			cancelScheduledFuture();
		}
		try
		{
			scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new Thread("updateView")
			{
				@Override
				public void run()
				{
					super.run();
					//					LogcatTools.debug(TAG, "ScheduledFuture,时间:" + DateTimeTools.getCurrentTime());
					mHandler.sendEmptyMessage(SCROLL_VIEW_PAGE);
				}
			}, SCROLL_TIME, SCROLL_TIME, TimeUnit.SECONDS);
		}
		catch (RejectedExecutionException e)
		{
			LogcatTools.debug(TAG, "ScheduledFuture->RejectedExecutionException:" + e.getMessage());
		}
	}

	synchronized protected void cancelScheduledFuture()
	{
		if (scheduledFuture != null)
		{
			scheduledFuture.cancel(true);
			scheduledFuture = null;
		}
	}

	synchronized protected void stopScheduledFuture()
	{
		cancelScheduledFuture();
		if (scheduledExecutorService != null)
		{
			scheduledExecutorService.shutdown();
			scheduledExecutorService = null;
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		updateView();
	}

	@Override
	protected void onPause()
	{
		cancelScheduledFuture();
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		stopScheduledFuture();
		super.onDestroy();
	}

	protected void initBannerView()
	{
		bannerViewPager.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE)
				{
					cancelScheduledFuture();
				}
				else
				{
					updateView();
				}
				return false;
			}
		});
	}

	/**
	 * 设置图片的长宽
	 *
	 * @param width  宽度
	 * @param height 高度
	 */
	protected void setBannerWidthAndHeight(int width, int height)
	{
		//设置图片的长宽，这里便于制作图片
		ViewGroup.LayoutParams layoutParams = bannerViewPager.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = height;
		bannerViewPager.setLayoutParams(layoutParams);
	}

	//设置图片适配器
	protected void setViewPagerAdapter(List<BannerInfo> bannerInfoList)
	{
		if (bannerInfoList != null && bannerInfoList.size() > 0)
		{
			mFragmentBannerList.clear();
			for (BannerInfo bannerInfo : bannerInfoList)
			{
				mFragmentBannerList.add(FragmentServerImage.newInstance(ApiConfig.getServerPicUrl(bannerInfo.getPicUrl()), DisplayTools.getWindowWidth(this), bannerInfo.getContent()));
			}
			bannerSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mFragmentBannerList);
			bannerViewPager.setAdapter(bannerSectionsPagerAdapter);
			bannerViewPagerTab.setViewPager(bannerViewPager);
		}
	}
}
