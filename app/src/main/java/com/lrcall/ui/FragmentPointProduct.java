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
import com.lrcall.appbst.models.PointProductInfo;
import com.lrcall.appbst.models.ProductPicInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.PointProductService;
import com.lrcall.appbst.services.ProductService;
import com.lrcall.appbst.services.UserService;
import com.lrcall.enums.ProductPicType;
import com.lrcall.ui.adapter.SectionsPagerAdapter;
import com.lrcall.ui.customer.FloatProductSelectCount;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

public class FragmentPointProduct extends MyBaseFragment implements View.OnClickListener, IAjaxDataResponse
{
	private final List<ProductPicInfo> mProductPicInfoList = new ArrayList<>();
	private final List<Fragment> mFragmentPicList = new ArrayList<>();
	private ViewPager viewPager;
	private SectionsPagerAdapter sectionsPagerAdapter;
	private SmartTabLayout viewPagerTab;
	private TextView tvTitle, tvDesc, tvPoint, tvMarketPrice;
	public TextView tvAmount;
	private String productId;
	private PointProductInfo mPointProductInfo;
	private PointProductService mPointProductService;
	private ProductService mProductService;
	public FloatProductSelectCount floatProductSelectCount;
	private static FragmentPointProduct instance = null;

	public static FragmentPointProduct getInstance()
	{
		return instance;
	}

	public FragmentPointProduct()
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
		mPointProductService = new PointProductService(this.getContext());
		mPointProductService.addDataResponse(this);
		mProductService = new ProductService(this.getContext());
		mProductService.addDataResponse(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		instance = this;
		View rootView = inflater.inflate(R.layout.fragment_point_product, container, false);
		viewInit(rootView);
		//		mPointProductInfo = DbProductInfoFactory.getInstance().getProductInfo(productId);
		initData();
		refreshData();
		return rootView;
	}

	@Override
	public void fragmentHide()
	{
		super.fragmentHide();
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
		tvPoint = (TextView) rootView.findViewById(R.id.tv_point);
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
					//					LogcatTools.debug("onTouch", "dx:" + dx + ",b=" + b);
					if (Math.abs(dx) > 5)
					{
						b = false;
					}
				}
				if (action == MotionEvent.ACTION_UP)
				{
					//					LogcatTools.debug("onTouch", "b=" + b);
					if (b && mPointProductInfo != null)
					{
						Intent intent = new Intent(FragmentPointProduct.this.getContext(), ActivityPicView.class);
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
		if (mPointProductInfo != null)
		{
			tvTitle.setText(mPointProductInfo.getName());
			tvDesc.setText(mPointProductInfo.getDescription());
			tvPoint.setText("积分：" + mPointProductInfo.getPoint());
			tvMarketPrice.setText("￥" + StringTools.getPrice(mPointProductInfo.getMarketPrice()));
		}
	}

	synchronized private void initPicData()
	{
		if (mProductPicInfoList != null)
		{
			for (ProductPicInfo productPicInfo : mProductPicInfoList)
			{
				mFragmentPicList.add(FragmentServerImage.newInstance(ApiConfig.getServerPicUrl(productPicInfo.getPicUrl()), DisplayTools.getWindowWidth(this.getContext()), ""));
			}
		}
		else
		{
			if (mPointProductInfo != null)
			{
				mFragmentPicList.add(FragmentServerImage.newInstance(ApiConfig.getServerPicUrl(mPointProductInfo.getPicUrl()), DisplayTools.getWindowWidth(this.getContext()), ""));
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
		mPointProductService.getPointProduct(productId, null, true);
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
				break;
			}
			case R.id.btn_kefu:
			{
				if (UserService.isLogin())
				{
					startActivity(new Intent(getActivity(), ActivityImKefuList.class));
				}
				else
				{
					startActivity(new Intent(this.getContext(), ActivityLogin.class));
				}
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.GET_POINT_PRODUCT))
		{
			mPointProductInfo = GsonTools.getReturnObject(result, PointProductInfo.class);
			if (mPointProductInfo != null)
			{
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
