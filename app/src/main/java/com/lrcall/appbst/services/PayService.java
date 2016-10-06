/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.models.PayInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.utils.GsonTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付服务类
 * Created by libit on 16/4/6.
 */
public class PayService extends BaseService
{
	public PayService(Context context)
	{
		super(context);
	}

	/**
	 * 获取支付方式列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getPayTypeList(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_PAY_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取支付方式信息
	 *
	 * @param payId                  ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getPayTypeInfo(String payId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("payId", payId);
		ajaxStringCallback(ApiConfig.GET_PAY_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 余额支付订单
	 *
	 * @param orderId                订单ID
	 * @param payPassword            支付密码
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void payOrderByBalance(String orderId, String payPassword, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("orderId", orderId);
		params.put("payPassword", payPassword);
		ajaxStringCallback(ApiConfig.PAY_BY_BALANCE, params, tips, needServiceProcessData);
	}

	/**
	 * 余额支付订单
	 *
	 * @param orderId                订单ID
	 * @param payPassword            支付密码
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void payDataTrafficOrderByBalance(String orderId, String payPassword, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("orderId", orderId);
		params.put("payPassword", payPassword);
		ajaxStringCallback(ApiConfig.PAY_DATA_TRAFFIC_BY_BALANCE, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_PAY_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<PayInfo> payInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<PayInfo>>()
				{
				}.getType());
				if (payInfoList != null)
				{
					//先清空分类
					for (PayInfo payInfo : payInfoList)
					{
					}
				}
			}
		}
		else if (url.endsWith(ApiConfig.GET_PAY_INFO))
		{
			PayInfo payInfo = GsonTools.getReturnObject(result, PayInfo.class);
			if (payInfo != null)
			{
			}
		}
	}
}
