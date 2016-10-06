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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.utils.StringTools;

public class DialogProgress extends Dialog implements OnClickListener
{
	private final String title, message;
	private TextView tvTitle, tvMessage;
	private Button btnCancel;
	private LinearLayout linearlayoutButtons;
	private ProgressBar pbWaiting;
	private final LibitDialogProgressListener listener;
	private final boolean showButtons;
	private final int max;

	public DialogProgress(Context context, LibitDialogProgressListener l, String title, String message, int total)
	{
		super(context, R.style.MyDialog);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		listener = l;
		this.title = title;
		this.message = message;
		this.max = total;
		showButtons = true;
	}

	public DialogProgress(Context context, LibitDialogProgressListener l, String title, String message, int total, boolean bButtons)
	{
		// super(context);
		super(context, R.style.MyDialog);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		listener = l;
		this.title = title;
		this.message = message;
		this.max = total;
		showButtons = bButtons;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_progress);
		initView();
	}

	private void initView()
	{
		tvTitle = (TextView) findViewById(R.id.dialog_tv_title);
		tvMessage = (TextView) findViewById(R.id.dialog_tv_message);
		if (!StringTools.isNull(title))
		{
			tvTitle.setText(title);
		}
		if (!StringTools.isNull(message))
		{
			tvMessage.setText(message);
		}
		btnCancel = (Button) findViewById(R.id.dialog_btn_cancel);
		btnCancel.setOnClickListener(this);
		linearlayoutButtons = (LinearLayout) findViewById(R.id.dialog_layout_buttons);
		if (showButtons)
		{
			linearlayoutButtons.setVisibility(View.VISIBLE);
		}
		else
		{
			linearlayoutButtons.setVisibility(View.GONE);
		}
		pbWaiting = (ProgressBar) findViewById(R.id.dialog_progressbar);
		pbWaiting.setMax(max);
	}

	public void setDialogMessage(String message)
	{
		if (tvMessage != null)
			tvMessage.setText(message);
	}

	public void setDialogMessage(int resId)
	{
		if (tvMessage != null)
			tvMessage.setText(this.getContext().getString(resId));
	}

	public void setCancelString(String cancel)
	{
		if (btnCancel != null)
			btnCancel.setText(cancel);
	}

	public void setCancelString(int cancelId)
	{
		if (btnCancel != null)
			btnCancel.setText(cancelId);
	}

	public void updateWaiting(int current)
	{
		pbWaiting.setProgress(current);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.dialog_btn_cancel:
			{
				if (listener != null)
				{
					listener.onCancelClick();
				}
				dismiss();
				break;
			}
		}
	}

	public interface LibitDialogProgressListener
	{
		void onCancelClick();
	}
}
