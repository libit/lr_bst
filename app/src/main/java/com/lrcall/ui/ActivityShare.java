/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.ui.customer.DisplayTools;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.BitmapTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.CryptoTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;
import com.lrcall.utils.ZXingUtils;

import java.io.File;

public class ActivityShare extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityShare.class.getSimpleName();
	private ImageView ivQr;
	private String mData = "";
	private Bitmap bitmap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			mData = bundle.getString(ConstValues.DATA_SHARE_DATA);
		}
		viewInit();
		if (!StringTools.isNull(mData))
		{
			bitmap = ZXingUtils.Create2QR(mData, DisplayTools.getWindowWidth(this) / 2);
			BitmapTools.saveBmpFile(bitmap, AppConfig.getPicFile(CryptoTools.getMD5Str(mData) + ".jpg"));
		}
		ivQr.setImageBitmap(bitmap);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		ivQr = (ImageView) findViewById(R.id.iv_qr);
		findViewById(R.id.btn_share).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_share:
			{
				Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送到属性
				intent.setType("image/*"); // 分享发送到数据类型
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享"); // 分享的主题
				intent.putExtra(Intent.EXTRA_TEXT, "分享的内容"); // 分享的内容
				intent.putExtra("sms_body", "分享到短信应用");//如果是分享到短信应用
				Uri imageUri = Uri.fromFile(new File(AppConfig.getPicFile(CryptoTools.getMD5Str(mData) + ".jpg")));
				intent.putExtra(Intent.EXTRA_STREAM, imageUri); // 分享的内容
				startActivity(Intent.createChooser(intent, "来自" + getString(R.string.app_name) + "的分享"));
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.ADD_ADDRESS_INFO))
		{
			showServerMsg(result, "");
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				finish();
			}
			return true;
		}
		return false;
	}
}
