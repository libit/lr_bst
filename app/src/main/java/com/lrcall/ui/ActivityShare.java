/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UserService;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.BitmapTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.CryptoTools;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;
import com.lrcall.utils.ZXingUtils;
import com.lrcall.utils.apptools.AppFactory;

public class ActivityShare extends MyBaseActivity implements View.OnClickListener
{
	private static final String TAG = ActivityShare.class.getSimpleName();
	private ImageView ivQr;
	private TextView tvContent, tvTipsTitle, tvTips;
	private String mData = "";
	private String mContent = "";
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
			mContent = bundle.getString(ConstValues.DATA_SHARE_CONTENT);
		}
		viewInit();
		if (!StringTools.isNull(mData))
		{
			bitmap = ZXingUtils.Create2QR(mData, DisplayTools.getWindowWidth(this) / 2);
			BitmapTools.saveBmpFile(bitmap, AppConfig.getPicFile(CryptoTools.getMD5Str(mData) + ".jpg"));
		}
		ivQr.setImageBitmap(bitmap);
		tvContent.setText(mContent);
		if (mData.contains(ApiConfig.getServerRegisterUrl(PreferenceUtils.getInstance().getUserId())))
		{
			UserService userService = new UserService(this);
			userService.addDataResponse(new IAjaxDataResponse()
			{
				@Override
				public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
				{
					ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
					if (ReturnInfo.isSuccess(returnInfo))
					{
						String tips = returnInfo.getMsg();
						tips = tips.replace("<br>", "\r\n");
						tips = tips.replace("<br/>", "\r\n");
						tips = tips.replace("<br />", "\r\n");
						tvTips.setText(tips);
						tvTipsTitle.setVisibility(View.VISIBLE);
					}
					else
					{
						tvTips.setText("");
						tvTipsTitle.setVisibility(View.GONE);
					}
					return false;
				}
			});
			userService.shareTips(null, false);
		}
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		ivQr = (ImageView) findViewById(R.id.iv_qr);
		tvContent = (TextView) findViewById(R.id.tv_content);
		tvTipsTitle = (TextView) findViewById(R.id.tv_tips_title);
		tvTips = (TextView) findViewById(R.id.tv_tips);
		findViewById(R.id.btn_share).setOnClickListener(this);
		tvContent.setOnClickListener(this);
		ivQr.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				BitmapTools.saveBmpFile(bitmap, AppConfig.getSysPicFile(AppConfig.AGENT_ID + "_" + CryptoTools.getMD5Str(mData) + ".jpg"));
				Toast.makeText(ActivityShare.this, "二维码已保存到系统相册！", Toast.LENGTH_LONG).show();
				return true;
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_share:
			{
				//				Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送到属性
				//				intent.setType("image/*"); // 分享发送到数据类型
				//				intent.putExtra(Intent.EXTRA_SUBJECT, "分享"); // 分享的主题
				//				intent.putExtra(Intent.EXTRA_TEXT, mContent); // 分享的内容
				//				intent.putExtra("sms_body", mContent);//如果是分享到短信应用
				//				Uri imageUri = Uri.fromFile(new File(AppConfig.getPicFile(CryptoTools.getMD5Str(mData) + ".jpg")));
				//				intent.putExtra(Intent.EXTRA_STREAM, imageUri); // 分享的内容
				//				startActivity(Intent.createChooser(intent, "来自" + getString(R.string.app_name) + "的分享"));
				Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送到属性
				intent.setType("text/plain"); // 分享发送到数据类型
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享"); // 分享的主题
				intent.putExtra(Intent.EXTRA_TEXT, mContent); // 分享的内容
				intent.putExtra("sms_body", mContent);//如果是分享到短信应用
				startActivity(Intent.createChooser(intent, "来自" + getString(R.string.app_name) + "的分享"));
				break;
			}
			case R.id.tv_content:
			{
				if (AppFactory.isCompatible(11))
				{
					ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
					String text = tvContent.getText().toString();
					ClipData myClip = ClipData.newPlainText("text", text);
					myClipboard.setPrimaryClip(myClip);
					Toast.makeText(this, "文字已复制到剪切板！", Toast.LENGTH_LONG).show();
				}
				break;
			}
		}
	}
}
