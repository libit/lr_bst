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
import com.lrcall.db.DbProductInfoFactory;
import com.lrcall.utils.GsonTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品服务类
 * Created by libit on 16/4/6.
 */
public class ProductService extends BaseService
{
	public ProductService(Context context)
	{
		super(context);
	}

	/**
	 * 获取推荐商品列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getRecommendProductList(int start, int size, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.GET_RECOMMEND_PRODUCT_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取促销商品列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getConcessionProductList(int start, int size, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.GET_CONCESSION_PRODUCT_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取最新商品列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getNewProductList(int start, int size, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.GET_NEW_PRODUCT_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取抢购商品列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getPanicBuyingProductList(int start, int size, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		ajaxStringCallback(ApiConfig.GET_PANIC_BUYING_PRODUCT_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取商品列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductList(String condition, int start, int size, List<TableOrderInfo> orderInfos, List<TableSearchInfo> searchInfos, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		params.put("search[value]", condition);
		ajaxStringCallback(ApiConfig.GET_PRODUCT_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取商品列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductListBySortId(String sortId, int start, int size, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		params.put("sortId", sortId);
		ajaxStringCallback(ApiConfig.GET_PRODUCT_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取商品信息
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductInfo(String productId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		ajaxStringCallback(ApiConfig.GET_PRODUCT_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 获取商品图片列表
	 *
	 * @param productId              商品ID
	 * @param picType                图片位置类型
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void getProductPicList(String productId, String picType, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		params.put("picType", picType);
		ajaxStringCallback(ApiConfig.GET_PRODUCT_PIC_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取商品分享文字
	 *
	 * @param productId              商品ID
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void getProductShareText(String productId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("productId", productId);
		ajaxStringCallback(ApiConfig.GET_SHARE_PRODUCT_TEXT, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_PRODUCT_LIST) || url.endsWith(ApiConfig.GET_NEW_PRODUCT_LIST))
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
						DbProductInfoFactory.getInstance().addOrUpdateProductInfo(productInfo);
					}
				}
			}
			else
			{
			}
		}
		else if (url.endsWith(ApiConfig.GET_PRODUCT_INFO))
		{
			ProductInfo productInfo = GsonTools.getReturnObject(result, ProductInfo.class);
			if (productInfo != null)
			{
				DbProductInfoFactory.getInstance().addOrUpdateProductInfo(productInfo);
			}
		}
	}
}
