/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.models.ProductHistoryInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.TableOrderInfo;
import com.lrcall.appbst.models.TableSearchInfo;
import com.lrcall.events.ProductEvent;
import com.lrcall.utils.GsonTools;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品浏览记录服务类
 * Created by libit on 16/4/6.
 */
public class ProductHistoryService extends BaseService
{
	public ProductHistoryService(Context context)
	{
		super(context);
	}

	/**
	 * 获取商品浏览记录列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductHistoryInfoList(String condition, int start, int size, List<TableOrderInfo> orderInfos, List<TableSearchInfo> searchInfos, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		params.put("search[value]", condition);
		params.put("order[0][column]", "4");
		params.put("columns[4][data]", "update_date_long");
		params.put("order[0][dir]", "desc");
		ajaxStringCallback(ApiConfig.GET_PRODUCT_HISTORY_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取商品浏览记录数量
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductHistoryInfoListCount(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_PRODUCT_HISTORY_LIST_COUNT, params, tips, needServiceProcessData);
	}

	/**
	 * 获取商品浏览记录
	 *
	 * @param productId              商品ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductHistoryInfo(String productId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		ajaxStringCallback(ApiConfig.GET_PRODUCT_HISTORY_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 增加商品浏览记录
	 *
	 * @param productId              商品ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void addProductHistoryInfo(String productId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		ajaxStringCallback(ApiConfig.ADD_PRODUCT_HISTORY_INFO, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_PRODUCT_HISTORY_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<ProductHistoryInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductHistoryInfo>>()
				{
				}.getType());
				if (list != null)
				{
					//先清空分类
					//					DbProductStarInfoFactory.getInstance().clearProductStarInfo();
					for (ProductHistoryInfo productHistoryInfo : list)
					{
						//						DbProductStarInfoFactory.getInstance().addOrUpdateProductStarInfo(productStarInfo);
					}
				}
			}
		}
		else if (url.endsWith(ApiConfig.ADD_PRODUCT_HISTORY_INFO))
		{
			ProductHistoryInfo productHistoryInfo = GsonTools.getReturnObject(result, ProductHistoryInfo.class);
			if (productHistoryInfo != null)
			{
				//				DbProductStarInfoFactory.getInstance().addOrUpdateProductStarInfo(productStarInfo);
				EventBus.getDefault().post(new ProductEvent(ProductEvent.EVENT_HISTORY_ADD));
			}
		}
	}
}
