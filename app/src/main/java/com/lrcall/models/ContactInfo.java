/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.models;

import android.graphics.Bitmap;
import android.provider.ContactsContract;

import com.google.gson.annotations.SerializedName;
import com.lrcall.appbst.MyApplication;

import java.util.List;

/**
 * Created by libit on 15/8/9.
 */
public class ContactInfo
{
	// 联系人属性
	@SerializedName("contactId")
	private Long contactId;// 联系人ID
	@SerializedName("name")
	private String name;// 联系人姓名
	@SerializedName("py")
	private String py;// 联系人拼音首字母，用于索引
	@SerializedName("starred")
	private boolean starred;// 联系人是否已收藏
	@SerializedName("contactPhoto")
	private Bitmap contactPhoto;// 联系人图片
	@SerializedName("photoId")
	private Long photoId;// 联系人图片ID
	@SerializedName("phoneInfoList")
	private List<PhoneInfo> phoneInfoList;// 联系人号码列表

	public ContactInfo()
	{
		contactId = null;
		name = "";
		py = "";
		starred = false;
		contactPhoto = null;
		photoId = 0L;
		phoneInfoList = null;
	}

	public Long getContactId()
	{
		return contactId;
	}

	public void setContactId(Long contactId)
	{
		this.contactId = contactId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPy()
	{
		return py;
	}

	public void setPy(String py)
	{
		this.py = py;
	}

	public boolean isStarred()
	{
		return starred;
	}

	public void setStarred(boolean starred)
	{
		this.starred = starred;
	}

	public Bitmap getContactPhoto()
	{
		return contactPhoto;
	}

	public void setContactPhoto(Bitmap contactPhoto)
	{
		this.contactPhoto = contactPhoto;
	}

	public Long getPhotoId()
	{
		return photoId;
	}

	public void setPhotoId(Long photoId)
	{
		this.photoId = photoId;
	}

	public List<PhoneInfo> getPhoneInfoList()
	{
		return phoneInfoList;
	}

	public void setPhoneInfoList(List<PhoneInfo> phoneInfoList)
	{
		this.phoneInfoList = phoneInfoList;
	}

	@Override
	public String toString()
	{
		return String.format("name:%s", name);
	}

	/**
	 * 号码信息
	 */
	public static class PhoneInfo
	{
		private String number;// 号码
		private String type;// 类型

		public PhoneInfo(String n, String t)
		{
			this.number = n;
			this.type = t;
		}

		public String getNumber()
		{
			return number;
		}

		public void setNumber(String number)
		{
			this.number = number;
		}

		public String getType()
		{
			try
			{
				return convertTypeToString(Integer.parseInt(type));
			}
			catch (Exception e)
			{
				return type;
			}
		}

		public void setType(String type)
		{
			this.type = type;
		}

		private String convertTypeToString(int type)
		{
			return MyApplication.getContext().getString(ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(type));
		}

		@Override
		public String toString()
		{
			return String.format("number:%s,type:%s", number, type);
		}
	}
}