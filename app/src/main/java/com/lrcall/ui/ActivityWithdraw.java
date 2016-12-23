/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.UserBankInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UserBankService;
import com.lrcall.appbst.services.WithdrawService;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityWithdraw extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityWithdraw.class.getSimpleName();
	private EditText etAmount, etBankName, etName, etCardId;
	private WithdrawService mWithdrawService;
	private UserBankService mUserBankService;
	private ArrayAdapter spBankCardAdapter;
	private Spinner spBankCard;
	private List<UserBankInfo> mBankCardList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw);
		mWithdrawService = new WithdrawService(this);
		mWithdrawService.addDataResponse(this);
		mUserBankService = new UserBankService(this);
		mUserBankService.addDataResponse(this);
		viewInit();
		mUserBankService.getUserBankInfoList(0, -1, null, null, null, false);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		etAmount = (EditText) findViewById(R.id.et_amount);
		spBankCard = (Spinner) findViewById(R.id.sp_cards);
		etBankName = (EditText) findViewById(R.id.et_bankname);
		etName = (EditText) findViewById(R.id.et_name);
		etCardId = (EditText) findViewById(R.id.et_cardid);
		findViewById(R.id.btn_withdraw).setOnClickListener(this);
		spBankCard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				UserBankInfo userBankInfo = mBankCardList.get(position);
				etBankName.setText(userBankInfo.getBankName());
				etName.setText(userBankInfo.getCardName());
				etCardId.setText(userBankInfo.getCardId());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_withdraw, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_list)
		{
			startActivity(new Intent(this, ActivityWithdrawList.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_withdraw:
			{
				String amount = etAmount.getText().toString();
				String bankName = etBankName.getText().toString();
				String cardName = etName.getText().toString();
				String cardId = etCardId.getText().toString();
				if (StringTools.isNull(amount))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "提现金额不能为空！");
					etAmount.requestFocus();
					return;
				}
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
				int p = 0;
				try
				{
					p = Integer.parseInt(amount);
					if (p <= 0)
					{
						ToastView.showCenterToast(this, R.drawable.ic_do_fail, "提现金额不能小于0！");
						etAmount.requestFocus();
						return;
					}
				}
				catch (Exception e)
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "提现金额错误！");
					etAmount.requestFocus();
					return;
				}
				mWithdrawService.addUserWithdrawInfo(p, bankName, cardName, cardId, "正在申请...", true);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.USER_ADD_WITHDRAW))
		{
			showServerMsg(result, null);
			finish();
			return true;
		}
		else if (url.endsWith(ApiConfig.USER_USER_BANK_LIST))
		{
			List<UserBankInfo> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			List<String> stringList = new ArrayList<>();
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<UserBankInfo>>()
				{
				}.getType());
			}
			if (list != null)
			{
				mBankCardList.clear();
				mBankCardList.addAll(list);
				for (UserBankInfo userBankInfo : list)
				{
					stringList.add(userBankInfo.getBankName() + userBankInfo.getCardId());
				}
				spBankCardAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringList);
				spBankCardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spBankCard.setAdapter(spBankCardAdapter);
			}
		}
		return false;
	}
}
