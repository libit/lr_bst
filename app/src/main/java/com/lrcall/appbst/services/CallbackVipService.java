/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2017.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 回拨VIP服务类
 * Created by libit on 16/4/6.
 */
public class CallbackVipService extends BaseService
{
	public CallbackVipService(Context context)
	{
		super(context);
	}

	/**
	 * 获取列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getCallbackVipPriceInfoList(int start, int size, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.GET_CALLBACK_VIP_PRICE_INFO_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取地址信息
	 *
	 * @param priceId                ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getCallbackVipPriceInfo(String priceId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("priceId", priceId);
		ajaxStringCallback(ApiConfig.GET_CALLBACK_VIP_PRICE_INFO, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_CALLBACK_VIP_PRICE_INFO_LIST))
		{
		}
		else if (url.endsWith(ApiConfig.GET_CALLBACK_VIP_PRICE_INFO))
		{
		}
	}
}
