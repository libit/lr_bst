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
import com.lrcall.enums.CallbackLineType;
import com.lrcall.utils.PreferenceUtils;

public class DialogSettingCallbackLine extends Dialog implements OnClickListener, RadioGroup.OnCheckedChangeListener
{
	private RadioGroup rgCallbackLine;
	private int line;
	private final DialogCommon.LibitDialogListener listenser;
	private final int type_id1 = R.id.rb_line1;
	private final int type_id2 = R.id.rb_line2;

	public DialogSettingCallbackLine(Context context, DialogCommon.LibitDialogListener listenser)
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
		setContentView(R.layout.dialog_setting_callback_line);
		initView();
	}

	private void initView()
	{
		rgCallbackLine = (RadioGroup) findViewById(R.id.rg_callback_line);
		findViewById(R.id.dialog_btn_ok).setOnClickListener(this);
		findViewById(R.id.dialog_btn_cancel).setOnClickListener(this);
		line = PreferenceUtils.getInstance().getIntegerValue(PreferenceUtils.PREF_CALLBACK_LINE_KEY);
		if (line == CallbackLineType.LINE_1.getType())
		{
			rgCallbackLine.check(type_id1);
		}
		else if (line == CallbackLineType.LINE_2.getType())
		{
			rgCallbackLine.check(type_id2);
		}
		rgCallbackLine.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.dialog_btn_ok:
			{
				PreferenceUtils.getInstance().setStringValue(PreferenceUtils.PREF_CALLBACK_LINE_KEY, line + "");
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
		if (group == rgCallbackLine)
		{
			switch (checkedId)
			{
				case type_id1:
				{
					line = CallbackLineType.LINE_1.getType();
					break;
				}
				case type_id2:
				{
					line = CallbackLineType.LINE_2.getType();
					break;
				}
			}
		}
	}
}
