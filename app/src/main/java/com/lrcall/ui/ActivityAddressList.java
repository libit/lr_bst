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
import com.lrcall.db.DbUserAddressInfoFactory;
import com.lrcall.ui.adapter.AddressAdapter;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityAddressList extends MyBaseActivity implements IAjaxDataResponse, XListView.IXListViewListener
{
	private static final String TAG = ActivityAddressList.class.getSimpleName();
	public static final int REQ_EDIT = 200;
	public static final int REQ_ADD = 201;
	private XListView xListView;
	private AddressService mAddressService;
	private List<UserAddressInfo> mUserAddressInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_list);
		mAddressService = new AddressService(this);
		mAddressService.addDataResponse(this);
		viewInit();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		initData();
		onRefresh();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		xListView = (XListView) findViewById(R.id.xlist);
	}

	private void initData()
	{
		mUserAddressInfoList = DbUserAddressInfoFactory.getInstance().getUserAddressInfoList(PreferenceUtils.getInstance().getUsername());
		AddressAdapter addressAdapter = new AddressAdapter(this, mUserAddressInfoList, new AddressAdapter.IAddressAdapterItemClicked()
		{
			@Override
			public void onAddressClicked(View v, UserAddressInfo userAddressInfo)
			{
				Intent intent = new Intent();
				intent.putExtra(ConstValues.DATA_ADDRESS_ID, userAddressInfo.getAddressId());
				setResult(RESULT_OK, intent);
				finish();
			}

//			@Override
//			public void onAddressEditClicked(View v, UserAddressInfo userAddressInfo)
//			{
//				Intent intent = new Intent(ActivityAddressList.this, ActivityAddressEdit.class);
//				intent.putExtra(ConstValues.DATA_ADDRESS_ID, userAddressInfo.getAddressId());
//				ActivityAddressList.this.startActivityForResult(intent, REQ_EDIT);
//			}
		});
		xListView.setAdapter(addressAdapter);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
	}

	@Override
	public void onRefresh()
	{
		mAddressService.getUserAddressInfoList(null, true);
	}

	@Override
	public void onLoadMore()
	{
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_ADDRESS_LIST))
		{
			xListView.stopRefresh();
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
