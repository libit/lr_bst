/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ShopService;
import com.lrcall.appbst.services.UserService;
import com.lrcall.enums.SmsCodeType;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;

public class ActivityShopRegister extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private EditText etUsername, etPassword, etRePassword, etName, etNickname, etNumber, etEmail, etCode;
	private ShopService mShopService;
	private String picId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_register);
		viewInit();
		mShopService = new ShopService(this);
		mShopService.addDataResponse(this);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		etUsername = (EditText) findViewById(R.id.et_username);
		etPassword = (EditText) findViewById(R.id.et_password);
		etRePassword = (EditText) findViewById(R.id.et_repassword);
		etName = (EditText) findViewById(R.id.et_name);
		etNickname = (EditText) findViewById(R.id.et_nickname);
		etNumber = (EditText) findViewById(R.id.et_number);
		etEmail = (EditText) findViewById(R.id.et_email);
		etCode = (EditText) findViewById(R.id.et_code);
		findViewById(R.id.btn_get_code).setOnClickListener(this);
		findViewById(R.id.btn_register).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_get_code:
			{
				UserService userService = new UserService(this);
				userService.addDataResponse(this);
				userService.getSmsCode(PreferenceUtils.getInstance().getUsername(), SmsCodeType.SHOP_REGISTER.getType(), "正在请求短信验证码,请稍后...", false);
				break;
			}
			case R.id.btn_register:
			{
				//				String username = etUsername.getText().toString();
				//				String password = etPassword.getText().toString();
				//				String rePassword = etRePassword.getText().toString();
				String name = etName.getText().toString();
				//				String nickname = etNickname.getText().toString();
				//				String number = etNumber.getText().toString();
				String email = etEmail.getText().toString();
				String code = etCode.getText().toString();
				//				if (StringTools.isNull(username))
				//				{
				//					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "账号不能为空！");
				//					etUsername.requestFocus();
				//					return;
				//				}
				//				if (username.length() < 5 || username.length() > 16)
				//				{
				//					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "账号位数不正确，请输入5-16位的账号！");
				//					etUsername.requestFocus();
				//					return;
				//				}
				if (StringTools.isNull(code))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "验证码不能为空！");
					etCode.requestFocus();
					return;
				}
				//				if (StringTools.isNull(password))
				//				{
				//					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "登录密码不能为空！");
				//					etPassword.requestFocus();
				//					return;
				//				}
				//				if (StringTools.isNull(rePassword))
				//				{
				//					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "确认登录密码不能为空！");
				//					etPassword.requestFocus();
				//					return;
				//				}
				//				if (password.length() < 6 || password.length() > 16)
				//				{
				//					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "登录密码位数不正确，请输入6-16位的登录密码！");
				//					etPassword.requestFocus();
				//					return;
				//				}
				//				if (!password.equals(rePassword))
				//				{
				//					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "两次输入的登录密码不一致，请重新输入！");
				//					etRePassword.requestFocus();
				//					return;
				//				}
				if (StringTools.isNull(name))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "姓名不能为空！");
					etName.requestFocus();
					return;
				}
				//				if (StringTools.isNull(nickname))
				//				{
				//					//					ToastView.showCenterToast(this, "昵称不能为空！");
				//					//					etNickname.requestFocus();
				//					//					return;
				//					nickname = name;
				//				}
				//				if (StringTools.isNull(number))
				//				{
				//					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "手机号码不能为空！");
				//					etNumber.requestFocus();
				//					return;
				//				}
				//				if (!CallTools.isChinaMobilePhoneNumber(number))
				//				{
				//					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "手机号码格式不正确！");
				//					etNumber.requestFocus();
				//					return;
				//				}
				if (StringTools.isNull(email))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "邮箱不能为空！");
					etEmail.requestFocus();
					return;
				}
				if (!StringTools.isEmail(email))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "邮箱格式不正确！");
					etEmail.requestFocus();
					return;
				}
				mShopService.register(name, name, email, picId, code, "正在申请...", true);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.SHOP_REGISTER))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				Toast.makeText(this, "恭喜，申请成功！", Toast.LENGTH_SHORT).show();
				finish();
				startActivity(new Intent(this, ActivityShopAuth.class));
			}
			else
			{
				String msg = result;
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, "申请失败:" + msg);
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
