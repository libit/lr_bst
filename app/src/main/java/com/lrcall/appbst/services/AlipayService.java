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
 * 支付宝服务类
 * Created by libit on 16/4/6.
 */
public class AlipayService extends BaseService
{


	public AlipayService(Context context)
	{
		super(context);
	}

	/**
	 * 获取支付宝配置信息
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getAlipayConfigInfo(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_ALIPAY_CONIFG, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_ALIPAY_CONIFG))
		{
//			AlipayConfigInfo alipayConfigInfo = GsonTools.getReturnObject(result, AlipayConfigInfo.class);
//			if (alipayConfigInfo != null)
//			{
//			}
		}
	}
}
