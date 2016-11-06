/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.CallbackService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.enums.AutoAnswerType;
import com.lrcall.models.ContactInfo;
import com.lrcall.services.AutoAnswerIntentService;
import com.lrcall.services.AutoAnswerIntentService2;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;

public class ActivityDialWaiting extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityDialWaiting.class.getSimpleName();
	private ImageView ivHead;
	private TextView tvName, tvNumber, tvResult;
	private CallbackService mCallbackService;
	private String number = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dial_waiting);
		number = getIntent().getStringExtra(ConstValues.DATA_NUMBER);
		if (StringTools.isNull(number))
		{
			finish();
			Toast.makeText(this, "呼叫号码为空!", Toast.LENGTH_LONG).show();
		}
		mCallbackService = new CallbackService(this);
		mCallbackService.addDataResponse(this);
		viewInit();
		tvNumber.setText(number);
		ContactInfo contactInfo = ContactsFactory.getInstance().getFirstContactInfoByNumber(this, number, true);
		String name = "陌生号码";
		if (contactInfo != null)
		{
			name = contactInfo.getName();
			if (contactInfo.getContactPhoto() != null)
			{
				ivHead.setImageBitmap(contactInfo.getContactPhoto());
			}
		}
		tvName.setText(name);
		tvResult.setText("正在呼叫,请稍后...");
		mCallbackService.makeCall(number, null, true);
		IntentFilter filter = new IntentFilter("android.intent.action.PHONE_STATE");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(receiver, filter);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		ivHead = (ImageView) findViewById(R.id.iv_head);
		tvName = (TextView) findViewById(R.id.tv_name);
		tvNumber = (TextView) findViewById(R.id.tv_number);
		tvResult = (TextView) findViewById(R.id.tv_result);
		findViewById(R.id.btn_end_call).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_end_call:
			{
				finish();
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.CALLBACK_MAKE_CALL))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				tvResult.setText(returnInfo.getErrmsg());
			}
			else
			{
				String msg = result;
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				tvResult.setText("呼叫失败：" + msg);
			}
			return true;
		}
		return false;
	}

	private BroadcastReceiver receiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
			if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) // 来电
			{
				int answerType = PreferenceUtils.getInstance().getIntegerValue(PreferenceUtils.PREF_CALLBACK_AUTO_ANSWER_KEY);
				if (answerType == AutoAnswerType.AUTO_ANSWER1.getType())
				{
					context.startService(new Intent(context, AutoAnswerIntentService.class));
				}
				else if (answerType == AutoAnswerType.AUTO_ANSWER2.getType())
				{
					context.startService(new Intent(context, AutoAnswerIntentService2.class));
				}
			}
			else if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)) // 接通
			{
				//				CallLogsFactory.deletePrivateCallLog(context);
			}
			ActivityDialWaiting.this.finish();
		}
	};

	@Override
	protected void onDestroy()
	{
		unregisterReceiver(receiver);
		super.onDestroy();
	}
}
