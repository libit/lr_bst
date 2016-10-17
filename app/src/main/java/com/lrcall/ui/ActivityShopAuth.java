/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.PicInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ShopService;
import com.lrcall.models.FuncInfo;
import com.lrcall.ui.adapter.FuncsHorizontalAdapter;
import com.lrcall.ui.adapter.FuncsVerticalAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.dialog.DialogList;
import com.lrcall.utils.BmpTools;
import com.lrcall.utils.CallTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ActivityShopAuth extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private EditText etName, etNumber, etAddress, etEmail, etIdentity, etBusinessLicense;
	private ImageView ivIdentityCardPicId1, ivIdentityCardPicId2, ivBusinessLicensePicId;
	private ShopService mShopService;
	private String picId = null;

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
			case R.id.iv_identity2:
			case R.id.iv_business_license:
			{
				final List<FuncInfo> list = new ArrayList<>();
				list.add(new FuncInfo(R.drawable.ic_done_grey600_18dp, "使用手机拍照"));
				list.add(new FuncInfo(R.drawable.ic_done_grey600_18dp, "从相册选择"));
				final DialogList dialogList = new DialogList(this);
				FuncsHorizontalAdapter adapter = new FuncsHorizontalAdapter(this, list, new FuncsVerticalAdapter.IFuncsAdapterItemClicked()
				{
					@Override
					public void onFuncClicked(FuncInfo funcInfo)
					{
						dialogList.dismiss();
						if (funcInfo.getLabel().equalsIgnoreCase(list.get(0).getLabel()))
						{
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent, ConstValues.REQUEST_CAPTURE_SET_PIC);
						}
						else if (funcInfo.getLabel().equalsIgnoreCase(list.get(1).getLabel()))
						{
							Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
							albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
							startActivityForResult(albumIntent, ConstValues.REQUEST_PICK_SET_PIC);
						}
					}
				});
				dialogList.setAdapter(adapter);
				dialogList.show();
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
				String identityCardPicId1 = (String) ivIdentityCardPicId1.getTag();
				String identityCardPicId2 = (String) ivIdentityCardPicId2.getTag();
				String businessLicensePicId = (String) ivBusinessLicensePicId.getTag();
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
				Toast.makeText(this, "审核资料上传成功,请等待管理员审核！", Toast.LENGTH_SHORT).show();
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
		else if (url.endsWith(ApiConfig.UPDATE_PIC))
		{
			PicInfo picInfo = GsonTools.getReturnObject(result, PicInfo.class);
			if (picInfo != null)
			{
				LogcatTools.debug("UPDATE_PIC", "picInfo:" + picInfo.toString());
			}
			return true;
		}
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ConstValues.REQUEST_CAPTURE_SET_PIC)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				Uri uri = data.getData();
				clipPhoto(uri);
			}
		}
		else if (requestCode == ConstValues.REQUEST_EDIT_PIC)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
				//				String userId = PreferenceUtils.getInstance().getUsername();
				//				String userHeadPath = AppConfig.getUserPicCacheDir(userId);
				//				File file = new File(userHeadPath.substring(0, userHeadPath.lastIndexOf("/")));
				//				if (!file.exists())
				//				{
				//					file.mkdirs();
				//				}
				ByteArrayOutputStream b = BmpTools.compressToByteArrayOutputStream(bitmap);
				if (b != null)
				{
					mShopService.updateHead(b.toByteArray(), "正在上传图片...", true);
				}
				else
				{
					Toast.makeText(this, "上传图片失败：数据为空！", Toast.LENGTH_LONG).show();
				}
				//				FileOutputStream f = BmpTools.compressToFileOutputStream(bitmap, userHeadPath);
				//				if (f == null)
				//				{
				//					Toast.makeText(this, "保存图片到手机失败！", Toast.LENGTH_LONG).show();
				//				}
			}
			else
			{
				Toast.makeText(this, "用户取消操作！", Toast.LENGTH_SHORT).show();
			}
		}
		else if (requestCode == ConstValues.REQUEST_PICK_SET_PIC)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				Uri uri = data.getData();
				clipPhoto(uri);
			}
			else
			{
				Toast.makeText(this, "用户取消操作！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 裁剪图片方法实现
	 *
	 * @param uri 图片来源
	 */
	public void clipPhoto(Uri uri)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, ConstValues.REQUEST_EDIT_PIC);
	}
}
