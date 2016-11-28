/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.ShopCartInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.db.DbShopCartInfoFactory;
import com.lrcall.utils.GsonTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车服务类
 * Created by libit on 16/4/6.
 */
public class ShopCartService extends BaseService
{
	public ShopCartService(Context context)
	{
		super(context);
	}

	/**
	 * 获取购物车列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getShopCartInfoList(int start, int size, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.GET_SHOP_CART_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 添加购物车信息
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void addShopCartInfo(String productId, int amount, String referrerId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		params.put("amount", amount);
		params.put("referrerId", referrerId);
		ajaxStringCallback(ApiConfig.ADD_SHOP_CART_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 更新购物车信息
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void updateShopCartInfo(String cartId, int amount, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("cartId", cartId);
		params.put("amount", amount);
		ajaxStringCallback(ApiConfig.UPDATE_SHOP_CART_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 删除购物车信息
	 *
	 * @param cartId                 ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void deleteShopCartInfo(String cartId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("cartId", cartId);
		ajaxStringCallback(ApiConfig.DELETE_SHOP_CART_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 获取购物车信息
	 *
	 * @param cartId                 ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getShopCartInfo(String cartId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("cartId", cartId);
		ajaxStringCallback(ApiConfig.GET_SHOP_CART_INFO, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_SHOP_CART_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<ShopCartInfo> shopCartInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ShopCartInfo>>()
				{
				}.getType());
				if (shopCartInfoList != null)
				{
					//先清空分类
					DbShopCartInfoFactory.getInstance().clearShopCartInfo();
					for (ShopCartInfo shopCartInfo : shopCartInfoList)
					{
						DbShopCartInfoFactory.getInstance().addOrUpdateShopCartInfo(shopCartInfo);
					}
				}
			}
		}
		else if (url.endsWith(ApiConfig.ADD_SHOP_CART_INFO))
		{
			ShopCartInfo shopCartInfo = GsonTools.getReturnObject(result, ShopCartInfo.class);
			if (shopCartInfo != null)
			{
				DbShopCartInfoFactory.getInstance().addOrUpdateShopCartInfo(shopCartInfo);
			}
		}
		else if (url.endsWith(ApiConfig.UPDATE_SHOP_CART_INFO))
		{
			ShopCartInfo shopCartInfo = GsonTools.getReturnObject(result, ShopCartInfo.class);
			if (shopCartInfo != null)
			{
				DbShopCartInfoFactory.getInstance().addOrUpdateShopCartInfo(shopCartInfo);
			}
		}
		else if (url.endsWith(ApiConfig.DELETE_SHOP_CART_INFO))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				DbShopCartInfoFactory.getInstance().deleteShopCartInfo(returnInfo.getErrmsg());
			}
		}
		else if (url.endsWith(ApiConfig.GET_SHOP_CART_INFO))
		{
			ShopCartInfo shopCartInfo = GsonTools.getReturnObject(result, ShopCartInfo.class);
			if (shopCartInfo != null)
			{
				DbShopCartInfoFactory.getInstance().addOrUpdateShopCartInfo(shopCartInfo);
			}
		}
	}
}
