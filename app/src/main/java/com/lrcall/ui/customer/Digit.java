/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.customer;

import android.content.Context;
import android.media.ToneGenerator;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import java.util.HashMap;
import java.util.Map;

public class Digit extends ImageButton implements AddressAware
{
	private static final Map<Integer, int[]> digitsButtons = new HashMap<Integer, int[]>()
	{
		private static final long serialVersionUID = -6640726621906396734L;

		{
			put(0, new int[]{ToneGenerator.TONE_DTMF_0, KeyEvent.KEYCODE_0});
			put(1, new int[]{ToneGenerator.TONE_DTMF_1, KeyEvent.KEYCODE_1});
			put(2, new int[]{ToneGenerator.TONE_DTMF_2, KeyEvent.KEYCODE_2});
			put(3, new int[]{ToneGenerator.TONE_DTMF_3, KeyEvent.KEYCODE_3});
			put(4, new int[]{ToneGenerator.TONE_DTMF_4, KeyEvent.KEYCODE_4});
			put(5, new int[]{ToneGenerator.TONE_DTMF_5, KeyEvent.KEYCODE_5});
			put(6, new int[]{ToneGenerator.TONE_DTMF_6, KeyEvent.KEYCODE_6});
			put(7, new int[]{ToneGenerator.TONE_DTMF_7, KeyEvent.KEYCODE_7});
			put(8, new int[]{ToneGenerator.TONE_DTMF_8, KeyEvent.KEYCODE_8});
			put(9, new int[]{ToneGenerator.TONE_DTMF_9, KeyEvent.KEYCODE_9});
			put(10, new int[]{ToneGenerator.TONE_DTMF_S, KeyEvent.KEYCODE_STAR});
			put(11, new int[]{ToneGenerator.TONE_DTMF_P, KeyEvent.KEYCODE_POUND});
		}
	};
	private AddressText mAddress;
	private DialingFeedback dialFeedback;

	public Digit(Context context, AttributeSet attrs, int style)
	{
		super(context, attrs, style);
		init();
	}

	public Digit(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public Digit(Context context)
	{
		super(context);
		init();
	}

	public void setAddressWidget(AddressText address)
	{
		mAddress = address;
	}

	private void init()
	{
		DialKeyListener lListener = new DialKeyListener();
		setOnClickListener(lListener);
		setOnTouchListener(lListener);
		setOnLongClickListener(lListener);
		setLongClickable(true);
	}

	private void dispatchDialKeyEvent(int buttonId)
	{
		if (dialFeedback == null)
		{
			dialFeedback = new DialingFeedback(this.getContext(), false);
		}
		if (digitsButtons.containsKey(buttonId))
		{
			int[] datas = digitsButtons.get(buttonId);
			dialFeedback.giveFeedback(datas[0]);
		}
	}

	private int convertToInt(final String keyCode)
	{
		final String[] digits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		for (String i : digits)
		{
			if (keyCode.equals(i))
			{
				return Integer.parseInt(i);
			}
		}
		if (keyCode.equals("*"))
		{
			return 10;
		}
		else if (keyCode.equals("#"))
		{
			return 11;
		}
		else
		{
			return 0;
		}
	}

	private class DialKeyListener implements OnClickListener, OnTouchListener, OnLongClickListener
	{
		final CharSequence mKeyCode;

		DialKeyListener()
		{
			mKeyCode = Digit.this.getContentDescription().subSequence(0, 1);
		}

		@Override
		public void onClick(View v)
		{
			if (mAddress != null)
			{
				int lBegin = mAddress.getSelectionStart();
				if (lBegin == -1)
				{
					lBegin = mAddress.length();
				}
				if (lBegin >= 0)
				{
					mAddress.getEditableText().insert(lBegin, mKeyCode);
				}
				dispatchDialKeyEvent(convertToInt(mKeyCode.toString()));
			}
		}

		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			return false;
		}

		@Override
		public boolean onLongClick(View v)
		{
			if (mAddress == null)
				return true;
			int lBegin = mAddress.getSelectionStart();
			if (lBegin == -1)
			{
				lBegin = mAddress.getEditableText().length();
			}
			if (lBegin >= 0)
			{
				if (mKeyCode.toString().equalsIgnoreCase("0"))
				{
					mAddress.getEditableText().insert(lBegin, "+");
				}
				else
				{
					mAddress.getEditableText().insert(lBegin, mKeyCode);
				}
			}
			return true;
		}
	}
}
