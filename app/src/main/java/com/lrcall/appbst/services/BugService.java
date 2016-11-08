/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.models.ErrorInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.FileTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;
import com.lrcall.utils.apptools.AppFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by libit on 16/4/6.
 */
public class BugService extends BaseService
{
	private static final String TAG = BugService.class.getSimpleName();

	public BugService(Context context)
	{
		super(context);
	}

	/**
	 * 提交日志文件
	 */
	public void uploadLogFile(String tips, final boolean needServiceProcessData)
	{
		String path = PreferenceUtils.getInstance().getStringValue(PreferenceUtils.PREF_CRASH_FILE);
		if (StringTools.isNull(path))
		{
			return;
		}
		File file = new File(FileTools.getDir(AppConfig.getLogcatFolder()) + "/" + path);
		File file1 = new File(FileTools.getDir(AppConfig.getLogcatFolder()) + "/" + path + ".err");
		if (file.exists())
		{
			Map<String, Object> params = new HashMap<>();
			params.put("upload", file);
			ajaxStringCallback(ApiConfig.UPLOAD_DEBUG_FILE, params, tips, needServiceProcessData);
		}
		else
		{
			PreferenceUtils.getInstance().setStringValue(PreferenceUtils.PREF_CRASH_FILE, "");
		}
		if (file1.exists())
		{
			Map<String, Object> params = new HashMap<>();
			params.put("upload", file1);
			ajaxStringCallback(ApiConfig.UPLOAD_DEBUG_FILE, params, tips, needServiceProcessData);
		}
	}

	/**
	 * 提交日志
	 */
	public void uploadLog(File file, String url, String tips, final boolean needServiceProcessData)
	{
		final String bug = FileTools.readFile(file);
		String content = bug;
		if (StringTools.isNull(bug))
		{
			PreferenceUtils.getInstance().setStringValue(PreferenceUtils.PREF_CRASH_FILE, "");
			OnDataResponse(ApiConfig.SUBMIT_BUG, new ReturnInfo(ErrorInfo.getParamErrorId(), "日志内容为空！").toString(), null);
			return;
		}
		else
		{
			if (bug.length() > 10240)
			{
				content = bug.substring(bug.length() - 10240, bug.length());
			}
		}
		Map<String, Object> params = new HashMap<>();
		params.put("content", content);
		params.put("url", url);
		ajaxStringCallback(ApiConfig.SUBMIT_BUG, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.SUBMIT_BUG))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				LogcatTools.debug(TAG, "日志已提交！");
			}
			else
			{
				LogcatTools.debug(TAG, "日志提交失败：" + result);
			}
		}
		else if (url.endsWith(ApiConfig.UPLOAD_DEBUG_FILE))
		{
			LogcatTools.debug(TAG, "上传日志文件结果：" + result);
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				String path = PreferenceUtils.getInstance().getStringValue(PreferenceUtils.PREF_CRASH_FILE);
				if (StringTools.isNull(path))
				{
					return;
				}
				File file = new File(FileTools.getDir(AppConfig.getLogcatFolder()) + "/" + path);
				uploadLog(file, returnInfo.getErrmsg(), null, true);
			}
		}
	}

	@Override
	protected void parseData(String url, File file, AjaxStatus status)
	{
		super.parseData(url, file, status);
		if (url.endsWith("apk"))
		{
			if (file == null)
			{
				return;
			}
			AppFactory.getInstance().installApp(file, false);
		}
	}
}
