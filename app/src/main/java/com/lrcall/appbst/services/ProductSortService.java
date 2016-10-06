/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.models.ProductSortInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.db.DbProductSortInfoFactory;
import com.lrcall.utils.GsonTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品分类服务类
 * Created by libit on 16/4/6.
 */
public class ProductSortService extends BaseService
{
	public ProductSortService(Context context)
	{
		super(context);
	}

	/**
	 * 获取分类列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getProductSortList(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_PRODUCT_SORT_LIST, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_PRODUCT_SORT_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<ProductSortInfo> productSortInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductSortInfo>>()
				{
				}.getType());
				if (productSortInfoList != null && productSortInfoList.size() > 0)
				{
					//先清空分类
					DbProductSortInfoFactory.getInstance().clearProductSortInfo();
					for (ProductSortInfo productSortInfo : productSortInfoList)
					{
						DbProductSortInfoFactory.getInstance().addOrUpdateProductSortInfo(productSortInfo);
					}
				}
			}
			else
			{
				//				ToastView.showCenterToast(context, "注册失败：" + result);
			}
		}
	}
}
