/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.lrcall.appbst.R;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.apptools.AppFactory;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.PermissionUtils;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ActivitySplash extends Activity
{
	//	private TextView tvStart;
	private final int INIT_RESULT = 1001;
	private final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
				case INIT_RESULT:
				{
					if (PreferenceUtils.getInstance().getBooleanValue(PreferenceUtils.IS_FIRST_RUN))
					{
						startActivity(new Intent(ActivitySplash.this, ActivityWelcome.class));
						PreferenceUtils.getInstance().setBooleanValue(PreferenceUtils.IS_FIRST_RUN, false);
					}
					else
					{
						startActivity(new Intent(ActivitySplash.this, ActivityMain.class));
					}
					finish();
					break;
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		final View rootView = View.inflate(this, R.layout.activity_splash, null);
		setContentView(rootView);
		if (AppFactory.isCompatible(23))
		{
			ActivitySplashPermissionsDispatcher.initViewWithCheck(this, rootView);
		}
		else
		{
			initView(rootView);
		}
	}

	@NeedsPermission({Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.FLASHLIGHT, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE})
	protected void initView(View rootView)
	{
		//		tvStart = (TextView) findViewById(R.id.tv_start);
		AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
		aa.setDuration(200);
		rootView.setAnimation(aa);
		aa.setAnimationListener(new Animation.AnimationListener()
		{
			@Override
			public void onAnimationStart(Animation animation)
			{
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				mHandler.sendEmptyMessage(INIT_RESULT);
			}
		});
	}

	@OnPermissionDenied({Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.FLASHLIGHT, Manifest.permission.ACCESS_COARSE_LOCATION})
	void initViewDenied()
	{
		Toast.makeText(this, "您拒绝了应用所需的权限，应用将不能工作！", Toast.LENGTH_LONG).show();
		finish();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// NOTE: delegate the permission handling to generated method
		ActivitySplashPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
		if (PermissionUtils.verifyPermissions(grantResults))
		{
			mHandler.sendEmptyMessage(INIT_RESULT);
		}
		else
		{
			initViewDenied();
		}
	}
}
