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
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.client.android.CaptureActivity;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.BannerInfo;
import com.lrcall.appbst.models.ClientFuncTypeInfo;
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
import com.lrcall.enums.ClientBannerType;
import com.lrcall.enums.ClientFuncActivityType;
import com.lrcall.enums.ClientFuncType;
import com.lrcall.ui.adapter.IndexDataTrafficProductsAdapter;
import com.lrcall.ui.adapter.IndexFuncsAdapter;
import com.lrcall.ui.adapter.IndexNewProductAdapter;
import com.lrcall.ui.adapter.IndexRecommendProducts4Adapter;
import com.lrcall.ui.customer.ViewHeightCalTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.StringTools;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

public class FragmentIndex extends MyBaseBannerPageFragment implements View.OnClickListener, AbsListView.OnScrollListener, IAjaxDataResponse
{
	private static final String TAG = FragmentIndex.class.getSimpleName();
	private static final int SCROLL_NEWS = 112;
	private static final int NEW_PRODUCT_COLUMNS_NUM = 1;
	private static final int NEW_DATA_TRAFFIC_PRODUCT_COLUMNS_NUM = 2;
	private static final int SHOW_NEWS_COUNT = 3;
//	private static final int DATA_TRAFFIC_COUNT = 4;
	private static final int RECOMMEND_COUNT = 4;
	private static final int REQ_SCAN_QR = 1200;
	private View layoutSearch, headView, layoutHuodong;
	private EditText etSearch;
	private TextView tvNews;
	private GridView gvFuncs/*功能区*/, gvNewProducts/*最新商品*/, gvDataTrafficProducts/*最新流量商品*/;
	private ListView lvRecommendProducts/*推荐商品*/, lvConcessionProducts/*促销商品*/, lvNewProducts;
	private NewsService mNewsService;
	private ProductService mProductService;
	private DataTrafficService mDataTrafficService;
	private BannerService mBannerService;
	private ClientService mClientService;
	private IndexRecommendProducts4Adapter mIndexRecommendProducts4Adapter;
	private IndexRecommendProducts4Adapter mIndexConcessionProductsAdapter;
	private IndexNewProductAdapter mIndexNewProductAdapter;
	private IndexDataTrafficProductsAdapter mIndexDataTrafficProductsAdapter;
	private final List<NewsInfo> mNewsInfoList = new ArrayList<>();
	private final List<DataTrafficInfo> mDataTrafficProductInfoList = new ArrayList<>();
	private final List<ProductInfo> mNewProductInfoList = new ArrayList<>();
	private final List<ProductInfo> mRecommendProductInfoList = new ArrayList<>();
	private final List<ProductInfo> mConcessionProductInfoList = new ArrayList<>();
	protected final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
				case SCROLL_VIEW_PAGE:
				{
					int index = bannerViewPager.getCurrentItem();
					if (index < mFragmentBannerList.size() - 1)
					{
						index++;
					}
					else
					{
						index = 0;
					}
					bannerViewPager.setCurrentItem(index);
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
			}
		}
	};

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
		updateView();
		//		setViewPagerAdapter(DbBannerInfoFactory.getInstance().getBannerInfoList(ClientBannerType.PAGE_INDEX.getType()));
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
					startActivity(new Intent(FragmentIndex.this.getContext(), ActivityProductsSearch.class));
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
		tvNews = (TextView) rootView.findViewById(R.id.tv_news);
		tvNews.setOnClickListener(this);
		rootView.findViewById(R.id.iv_more_news).setOnClickListener(this);
		rootView.findViewById(R.id.tv_more_news).setOnClickListener(this);
		gvFuncs = (GridView) headView.findViewById(R.id.gv_func);
		bannerViewPager = (ViewPager) rootView.findViewById(R.id.banner_viewpager);
		bannerViewPagerTab = (SmartTabLayout) rootView.findViewById(R.id.banner_viewpagertab);
		setBannerWidthAndHeight(DisplayTools.getWindowWidth(this.getContext()), DisplayTools.getWindowWidth(this.getContext()) * 1 / 2);
		initBannerView();
		lvRecommendProducts = (ListView) rootView.findViewById(R.id.list_recommend_products);
		lvConcessionProducts = (ListView) rootView.findViewById(R.id.list_concession_products);
		gvNewProducts = (GridView) rootView.findViewById(R.id.gv_new_products);
		gvNewProducts.setNumColumns(NEW_PRODUCT_COLUMNS_NUM);
		gvDataTrafficProducts = (GridView) rootView.findViewById(R.id.gv_new_data_traffic_products);
		gvDataTrafficProducts.setNumColumns(NEW_DATA_TRAFFIC_PRODUCT_COLUMNS_NUM);
		rootView.findViewById(R.id.tv_new_product_more).setOnClickListener(this);
		rootView.findViewById(R.id.tv_data_traffic_product_more).setOnClickListener(this);
		layoutHuodong = rootView.findViewById(R.id.layout_huodong);
		//设置图片的长宽，这里便于制作图片
		ViewGroup.LayoutParams layoutParams = layoutHuodong.getLayoutParams();
		layoutParams.width = DisplayTools.getWindowWidth(this.getContext());
		layoutParams.height = layoutParams.width / 3 - DisplayTools.dip2px(this.getContext(), 20);
		layoutHuodong.setLayoutParams(layoutParams);
		rootView.findViewById(R.id.layout_product_today_new).setOnClickListener(this);
		rootView.findViewById(R.id.layout_product_panic_buying).setOnClickListener(this);
		rootView.findViewById(R.id.layout_product_recommend).setOnClickListener(this);
		lvNewProducts = (ListView) rootView.findViewById(R.id.list_new_products);
		super.viewInit(rootView);
	}

	//初始化数据
	synchronized protected void initFuncData(List<ClientIndexFuncInfo> list)
	{
		if (list == null || list.size() < 1)
		{
			gvFuncs.setVisibility(View.GONE);
			return;
		}
		IndexFuncsAdapter indexFuncsAdapter = new IndexFuncsAdapter(this.getContext(), list, new IndexFuncsAdapter.IItemClicked()
		{
			@Override
			public void onFuncClicked(ClientIndexFuncInfo clientIndexFuncInfo)
			{
				if (clientIndexFuncInfo != null)
				{
					ClientFuncTypeInfo clientClientFuncTypeInfo = GsonTools.getObject(clientIndexFuncInfo.getContent(), ClientFuncTypeInfo.class);
					if (clientClientFuncTypeInfo != null)
					{
						if (clientClientFuncTypeInfo.getType().equalsIgnoreCase(ClientFuncType.OPEN_URL.getType()))
						{
							ActivityWebView.startWebActivity(FragmentIndex.this.getContext(), clientIndexFuncInfo.getName(), clientClientFuncTypeInfo.getContent());
						}
						else if (clientClientFuncTypeInfo.getType().equalsIgnoreCase(ClientFuncType.OPEN_ACTIIVTY.getType()))
						{
							if (clientClientFuncTypeInfo.getContent().equalsIgnoreCase(ClientFuncActivityType.SHOP_PRODUCTS.getType()))
							{
								startActivity(new Intent(FragmentIndex.this.getContext(), ActivityShopProducts.class));
							}
							else if (clientClientFuncTypeInfo.getContent().equalsIgnoreCase(ClientFuncActivityType.RECHARGE_DATA_TRAFFIC.getType()))
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
							else if (clientClientFuncTypeInfo.getContent().equalsIgnoreCase(ClientFuncActivityType.POINT_PRODUCT_SHOP.getType()))
							{
								startActivity(new Intent(FragmentIndex.this.getContext(), ActivityPointProductShop.class));
							}
							else if (clientClientFuncTypeInfo.getContent().equalsIgnoreCase(ClientFuncActivityType.PRODUCTS.getType()))
							{
								Intent intent = new Intent(FragmentIndex.this.getContext(), ActivityProductsSearch.class);
								intent.putExtra(ConstValues.DATA_PRODUCT_SORT_ID, clientClientFuncTypeInfo.getParams());
								startActivity(intent);
							}
							else if (clientClientFuncTypeInfo.getContent().equalsIgnoreCase(ClientFuncActivityType.SHENGXIAN.getType()))
							{
								Intent intent = new Intent(FragmentIndex.this.getContext(), ActivityShengxian.class);
								intent.putExtra(ConstValues.DATA_PRODUCT_SORT_ID, clientClientFuncTypeInfo.getParams());
								startActivity(intent);
							}
						}
					}
					else
					{
					}
				}
			}
		});
		gvFuncs.setAdapter(indexFuncsAdapter);
		int size = (list.size() / 2) + (list.size() % 2 == 0 ? 0 : 1);
		gvFuncs.setNumColumns(size);
		ViewHeightCalTools.setGridViewHeight(gvFuncs, size, true);
	}

	//刷新数据
	@Override
	synchronized public void refreshData()
	{
		mClientService.getIndexFuncList(null, true);
		mBannerService.getBannerInfoList(ClientBannerType.PAGE_INDEX.getType(), 0, RECOMMEND_COUNT, null, true);
		//		mDataTrafficService.getNewDataTrafficInfoList(0, DATA_TRAFFIC_COUNT, null, true);
		//获取新闻
		mNewsService.getNewsInfoList(0, SHOW_NEWS_COUNT, null, null, null, true);
		//获取商品
		loadMoreData();
	}

	//加载更多商品
	@Override
	synchronized public void loadMoreData()
	{
		mProductService.getNewProductList(mDataStart, 6, null, true);
	}

	@Override
	synchronized public void onRefresh()
	{
		mNewProductInfoList.clear();
		if (mIndexNewProductAdapter != null)
		{
			mIndexNewProductAdapter.notifyDataSetChanged();
		}
		mIndexNewProductAdapter = null;
		super.onRefresh();
	}

	synchronized private void refreshDataTrafficProducts(List<DataTrafficInfo> dataTrafficInfoList)
	{
		if (dataTrafficInfoList == null || dataTrafficInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			return;
		}
		if (dataTrafficInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (DataTrafficInfo dataTrafficInfo : dataTrafficInfoList)
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
		if (mIndexNewProductAdapter == null)
		{
			mIndexNewProductAdapter = new IndexNewProductAdapter(this.getContext(), mNewProductInfoList, new IndexNewProductAdapter.IItemClick()
			{
				@Override
				public void onProductClicked(ProductInfo productInfo)
				{
					Intent intent = new Intent(FragmentIndex.this.getContext(), ActivityProduct.class);
					intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
					startActivity(intent);
				}
			});
			lvNewProducts.setAdapter(mIndexNewProductAdapter);
		}
		else
		{
			mIndexNewProductAdapter.notifyDataSetChanged();
		}
		//		ViewHeightCalTools.setGridViewHeight(gvNewProducts, NEW_PRODUCT_COLUMNS_NUM, true);
		ViewHeightCalTools.setListViewHeight(lvNewProducts, false);
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
		//获取新闻
		mNewsService.getNewsInfoList(0, SHOW_NEWS_COUNT, null, null, null, true);
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
				startActivity(new Intent(this.getContext(), ActivityProductsSearch.class));
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
			case R.id.layout_product_today_new:
			{
				Intent intent = new Intent(this.getContext(), ActivityHuodong.class);
				intent.putExtra(ConstValues.DATA_INDEX, 0);
				startActivity(intent);
				break;
			}
			case R.id.layout_product_panic_buying:
			{
				Intent intent = new Intent(this.getContext(), ActivityHuodong.class);
				intent.putExtra(ConstValues.DATA_INDEX, 1);
				startActivity(intent);
				break;
			}
			case R.id.layout_product_recommend:
			{
				Intent intent = new Intent(this.getContext(), ActivityHuodong.class);
				intent.putExtra(ConstValues.DATA_INDEX, 2);
				startActivity(intent);
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
			mProductService.getConcessionProductList(0, RECOMMEND_COUNT, null, true);
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
			mProductService.getRecommendProductList(0, RECOMMEND_COUNT, null, true);
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
}
