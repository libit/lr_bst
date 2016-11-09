package com.lrcall.appbst;

import android.app.Application;
import android.content.Context;

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
		super.onCreate();
		instance = this;
		new Thread("splash")
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
	}

	@Override
	public void onTerminate()
	{
		LogcatTools.getInstance().stop();
		instance = null;
		super.onTerminate();
	}
	//	public String sHA1(Context context)
	//	{
	//		try
	//		{
	//			PackageInfo info = getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
	//			byte[] cert = info.signatures[0].toByteArray();
	//			MessageDigest md = MessageDigest.getInstance("SHA1");
	//			byte[] publicKey = md.digest(cert);
	//			StringBuilder hexString = new StringBuilder();
	//			for (int i = 0; i < publicKey.length; i++)
	//			{
	//				String appendString = Integer.toHexString(0xFF & publicKey[i]).toUpperCase(Locale.US);
	//				if (appendString.length() == 1)
	//					hexString.append("0");
	//				hexString.append(appendString);
	//				hexString.append(":");
	//			}
	//			return hexString.toString();
	//		}
	//		catch (PackageManager.NameNotFoundException e)
	//		{
	//			e.printStackTrace();
	//		}
	//		catch (NoSuchAlgorithmException e)
	//		{
	//			e.printStackTrace();
	//		}
	//		return null;
	//	}
}
