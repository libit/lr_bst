/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.UserAddressInfo;
import com.lrcall.db.DbUserAddressInfoFactory;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地址服务类
 * Created by libit on 16/4/6.
 */
public class AddressService extends BaseService
{
	public AddressService(Context context)
	{
		super(context);
	}

	/**
	 * 获取地址列表
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getUserAddressInfoList(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_ADDRESS_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 添加地址信息
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void addUserAddressInfo(String name, String number, String country, String provinceId, String cityId, String districtId, String address, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("number", number);
		params.put("country", country);
		params.put("provinceId", provinceId);
		params.put("cityId", cityId);
		params.put("districtId", districtId);
		params.put("address", address);
		ajaxStringCallback(ApiConfig.ADD_ADDRESS_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 更新地址信息
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void updateUserAddressInfo(String addressId, String name, String number, String country, String provinceId, String cityId, String districtId, String address, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("addressId", addressId);
		params.put("name", name);
		params.put("number", number);
		params.put("country", country);
		params.put("provinceId", provinceId);
		params.put("cityId", cityId);
		params.put("districtId", districtId);
		params.put("address", address);
		ajaxStringCallback(ApiConfig.UPDATE_ADDRESS_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 更新地址信息
	 *
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void updateUserAddressInfoStatus(String addressId, byte status, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("addressId", addressId);
		params.put("status", status);
		ajaxStringCallback(ApiConfig.UPDATE_ADDRESS_STATUS, params, tips, needServiceProcessData);
	}

	/**
	 * 删除地址信息
	 *
	 * @param addressId              ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void deleteUserAddressInfo(String addressId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("addressId", addressId);
		ajaxStringCallback(ApiConfig.DELETE_ADDRESS_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 获取地址信息
	 *
	 * @param addressId              ID
	 * @param tips                   提示信息
	 * @param needServiceProcessData
	 */
	public void getUserAddressInfo(String addressId, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("addressId", addressId);
		ajaxStringCallback(ApiConfig.GET_ADDRESS_INFO, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_ADDRESS_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<UserAddressInfo> userAddressInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<UserAddressInfo>>()
				{
				}.getType());
				if (userAddressInfoList != null)
				{
					//先清空分类
					DbUserAddressInfoFactory.getInstance().clearUserAddressInfo();
					for (UserAddressInfo userAddressInfo : userAddressInfoList)
					{
						DbUserAddressInfoFactory.getInstance().addOrUpdateUserAddressInfo(userAddressInfo);
					}
				}
			}
		}
		else if (url.endsWith(ApiConfig.ADD_ADDRESS_INFO))
		{
			UserAddressInfo userAddressInfo = GsonTools.getReturnObject(result, UserAddressInfo.class);
			if (userAddressInfo != null)
			{
				DbUserAddressInfoFactory.getInstance().addOrUpdateUserAddressInfo(userAddressInfo);
			}
		}
		else if (url.endsWith(ApiConfig.UPDATE_ADDRESS_INFO))
		{
			UserAddressInfo userAddressInfo = GsonTools.getReturnObject(result, UserAddressInfo.class);
			if (userAddressInfo != null)
			{
				DbUserAddressInfoFactory.getInstance().addOrUpdateUserAddressInfo(userAddressInfo);
			}
		}
		else if (url.endsWith(ApiConfig.UPDATE_ADDRESS_STATUS))
		{
			UserAddressInfo userAddressInfo = GsonTools.getReturnObject(result, UserAddressInfo.class);
			if (userAddressInfo != null)
			{
				DbUserAddressInfoFactory.getInstance().addOrUpdateUserAddressInfo(userAddressInfo);
			}
		}
		else if (url.endsWith(ApiConfig.DELETE_ADDRESS_INFO))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				DbUserAddressInfoFactory.getInstance().deleteUserAddressInfo(returnInfo.getErrmsg());
			}
		}
		else if (url.endsWith(ApiConfig.GET_ADDRESS_INFO))
		{
			UserAddressInfo userAddressInfo = GsonTools.getReturnObject(result, UserAddressInfo.class);
			if (userAddressInfo != null)
			{
				DbUserAddressInfoFactory.getInstance().addOrUpdateUserAddressInfo(userAddressInfo);
			}
		}
		else if (url.endsWith(ApiConfig.DELETE_ADDRESS_INFO))
		{
			String addressId = GsonTools.getReturnObject(result, String.class);
			if (!StringTools.isNull(addressId))
			{
				DbUserAddressInfoFactory.getInstance().deleteUserAddressInfo(addressId);
			}
		}
	}
}
