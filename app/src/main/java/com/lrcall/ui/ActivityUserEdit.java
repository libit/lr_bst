/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2017.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UserService;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.dialog.DialogSelectDate;
import com.lrcall.utils.DateTimeTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;
import com.lrcall.utils.apptools.AppFactory;

public class ActivityUserEdit extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private EditText etUsername, etName, etIdentifityNumber, etDate;
	private UserService mUserService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_edit);
		viewInit();
		mUserService = new UserService(this);
		mUserService.addDataResponse(this);
		String number = AppFactory.getInstance().getPhoneNumber();
		etUsername.setText(number);
		etUsername.setEnabled(false);
		etDate.setEnabled(false);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		etUsername = (EditText) findViewById(R.id.et_username);
		etName = (EditText) findViewById(R.id.et_name);
		etIdentifityNumber = (EditText) findViewById(R.id.et_identifity_number);
		etDate = (EditText) findViewById(R.id.et_register_date);
		findViewById(R.id.btn_select_date).setOnClickListener(this);
		findViewById(R.id.btn_submit).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_select_date:
			{
				DialogSelectDate dialogSelectDate = new DialogSelectDate(this, new DialogSelectDate.OnSelectDateListenser()
				{
					@Override
					public void onOkClick(DatePicker datePicker)
					{
						int year = datePicker.getYear();
						int month = datePicker.getMonth();
						int day = datePicker.getDayOfMonth();
						long date = DateTimeTools.getDateTimeLong(year, month, day);
						etDate.setText(DateTimeTools.getDateString(date));
					}

					@Override
					public void onCancelClick()
					{
					}
				});
				dialogSelectDate.show();
				dialogSelectDate.setDate(DateTimeTools.getDateTimeLong(2010, 0, 1), DateTimeTools.getDateTimeLong(2017, 0, 1));
				break;
			}
			case R.id.btn_submit:
			{
				String name = etName.getText().toString();
				String identifityNumber = etIdentifityNumber.getText().toString();
				String date = etDate.getText().toString();
				if (StringTools.isNull(name))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "姓名不能为空！");
					etName.requestFocus();
					return;
				}
				if (StringTools.isNull(identifityNumber))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "身份证号不能为空！");
					etIdentifityNumber.requestFocus();
					return;
				}
				if (StringTools.isNull(date))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "注册时间不能为空！");
					etDate.requestFocus();
					return;
				}
				long tm = 0;
				try
				{
					tm = DateTimeTools.getTime(date + " 00:00:00");
				}
				catch (Exception e)
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "时间输入错误！");
					etDate.requestFocus();
					return;
				}
				mUserService.updateUserInfo(name, identifityNumber, tm, "正在提交,请稍后...", false);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.UPDATE_USER_INFO))
		{
			showServerMsg(result, "更新资料成功！");
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				setResult(RESULT_OK);
				finish();
			}
			else
			{
				setResult(RESULT_CANCELED);
			}
			return true;
		}
		return false;
	}
}
