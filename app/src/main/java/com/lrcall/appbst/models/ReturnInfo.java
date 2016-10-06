/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;
import com.lrcall.utils.GsonTools;

public class ReturnInfo
{
	@SerializedName("errcode")
	private Integer errcode;
	@SerializedName("errmsg")
	private String errmsg;

	public ReturnInfo()
	{
		super();
	}

	public ReturnInfo(Integer errcode, String errmsg)
	{
		super();
		this.errcode = errcode;
		this.errmsg = errmsg;
	}

	public static boolean isSuccess(ReturnInfo info)
	{
		return (info != null && info.getErrcode() != null && info.getErrcode() == 0);
	}

	public Integer getErrcode()
	{
		return errcode;
	}

	public void setErrcode(Integer errcode)
	{
		this.errcode = errcode;
	}

	public String getErrmsg()
	{
		return errmsg;
	}

	public void setErrmsg(String errmsg)
	{
		this.errmsg = errmsg;
	}

	@Override
	public String toString()
	{
		return GsonTools.toJson(this);
	}
}
