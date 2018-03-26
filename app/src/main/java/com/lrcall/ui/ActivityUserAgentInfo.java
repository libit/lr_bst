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
import com.lrcall.appbst.models.UserAgentInfo;
import com.lrcall.appbst.models.UserApplyAgentInfo;
import com.lrcall.appbst.models.UserInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UserAgentService;
import com.lrcall.appbst.services.UserService;
import com.lrcall.enums.UserApplyStatus;
import com.lrcall.enums.UserType;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.dialog.DialogSelectArea;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.BitmapTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;

public class ActivityUserAgentInfo extends MyBaseActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityUserAgentInfo.class.getSimpleName();
	private TextView tvType, tvApplyStatus;
	private ImageView ivHead;
	private Button btnUpgrade;
	private UserService mUserService;
	private UserAgentService mUserAgentService;
	private byte userType = -1;
	private boolean bFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_agent_info);
		mUserService = new UserService(this);
		mUserService.addDataResponse(this);
		mUserAgentService = new UserAgentService(this);
		mUserAgentService.addDataResponse(this);
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
		tvType = (TextView) findViewById(R.id.tv_type);
		tvApplyStatus = (TextView) findViewById(R.id.tv_apply_status);
		ivHead = (ImageView) findViewById(R.id.iv_head);
		btnUpgrade = (Button) findViewById(R.id.btn_upgrade);
		btnUpgrade.setOnClickListener(this);
		findViewById(R.id.layout_query_balance).setOnClickListener(this);
		findViewById(R.id.layout_my_fans).setOnClickListener(this);
		findViewById(R.id.layout_my_performance).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.layout_query_balance:
			{
				startActivity(new Intent(this, ActivityQueryUserBalance.class));
				break;
			}
			case R.id.layout_my_fans:
			{
				startActivity(new Intent(this, ActivityAgentUserList.class));
				break;
			}
			case R.id.layout_my_performance:
			{
				startActivity(new Intent(this, ActivityUserAgentSharePorfitList.class));
				break;
			}
			case R.id.btn_upgrade:
			{
				final byte type = UserType.getNextType(userType);
				if (type == (byte) -1)
				{
					ToastView.showCenterToast(ActivityUserAgentInfo.this, R.drawable.ic_do_fail, "申请级别错误!");
					return;
				}
				new DialogSelectArea(this, type, new DialogSelectArea.OnSelectAreaListenser()
				{
					@Override
					public void onOkClick(byte agentType, String provinceId, String cityId, String countryId)
					{
						//						LogcatTools.debug("DialogSelectArea", "provinceId:" + provinceId + ",cityId:" + cityId + ",countryId:" + countryId);
						mUserAgentService.applyAgent(agentType, provinceId, cityId, countryId, "正在申请,请稍后...", true);
					}

					@Override
					public void onCancelClick()
					{
					}
				}).show();
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
				userType = userInfo.getUserType();
				tvType.setText(UserType.getDesc(userType));
				if (userType != UserType.COMMON.getType())
				{
					mUserAgentService.getUserAgentInfo(null, false);
				}
				mUserAgentService.getUserLastApplyAgentInfo(null, false);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.USER_APPLY_AGENT))
		{
			showServerMsg(result, null);
			mUserService.getUserInfo(null, false);
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_USER_AGENT_INFO))
		{
			UserAgentInfo userAgentInfo = GsonTools.getReturnObject(result, UserAgentInfo.class);
			if (userAgentInfo != null)
			{
				findViewById(R.id.layout_funcs).setVisibility(View.VISIBLE);
				userType = userAgentInfo.getUserType();
				String provinceName = userAgentInfo.getProvinceId();
				if (userAgentInfo.getProvinceInfo() != null)
				{
					provinceName = userAgentInfo.getProvinceInfo().getName();
				}
				String cityName = userAgentInfo.getCityId();
				if (userAgentInfo.getCityInfo() != null)
				{
					cityName = " " + userAgentInfo.getCityInfo().getName();
				}
				String countryName = userAgentInfo.getCountryId();
				if (userAgentInfo.getCountryInfo() != null)
				{
					countryName = " " + userAgentInfo.getCountryInfo().getName();
				}
				tvType.setText(String.format("%s(%s%s%s)", UserType.getDesc(userType), provinceName, cityName, countryName));
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_USER_LAST_APPLY_AGENT_INFO))
		{
			UserApplyAgentInfo userApplyAgentInfo = GsonTools.getReturnObject(result, UserApplyAgentInfo.class);
			if (userApplyAgentInfo != null)
			{
				if (userApplyAgentInfo.getStatus() == UserApplyStatus.VERIFY_FAIL.getStatus())
				{
					tvApplyStatus.setVisibility(View.VISIBLE);
					btnUpgrade.setVisibility(View.VISIBLE);
					tvApplyStatus.setText(String.format("申请 %s %s %s代理未通过!", userApplyAgentInfo.getProvinceId(), userApplyAgentInfo.getCityId(), userApplyAgentInfo.getCountryId()));
					return true;
				}
				else if (userApplyAgentInfo.getStatus() == UserApplyStatus.APPLY.getStatus())
				{
					tvApplyStatus.setVisibility(View.VISIBLE);
					btnUpgrade.setVisibility(View.GONE);
					tvApplyStatus.setText(String.format("正在申请 %s %s %s代理!", userApplyAgentInfo.getProvinceId(), userApplyAgentInfo.getCityId(), userApplyAgentInfo.getCountryId()));
					return true;
				}
			}
			btnUpgrade.setVisibility(View.VISIBLE);
			tvApplyStatus.setVisibility(View.GONE);
			return true;
		}
		return false;
	}
}
