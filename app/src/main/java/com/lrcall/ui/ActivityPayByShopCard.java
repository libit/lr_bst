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
import com.lrcall.appbst.models.PayTypeInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PayService;
import com.lrcall.enums.PayType;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

public class ActivityPayByShopCard extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityPayByShopCard.class.getSimpleName();
	private EditText etCardId, etCardPwd;
	private PayService mPayService;
	private PayTypeInfo payTypeInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_by_shop_card);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			String params = bundle.getString(ConstValues.DATA_PAY_TYPE_INFO);
			payTypeInfo = GsonTools.getObject(params, PayTypeInfo.class);
			if (payTypeInfo == null)
			{
				finish();
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, "支付信息为空！");
				return;
			}
		}
		mPayService = new PayService(this);
		mPayService.addDataResponse(this);
		viewInit();
		initData();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		etCardId = (EditText) findViewById(R.id.et_card_id);
		etCardPwd = (EditText) findViewById(R.id.et_card_pwd);
		findViewById(R.id.btn_pay).setOnClickListener(this);
	}

	private void initData()
	{
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_pay:
			{
				String cardId = etCardId.getText().toString();
				String cardPwd = etCardPwd.getText().toString();
				if (StringTools.isNull(cardId))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "卡号不能为空！");
					etCardId.requestFocus();
					return;
				}
				if (StringTools.isNull(cardPwd))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "卡密不能为空！");
					etCardPwd.requestFocus();
					return;
				}
				if (payTypeInfo.getPayType().getType().equals(PayType.PAY_UPGRADE.getType()))
				{
					mPayService.payUserUpgradeByShopCard(cardId, cardPwd, "请稍后...", true);
				}
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.USER_UPGRADE_BY_SHOP_CARD))
		{
			showServerMsg(result, "支付成功！");
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
