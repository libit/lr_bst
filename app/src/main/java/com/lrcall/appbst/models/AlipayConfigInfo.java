package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

public class AlipayConfigInfo implements java.io.Serializable
{
	private static final long serialVersionUID = -4226184432928484023L;
	@SerializedName("partner")
	private String partner;// 商户PID
	@SerializedName("seller")
	private String seller;// 商户收款账号
	@SerializedName("rsaPrivateKey")
	private String rsaPrivateKey;// 商户私钥，pkcs8格式
	@SerializedName("rsaPublicKey")
	private String rsaPublicKey;// 支付宝公钥
	@SerializedName("notifyUrl")
	private String notifyUrl;// 服务器异步通知页面路径

	public AlipayConfigInfo()
	{
		super();
	}

	public AlipayConfigInfo(String partner, String seller, String rsaPrivateKey, String rsaPublicKey, String notifyUrl)
	{
		super();
		this.partner = partner;
		this.seller = seller;
		this.rsaPrivateKey = rsaPrivateKey;
		this.rsaPublicKey = rsaPublicKey;
		this.notifyUrl = notifyUrl;
	}

	public String getPartner()
	{
		return partner;
	}

	public void setPartner(String partner)
	{
		this.partner = partner;
	}

	public String getSeller()
	{
		return seller;
	}

	public void setSeller(String seller)
	{
		this.seller = seller;
	}

	public String getRsaPrivateKey()
	{
		return rsaPrivateKey;
	}

	public void setRsaPrivateKey(String rsaPrivateKey)
	{
		this.rsaPrivateKey = rsaPrivateKey;
	}

	public String getRsaPublicKey()
	{
		return rsaPublicKey;
	}

	public void setRsaPublicKey(String rsaPublicKey)
	{
		this.rsaPublicKey = rsaPublicKey;
	}

	public String getNotifyUrl()
	{
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl)
	{
		this.notifyUrl = notifyUrl;
	}
}
