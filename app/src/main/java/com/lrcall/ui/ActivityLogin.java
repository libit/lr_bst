/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
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
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;
import com.lrcall.utils.apptools.AppFactory;

public class ActivityLogin extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private EditText etUsername, etPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		viewInit();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		etUsername = (EditText) findViewById(R.id.et_username);
		etPassword = (EditText) findViewById(R.id.et_password);
		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.btn_register).setOnClickListener(this);
		findViewById(R.id.tv_reset_pwd).setOnClickListener(this);
		String number = PreferenceUtils.getInstance().getUsername();
		if (StringTools.isNull(number))
		{
			number = AppFactory.getInstance().getPhoneNumber();
		}
		etUsername.setText(number);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_login:
			{
				String username = etUsername.getText().toString();
				String password = etPassword.getText().toString();
				if (StringTools.isNull(username))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "账号不能为空！");
					etUsername.requestFocus();
					return;
				}
				if (username.length() < 5 || username.length() > 16)
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "账号位数不正确，请输入5-16位的账号！");
					etUsername.requestFocus();
					return;
				}
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
				UserService userService = new UserService(this);
				userService.addDataResponse(this);
				userService.login(username, password, "正在登录...", true);
				break;
			}
			case R.id.btn_register:
			{
				startActivityForResult(new Intent(this, ActivityRegister.class), ConstValues.REQUEST_REGISTER);
				break;
			}
			case R.id.tv_reset_pwd:
			{
				startActivityForResult(new Intent(this, ActivityResetPwd.class), ConstValues.REQUEST_RESET_PWD);
				break;
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == ConstValues.REQUEST_REGISTER)
		{
			if (resultCode == ConstValues.RESULT_REGISTER_SUCCESS)
			{
				setResult(ConstValues.RESULT_LOGIN_SUCCESS);
				finish();
			}
			else
			{
				setResult(ConstValues.RESULT_LOGIN_ERROR);
				finish();
			}
		}
		else if (requestCode == ConstValues.REQUEST_RESET_PWD)
		{
			if (resultCode == ConstValues.RESULT_RESET_PWD_SUCCESS)
			{
				setResult(ConstValues.RESULT_LOGIN_SUCCESS);
				finish();
			}
			else
			{
				setResult(ConstValues.RESULT_LOGIN_ERROR);
				finish();
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.USER_LOGIN))
		{
			showServerMsg(result, "登录成功！");
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				setResult(ConstValues.RESULT_LOGIN_SUCCESS);
				finish();
			}
		}
		return true;
	}
}
