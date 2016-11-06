/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.UserAddressInfo;
import com.lrcall.appbst.services.AddressService;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.enums.AddressStatus;
import com.lrcall.events.AddressEvent;
import com.lrcall.models.FuncInfo;
import com.lrcall.ui.adapter.AddressAdapter;
import com.lrcall.ui.adapter.FuncsHorizontalAdapter;
import com.lrcall.ui.adapter.FuncsVerticalAdapter;
import com.lrcall.ui.dialog.DialogList;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ActivityAddressManage extends MyBasePageActivity implements IAjaxDataResponse, View.OnClickListener
{
	private static final String TAG = ActivityAddressManage.class.getSimpleName();
	public static final int REQ_EDIT = 100;
	public static final int REQ_ADD = 101;
	private View layoutAddressList, layoutNoAddress;
	private AddressService mAddressService;
	private List<UserAddressInfo> mUserAddressInfoList = new ArrayList<>();
	private AddressAdapter mAddressAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_manage);
		mAddressService = new AddressService(this);
		mAddressService.addDataResponse(this);
		viewInit();
		EventBus.getDefault().register(this);
		//		mUserAddressInfoList = DbUserAddressInfoFactory.getInstance().getUserAddressInfoList(PreferenceUtils.getInstance().getUsername());
		onRefresh();
	}

	@Override
	protected void onDestroy()
	{
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Subscribe
	public void onEventMainThread(AddressEvent addressEvent)
	{
		if (addressEvent != null)
		{
			//			if (addressEvent.getEvent().equals(AddressEvent.EVENT_ADD) || addressEvent.getEvent().equals(AddressEvent.EVENT_UPDATE))
			{
				onRefresh();
			}
		}
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		layoutAddressList = findViewById(R.id.layout_address_list);
		layoutNoAddress = findViewById(R.id.layout_no_address);
		xListView = (XListView) findViewById(R.id.xlist);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
		findViewById(R.id.btn_add).setOnClickListener(this);
	}

	@Override
	public void refreshData()
	{
		mUserAddressInfoList.clear();
		mAddressAdapter = null;
		loadMoreData();
	}

	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		mAddressService.getUserAddressInfoList(mDataStart, getPageSize(), null, null, tips, true);
	}

	synchronized private void refreshAddressInfoList(List<UserAddressInfo> userAddressInfoList)
	{
		if (userAddressInfoList == null || userAddressInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mUserAddressInfoList.size() < 1)
			{
				layoutAddressList.setVisibility(View.GONE);
				layoutNoAddress.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutAddressList.setVisibility(View.VISIBLE);
		layoutNoAddress.setVisibility(View.GONE);
		if (userAddressInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (UserAddressInfo userAddressInfo : userAddressInfoList)
		{
			mUserAddressInfoList.add(userAddressInfo);
		}
		if (mAddressAdapter == null)
		{
			AddressAdapter.IItemClick addressAdapterItemClicked = new AddressAdapter.IItemClick()
			{
				@Override
				public void onAddressClicked(View v, final UserAddressInfo userAddressInfo)
				{
					if (userAddressInfo != null)
					{
						final List<FuncInfo> list = new ArrayList<>();
						list.add(new FuncInfo(R.drawable.ic_done_grey600_18dp, "修改地址"));
						list.add(new FuncInfo(R.drawable.ic_done_grey600_18dp, "设为默认"));
						list.add(new FuncInfo(R.drawable.ic_done_grey600_18dp, "删除地址"));
						final DialogList dialogList = new DialogList(ActivityAddressManage.this);
						FuncsHorizontalAdapter adapter = new FuncsHorizontalAdapter(ActivityAddressManage.this, list, new FuncsVerticalAdapter.IFuncsAdapterItemClicked()
						{
							@Override
							public void onFuncClicked(FuncInfo funcInfo)
							{
								dialogList.dismiss();
								if (funcInfo.getLabel().equalsIgnoreCase(list.get(0).getLabel()))
								{
									Intent intent = new Intent(ActivityAddressManage.this, ActivityAddressEdit.class);
									intent.putExtra(ConstValues.DATA_ADDRESS_ID, userAddressInfo.getAddressId());
									ActivityAddressManage.this.startActivityForResult(intent, REQ_EDIT);
								}
								else if (funcInfo.getLabel().equalsIgnoreCase(list.get(1).getLabel()))
								{
									mAddressService.updateUserAddressInfoStatus(userAddressInfo.getAddressId(), AddressStatus.DEFAULT.getStatus(), "请稍后...", true);
								}
								else if (funcInfo.getLabel().equalsIgnoreCase(list.get(2).getLabel()))
								{
									mAddressService.deleteUserAddressInfo(userAddressInfo.getAddressId(), "请稍后...", true);
								}
							}
						});
						dialogList.setAdapter(adapter);
						dialogList.show();
					}
				}
				//			@Override
				//			public void onAddressEditClicked(View v, UserAddressInfo userAddressInfo)
				//			{
				//				Intent intent = new Intent(ActivityAddressManage.this, ActivityAddressEdit.class);
				//				intent.putExtra(ConstValues.DATA_ADDRESS_ID, userAddressInfo.getAddressId());
				//				ActivityAddressManage.this.startActivityForResult(intent, REQ_EDIT);
				//			}
			};
			mAddressAdapter = new AddressAdapter(this, mUserAddressInfoList, addressAdapterItemClicked);
			xListView.setAdapter(mAddressAdapter);
		}
		else
		{
			mAddressAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_add:
			{
				startActivityForResult(new Intent(this, ActivityAddressAdd.class), REQ_ADD);
				break;
			}
		}
	}
	//	@Override
	//	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	//	{
	//		super.onCreateContextMenu(menu, v, menuInfo);
	//		getMenuInflater().inflate(R.menu.context_menu_address, menu);
	//	}
	//
	//	@Override
	//	public boolean onContextItemSelected(MenuItem item)
	//	{
	//		LogcatTools.debug(TAG, "点击菜单");
	//		AdapterView.AdapterContextMenuInfo info;
	//		try
	//		{
	//			info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	//		}
	//		catch (ClassCastException e)
	//		{
	//			return false;
	//		}
	//		UserAddressInfo userAddressInfo = (UserAddressInfo) xListView.getAdapter().getItem(info.position);
	//		if (userAddressInfo == null)
	//		{
	//			return false;
	//		}
	//		switch (item.getItemId())
	//		{
	//			case R.id.action_set_default:
	//			{
	//				mAddressService.updateUserAddressInfoStatus(userAddressInfo.getAddressId(), AddressStatus.DEFAULT.getStatus(), "请稍后...", true);
	//				return true;
	//			}
	//			case R.id.action_delete_address:
	//			{
	//				mAddressService.deleteUserAddressInfo(userAddressInfo.getAddressId(), "请稍后...", true);
	//				return true;
	//			}
	//		}
	//		return super.onContextItemSelected(item);
	//	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_ADDRESS_LIST))
		{
			List<UserAddressInfo> userAddressInfoList = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				userAddressInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<UserAddressInfo>>()
				{
				}.getType());
			}
			refreshAddressInfoList(userAddressInfoList);
			return true;
		}
		else if (url.endsWith(ApiConfig.UPDATE_ADDRESS_STATUS) || url.endsWith(ApiConfig.DELETE_ADDRESS_INFO))
		{
			//			onRefresh();
			return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_address_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_refresh)
		{
			onRefresh();
			return true;
		}
		else if (id == R.id.action_add_address)
		{
			startActivityForResult(new Intent(this, ActivityAddressAdd.class), REQ_ADD);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_ADD || requestCode == REQ_EDIT)
		{
			if (resultCode == RESULT_OK)
			{
				//				onRefresh();
			}
		}
	}
}
