/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.client.android.CaptureActivity;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ProductSortInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ProductSortService;
import com.lrcall.db.DbProductSortInfoFactory;
import com.lrcall.ui.adapter.CategoryAdapter;
import com.lrcall.ui.adapter.CategorySortAdapter;
import com.lrcall.utils.DisplayTools;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.customer.ViewHeightCalTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;

import java.util.List;

public class FragmentCategory extends MyBaseFragment implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = FragmentCategory.class.getSimpleName();
	private static final int SORT_COLUMNS_NUM = 2;
	private ListView lvCategory;
	private GridView gvSort;
	private ImageView ivSortPic;
	private TextView tvSortName;
	private ProductSortService mProductSortService;
	private View currentFirstView;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mProductSortService = new ProductSortService(this.getContext());
		mProductSortService.addDataResponse(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_category, container, false);
		viewInit(rootView);
		initData();
		return rootView;
	}

	@Override
	protected void viewInit(View rootView)
	{
		lvCategory = (ListView) rootView.findViewById(R.id.list_category);
		ivSortPic = (ImageView) rootView.findViewById(R.id.iv_sort_pic);
		tvSortName = (TextView) rootView.findViewById(R.id.tv_sort_name);
		gvSort = (GridView) rootView.findViewById(R.id.gv_sort);
		final EditText etSearch = (EditText) rootView.findViewById(R.id.et_search);
		etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (v.getId() == etSearch.getId())
				{
					if (hasFocus)
					{
						startActivity(new Intent(FragmentCategory.this.getContext(), ActivitySearchProducts.class));
						v.clearFocus();
					}
				}
			}
		});
		rootView.findViewById(R.id.btn_qr).setOnClickListener(this);
		super.viewInit(rootView);
	}

	protected void initData()
	{
		setFirstSortList();
		ViewGroup.LayoutParams layoutParams = ivSortPic.getLayoutParams();
		layoutParams.width = DisplayTools.getWindowWidth(this.getContext()) * 2 / 3;
		layoutParams.height = layoutParams.width * 2 / 3;
		ivSortPic.setLayoutParams(layoutParams);
		mProductSortService.getProductSortList(null, true);
	}

	//设置一级分类
	private void setFirstSortList()
	{
		List<ProductSortInfo> productSortInfoList = DbProductSortInfoFactory.getInstance().getProductSortInfoListByLevelId(1);
		CategoryAdapter lvAdapter = new CategoryAdapter(getContext(), productSortInfoList, new CategoryAdapter.IItemClick()
		{
			@Override
			public void onProductSortClicked(View v, ProductSortInfo productSortInfo)
			{
				if (currentFirstView != null)
				{
					currentFirstView.setBackgroundColor(getResources().getColor(R.color.white));
				}
				currentFirstView = v;
				currentFirstView.setBackgroundColor(getResources().getColor(R.color.second_list_bg));
				onFirstProductSortClicked(productSortInfo);
			}
		});
		lvCategory.setAdapter(lvAdapter);
		if (productSortInfoList != null && productSortInfoList.size() > 0)
		{
			onFirstProductSortClicked(productSortInfoList.get(0));
		}
		//		currentFirstView = lvAdapter.getView(0, null, lvCategory);
		//		LogcatTools.debug("getView", "currentFirstView:" + currentFirstView);
		//		currentFirstView.setBackgroundColor(getResources().getColor(R.color.second_list_bg));
	}

	//一级分类点击事件
	private void onFirstProductSortClicked(final ProductSortInfo productSortInfo)
	{
		LogcatTools.debug("onProductSortClicked", "一级分类已点击：" + productSortInfo.toString());
		AQuery aQuery = new AQuery(ivSortPic);
		String url = ApiConfig.getServerPicUrl(productSortInfo.getPicId());
		Bitmap bitmap = aQuery.getCachedImage(url);
		if (bitmap != null)
		{
			LogcatTools.debug("aQuery", "从缓存读取图片:" + url);
			ivSortPic.setImageBitmap(bitmap);
		}
		else
		{
			LogcatTools.debug("aQuery", "图片地址:" + url);
			aQuery.image(url, false, true);
		}
		tvSortName.setText(productSortInfo.getName());
		List<ProductSortInfo> productSortInfoList = DbProductSortInfoFactory.getInstance().getProductSortInfoListByParentId(productSortInfo.getSortId());
		setSecondSortList(productSortInfoList);
		ivSortPic.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(FragmentCategory.this.getContext(), ActivitySearchProducts.class);
				intent.putExtra(ConstValues.DATA_PRODUCT_SORT_ID, productSortInfo.getSortId());
				startActivity(intent);
			}
		});
	}

	//设置二级分类
	private void setSecondSortList(List<ProductSortInfo> productSortInfoList)
	{
		CategorySortAdapter gvAdapter = new CategorySortAdapter(getContext(), productSortInfoList, new CategorySortAdapter.IItemClick()
		{
			@Override
			public void onProductSortClicked(ProductSortInfo productSortInfo)
			{
				Intent intent = new Intent(FragmentCategory.this.getContext(), ActivitySearchProducts.class);
				intent.putExtra(ConstValues.DATA_PRODUCT_SORT_ID, productSortInfo.getSortId());
				startActivity(intent);
			}
		});
		gvSort.setNumColumns(SORT_COLUMNS_NUM);
		gvSort.setAdapter(gvAdapter);
		ViewHeightCalTools.setGridViewHeight(gvSort, SORT_COLUMNS_NUM, true);
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_PRODUCT_SORT_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<ProductSortInfo> productSortInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductSortInfo>>()
				{
				}.getType());
				if (productSortInfoList != null && productSortInfoList.size() > 0)
				{
					setFirstSortList();
				}
				else
				{
					ToastView.showCenterToast(this.getContext(), R.drawable.ic_do_fail, "分类为空！");
				}
			}
		}
		return false;
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.iv_menu:
			{
				if (ActivityMain.getInstance() != null)
				{
					ActivityMain.getInstance().getmDrawerLayout().openDrawer(Gravity.LEFT);
				}
				break;
			}
			case R.id.btn_qr:
			{
				Intent intent = new Intent(this.getContext(), CaptureActivity.class);
				startActivity(intent);
				break;
			}
		}
	}
}
