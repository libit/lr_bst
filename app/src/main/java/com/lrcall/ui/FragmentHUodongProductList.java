/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ProductService;
import com.lrcall.ui.adapter.ProductAdapter;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class FragmentHuodongProductList extends MyBasePageFragment implements IAjaxDataResponse, View.OnClickListener
{
	private static final String TAG = FragmentHuodongProductList.class.getSimpleName();
	public static final String ARG_SORT_ID = "sortId";
	private View layoutNoProduct;
	private ProductService mProductService;
	private final List<ProductInfo> mProductInfoList = new ArrayList<>();
	private String sortId;
	private ProductAdapter mProductAdapter;

	public FragmentHuodongProductList()
	{
	}

	public static FragmentHuodongProductList newInstance(String sortId)
	{
		FragmentHuodongProductList fragment = new FragmentHuodongProductList();
		Bundle args = new Bundle();
		args.putString(ARG_SORT_ID, sortId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
		{
			sortId = getArguments().getString(ARG_SORT_ID);
		}
		mProductService = new ProductService(this.getContext());
		mProductService.addDataResponse(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);
		viewInit(rootView);
		onRefresh();
		return rootView;
	}

	@Override
	protected void viewInit(View rootView)
	{
		xListView = (XListView) rootView.findViewById(R.id.xlist);
		layoutNoProduct = rootView.findViewById(R.id.layout_no_product);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setAdapter(null);
		xListView.setXListViewListener(this);
		rootView.findViewById(R.id.btn_refresh).setOnClickListener(this);
		super.viewInit(rootView);
	}

	/**
	 * 刷新数据
	 */
	@Override
	public void refreshData()
	{
		mProductInfoList.clear();
		if (mProductAdapter != null)
		{
			mProductAdapter.notifyDataSetChanged();
		}
		mProductAdapter = null;
		loadMoreData();
	}

	/**
	 * 加载更多数据
	 */
	@Override
	public void loadMoreData()
	{
		String tips = (mDataStart == 0 ? "" : "");
		LogcatTools.debug("loadMoreData", "sortId:" + sortId);
		if (!StringTools.isNull(sortId))
		{
			if (sortId.equalsIgnoreCase(ActivityHuodong.NEWS))
			{
				mProductService.getNewProductList(mDataStart, getPageSize(), tips, false);
			}
			else if (sortId.equalsIgnoreCase(ActivityHuodong.PANIC))
			{
				mProductService.getPanicBuyingProductList(mDataStart, getPageSize(), tips, false);
			}
			else if (sortId.equalsIgnoreCase(ActivityHuodong.RECOMMEND))
			{
				mProductService.getRecommendProductList(mDataStart, getPageSize(), tips, false);
			}
		}
	}

	//设置List
	synchronized private void setProductInfoList(List<ProductInfo> productInfoList)
	{
		if (productInfoList == null || productInfoList.size() < 1)
		{
			if (mProductInfoList.size() < 1)
			{
				layoutNoProduct.setVisibility(View.VISIBLE);
				xListView.setVisibility(View.GONE);
			}
			return;
		}
		layoutNoProduct.setVisibility(View.GONE);
		xListView.setVisibility(View.VISIBLE);
		xListView.setPullLoadEnable(productInfoList.size() >= getPageSize());
		for (ProductInfo productInfo : productInfoList)
		{
			mProductInfoList.add(productInfo);
		}
		if (mProductAdapter == null)
		{
			mProductAdapter = new ProductAdapter(getContext(), mProductInfoList, new ProductAdapter.IItemClick()
			{
				@Override
				public void onProductClicked(ProductInfo productInfo)
				{
					if (productInfo != null)
					{
						Intent intent = new Intent(FragmentHuodongProductList.this.getContext(), ActivityProduct.class);
						intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
						startActivity(intent);
					}
				}
			});
			xListView.setAdapter(mProductAdapter);
		}
		else
		{
			mProductAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_NEW_PRODUCT_LIST) || url.endsWith(ApiConfig.GET_PANIC_BUYING_PRODUCT_LIST) || url.endsWith(ApiConfig.GET_RECOMMEND_PRODUCT_LIST))
		{
			List<ProductInfo> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductInfo>>()
				{
				}.getType());
			}
			setProductInfoList(list);
		}
		return false;
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_refresh:
			{
				onRefresh();
				break;
			}
		}
	}
}
