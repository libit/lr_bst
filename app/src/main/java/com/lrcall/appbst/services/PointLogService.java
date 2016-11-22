/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 积分日志服务类
 * Created by libit on 16/4/6.
 */
public class PointLogService extends BaseService
{
	public PointLogService(Context context)
	{
		super(context);
	}

	/**
	 * 获取积分订单列表
	 *
	 * @param start                  记录开始
	 * @param size                   记录条数
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getPointLogInfoList(int start, int size, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.GET_USER_POINT_LOG_LIST, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_USER_POINT_LOG_LIST))
		{
			//			TableData tableData = GsonTools.getObject(result, TableData.class);
			//			if (tableData != null)
			//			{
			//				List<OrderInfo> orderInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<OrderInfo>>()
			//				{
			//				}.getType());
			//				if (orderInfoList != null)
			//				{
			//					for (OrderInfo orderInfo : orderInfoList)
			//					{
			//					}
			//				}
			//			}
		}
	}
}
