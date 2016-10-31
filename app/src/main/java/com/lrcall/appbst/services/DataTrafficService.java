/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.models.DataTrafficInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.db.DbDataTrafficInfoFactory;
import com.lrcall.utils.GsonTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流量充值服务类
 * Created by libit on 16/4/6.
 */
public class DataTrafficService extends BaseService
{
	public DataTrafficService(Context context)
	{
		super(context);
	}

	/**
	 * 获取流量充值商品列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getDataTrafficInfoList(int start, int size, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.GET_DATA_TRAFFIC_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取流量充值商品列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getNewDataTrafficInfoList(int start, int size, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.GET_NEW_DATA_TRAFFIC_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取流量充值商品信息
	 *
	 * @param productId              ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getDataTrafficInfo(String productId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		ajaxStringCallback(ApiConfig.GET_DATA_TRAFFIC_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 套餐用户每月领取流量卡
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getPackageDataTraffic(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_PACKAGE_DATA_TRAFFIC, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_DATA_TRAFFIC_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<DataTrafficInfo> dataTrafficInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<DataTrafficInfo>>()
				{
				}.getType());
				if (dataTrafficInfoList != null)
				{
					//先清空分类
					DbDataTrafficInfoFactory.getInstance().clearDataTrafficInfo();
					for (DataTrafficInfo dataTrafficInfo : dataTrafficInfoList)
					{
						DbDataTrafficInfoFactory.getInstance().addOrUpdateDataTrafficInfo(dataTrafficInfo);
					}
				}
			}
		}
		else if (url.endsWith(ApiConfig.GET_DATA_TRAFFIC_INFO))
		{
			DataTrafficInfo dataTrafficInfo = GsonTools.getReturnObject(result, DataTrafficInfo.class);
			if (dataTrafficInfo != null)
			{
				DbDataTrafficInfoFactory.getInstance().addOrUpdateDataTrafficInfo(dataTrafficInfo);
			}
		}
	}
}
