/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

/**
 * 定义客户端可操作的类型
 * Created by libit on 2016/11/30.
 */
public class ClientFuncType
{
	private String type;//操作类型
	private String content;//内容

	public ClientFuncType()
	{
	}

	public ClientFuncType(String type, String content)
	{
		this.type = type;
		this.content = content;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
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
