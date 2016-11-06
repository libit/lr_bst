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
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

public class ActivityAdvice extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private EditText etNumber, etContent;
	private UserService mUserService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advice);
		mUserService = new UserService(this);
		mUserService.addDataResponse(this);
		viewInit();
		if (UserService.isLogin())
		{
			mUserService.getUserInfo("请稍后...", false);
		}
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		etNumber = (EditText) findViewById(R.id.et_number);
		etContent = (EditText) findViewById(R.id.et_content);
		findViewById(R.id.btn_ok).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_ok:
			{
				String number = etNumber.getText().toString();
				String content = etContent.getText().toString();
				if (StringTools.isNull(content))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "反馈内容不能为空！");
					etContent.requestFocus();
					return;
				}
				if (content.length() < 10)
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "请至少输入10字！");
					etContent.requestFocus();
					return;
				}
				mUserService.submitAdvice(number, content, "请稍后...", false);
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
		if (url.endsWith(ApiConfig.GET_USER_INFO))
		{
			UserInfo userInfo = GsonTools.getReturnObject(result, UserInfo.class);
			if (userInfo != null)
			{
				etNumber.setText(userInfo.getNumber());
				etNumber.setEnabled(false);
			}
			else
			{
				etNumber.setText("");
				etNumber.setEnabled(true);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.SUBMIT_ADVICE))
		{
			showServerMsg(result, "提交反馈成功！");
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				etNumber.setText("");
				etContent.setText("");
			}
			return true;
		}
		return false;
	}
}
