/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by libit on 2016/11/30.
 */
public class ClientIndexFuncInfo
{
	@SerializedName("funcId")
	private String funcId;
	@SerializedName("picId")
	private String picId;
	@SerializedName("name")
	private String name;
	@SerializedName("content")
	private String content;

	public ClientIndexFuncInfo()
	{
	}

	public ClientIndexFuncInfo(String funcId, String picId, String name, String content)
	{
		this.funcId = funcId;
		this.picId = picId;
		this.name = name;
		this.content = content;
	}

	public String getFuncId()
	{
		return funcId;
	}

	public void setFuncId(String funcId)
	{
		this.funcId = funcId;
	}

	public String getPicId()
	{
		return picId;
	}

	public void setPicId(String picId)
	{
		this.picId = picId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
}
