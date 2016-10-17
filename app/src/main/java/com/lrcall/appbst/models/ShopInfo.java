/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by libit on 16/7/14.
 */
public class ShopInfo
{
	@SerializedName("shopId")
	private String shopId;//商家ID
	@SerializedName("levelId")
	private byte levelId;//级别ID
	@SerializedName("name")
	private String name;//店铺名
	@SerializedName("nickname")
	private String nickname;//简称
	@SerializedName("email")
	private String email;//邮箱
	@SerializedName("authStatus")
	private byte authStatus;//认证状态
	@SerializedName("remark")
	private String remark;//备注
	@SerializedName("addDateLong")
	private long addDateLong;//注册时间
	@SerializedName("picId")
	private String picUrl;//头像地址

	public ShopInfo()
	{
	}

	public ShopInfo(String shopId, byte levelId, String name, String nickname, byte authStatus, String remark, long addDateLong, String picUrl)
	{
		this.shopId = shopId;
		this.levelId = levelId;
		this.name = name;
		this.nickname = nickname;
		this.authStatus = authStatus;
		this.remark = remark;
		this.addDateLong = addDateLong;
		this.picUrl = picUrl;
	}

	public String getShopId()
	{
		return shopId;
	}

	public void setShopId(String shopId)
	{
		this.shopId = shopId;
	}

	public byte getLevelId()
	{
		return levelId;
	}

	public void setLevelId(byte levelId)
	{
		this.levelId = levelId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public byte getAuthStatus()
	{
		return authStatus;
	}

	public void setAuthStatus(byte authStatus)
	{
		this.authStatus = authStatus;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public long getAddDateLong()
	{
		return addDateLong;
	}

	public void setAddDateLong(long addDateLong)
	{
		this.addDateLong = addDateLong;
	}

	public String getPicUrl()
	{
		return picUrl;
	}

	public void setPicUrl(String picUrl)
	{
		this.picUrl = picUrl;
	}
}
