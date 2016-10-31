/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.PicInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PicService;
import com.lrcall.appbst.services.ShopService;
import com.lrcall.ui.customer.DisplayTools;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.utils.CallTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

public class ActivityShopAuth extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private EditText etName, etNumber, etAddress, etEmail, etIdentity, etBusinessLicense;
	private ImageView ivIdentityCardPicId1, ivIdentityCardPicId2, ivBusinessLicensePicId;
	private ShopService mShopService;
	private String picId, identityCardPicId1, identityCardPicId2, businessLicensePicId;
	private int currentIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_auth);
		viewInit();
		mShopService = new ShopService(this);
		mShopService.addDataResponse(this);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		etName = (EditText) findViewById(R.id.et_name);
		etNumber = (EditText) findViewById(R.id.et_number);
		etAddress = (EditText) findViewById(R.id.et_address);
		etEmail = (EditText) findViewById(R.id.et_email);
		etIdentity = (EditText) findViewById(R.id.et_identity);
		etBusinessLicense = (EditText) findViewById(R.id.et_business_license);
		ivIdentityCardPicId1 = (ImageView) findViewById(R.id.iv_identity1);
		ivIdentityCardPicId2 = (ImageView) findViewById(R.id.iv_identity2);
		ivBusinessLicensePicId = (ImageView) findViewById(R.id.iv_business_license);
		ivIdentityCardPicId1.setOnClickListener(this);
		ivIdentityCardPicId2.setOnClickListener(this);
		ivBusinessLicensePicId.setOnClickListener(this);
		findViewById(R.id.btn_submit).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.iv_identity1:
			{
				currentIndex = 1;
				selectPhoto();
				break;
			}
			case R.id.iv_identity2:
			{
				currentIndex = 2;
				selectPhoto();
				break;
			}
			case R.id.iv_business_license:
			{
				currentIndex = 3;
				selectPhoto();
				break;
			}
			case R.id.btn_submit:
			{
				String name = etName.getText().toString();
				String number = etNumber.getText().toString();
				String address = etAddress.getText().toString();
				String email = etEmail.getText().toString();
				String identityCard = etIdentity.getText().toString();
				String businessLicense = etBusinessLicense.getText().toString();
				byte sex = (byte) 2;
				//				identityCardPicId1 = (String) ivIdentityCardPicId1.getTag();
				//				identityCardPicId2 = (String) ivIdentityCardPicId2.getTag();
				//				businessLicensePicId = (String) ivBusinessLicensePicId.getTag();
				String remark = null;
				if (StringTools.isNull(name))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "姓名不能为空！");
					etName.requestFocus();
					return;
				}
				if (StringTools.isNull(number))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "手机号码不能为空！");
					etNumber.requestFocus();
					return;
				}
				if (!CallTools.isChinaMobilePhoneNumber(number))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "手机号码格式不正确！");
					etNumber.requestFocus();
					return;
				}
				if (StringTools.isNull(address))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "地址不能为空！");
					etAddress.requestFocus();
					return;
				}
				if (StringTools.isNull(email))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "邮箱不能为空！");
					etEmail.requestFocus();
					return;
				}
				if (!StringTools.isEmail(email))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "邮箱格式不正确！");
					etEmail.requestFocus();
					return;
				}
				if (StringTools.isNull(identityCard))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "身份证号码不能为空！");
					etIdentity.requestFocus();
					return;
				}
				if (StringTools.isNull(businessLicense))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "营业执照号码不能为空！");
					etBusinessLicense.requestFocus();
					return;
				}
				if (StringTools.isNull(identityCardPicId1))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "身份证正面图片未上传！");
					ivIdentityCardPicId1.requestFocus();
					return;
				}
				if (StringTools.isNull(identityCardPicId2))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "身份证反面图片未上传！");
					ivIdentityCardPicId2.requestFocus();
					return;
				}
				if (StringTools.isNull(businessLicensePicId))
				{
					ToastView.showCenterToast(this, R.drawable.ic_do_fail, "营业执照图片未上传！");
					ivBusinessLicensePicId.requestFocus();
					return;
				}
				mShopService.auth(name, number, sex, address, email, picId, identityCard, identityCardPicId1, identityCardPicId2, businessLicense, businessLicensePicId, remark, "正在申请...", true);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.SHOP_AUTH))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				setResult(RESULT_OK);
				finish();
				ToastView.showCenterToast(this, R.drawable.ic_done, "审核资料上传成功,请等待管理员审核！");
			}
			else
			{
				String msg = result;
				if (returnInfo != null)
				{
					msg = returnInfo.getErrmsg();
				}
				ToastView.showCenterToast(this, R.drawable.ic_do_fail, "资料上传失败:" + msg);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.SHOP_UPDATE_PIC))
		{
			PicInfo picInfo = GsonTools.getReturnObject(result, PicInfo.class);
			if (picInfo != null)
			{
				ToastView.showCenterToast(this, R.drawable.ic_done, "图片上传成功！");
				ImageView iv = null;
				if (currentIndex == 0)
				{
					picId = picInfo.getPicId();
				}
				else if (currentIndex == 1)
				{
					identityCardPicId1 = picInfo.getPicId();
					iv = ivIdentityCardPicId1;
				}
				else if (currentIndex == 2)
				{
					identityCardPicId2 = picInfo.getPicId();
					iv = ivIdentityCardPicId2;
				}
				else if (currentIndex == 3)
				{
					businessLicensePicId = picInfo.getPicId();
					iv = ivBusinessLicensePicId;
				}
				if (iv != null)
				{
					PicService.ajaxGetPic(iv, ApiConfig.getServerPicUrl(picInfo.getPicUrl()), DisplayTools.getWindowWidth(this) / 3);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	protected void picSelected(Bitmap bitmap)
	{
		super.picSelected(bitmap);
		ShopService shopService = new ShopService(this);
		shopService.addDataResponse(this);
		shopService.updateShopPic(bitmap, "正在上传图片,请稍后...", true);
	}
}
