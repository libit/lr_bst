/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class UpdateInfo
{
	@SerializedName("platform")
	private String platform;
	@SerializedName("versionName")
	private String version;
	@SerializedName("versionCode")
	private Integer versionCode;
	@SerializedName("url")
	private String url;
	@SerializedName("description")
	private String description;
	@SerializedName("addDateLong")
	private Long addDateLong;

	public UpdateInfo()
	{
		super();
	}

	public String getPlatform()
	{
		return platform;
	}

	public void setPlatform(String platform)
	{
		this.platform = platform;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public Integer getVersionCode()
	{
		return versionCode;
	}

	public void setVersionCode(Integer versionCode)
	{
		this.versionCode = versionCode;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Long getAddDateLong()
	{
		return addDateLong;
	}

	public void setAddDateLong(Long addDateLong)
	{
		this.addDateLong = addDateLong;
	}
}
