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
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.NewsService;
import com.lrcall.db.DbNewsInfoFactory;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;

public class ActivityNews extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityNews.class.getSimpleName();
	private String newsId;
	private TextView tvContent, tvLink;
	private NewsInfo newsInfo;
	private NewsService mNewsService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			newsId = bundle.getString(ConstValues.DATA_NEWS_ID);
		}
		if (StringTools.isNull(newsId))
		{
			finish();
			Toast.makeText(this, "消息不存在！", Toast.LENGTH_LONG).show();
		}
		mNewsService = new NewsService(this);
		mNewsService.addDataResponse(this);
		viewInit();
		newsInfo = DbNewsInfoFactory.getInstance().getNewsInfo(newsId);
		if (newsInfo == null || StringTools.isNull(newsInfo.getDescripition()))
		{
			mNewsService.getNewsInfo(newsId, "正在获取消息内容...", false);
		}
		else
		{
			initData();
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

	private void initData()
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
				String url = ApiConfig.getServerNewsUrl(newsId);
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
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				newsInfo = GsonTools.getReturnObject(result, NewsInfo.class);
				initData();
			}
			else
			{
				if (returnInfo != null)
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "获取消息失败：" + returnInfo.getErrmsg());
				}
				else
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "获取消息失败：" + result);
				}
			}
			return true;
		}
		return false;
	}
}
