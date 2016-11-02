/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.models.TableOrderInfo;
import com.lrcall.appbst.models.TableSearchInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分商品服务类
 * Created by libit on 16/4/6.
 */
public class PointProductService extends BaseService
{
	public PointProductService(Context context)
	{
		super(context);
	}

	/**
	 * 获取所有积分商品列表
	 *
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void getPointProductList(String condition, int start, int size, List<TableOrderInfo> orderInfos, List<TableSearchInfo> searchInfos, boolean advancedSearch, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		params.put("search[value]", condition);
		ajaxStringCallback(ApiConfig.GET_POINT_PRODUCT_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取积分商品信息
	 *
	 * @param productId              商品ID
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void getPointProduct(String productId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		ajaxStringCallback(ApiConfig.GET_POINT_PRODUCT, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_POINT_PRODUCT_LIST))
		{
		}
	}
}
