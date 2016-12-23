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
 * 提现服务类
 * Created by libit on 16/4/6.
 */
public class WithdrawService extends BaseService
{
	public WithdrawService(Context context)
	{
		super(context);
	}

	/**
	 * 获取提现列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getUserWithdrawInfoList(int start, int size, List<TableOrderInfo> orderInfos, List<TableSearchInfo> searchInfos, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.USER_WITHDRAW_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 添加提现
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void addUserWithdrawInfo(int amount, String bankName, String cardName, String cardId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("amount", amount);
		params.put("bankName", bankName);
		params.put("cardName", cardName);
		params.put("cardId", cardId);
		ajaxStringCallback(ApiConfig.USER_ADD_WITHDRAW, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.USER_ADD_WITHDRAW))
		{
		}
	}
}
