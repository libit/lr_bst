/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;
import com.lrcall.db.DbConstant;

/**
 * Created by libit on 16/7/14.
 */
public class ShopCartInfo extends DbObject
{
	public static final String FIELD_CART_ID = "cart_id";
	public static final String FIELD_USER_ID = "user_id";
	public static final String FIELD_PRODUCT_ID = "product_id";
	public static final String FIELD_AMOUNT = "amount";
	public static final String FIELD_DATE = "date";
	@SerializedName("id")
	private String id;// 主键
	@SerializedName("cartId")
	private String cartId;//ID
	@SerializedName("userId")
	private String userId;//用户ID
	@SerializedName("productId")
	private String productId;//商品ID
	@SerializedName("amount")
	private int amount;//数量
	@SerializedName("updateDateLong")
	private long date;//更新时间

	/**
	 * 获取创建表的SQL语句
	 *
	 * @return SQL语句
	 */
	public static final String getCreateTableSQL()
	{
		return String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s TEXT NOT NULL,%s INT NOT NULL,%s INT DEFAULT 0);", DbConstant.TABLE_NAME_SHOP_CART, FIELD_ID, FIELD_CART_ID, FIELD_USER_ID, FIELD_PRODUCT_ID, FIELD_AMOUNT, FIELD_DATE);
	}

	/**
	 * 从数据库中取出对象
	 *
	 * @param cursor
	 * @return
	 */
	public static ShopCartInfo getObjectFromDb(Cursor cursor)
	{
		ShopCartInfo shopCartInfo = new ShopCartInfo();
		shopCartInfo.setId(cursor.getString(cursor.getColumnIndex(FIELD_ID)));
		shopCartInfo.setCartId(cursor.getString(cursor.getColumnIndex(FIELD_CART_ID)));
		shopCartInfo.setUserId(cursor.getString(cursor.getColumnIndex(FIELD_USER_ID)));
		shopCartInfo.setProductId(cursor.getString(cursor.getColumnIndex(FIELD_PRODUCT_ID)));
		shopCartInfo.setAmount(cursor.getInt(cursor.getColumnIndex(FIELD_AMOUNT)));
		shopCartInfo.setDate(cursor.getLong(cursor.getColumnIndex(FIELD_DATE)));
		return shopCartInfo;
	}

	/**
	 * 转换成数据库存储的数据
	 *
	 * @return ContentValues
	 */
	public ContentValues getObjectContentValues()
	{
		ContentValues contentValues = new ContentValues();
		//		if (id != null)
		//		{
		//			contentValues.put(FIELD_ID, id);
		//		}
		contentValues.put(FIELD_CART_ID, cartId);
		contentValues.put(FIELD_USER_ID, userId);
		contentValues.put(FIELD_PRODUCT_ID, productId);
		contentValues.put(FIELD_AMOUNT, amount);
		contentValues.put(FIELD_DATE, date);
		return contentValues;
	}

	public ShopCartInfo()
	{
	}

	public ShopCartInfo(String cartId, String userId, String productId, int amount, long date)
	{
		this.cartId = cartId;
		this.userId = userId;
		this.productId = productId;
		this.amount = amount;
		this.date = date;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getCartId()
	{
		return cartId;
	}

	public void setCartId(String cartId)
	{
		this.cartId = cartId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public int getAmount()
	{
		return amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	public long getDate()
	{
		return date;
	}

	public void setDate(long date)
	{
		this.date = date;
	}
}
