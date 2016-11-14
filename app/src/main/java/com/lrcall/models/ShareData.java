/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.models;

/**
 * Created by JC on 2016/11/13.
 */
public class ShareData
{
	private String url;
	private String shareUserId;

	public ShareData()
	{
	}

	public ShareData(String url, String shareUserId)
	{
		this.url = url;
		this.shareUserId = shareUserId;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getShareUserId()
	{
		return shareUserId;
	}

	public void setShareUserId(String shareUserId)
	{
		this.shareUserId = shareUserId;
	}
}
