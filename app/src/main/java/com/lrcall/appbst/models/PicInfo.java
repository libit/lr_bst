/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by libit on 16/4/13.
 */
public class PicInfo
{
	@SerializedName("picId")
	private String picId;
	@SerializedName("sortId")
	private String sortId;
	@SerializedName("picUrl")
	private String picUrl;
	@SerializedName("picSizeInfo")
	private String picSizeInfo;
	@SerializedName("picSortInfo")
	private PicSortInfo picSortInfo;

	public PicInfo()
	{
	}

	public PicInfo(String picId, String sortId, String picUrl, String picSizeInfo, PicSortInfo picSortInfo)
	{
		this.picId = picId;
		this.sortId = sortId;
		this.picUrl = picUrl;
		this.picSizeInfo = picSizeInfo;
		this.picSortInfo = picSortInfo;
	}

	public String getPicId()
	{
		return picId;
	}

	public void setPicId(String picId)
	{
		this.picId = picId;
	}

	public String getSortId()
	{
		return sortId;
	}

	public void setSortId(String sortId)
	{
		this.sortId = sortId;
	}

	public String getPicUrl()
	{
		return picUrl;
	}

	public void setPicUrl(String picUrl)
	{
		this.picUrl = picUrl;
	}

	public String getPicSizeInfo()
	{
		return picSizeInfo;
	}

	public void setPicSizeInfo(String picSizeInfo)
	{
		this.picSizeInfo = picSizeInfo;
	}

	public PicSortInfo getPicSortInfo()
	{
		return picSortInfo;
	}

	public void setPicSortInfo(PicSortInfo picSortInfo)
	{
		this.picSortInfo = picSortInfo;
	}
}
