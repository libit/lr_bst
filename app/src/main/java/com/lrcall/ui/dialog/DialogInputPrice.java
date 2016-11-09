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
import android.widget.EditText;

import com.lrcall.appbst.R;
import com.lrcall.utils.StringTools;

public class DialogInputPrice extends Dialog implements OnClickListener
{
	private EditText etContent;
	private final OnInputListenser listenser;

	public DialogInputPrice(Context context, OnInputListenser listenser)
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
		setContentView(R.layout.dialog_input_price);
		initView();
	}

	private void initView()
	{
		etContent = (EditText) findViewById(R.id.et_content);
		findViewById(R.id.dialog_btn_ok).setOnClickListener(this);
		findViewById(R.id.dialog_btn_cancel).setOnClickListener(this);
	}

	public void setDefaultContent(String content)
	{
		etContent.setText(content);
		if (!StringTools.isNull(content))
		{
			etContent.setSelection(content.length());
		}
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
					listenser.onOkClick(etContent.getText().toString());
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

	public interface OnInputListenser
	{
		void onOkClick(String content);

		void onCancelClick();
	}
}
