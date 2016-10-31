/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.models.ProductPricePointInfo;
import com.lrcall.utils.GsonTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品积分抵扣服务类
 * Created by libit on 16/4/6.
 */
public class ProductPricePointService extends BaseService
{
	public ProductPricePointService(Context context)
	{
		super(context);
	}

	/**
	 * 获取商品积分抵扣信息
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductPricePointInfo(String productId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		ajaxStringCallback(ApiConfig.GET_PRICE_POINT_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 批量获取商品积分抵扣信息
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductPricePointInfoList(String productIds, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productIds", productIds);
		ajaxStringCallback(ApiConfig.GET_PRICE_POINT_INFO_LIST, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_PRICE_POINT_INFO_LIST))
		{
			List<ProductPricePointInfo> productPricePointInfoList = GsonTools.getObjects(result, new TypeToken<List<ProductPricePointInfo>>()
			{
			}.getType());
			if (productPricePointInfoList != null)
			{
			}
		}
		else if (url.endsWith(ApiConfig.GET_PRICE_POINT_INFO))
		{
			ProductPricePointInfo productPricePointInfo = GsonTools.getReturnObject(result, ProductPricePointInfo.class);
			if (productPricePointInfo != null)
			{
			}
		}
	}
}
