/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.ProductPicInfo;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.ProductService;
import com.lrcall.db.DbProductInfoFactory;
import com.lrcall.enums.ProductPicType;
import com.lrcall.ui.adapter.SectionsPagerAdapter;
import com.lrcall.ui.customer.DisplayTools;
import com.lrcall.ui.customer.FloatProductSelectCount;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

public class FragmentProduct extends MyBaseFragment implements View.OnClickListener, IAjaxDataResponse
{
	private final List<ProductPicInfo> mProductPicInfoList = new ArrayList<>();
	private final List<Fragment> mFragmentPicList = new ArrayList<>();
	private ViewPager viewPager;
	private SectionsPagerAdapter sectionsPagerAdapter;
	private SmartTabLayout viewPagerTab;
	private TextView tvTitle, tvDesc, tvPrice, tvMarketPrice;
	public TextView tvAmount;
	private String productId;
	private ProductInfo mProductInfo;
	private ProductService mProductService;
	public FloatProductSelectCount floatProductSelectCount;
	private static FragmentProduct instance = null;

	public static FragmentProduct getInstance()
	{
		return instance;
	}

	public FragmentProduct()
	{
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
		{
			productId = getArguments().getString(ConstValues.DATA_PRODUCT_ID);
		}
		mProductService = new ProductService(this.getContext());
		mProductService.addDataResponse(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		instance = this;
		View rootView = inflater.inflate(R.layout.fragment_product, container, false);
		viewInit(rootView);
		mProductInfo = DbProductInfoFactory.getInstance().getProductInfo(productId);
		initData();
		refreshData();
		return rootView;
	}

	@Override
	public void fragmentHide()
	{
		super.fragmentHide();
		LogcatTools.debug("fragmentHide", "关闭floatProductSelectCount");
		clearFloatWindow();
	}

	@Override
	public void onDestroyView()
	{
		clearFloatWindow();
		instance = null;
		super.onDestroyView();
	}

	@Override
	synchronized protected void viewInit(View rootView)
	{
		viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
		//设置图片的长宽，这里便于制作图片
		ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
		layoutParams.width = DisplayTools.getWindowWidth(this.getContext());
		layoutParams.height = DisplayTools.getWindowWidth(this.getContext()) * 4 / 5;
		viewPager.setLayoutParams(layoutParams);
		viewPagerTab = (SmartTabLayout) rootView.findViewById(R.id.viewpagertab);
		tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
		tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
		tvPrice = (TextView) rootView.findViewById(R.id.tv_price);
		tvMarketPrice = (TextView) rootView.findViewById(R.id.tv_market_price);
		tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		tvAmount = (TextView) rootView.findViewById(R.id.tv_amount);
		rootView.findViewById(R.id.layout_show_choose_amount).setOnClickListener(this);
		rootView.findViewById(R.id.btn_show_choose_amount).setOnClickListener(this);
		rootView.findViewById(R.id.layout_comment).setOnClickListener(this);
		rootView.findViewById(R.id.btn_kefu).setOnClickListener(this);
		viewPager.setOnTouchListener(new View.OnTouchListener()
		{
			boolean b;
			float lastX;

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN)
				{
					lastX = event.getRawX();
					b = true;
				}
				if (action == MotionEvent.ACTION_MOVE)
				{
					float dx = event.getRawX() - lastX;
					LogcatTools.debug("onTouch", "dx:" + dx + ",b=" + b);
					if (Math.abs(dx) > 5)
					{
						b = false;
					}
				}
				if (action == MotionEvent.ACTION_UP)
				{
					LogcatTools.debug("onTouch", "b=" + b);
					if (b && mProductInfo != null)
					{
						Intent intent = new Intent(FragmentProduct.this.getContext(), ActivityPicView.class);
						ArrayList<String> urls = new ArrayList<>();
						if (mProductPicInfoList != null)
						{
							for (ProductPicInfo productPicInfo : mProductPicInfoList)
							{
								urls.add(ApiConfig.getServerPicUrl(productPicInfo.getPicUrl()));
							}
						}
						intent.putStringArrayListExtra(ConstValues.DATA_PRODUCT_SHOW_PIC_URLS, urls);
						startActivity(intent);
					}
				}
				return false;
			}
		});
		super.viewInit(rootView);
	}

	private void initData()
	{
		if (mProductInfo != null)
		{
			tvTitle.setText(mProductInfo.getName());
			tvDesc.setText(mProductInfo.getDescription());
			tvPrice.setText("￥" + StringTools.getPrice(mProductInfo.getPrice()));
			tvMarketPrice.setText("￥" + StringTools.getPrice(mProductInfo.getMarketPrice()));
		}
	}

	synchronized private void initPicData()
	{
		if (mProductPicInfoList != null && mProductPicInfoList.size() > 0)
		{
			for (ProductPicInfo productPicInfo : mProductPicInfoList)
			{
				mFragmentPicList.add(FragmentServerImage.newInstance(ApiConfig.getServerPicUrl(productPicInfo.getPicUrl()), DisplayTools.getWindowWidth(this.getContext()), ""));
			}
		}
		else
		{
			if (mProductInfo != null)
			{
				mFragmentPicList.add(FragmentServerImage.newInstance(ApiConfig.getServerPicUrl(mProductInfo.getPicId()), DisplayTools.getWindowWidth(this.getContext()), ""));
			}
		}
		if (sectionsPagerAdapter == null)
		{
			sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), mFragmentPicList);
			viewPager.setAdapter(sectionsPagerAdapter);
			viewPagerTab.setViewPager(viewPager);
		}
		else
		{
			sectionsPagerAdapter.notifyDataSetChanged();
		}
	}

	private void refreshData()
	{
		mProductService.getProductInfo(productId, null, true);
		mProductService.getProductPicList(productId, ProductPicType.CLIENT_DETAIL_BIG.getType(), null, false);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.layout_show_choose_amount:
			case R.id.btn_show_choose_amount:
			{
				if (floatProductSelectCount == null)
				{
					floatProductSelectCount = new FloatProductSelectCount(this.getActivity(), tvAmount);
					floatProductSelectCount.showTopWindow();
				}
				else
				{
					floatProductSelectCount.clearTopWindow();
					floatProductSelectCount.showTopWindow();
				}
				break;
			}
			case R.id.layout_comment:
			{
				ActivityProduct activityProduct = (ActivityProduct) getActivity();
				if (activityProduct != null)
				{
					activityProduct.setPage(2);
				}
				break;
			}
			case R.id.btn_kefu:
			{
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_PRODUCT_INFO))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				mProductInfo = GsonTools.getReturnObject(result, ProductInfo.class);
				initData();
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_PRODUCT_PIC_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<ProductPicInfo> productPicInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductPicInfo>>()
				{
				}.getType());
				if (productPicInfoList != null && productPicInfoList.size() > 0)
				{
					mProductPicInfoList.clear();
					mProductPicInfoList.addAll(productPicInfoList);
				}
			}
			initPicData();
			return true;
		}
		return false;
	}

	//关闭选择数量
	public void clearFloatWindow()
	{
		if (floatProductSelectCount != null)
		{
			floatProductSelectCount.clearTopWindow();
			floatProductSelectCount = null;
		}
	}
}
