/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.models.ProvinceInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.utils.GsonTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地址区域服务类
 * Created by libit on 16/4/6.
 */
public class AreaService extends BaseService
{
	public AreaService(Context context)
	{
		super(context);
	}

	/**
	 * 获取省列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData 是否需要服务类处理
	 */
	public void getProvinceList(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_PROVINCE_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取市列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData 是否需要服务类处理
	 */
	public void getCityList(String provinceId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("provinceId", provinceId);
		ajaxStringCallback(ApiConfig.GET_CITY_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取区列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData 是否需要服务类处理
	 */
	public void getCountryList(String cityId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("cityId", cityId);
		ajaxStringCallback(ApiConfig.GET_COUNTRY_LIST, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_PROVINCE_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<ProvinceInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProvinceInfo>>()
				{
				}.getType());
				if (list != null)
				{
					//先清空分类
					for (ProvinceInfo provinceInfo : list)
					{
					}
				}
			}
		}
	}
}
