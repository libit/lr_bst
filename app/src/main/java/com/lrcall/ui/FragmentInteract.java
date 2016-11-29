/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.BannerInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.BannerService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UserService;
import com.lrcall.db.DbBannerInfoFactory;
import com.lrcall.enums.ClientBannerType;
import com.lrcall.ui.adapter.SectionsPagerAdapter;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FragmentInteract extends MyBaseFragment implements View.OnClickListener, IAjaxDataResponse, XListView.IXListViewListener
{
	private static final String TAG = FragmentInteract.class.getSimpleName();
	private static final int SCROLL_VIEW_PAGE = 111;
	private static final long SCROLL_TIME = 5;
	private static final int RECOMMEND_COUNT = 4;
	private View headView;
	private ViewPager viewPager;
	private ScheduledExecutorService scheduledExecutorService = null;
	private ScheduledFuture scheduledFuture = null;
	private final List<Fragment> mFragmentRecommendList = new ArrayList<>();
	private SmartTabLayout viewPagerTab;
	private SectionsPagerAdapter sectionsPagerAdapter;
	protected XListView xListView;
	private BannerService mBannerService;
	private UserService mUserService;
	private final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
				case SCROLL_VIEW_PAGE:
				{
					int index = viewPager.getCurrentItem();
					if (index < mFragmentRecommendList.size() - 1)
					{
						index++;
					}
					else
					{
						index = 0;
					}
					viewPager.setCurrentItem(index);
					break;
				}
			}
		}
	};

	synchronized private void updateView()
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

					mHandler.sendEmptyMessage(SCROLL_VIEW_PAGE);
				}
			}, SCROLL_TIME, SCROLL_TIME, TimeUnit.SECONDS);
		}
		catch (RejectedExecutionException e)
		{
			LogcatTools.debug(TAG, "ScheduledFuture->RejectedExecutionException:" + e.getMessage());
		}
	}

	synchronized private void cancelScheduledFuture()
	{
		if (scheduledFuture != null)
		{
			scheduledFuture.cancel(true);
			scheduledFuture = null;
		}
	}

	synchronized private void stopScheduledFuture()
	{
		cancelScheduledFuture();
		if (scheduledExecutorService != null)
		{
			scheduledExecutorService.shutdown();
			scheduledExecutorService = null;
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mBannerService = new BannerService(this.getContext());
		mBannerService.addDataResponse(this);
		mUserService = new UserService(this.getContext());
		mUserService.addDataResponse(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_interact, container, false);
		viewInit(rootView);
		updateView();
		setViewPagerAdapter(DbBannerInfoFactory.getInstance().getBannerInfoList(ClientBannerType.PAGE_INDEX.getType()));
		onRefresh();
		return rootView;
	}

	@Override
	protected void viewInit(View rootView)
	{
		rootView.findViewById(R.id.btn_back).setVisibility(View.GONE);
		rootView.findViewById(R.id.btn_more).setVisibility(View.GONE);
		TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
		tvTitle.setText("互动");
		xListView = (XListView) rootView.findViewById(R.id.xlist);
		headView = LayoutInflater.from(this.getContext()).inflate(R.layout.fragment_interact_head, null);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.addHeaderView(headView);
		xListView.setAdapter(null);
		xListView.setXListViewListener(this);
		viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
		//设置图片的长宽，这里便于制作图片
		ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
		layoutParams.width = DisplayTools.getWindowWidth(this.getContext());
		layoutParams.height = DisplayTools.getWindowWidth(this.getContext()) * 1 / 2;
		viewPager.setLayoutParams(layoutParams);
		viewPagerTab = (SmartTabLayout) rootView.findViewById(R.id.viewpagertab);
		viewPager.setOnTouchListener(new View.OnTouchListener()
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
		rootView.findViewById(R.id.layout_sign).setOnClickListener(this);
		rootView.findViewById(R.id.layout_share).setOnClickListener(this);
		rootView.findViewById(R.id.layout_slyder).setOnClickListener(this);
		rootView.findViewById(R.id.layout_notice).setOnClickListener(this);
		rootView.findViewById(R.id.layout_brand).setOnClickListener(this);
		rootView.findViewById(R.id.layout_free).setOnClickListener(this);
		rootView.findViewById(R.id.layout_flow).setOnClickListener(this);
		super.viewInit(rootView);
	}

	//设置图片适配器
	private void setViewPagerAdapter(List<BannerInfo> bannerInfoList)
	{
		if (bannerInfoList != null && bannerInfoList.size() > 0)
		{
			mFragmentRecommendList.clear();
			for (BannerInfo bannerInfo : bannerInfoList)
			{
				mFragmentRecommendList.add(FragmentServerImage.newInstance(ApiConfig.getServerPicUrl(bannerInfo.getPicUrl()), DisplayTools.getWindowWidth(this.getContext()), bannerInfo.getContent()));
			}
			sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), mFragmentRecommendList);
			viewPager.setAdapter(sectionsPagerAdapter);
			viewPagerTab.setViewPager(viewPager);
		}
	}

	//刷新数据
	synchronized public void refreshData()
	{
		mBannerService.getBannerInfoList(ClientBannerType.PAGE_INDEX.getType(), 0, RECOMMEND_COUNT, null, true);
	}

	@Override
	public void onRefresh()
	{
		refreshData();
	}

	@Override
	public void onLoadMore()
	{
	}

	@Override
	public void fragmentShow()
	{
		super.fragmentShow();
		updateView();
	}

	@Override
	public void fragmentHide()
	{
		super.fragmentHide();
		cancelScheduledFuture();
	}

	@Override
	public void onDestroyView()
	{
		stopScheduledFuture();
		super.onDestroyView();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.layout_sign:
			{
				mUserService.userSignToday("正在签到，请稍后...", true);
				break;
			}
			case R.id.layout_share:
			{
				mUserService.share("请稍后...", true);
				break;
			}
			case R.id.layout_slyder:
			{
				break;
			}
			case R.id.layout_notice:
			{
				Intent intent = new Intent(this.getContext(), ActivityNewsList.class);
				startActivity(intent);
				break;
			}
			case R.id.layout_brand:
			{
				break;
			}
			case R.id.layout_free:
			{
				break;
			}
			case R.id.layout_flow:
			{
				if (UserService.isLogin())
				{
					startActivity(new Intent(this.getContext(), ActivityRechargeDataTraffic.class));
				}
				else
				{
					startActivity(new Intent(this.getContext(), ActivityLogin.class));
				}
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_BANNER_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<BannerInfo> bannerInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<BannerInfo>>()
				{
				}.getType());
				setViewPagerAdapter(bannerInfoList);
			}
		}
		else if (url.endsWith(ApiConfig.USER_SIGN_TODAY))
		{
			showServerMsg(result, null);
		}
		return false;
	}
}
