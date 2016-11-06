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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.BannerInfo;
import com.lrcall.appbst.models.NewsInfo;
import com.lrcall.appbst.models.PointProductInfo;
import com.lrcall.appbst.models.ProductInfo;
import com.lrcall.appbst.models.TableData;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.BannerService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.NewsService;
import com.lrcall.appbst.services.PointProductService;
import com.lrcall.db.DbBannerInfoFactory;
import com.lrcall.ui.adapter.IndexRecommendProducts4Adapter;
import com.lrcall.ui.adapter.PointProductsAdapter;
import com.lrcall.ui.adapter.SectionsPagerAdapter;
import com.lrcall.ui.customer.DisplayTools;
import com.lrcall.ui.customer.ViewHeightCalTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.DateTimeTools;
import com.lrcall.utils.GsonTools;
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

public class ActivityPointProductShop extends MyBasePageActivity implements View.OnClickListener, IAjaxDataResponse
{
	private static final String TAG = ActivityPointProductShop.class.getSimpleName();
	private static final int SCROLL_VIEW_PAGE = 111;
	private static final int SCROLL_NEWS = 112;
	private static final int NEW_PRODUCT_COLUMNS_NUM = 2;
	private static final int SHOW_NEWS_COUNT = 3;
	private static final long SCROLL_TIME = 5;
	private static final int RECOMMEND_COUNT = 4;
	private View layoutSearch, headView;
	private EditText etSearch;
	private TextView tvNews;
	private GridView gvNewProducts/*最新商品*/;
	private ViewPager viewPager;
	private ListView lvRecommendProducts/*推荐商品*/;
	private ScheduledExecutorService scheduledExecutorService = null;
	private ScheduledFuture scheduledFuture = null;
	private NewsService mNewsService;
	private PointProductService mPointProductService;
	private BannerService mBannerService;
	private IndexRecommendProducts4Adapter indexRecommendProductsAdapter;
	private PointProductsAdapter mPointProductsAdapter;
	private final List<Fragment> mFragmentRecommendList = new ArrayList<>();
	private final List<NewsInfo> mNewsInfoList = new ArrayList<>();
	private final List<PointProductInfo> mNewProductInfoList = new ArrayList<>();
	private final List<ProductInfo> mRecommendProductInfoList = new ArrayList<>();
	private SmartTabLayout viewPagerTab;
	private SectionsPagerAdapter sectionsPagerAdapter;
	private View layoutNoProduct;
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
					}
					break;
				}
			}
		}
	};

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
					LogcatTools.debug(TAG, "ScheduledFuture,时间:" + DateTimeTools.getCurrentTime());
					mHandler.sendEmptyMessage(SCROLL_VIEW_PAGE);
					mHandler.sendEmptyMessage(SCROLL_NEWS);
				}
			}, SCROLL_TIME, SCROLL_TIME, TimeUnit.SECONDS);
		}
		catch (RejectedExecutionException e)
		{
			LogcatTools.debug(TAG, "ScheduledFuture,RejectedExecutionException:" + e.getMessage());
		}
	}

	synchronized private void cancelScheduledFuture()
	{
		if (scheduledFuture != null)
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
		setContentView(R.layout.activity_point_product_shop);
		mNewsService = new NewsService(this);
		mNewsService.addDataResponse(this);
		mPointProductService = new PointProductService(this);
		mPointProductService.addDataResponse(this);
		mBannerService = new BannerService(this);
		mBannerService.addDataResponse(this);
		viewInit();
		updateView();
		setViewPagerAdapter(DbBannerInfoFactory.getInstance().getBannerInfoList());
		onRefresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_point_product_shop, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_order_list)
		{
			startActivity(new Intent(this, ActivityPointOrderList.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		layoutSearch = findViewById(R.id.layout_search);
		etSearch = (EditText) findViewById(R.id.et_search);
		etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)//actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
				{
					onRefresh();
					return true;
				}
				return false;
			}
		});
		xListView = (XListView) findViewById(R.id.xlist);
		headView = LayoutInflater.from(this).inflate(R.layout.activity_point_product_shop_head, null);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.addHeaderView(headView);
		xListView.setAdapter(null);
		xListView.setXListViewListener(this);
		tvNews = (TextView) findViewById(R.id.tv_news);
		tvNews.setOnClickListener(this);
		findViewById(R.id.iv_more_news).setOnClickListener(this);
		findViewById(R.id.tv_more_news).setOnClickListener(this);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		//设置图片的长宽，这里便于制作图片
		ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
		layoutParams.width = DisplayTools.getWindowWidth(this);
		layoutParams.height = DisplayTools.getWindowWidth(this) * 1 / 2;
		viewPager.setLayoutParams(layoutParams);
		viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
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
		lvRecommendProducts = (ListView) findViewById(R.id.list_recommend_products);
		gvNewProducts = (GridView) findViewById(R.id.gv_new_products);
		gvNewProducts.setNumColumns(NEW_PRODUCT_COLUMNS_NUM);
		findViewById(R.id.tv_new_product_more).setOnClickListener(this);
		findViewById(R.id.btn_search).setOnClickListener(this);
		layoutNoProduct = findViewById(R.id.layout_no_product);
	}

	//设置图片适配器
	private void setViewPagerAdapter(List<BannerInfo> bannerInfoList)
	{
		if (bannerInfoList != null && bannerInfoList.size() > 0)
		{
			mFragmentRecommendList.clear();
			for (BannerInfo bannerInfo : bannerInfoList)
			{
				mFragmentRecommendList.add(FragmentServerImage.newInstance(ApiConfig.getServerPicUrl(bannerInfo.getPicUrl()), DisplayTools.getWindowWidth(this), bannerInfo.getContent()));
			}
			sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mFragmentRecommendList);
			viewPager.setAdapter(sectionsPagerAdapter);
			viewPagerTab.setViewPager(viewPager);
		}
	}

	//刷新数据
	@Override
	synchronized public void refreshData()
	{
		mNewProductInfoList.clear();
		mPointProductsAdapter = null;
		mBannerService.getBannerInfoList(0, RECOMMEND_COUNT, null, true);
		//获取新闻
		//		mNewsService.getNewsInfoList(null, true);
		//		mPointProductService.getRecommendProductList(mDataStart, RECOMMEND_COUNT, null, true);
		//获取商品
		loadMoreData();
	}

	//加载更多商品
	@Override
	synchronized public void loadMoreData()
	{
		String condition = etSearch.getText().toString();
		mPointProductService.getPointProductList(condition, mDataStart, getPageSize(), null, null, null, true);
	}

	synchronized private void refreshNewProducts(List<PointProductInfo> pointProductInfoList)
	{
		if (pointProductInfoList == null || pointProductInfoList.size() < 1)
		{
			xListView.setPullLoadEnable(false);
			if (mNewProductInfoList.size() < 1)
			{
				gvNewProducts.setVisibility(View.GONE);
				layoutNoProduct.setVisibility(View.VISIBLE);
			}
			return;
		}
		gvNewProducts.setVisibility(View.VISIBLE);
		layoutNoProduct.setVisibility(View.GONE);
		if (pointProductInfoList.size() < getPageSize())
		{
			xListView.setPullLoadEnable(false);
		}
		for (PointProductInfo pointProductInfo : pointProductInfoList)
		{
			mNewProductInfoList.add(pointProductInfo);
		}
		if (mPointProductsAdapter == null)
		{
			mPointProductsAdapter = new PointProductsAdapter(this, mNewProductInfoList, new PointProductsAdapter.IItemClick()
			{
				@Override
				public void onProductClicked(PointProductInfo pointProductInfo)
				{
					if (pointProductInfo != null)
					{
						Intent intent = new Intent(ActivityPointProductShop.this, ActivityPointProduct.class);
						intent.putExtra(ConstValues.DATA_PRODUCT_ID, pointProductInfo.getProductId());
						startActivity(intent);
					}
				}
			});
			gvNewProducts.setAdapter(mPointProductsAdapter);
		}
		else
		{
			mPointProductsAdapter.notifyDataSetChanged();
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
		indexRecommendProductsAdapter = null;
		for (ProductInfo productInfo : productInfoList)
		{
			mRecommendProductInfoList.add(productInfo);
		}
		if (indexRecommendProductsAdapter == null)
		{
			indexRecommendProductsAdapter = new IndexRecommendProducts4Adapter(this, mRecommendProductInfoList, new IndexRecommendProducts4Adapter.IItemClick()
			{
				@Override
				public void onProduct1Clicked(ProductInfo productInfo)
				{
					if (productInfo != null)
					{
						Intent intent = new Intent(ActivityPointProductShop.this, ActivityProduct.class);
						intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
						startActivity(intent);
					}
				}

				@Override
				public void onProduct2Clicked(ProductInfo productInfo)
				{
					if (productInfo != null)
					{
						Intent intent = new Intent(ActivityPointProductShop.this, ActivityProduct.class);
						intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
						startActivity(intent);
					}
				}

				@Override
				public void onProduct3Clicked(ProductInfo productInfo)
				{
					if (productInfo != null)
					{
						Intent intent = new Intent(ActivityPointProductShop.this, ActivityProduct.class);
						intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
						startActivity(intent);
					}
				}

				@Override
				public void onProduct4Clicked(ProductInfo productInfo)
				{
					if (productInfo != null)
					{
						Intent intent = new Intent(ActivityPointProductShop.this, ActivityProduct.class);
						intent.putExtra(ConstValues.DATA_PRODUCT_ID, productInfo.getProductId());
						startActivity(intent);
					}
				}
			});
			lvRecommendProducts.setAdapter(indexRecommendProductsAdapter);
		}
		else
		{
			indexRecommendProductsAdapter.notifyDataSetChanged();
		}
		ViewHeightCalTools.setListViewHeight(lvRecommendProducts, true);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		updateView();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		cancelScheduledFuture();
	}

	@Override
	public void onDestroy()
	{
		stopScheduledFuture();
		super.onDestroy();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.tv_news:
			{
				NewsInfo newsInfo = (NewsInfo) tvNews.getTag();
				if (newsInfo != null)
				{
					Intent intent = new Intent(this, ActivityNews.class);
					intent.putExtra(ConstValues.DATA_NEWS_ID, newsInfo.getNewsId());
					startActivity(intent);
				}
				break;
			}
			case R.id.iv_more_news:
			case R.id.tv_more_news:
			{
				Intent intent = new Intent(this, ActivityNewsList.class);
				startActivity(intent);
				break;
			}
			case R.id.tv_new_product_more:
			{
				startActivity(new Intent(this, ActivitySearchProducts.class));
				break;
			}
			case R.id.btn_search:
			{
				onRefresh();
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
		else if (url.endsWith(ApiConfig.GET_POINT_PRODUCT_LIST))
		{
			List<PointProductInfo> list = null;
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				list = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<PointProductInfo>>()
				{
				}.getType());
			}
			refreshNewProducts(list);
		}
		else if (url.endsWith(ApiConfig.GET_BANNER_LIST))
		{
			TableData tableData = GsonTools.getObject(result, TableData.class);
			if (tableData != null)
			{
				List<BannerInfo> bannerInfoList = GsonTools.getObjects(GsonTools.toJson(tableData.getData()), new TypeToken<List<BannerInfo>>()
				{
				}.getType());
				setViewPagerAdapter(bannerInfoList);
			}
		}
		return false;
	}
}
