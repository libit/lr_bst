/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.UserInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UserService;
import com.lrcall.enums.UserLevel;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.BitmapTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;

/**
 * 用户推荐信息
 */
public class ActivityUserReferrerInfo extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityUserReferrerInfo.class.getSimpleName();
	private TextView tvLevel, tvStatus;
	private ImageView ivHead;
	private Button btnUpgrade;
	private UserService mUserService;
	private byte userLevel = -1;
	private boolean bFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_referrer_info);
		mUserService = new UserService(this);
		mUserService.addDataResponse(this);
		viewInit();
		Bitmap bitmap = BitmapTools.getBmpFile(AppConfig.getUserPicCacheDir(PreferenceUtils.getInstance().getUserId()));
		if (bitmap != null)
		{
			ivHead.setImageBitmap(bitmap);
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		String tips = null;
		if (bFirst)
		{
			tips = "请稍后...";
		}
		bFirst = false;
		mUserService.getUserInfo(tips, false);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		tvLevel = (TextView) findViewById(R.id.tv_level);
		tvStatus = (TextView) findViewById(R.id.tv_status);
		ivHead = (ImageView) findViewById(R.id.iv_head);
		btnUpgrade = (Button) findViewById(R.id.btn_upgrade);
		btnUpgrade.setOnClickListener(this);
		findViewById(R.id.layout_manage_bankcard).setOnClickListener(this);
		findViewById(R.id.layout_my_fans).setOnClickListener(this);
		findViewById(R.id.layout_my_performance).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.layout_manage_bankcard:
			{
				startActivity(new Intent(this, ActivityUserBankList.class));
				break;
			}
			case R.id.layout_my_fans:
			{
				startActivity(new Intent(this, ActivityReferrerUserList.class));
				break;
			}
			case R.id.layout_my_performance:
			{
				startActivity(new Intent(this, ActivityUserSharePorfitList.class));
				break;
			}
			case R.id.btn_upgrade:
			{
				Intent intent = new Intent(this, ActivityUserUpgrade.class);
				startActivity(intent);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_USER_INFO))
		{
			UserInfo userInfo = GsonTools.getReturnObject(result, UserInfo.class);
			if (userInfo != null)
			{
				userLevel = userInfo.getUserLevel();
				tvLevel.setText(UserLevel.getLevelDesc(userLevel));
				if (userInfo.getUserLevel() == UserLevel.L1.getLevel())
				{
					tvStatus.setText("您现在是" + UserLevel.L1.getDesc() + "，升级到" + UserLevel.L2.getDesc() + "即可开始分销。");
					btnUpgrade.setVisibility(View.VISIBLE);
				}
				else if (userInfo.getUserLevel() == UserLevel.L2.getLevel())
				{
					tvStatus.setText("您现在是" + UserLevel.L2.getDesc() + "，可升级到" + UserLevel.L3.getDesc() + "。");
					btnUpgrade.setVisibility(View.VISIBLE);
				}
				else if (userInfo.getUserLevel() == UserLevel.L3.getLevel())
				{
					tvStatus.setText("您现在是" + UserLevel.L3.getDesc() + "，升级到" + UserLevel.L4.getDesc() + "即可开始分销自己的商品。");
					btnUpgrade.setVisibility(View.VISIBLE);
				}
				else if (userInfo.getUserLevel() == UserLevel.L4.getLevel())
				{
					tvStatus.setText("您现在是" + UserLevel.L4.getDesc() + "，可升级到" + UserLevel.L5.getDesc() + "。");
					btnUpgrade.setVisibility(View.VISIBLE);
				}
				else if (userInfo.getUserLevel() == UserLevel.L5.getLevel())
				{
					tvStatus.setText("您现在是" + UserLevel.L5.getDesc() + "。");
					btnUpgrade.setVisibility(View.GONE);
				}
				if (userInfo.getUserLevel() < UserLevel.L2.getLevel())
				{
					findViewById(R.id.layout_funcs).setVisibility(View.GONE);
				}
				else
				{
					findViewById(R.id.layout_funcs).setVisibility(View.VISIBLE);
				}
			}
			return true;
		}
		return false;
	}
}
