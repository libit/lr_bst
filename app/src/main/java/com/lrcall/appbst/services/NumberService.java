/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;
import android.widget.TextView;

import com.androidquery.AQuery;

/**
 * 归属地服务类
 * Created by libit on 16/4/6.
 */
public class NumberService extends BaseService
{
	public NumberService(Context context, TextView v)
	{
		super(context);
		aQuery = new AQuery(v);
	}

	/**
	 * 服务器获取归属地
	 *
	 * @param number 电话号码
	 * @param append 是否附加到当前TextView
	 */
	public void getLocalInfo(final String number, final boolean append)
	{
//		Map<String, Object> params = new HashMap<>();
//		params.put("number", number);
//		aQuery.ajax(new AjaxCallback<String>()
//		{
//			@Override
//			public void callback(String url, String result, AjaxStatus status)
//			{
//				LogcatTools.debug("ajaxStringCallback", "url:" + url + " , params:" + params.get("number") + ", result:" + result);
//				if (!StringTools.isNull(result))
//				{
//					DbNumberLocalInfoFactory.getInstance().addOrUpdateNumberLocalInfo(number, result);
//					if (append)
//					{
//						aQuery.getTextView().setText(aQuery.getText() + "  " + result);
//					}
//					else
//					{
//						aQuery.getTextView().setText(result);
//					}
//				}
//				getNumberLabelInfo(number, true);
//			}
//		}.url(ApiConfig.GET_NUMBER_LOCAL).type(String.class).params(params));
	}

	/**
	 * 服务器获取号码标记
	 *
	 * @param number 电话号码
	 * @param append 是否附加到当前TextView
	 */
	public void getNumberLabelInfo(final String number, final boolean append)
	{
//		Map<String, Object> params = new HashMap<>();
//		params.put("number", number);
//		aQuery.ajax(new AjaxCallback<String>()
//		{
//			@Override
//			public void callback(String url, String result, AjaxStatus status)
//			{
//				LogcatTools.debug("ajaxStringCallback", "url:" + url + " , params:" + params.get("number") + ", result:" + result);
//				if (!StringTools.isNull(result))
//				{
//					NumberLabelInfo numberLabelInfo = GsonTools.getReturnObject(result, NumberLabelInfo.class);
//					if (numberLabelInfo != null)
//					{
//						DbNumberLabelInfoFactory.getInstance().addOrUpdateNumberLabelInfo(number, numberLabelInfo.getLabel(), numberLabelInfo.getCount());
//						if (append)
//						{
//							aQuery.getTextView().setText(aQuery.getText() + "  " + numberLabelInfo.getLabel());
//						}
//						else
//						{
//							aQuery.getTextView().setText(result);
//						}
//					}
//				}
//			}
//		}.url(ApiConfig.GET_NUMBER_LABEL).type(String.class).params(params));
	}
}
