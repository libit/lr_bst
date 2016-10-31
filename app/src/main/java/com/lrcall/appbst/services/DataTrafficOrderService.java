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
 * 流量充值订单服务类
 * Created by libit on 16/4/6.
 */
public class DataTrafficOrderService extends BaseService
{
	public DataTrafficOrderService(Context context)
	{
		super(context);
	}

	/**
	 * 获取订单列表
	 *
	 * @param status                 订单状态
	 * @param start                  记录开始
	 * @param size                   记录条数
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getOrderInfoList(byte status, int start, int size, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("status", status);
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.GET_DATA_TRAFFIC_ORDER_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取订单列表
	 *
	 * @param start                  记录开始
	 * @param size                   记录条数
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getOrderInfoList(int start, int size, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.GET_DATA_TRAFFIC_ORDER_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 添加订单信息
	 *
	 * @param comment                留言信息
	 * @param remark                 备注
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void addOrder(String productId, int count, String number, String comment, String remark, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		params.put("count", count);
		params.put("number", number);
		params.put("comment", comment);
		params.put("remark", remark);
		ajaxStringCallback(ApiConfig.ADD_DATA_TRAFFIC_ORDER, params, tips, needServiceProcessData);
	}

	/**
	 * 添加订单信息
	 *
	 * @param number                 手机号码
	 * @param cardId                 卡号
	 * @param cardPwd                密码
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void addOrderByCard(String number, String cardId, String cardPwd, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("number", number);
		params.put("cardId", cardId);
		params.put("cardPwd", cardPwd);
		ajaxStringCallback(ApiConfig.DATA_TRAFFIC_RECHARGE_BY_CARD, params, tips, needServiceProcessData);
	}

	/**
	 * 删除订单信息
	 *
	 * @param orderId                订单ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void deleteOrder(String orderId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("orderId", orderId);
		ajaxStringCallback(ApiConfig.DELETE_DATA_TRAFFIC_ORDER, params, tips, needServiceProcessData);
	}

	/**
	 * 获取订单信息
	 *
	 * @param orderId                订单ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getOrderInfo(String orderId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("orderId", orderId);
		ajaxStringCallback(ApiConfig.GET_DATA_TRAFFIC_ORDER_INFO, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_DATA_TRAFFIC_ORDER_LIST))
		{
		}
		else if (url.endsWith(ApiConfig.ADD_DATA_TRAFFIC_ORDER))
		{
		}
	}
}
