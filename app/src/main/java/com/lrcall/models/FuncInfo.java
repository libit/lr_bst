/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.models;

public class FuncInfo
{
	private int id;
	private int imgRes;
	private String label;

	public FuncInfo()
	{
	}

	public FuncInfo(int imgRes, String label)
	{
		this.imgRes = imgRes;
		this.label = label;
	}

	public FuncInfo(int id, int imgRes, String label)
	{
		this.id = id;
		this.imgRes = imgRes;
		this.label = label;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getImgRes()
	{
		return imgRes;
	}

	public void setImgRes(int imgRes)
	{
		this.imgRes = imgRes;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}
}
