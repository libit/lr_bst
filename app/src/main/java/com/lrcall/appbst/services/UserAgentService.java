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
 * 用户代理服务类，用于操作与用户代理相关的服务
 * Created by libit on 16/4/6.
 */
public class UserAgentService extends BaseService
{
	public UserAgentService(Context context)
	{
		super(context);
	}

	/**
	 * 用户申请代理
	 *
	 * @param userType               申请类型
	 * @param provinceId             省ID
	 * @param cityId                 市ID
	 * @param countryId              区ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData 是否需要服务类处理
	 */
	public void applyAgent(Byte userType, String provinceId, String cityId, String countryId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("applyUserType", userType);
		params.put("provinceId", provinceId);
		params.put("cityId", cityId);
		params.put("countryId", countryId);
		ajaxStringCallback(ApiConfig.USER_APPLY_AGENT, params, tips, needServiceProcessData);
	}

	/**
	 * 获取用户代理信息
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData 是否需要服务类处理
	 */
	public void getUserAgentInfo(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_USER_AGENT_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 获取用户最后一次申请代理信息
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData 是否需要服务类处理
	 */
	public void getUserLastApplyAgentInfo(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_USER_LAST_APPLY_AGENT_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 获取用户推荐人列表
	 *
	 * @param tips               提示信息
	 * @param needServiceProcess
	 */
	public void getReferrerUserList(int start, int size, String tips, final boolean needServiceProcess)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.GET_REFERRER_USER_LIST, params, tips, needServiceProcess);
	}

	/**
	 * 获取用户分润总额
	 *
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void getTotalUserShareProfit(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_TOTAL_USER_SHARE_PROFIT, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.USER_APPLY_AGENT))
		{
			//			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			//			if (ReturnInfo.isSuccess(returnInfo))
			//			{
			//				ToastView.showCenterToast(context, R.drawable.ic_done, returnInfo.getErrmsg());
			//			}
			//			else
			//			{
			//				String msg = "申请失败：" + result;
			//				if (returnInfo != null)
			//				{
			//					msg = "申请失败：" + returnInfo.getErrmsg();
			//				}
			//				ToastView.showCenterToast(context, R.drawable.ic_do_fail, msg);
			//			}
		}
	}
}
