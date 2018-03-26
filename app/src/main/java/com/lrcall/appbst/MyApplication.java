package com.lrcall.appbst;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.easemob.redpacketsdk.RedPacket;
import com.hyphenate.chatuidemo.DemoHelper;
import com.lrcall.appbst.services.BugService;
import com.lrcall.utils.LogcatTools;

/**
 * Created by libit on 15/8/19.
 */
public class MyApplication extends Application
{
	private static MyApplication instance;

	public static MyApplication getInstance()
	{
		return instance;
	}

	public static Context getContext()
	{
		return getInstance();
	}

	@Override
	public void onCreate()
	{
//		MultiDex.install(this);
		super.onCreate();
		instance = this;
		new Thread("startLogcat")
		{
			@Override
			public void run()
			{
				super.run();
				LogcatTools.getInstance().start();
			}
		}.start();
		//提交日志
		new BugService(this).uploadLogFile(null, true);
		//=====================配置环信===============================
		//init demo helper
		DemoHelper.getInstance().init(this);
		//red packet code : 初始化红包上下文，开启日志输出开关
		RedPacket.getInstance().initContext(this);
		RedPacket.getInstance().setDebugMode(true);
		//end of red packet code
		//=====================配置环信结束=============================
	}

	@Override
	protected void attachBaseContext(Context base)
	{
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	@Override
	public void onTerminate()
	{
		LogcatTools.getInstance().stop();
		instance = null;
		super.onTerminate();
	}
}
