package com.lrcall.enums;

/**
 * 验证码类型
 *
 * @author libit
 */
public enum SmsCodeType
{
	REGISTER(1, "注册验证码"), FIND_PWD(2, "找回密码"), SHOP_REGISTER(3, "商家注册");
	private int type;
	private String desc;

	private SmsCodeType(int type, String desc)
	{
		this.type = type;
		this.desc = desc;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public static String getCodeType(int type)
	{
		SmsCodeType[] list = SmsCodeType.values();
		for (SmsCodeType smsCodeType : list)
		{
			if (smsCodeType.getType() == type)
			{
				return smsCodeType.getDesc();
			}
		}
		return "";
	}
}
