/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.TableOrderInfo;
import com.lrcall.appbst.models.TableSearchInfo;
import com.lrcall.appbst.models.UserBalanceLogInfo;
import com.lrcall.utils.GsonTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户余额变动日志服务类
 * Created by libit on 16/4/6.
 */
public class UserBalanceLogService extends BaseService
{
	public UserBalanceLogService(Context context)
	{
		super(context);
	}

	/**
	 * 获取用户余额变动日志列表
	 *
	 * @param tips               提示信息
	 * @param needServiceProcess
	 */
	public void getUserBalanceLogList(int start, int size, List<TableOrderInfo> orderInfos, List<TableSearchInfo> searchInfos, String tips, final boolean needServiceProcess)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		if (searchInfos != null && searchInfos.size() > 0)
		{
			params.put("search[value]", searchInfos.get(0).getSearchValue());
		}
		ajaxStringCallback(ApiConfig.GET_USER_BALANCE_LOG_LIST, params, tips, needServiceProcess);
	}

	/**
	 * 获取用户分润列表
	 *
	 * @param tips               提示信息
	 * @param needServiceProcess
	 */
	public void getUserShareProfitList(int start, int size, String tips, final boolean needServiceProcess)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.GET_USER_SHARE_PROFIT_LIST, params, tips, needServiceProcess);
	}

	/**
	 * 获取代理商用户分润列表
	 *
	 * @param start
	 * @param size
	 * @param tips
	 * @param needServiceProcess
	 */
	public void getUserAgentShareProfitList(int start, int size, String tips, final boolean needServiceProcess)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.GET_USER_AGENT_SHARE_PROFIT_LIST, params, tips, needServiceProcess);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_USER_BALANCE_LOG_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<UserBalanceLogInfo> list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<UserBalanceLogInfo>>()
				{
				}.getType());
				if (list != null)
				{
					for (UserBalanceLogInfo userBalanceLogInfo : list)
					{
					}
				}
			}
		}
	}
}
