/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by libit on 16/4/12.
 */
public class ReturnListInfo extends ReturnInfo
{
	@SerializedName("count")
	protected long count;
	@SerializedName("totalCount")
	protected long totalCount;

	public ReturnListInfo()
	{
		super();
	}

	public ReturnListInfo(Integer errcode, String errmsg)
	{
		super(errcode, errmsg);
	}

	public ReturnListInfo(ReturnInfo returnInfo)
	{
		super(returnInfo.getCode(), returnInfo.getMsg());
		this.count = 0;
		this.totalCount = 0;
	}

	public ReturnListInfo(ReturnInfo returnInfo, long count, long totalCount)
	{
		super(returnInfo.getCode(), returnInfo.getMsg());
		this.count = count;
		this.totalCount = totalCount;
	}

	public ReturnListInfo(Integer errcode, String errmsg, long count, long totalCount)
	{
		super(errcode, errmsg);
		this.count = count;
		this.totalCount = totalCount;
	}

	public long getCount()
	{
		return count;
	}

	public void setCount(long count)
	{
		this.count = count;
	}

	public long getTotalCount()
	{
		return totalCount;
	}

	public void setTotalCount(long totalCount)
	{
		this.totalCount = totalCount;
	}
}
