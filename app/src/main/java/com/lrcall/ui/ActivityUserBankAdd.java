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
import com.lrcall.appbst.services.UserBankService;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

public class ActivityUserBankAdd extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityUserBankAdd.class.getSimpleName();
	private EditText etBankName, etName, etCardId;
	private UserBankService mUserBankService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_bank_add);
		mUserBankService = new UserBankService(this);
		mUserBankService.addDataResponse(this);
		viewInit();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		etBankName = (EditText) findViewById(R.id.et_bankname);
		etName = (EditText) findViewById(R.id.et_name);
		etCardId = (EditText) findViewById(R.id.et_cardid);
		findViewById(R.id.btn_add).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_add:
			{
				String bankName = etBankName.getText().toString();
				String cardName = etName.getText().toString();
				String cardId = etCardId.getText().toString();
				if (StringTools.isNull(bankName))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "银行名称不能为空！");
					etBankName.requestFocus();
					return;
				}
				if (StringTools.isNull(cardName))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "开户名不能为空！");
					etName.requestFocus();
					return;
				}
				if (StringTools.isNull(cardId))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "卡号不能为空！");
					etCardId.requestFocus();
					return;
				}
				mUserBankService.addUserBankInfo(bankName, cardName, cardId, "请稍后...", true);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.USER_ADD_USER_BANK_INFO))
		{
			showServerMsg(result, null);
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				setResult(RESULT_OK);
				finish();
			}
			return true;
		}
		return false;
	}
}
