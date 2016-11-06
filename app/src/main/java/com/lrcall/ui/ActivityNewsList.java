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

public class ActivityNewsList extends MyBasePageActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityNewsList.class.getSimpleName();
	private View layoutNewsList, layoutNoNews;
	private NewsService mNewsService;
	private List<NewsInfo> mNewsInfoList = new ArrayList<>();
	private NewsAdapter mNewsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_list);
		mNewsService = new NewsService(this);
		mNewsService.addDataResponse(this);
		viewInit();
		//		List<NewsInfo> newsInfoList = DbNewsInfoFactory.getInstance().getNewsInfoList();
		//		refreshNewsList(newsInfoList);
		onRefresh();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		layoutNewsList = findViewById(R.id.layout_news_list);
		layoutNoNews = findViewById(R.id.layout_no_news);
		xListView = (XListView) findViewById(R.id.xlist);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
	}

	@Override
	public void refreshData()
	{
		mNewsInfoList.clear();
		mNewsAdapter = null;
		loadMoreData();
	}

	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		mNewsService.getNewsInfoList(mDataStart, getPageSize(), null, null, tips, true);
	}

	private void refreshNewsList(List<NewsInfo> newsInfoList)
	{
		if (newsInfoList == null || newsInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mNewsInfoList.size() < 1)
			{
				layoutNewsList.setVisibility(View.GONE);
				layoutNoNews.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutNewsList.setVisibility(View.VISIBLE);
		layoutNoNews.setVisibility(View.GONE);
		if (newsInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (NewsInfo newsInfo : newsInfoList)
		{
			mNewsInfoList.add(newsInfo);
		}
		if (mNewsAdapter == null)
		{
			mNewsAdapter = new NewsAdapter(this, mNewsInfoList, new NewsAdapter.IItemClick()
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
			xListView.setAdapter(mNewsAdapter);
		}
		else
		{
			mNewsAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_NEWS_LIST))
		{
			List<NewsInfo> newsInfoList = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				newsInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<NewsInfo>>()
				{
				}.getType());
			}
			refreshNewsList(newsInfoList);
			return true;
		}
		return false;
	}
}
