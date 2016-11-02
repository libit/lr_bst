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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.models.FuncInfo;
import com.lrcall.ui.adapter.FuncsHorizontalAdapter;
import com.lrcall.ui.adapter.FuncsVerticalAdapter;
import com.lrcall.ui.customer.DisplayTools;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.dialog.DialogList;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * 自定义ActionBar基类
 * Created by libit on 15/8/30.
 */
public abstract class MyBaseActivity extends SwipeBackActivity
{
	protected Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//		setOverflowShowingAlways();
	}

	/**
	 * 设置标题
	 *
	 * @param title
	 */
	@Override
	public void setTitle(CharSequence title)
	{
		if (mToolbar != null)
		{
			mToolbar.setTitle(title);
		}
		else
		{
			super.setTitle(title);
		}
	}

	protected void viewInit()
	{
		mToolbar = (Toolbar) findViewById(R.id.toolbar_title);
		if (mToolbar != null)
		{
			mToolbar.setTitle(getTitle().toString());// 标题的文字需在setSupportActionBar之前，不然会无效
			// mToolbar.setSubtitle("副标题");
			setSupportActionBar(mToolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		//		setBackButton();
		//设置滑动返回区域
		getSwipeBackLayout().setEdgeSize(DisplayTools.getWindowWidth(this) / 2);
	}

	//后退按钮
	protected void setBackButton()
	{
		//		mToolbar.setNavigationIcon(R.drawable.back);
		if (mToolbar != null)
		{
			mToolbar.setNavigationOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					finish();
				}
			});
		}
	}

	/**
	 * 选择照片
	 */
	protected void selectPhoto()
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
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
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
				picSelected(bitmap);
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
	 * 图片选择之后的回调方法
	 *
	 * @param bitmap
	 */
	protected void picSelected(Bitmap bitmap)
	{
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

	/**
	 * 显示服务器返回的消息--json格式
	 *
	 * @param jsonResult json数据
	 */
	public void showServerMsg(String jsonResult)
	{
		ReturnInfo returnInfo = GsonTools.getReturnInfo(jsonResult);
		if (ReturnInfo.isSuccess(returnInfo))
		{
			ToastView.showCenterToast(this, R.drawable.ic_done, returnInfo.getErrmsg());
		}
		else
		{
			String msg = jsonResult;
			if (returnInfo != null)
			{
				msg = returnInfo.getErrmsg();
			}
			ToastView.showCenterToast(this, R.drawable.ic_do_fail, msg);
		}
	}
}
