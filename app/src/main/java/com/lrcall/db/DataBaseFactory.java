/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lrcall.appbst.models.BannerInfo;
import com.lrcall.appbst.models.BlackNumberInfo;
import com.lrcall.appbst.models.DataTrafficInfo;
import com.lrcall.appbst.models.NewsInfo;
import com.lrcall.appbst.models.NumberLabelInfo;
import com.lrcall.appbst.models.NumberLocalInfo;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.ProductSortInfo;
import com.lrcall.appbst.models.ProductStarInfo;
import com.lrcall.appbst.models.ShopCartInfo;
import com.lrcall.appbst.models.UserAddressInfo;
import com.lrcall.appbst.models.WhiteNumberInfo;

/**
 * Created by libit on 15/8/26.
 */
public class DataBaseFactory
{
	public static class DBHelper extends SQLiteOpenHelper
	{
		private static final int DATABASE_VERSION = 18;
		private static final String[] TABLES = new String[]{NumberLocalInfo.getCreateTableSQL()// 归属地信息表
				, NumberLabelInfo.getCreateTableSQL()//号码标记信息
				, BlackNumberInfo.getCreateTableSQL()//号码黑名单
				, WhiteNumberInfo.getCreateTableSQL()//号码白名单
				, ProductSortInfo.getCreateTableSQL()//商品分类
				, NewsInfo.getCreateTableSQL()//消息
				, ProductInfo.getCreateTableSQL()//商品
				, BannerInfo.getCreateTableSQL()//Banner
				, ProductStarInfo.getCreateTableSQL()//商品收藏
				, UserAddressInfo.getCreateTableSQL()//用户地址
				, ShopCartInfo.getCreateTableSQL()//购物车
				, DataTrafficInfo.getCreateTableSQL()//流量充值
		};

		DBHelper(Context context)
		{
			super(context, DbConstant.AUTHORITY, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			int count = TABLES.length;
			for (int i = 0; i < count; i++)
			{
				db.execSQL(TABLES[i]);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			if (oldVersion < 1)
			{
				db.execSQL("DROP TABLE IF EXISTS " + DbConstant.TABLE_NAME_LOCAL_INFO);
			}
			if (oldVersion < 2)
			{
				db.execSQL("DROP TABLE IF EXISTS " + DbConstant.TABLE_NAME_NUMBER_LABEL);
			}
			if (oldVersion < 4)
			{
				db.execSQL("DROP TABLE IF EXISTS " + DbConstant.TABLE_NAME_BLACK_NUMBER);
				db.execSQL("DROP TABLE IF EXISTS " + DbConstant.TABLE_NAME_WHITE_NUMBER);
			}
			if (oldVersion < 5)
			{
				db.execSQL("DROP TABLE IF EXISTS " + DbConstant.TABLE_NAME_PRODUCT_SORT);
			}
			if (oldVersion < 6)
			{
				db.execSQL("DROP TABLE IF EXISTS " + DbConstant.TABLE_NAME_NEWS);
			}
			if (oldVersion < 7)
			{
				db.execSQL("DROP TABLE IF EXISTS " + DbConstant.TABLE_NAME_PRODUCT);
			}
			if (oldVersion < 8)
			{
			}
			if (oldVersion < 9)
			{
				db.execSQL("DROP TABLE IF EXISTS " + DbConstant.TABLE_NAME_STAR);
			}
			if (oldVersion < 10)
			{
				db.execSQL("DROP TABLE IF EXISTS " + DbConstant.TABLE_NAME_ADDRESS);
			}
			if (oldVersion < 11)
			{
				db.execSQL("DROP TABLE IF EXISTS " + DbConstant.TABLE_NAME_BANNER);
				db.execSQL("DROP TABLE IF EXISTS " + DbConstant.TABLE_NAME_SHOP_CART);
			}
			if (oldVersion < 12)
			{
				db.execSQL("ALTER TABLE " + DbConstant.TABLE_NAME_PRODUCT + " ADD COLUMN '" + ProductInfo.FIELD_NEED_EXPRESS + "' INTEGER;");
			}
			if (oldVersion < 13)
			{
				//				db.execSQL("ALTER TABLE " + DbConstant.TABLE_NAME_PRODUCT + " ADD COLUMN '" + ProductInfo.FIELD_EXPRESS_PRICE + "' INTEGER;");
				db.execSQL("DROP TABLE IF EXISTS " + DbConstant.TABLE_NAME_PRODUCT);
			}
			if (oldVersion < 17)
			{
				db.execSQL("DROP TABLE IF EXISTS " + DbConstant.TABLE_NAME_DATA_TRAFFIC);
			}
			if (oldVersion < 18)
			{
				db.execSQL("DROP TABLE IF EXISTS " + DbConstant.TABLE_NAME_PRODUCT);
			}
			onCreate(db);
		}
	}
}
