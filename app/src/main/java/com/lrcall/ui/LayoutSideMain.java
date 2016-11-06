/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UserService;
import com.lrcall.enums.EventTypeLayoutSideMain;
import com.lrcall.models.FuncInfo;
import com.lrcall.ui.adapter.FuncsHorizontalAdapter;
import com.lrcall.ui.adapter.FuncsVerticalAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.dialog.DialogList;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.BmpTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by libit on 16/6/29.
 */
public class LayoutSideMain extends LinearLayout implements View.OnClickListener, IAjaxDataResponse
{
	private final Activity mActivity;
	private final Context mContext;
	private View rootView, btnLogout;
	private TextView tvName;
	private ImageView ivPhoto;
	private UserService mUserService;
	private boolean bLogin = false;

	public LayoutSideMain(Activity context)
	{
		super(context);
		this.mActivity = context;
		this.mContext = context;
		viewInit();
	}

	public LayoutSideMain(Activity context, AttributeSet attrs)
	{
		super(context, attrs);
		this.mActivity = context;
		this.mContext = context;
		viewInit();
	}

	private void viewInit()
	{
		if (mUserService == null)
		{
			mUserService = new UserService(mContext);
		}
		mUserService.addDataResponse(this);
		rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_slide_main, null);
		tvName = (TextView) rootView.findViewById(R.id.tv_name);
		ivPhoto = (ImageView) rootView.findViewById(R.id.iv_photo);
		btnLogout = rootView.findViewById(R.id.btn_logout);
		rootView.findViewById(R.id.iv_photo).setOnClickListener(this);
		rootView.findViewById(R.id.layout_user).setOnClickListener(this);
		rootView.findViewById(R.id.layout_upload).setOnClickListener(this);
		rootView.findViewById(R.id.layout_dial).setOnClickListener(this);
		rootView.findViewById(R.id.layout_change_password).setOnClickListener(this);
		rootView.findViewById(R.id.layout_share).setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		this.addView(rootView);
		setbLogin(false);
		refresh();
	}

	public void refresh()
	{
		if (UserService.isLogin())
		{
			String userId = PreferenceUtils.getInstance().getUsername();
			tvName.setText(userId);
			mUserService.getUserInfo(null, true);
		}
		else
		{
			setbLogin(false);
		}
	}

	/**
	 * 设置用户头像
	 *
	 * @param bitmap
	 */
	public void setHeader(Bitmap bitmap)
	{
		if (bitmap != null)
		{
			ivPhoto.setImageBitmap(bitmap);
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.iv_photo:
			{
				if (isbLogin())
				{
					final List<FuncInfo> list = new ArrayList<>();
					list.add(new FuncInfo(R.drawable.ic_done_grey600_18dp, "使用手机拍照"));
					list.add(new FuncInfo(R.drawable.ic_done_grey600_18dp, "从相册选择"));
					final DialogList dialogList = new DialogList(getContext());
					FuncsHorizontalAdapter adapter = new FuncsHorizontalAdapter(getContext(), list, new FuncsVerticalAdapter.IFuncsAdapterItemClicked()
					{
						@Override
						public void onFuncClicked(FuncInfo funcInfo)
						{
							dialogList.dismiss();
							if (funcInfo.getLabel().equalsIgnoreCase(list.get(0).getLabel()))
							{
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								mActivity.startActivityForResult(intent, ConstValues.REQUEST_CAPTURE_SET_PIC);
							}
							else if (funcInfo.getLabel().equalsIgnoreCase(list.get(1).getLabel()))
							{
								Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
								albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
								mActivity.startActivityForResult(albumIntent, ConstValues.REQUEST_PICK_SET_PIC);
							}
						}
					});
					dialogList.setAdapter(adapter);
					dialogList.show();
				}
				else
				{
					mActivity.startActivityForResult(new Intent(mContext, ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
				}
				break;
			}
			case R.id.layout_upload:
			{
				EventBus.getDefault().post(EventTypeLayoutSideMain.CLOSE_DRAWER);
				if (isbLogin())
				{
					mContext.startActivity(new Intent(mContext, ActivityBackups.class));
				}
				else
				{
					mContext.startActivity(new Intent(mContext, ActivityLogin.class));
				}
				break;
			}
			case R.id.layout_dial:
			{
				EventBus.getDefault().post(EventTypeLayoutSideMain.CLOSE_DRAWER);
				//				if (isbLogin())
				{
					mContext.startActivity(new Intent(mContext, ActivityDial.class));
				}
				//				else
				//				{
				//					mContext.startActivity(new Intent(mContext, ActivityLogin.class));
				//				}
				break;
			}
			case R.id.layout_change_password:
			{
				EventBus.getDefault().post(EventTypeLayoutSideMain.CLOSE_DRAWER);
				if (isbLogin())
				{
					mContext.startActivity(new Intent(mContext, ActivityChangePwd.class));
				}
				else
				{
					mContext.startActivity(new Intent(mContext, ActivityLogin.class));
				}
				break;
			}
			case R.id.layout_share:
			{
				mUserService.share("请稍后...", true);
				break;
			}
			case R.id.btn_logout:
			{
				EventBus.getDefault().post(EventTypeLayoutSideMain.CLOSE_DRAWER);
				mUserService.logout();
				mContext.startActivity(new Intent(mContext, ActivityLogin.class));
				break;
			}
			case R.id.layout_user:
			{
				EventBus.getDefault().post(EventTypeLayoutSideMain.CLOSE_DRAWER);
				if (isbLogin())
				{
					mContext.startActivity(new Intent(mContext, ActivityUserWallet.class));
				}
				else
				{
					mContext.startActivity(new Intent(mContext, ActivityLogin.class));
				}
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_USER_INFO))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				setbLogin(true);
			}
			else
			{
				setbLogin(false);
				ToastView.showCenterToast(mContext, R.drawable.ic_do_fail, "登录信息已过期，请重新登录！");
			}
		}
		else if (url.endsWith(ApiConfig.USER_UPDATE_PIC_INFO))
		{
			Bitmap bitmap = BmpTools.getBmpFile(AppConfig.getUserPicCacheDir(PreferenceUtils.getInstance().getUsername()));
			if (bitmap != null)
			{
				ivPhoto.setImageBitmap(bitmap);
			}
			return true;
		}
		return true;
	}

	public boolean isbLogin()
	{
		return bLogin;
	}

	public void setbLogin(boolean bLogin)
	{
		this.bLogin = bLogin;
		if (bLogin)
		{
			String userId = PreferenceUtils.getInstance().getUsername();
			tvName.setText(userId);
			Bitmap bitmap = BmpTools.getBmpFile(AppConfig.getUserPicCacheDir(userId));
			if (bitmap != null)
			{
				ivPhoto.setImageBitmap(bitmap);
			}
			btnLogout.setVisibility(View.VISIBLE);
		}
		else
		{
			tvName.setText("请登录！");
			btnLogout.setVisibility(View.GONE);
		}
	}
}
