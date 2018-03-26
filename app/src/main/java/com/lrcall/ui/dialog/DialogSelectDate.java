/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2017.
 */
package com.lrcall.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;

import com.lrcall.appbst.R;

public class DialogSelectDate extends Dialog implements OnClickListener
{
	private DatePicker mDatePicker;
	private final OnSelectDateListenser listenser;

	public DialogSelectDate(Context context, OnSelectDateListenser listenser)
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
		setContentView(R.layout.dialog_select_date);
		initView();
	}

	private void initView()
	{
		mDatePicker = (DatePicker) findViewById(R.id.datePicker);
		findViewById(R.id.dialog_btn_ok).setOnClickListener(this);
		findViewById(R.id.dialog_btn_cancel).setOnClickListener(this);
	}

	public void setDate(long minDate, long maxDate)
	{
		mDatePicker.setMinDate(minDate);
		mDatePicker.setMaxDate(maxDate);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.dialog_btn_ok:
			{
				if (listenser != null)
				{
					listenser.onOkClick(mDatePicker);
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

	public interface OnSelectDateListenser
	{
		void onOkClick(DatePicker datePicker);

		void onCancelClick();
	}
}
