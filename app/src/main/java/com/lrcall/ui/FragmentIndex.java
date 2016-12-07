/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.client.android.CaptureActivity;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.BannerInfo;
import com.lrcall.appbst.models.ClientIndexFuncInfo;
import com.lrcall.appbst.models.DataTrafficInfo;
import com.lrcall.appbst.models.NewsInfo;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.BannerService;
import com.lrcall.appbst.services.ClientService;
import com.lrcall.appbst.services.DataTrafficService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.NewsService;
import com.lrcall.appbst.services.ProductService;
import com.lrcall.appbst.services.UserService;
import com.lrcall.db.DbBannerInfoFactory;
import com.lrcall.enums.ClientBannerType;
import com.lrcall.enums.ClientFuncType;
import com.lrcall.ui.adapter.IndexDataTrafficProductsAdapter;
import com.lrcall.ui.adapter.IndexFuncsAdapter;
import com.lrcall.ui.adapter.IndexNewProductsAdapter;
import com.lrcall.ui.adapter.IndexRecommendProducts4Adapter;
import com.lrcall.ui.adapter.SectionsPagerAdapter;
import com.lrcall.ui.customer.ViewHeightCalTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.LocationTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FragmentIndex extends MyBasePageFragment implements View.OnClickListener, AbsListView.OnScrollListener, IAjaxDataResponse, AMapLocationListener
{
	private static final String TAG = FragmentIndex.class.getSimpleName();
	public static final int MSG_LOCATION_START = 113;//开始定位
	public static final int MSG_LOCATION_FINISH = 114;//定位完成
	private static final int SCROLL_VIEW_PAGE = 111;
	private static final int SCROLL_NEWS = 112;
	private static final int FUNC_COLUMNS_NUM = 5;
	private static final int NEW_PRODUCT_COLUMNS_NUM = 2;
	private static final int NEW_DATA_TRAFFIC_PRODUCT_COLUMNS_NUM = 2;
	private static final int SHOW_NEWS_COUNT = 3;
	private static final int FUNC_SHENXIAN = 1;
	private static final int FUNC_MEISHI = 2;
	private static final int FUNC_MEIZHUAN = 3;
	private static final int FUNC_HAIWAIGOU = 4;
	private static final int FUNC_GUANFANZHIYIN = 5;
	private static final int FUNC_TECHANG = 6;
	private static final int FUNC_FUZHUAN = 7;
	private static final int FUNC_SHUMA = 8;
	private static final int FUNC_DATA_TRAFFIC = 9;
	private static final int FUNC_JIFENSANCHENG = 10;
	private static final long SCROLL_TIME = 5;
	private static final int DATA_TRAFFIC_COUNT = 4;
	private static final int RECOMMEND_COUNT = 4;
	private static final int REQ_SCAN_QR = 1200;
	private View layoutSearch, headView;
	private EditText etSearch;
	private TextView tvLoaction, tvAddress, tvNews;
	private GridView gvFuncs/*功能区*/, gvNewProducts/*最新商品*/, gvDataTrafficProducts/*最新流量商品*/;
	private ViewPager viewPager;
	private ListView lvRecommendProducts/*推荐商品*/, lvConcessionProducts/*促销商品*/;
	private ScheduledExecutorService scheduledExecutorService = null;
	private ScheduledFuture scheduledFuture = null;
	private NewsService mNewsService;
	private ProductService mProductService;
	private DataTrafficService mDataTrafficService;
	private BannerService mBannerService;
	private ClientService mClientService;
	private IndexRecommendProducts4Adapter mIndexRecommendProducts4Adapter;
	private IndexRecommendProducts4Adapter mIndexConcessionProductsAdapter;
	private IndexNewProductsAdapter mIndexNewProductsAdapter;
	private IndexDataTrafficProductsAdapter mIndexDataTrafficProductsAdapter;
	private final List<Fragment> mFragmentRecommendList = new ArrayList<>();
	private final List<NewsInfo> mNewsInfoList = new ArrayList<>();
	private final List<DataTrafficInfo> mDataTrafficProductInfoList = new ArrayList<>();
	private final List<ProductInfo> mNewProductInfoList = new ArrayList<>();
	private final List<ProductInfo> mRecommendProductInfoList = new ArrayList<>();
	private final List<ProductInfo> mConcessionProductInfoList = new ArrayList<>();
	private SmartTabLayout viewPagerTab;
	private SectionsPagerAdapter sectionsPagerAdapter;
	private final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
				case SCROLL_VIEW_PAGE:
				{
					int index = viewPager.getCurrentItem();
					if (index < mFragmentRecommendList.size() - 1)
					{
						index++;
					}
					else
					{
						index = 0;
					}
					viewPager.setCurrentItem(index);
					break;
				}
				case SCROLL_NEWS:
				{
					if (mNewsInfoList != null && mNewsInfoList.size() > 0)
					{
						String title = tvNews.getText().toString();
						int index = 0;
						int size = mNewsInfoList.size();
						if (!StringTools.isNull(title))
						{
							for (int i = 0; i < size; i++)
							{
								if (title.equals(mNewsInfoList.get(i).getTitle()))
								{
									index = i;
									break;
								}
							}
						}
						if (index >= (size - 1))
						{
							index = 0;
						}
						else
						{
							index++;
						}
						tvNews.setText(mNewsInfoList.get(index).getTitle());
						//						tvNews.setTag(mNewsInfoList.get(index).getTitle());
					}
					break;
				}
				case MSG_LOCATION_START:
				{
					tvLoaction.setText("正在定位...");
					tvAddress.setVisibility(View.GONE);
					break;
				}
				case MSG_LOCATION_FINISH:
				{
					AMapLocation loc = (AMapLocation) msg.obj;
					tvLoaction.setText(LocationTools.getLocationCityStr(loc));
					//					String address = LocationTools.getLocationAddressStr(loc);
					//					if (!StringTools.isNull(address))
					//					{
					//						tvAddress.setText("您当前位置：" + address);
					//						tvAddress.setVisibility(View.VISIBLE);
					//					}
					break;
				}
			}
		}
	};
	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;

	synchronized private void updateView()
	{
		if (scheduledExecutorService == null)
		{
			scheduledExecutorService = Executors.newScheduledThreadPool(1);
		}
		else
		{
			cancelScheduledFuture();
		}
		try
		{
			scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new Thread("updateView")
			{
				@Override
				public void run()
				{
					super.run();
					//					LogcatTools.debug(TAG, "ScheduledFuture,时间:" + DateTimeTools.getCurrentTime());
					mHandler.sendEmptyMessage(SCROLL_VIEW_PAGE);
					mHandler.sendEmptyMessage(SCROLL_NEWS);
				}
			}, SCROLL_TIME, SCROLL_TIME, TimeUnit.SECONDS);
		}
		catch (RejectedExecutionException e)
		{
			LogcatTools.debug(TAG, "ScheduledFuture->RejectedExecutionException:" + e.getMessage());
		}
	}

	synchronized private void cancelScheduledFuture()
	{
		if (scheduledFuture != null)// && !scheduledFuture.isCancelled())
		{
			scheduledFuture.cancel(true);
			scheduledFuture = null;
		}
	}

	synchronized private void stopScheduledFuture()
	{
		cancelScheduledFuture();
		if (scheduledExecutorService != null)
		{
			scheduledExecutorService.shutdown();
			scheduledExecutorService = null;
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mNewsService = new NewsService(this.getContext());
		mNewsService.addDataResponse(this);
		mProductService = new ProductService(this.getContext());
		mProductService.addDataResponse(this);
		mDataTrafficService = new DataTrafficService(this.getContext());
		mDataTrafficService.addDataResponse(this);
		mBannerService = new BannerService(this.getContext());
		mBannerService.addDataResponse(this);
		mClientService = new ClientService(this.getContext());
		mClientService.addDataResponse(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_index, container, false);
		viewInit(rootView);
		//		initFuncData();
		updateView();
		//		beginLocation();
		setViewPagerAdapter(DbBannerInfoFactory.getInstance().getBannerInfoList(ClientBannerType.PAGE_INDEX.getType()));
		onRefresh();
		return rootView;
	}

	@Override
	protected void viewInit(View rootView)
	{
		rootView.findViewById(R.id.iv_menu).setOnClickListener(this);
		rootView.findViewById(R.id.btn_qr).setOnClickListener(this);
		layoutSearch = rootView.findViewById(R.id.layout_search);
		etSearch = (EditText) rootView.findViewById(R.id.et_search);
		etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (v.getId() == etSearch.getId() && hasFocus)
				{
					startActivity(new Intent(FragmentIndex.this.getContext(), ActivitySearchProducts.class));
					v.clearFocus();
				}
			}
		});
		xListView = (XListView) rootView.findViewById(R.id.xlist);
		headView = LayoutInflater.from(this.getContext()).inflate(R.layout.fragment_index_head, null);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.addHeaderView(headView);
		xListView.setAdapter(null);
		xListView.setXListViewListener(this);
		xListView.setOnScrollListener(this);
		tvLoaction = (TextView) rootView.findViewById(R.id.tv_location);
		tvAddress = (TextView) rootView.findViewById(R.id.tv_address);
		tvNews = (TextView) rootView.findViewById(R.id.tv_news);
		tvNews.setOnClickListener(this);
		rootView.findViewById(R.id.iv_more_news).setOnClickListener(this);
		rootView.findViewById(R.id.tv_more_news).setOnClickListener(this);
		gvFuncs = (GridView) headView.findViewById(R.id.gv_func);
		gvFuncs.setNumColumns(FUNC_COLUMNS_NUM);
		viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
		//设置图片的长宽，这里便于制作图片
		ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
		layoutParams.width = DisplayTools.getWindowWidth(this.getContext());
		layoutParams.height = DisplayTools.getWindowWidth(this.getContext()) * 1 / 2;
		viewPager.setLayoutParams(layoutParams);
		viewPagerTab = (SmartTabLayout) rootView.findViewById(R.id.viewpagertab);
		viewPager.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE)
				{
					cancelScheduledFuture();
				}
				else
				{
					updateView();
				}
				return false;
			}
		});
		lvRecommendProducts = (ListView) rootView.findViewById(R.id.list_recommend_products);
		lvConcessionProducts = (ListView) rootView.findViewById(R.id.list_concession_products);
		gvNewProducts = (GridView) rootView.findViewById(R.id.gv_new_products);
		gvNewProducts.setNumColumns(NEW_PRODUCT_COLUMNS_NUM);
		gvDataTrafficProducts = (GridView) rootView.findViewById(R.id.gv_new_data_traffic_products);
		gvDataTrafficProducts.setNumColumns(NEW_DATA_TRAFFIC_PRODUCT_COLUMNS_NUM);
		locationClient = new AMapLocationClient(this.getContext());
		locationOption = new AMapLocationClientOption();
		// 设置定位模式为低功耗模式
		locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
		// 设置定位监听
		locationClient.setLocationListener(this);
		rootView.findViewById(R.id.tv_new_product_more).setOnClickListener(this);
		rootView.findViewById(R.id.tv_data_traffic_product_more).setOnClickListener(this);
		super.viewInit(rootView);
	}

	//设置图片适配器
	private void setViewPagerAdapter(List<BannerInfo> bannerInfoList)
	{
		if (bannerInfoList != null && bannerInfoList.size() > 0)
		{
			mFragmentRecommendList.clear();
			for (BannerInfo bannerInfo : bannerInfoList)
			{
				mFragmentRecommendList.add(FragmentServerImage.newInstance(ApiConfig.getServerPicUrl(bannerInfo.getPicUrl()), DisplayTools.getWindowWidth(this.getContext()), bannerInfo.getContent()));
			}
			sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), mFragmentRecommendList);
			viewPager.setAdapter(sectionsPagerAdapter);
			viewPagerTab.setViewPager(viewPager);
		}
	}

	//初始化数据
	synchronized protected void initFuncData(List<ClientIndexFuncInfo> list)
	{
		if (list == null || list.size() < 1)
		{
			gvFuncs.setVisibility(View.GONE);
			return;
		}
		//		List<FuncInfo> funcInfoList = new ArrayList<>();
		//		funcInfoList.add(new FuncInfo(FUNC_SHENXIAN, R.drawable.mine_icon_waitpay, "生鲜"));
		//		funcInfoList.add(new FuncInfo(FUNC_MEISHI, R.drawable.mine_icon_ordrers, "美食"));
		//		funcInfoList.add(new FuncInfo(FUNC_MEIZHUAN, R.drawable.mine_icon_address, "美妆"));
		//		funcInfoList.add(new FuncInfo(FUNC_HAIWAIGOU, R.drawable.mine_icon_care, "海外购"));
		//		funcInfoList.add(new FuncInfo(FUNC_GUANFANZHIYIN, R.drawable.mine_icon_website, "官方直营"));
		//		funcInfoList.add(new FuncInfo(FUNC_TECHANG, R.drawable.mine_icon_shipped, "特产"));
		//		funcInfoList.add(new FuncInfo(FUNC_FUZHUAN, R.drawable.mine_icon_changps, "服装"));
		//		funcInfoList.add(new FuncInfo(FUNC_SHUMA, R.drawable.mine_icon_wallet, "数码"));
		//		funcInfoList.add(new FuncInfo(FUNC_DATA_TRAFFIC, R.drawable.mine_icon_changps, "充值中心"));
		//		funcInfoList.add(new FuncInfo(FUNC_JIFENSANCHENG, R.drawable.mine_icon_wallet, "积分商城"));
		IndexFuncsAdapter indexFuncsAdapter = new IndexFuncsAdapter(this.getContext(), list, new IndexFuncsAdapter.IItemClicked()
		{
			@Override
			public void onFuncClicked(ClientIndexFuncInfo clientIndexFuncInfo)
			{
				if (clientIndexFuncInfo != null)
				{
					com.lrcall.appbst.models.ClientFuncType clientClientFuncType = GsonTools.getObject(clientIndexFuncInfo.getContent(), com.lrcall.appbst.models.ClientFuncType.class);
					if (clientClientFuncType != null)
					{
						if (clientClientFuncType.getType().equalsIgnoreCase(ClientFuncType.OPEN_URL.getType()))
						{
							ActivityWebView.startWebActivity(FragmentIndex.this.getContext(), clientIndexFuncInfo.getName(), clientClientFuncType.getContent());
						}
						else if (clientClientFuncType.getType().equalsIgnoreCase(ClientFuncType.OPEN_ACTIIVTY.getType()))
						{
							if (clientClientFuncType.getContent().equalsIgnoreCase("ActivityShopProducts"))
							{
								startActivity(new Intent(FragmentIndex.this.getContext(), ActivityShopProducts.class));
							}
							else if (clientClientFuncType.getContent().equalsIgnoreCase("ActivityRechargeDataTraffic"))
							{
								if (UserService.isLogin())
								{
									startActivity(new Intent(FragmentIndex.this.getContext(), ActivityRechargeDataTraffic.class));
								}
								else
								{
									startActivity(new Intent(FragmentIndex.this.getContext(), ActivityLogin.class));
								}
							}
							else if (clientClientFuncType.getContent().equalsIgnoreCase("ActivityPointProductShop"))
							{
								startActivity(new Intent(FragmentIndex.this.getContext(), ActivityPointProductShop.class));
							}
						}
					}
				}
			}
		});
		gvFuncs.setAdapter(indexFuncsAdapter);
		ViewHeightCalTools.setGridViewHeight(gvFuncs, FUNC_COLUMNS_NUM, true);
	}

	//刷新数据
	@Override
	synchronized public void refreshData()
	{
		mClientService.getIndexFuncList(null, true);
		mBannerService.getBannerInfoList(ClientBannerType.PAGE_INDEX.getType(), 0, RECOMMEND_COUNT, null, true);
		//获取新闻
		mNewsService.getNewsInfoList(0, SHOW_NEWS_COUNT, null, null, null, true);
		mDataTrafficService.getNewDataTrafficInfoList(mDataStart, DATA_TRAFFIC_COUNT, null, true);
		mProductService.getRecommendProductList(mDataStart, RECOMMEND_COUNT, null, true);
		mProductService.getConcessionProductList(mDataStart, RECOMMEND_COUNT, null, true);
		//获取商品
		loadMoreData();
	}

	//加载更多商品
	@Override
	synchronized public void loadMoreData()
	{
		mProductService.getNewProductList(mDataStart, getPageSize(), null, true);
	}

	@Override
	synchronized public void onRefresh()
	{
		mNewProductInfoList.clear();
		mIndexNewProductsAdapter = null;
		super.onRefresh();
	}

	synchronized private void refreshDataTrafficProducts(List<DataTrafficInfo> productInfoList)
	{
		if (productInfoList == null || productInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			return;
		}
		if (productInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (DataTrafficInfo dataTrafficInfo : productInfoList)
		{
			mDataTrafficProductInfoList.add(dataTrafficInfo);
		}
		if (mIndexDataTrafficProductsAdapter == null)
		{
			mIndexDataTrafficProductsAdapter = new IndexDataTrafficProductsAdapter(this.getContext(), mDataTrafficProductInfoList, new IndexDataTrafficProductsAdapter.IItemClick()
			{
				@Override
				public void onProductClicked(DataTrafficInfo dataTrafficInfo)
				{
					if (UserService.isLogin())
					{
						Intent intent = new Intent(FragmentIndex.this.getContext(), ActivityRechargeDataTraffic.class);
						intent.putExtra(ConstValues.DATA_PRODUCT_ID, dataTrafficInfo.getProductId());
						startActivity(intent);
					}
					else
					{
						startActivity(new Intent(FragmentIndex.this.getContext(), ActivityLogin.class));
					}
				}
			});
			gvDataTrafficProducts.setAdapter(mIndexDataTrafficProductsAdapter);
		}
		else
		{
			mIndexDataTrafficProductsAdapter.notifyDataSetChanged();
		}
		ViewHeightCalTools.setGridViewHeight(gvDataTrafficProducts, NEW_DATA_TRAFFIC_PRODUCT_COLUMNS_NUM, true);
	}

	synchronized private void refreshNewProducts(List<ProductInfo> productInfoList)
	{
		if (productInfoList == null || productInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			return;
		}
		if (productInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (ProductInfo productInfo : productInfoList)
		{
			mNewProductInfoList.add(productInfo);
		}
		if (mIndexNewProductsAdapter == null)
		{
			mIndexNewProductsAdapter = new IndexNewProductsAdapter(this.getContext(), mNewProductInfoList, new IndexNewProductsAdapter.IItemClick()
			{
				@Override
				public void onProductClicked(ProductInfo productInfo)
				{
					Intent intent = new Intent(FragmentIndex.this.getContext(), ActivityProduct.class);
					intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
					startActivity(intent);
				}
			});
			gvNewProducts.setAdapter(mIndexNewProductsAdapter);
		}
		else
		{
			mIndexNewProductsAdapter.notifyDataSetChanged();
		}
		ViewHeightCalTools.setGridViewHeight(gvNewProducts, NEW_PRODUCT_COLUMNS_NUM, true);
	}

	synchronized private void refreshRecommendProducts(List<ProductInfo> productInfoList)
	{
		if (productInfoList == null || productInfoList.size() < 1)
		{
			return;
		}
		mRecommendProductInfoList.clear();
		mIndexRecommendProducts4Adapter = null;
		for (ProductInfo productInfo : productInfoList)
		{
			mRecommendProductInfoList.add(productInfo);
		}
		if (mIndexRecommendProducts4Adapter == null)
		{
			mIndexRecommendProducts4Adapter = new IndexRecommendProducts4Adapter(this.getContext(), mRecommendProductInfoList, new IndexRecommendProducts4Adapter.IItemClick()
			{
				@Override
				public void onProduct1Clicked(ProductInfo productInfo)
				{
					Intent intent = new Intent(FragmentIndex.this.getContext(), ActivityProduct.class);
					intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
					startActivity(intent);
				}

				@Override
				public void onProduct2Clicked(ProductInfo productInfo)
				{
					Intent intent = new Intent(FragmentIndex.this.getContext(), ActivityProduct.class);
					intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
					startActivity(intent);
				}

				@Override
				public void onProduct3Clicked(ProductInfo productInfo)
				{
					Intent intent = new Intent(FragmentIndex.this.getContext(), ActivityProduct.class);
					intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
					startActivity(intent);
				}

				@Override
				public void onProduct4Clicked(ProductInfo productInfo)
				{
					Intent intent = new Intent(FragmentIndex.this.getContext(), ActivityProduct.class);
					intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
					startActivity(intent);
				}
			});
			lvRecommendProducts.setAdapter(mIndexRecommendProducts4Adapter);
		}
		else
		{
			mIndexRecommendProducts4Adapter.notifyDataSetChanged();
		}
		ViewHeightCalTools.setListViewHeight(lvRecommendProducts, true);
	}

	synchronized private void refreshConcessionProducts(List<ProductInfo> productInfoList)
	{
		if (productInfoList == null || productInfoList.size() < 1)
		{
			return;
		}
		mConcessionProductInfoList.clear();
		mIndexConcessionProductsAdapter = null;
		for (ProductInfo productInfo : productInfoList)
		{
			mConcessionProductInfoList.add(productInfo);
		}
		if (mIndexConcessionProductsAdapter == null)
		{
			mIndexConcessionProductsAdapter = new IndexRecommendProducts4Adapter(this.getContext(), mConcessionProductInfoList, new IndexRecommendProducts4Adapter.IItemClick()
			{
				@Override
				public void onProduct1Clicked(ProductInfo productInfo)
				{
					Intent intent = new Intent(FragmentIndex.this.getContext(), ActivityProduct.class);
					intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
					startActivity(intent);
				}

				@Override
				public void onProduct2Clicked(ProductInfo productInfo)
				{
					Intent intent = new Intent(FragmentIndex.this.getContext(), ActivityProduct.class);
					intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
					startActivity(intent);
				}

				@Override
				public void onProduct3Clicked(ProductInfo productInfo)
				{
					Intent intent = new Intent(FragmentIndex.this.getContext(), ActivityProduct.class);
					intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
					startActivity(intent);
				}

				@Override
				public void onProduct4Clicked(ProductInfo productInfo)
				{
					Intent intent = new Intent(FragmentIndex.this.getContext(), ActivityProduct.class);
					intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
					startActivity(intent);
				}
			});
			lvConcessionProducts.setAdapter(mIndexConcessionProductsAdapter);
		}
		else
		{
			mIndexConcessionProductsAdapter.notifyDataSetChanged();
		}
		ViewHeightCalTools.setListViewHeight(lvConcessionProducts, true);
	}

	@Override
	public void fragmentShow()
	{
		super.fragmentShow();
		updateView();
		//获取新闻
		mNewsService.getNewsInfoList(0, SHOW_NEWS_COUNT, null, null, null, true);
	}

	@Override
	public void fragmentHide()
	{
		super.fragmentHide();
		cancelScheduledFuture();
	}

	@Override
	public void onDestroyView()
	{
		stopScheduledFuture();
		if (null != locationClient)
		{
			/**
			 * 如果AMapLocationClient是在当前Activity实例化的，
			 * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
			 */
			locationClient.onDestroy();
			locationClient = null;
			locationOption = null;
		}
		super.onDestroyView();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		//		if (scrollState == SCROLL_STATE_TOUCH_SCROLL)
		//		{
		//			layoutSearch.setVisibility(View.GONE);
		//		}
		//		else if (scrollState == SCROLL_STATE_IDLE)
		//		{
		//			layoutSearch.setVisibility(View.VISIBLE);
		//		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
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
			case R.id.tv_news:
			{
				NewsInfo newsInfo = (NewsInfo) tvNews.getTag();
				if (newsInfo != null)
				{
					Intent intent = new Intent(this.getContext(), ActivityNews.class);
					intent.putExtra(ConstValues.DATA_NEWS_ID, newsInfo.getNewsId());
					startActivity(intent);
				}
				break;
			}
			case R.id.iv_more_news:
			case R.id.tv_more_news:
			{
				Intent intent = new Intent(this.getContext(), ActivityNewsList.class);
				startActivity(intent);
				break;
			}
			case R.id.btn_qr:
			{
				Intent intent = new Intent(this.getContext(), CaptureActivity.class);
				startActivityForResult(intent, REQ_SCAN_QR);
				break;
			}
			case R.id.tv_new_product_more:
			{
				startActivity(new Intent(this.getContext(), ActivitySearchProducts.class));
				break;
			}
			case R.id.tv_data_traffic_product_more:
			{
				if (UserService.isLogin())
				{
					startActivity(new Intent(this.getContext(), ActivityRechargeDataTraffic.class));
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
		xListView.stopRefresh();
		xListView.stopLoadMore();
		if (url.endsWith(ApiConfig.GET_NEWS_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<NewsInfo> newsInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<NewsInfo>>()
				{
				}.getType());
				if (newsInfoList != null && newsInfoList.size() > 0)
				{
					mNewsInfoList.clear();
					int count = 0;
					for (NewsInfo newsInfo : newsInfoList)
					{
						if (count == 0)
						{
							tvNews.setText(newsInfo.getTitle());
							tvNews.setTag(newsInfo);
						}
						mNewsInfoList.add(newsInfo);
						count++;
						if (count > SHOW_NEWS_COUNT)
						{
							break;
						}
					}
				}
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.GET_RECOMMEND_PRODUCT_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<ProductInfo> productInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductInfo>>()
				{
				}.getType());
				refreshRecommendProducts(productInfoList);
			}
		}
		else if (url.endsWith(ApiConfig.GET_CONCESSION_PRODUCT_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<ProductInfo> productInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductInfo>>()
				{
				}.getType());
				refreshConcessionProducts(productInfoList);
			}
		}
		else if (url.endsWith(ApiConfig.GET_NEW_PRODUCT_LIST))
		{
			List<ProductInfo> productInfoList = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				productInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ProductInfo>>()
				{
				}.getType());
			}
			refreshNewProducts(productInfoList);
		}
		else if (url.endsWith(ApiConfig.GET_NEW_DATA_TRAFFIC_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<DataTrafficInfo> dataTrafficInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<DataTrafficInfo>>()
				{
				}.getType());
				mDataTrafficProductInfoList.clear();
				refreshDataTrafficProducts(dataTrafficInfoList);
			}
		}
		else if (url.endsWith(ApiConfig.GET_BANNER_LIST))
		{
			List<BannerInfo> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<BannerInfo>>()
				{
				}.getType());
			}
			setViewPagerAdapter(list);
		}
		else if (url.endsWith(ApiConfig.GET_INDEX_FUNC_LIST))
		{
			List<ClientIndexFuncInfo> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<ClientIndexFuncInfo>>()
				{
				}.getType());
			}
			initFuncData(list);
		}
		return false;
	}

	private void beginLocation()
	{
		initOption();
		// 设置定位参数
		locationClient.setLocationOption(locationOption);
		// 启动定位
		locationClient.startLocation();
		mHandler.sendEmptyMessage(MSG_LOCATION_START);
	}

	// 根据控件的选择，重新设置定位参数
	private void initOption()
	{
		// 设置是否需要显示地址信息
		locationOption.setNeedAddress(true);
		// 设置是否开启缓存
		locationOption.setLocationCacheEnable(true);
		//设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
		locationOption.setOnceLocationLatest(false);
		/**
		 *  设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
		 *  只有持续定位设置定位间隔才有效，单次定位无效
		 */
		locationOption.setInterval(1000);
	}

	// 定位监听
	@Override
	public void onLocationChanged(AMapLocation loc)
	{
		if (null != loc)
		{
			Message msg = mHandler.obtainMessage();
			msg.obj = loc;
			msg.what = MSG_LOCATION_FINISH;
			mHandler.sendMessage(msg);
		}
	}
	//	@Override
	//	public void onActivityResult(int requestCode, int resultCode, Intent data)
	//	{
	//		super.onActivityResult(requestCode, resultCode, data);
	//		if (requestCode == REQ_SCAN_QR)
	//		{
	//			if (resultCode == RESULT_OK)
	//			{
	//				Bundle bundle = data.getExtras();
	//				String scanResult = bundle.getString("result");
	//				ToastView.showCenterToast(this.getContext(), R.drawable.ic_done, "扫描结果：" + scanResult);
	//			}
	//		}
	//	}
}
