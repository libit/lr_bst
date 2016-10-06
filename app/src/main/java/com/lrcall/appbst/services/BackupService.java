/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.UserBackupInfo;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.CryptoTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by libit on 16/4/6.
 */
public class BackupService extends BaseService
{
	public BackupService(Context context)
	{
		super(context);
	}

	/**
	 * 用户获取配置备份
	 *
	 * @param name 备份名称
	 */
	public void getBackupConfig(String name, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		ajaxStringCallback(ApiConfig.GET_BACKUP_CONFIG, params, tips, needServiceProcessData);
	}

	/**
	 * 用户备份配置
	 *
	 * @param name        备份名称
	 * @param data        备份数据
	 * @param description 描述
	 */
	public void updateBackupConfig(String name, String data, String description, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("data", data);
		params.put("description", description);
		params.put("signData", CryptoTools.getMD5Str(PreferenceUtils.getInstance().getUsername() + data));
		ajaxStringCallback(ApiConfig.UPDATE_BACKUP_CONFIG, params, tips, needServiceProcessData);
	}

	/**
	 * 用户备份联系人
	 *
	 * @param content 备份数据
	 */
	public void updateContactBackupInfos(String content, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("content", content);
		ajaxStringCallback(ApiConfig.USER_BACKUP_CONTACTS, params, tips, needServiceProcessData);
	}

	/**
	 * 获取用户备份联系人
	 */
	public void getContactBackupInfos(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.USER_GET_CONTACTS, params, tips, needServiceProcessData);
	}

	/**
	 * 获取用户备份联系人数量
	 */
	public void getContactBackupInfoCount(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.USER_GET_CONTACTS_COUNT, params, tips, needServiceProcessData);
	}

	/**
	 * 用户备份通话记录
	 *
	 * @param content 备份数据
	 */
	public void updateHistoryBackupInfos(String content, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("content", content);
		ajaxStringCallback(ApiConfig.USER_BACKUP_HISOTRY, params, tips, needServiceProcessData);
	}

	/**
	 * 获取用户备份通话记录
	 */
	public void getHistoryBackupInfos(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.USER_GET_HISOTRY, params, tips, needServiceProcessData);
	}

	/**
	 * 获取用户备份通话记录数量
	 */
	public void getHistoryBackupInfoCount(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.USER_GET_HISOTRY_COUNT, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.UPDATE_BACKUP_CONFIG) || url.endsWith(ApiConfig.USER_BACKUP_CONTACTS) || url.endsWith(ApiConfig.USER_BACKUP_HISOTRY))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				ToastView.showCenterToast(context, R.drawable.ic_done, "备份成功！");
			}
			else
			{
				if (returnInfo != null)
				{
					ToastView.showCenterToast(context, R.drawable.ic_do_fail, "备份失败：" + returnInfo.getErrmsg());
				}
				else
				{
					ToastView.showCenterToast(context, R.drawable.ic_do_fail, "备份失败：" + result);
				}
			}
		}
		else if (url.endsWith(ApiConfig.GET_BACKUP_CONFIG))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			UserBackupInfo userBackupInfo = GsonTools.getReturnObjects(returnInfo, UserBackupInfo.class);
			if (ReturnInfo.isSuccess(returnInfo) && userBackupInfo != null)
			{
				//获取成功
				Map map = GsonTools.getObjects(userBackupInfo.getData(), new TypeToken<Map<String, String>>()
				{
				}.getType());
				if (map != null)
				{
					Iterator<String> iterator = map.keySet().iterator();
					while (iterator.hasNext())
					{
						String key = iterator.next();
						String value = (String) map.get(key);
						if (value.equals("true") || value.equals("false"))
						{
							PreferenceUtils.getInstance().setBooleanValue(key, Boolean.valueOf(value));
						}
						else
						{
							PreferenceUtils.getInstance().setStringValue(key, value);
						}
					}
					PreferenceUtils.getInstance().setBooleanValue(PreferenceUtils.IS_FIRST_RUN, false);
					ToastView.showCenterToast(context, R.drawable.ic_done, "同步成功！");
				}
				else
				{
					ToastView.showCenterToast(context, R.drawable.ic_do_fail, "用户未备份！");
				}
			}
			else
			{
				// 获取失败
				if (returnInfo != null)
				{
					ToastView.showCenterToast(context, R.drawable.ic_do_fail, "同步失败：" + returnInfo.getErrmsg());
				}
				else
				{
					ToastView.showCenterToast(context, R.drawable.ic_do_fail, "同步失败：" + result);
				}
			}
		}
		else if (url.endsWith(ApiConfig.USER_GET_CONTACTS))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
			}
			else
			{
			}
		}
	}
}
