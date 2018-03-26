/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.ShopProductAgentInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ShopProductService;
import com.lrcall.models.FuncInfo;
import com.lrcall.ui.ActivityProduct;
import com.lrcall.ui.MyBasePageActivity;
import com.lrcall.ui.adapter.AgentProductsAdapter;
import com.lrcall.ui.adapter.FuncsHorizontalAdapter;
import com.lrcall.ui.adapter.FuncsVerticalAdapter;
import com.lrcall.ui.dialog.DialogList;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityShopAgentProductManage extends MyBasePageActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityShopAgentProductManage.class.getSimpleName();
	public static final int REQ_ADD = 301;
	private View layoutProductList, layoutNoProduct;
	private EditText etSearch;
	private AgentProductsAdapter mAgentProductsAdapter;
	private ShopProductService mProductService;
	private final List<ShopProductAgentInfo> mShopProductAgentInfoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_agent_product_manage);
		mProductService = new ShopProductService(this);
		mProductService.addDataResponse(this);
		viewInit();
		onRefresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_shop_agent_product_manage, menu);
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
		else if (id == R.id.action_add_agent_product)
		{
			startActivityForResult(new Intent(this, ActivityShopChooseAgentProduct.class), REQ_ADD);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		layoutProductList = findViewById(R.id.layout_search_result);
		layoutNoProduct = findViewById(R.id.layout_no_product);
		xListView = (XListView) findViewById(R.id.xlist);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
		etSearch = (EditText) findViewById(R.id.et_search);
		etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND || (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
				{
					onRefresh();
					return true;
				}
				return false;
			}
		});
		findViewById(R.id.search_icon).setOnClickListener(this);
		findViewById(R.id.search_del).setOnClickListener(this);
		findViewById(R.id.btn_agent).setOnClickListener(this);
	}

	//刷新数据
	@Override
	public void refreshData()
	{
		mShopProductAgentInfoList.clear();
		if (mAgentProductsAdapter != null)
		{
			mAgentProductsAdapter.notifyDataSetChanged();
		}
		mAgentProductsAdapter = null;
		loadMoreData();
	}

	//加载更多商品
	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "请稍后..." : "");
		String condition = etSearch.getText().toString();
		mProductService.getAgentProductList(condition, mDataStart, getPageSize(), null, null, tips, true);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.search_icon:
			{
				onRefresh();
				break;
			}
			case R.id.search_del:
			{
				String number = etSearch.getText().toString();
				if (!StringTools.isNull(number))
				{
					etSearch.setTextKeepState(number.substring(0, number.length() - 1));
				}
				//				search();
				break;
			}
			case R.id.btn_agent:
			{
				startActivityForResult(new Intent(this, ActivityShopChooseAgentProduct.class), REQ_ADD);
				break;
			}
		}
	}

	synchronized private void refreshAgentProducts(List<ShopProductAgentInfo> shopProductAgentInfoList)
	{
		if (shopProductAgentInfoList == null || shopProductAgentInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mShopProductAgentInfoList.size() < 1)
			{
				layoutProductList.setVisibility(View.GONE);
				layoutNoProduct.setVisibility(View.VISIBLE);
			}
			return;
		}
		layoutProductList.setVisibility(View.VISIBLE);
		layoutNoProduct.setVisibility(View.GONE);
		if (shopProductAgentInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (ShopProductAgentInfo shopProductAgentInfo : shopProductAgentInfoList)
		{
			mShopProductAgentInfoList.add(shopProductAgentInfo);
		}
		if (mAgentProductsAdapter == null)
		{
			mAgentProductsAdapter = new AgentProductsAdapter(this, mShopProductAgentInfoList, new AgentProductsAdapter.IItemClick()
			{
				@Override
				public void onProductClicked(final ShopProductAgentInfo shopProductAgentInfo)
				{
					if (shopProductAgentInfo != null)
					{
						final List<FuncInfo> list = new ArrayList<>();
						list.add(new FuncInfo(R.drawable.ic_done_grey600_18dp, "查看商品"));
						list.add(new FuncInfo(R.drawable.ic_done_grey600_18dp, "取消代理"));
						final DialogList dialogList = new DialogList(ActivityShopAgentProductManage.this);
						FuncsHorizontalAdapter adapter = new FuncsHorizontalAdapter(ActivityShopAgentProductManage.this, list, new FuncsVerticalAdapter.IFuncsAdapterItemClicked()
						{
							@Override
							public void onFuncClicked(FuncInfo funcInfo)
							{
								dialogList.dismiss();
								if (funcInfo.getLabel().equalsIgnoreCase(list.get(0).getLabel()))
								{
									Intent intent = new Intent(ActivityShopAgentProductManage.this, ActivityProduct.class);
									intent.putExtra(ConstValues.DATA_PRODUCT_ID, shopProductAgentInfo.getProductInfo().getProductId());
									startActivity(intent);
								}
								else if (funcInfo.getLabel().equalsIgnoreCase(list.get(1).getLabel()))
								{
									mProductService.deleteAgetnProduct(shopProductAgentInfo.getProductId(), "请稍后...", true);
								}
							}
						});
						dialogList.setAdapter(adapter);
						dialogList.show();
					}
				}
			});
			xListView.setAdapter(mAgentProductsAdapter);
		}
		else
		{
			mAgentProductsAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_SHOP_AGENT_PRODUCT_LIST))
		{
			List<ShopProductAgentInfo> shopProductAgentInfoList = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				shopProductAgentInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ShopProductAgentInfo>>()
				{
				}.getType());
			}
			refreshAgentProducts(shopProductAgentInfoList);
		}
		else if (url.endsWith(ApiConfig.SHOP_DELETE_PRODUCT_AGENT))
		{
			showServerMsg(result, null);
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				onRefresh();
			}
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK)
		{
			onRefresh();
		}
	}
}
