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
import com.lrcall.events.AddressEvent;
import com.lrcall.ui.adapter.AddressAdapter;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ActivityAddressList extends MyBasePageActivity implements IAjaxDataResponse, View.OnClickListener
{
	private static final String TAG = ActivityAddressList.class.getSimpleName();
	public static final int REQ_EDIT = 200;
	public static final int REQ_ADD = 201;
	private View layoutAddressList, layoutNoAddress;
	private AddressService mAddressService;
	private List<UserAddressInfo> mUserAddressInfoList = new ArrayList<>();
	private AddressAdapter mAddressAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_list);
		mAddressService = new AddressService(this);
		mAddressService.addDataResponse(this);
		viewInit();
		EventBus.getDefault().register(this);
		onRefresh();
	}

	@Override
	protected void onDestroy()
	{
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Subscribe
	public void onEventMainThread(final AddressEvent addressEvent)
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
		if (mAddressAdapter != null)
		{
			mAddressAdapter.notifyDataSetChanged();
		}
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
		xListView.setPullLoadEnable(userAddressInfoList.size() >= getPageSize());
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
						Intent intent = new Intent();
						intent.putExtra(ConstValues.DATA_ADDRESS_ID, userAddressInfo.getAddressId());
						setResult(RESULT_OK, intent);
						finish();
					}
				}
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
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == REQ_ADD || requestCode == REQ_EDIT)
		{
			if (resultCode == RESULT_OK)
			{
				//				onRefresh();
			}
		}
	}
}
