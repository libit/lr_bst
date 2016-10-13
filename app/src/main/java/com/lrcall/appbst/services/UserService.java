/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;
import android.content.Intent;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.PicInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.UserInfo;
import com.lrcall.events.UserEvent;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务类，用于操作与用户相关的服务
 * Created by libit on 16/4/6.
 */
public class UserService extends BaseService
{
	public UserService(Context context)
	{
		super(context);
	}

	/**
	 * 检查本地是否已登录
	 *
	 * @return
	 */
	public static boolean isLogin()
	{
		return (!StringTools.isNull(PreferenceUtils.getInstance().getUsername()) && !StringTools.isNull(PreferenceUtils.getInstance().getSessionId()));
	}

	/**
	 * 获取验证码
	 *
	 * @param number 手机号码
	 */
	public void getSmsCode(String number, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("number", number);
		ajaxStringCallback(ApiConfig.GET_SMS_CODE, params, tips, needServiceProcessData);
	}

	/**
	 * 用户登录
	 *
	 * @param username 账号
	 * @param password 密码
	 */
	public void login(String username, String password, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("userId", username);
		params.put("password", password);
		ajaxStringCallback(ApiConfig.USER_LOGIN, params, tips, needServiceProcessData);
	}

	/**
	 * 用户注册
	 *
	 * @param username    账号
	 * @param password    密码
	 * @param payPassword 支付密码
	 * @param name        姓名
	 * @param nickname    昵称
	 * @param number      手机号码
	 * @param email       邮箱
	 * @param sex         性别
	 * @param picId       图片ID
	 * @param code        验证码
	 */
	public void register(String username, String password, String payPassword, String name, String nickname, String number, String email, String referrerId, Byte sex, String picId, String code, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("userId", username);
		params.put("password", password);
		params.put("payPassword", payPassword);
		params.put("name", name);
		params.put("nickname", nickname);
		params.put("number", number);
		params.put("sex", sex);
		params.put("email", email);
		params.put("referrerId", referrerId);
		params.put("picId", picId);
		params.put("code", code);
		ajaxStringCallback(ApiConfig.USER_REGISTER, params, tips, needServiceProcessData);
	}

	/**
	 * 获取用户信息
	 */
	public void getUserInfo(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_USER_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 获取用户余额信息
	 */
	public void getUserBalanceInfo(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_USER_BALANCE_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 注销登录
	 */
	public void logout()
	{
		PreferenceUtils.getInstance().setSessionId("");
		EventBus.getDefault().post(new UserEvent(UserEvent.EVENT_LOGOUT));
	}

	/**
	 * 分享给好友
	 */
	public void share(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.USER_SHARE, params, tips, needServiceProcessData);
	}

	/**
	 * 用户修改密码
	 *
	 * @param password    旧密码
	 * @param newPassword 新密码
	 */
	public void changePwd(String password, String newPassword, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("password", password);
		params.put("newPassword", newPassword);
		ajaxStringCallback(ApiConfig.USER_CHANGE_PWD, params, tips, needServiceProcessData);
	}

	/**
	 * 上传用户头像
	 *
	 * @param data 图片数据
	 */
	public void updateUserHead(byte[] data, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("pic", data);
		params.put("picFileName", System.currentTimeMillis() + ".jpg");
		params.put("picContentType", "jpg");
		params.put("sortId", 6);
		ajaxStringCallback(ApiConfig.UPDATE_PIC, params, tips, needServiceProcessData);
	}

	/**
	 * 下载用户头像
	 *
	 * @param picPath                图片路径
	 * @param needServiceProcessData
	 */
	public void getUserHead(String picPath, String tips, final boolean needServiceProcessData)
	{
		ajaxFileCallback(String.format("%s/../%s", ApiConfig.getServerUrl(), picPath), null, tips, needServiceProcessData);
	}

	/**
	 * 意见反馈
	 *
	 * @param number  手机号码
	 * @param content 内容
	 */
	public void submitAdvice(String number, String content, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("number", number);
		params.put("content", content);
		ajaxStringCallback(ApiConfig.SUBMIT_ADVICE, params, tips, needServiceProcessData);
	}

	/**
	 * 获取公共配置
	 */
	public void getClientConfig(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_CLIENT_CONFIG, params, tips, needServiceProcessData);
	}

	/**
	 * 用户签到
	 */
	public void userSignToday(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.USER_SIGN_TODAY, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.USER_LOGIN))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				//登录成功，保存账号和SessionId
				UserInfo userInfo = GsonTools.getReturnObject(returnInfo, UserInfo.class);
				if (userInfo != null)
				{
					PreferenceUtils.getInstance().setUsername(userInfo.getUserId());
					PreferenceUtils.getInstance().setSessionId(userInfo.getSessionId());
					if (userInfo.getPicInfo() != null)
					{
						getUserHead(userInfo.getPicInfo().getPicUrl(), null, true);
					}
					EventBus.getDefault().post(new UserEvent(UserEvent.EVENT_LOGINED));
				}
			}
			else
			{
				// 登录失败，清空SessionId
				PreferenceUtils.getInstance().setSessionId("");
				String msg = result;
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(context, R.drawable.ic_do_fail, "登录失败：" + msg);
				EventBus.getDefault().post(new UserEvent(UserEvent.EVENT_LOGOUT));
			}
		}
		else if (url.endsWith(ApiConfig.USER_REGISTER))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				//注册成功，保存账号和SessionId
				UserInfo userInfo = GsonTools.getReturnObject(returnInfo, UserInfo.class);
				if (userInfo != null)
				{
					PreferenceUtils.getInstance().setUsername(userInfo.getUserId());
					PreferenceUtils.getInstance().setSessionId(userInfo.getSessionId());
					EventBus.getDefault().post(new UserEvent(UserEvent.EVENT_LOGINED));
				}
			}
			else
			{
				// 注册失败，清空SessionId
				PreferenceUtils.getInstance().setSessionId("");
				if (returnInfo != null)
				{
					ToastView.showCenterToast(context, R.drawable.ic_do_fail, "注册失败：" + returnInfo.getErrmsg());
				}
				else
				{
					ToastView.showCenterToast(context, R.drawable.ic_do_fail, "注册失败：" + result);
				}
				EventBus.getDefault().post(new UserEvent(UserEvent.EVENT_LOGOUT));
			}
		}
		else if (url.endsWith(ApiConfig.GET_USER_INFO))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (!ReturnInfo.isSuccess(returnInfo))
			{
				// 获取失败，清空SessionId
				PreferenceUtils.getInstance().setSessionId("");
				EventBus.getDefault().post(new UserEvent(UserEvent.EVENT_LOGOUT));
			}
		}
		else if (url.endsWith(ApiConfig.USER_SHARE))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				String msg = returnInfo.getErrmsg();
				Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送到属性
				intent.setType("text/plain"); // 分享发送到数据类型
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享"); // 分享的主题
				intent.putExtra(Intent.EXTRA_TEXT, msg); // 分享的内容
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 允许intent启动新的activity
				context.startActivity(Intent.createChooser(intent, "来自" + context.getString(R.string.app_name) + "的分享")); // //目标应用选择对话框的
			}
			else
			{
				ToastView.showCenterToast(context, R.drawable.ic_do_fail, "暂时无法分享！");
			}
		}
		else if (url.endsWith(ApiConfig.USER_CHANGE_PWD))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				//修改成功，保存SessionId
				UserInfo userInfo = GsonTools.getReturnObject(returnInfo, UserInfo.class);
				PreferenceUtils.getInstance().setSessionId(userInfo.getSessionId());
				EventBus.getDefault().post(new UserEvent(UserEvent.EVENT_LOGINED));
				ToastView.showCenterToast(context, R.drawable.ic_done, "修改密码成功！");
			}
			else
			{
				if (returnInfo != null)
				{
					ToastView.showCenterToast(context, R.drawable.ic_do_fail, "修改密码失败：" + returnInfo.getErrmsg());
				}
				else
				{
					ToastView.showCenterToast(context, R.drawable.ic_do_fail, "修改密码失败：" + result);
				}
			}
		}
		else if (url.endsWith(ApiConfig.UPDATE_PIC))
		{
			PicInfo picInfo = GsonTools.getReturnObject(result, PicInfo.class);
			if (picInfo != null)
			{
				LogcatTools.debug("USER_UPDATE_PIC", "picInfo:" + picInfo.toString());
				Map<String, Object> params = new HashMap<>();
				params.put("picId", picInfo.getPicId());
				ajaxStringCallback(ApiConfig.USER_UPDATE_PIC_INFO, params, null, true);
			}
		}
		else if (url.endsWith(ApiConfig.USER_UPDATE_PIC_INFO))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
		}
		else if (url.endsWith(ApiConfig.USER_SIGN_TODAY))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				ToastView.showCenterToast(context, R.drawable.ic_done, returnInfo.getErrmsg());
			}
			else
			{
				String msg = "签到失败！";
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(context, R.drawable.ic_do_fail, msg);
			}
		}
	}

	@Override
	protected void parseData(String url, File file, AjaxStatus status)
	{
		super.parseData(url, file, status);
		if (url.endsWith("jpg"))
		{
			if (file != null)
			{
				String userId = PreferenceUtils.getInstance().getUsername();
				String userHeadPath = AppConfig.getUserPicCacheDir(userId);
				File userHeadFile = new File(userHeadPath);//new File(userHeadPath.substring(0, userHeadPath.lastIndexOf("/")));
				File f = new File(userHeadPath.substring(0, userHeadPath.lastIndexOf("/")));
				if (!f.exists())
				{
					f.mkdirs();
				}
				FileInputStream fileInputStream = null;
				FileOutputStream fileOutputStream = null;
				try
				{
					fileInputStream = new FileInputStream(file);
					fileOutputStream = new FileOutputStream(userHeadFile);
					byte[] buffer = new byte[1024000];
					fileInputStream.read(buffer);
					fileOutputStream.write(buffer);
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					if (fileInputStream != null)
					{
						try
						{
							fileInputStream.close();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
					if (fileOutputStream != null)
					{
						try
						{
							fileOutputStream.close();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
