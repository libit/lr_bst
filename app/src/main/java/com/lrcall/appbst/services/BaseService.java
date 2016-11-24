/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.app.ProgressDialog;
import android.content.Context;

import com.androidquery.AQuery;
import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ErrorInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.events.UserEvent;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.dialog.MyProgressDialog;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.CryptoTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;
import com.lrcall.utils.apptools.AppFactory;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by libit on 16/4/6.
 */
public abstract class BaseService
{
	protected final Context context;
	private final Set<IAjaxDataResponse> IAjaxDataResponseList = new HashSet<>();
	protected AQuery aQuery;

	/**
	 * 构造参数,需要传入Context上下文
	 *
	 * @param context 应用上下文
	 */
	public BaseService(Context context)
	{
		super();
		this.context = context;
		aQuery = new AQuery(context);
	}

	/**
	 * 处理String的返回数据
	 *
	 * @param url    接口地址
	 * @param result 返回结果
	 * @param status 状态
	 */
	protected void parseData(String url, String result, AjaxStatus status)
	{
	}

	/**
	 * 处理File类型的返回数据
	 *
	 * @param url    接口地址
	 * @param file   返回文件
	 * @param status 返回状态
	 */
	protected void parseData(String url, File file, AjaxStatus status)
	{
	}

	/**
	 * 构建公共参数
	 *
	 * @param params 参数
	 * @return 参数
	 */
	private Map<String, Object> buildParams(Map<String, Object> params)
	{
		if (params == null)
		{
			return null;
		}
		if (!params.containsKey("userId"))
		{
			params.put("userId", PreferenceUtils.getInstance().getUserId());
		}
		if (!params.containsKey("sessionId"))
		{
			params.put("sessionId", PreferenceUtils.getInstance().getSessionId());
		}
		if (!params.containsKey("agentId"))
		{
			params.put("agentId", AppConfig.getAgent());
		}
		params.put("platform", AppConfig.PLATFORM);
		params.put("deviceName", AppFactory.getInstance().getDeviceName());
		params.put("sysVersion", AppFactory.getInstance().getSysVersion());
		params.put("versionCode", AppFactory.getInstance().getVersionCode());
		String signData = CryptoTools.getSignValue(params.values());
		params.put("sign", signData);
		//		params.put("picQuality", AppConfig.PLATFORM);
		params.put("v", ApiConfig.API_VERSION);
		return params;
	}

	/**
	 * ajax异步取结果，返回值为String类型的Gson对象
	 *
	 * @param url                接口地址
	 * @param params             参数
	 * @param showProgressDialog 是否显示等待对话框
	 * @param tips               提示文字
	 * @param needServiceProcess 是否需要服务类处理
	 */
	private void ajaxStringCallback(String url, Map<String, Object> params, boolean showProgressDialog, String tips, final boolean needServiceProcess)
	{
		GsonCallBack<String> cb = new GsonCallBack<String>()
		{
			@Override
			public void callback(String url, String result, AjaxStatus status)
			{
				LogcatTools.debug("ajaxStringCallback", "url:" + url + " , result:" + result);
				commomResultError(url, result, status);
				if (needServiceProcess)
				{
					parseData(url, result, status);
				}
				OnDataResponse(url, result, status);
			}
		};
		AbstractAjaxCallback.setReuseHttpClient(false);
		params = buildParams(params);
		LogcatTools.debug("ajaxStringCallback", "url:" + url + " , params:" + GsonTools.toJson(params));
		cb.url(url).type(String.class).params(params);
		if (showProgressDialog)
		{
			MyProgressDialog pd = new MyProgressDialog(context, tips);
			aQuery.progress(pd.mDialog).ajax(cb);
		}
		else
		{
			aQuery.ajax(cb);
		}
	}

	/**
	 * ajax异步取结果
	 *
	 * @param url                接口地址
	 * @param params             参数
	 * @param tips               提示文字
	 * @param needServiceProcess 是否需要服务类处理
	 */
	protected void ajaxStringCallback(String url, Map<String, Object> params, String tips, final boolean needServiceProcess)
	{
		if (StringTools.isNull(tips))
		{
			ajaxStringCallback(url, params, false, null, needServiceProcess);
		}
		else
		{
			ajaxStringCallback(url, params, true, tips, needServiceProcess);
		}
	}

	/**
	 * ajax异步取结果
	 *
	 * @param url                接口地址
	 * @param params             参数
	 * @param needServiceProcess 是否需要服务类处理
	 */
	protected void ajaxStringCallback(String url, Map<String, Object> params, final boolean needServiceProcess)
	{
		ajaxStringCallback(url, params, false, null, needServiceProcess);
	}

	/**
	 * ajax异步取结果
	 *
	 * @param url    接口地址
	 * @param params 参数
	 */
	protected void ajaxStringCallback(String url, Map<String, Object> params)
	{
		ajaxStringCallback(url, params, false, null, false);
	}

	/**
	 * ajax异步取文件
	 *
	 * @param url                接口地址
	 * @param params             参数
	 * @param tips               提示文字
	 * @param needServiceProcess 是否需要服务类处理
	 */
	protected void ajaxFileCallback(String url, Map<String, Object> params, String tips, final boolean needServiceProcess)
	{
		GsonCallBack<File> cb = new GsonCallBack<File>()
		{
			@Override
			public void callback(String url, File file, AjaxStatus status)
			{
				LogcatTools.debug("ajaxFileCallback", "url:" + url + ",result:" + result);
				if (needServiceProcess)
				{
					parseData(url, file, status);
				}
			}
		};
		params = buildParams(params);
		LogcatTools.debug("ajaxFileCallback", "url:" + url + ",params:" + GsonTools.toJson(params));
		cb.url(url).type(File.class).params(params);
		if (!StringTools.isNull(tips))
		{
			final ProgressDialog pd = new ProgressDialog(context);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMessage(tips);
			pd.setCanceledOnTouchOutside(false);
			aQuery.progress(pd).ajax(cb);
		}
		else
		{
			aQuery.ajax(cb);
		}
	}

	/**
	 * ajax异步下载文件
	 *
	 * @param url                接口地址
	 * @param params             参数
	 * @param file               文件
	 * @param tips               提示文字
	 * @param needServiceProcess 是否需要服务类处理
	 */
	protected void ajaxDownloadFileCallback(String url, Map<String, Object> params, File file, String tips, final boolean needServiceProcess)
	{
		GsonCallBack<File> cb = new GsonCallBack<File>()
		{
			@Override
			public void callback(String url, File file, AjaxStatus status)
			{
				LogcatTools.debug("ajaxDownloadFileCallback", "url:" + url);
				if (needServiceProcess)
				{
					parseData(url, file, status);
				}
			}
		};
		params = buildParams(params);
		LogcatTools.debug("ajaxDownloadFileCallback", "url:" + url + ",params:" + GsonTools.toJson(params));
		cb.url(url).type(File.class).params(params);
		if (!StringTools.isNull(tips))
		{
			final ProgressDialog pd = new ProgressDialog(context);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMessage(tips);
			pd.setCanceledOnTouchOutside(false);
			aQuery.progress(pd).download(url, file, cb);
		}
		else
		{
			aQuery.download(url, file, cb);
		}
	}

	/**
	 * 添加观察者
	 *
	 * @param IAjaxDataResponse
	 * @return
	 */
	public boolean addDataResponse(IAjaxDataResponse IAjaxDataResponse)
	{
		synchronized (IAjaxDataResponseList)
		{
			if (!IAjaxDataResponseList.contains(IAjaxDataResponse))
			{
				return IAjaxDataResponseList.add(IAjaxDataResponse);
			}
		}
		return true;
	}

	/**
	 * 移除观察者
	 *
	 * @param IAjaxDataResponse
	 * @return
	 */
	public boolean removeDataResponse(IAjaxDataResponse IAjaxDataResponse)
	{
		synchronized (IAjaxDataResponseList)
		{
			if (IAjaxDataResponseList.contains(IAjaxDataResponse))
			{
				return IAjaxDataResponseList.remove(IAjaxDataResponse);
			}
		}
		return true;
	}

	/**
	 * 通知观察者
	 *
	 * @param url    接口地址
	 * @param result 返回结果
	 * @param status 返回状态
	 */
	protected void OnDataResponse(String url, String result, AjaxStatus status)
	{
		for (IAjaxDataResponse IAjaxDataResponse : IAjaxDataResponseList)
		{
			boolean b = IAjaxDataResponse.onAjaxDataResponse(url, result, status);
			//			if (b)
			//			{
			// 如果处理成功，则终止向下执行
			//				break;
			//			}
		}
	}

	/**
	 * 公共的错误处理方法
	 *
	 * @param url    接口地址
	 * @param result 返回结果
	 * @param status 返回状态
	 * @return
	 */
	public boolean commomResultError(String url, String result, AjaxStatus status)
	{
		if (StringTools.isNull(result))
		{
			ToastView.showCenterToast(context, R.drawable.ic_do_fail, "网络错误，请检查网络设置！");
			return true;
		}
		ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
		if (returnInfo == null)
		{
			//			ToastView.showCenterToast(context, R.drawable.ic_do_fail, "网络错误，请检查网络设置！");
			//			return true;
		}
		else
		{
			//如果是密码错误,则需要登出
			if (returnInfo.getErrcode() == ErrorInfo.getPasswordErrorId())
			{
				PreferenceUtils.getInstance().setSessionId("");
				EventBus.getDefault().post(new UserEvent(UserEvent.EVENT_LOGOUT));
			}
		}
		return false;
	}
}
