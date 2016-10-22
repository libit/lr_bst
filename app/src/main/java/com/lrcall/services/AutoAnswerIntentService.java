/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.services;

import android.app.Instrumentation;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class AutoAnswerIntentService extends IntentService
{
	public static final String TAG = AutoAnswerIntentService.class.getSimpleName();

	public AutoAnswerIntentService()
	{
		super("AutoAnswerIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		Context context = getBaseContext();
		// Make sure the phone is still ringing
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getCallState() != TelephonyManager.CALL_STATE_RINGING)
		{
			return;
		}
		// Answer the phone
		try
		{
			Log.d(TAG, "开始调用反射方法。");
			answerPhoneAidl();
		}
		catch (Exception e1)
		{
			// if(Build.MODEL.equalsIgnoreCase("motorola")||Build.MODEL.equalsIgnoreCase("samsung"))
			// if(Build.MODEL.equalsIgnoreCase("samsung"))
			// {
			// try {
			//
			// answerPhoneHeadsethook(context);
			// } catch (Exception e2) {
			// Log.e("iii","Error trying to answer using telephony service.  Falling back to headset.");
			// try {
			// answerRingingCall(context);
			// } catch (Exception e3) {
			// try {
			// answer();
			// } catch (Exception e4) {
			// }
			// }
			// }
			// }
			// else
			// {
			Log.e(TAG, "调用反射方法失败。");
			try
			{
				answerRingingCall(context);
			}
			catch (Exception e2)
			{
				Log.e(TAG, "answerRingingCall Error.  Falling back to headset.");
				try
				{
					answerPhoneHeadsethook(context);
				}
				catch (Exception e3)
				{
					Log.e(TAG, "answerPhoneHeadsethook Error.");
					try
					{
						answer();
					}
					catch (Exception e4)
					{
						Toast.makeText(context, "已经尽力为您接听了，请手动接听！", Toast.LENGTH_LONG).show();
					}
				}
			}
			// }
		}
		// /Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// long[] pattern = {800, 50, 400, 30}; // OFF/ON/OFF/ON...
		// vibrator.vibrate(500);
	}

	private void enableSpeakerPhone(Context context)
	{
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setSpeakerphoneOn(true);
	}

	private void answerPhoneHeadsethook(Context context) throws Exception
	{
		// Simulate a press of the headset button to pick up the call
		Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
		buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
		context.sendOrderedBroadcast(buttonDown, "android.permission.CALL_PRIVILEGED");
		// froyo and beyond trigger on buttonUp instead of buttonDown
		Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
		buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
		context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
	}

	private void answer() throws Exception
	{
		Instrumentation localInstrumentation = new Instrumentation();
		long l1 = SystemClock.uptimeMillis();
		long l2 = SystemClock.uptimeMillis();
		MotionEvent localMotionEvent1 = MotionEvent.obtain(l1, l2, 0, 40.0F, 230.0F, 0);
		localInstrumentation.sendPointerSync(localMotionEvent1);
		long l3 = SystemClock.uptimeMillis();
		long l4 = SystemClock.uptimeMillis();
		MotionEvent localMotionEvent2 = MotionEvent.obtain(l3, l4, 1, 40.0F, 230.0F, 0);
		localInstrumentation.sendPointerSync(localMotionEvent2);
		localMotionEvent1.recycle();
		localMotionEvent2.recycle();
	}

	public synchronized void answerRingingCall(Context context) throws Exception
	{
		if (context == null)
		{
			return;
		}
		// 据说该方法只能用于Android2.3及2.3以上的版本上 发送 耳机的方式
		Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
		localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		localIntent1.putExtra("state", 1);
		localIntent1.putExtra("microphone", 1);
		localIntent1.putExtra("name", "Headset");
		context.sendOrderedBroadcast(localIntent1, "android.permission.CALL_PRIVILEGED");
		Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
		KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
		localIntent2.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent1);
		context.sendOrderedBroadcast(localIntent2, "android.permission.CALL_PRIVILEGED");
		Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
		KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
		localIntent3.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent2);
		context.sendOrderedBroadcast(localIntent3, "android.permission.CALL_PRIVILEGED");
		Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
		localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		localIntent4.putExtra("state", 0);
		localIntent4.putExtra("microphone", 1);
		localIntent4.putExtra("name", "Headset");
		context.sendOrderedBroadcast(localIntent4, "android.permission.CALL_PRIVILEGED");
	}

	@SuppressWarnings("unchecked")
	private void answerPhoneAidl() throws Exception
	{
		// Set up communication with the telephony service (thanks to Tedd's Droid Tools!)
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		Class c = Class.forName(tm.getClass().getName());
		Method m = c.getDeclaredMethod("getITelephony");
		m.setAccessible(true);
		ITelephony telephonyService = (ITelephony) m.invoke(tm);
		// Silence the ringer and answer the call!
		//		telephonyService.silenceRinger();
		telephonyService.answerRingingCall();
	}
}
