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
 * 订单服务类
 * Created by libit on 16/4/6.
 */
public class OrderService extends BaseService
{
	public OrderService(Context context)
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
		ajaxStringCallback(ApiConfig.GET_ORDER_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 添加订单信息
	 *
	 * @param comment                留言信息
	 * @param remark                 备注
	 * @param productsJson           商品ID和数量
	 * @param expressJson            物流信息（地址ID和快递名、费用）
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void addOrder(String comment, String remark, String productsJson, String expressJson, int pointAmount, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("comment", comment);
		params.put("remark", remark);
		params.put("productsJson", productsJson);
		params.put("expressJson", expressJson);
		params.put("pointAmount", pointAmount);
		ajaxStringCallback(ApiConfig.ADD_ORDER, params, tips, needServiceProcessData);
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
		ajaxStringCallback(ApiConfig.DELETE_ORDER, params, tips, needServiceProcessData);
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
		ajaxStringCallback(ApiConfig.GET_ORDER_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 订单已完成
	 *
	 * @param orderId                订单ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void orderFinish(String orderId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("orderId", orderId);
		ajaxStringCallback(ApiConfig.ORDER_FINISH, params, tips, needServiceProcessData);
	}

	/**
	 * 获取订单的快递信息
	 *
	 * @param orderId                订单ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getOrderExpressList(String orderId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("orderId", orderId);
		ajaxStringCallback(ApiConfig.GET_ORDER_EXPRESS_LIST, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_ORDER_LIST))
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
		else if (url.endsWith(ApiConfig.ADD_ORDER))
		{
		}
	}
}
