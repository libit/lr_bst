package com.lrcall.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.lrcall.appbst.MyApplication;
import com.lrcall.enums.AutoAnswerType;
import com.lrcall.enums.CallbackLineType;
import com.lrcall.enums.LogLevel;
import com.lrcall.enums.ModType;
import com.lrcall.enums.PicQuality;

import java.util.HashMap;
import java.util.Map;

public class PreferenceUtils
{
	private static final String TAG = PreferenceUtils.class.getName();
	public static final String PREF_USERNAME_KEY = "userId";//用户ID
	public static final String PREF_SESSION_ID_KEY = "session_id";// 会话ID
	public static final String PREF_CRASH_FILE = "crash_file_name";//崩溃日志名
	public static final String LOGCAT_LEVEL = "logcat_level";//日志记录级别
	public static final String LOGCAT_AUTO_UPDATE = "logcat_auto_update";//自动上传日志
	public static final String IS_FIRST_RUN = "is_first_run";//是否第一次运行
	public static final String IS_SHOW_NOTIFICATION = "is_show_notification";//是否显示系统通知栏
	public static final String DIAL_PRESS_TONE_MODE = "dial_press_tone_mode";//拨号按键铃音模式
	public static final String DIAL_PRESS_VIBRATE_MODE = "dial_press_vibrate_mode";//拨号按键震动模式
	public static final String PIC_QUALITY = "pic_quality";//图片质量
	public static final String WX_APP_ID = "wx_app_id";//微信支付ID
	public static final String IS_DEBUG = "is_debug";//是否调试
	public static final String CLIENT_CONFIG = "client_config";// 客户端配置
	public static final String PREF_CALLBACK_AUTO_ANSWER_KEY = "pref_callback_answer_key";//自动接听
	public static final String PREF_CALLBACK_LINE_KEY = "pref_callback_line_key";//语音通道
	public static final String PREF_LAST_READ_NEWS_TIME = "pref_last_read_news_time";// 上次阅读新闻的时间
	private final static HashMap<String, String> STRING_PREFS = new HashMap<String, String>()
	{
		private static final long serialVersionUID = 1L;

		{
			put(PREF_USERNAME_KEY, "");
			put(PREF_SESSION_ID_KEY, "");
			put(PREF_CRASH_FILE, "");
			put(LOGCAT_LEVEL, LogLevel.LEVEL_3.getLevel() + "");
			put(DIAL_PRESS_TONE_MODE, ModType.GENERIC_TYPE_PREVENT.getType() + "");
			put(DIAL_PRESS_VIBRATE_MODE, ModType.GENERIC_TYPE_PREVENT.getType() + "");
			put(PIC_QUALITY, PicQuality.HIGH.getLevel() + "");
			put(WX_APP_ID, "");
			put(CLIENT_CONFIG, "");
			put(PREF_CALLBACK_AUTO_ANSWER_KEY, AutoAnswerType.ANSWER_USER.getType() + "");
			put(PREF_CALLBACK_LINE_KEY, CallbackLineType.LINE_1.getType() + "");
			put(PREF_LAST_READ_NEWS_TIME, "");
		}
	};
	private final static HashMap<String, Boolean> BOOLEAN_PREFS = new HashMap<String, Boolean>()
	{
		private static final long serialVersionUID = 2L;

		{
			put(LOGCAT_AUTO_UPDATE, true);
			put(IS_FIRST_RUN, true);
			put(IS_SHOW_NOTIFICATION, true);
			put(IS_DEBUG, true);
		}
	};
	protected static PreferenceUtils instance = null;
	private final SharedPreferences prefs;
	private final Context context;

	protected PreferenceUtils()
	{
		context = MyApplication.getContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static PreferenceUtils getInstance()
	{
		if (instance == null)
		{
			synchronized (PreferenceUtils.class)
			{
				if (instance == null)
				{
					instance = new PreferenceUtils();
				}
			}
		}
		return instance;
	}

	private static String gPrefStringValue(SharedPreferences aPrefs, String key)
	{
		if (STRING_PREFS.containsKey(key))
		{
			return aPrefs.getString(key, STRING_PREFS.get(key));
		}
		// return null;
		return "";
	}

	private static Boolean gPrefBooleanValue(SharedPreferences aPrefs, String key)
	{
		if (BOOLEAN_PREFS.containsKey(key))
		{
			return aPrefs.getBoolean(key, BOOLEAN_PREFS.get(key));
		}
		return false;
	}

	public String getUsername()
	{
		return getStringValue(PREF_USERNAME_KEY);
	}

	public void setUsername(String value)
	{
		setStringValue(PREF_USERNAME_KEY, value);
	}

	public String getSessionId()
	{
		return getStringValue(PREF_SESSION_ID_KEY);
	}

	public void setSessionId(String value)
	{
		setStringValue(PREF_SESSION_ID_KEY, value);
	}

	public void setStringValue(String key, String value)
	{
		Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.apply();
	}

	public String getStringValue(String key)
	{
		return gPrefStringValue(prefs, key);
	}

	public void setBooleanValue(String key, Boolean value)
	{
		Editor editor = prefs.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}

	public Boolean getBooleanValue(String key)
	{
		return gPrefBooleanValue(prefs, key);
	}

	public int getIntegerValue(String key)
	{
		try
		{
			return Integer.parseInt(getStringValue(key));
		}
		catch (NumberFormatException e)
		{
			Log.e(TAG, "Invalid " + key + " format : expect a int");
		}
		try
		{
			return Integer.parseInt(STRING_PREFS.get(key));
		}
		catch (NumberFormatException e)
		{
			Log.e(TAG, "Invalid " + key + " format : expect a int");
		}
		return -1;
	}

	/**
	 * Set all values to default
	 */
	public void resetAllDefaultValues()
	{
		for (String key : STRING_PREFS.keySet())
		{
			setStringValue(key, STRING_PREFS.get(key));
		}
		for (String key : BOOLEAN_PREFS.keySet())
		{
			setBooleanValue(key, BOOLEAN_PREFS.get(key));
		}
	}

	/**
	 * 备份数据
	 *
	 * @return
	 */
	public String backup()
	{
		Map<String, String> map = new HashMap<>();
		for (String key : STRING_PREFS.keySet())
		{
			map.put(key, getStringValue(key));
		}
		for (String key : BOOLEAN_PREFS.keySet())
		{
			setBooleanValue(key, BOOLEAN_PREFS.get(key));
			map.put(key, getBooleanValue(key).toString());
		}
		return GsonTools.toJson(map);
	}

	// ----
	// UI related
	// ----
	public boolean dialPressTone()
	{
		int mode = getIntegerValue(DIAL_PRESS_TONE_MODE);
		if (mode == ModType.GENERIC_TYPE_AUTO.getType())
		{
			return Settings.System.getInt(context.getContentResolver(), Settings.System.DTMF_TONE_WHEN_DIALING, 1) == 1;
		}
		else if (mode == ModType.GENERIC_TYPE_FORCE.getType())
		{
			return true;
		}
		else if (mode == ModType.GENERIC_TYPE_PREVENT.getType())
		{
			return false;
		}
		return false;
	}

	public boolean dialPressVibrate()
	{
		int mode = getIntegerValue(DIAL_PRESS_VIBRATE_MODE);
		if (mode == ModType.GENERIC_TYPE_AUTO.getType())
		{
			return Settings.System.getInt(context.getContentResolver(), Settings.System.HAPTIC_FEEDBACK_ENABLED, 1) == 1;
		}
		else if (mode == ModType.GENERIC_TYPE_FORCE.getType())
		{
			return true;
		}
		else if (mode == ModType.GENERIC_TYPE_PREVENT.getType())
		{
			return false;
		}
		return false;
	}
}
