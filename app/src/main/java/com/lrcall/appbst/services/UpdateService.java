/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.UpdateInfo;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.dialog.DialogCommon;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.FileTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.apptools.AppFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 更新服务类
 * Created by libit on 16/4/6.
 */
public class UpdateService extends BaseService
{
	public UpdateService(Context context)
	{
		super(context);
	}

	/**
	 * 检查服务器更新
	 */
	public void checkUpdate(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.CHECK_UPDATE, params, tips, needServiceProcessData);
	}

	/**
	 * 下载服务器更新
	 *
	 * @param updateInfo 更新信息
	 */
	public void downLoading(final UpdateInfo updateInfo)
	{
		Map<String, Object> params = new HashMap<>();
		File file = FileTools.getFile(AppConfig.getUpdateFolder(), context.getString(R.string.app_name) + "_v" + updateInfo.getVersion() + ".apk");
		ajaxDownloadFileCallback(updateInfo.getUrl(), params, file, context.getString(R.string.downloading), true);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.CHECK_UPDATE))
		{
			UpdateInfo updateInfo = GsonTools.getReturnObject(result, UpdateInfo.class);
			if (updateInfo != null && AppFactory.getInstance().getVersionCode() < updateInfo.getVersionCode())
			{
				showUpdataDialog(updateInfo);
			}
			else
			{
				//				ToastView.showCenterToast(context, "您的版本已是最新版！");
				Toast.makeText(context, "您的版本已是最新版！", Toast.LENGTH_SHORT).show();
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
				ToastView.showCenterToast(context, R.drawable.ic_do_fail, "下载升级程序失败！");
				return;
			}
			AppFactory.getInstance().installApp(file, false);
		}
	}

	/**
	 * 显示下载确认对话框
	 *
	 * @param updateInfo 更新信息
	 */
	public void showUpdataDialog(final UpdateInfo updateInfo)
	{
		DialogCommon dialog = new DialogCommon(context, new DialogCommon.LibitDialogListener()
		{
			@Override
			public void onOkClick()
			{
				downLoading(updateInfo);
			}

			@Override
			public void onCancelClick()
			{
			}
		}, context.getString(R.string.update_title), "v" + updateInfo.getVersion() + "版本更新日志：\n" + updateInfo.getDescription() + "\n", true, false);
		//		更新时间：+StringTools.getTime(updateInfo.getAddDateLong())
		dialog.show();
		dialog.setOKString(R.string.yes);
		dialog.setCancelString(R.string.no);
	}
}
