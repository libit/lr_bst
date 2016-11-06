/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.TableOrderInfo;
import com.lrcall.appbst.models.TableSearchInfo;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家订单服务类
 * Created by libit on 16/4/6.
 */
public class ShopOrderService extends BaseService
{
	public ShopOrderService(Context context)
	{
		super(context);
	}

	/**
	 * 获取订单列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getOrderList(String condition, int start, int size, List<TableOrderInfo> orderInfos, List<TableSearchInfo> searchInfos, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		if (!StringTools.isNull(condition))
		{
			params.put("search[value]", condition);
		}
		ajaxStringCallback(ApiConfig.GET_SHOP_ORDER_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取订单信息
	 *
	 * @param orderSubId             订单ID
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void getOrderInfo(String orderSubId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("orderSubId", orderSubId);
		ajaxStringCallback(ApiConfig.GET_SHOP_ORDER_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 获取订单的快递信息
	 *
	 * @param orderSubId             订单ID
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void getOrderExpressInfo(String orderSubId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("orderSubId", orderSubId);
		ajaxStringCallback(ApiConfig.GET_SHOP_ORDER_EXPRESS_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 订单发货
	 *
	 * @param orderSubId             订单ID
	 * @param expressName            快递名
	 * @param expressId              快递单号
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void orderShip(String orderSubId, String expressName, String expressId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("orderSubId", orderSubId);
		params.put("expressName", expressName);
		params.put("expressId", expressId);
		ajaxStringCallback(ApiConfig.SHOP_ORDER_SHIP, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_SHOP_PRODUCT_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<ProductInfo> productInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductInfo>>()
				{
				}.getType());
				if (productInfoList != null && productInfoList.size() > 0)
				{
					//先清空
					//					DbProductInfoFactory.getInstance().clearProductInfo();
					for (ProductInfo productInfo : productInfoList)
					{
						//						DbProductInfoFactory.getInstance().addOrUpdateProductInfo(productInfo);
					}
				}
			}
			else
			{
			}
		}
	}
}
