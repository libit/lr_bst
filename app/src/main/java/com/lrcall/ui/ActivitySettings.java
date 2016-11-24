/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.UpdateService;
import com.lrcall.enums.AutoAnswerType;
import com.lrcall.enums.CallbackLineType;
import com.lrcall.ui.dialog.DialogCommon;
import com.lrcall.ui.dialog.DialogSettingAutoAnswer;
import com.lrcall.ui.dialog.DialogSettingBugLevel;
import com.lrcall.ui.dialog.DialogSettingCallbackLine;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.apptools.AppFactory;

public class ActivitySettings extends MyBaseActivity implements View.OnClickListener
{
	private ImageView ivIsDebug;
	private TextView tvVersion, tvCallbackLine, tvAutoAnswerStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		viewInit();
		initData();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		ivIsDebug = (ImageView) findViewById(R.id.app_setting_debug_value);
		tvVersion = (TextView) findViewById(R.id.app_setting_current_version);
		tvVersion.setText(AppFactory.getInstance().getVersionName());
		tvCallbackLine = (TextView) findViewById(R.id.tv_choose_callback_line);
		tvAutoAnswerStatus = (TextView) findViewById(R.id.tv_auto_answer_status);
		findViewById(R.id.layout_debug).setOnClickListener(this);
		findViewById(R.id.layout_about).setOnClickListener(this);
		findViewById(R.id.layout_choose_callback_line).setOnClickListener(this);
		findViewById(R.id.layout_auto_answer).setOnClickListener(this);
		findViewById(R.id.layout_advice).setOnClickListener(this);
		findViewById(R.id.layout_bug_level).setOnClickListener(this);
		findViewById(R.id.layout_update).setOnClickListener(this);
		findViewById(R.id.layout_more_app).setOnClickListener(this);
		findViewById(R.id.layout_share).setOnClickListener(this);
	}

	private void initData()
	{
		if (PreferenceUtils.getInstance().getBooleanValue(PreferenceUtils.IS_DEBUG))
		{
			ivIsDebug.setImageResource(R.drawable.btn_checked);
		}
		else
		{
			ivIsDebug.setImageResource(R.drawable.btn_nocheck);
		}
		int callbackLine = PreferenceUtils.getInstance().getIntegerValue(PreferenceUtils.PREF_CALLBACK_LINE_KEY);
		tvCallbackLine.setText(CallbackLineType.getTypeDesc(callbackLine));
		int answerType = PreferenceUtils.getInstance().getIntegerValue(PreferenceUtils.PREF_CALLBACK_AUTO_ANSWER_KEY);
		tvAutoAnswerStatus.setText(AutoAnswerType.getTypeDesc(answerType));
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.layout_debug:
			{
				if (PreferenceUtils.getInstance().getBooleanValue(PreferenceUtils.IS_DEBUG))
				{
					PreferenceUtils.getInstance().setBooleanValue(PreferenceUtils.IS_DEBUG, false);
					ivIsDebug.setImageResource(R.drawable.btn_nocheck);
				}
				else
				{
					PreferenceUtils.getInstance().setBooleanValue(PreferenceUtils.IS_DEBUG, true);
					ivIsDebug.setImageResource(R.drawable.btn_checked);
				}
				break;
			}
			case R.id.layout_about:
			{
				ActivityWebView.startWebActivity(this, "关于我们", ApiConfig.getServerAboutUrl());
				break;
			}
			case R.id.layout_choose_callback_line:
			{
				new DialogSettingCallbackLine(this, new DialogCommon.LibitDialogListener()
				{
					@Override
					public void onOkClick()
					{
						initData();
					}

					@Override
					public void onCancelClick()
					{
					}
				}).show();
				break;
			}
			case R.id.layout_auto_answer:
			{
				new DialogSettingAutoAnswer(this, new DialogCommon.LibitDialogListener()
				{
					@Override
					public void onOkClick()
					{
						initData();
					}

					@Override
					public void onCancelClick()
					{
					}
				}).show();
				break;
			}
			case R.id.layout_advice:
			{
				startActivity(new Intent(this, ActivityAdvice.class));
				break;
			}
			case R.id.layout_bug_level:
			{
				new DialogSettingBugLevel(this, null).show();
				break;
			}
			case R.id.layout_update:
			{
				new UpdateService(this).checkUpdate("正在检查更新...", true);
				break;
			}
			case R.id.layout_more_app:
			{
				ActivityWebView.startWebActivity(this, "更多应用", ApiConfig.getServerMoreAppUrl());
				break;
			}
			case R.id.layout_share:
			{
				Intent intent = new Intent(this, ActivityShare.class);
				//				String data = GsonTools.toJson(new ShareData(ApiConfig.getServerRegisterUrl(PreferenceUtils.getInstance().getUserId()), PreferenceUtils.getInstance().getUserId()));
				String data = ApiConfig.getServerRegisterUrl(PreferenceUtils.getInstance().getUserId());
				intent.putExtra(ConstValues.DATA_SHARE_DATA, data);
				startActivity(intent);
				break;
			}
		}
	}
}
