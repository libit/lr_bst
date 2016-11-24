/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.appbst.services;

import android.content.Context;
import android.graphics.Bitmap;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.models.TableOrderInfo;
import com.lrcall.appbst.models.TableSearchInfo;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.BitmapTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家服务类，用于操作与商家相关的服务
 * Created by libit on 16/4/6.
 */
public class ShopService extends BaseService
{
	public ShopService(Context context)
	{
		super(context);
	}

	/**
	 * 商家注册
	 *
	 * @param name     姓名
	 * @param nickname 昵称
	 * @param email    邮箱
	 * @param picId    图片ID
	 * @param code     验证码
	 */
	public void register(String name, String nickname, String email, String picId, String code, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("shopId", PreferenceUtils.getInstance().getUserId());
		params.put("name", name);
		params.put("nickname", nickname);
		params.put("email", email);
		params.put("picId", picId);
		params.put("code", code);
		ajaxStringCallback(ApiConfig.SHOP_REGISTER, params, tips, needServiceProcessData);
	}

	/**
	 * 商家认证
	 *
	 * @param name                   法人姓名
	 * @param number                 手机号码
	 * @param sex                    性别
	 * @param address                地址
	 * @param email                  邮箱
	 * @param picId                  头像ID
	 * @param identityCard           身份证号码
	 * @param identityCardPicId1     身份证号码正面图片ID
	 * @param identityCardPicId2     身份证号码反面图片ID
	 * @param businessLicense        营业执照号码
	 * @param businessLicensePicId   营业执照号码图片ID
	 * @param remark                 备注
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void auth(String name, String number, byte sex, String address, String email, String picId, String identityCard, String identityCardPicId1, String identityCardPicId2, String businessLicense, String businessLicensePicId, String remark, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("shopId", PreferenceUtils.getInstance().getUserId());
		params.put("name", name);
		params.put("number", number);
		params.put("sex", sex);
		params.put("address", address);
		params.put("email", email);
		params.put("picId", picId);
		params.put("identityCard", identityCard);
		params.put("identityCardPicId1", identityCardPicId1);
		params.put("identityCardPicId2", identityCardPicId2);
		params.put("businessLicense", businessLicense);
		params.put("businessLicensePicId", businessLicensePicId);
		params.put("remark", remark);
		ajaxStringCallback(ApiConfig.SHOP_AUTH, params, tips, needServiceProcessData);
	}

	/**
	 * 获取商家信息
	 */
	public void getShopInfo(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_SHOP_INFO, params, tips, needServiceProcessData);
	}

	/**
	 * 上传图片
	 *
	 * @param bitmap 图片
	 */
	public void updateShopPic(Bitmap bitmap, String tips, final boolean needServiceProcessData)
	{
		byte[] data = null;
		ByteArrayOutputStream b = BitmapTools.compressToByteArrayOutputStream(bitmap);
		if (b != null)
		{
			data = b.toByteArray();
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pic", data);
		params.put("picFileName", "shop_" + System.currentTimeMillis() + ".jpg");
		params.put("picContentType", "jpg");
		params.put("sortId", 6);
		ajaxStringCallback(ApiConfig.SHOP_UPDATE_PIC, params, tips, needServiceProcessData);
	}

	/**
	 * 获取商家列表
	 *
	 * @param condition              查询条件
	 * @param start
	 * @param size
	 * @param orderInfos
	 * @param searchInfos
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void getShopList(String condition, int start, int size, List<TableOrderInfo> orderInfos, List<TableSearchInfo> searchInfos, String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("length", size);
		if (!StringTools.isNull(condition))
		{
			params.put("search[value]", condition);
		}
		ajaxStringCallback(ApiConfig.GET_SHOP_LIST, params, tips, needServiceProcessData);
	}

	/**
	 * 获取商家销售数据
	 *
	 * @param tips
	 * @param needServiceProcessData
	 */
	public void getSaleData(String tips, final boolean needServiceProcessData)
	{
		Map<String, Object> params = new HashMap<>();
		ajaxStringCallback(ApiConfig.GET_SHOP_SALE_DATA, params, tips, needServiceProcessData);
	}

	@Override
	public void parseData(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.SHOP_REGISTER))
		{
		}
	}

	@Override
	protected void parseData(String url, File file, AjaxStatus status)
	{
		super.parseData(url, file, status);
		if (url.endsWith("jpg"))
		{
			if (file != null)
			{
				String userId = PreferenceUtils.getInstance().getUserId();
				String headPath = AppConfig.getShopPicCacheDir(userId);
				File headFile = new File(headPath);
				File f = new File(headPath.substring(0, headPath.lastIndexOf("/")));
				if (!f.exists())
				{
					f.mkdirs();
				}
				FileInputStream fileInputStream = null;
				FileOutputStream fileOutputStream = null;
				try
				{
					fileInputStream = new FileInputStream(file);
					fileOutputStream = new FileOutputStream(headFile);
					byte[] buffer = new byte[1024000];
					fileInputStream.read(buffer);
					fileOutputStream.write(buffer);
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					if (fileInputStream != null)
					{
						try
						{
							fileInputStream.close();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
					if (fileOutputStream != null)
					{
						try
						{
							fileOutputStream.close();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
