/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;
import com.lrcall.utils.GsonTools;

public class ReturnInfo
{
	@SerializedName("code")
	private int code;
	@SerializedName("msg")
	private String msg;

	public ReturnInfo()
	{
		super();
	}

	public ReturnInfo(int code, String msg)
	{
		super();
		this.code = code;
		this.msg = msg;
	}

	public static boolean isSuccess(ReturnInfo info)
	{
		if (info != null && info.getCode() == ErrorInfo.getSuccessId())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	@Override
	public String toString()
	{
		return GsonTools.toJson(this);
	}
}
