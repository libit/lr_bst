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
public class ProductInfo extends DbObject
{
	public static final String FIELD_PRODUCT_ID = "product_id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_SORT_ID = "sort_id";
	public static final String FIELD_BRAND_ID = "brand_id";
	public static final String FIELD_PIC_ID = "pic_id";
	public static final String FIELD_PRICE = "price";
	public static final String FIELD_MARKET_PRICE = "market_price";
	public static final String FIELD_EXPRESS_PRICE = "express_price";
	public static final String FIELD_COUNT = "count";
	public static final String FIELD_DESC = "desc";
	public static final String FIELD_CONFIG = "config";
	public static final String FIELD_CONTENT = "content";
	public static final String FIELD_NEED_EXPRESS = "need_express";
	@SerializedName("id")
	private String id;// 主键
	@SerializedName("productId")
	private String productId;//ID
	@SerializedName("name")
	private String name;//商品目录
	@SerializedName("sortId")
	private String sortId;//分类ID-存储分类名
	@SerializedName("brandId")
	private String brandId;//品牌ID-存储品牌名
	@SerializedName("picId")
	private String picId;//商品图片
	@SerializedName("price")
	private int price;//价格
	@SerializedName("marketPrice")
	private int marketPrice;//市场价
	@SerializedName("expressPrice")
	private int expressPrice;//快递费
	@SerializedName("count")
	private int count;//库存
	@SerializedName("description")
	private String description;//描述
	@SerializedName("config")
	private String config;//配置参数
	@SerializedName("content")
	private String content;//内容
	@SerializedName("needExpress")
	private byte needExpress;//是否需要快递

	public ProductInfo()
	{
	}

	public ProductInfo(String productId, String name, String sortId, String brandId, String picId, int price, int marketPrice, int expressPrice, int count, String description, String config, String content, byte needExpress)
	{
		this.productId = productId;
		this.name = name;
		this.sortId = sortId;
		this.brandId = brandId;
		this.picId = picId;
		this.price = price;
		this.marketPrice = marketPrice;
		this.expressPrice = expressPrice;
		this.count = count;
		this.description = description;
		this.config = config;
		this.content = content;
		this.needExpress = needExpress;
	}

	/**
	 * 获取创建表的SQL语句
	 *
	 * @return SQL语句
	 */
	public static final String getCreateTableSQL()
	{
		return String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT,%s INTEGER,%s INTEGER,%s INTEGER,%s INTEGER,%s TEXT NOT NULL,%s TEXT,%s TEXT,%s INTEGER);", DbConstant.TABLE_NAME_PRODUCT, FIELD_ID, FIELD_PRODUCT_ID, FIELD_NAME, FIELD_SORT_ID, FIELD_BRAND_ID, FIELD_PIC_ID, FIELD_PRICE, FIELD_MARKET_PRICE, FIELD_EXPRESS_PRICE, FIELD_COUNT, FIELD_DESC, FIELD_CONFIG, FIELD_CONTENT, FIELD_NEED_EXPRESS);
	}

	/**
	 * 从数据库中取出对象
	 *
	 * @param cursor
	 * @return
	 */
	public static ProductInfo getObjectFromDb(Cursor cursor)
	{
		ProductInfo productInfo = new ProductInfo();
		productInfo.setId(cursor.getString(cursor.getColumnIndex(FIELD_ID)));
		productInfo.setProductId(cursor.getString(cursor.getColumnIndex(FIELD_PRODUCT_ID)));
		productInfo.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
		productInfo.setSortId(cursor.getString(cursor.getColumnIndex(FIELD_SORT_ID)));
		productInfo.setBrandId(cursor.getString(cursor.getColumnIndex(FIELD_BRAND_ID)));
		productInfo.setPicId(cursor.getString(cursor.getColumnIndex(FIELD_PIC_ID)));
		productInfo.setPrice(cursor.getInt(cursor.getColumnIndex(FIELD_PRICE)));
		productInfo.setMarketPrice(cursor.getInt(cursor.getColumnIndex(FIELD_MARKET_PRICE)));
		productInfo.setExpressPrice(cursor.getInt(cursor.getColumnIndex(FIELD_EXPRESS_PRICE)));
		productInfo.setCount(cursor.getInt(cursor.getColumnIndex(FIELD_COUNT)));
		productInfo.setDescription(cursor.getString(cursor.getColumnIndex(FIELD_DESC)));
		productInfo.setConfig(cursor.getString(cursor.getColumnIndex(FIELD_CONFIG)));
		productInfo.setContent(cursor.getString(cursor.getColumnIndex(FIELD_CONTENT)));
		productInfo.setNeedExpress((byte) cursor.getInt(cursor.getColumnIndex(FIELD_NEED_EXPRESS)));
		return productInfo;
	}

	/**
	 * 转换成数据库存储的数据
	 *
	 * @return ContentValues
	 */
	public ContentValues getObjectContentValues()
	{
		ContentValues contentValues = new ContentValues();
		if (id != null)
		{
			contentValues.put(FIELD_ID, id);
		}
		contentValues.put(FIELD_PRODUCT_ID, productId);
		contentValues.put(FIELD_NAME, name);
		contentValues.put(FIELD_SORT_ID, sortId);
		contentValues.put(FIELD_BRAND_ID, brandId);
		contentValues.put(FIELD_PIC_ID, picId);
		contentValues.put(FIELD_PRICE, price);
		contentValues.put(FIELD_MARKET_PRICE, marketPrice);
		contentValues.put(FIELD_EXPRESS_PRICE, expressPrice);
		contentValues.put(FIELD_COUNT, count);
		contentValues.put(FIELD_DESC, description);
		contentValues.put(FIELD_CONFIG, config);
		contentValues.put(FIELD_CONTENT, content);
		contentValues.put(FIELD_NEED_EXPRESS, needExpress);
		return contentValues;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSortId()
	{
		return sortId;
	}

	public void setSortId(String sortId)
	{
		this.sortId = sortId;
	}

	public String getBrandId()
	{
		return brandId;
	}

	public void setBrandId(String brandId)
	{
		this.brandId = brandId;
	}

	public String getPicId()
	{
		return picId;
	}

	public void setPicId(String picId)
	{
		this.picId = picId;
	}

	public int getPrice()
	{
		return price;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}

	public int getMarketPrice()
	{
		return marketPrice;
	}

	public void setMarketPrice(int marketPrice)
	{
		this.marketPrice = marketPrice;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getConfig()
	{
		return config;
	}

	public void setConfig(String config)
	{
		this.config = config;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public byte getNeedExpress()
	{
		return needExpress;
	}

	public void setNeedExpress(byte needExpress)
	{
		this.needExpress = needExpress;
	}

	public int getExpressPrice()
	{
		return expressPrice;
	}

	public void setExpressPrice(int expressPrice)
	{
		this.expressPrice = expressPrice;
	}
}
