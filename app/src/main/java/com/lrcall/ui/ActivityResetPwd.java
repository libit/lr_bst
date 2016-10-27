/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.UserInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UserService;
import com.lrcall.enums.SmsCodeType;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.CallTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;

public class ActivityResetPwd extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private EditText etUserId, etNewPassword, etReNewPassword, etCode;
	private UserService mUserService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_pwd);
		mUserService = new UserService(this);
		mUserService.addDataResponse(this);
		viewInit();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		etUserId = (EditText) findViewById(R.id.et_username);
		etNewPassword = (EditText) findViewById(R.id.et_new_password);
		etReNewPassword = (EditText) findViewById(R.id.et_re_new_password);
		etCode = (EditText) findViewById(R.id.et_code);
		findViewById(R.id.btn_get_code).setOnClickListener(this);
		findViewById(R.id.btn_ok).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_get_code:
			{
				String username = etUserId.getText().toString();
				if (StringTools.isNull(username))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "手机号码不能为空！");
					etUserId.requestFocus();
					return;
				}
				if (!CallTools.isChinaMobilePhoneNumber(username))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "手机号码格式不正确！");
					etUserId.requestFocus();
					return;
				}
				mUserService.getSmsCode(username, SmsCodeType.RESET_PWD.getType(), "正在请求短信验证码,请稍后...", false);
				break;
			}
			case R.id.btn_ok:
			{
				String username = etUserId.getText().toString();
				String newPassword = etNewPassword.getText().toString();
				String reNewPassword = etReNewPassword.getText().toString();
				String code = etCode.getText().toString();
				if (StringTools.isNull(username))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "手机号码不能为空！");
					etUserId.requestFocus();
					return;
				}
				if (!CallTools.isChinaMobilePhoneNumber(username))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "手机号码格式不正确！");
					etUserId.requestFocus();
					return;
				}
				if (StringTools.isNull(newPassword))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "新密码不能为空！");
					etNewPassword.requestFocus();
					return;
				}
				if (newPassword.length() < 6 || newPassword.length() > 16)
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "新密码位数不正确，请输入6-16位的新密码！");
					etNewPassword.requestFocus();
					return;
				}
				if (StringTools.isNull(reNewPassword))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "确认密码不能为空！");
					etReNewPassword.requestFocus();
					return;
				}
				if (!reNewPassword.equals(newPassword))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "两次输入的新密码不一致！");
					etNewPassword.requestFocus();
					return;
				}
				if (StringTools.isNull(code))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "验证码不能为空！");
					etCode.requestFocus();
					return;
				}
				mUserService.resetPwd(username, newPassword, code, "正在重置密码...", false);
				break;
			}
		}
	}

	/**
	 * ajax回调函数
	 *
	 * @param url
	 * @param result 返回结果
	 * @param status 状态
	 * @return
	 */
	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.USER_RESET_PWD))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				//修改成功，保存SessionId
				UserInfo userInfo = GsonTools.getReturnObject(returnInfo, UserInfo.class);
				PreferenceUtils.getInstance().setSessionId(userInfo.getSessionId());
				ToastView.showCenterToast(this, R.drawable.ic_done, "重置密码成功！");
				etNewPassword.setText("");
				etReNewPassword.setText("");
				setResult(ConstValues.RESULT_RESET_PWD_SUCCESS);
				finish();
			}
			else
			{
				if (returnInfo != null)
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "重置密码失败：" + returnInfo.getErrmsg());
				}
				else
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "重置密码失败：" + result);
				}
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_SMS_CODE))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				ToastView.showCenterToast(this, R.drawable.ic_done, returnInfo.getErrmsg());
			}
			else
			{
				String msg = result;
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, "获取验证码失败:" + msg);
			}
			return true;
		}
		return false;
	}
}
