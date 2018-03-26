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
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.BannerInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.BannerService;
import com.lrcall.appbst.services.CallbackService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.enums.AutoAnswerType;
import com.lrcall.enums.ClientBannerType;
import com.lrcall.events.CallLogEvent;
import com.lrcall.models.ContactInfo;
import com.lrcall.services.AutoAnswerIntentService;
import com.lrcall.services.AutoAnswerIntentService2;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ActivityDialWaiting extends MyBaseBannerActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityDialWaiting.class.getSimpleName();
	private ImageView ivHead;
	private TextView tvName, tvNumber, tvResult;
	private CallbackService mCallbackService;
	private String number = "";
	private BannerService mBannerService;

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
		mBannerService = new BannerService(this);
		mBannerService.addDataResponse(this);
		mCallbackService = new CallbackService(this);
		mCallbackService.addDataResponse(this);
		viewInit();
		tvNumber.setText(number);
		tvResult.setText("正在呼叫,请稍后...");
		IntentFilter filter = new IntentFilter("android.intent.action.PHONE_STATE");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(receiver, filter);
		updateView();
		runOnUiThread(new Thread()
		{
			@Override
			public void run()
			{
				super.run();
				mCallbackService.makeCall(number, null, true);
				ContactInfo contactInfo = ContactsFactory.getInstance().getFirstContactInfoByNumber(ActivityDialWaiting.this, number, true);
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
				refreshData();
			}
		});
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
		bannerViewPager = (ViewPager) findViewById(R.id.banner_viewpager);
		bannerViewPagerTab = (SmartTabLayout) findViewById(R.id.banner_viewpagertab);
		setBannerWidthAndHeight(DisplayTools.getWindowWidth(this), DisplayTools.getWindowWidth(this));
		initBannerView();
	}

	//刷新数据
	synchronized public void refreshData()
	{
		mBannerService.getBannerInfoList(ClientBannerType.DIAL_WAITING.getType(), 0, 4, null, true);
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
				tvResult.setText(returnInfo.getMsg());
			}
			else
			{
				String msg = result;
				if (returnInfo != null)
				{
					msg = returnInfo.getMsg();
				}
				tvResult.setText(msg);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_BANNER_LIST))
		{
			List<BannerInfo> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<BannerInfo>>()
				{
				}.getType());
			}
			setViewPagerAdapter(list);
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
			EventBus.getDefault().post(new CallLogEvent(CallLogEvent.EVENT_CALLLOG_ADD));
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
