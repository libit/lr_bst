package com.lrcall.appbst.models;

/**
 * Created by libit on 2017/1/1.
 */
public class HXKefuInfo
{
	private String username;
	private String nickname;

	public HXKefuInfo()
	{
		super();
	}

	public HXKefuInfo(String username, String nickname)
	{
		super();
		this.username = username;
		this.nickname = nickname;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}
}
