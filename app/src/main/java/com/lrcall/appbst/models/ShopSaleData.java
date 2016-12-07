/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by libit on 2016/11/8.
 */
public class ShopSaleData
{
	@SerializedName("totalSaleAmount")
	private long totalSaleAmount;// 总收入
	@SerializedName("recent7SaleAmont")
	private long recent7SaleAmont;// 最近7日营收
	@SerializedName("recent7OrderCount")
	private long recent7OrderCount;// 最近7日订单数

	public ShopSaleData()
	{
		super();
	}

	public ShopSaleData(long totalSaleAmount, long recent7SaleAmont, long recent7OrderCount)
	{
		super();
		this.totalSaleAmount = totalSaleAmount;
		this.recent7SaleAmont = recent7SaleAmont;
		this.recent7OrderCount = recent7OrderCount;
	}

	public long getTotalSaleAmount()
	{
		return totalSaleAmount;
	}

	public void setTotalSaleAmount(long totalSaleAmount)
	{
		this.totalSaleAmount = totalSaleAmount;
	}

	public long getRecent7SaleAmont()
	{
		return recent7SaleAmont;
	}

	public void setRecent7SaleAmont(long recent7SaleAmont)
	{
		this.recent7SaleAmont = recent7SaleAmont;
	}

	public long getRecent7OrderCount()
	{
		return recent7OrderCount;
	}

	public void setRecent7OrderCount(long recent7OrderCount)
	{
		this.recent7OrderCount = recent7OrderCount;
	}
}
