/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.NewsInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.NewsService;
import com.lrcall.db.DbNewsInfoFactory;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;

public class ActivityNews extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityNews.class.getSimpleName();
	private String mNewsId;
	private TextView tvContent, tvLink;
	private NewsService mNewsService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			mNewsId = bundle.getString(ConstValues.DATA_NEWS_ID);
		}
		if (StringTools.isNull(mNewsId))
		{
			finish();
			Toast.makeText(this, "消息不存在！", Toast.LENGTH_LONG).show();
		}
		mNewsService = new NewsService(this);
		mNewsService.addDataResponse(this);
		viewInit();
		NewsInfo newsInfo = DbNewsInfoFactory.getInstance().getNewsInfo(mNewsId);
		if (newsInfo == null || StringTools.isNull(newsInfo.getDescripition()))
		{
			mNewsService.getNewsInfo(mNewsId, "正在获取消息内容...", false);
		}
		else
		{
			initData(newsInfo);
		}
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		setTitle("消息详情");
		tvContent = (TextView) findViewById(R.id.tv_content);
		tvLink = (TextView) findViewById(R.id.tv_link);
		tvLink.setOnClickListener(this);
	}

	private void initData(NewsInfo newsInfo)
	{
		if (newsInfo != null)
		{
			String content = newsInfo.getDescripition();
			LogcatTools.debug(TAG, "content:" + content);
			content = content.replace("\\n", "\r\n");
			tvContent.setText(content);
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.tv_link:
			{
				String url = ApiConfig.getServerNewsUrl(mNewsId);
				ActivityWebView.startWebActivity(this, "消息详情", url);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_NEWS_INFO))
		{
			NewsInfo newsInfo = GsonTools.getReturnObject(result, NewsInfo.class);
			if (newsInfo != null)
			{
				initData(newsInfo);
			}
			else
			{
				showServerMsg(result, null);
			}
			return true;
		}
		return false;
	}
}
