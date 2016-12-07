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
 * 客户端服务类，用于获取与客户端有关配置等
 * Created by libit on 16/4/6.
 */
public class ClientService extends BaseService
{
	public ClientService(Context context)
	{
		super(context);
	}

	/**
	 * 获取客户端首页功能列表
	 *
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void getIndexFuncList(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_INDEX_FUNC_LIST, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_INDEX_FUNC_LIST))
		{
		}
	}
}
