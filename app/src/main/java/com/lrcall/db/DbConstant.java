/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

import com.lrcall.utils.AppConfig;

/**
 * Created by libit on 15/8/26.
 */
public class DbConstant
{
	/**
	 * 归属地信息表
	 */
	public static final String TABLE_NAME_LOCAL_INFO = "local_info";
	/**
	 * 标记信息表
	 */
	public static final String TABLE_NAME_NUMBER_LABEL = "number_label";
	/**
	 * 黑名单表
	 */
	public static final String TABLE_NAME_BLACK_NUMBER = "black_numbers";
	/**
	 * 白名单表
	 */
	public static final String TABLE_NAME_WHITE_NUMBER = "white_numbers";
	/**
	 * 商品分类表
	 */
	public static final String TABLE_NAME_PRODUCT_SORT = "product_sort";
	/**
	 * 商品表
	 */
	public static final String TABLE_NAME_PRODUCT = "product";
	/**
	 * 消息表
	 */
	public static final String TABLE_NAME_NEWS = "news";
	/**
	 * Banner表
	 */
	public static final String TABLE_NAME_BANNER = "banners";
	/**
	 * 商品收藏表
	 */
	public static final String TABLE_NAME_STAR = "stars";
	/**
	 * 用户地址表
	 */
	public static final String TABLE_NAME_ADDRESS = "addresses";
	/**
	 * 购物车表
	 */
	public static final String TABLE_NAME_SHOP_CART = "shop_cart";
	/**
	 * DataTraffic表
	 */
	public static final String TABLE_NAME_DATA_TRAFFIC = "data_traffics";
	/**
	 * Authority for regular database of the application.
	 * 值与com.lrcall.db.DBProvider的authorities值相同
	 */
	public static final String AUTHORITY = AppConfig.AUTHORITY_NAME + ".db";
	/**
	 * Base content type for appinfo objects.
	 */
	public static final String BASE_DIR_TYPE = "vnd.android.cursor.dir/vnd.bst";
	/**
	 * Base item content type for appinfo objects.
	 */
	public static final String BASE_ITEM_TYPE = "vnd.android.cursor.item/vnd.bst";

	/**
	 * Base uri for the table. <br/>
	 * To append with FIELD_ID
	 *
	 * @param tableName 表名
	 * @return 表的Uri
	 * @see ContentUris#appendId(android.net.Uri.Builder, long)
	 */
	public static Uri getTableUriBase(String tableName)
	{
		return Uri.parse(ContentResolver.SCHEME_CONTENT + "://" + AUTHORITY + "/" + tableName + "/");
	}

	/**
	 * Uri of table.
	 *
	 * @param tableName 表名
	 * @return 表的Uri
	 */
	public static Uri getTableUri(String tableName)
	{
		return Uri.parse(ContentResolver.SCHEME_CONTENT + "://" + AUTHORITY + "/" + tableName);
	}

	/**
	 * Content type for table
	 *
	 * @param tableName 表名
	 * @return
	 */
	public static String getTableContentType(String tableName)
	{
		return BASE_DIR_TYPE + "." + tableName;
	}

	/**
	 * Item type for table
	 *
	 * @param tableName 表名
	 * @return
	 */
	public static String getTableContentItemType(String tableName)
	{
		return BASE_ITEM_TYPE + "." + tableName;
	}
}
