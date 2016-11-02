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
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UserService;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

public class ActivityChangePwd extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private EditText etPassword, etNewPassword, etReNewPassword;
	private UserService userService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pwd);
		viewInit();
		userService = new UserService(this);
		userService.addDataResponse(this);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		etPassword = (EditText) findViewById(R.id.et_current_password);
		etNewPassword = (EditText) findViewById(R.id.et_new_password);
		etReNewPassword = (EditText) findViewById(R.id.et_re_new_password);
		findViewById(R.id.btn_ok).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_ok:
			{
				String password = etPassword.getText().toString();
				String newPassword = etNewPassword.getText().toString();
				String reNewPassword = etReNewPassword.getText().toString();
				if (StringTools.isNull(password))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "密码不能为空！");
					etPassword.requestFocus();
					return;
				}
				if (password.length() < 6 || password.length() > 16)
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "密码位数不正确，请输入6-16位的密码！");
					etPassword.requestFocus();
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
				if (password.equals(newPassword))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "原密码和新密码一致，无需修改！");
					etNewPassword.requestFocus();
					return;
				}
				userService.changePwd(password, newPassword, "正在修改密码...", true);
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
		if (url.endsWith(ApiConfig.USER_CHANGE_PWD))
		{
			showServerMsg(result);
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				etPassword.setText("");
				etNewPassword.setText("");
				etReNewPassword.setText("");
			}
			return true;
		}
		return false;
	}
}
