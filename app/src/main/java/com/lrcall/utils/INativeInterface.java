package com.lrcall.utils;

import java.util.HashMap;

public class INativeInterface
{
	// 加载so
	static
	{
		System.loadLibrary("appbstsdk");
	}

	// so调用的HTTP GET方法
	public static String doGet(String url)
	{
		return HttpTools.doGet(url, "utf-8");
	}

	// so调用的HTTP POST方法
	public static String doPost(String url, String params)
	{
		// [key,value];[key,value];...
		HashMap<String, String> map = new HashMap<>();
		String[] param = params.split(";");
		if (param.length > 0)
		{
			for (String str : param)
			{
				if (str.indexOf("[") > -1 && str.indexOf(",") > str.indexOf("[") && str.indexOf("]") > str.indexOf(","))
				{
					String key = str.substring(str.indexOf("[") + 1, str.indexOf(","));
					String value = str.substring(str.indexOf(",") + 1, str.indexOf("]"));
					map.put(key, value);
				}
			}
		}
		return HttpTools.doPost(url, map);
	}

	public native String getUrl(String key);

	public native String login(String number, String pwd, String agentId, String signKey, String key);

	public native String getUpdate(String number, String pwd, String agentId, String signedData, String platformInfo, String key);
}
