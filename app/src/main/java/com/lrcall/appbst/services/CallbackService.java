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
 * 回拨服务类
 * Created by libit on 16/4/6.
 */
public class CallbackService extends BaseService
{
	public CallbackService(Context context)
	{
		super(context);
	}

	/**
	 * 回拨呼叫
	 *
	 * @param number                 ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void makeCall(String number, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("number", number);
		ajaxStringCallback(ApiConfig.CALLBACK_MAKE_CALL, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.CALLBACK_MAKE_CALL))
		{
		}
	}
}
