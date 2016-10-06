/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.models.ProductStarInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.db.DbProductStarInfoFactory;
import com.lrcall.utils.GsonTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收藏服务类
 * Created by libit on 16/4/6.
 */
public class ProductStarService extends BaseService
{
	public ProductStarService(Context context)
	{
		super(context);
	}

	/**
	 * 获取收藏列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductStarInfoList(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("order[0][column]", "4");
		params.put("columns[4][data]", "add_date_long");
		params.put("order[0][dir]", "desc");
		ajaxStringCallback(ApiConfig.GET_STAR_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取收藏列表数量
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductStarInfoListCount(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_STAR_LIST_COUNT, params, tips, needServiceProcessData);
	}

	/**
	 * 获取收藏信息
	 *
	 * @param productId              商品ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductStarInfo(String productId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		ajaxStringCallback(ApiConfig.GET_STAR_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 增加收藏信息
	 *
	 * @param productId              商品ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void addProductStarInfo(String productId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		ajaxStringCallback(ApiConfig.ADD_STAR_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 删除收藏信息
	 *
	 * @param productId              商品ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void deleteProductStarInfo(String productId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		ajaxStringCallback(ApiConfig.DELETE_STAR_INFO, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_STAR_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<ProductStarInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductStarInfo>>()
				{
				}.getType());
				if (list != null)
				{
					//先清空分类
					DbProductStarInfoFactory.getInstance().clearProductStarInfo();
					for (ProductStarInfo productStarInfo : list)
					{
						DbProductStarInfoFactory.getInstance().addOrUpdateProductStarInfo(productStarInfo);
					}
				}
			}
		}
		else if (url.endsWith(ApiConfig.ADD_STAR_INFO))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				ProductStarInfo productStarInfo = GsonTools.getReturnObject(result, ProductStarInfo.class);
				if (productStarInfo != null)
				{
					DbProductStarInfoFactory.getInstance().addOrUpdateProductStarInfo(productStarInfo);
				}
			}
		}
		else if (url.endsWith(ApiConfig.DELETE_STAR_INFO))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				String starId = returnInfo.getErrmsg();
				DbProductStarInfoFactory.getInstance().deleteProductStarInfo(starId);
			}
		}
	}
}
