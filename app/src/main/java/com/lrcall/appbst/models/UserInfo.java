/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class UserInfo
{
	@SerializedName("userId")
	private String userId;
	@SerializedName("sessionId")
	private String sessionId;
	@SerializedName("number")
	private String number;
	@SerializedName("userType")
	private byte userType;
	@SerializedName("userLevel")
	private byte userLevel;
	@SerializedName("name")
	private String name;
	@SerializedName("nickname")
	private String nickname;
	@SerializedName("sex")
	private byte sex;
	@SerializedName("picId")
	private String picId;
	@SerializedName("birthday")
	private Long birthday;
	@SerializedName("country")
	private String country;
	@SerializedName("province")
	private String province;
	@SerializedName("city")
	private String city;
	@SerializedName("address")
	private String address;
	@SerializedName("subscribe")
	private String subscribe;
	@SerializedName("subscribeTime")
	private Long subscribeTime;
	@SerializedName("language")
	private String language;
	@SerializedName("remark")
	private String remark;
	@SerializedName("addDateLong")
	private long addDateLong;
	@SerializedName("picInfo")
	private PicInfo picInfo;

	public UserInfo()
	{
	}

	public UserInfo(String userId, String sessionId, String number, String name, String nickname, byte sex, String picId, Long birthday, String country, String province, String city, String address, String subscribe, Long subscribeTime, String language, String remark, PicInfo picInfo)
	{
		this.userId = userId;
		this.sessionId = sessionId;
		this.number = number;
		this.name = name;
		this.nickname = nickname;
		this.sex = sex;
		this.picId = picId;
		this.birthday = birthday;
		this.country = country;
		this.province = province;
		this.city = city;
		this.address = address;
		this.subscribe = subscribe;
		this.subscribeTime = subscribeTime;
		this.language = language;
		this.remark = remark;
		this.picInfo = picInfo;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
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

	public byte getSex()
	{
		return sex;
	}

	public void setSex(byte sex)
	{
		this.sex = sex;
	}

	public String getPicId()
	{
		return picId;
	}

	public void setPicId(String picId)
	{
		this.picId = picId;
	}

	public Long getBirthday()
	{
		return birthday;
	}

	public void setBirthday(Long birthday)
	{
		this.birthday = birthday;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getProvince()
	{
		return province;
	}

	public void setProvince(String province)
	{
		this.province = province;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getSubscribe()
	{
		return subscribe;
	}

	public void setSubscribe(String subscribe)
	{
		this.subscribe = subscribe;
	}

	public Long getSubscribeTime()
	{
		return subscribeTime;
	}

	public void setSubscribeTime(Long subscribeTime)
	{
		this.subscribeTime = subscribeTime;
	}

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public PicInfo getPicInfo()
	{
		return picInfo;
	}

	public void setPicInfo(PicInfo picInfo)
	{
		this.picInfo = picInfo;
	}

	public byte getUserType()
	{
		return userType;
	}

	public void setUserType(byte userType)
	{
		this.userType = userType;
	}

	public byte getUserLevel()
	{
		return userLevel;
	}

	public void setUserLevel(byte userLevel)
	{
		this.userLevel = userLevel;
	}

	public long getAddDateLong()
	{
		return addDateLong;
	}

	public void setAddDateLong(long addDateLong)
	{
		this.addDateLong = addDateLong;
	}
}
