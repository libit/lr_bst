/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.models;

import android.graphics.Bitmap;

import com.lrcall.utils.ConstValues;

/**
 * Created by libit on 15/8/19.
 */
public class AppInfo
{
	private String id;// 主键
	private String uid;// 程序的用户ID，不同的程序ID可能一样
	private String name;//程序名称
	private String nameLabel;//程序名称的拼音，用户检索
	private String packageName;// 包名，唯一标识
	private String launchClassName;// 启动的class
	private Bitmap photo;// 程序图片
	private int type;// 程序类型（系统程序或用户程序）
	private boolean isEnabled;// 程序状态（启用或禁用）
	private boolean isHide;// 程序是否是隐藏程序
	private boolean isExist;// 程序是否还存在（当用户卸载后）

	public AppInfo()
	{
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public String getPackageName()
	{
		return packageName;
	}

	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLaunchClassName()
	{
		return launchClassName;
	}

	public void setLaunchClassName(String launchClassName)
	{
		this.launchClassName = launchClassName;
	}

	public Bitmap getPhoto()
	{
		return photo;
	}

	public void setPhoto(Bitmap photo)
	{
		this.photo = photo;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public boolean isEnabled()
	{
		return isEnabled;
	}

	public void setIsEnabled(boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}

	public boolean isHide()
	{
		return isHide;
	}

	public void setIsHide(boolean isHide)
	{
		this.isHide = isHide;
	}

	public boolean isExist()
	{
		return isExist;
	}

	public void setIsExist(boolean isExist)
	{
		this.isExist = isExist;
	}

	public String getNameLabel()
	{
		return nameLabel;
	}

	public void setNameLabel(String nameLabel)
	{
		this.nameLabel = nameLabel;
	}

	@Override
	public String toString()
	{
		return String.format("uid:%s,name:%s,nameLabel:%s,packageName:%s,type:%d,enabled:%d,hide:%d,exist:%d.", uid, name, nameLabel, packageName, type, isEnabled ? ConstValues.STATUS_ENABLED : ConstValues.STATUS_DISABLED, isHide() ? ConstValues.STATUS_HIDE : ConstValues.STATUS_SHOW, isExist ? ConstValues.STATUS_EXIST : ConstValues.STATUS_NOT_EXIST);
	}
}
