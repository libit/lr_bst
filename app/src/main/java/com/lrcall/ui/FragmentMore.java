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
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ClientConfigInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.UserBalanceInfo;
import com.lrcall.appbst.models.UserInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ProductStarService;
import com.lrcall.appbst.services.UserService;
import com.lrcall.enums.OrderStatus;
import com.lrcall.enums.UserLevel;
import com.lrcall.enums.UserType;
import com.lrcall.events.UserEvent;
import com.lrcall.models.FuncInfo;
import com.lrcall.ui.adapter.FuncsHorizontalAdapter;
import com.lrcall.ui.adapter.FuncsVerticalAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.dialog.DialogList;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.BmpTools;
import com.lrcall.utils.CallTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.StringTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.lrcall.appbst.R.id.layout_apply_agent;
import static com.lrcall.appbst.R.id.layout_apply_shop;

public class FragmentMore extends MyBaseFragment implements XListView.IXListViewListener, View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = FragmentMore.class.getSimpleName();
	private XListView xListView;
	private View headView, vLogined, vUnLogin;
	private TextView tvName, tvUserType, tvOfficalWeb, tvServerPhone, tvBalance, tvFreezeBalance, tvPoint, tvStarCount, tvHistoryCount;
	private ImageView ivPhoto;
	private UserService mUserService;
	private ProductStarService mProductStarService;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mUserService = new UserService(this.getContext());
		mUserService.addDataResponse(this);
		mProductStarService = new ProductStarService(this.getContext());
		mProductStarService.addDataResponse(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_more, container, false);
		viewInit(rootView);
		initData();
		EventBus.getDefault().register(this);
		return rootView;
	}

	@Override
	public void onDestroyView()
	{
		EventBus.getDefault().unregister(this);
		super.onDestroyView();
	}

	@Override
	protected void viewInit(View rootView)
	{
		xListView = (XListView) rootView.findViewById(R.id.xlist);
		headView = LayoutInflater.from(this.getActivity()).inflate(R.layout.fragment_more_head, null);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.addHeaderView(headView);
		xListView.setAdapter(null);
		xListView.setXListViewListener(this);
		vUnLogin = rootView.findViewById(R.id.layout_unlogin);
		vLogined = rootView.findViewById(R.id.layout_logined);
		tvName = (TextView) rootView.findViewById(R.id.tv_name);
		tvUserType = (TextView) rootView.findViewById(R.id.tv_user_type);
		tvOfficalWeb = (TextView) rootView.findViewById(R.id.tv_url);
		tvServerPhone = (TextView) rootView.findViewById(R.id.tv_server_phone);
		tvBalance = (TextView) rootView.findViewById(R.id.tv_balance);
		tvFreezeBalance = (TextView) rootView.findViewById(R.id.tv_freeze_balance);
		tvPoint = (TextView) rootView.findViewById(R.id.tv_point);
		tvStarCount = (TextView) rootView.findViewById(R.id.tv_star_count);
		tvHistoryCount = (TextView) rootView.findViewById(R.id.tv_history_count);
		ivPhoto = (ImageView) rootView.findViewById(R.id.iv_photo);
		rootView.findViewById(R.id.layout_orders).setOnClickListener(this);
		rootView.findViewById(R.id.layout_order_wait_pay).setOnClickListener(this);
		rootView.findViewById(R.id.layout_order_wait_ship).setOnClickListener(this);
		rootView.findViewById(R.id.layout_order_wait_confirm_ship).setOnClickListener(this);
		rootView.findViewById(R.id.layout_order_finish).setOnClickListener(this);
		rootView.findViewById(R.id.layout_user_wallet).setOnClickListener(this);
		rootView.findViewById(R.id.layout_address).setOnClickListener(this);
		rootView.findViewById(R.id.layout_change_password).setOnClickListener(this);
		rootView.findViewById(R.id.layout_advice).setOnClickListener(this);
		rootView.findViewById(R.id.layout_server_phone).setOnClickListener(this);
		rootView.findViewById(R.id.layout_offical_web).setOnClickListener(this);
		rootView.findViewById(R.id.btn_login).setOnClickListener(this);
		rootView.findViewById(R.id.layout_star).setOnClickListener(this);
		rootView.findViewById(R.id.layout_history).setOnClickListener(this);
		rootView.findViewById(R.id.layout_settings).setOnClickListener(this);
		rootView.findViewById(layout_apply_shop).setOnClickListener(this);
		rootView.findViewById(layout_apply_agent).setOnClickListener(this);
		ivPhoto.setOnClickListener(this);
		super.viewInit(rootView);
	}

	protected void initData()
	{
		mUserService.getClientConfig(null, false);
		String config = PreferenceUtils.getInstance().getStringValue(PreferenceUtils.CLIENT_CONFIG);
		ClientConfigInfo clientConfigInfo = GsonTools.getReturnObject(config, ClientConfigInfo.class);
		if (clientConfigInfo != null)
		{
			tvServerPhone.setText(clientConfigInfo.getKefuNumber());
			tvOfficalWeb.setText(clientConfigInfo.getOfficalWeb());
		}
		String userId = PreferenceUtils.getInstance().getUsername();
		String sessionId = PreferenceUtils.getInstance().getSessionId();
		if (!StringTools.isNull(userId) && !StringTools.isNull(sessionId))
		{
			setbLogin(true);
			mUserService.getUserInfo(null, false);
			mUserService.getUserBalanceInfo(null, false);
		}
		else
		{
			setbLogin(false);
		}
	}

	@Subscribe
	public void onEventMainThread(UserEvent userEvent)
	{
		LogcatTools.debug(TAG, "收到用户登录登出操作！");
		setbLogin(isbLogin());
		if (isbLogin())
		{
			mUserService.getUserInfo(null, false);
		}
	}

	@Override
	public void onRefresh()
	{
		setbLogin(isbLogin());
		xListView.stopRefresh();
	}

	@Override
	public void onLoadMore()
	{
		xListView.stopLoadMore();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_login:
			{
				startActivityForResult(new Intent(this.getContext(), ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
				break;
			}
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
				else
				{
					startActivityForResult(new Intent(this.getContext(), ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
				}
				break;
			}
			case R.id.layout_orders:
			{
				if (isbLogin())
				{
					Intent intent = new Intent(this.getContext(), ActivityOrders.class);
					intent.putExtra(ConstValues.DATA_ORDER_TYPE, OrderStatus.WAIT_PAY.getStatus());
					startActivity(intent);
				}
				else
				{
					startActivityForResult(new Intent(this.getContext(), ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
				}
				break;
			}
			case R.id.layout_order_wait_pay:
			{
				if (isbLogin())
				{
					Intent intent = new Intent(this.getContext(), ActivityOrders.class);
					intent.putExtra(ConstValues.DATA_ORDER_TYPE, OrderStatus.WAIT_PAY.getStatus());
					startActivity(intent);
				}
				else
				{
					startActivityForResult(new Intent(this.getContext(), ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
				}
				break;
			}
			case R.id.layout_order_wait_ship:
			{
				if (isbLogin())
				{
					Intent intent = new Intent(this.getContext(), ActivityOrders.class);
					intent.putExtra(ConstValues.DATA_ORDER_TYPE, OrderStatus.PAYED.getStatus());
					startActivity(intent);
				}
				else
				{
					startActivityForResult(new Intent(this.getContext(), ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
				}
				break;
			}
			case R.id.layout_order_wait_confirm_ship:
			{
				if (isbLogin())
				{
					Intent intent = new Intent(this.getContext(), ActivityOrders.class);
					intent.putExtra(ConstValues.DATA_ORDER_TYPE, OrderStatus.EXPRESS.getStatus());
					startActivity(intent);
				}
				else
				{
					startActivityForResult(new Intent(this.getContext(), ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
				}
				break;
			}
			case R.id.layout_order_finish:
			{
				if (isbLogin())
				{
					Intent intent = new Intent(this.getContext(), ActivityOrders.class);
					intent.putExtra(ConstValues.DATA_ORDER_TYPE, OrderStatus.FINISH.getStatus());
					startActivity(intent);
				}
				else
				{
					startActivityForResult(new Intent(this.getContext(), ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
				}
				break;
			}
			case R.id.layout_user_wallet:
			{
				if (isbLogin())
				{
					Intent intent = new Intent(this.getContext(), ActivityUserWallet.class);
					startActivity(intent);
				}
				else
				{
					startActivityForResult(new Intent(this.getContext(), ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
				}
				break;
			}
			case R.id.layout_address:
			{
				if (isbLogin())
				{
					Intent intent = new Intent(this.getContext(), ActivityAddressManage.class);
					startActivity(intent);
				}
				else
				{
					startActivityForResult(new Intent(this.getContext(), ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
				}
				break;
			}
			case R.id.layout_change_password:
			{
				if (isbLogin())
				{
					startActivity(new Intent(this.getContext(), ActivityChangePwd.class));
				}
				else
				{
					startActivityForResult(new Intent(this.getContext(), ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
				}
				break;
			}
			case R.id.layout_advice:
			{
				startActivity(new Intent(this.getContext(), ActivityAdvice.class));
				break;
			}
			case R.id.layout_server_phone:
			{
				CallTools.makeLocalCall(this.getContext(), tvServerPhone.getText().toString());
				break;
			}
			case R.id.layout_offical_web:
			{
				ActivityWebView.startWebActivity(this.getContext(), "官方网站", tvOfficalWeb.getText().toString());
				break;
			}
			case R.id.layout_star:
			{
				if (isbLogin())
				{
					startActivity(new Intent(this.getContext(), ActivityStarList.class));
				}
				else
				{
					startActivityForResult(new Intent(this.getContext(), ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
				}
				break;
			}
			case R.id.layout_history:
			{
				if (isbLogin())
				{
					startActivity(new Intent(this.getContext(), ActivityProductHistoryList.class));
				}
				else
				{
					startActivityForResult(new Intent(this.getContext(), ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
				}
				break;
			}
			case R.id.layout_settings:
			{
				startActivity(new Intent(this.getContext(), ActivitySettings.class));
				break;
			}
			case R.id.layout_apply_shop:
			{
				if (isbLogin())
				{
					startActivity(new Intent(this.getContext(), ActivityShopInfo.class));
				}
				else
				{
					startActivityForResult(new Intent(this.getContext(), ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
				}
				break;
			}
			case R.id.layout_apply_agent:
			{
				if (isbLogin())
				{
					startActivity(new Intent(this.getContext(), ActivityUserAgentInfo.class));
				}
				else
				{
					startActivityForResult(new Intent(this.getContext(), ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
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
			UserInfo userInfo = GsonTools.getReturnObject(result, UserInfo.class);
			if (userInfo == null)
			{
				// 获取失败，清空SessionId
				PreferenceUtils.getInstance().setSessionId("");
				ToastView.showCenterToast(this.getContext(), R.drawable.ic_do_fail, "登录信息已过期，请重新登录！");
				setbLogin(false);
			}
			else
			{
				setbLogin(true);
				String level = UserLevel.getLevelDesc(userInfo.getUserLevel());
				String agent = "";
				if (userInfo.getUserType() > UserType.COMMON.getType())
				{
					agent = UserType.getDesc(userInfo.getUserType());
				}
				tvUserType.setText(level + " " + agent);
			}
		}
		else if (url.endsWith(ApiConfig.GET_USER_BALANCE_INFO))
		{
			UserBalanceInfo userBalanceInfo = GsonTools.getReturnObject(result, UserBalanceInfo.class);
			if (userBalanceInfo != null)
			{
				tvBalance.setText("￥" + StringTools.getPrice(userBalanceInfo.getBalance()));
				tvFreezeBalance.setText("￥" + StringTools.getPrice(userBalanceInfo.getFreezeBalance()));
				tvPoint.setText("" + userBalanceInfo.getPoint());
			}
			return true;
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
		else if (url.endsWith(ApiConfig.GET_CLIENT_CONFIG))
		{
			ClientConfigInfo clientConfigInfo = GsonTools.getReturnObject(result, ClientConfigInfo.class);
			if (clientConfigInfo != null)
			{
				tvServerPhone.setText(clientConfigInfo.getKefuNumber());
				tvOfficalWeb.setText(clientConfigInfo.getOfficalWeb());
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_STAR_LIST_COUNT))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				tvStarCount.setText(returnInfo.getErrmsg());
			}
			else
			{
				tvStarCount.setText("0");
			}
			return true;
		}
		return true;
	}

	public boolean isbLogin()
	{
		return UserService.isLogin();
	}

	public void setbLogin(boolean bLogin)
	{
		if (bLogin)
		{
			String userId = PreferenceUtils.getInstance().getUsername();
			tvName.setText(userId);
			Bitmap bitmap = BmpTools.getBmpFile(AppConfig.getUserPicCacheDir(userId));
			if (bitmap != null)
			{
				ivPhoto.setImageBitmap(bitmap);
			}
			vUnLogin.setVisibility(View.GONE);
			vLogined.setVisibility(View.VISIBLE);
			mUserService.getUserBalanceInfo(null, false);
			mProductStarService.getProductStarInfoListCount(null, false);
		}
		else
		{
			tvName.setText("请登录！");
			tvUserType.setText("");
			vLogined.setVisibility(View.GONE);
			vUnLogin.setVisibility(View.VISIBLE);
			tvBalance.setText("0");
			tvFreezeBalance.setText("0");
			tvPoint.setText("0");
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ConstValues.REQUEST_LOGIN_USER)
		{
			if (resultCode == ConstValues.RESULT_LOGIN_SUCCESS)
			{
				setbLogin(true);
			}
			else
			{
				Toast.makeText(this.getContext(), "登录失败！", Toast.LENGTH_SHORT).show();
				setbLogin(false);
			}
		}
		else if (requestCode == ConstValues.REQUEST_CAPTURE_SET_PIC)
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
				String userId = PreferenceUtils.getInstance().getUsername();
				String userHeadPath = AppConfig.getUserPicCacheDir(userId);
				File file = new File(userHeadPath.substring(0, userHeadPath.lastIndexOf("/")));
				if (!file.exists())
				{
					file.mkdirs();
				}
				ByteArrayOutputStream b = BmpTools.compressToByteArrayOutputStream(bitmap);
				if (b != null)
				{
					mUserService.updateUserHead(b.toByteArray(), "正在上传图片...", true);
				}
				else
				{
					Toast.makeText(this.getContext(), "上传图片失败：数据为空！", Toast.LENGTH_LONG).show();
				}
				FileOutputStream f = BmpTools.compressToFileOutputStream(bitmap, userHeadPath);
				if (f == null)
				{
					Toast.makeText(this.getContext(), "保存图片到手机失败！", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				Toast.makeText(this.getContext(), "用户取消操作！", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(this.getContext(), "用户取消操作！", Toast.LENGTH_SHORT).show();
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
