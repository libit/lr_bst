/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by libit on 16/4/13.
 */
public class PicSortInfo
{
	@SerializedName("id")
	private Integer id;
	@SerializedName("sortId")
	private String sortId;
	@SerializedName("name")
	private String name;

	public PicSortInfo()
	{
	}

	public PicSortInfo(String sortId, String name)
	{
		this.sortId = sortId;
		this.name = name;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getSortId()
	{
		return sortId;
	}

	public void setSortId(String sortId)
	{
		this.sortId = sortId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
