/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.models.NewsInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.TableOrderInfo;
import com.lrcall.appbst.models.TableSearchInfo;
import com.lrcall.db.DbNewsInfoFactory;
import com.lrcall.ui.ActivityWebView;
import com.lrcall.ui.dialog.DialogCommon;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息服务类
 * Created by libit on 16/4/6.
 */
public class NewsService extends BaseService
{
	public NewsService(Context context)
	{
		super(context);
	}

	/**
	 * 获取消息列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getNewsInfoList(int start, int size, List<TableOrderInfo> orderInfos, List<TableSearchInfo> searchInfos, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		params.put("order[0][column]", "5");
		params.put("columns[5][data]", "update_date_long");
		params.put("order[0][dir]", "desc");
		ajaxStringCallback(ApiConfig.GET_NEWS_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取消息信息
	 *
	 * @param newsId                 消息ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getNewsInfo(String newsId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("newsId", newsId);
		ajaxStringCallback(ApiConfig.GET_NEWS_INFO, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_NEWS_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<NewsInfo> newsInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<NewsInfo>>()
				{
				}.getType());
				if (newsInfoList != null && newsInfoList.size() > 0)
				{
					//先清空
					//					DbNewsInfoFactory.getInstance().clearNewsInfo();
					for (NewsInfo newsInfo : newsInfoList)
					{
						LogcatTools.debug("newsInfo", "newsInfo:" + newsInfo.getNewsId());
						if (DbNewsInfoFactory.getInstance().getNewsInfo(newsInfo.getNewsId()) == null)
						{
							LogcatTools.debug("newsInfo", "新消息->newsInfo:" + newsInfo.getNewsId());
							final String newsId = newsInfo.getNewsId();
							DialogCommon dialogCommon = new DialogCommon(context, new DialogCommon.LibitDialogListener()
							{
								@Override
								public void onOkClick()
								{
									String url = ApiConfig.getServerNewsUrl(newsId);
									ActivityWebView.startWebActivity(context, "消息详情", url);
								}

								@Override
								public void onCancelClick()
								{
								}
							}, newsInfo.getTitle(), newsInfo.getDescripition(), true, false, true);
							dialogCommon.show();
							dialogCommon.setOKString("查看");
							dialogCommon.setCancelString("关闭");
						}
						DbNewsInfoFactory.getInstance().addOrUpdateNewsInfo(newsInfo);
					}
				}
			}
		}
		else if (url.endsWith(ApiConfig.GET_NEWS_INFO))
		{
		}
	}
}
