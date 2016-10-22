/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.services;

import android.app.Instrumentation;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.android.internal.telephony.ITelephony;
import com.lrcall.utils.apptools.AppFactory;

import java.lang.reflect.Method;

public class AutoAnswerIntentService2 extends IntentService
{
	public static final String TAG = "AutoAnswer2";

	public AutoAnswerIntentService2()
	{
		super("AutoAnswerIntentService2");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		// Make sure the phone is still ringing
		TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		int i = 0;
		while (tm.getCallState() == TelephonyManager.CALL_STATE_RINGING)
		{
			if (!AppFactory.isCompatible(9))// 2.3
			{
				Log.d(TAG, "api level is < 9.");
				try
				{
					Thread.sleep(1500);
					answerPhoneAidl(getBaseContext());
					Log.d(TAG, "answerPhoneAidl invoked.");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					Thread.sleep(2000);
				}
				catch (InterruptedException e)
				{
				}
				Log.d(TAG, "api level is >= 9.");
				if (i < 3)
				{
					answerRingingCall();// >2.3
					Log.d(TAG, "answerPhoneHeadsethook invoked.  i=" + i);
				}
				else if (i < 4)
				{
					answer();
					Log.d(TAG, "answer invoked.  i=" + i);
				}
				try
				{
					Thread.sleep(2000);
				}
				catch (InterruptedException e)
				{
				}
				i++;
			}
		}
		this.stopSelf();
	}
	// private void answerPhoneHeadsethook(Context context)
	// {
	// // Simulate a press of the headset button to pick up the call
	// Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
	// buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
	// context.sendOrderedBroadcast(buttonDown, "android.permission.CALL_PRIVILEGED");
	//
	// // froyo and beyond trigger on buttonUp instead of buttonDown
	// Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
	// buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
	// context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
	// }

	private void answer()
	{
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e1)
		{
			// We don't really care
		}
		Instrumentation inst = new Instrumentation();
		try
		{
			inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 40, 230, 0));
			inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 40, 230, 0));
			// Log.d(TAG, "ģ�ⰴ���ѷ���");
		}
		catch (Exception e)
		{
			// Assert.assertTrue("Click can not be completed!", false);
		}
	}

	private void answerRingingCall()
	{
		// 4.1.1�汾��ֹ���Ͳ������¼�
		if (!AppFactory.isCompatible(16))
		{
			Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
			localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			localIntent1.putExtra("state", 1);
			localIntent1.putExtra("microphone", 1);
			localIntent1.putExtra("name", "Headset");
			sendOrderedBroadcast(localIntent1, null);// "android.permission.CALL_PRIVILEGED");
			try
			{
				Thread.sleep(50);
			}
			catch (InterruptedException e)
			{
				// We don't really care
			}
		}
		Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
		KeyEvent keyEvent2 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
		localIntent2.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent2);
		sendOrderedBroadcast(localIntent2, null);// "android.permission.CALL_PRIVILEGED");
		try
		{
			Thread.sleep(50);
		}
		catch (InterruptedException e)
		{
			// We don't really care
		}
		Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
		KeyEvent keyEvent3 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
		localIntent3.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent3);
		sendOrderedBroadcast(localIntent3, null);// "android.permission.CALL_PRIVILEGED");
		try
		{
			Thread.sleep(50);
		}
		catch (InterruptedException e)
		{
			// We don't really care
		}
		if (!AppFactory.isCompatible(16))
		{
			Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
			localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			localIntent4.putExtra("state", 0);
			localIntent4.putExtra("microphone", 1);
			localIntent4.putExtra("name", "Headset");
			sendOrderedBroadcast(localIntent4, null);// "android.permission.CALL_PRIVILEGED");
		}
	}

	@SuppressWarnings("unchecked")
	private void answerPhoneAidl(Context context) throws Exception
	{
		// Set up communication with the telephony service (thanks to Tedd's Droid Tools!)
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		Class c = Class.forName(tm.getClass().getName());
		Method m = c.getDeclaredMethod("getITelephony");
		m.setAccessible(true);
		ITelephony telephonyService;
		telephonyService = (ITelephony) m.invoke(tm);
		// Silence the ringer and answer the call!
		telephonyService.silenceRinger();
		telephonyService.answerRingingCall();
	}
}
