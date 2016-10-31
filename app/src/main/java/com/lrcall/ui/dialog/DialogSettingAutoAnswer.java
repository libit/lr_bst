/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioGroup;

import com.lrcall.appbst.R;
import com.lrcall.enums.AutoAnswerType;
import com.lrcall.utils.PreferenceUtils;

public class DialogSettingAutoAnswer extends Dialog implements OnClickListener, RadioGroup.OnCheckedChangeListener
{
	private RadioGroup rgCallback;
	private int type;
	private final DialogCommon.LibitDialogListener listenser;
	private final int type_id1 = R.id.rb_answer1;
	private final int type_id2 = R.id.rb_answer2;
	private final int type_id0 = R.id.rb_answer_user;

	public DialogSettingAutoAnswer(Context context, DialogCommon.LibitDialogListener listenser)
	{
		super(context, R.style.MyDialog);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		this.listenser = listenser;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_setting_auto_answer);
		initView();
	}

	private void initView()
	{
		rgCallback = (RadioGroup) findViewById(R.id.rg_callback_answer);
		findViewById(R.id.dialog_btn_ok).setOnClickListener(this);
		findViewById(R.id.dialog_btn_cancel).setOnClickListener(this);
		type = PreferenceUtils.getInstance().getIntegerValue(PreferenceUtils.PREF_CALLBACK_AUTO_ANSWER_KEY);
		if (type == AutoAnswerType.AUTO_ANSWER1.getType())
		{
			rgCallback.check(type_id1);
		}
		else if (type == AutoAnswerType.AUTO_ANSWER2.getType())
		{
			rgCallback.check(type_id2);
		}
		else
		{
			rgCallback.check(type_id0);
		}
		rgCallback.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.dialog_btn_ok:
			{
				PreferenceUtils.getInstance().setStringValue(PreferenceUtils.PREF_CALLBACK_AUTO_ANSWER_KEY, type + "");
				if (listenser != null)
				{
					listenser.onOkClick();
				}
				dismiss();
				break;
			}
			case R.id.dialog_btn_cancel:
			{
				if (listenser != null)
				{
					listenser.onCancelClick();
				}
				dismiss();
				break;
			}
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		if (group == rgCallback)
		{
			switch (checkedId)
			{
				case type_id1:
				{
					type = AutoAnswerType.AUTO_ANSWER1.getType();
					break;
				}
				case type_id2:
				{
					type = AutoAnswerType.AUTO_ANSWER2.getType();
					break;
				}
				case type_id0:
				{
					type = AutoAnswerType.ANSWER_USER.getType();
					break;
				}
			}
		}
	}
}
