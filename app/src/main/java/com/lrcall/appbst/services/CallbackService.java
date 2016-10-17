/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.GsonTools;

import java.util.HashMap;
import java.util.Map;

/**
 * 回拨服务类
 * Created by libit on 16/4/6.
 */
public class CallbackService extends BaseService
{
	public CallbackService(Context context)
	{
		super(context);
	}

	/**
	 * 注册回拨
	 *
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void register(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.CALLBACK_REGISTER, params, tips, needServiceProcessData);
	}

	/**
	 * 查询余额信息
	 *
	 * @param showDate               是否显示有效期
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void getBalanceInfo(boolean showDate, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("showDate", showDate);
		ajaxStringCallback(ApiConfig.CALLBACK_GET_BALANCE_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 回拨呼叫
	 *
	 * @param number                 被叫号码
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void makeCall(String number, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("number", number);
		ajaxStringCallback(ApiConfig.CALLBACK_MAKE_CALL, params, tips, needServiceProcessData);
	}

	/**
	 * 回拨充值卡充值
	 *
	 * @param cardId                 充值卡号
	 * @param cardPwd                充值密码
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void recharge(String cardId, String cardPwd, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("cardId", cardId);
		params.put("cardPwd", cardPwd);
		ajaxStringCallback(ApiConfig.CALLBACK_RECHARGE, params, tips, needServiceProcessData);
	}

	/**
	 * 获取回拨呼叫记录列表
	 *
	 * @param start
	 * @param size
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void getCallLogList(int start, int size, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.CALLBACK_GET_CALL_LOG_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取回拨充值记录列表
	 *
	 * @param start
	 * @param size
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void getRechargeLogList(int start, int size, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.CALLBACK_GET_RECHARGE_LOG_LIST, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.CALLBACK_MAKE_CALL))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				ToastView.showCenterToast(context, R.drawable.ic_done, returnInfo.getErrmsg());
			}
			else
			{
				String msg = result;
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(context, R.drawable.ic_do_fail, "呼叫失败:" + msg);
			}
		}
		else if (url.endsWith(ApiConfig.CALLBACK_REGISTER))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				ToastView.showCenterToast(context, R.drawable.ic_done, "开通回拨服务成功!");
			}
			else
			{
				String msg = result;
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(context, R.drawable.ic_do_fail, "开通回拨服务失败:" + msg);
			}
		}
	}
}
