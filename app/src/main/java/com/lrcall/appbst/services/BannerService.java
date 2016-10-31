/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.models.BannerInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.db.DbBannerInfoFactory;
import com.lrcall.utils.GsonTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页Banner服务类
 * Created by libit on 16/4/6.
 */
public class BannerService extends BaseService
{
	public BannerService(Context context)
	{
		super(context);
	}

	/**
	 * 获取首页Banner列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getBannerInfoList(int start, int size, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.GET_BANNER_LIST, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_BANNER_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<BannerInfo> bannerInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<BannerInfo>>()
				{
				}.getType());
				if (bannerInfoList != null && bannerInfoList.size() > 0)
				{
					//先清空
					DbBannerInfoFactory.getInstance().clearBannerInfo();
					for (BannerInfo bannerInfo : bannerInfoList)
					{
						DbBannerInfoFactory.getInstance().addOrUpdateBannerInfo(bannerInfo);
					}
				}
			}
		}
	}
}
