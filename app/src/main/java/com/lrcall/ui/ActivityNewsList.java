/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.NewsInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.NewsService;
import com.lrcall.db.DbNewsInfoFactory;
import com.lrcall.enums.NewsStatus;
import com.lrcall.ui.adapter.NewsAdapter;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityNewsList extends MyBaseActivity implements IAjaxDataResponse, XListView.IXListViewListener
{
	private static final String TAG = ActivityNewsList.class.getSimpleName();
	private XListView xListView;
	private NewsService mNewsService;
	private List<NewsInfo> mNewsInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_list);
		mNewsService = new NewsService(this);
		mNewsService.addDataResponse(this);
		viewInit();
		mNewsInfoList = DbNewsInfoFactory.getInstance().getNewsInfoList();
		initData();
		mNewsService.getNewsInfoList(null, true);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		xListView = (XListView) findViewById(R.id.xlist);
	}

	private void initData()
	{
		NewsAdapter newsAdapter = new NewsAdapter(this, mNewsInfoList, new NewsAdapter.INewsAdapterItemClicked()
		{
			@Override
			public void onNewsClicked(View v, NewsInfo newsInfo)
			{
				Intent intent = new Intent(ActivityNewsList.this, ActivityNews.class);
				intent.putExtra(ConstValues.DATA_NEWS_ID, newsInfo.getNewsId());
				startActivity(intent);
				if (newsInfo.getIsRead() == NewsStatus.UNREAD.getStatus())
				{
					v.setBackgroundColor(getResources().getColor(R.color.read_news_bg));
					DbNewsInfoFactory.getInstance().updateNewsInfoStatus(newsInfo.getNewsId(), NewsStatus.READ.getStatus());
				}
			}
		});
		xListView.setAdapter(newsAdapter);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
	}

	@Override
	public void onRefresh()
	{
		mNewsService.getNewsInfoList(null, true);
	}

	@Override
	public void onLoadMore()
	{
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_NEWS_LIST))
		{
			xListView.stopRefresh();
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<NewsInfo> newsInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<NewsInfo>>()
				{
				}.getType());
				if (newsInfoList != null && newsInfoList.size() > 0)
				{
					mNewsInfoList.clear();
					mNewsInfoList = DbNewsInfoFactory.getInstance().getNewsInfoList();
					initData();
				}
			}
			return true;
		}
		return false;
	}
}
