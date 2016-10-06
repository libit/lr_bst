/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.models.UserAddressInfo;
import com.lrcall.appbst.services.AddressService;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.db.DbUserAddressInfoFactory;
import com.lrcall.enums.AddressStatus;
import com.lrcall.ui.adapter.AddressAdapter;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityAddressManage extends MyBaseActivity implements IAjaxDataResponse
{
	private static final String TAG = ActivityAddressManage.class.getSimpleName();
	public static final int REQ_EDIT = 100;
	public static final int REQ_ADD = 101;
	private ListView xListView;
	private AddressService mAddressService;
	private List<UserAddressInfo> mUserAddressInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_manage);
		mAddressService = new AddressService(this);
		mAddressService.addDataResponse(this);
		viewInit();
		initData();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		xListView = (ListView) findViewById(R.id.xlist);
		registerForContextMenu(xListView);
	}

	private void initData()
	{
		mUserAddressInfoList = DbUserAddressInfoFactory.getInstance().getUserAddressInfoList(PreferenceUtils.getInstance().getUsername());
		AddressAdapter.IAddressAdapterItemClicked addressAdapterItemClicked = new AddressAdapter.IAddressAdapterItemClicked()
		{
			@Override
			public void onAddressClicked(View v, UserAddressInfo userAddressInfo)
			{
				Intent intent = new Intent(ActivityAddressManage.this, ActivityAddressEdit.class);
				intent.putExtra(ConstValues.DATA_ADDRESS_ID, userAddressInfo.getAddressId());
				ActivityAddressManage.this.startActivityForResult(intent, REQ_EDIT);
			}

//			@Override
//			public void onAddressEditClicked(View v, UserAddressInfo userAddressInfo)
//			{
//				Intent intent = new Intent(ActivityAddressManage.this, ActivityAddressEdit.class);
//				intent.putExtra(ConstValues.DATA_ADDRESS_ID, userAddressInfo.getAddressId());
//				ActivityAddressManage.this.startActivityForResult(intent, REQ_EDIT);
//			}
		};
		AddressAdapter addressAdapter = new AddressAdapter(this, mUserAddressInfoList, null);
		xListView.setAdapter(addressAdapter);
		mAddressService.getUserAddressInfoList(null, true);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.context_menu_address, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		LogcatTools.debug(TAG, "点击菜单");
		AdapterView.AdapterContextMenuInfo info;
		try
		{
			info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		}
		catch (ClassCastException e)
		{
			return false;
		}
		UserAddressInfo userAddressInfo = (UserAddressInfo) xListView.getAdapter().getItem(info.position);
		if (userAddressInfo == null)
		{
			return false;
		}
		switch (item.getItemId())
		{
			case R.id.action_set_default:
			{
				mAddressService.updateUserAddressInfoStatus(userAddressInfo.getAddressId(), AddressStatus.DEFAULT.getStatus(), "请稍后...", true);
				return true;
			}
			case R.id.action_delete_address:
			{
				mAddressService.deleteUserAddressInfo(userAddressInfo.getAddressId(), "请稍后...", true);
				return true;
			}
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_ADDRESS_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<UserAddressInfo> userAddressInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<UserAddressInfo>>()
				{
				}.getType());
				if (userAddressInfoList != null && userAddressInfoList.size() > 0)
				{
					mUserAddressInfoList.clear();
					initData();
				}
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.UPDATE_ADDRESS_STATUS) || url.endsWith(ApiConfig.DELETE_ADDRESS_INFO))
		{
			mUserAddressInfoList.clear();
			initData();
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
		if (id == R.id.action_add_address)
		{
			startActivityForResult(new Intent(this, ActivityAddressAdd.class), REQ_ADD);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_ADD || requestCode == REQ_EDIT)
		{
			if (resultCode == RESULT_OK)
			{
				initData();
			}
		}
	}
}
