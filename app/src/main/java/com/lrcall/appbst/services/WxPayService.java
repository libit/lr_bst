/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付服务类
 * Created by libit on 16/4/6.
 */
public class WxPayService extends BaseService
{
	public WxPayService(Context context)
	{
		super(context);
	}

	// 获取微信id
	public static String getWeixinAppId()
	{
		return PreferenceUtils.getInstance().getStringValue(PreferenceUtils.WX_APP_ID);
	}

	/**
	 * 微信预付款
	 *
	 * @param orderId                订单号
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void wxPrePay(String orderId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("orderId", orderId);
		ajaxStringCallback(ApiConfig.WX_PRE_PAY, params, tips, needServiceProcessData);
	}

	/**
	 * 微信配置信息
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getWxPayInfo(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_WX_PAY_INFO, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.WX_PRE_PAY))
		{
		}
		else if (url.endsWith(ApiConfig.GET_WX_PAY_INFO))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				String wxAppId = returnInfo.getErrmsg();
				PreferenceUtils.getInstance().setStringValue(PreferenceUtils.WX_APP_ID, wxAppId);
			}
		}
	}
}
