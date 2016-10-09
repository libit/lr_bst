/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lrcall.appbst.R;
import com.lrcall.appbst.models.PayTypeInfo;
import com.lrcall.enums.PayType;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;

public class ActivityBalanceRecharge extends MyBaseActivity implements View.OnClickListener
{
	private static final String TAG = ActivityBalanceRecharge.class.getSimpleName();
	private EditText etAmount;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_balance_recharge);
		viewInit();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		etAmount = (EditText) findViewById(R.id.et_amount);
		findViewById(R.id.btn_recharge).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_recharge:
			{
				String amount = etAmount.getText().toString();
				if (StringTools.isNull(amount))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "充值金额不能为空！");
					etAmount.requestFocus();
					return;
				}
				int p = 0;
				try
				{
					p = Integer.parseInt(amount);
					if (p <= 0)
					{
						ToastView.showCenterToast(this, R.drawable.ic_do_fail, "充值金额不能小于0！");
						etAmount.requestFocus();
						return;
					}
				}
				catch (Exception e)
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "充值金额错误！");
					etAmount.requestFocus();
					return;
				}
				p = p * 100;//转化成分
				Intent intent = new Intent(this, ActivityPayList.class);
				intent.putExtra(ConstValues.DATA_PAY_TYPE_INFO, GsonTools.toJson(new PayTypeInfo(PayType.PAY_BALANCE, p, getString(R.string.app_name) + "账户充值", PreferenceUtils.getInstance().getUsername())));
				startActivity(intent);
				finish();
				break;
			}
		}
	}
}
