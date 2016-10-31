/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.TableOrderInfo;
import com.lrcall.appbst.models.TableSearchInfo;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家商品服务类
 * Created by libit on 16/4/6.
 */
public class ShopProductService extends BaseService
{
	public ShopProductService(Context context)
	{
		super(context);
	}

	/**
	 * 获取商品列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductList(String condition, int start, int size, List<TableOrderInfo> orderInfos, List<TableSearchInfo> searchInfos, boolean advancedSearch, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		if (!StringTools.isNull(condition))
		{
			params.put("search[value]", condition);
		}
		ajaxStringCallback(ApiConfig.GET_SHOP_PRODUCT_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取商品列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductSortList(String condition, int start, int size, List<TableOrderInfo> orderInfos, List<TableSearchInfo> searchInfos, boolean advancedSearch, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		params.put("showCommon", true);
		if (!StringTools.isNull(condition))
		{
			params.put("search[value]", condition);
		}
		ajaxStringCallback(ApiConfig.GET_SHOP_PRODUCT_SORT_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取商品列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getBrandList(String condition, int start, int size, List<TableOrderInfo> orderInfos, List<TableSearchInfo> searchInfos, boolean advancedSearch, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		params.put("showCommon", true);
		if (!StringTools.isNull(condition))
		{
			params.put("search[value]", condition);
		}
		ajaxStringCallback(ApiConfig.GET_BRAND_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 添加商品
	 *
	 * @param sortId
	 * @param brandId
	 * @param name
	 * @param picId
	 * @param price
	 * @param marketPrice
	 * @param expressPrice
	 * @param count
	 * @param sharePrice
	 * @param shareAddPrice
	 * @param description
	 * @param config
	 * @param content
	 * @param needExpress
	 * @param sortIndex
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void addProduct(String sortId, String brandId, String name, String picId, int price, int marketPrice, int expressPrice, int count, int sharePrice, int shareAddPrice, String description, String config, String content, byte needExpress, int sortIndex, int pointAmount, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("sortId", sortId);
		params.put("brandId", brandId);
		params.put("name", name);
		params.put("picId", picId);
		params.put("price", price);
		params.put("marketPrice", marketPrice);
		params.put("expressPrice", expressPrice);
		params.put("count", count);
		params.put("sharePrice", sharePrice);
		params.put("shareAddPrice", shareAddPrice);
		params.put("description", description);
		params.put("config", config);
		params.put("content", content);
		params.put("needExpress", needExpress);
		params.put("sortIndex", sortIndex);
		params.put("pointAmount", pointAmount);
		ajaxStringCallback(ApiConfig.SHOP_ADD_PRODUCT, params, tips, needServiceProcessData);
	}

	/**
	 * 更新商品
	 *
	 * @param sortId
	 * @param brandId
	 * @param name
	 * @param picId
	 * @param price
	 * @param marketPrice
	 * @param expressPrice
	 * @param count
	 * @param sharePrice
	 * @param shareAddPrice
	 * @param description
	 * @param config
	 * @param content
	 * @param needExpress
	 * @param sortIndex
	 * @param pointAmount
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void updateProduct(String productId, String sortId, String brandId, String name, String picId, int price, int marketPrice, int expressPrice, int count, int sharePrice, int shareAddPrice, String description, String config, String content, byte needExpress, int sortIndex, Integer pointAmount, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		params.put("sortId", sortId);
		params.put("brandId", brandId);
		params.put("name", name);
		params.put("picId", picId);
		params.put("price", price);
		params.put("marketPrice", marketPrice);
		params.put("expressPrice", expressPrice);
		params.put("count", count);
		params.put("sharePrice", sharePrice);
		params.put("shareAddPrice", shareAddPrice);
		params.put("description", description);
		params.put("config", config);
		params.put("content", content);
		params.put("needExpress", needExpress);
		params.put("sortIndex", sortIndex);
		params.put("pointAmount", pointAmount);
		ajaxStringCallback(ApiConfig.SHOP_UPDATE_PRODUCT, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_SHOP_PRODUCT_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<ProductInfo> productInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductInfo>>()
				{
				}.getType());
				if (productInfoList != null && productInfoList.size() > 0)
				{
					//先清空
					//					DbProductInfoFactory.getInstance().clearProductInfo();
					for (ProductInfo productInfo : productInfoList)
					{
						//						DbProductInfoFactory.getInstance().addOrUpdateProductInfo(productInfo);
					}
				}
			}
			else
			{
			}
		}
	}
}
