/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.customer;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.utils.DisplayTools;

/**
 * Created by libit on 16/7/21.
 */
public class FloatProductSelectCount implements View.OnClickListener
{
	protected final Activity mActivity;
	protected final Context mContext;
	protected View rootView;
	protected final WindowManager windowManager;
	protected final WindowManager.LayoutParams params;
	private EditText etCount;
	private TextView tvCount;

	public FloatProductSelectCount(Activity activity, TextView tvCount)
	{
		super();
		this.mActivity = activity;
		this.mContext = activity;
		this.tvCount = tvCount;
		windowManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		params = new WindowManager.LayoutParams();
	}

	public void viewInit()
	{
		rootView = LayoutInflater.from(mContext).inflate(R.layout.float_product_select_count, null);
		etCount = (EditText) rootView.findViewById(R.id.et_count);
		rootView.setOnClickListener(this);
		rootView.findViewById(R.id.btn_close).setOnClickListener(this);
		rootView.findViewById(R.id.btn_add).setOnClickListener(this);
		rootView.findViewById(R.id.btn_sub).setOnClickListener(this);
		etCount.setText(tvCount.getText());
	}

	/**
	 * @Method: showTopWindow
	 * @Description: 显示最顶层view
	 */
	public void showTopWindow()
	{
		viewInit();
		// window管理器
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
		params.flags = params.flags | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
		params.flags = params.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
		//		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;//WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
		// 设置全屏显示 可以根据自己需要设置大小
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.BOTTOM;
		// 设置显示初始位置 屏幕左上角为原点
		int intX = 0, intY = 0;
		params.x = intX;
		params.y = intY;
		params.format = PixelFormat.TRANSPARENT;
		// topWindow显示到最顶部
		windowManager.addView(rootView, params);
		final int windowHeight = DisplayTools.getWindowHeight(mContext);
		rootView.setOnTouchListener(new View.OnTouchListener()
		{
			private float lastY = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				int action = event.getAction();
				//				LogcatTools.debug("DraftImageView", "action:" + action + ",lastY:" + lastY + ".");
				if (action == MotionEvent.ACTION_DOWN)
				{
					lastY = event.getRawY();
					//					LogcatTools.debug("DraftImageView", "ACTION_DOWN lastY:" + lastY + ".");
					//					showList();
					//					return true;
				}
				else if (action == MotionEvent.ACTION_UP)
				{
					float dy = lastY - event.getRawY();
					lastY = event.getRawY();
					//					LogcatTools.debug("DraftImageView", "ACTION_UP lastY:" + lastY + ",dy:" + dy + ".");
					if (dy > windowHeight / 8)
					{
					}
					else if (dy < -(2))
					{
					}
					else
					{
						return false;
					}
					return true;
				}
				else if (action == MotionEvent.ACTION_OUTSIDE)
				{
					//					LogcatTools.debug("DraftImageView", "ACTION_OUTSIDE,lastY:" + lastY + ".");
					clearTopWindow();
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * @Method: clearTopWindow
	 * @Description: 移除最顶层view
	 */
	public void clearTopWindow()
	{
		if (rootView != null && rootView.isShown())
		{
			windowManager.removeView(rootView);
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_close:
			{
				clearTopWindow();
				break;
			}
			case R.id.btn_add:
			{
				String count = etCount.getText().toString();
				int c = 1;
				try
				{
					c = Integer.parseInt(count);
					c++;
				}
				catch (Exception e)
				{
				}
				etCount.setText(c + "");
				if (tvCount != null)
				{
					tvCount.setText(c + "");
				}
				break;
			}
			case R.id.btn_sub:
			{
				String count = etCount.getText().toString();
				int c = 1;
				try
				{
					c = Integer.parseInt(count);
					if (c <= 1)
					{
						c = 1;
					}
					else
					{
						c--;
					}
				}
				catch (Exception e)
				{
				}
				etCount.setText(c + "");
				if (tvCount != null)
				{
					tvCount.setText(c + "");
				}
				break;
			}
			default:
			{
			}
		}
	}
}
