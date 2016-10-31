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
 * 商品评价服务类
 * Created by libit on 16/4/6.
 */
public class OrderProductCommentService extends BaseService
{
	public OrderProductCommentService(Context context)
	{
		super(context);
	}

	/**
	 * 增加商品评价
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void addProductCommentInfo(String orderId, String productId, String comment, Byte type, int level, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("orderId", orderId);
		params.put("productId", productId);
		params.put("content", comment);
		params.put("commentType", type);
		params.put("commentLevel", level);
		ajaxStringCallback(ApiConfig.ADD_PRODUCT_COMMENT, params, tips, needServiceProcessData);
	}

	/**
	 * 获取订单商品的评论条数，如果大于0则说明一评论，否则就是未评论
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductCommentInfoCount(String orderId, String productId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("orderId", orderId);
		params.put("productId", productId);
		ajaxStringCallback(ApiConfig.GET_PRODUCT_COMMENT_INFO_COUNT, params, tips, needServiceProcessData);
	}

	/**
	 * 获取订单商品的评论列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductCommentInfoList(String productId, Byte type, int start, int size, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		params.put("type", type);
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.GET_PRODUCT_COMMENT_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取订单商品的评论条数
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductCommentInfoListCount(String productId, Byte type, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		params.put("type", type);
		ajaxStringCallback(ApiConfig.GET_PRODUCT_COMMENT_COUNT, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_PRODUCT_COMMENT_LIST))
		{
		}
	}
}
