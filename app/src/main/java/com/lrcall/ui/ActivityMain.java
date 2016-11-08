/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.external.xlistview.XListView;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.UpdateInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UpdateService;
import com.lrcall.appbst.services.UserService;
import com.lrcall.enums.EventTypeLayoutSideMain;
import com.lrcall.events.UserEvent;
import com.lrcall.models.TabInfo;
import com.lrcall.ui.customer.MyActionBarDrawerToggle;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.BmpTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.GsonTools;
import com.lrcall.utils.PreferenceUtils;
import com.lrcall.utils.apptools.AppFactory;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends MyBaseActivity implements MyActionBarDrawerToggle.ActionBarDrawerToggleStatusChanged, XListView.IXListViewListener, IAjaxDataResponse
{
	public static final int INDEX = 0;
	public static final int DIALER = 1;
	public static final int USER = 2;
	private static final int PAGE_COUNT = 3;
	//暂时没用的
	public static final int CATEGORY = 6;
	public static final int INTERACT = 4;
	public static final int FIND = 7;
	private static ActivityMain instance = null;
	private final List<TabInfo> tabInfos = new ArrayList<>();
	private View vHead;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ViewPager viewPager;
	private XListView xListView;
	private LayoutSideMain layoutSideMain;
	private Menu menu;
	private UpdateService mUpdateService;

	public static ActivityMain getInstance()
	{
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		instance = this;
		viewInit();
		EventBus.getDefault().register(this);
		//检查更新
		mUpdateService = new UpdateService(this);
		mUpdateService.addDataResponse(this);
		mUpdateService.checkUpdate(null, false);
		//		int i = 9 / 0;
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		closeDrawerLayout();
	}

	@Override
	protected void onDestroy()
	{
		instance = null;
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	synchronized protected void viewInit()
	{
		super.viewInit();
		setSwipeBackEnable(false); //禁止滑动返回
		vHead = findViewById(R.id.header);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
		mDrawerToggle = new MyActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close, this);
		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		//初始化Tab
		tabInfos.add(new TabInfo(INDEX, "首页", R.drawable.ic_tab_dialer_normal, FragmentIndex.class));
		//		tabInfos.add(new TabInfo(INTERACT, "互动", R.drawable.ic_tab_contacts_normal, FragmentInteract.class));
		//		tabInfos.add(new TabInfo(CATEGORY, "分类", R.drawable.ic_tab_contacts_normal, FragmentCategory.class));
		tabInfos.add(new TabInfo(DIALER, "电话", R.drawable.ic_tab_money_normal, FragmentDialer2.class));
		//		tabInfos.add(new TabInfo(FIND, "发现", R.drawable.ic_tab_money_normal, FragmentFind.class));
		tabInfos.add(new TabInfo(USER, "我的", R.drawable.ic_tab_more_normal, FragmentMore.class));
		ViewGroup tab = (ViewGroup) findViewById(R.id.tab);
		//加载tab布局
		tab.addView(LayoutInflater.from(this).inflate(R.layout.layout_main_tab, tab, false));
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
		viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider()
		{
			@Override
			public View createTabView(ViewGroup container, int position, PagerAdapter adapter)
			{
				TabInfo tabInfo = tabInfos.get(position);
				View view = LayoutInflater.from(viewPagerTab.getContext()).inflate(R.layout.item_main_tab, container, false);
				tabInfo.setImgIcon((ImageView) view.findViewById(R.id.tab_icon));
				tabInfo.setTvLabel((TextView) view.findViewById(R.id.tab_label));
				tabInfo.getImgIcon().setImageResource(tabInfo.getImgResId());
				tabInfo.getTvLabel().setText(tabInfo.getLabel());
				if (tabInfo.getIndex() == DIALER)
				{
					View.OnClickListener listener = new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							//							LogcatTools.debug("onPageSelected", "点击事件");
							if (viewPager.getCurrentItem() == DIALER)
							{
								if (FragmentDialer2.getInstance() != null)
								{
									//									LogcatTools.debug("onPageSelected", "切换键盘2");
									FragmentDialer2.getInstance().switchPad();
								}
							}
							else
							{
								setCurrentPage(DIALER);
							}
						}
					};
					//					LogcatTools.debug("onPageSelected", "添加点击事件");
					tabInfo.getImgIcon().setOnClickListener(listener);
					tabInfo.getTvLabel().setOnClickListener(listener);
					//					view.setOnClickListener(listener);
				}
				return view;
			}
		});
		viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{
			}

			@Override
			public void onPageSelected(int position)
			{
				int size = tabInfos.size();
				for (int i = 0; i < size; i++)
				{
					TabInfo tabInfo = tabInfos.get(i);
					if (i == position)
					{
						tabInfo.getTvLabel().setTextColor(getResources().getColor(R.color.main_icon_enabled));
						mToolbar.setTitle(tabInfo.getLabel());
					}
					else
					{
						tabInfo.getTvLabel().setTextColor(getResources().getColor(R.color.main_icon_disabled));
					}
				}
				if (position == INDEX || position == CATEGORY || position == INTERACT)
				{
					//隐藏标题
					vHead.setVisibility(View.GONE);
				}
				else
				{
					//显示标题
					vHead.setVisibility(View.VISIBLE);
				}
				setMenu();
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
			}
		});
		viewPager.setOffscreenPageLimit(PAGE_COUNT);
		FragmentPagerItems pages = new FragmentPagerItems(this);
		for (TabInfo tabInfo : tabInfos)
		{
			pages.add(FragmentPagerItem.of(tabInfo.getLabel(), tabInfo.getLoadClass()));
		}
		FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);
		viewPager.setAdapter(adapter);
		viewPagerTab.setViewPager(viewPager);
		tabInfos.get(0).getTvLabel().setTextColor(getResources().getColor(R.color.main_icon_enabled));
		//侧滑布局
		xListView = (XListView) findViewById(R.id.xlist);
		layoutSideMain = new LayoutSideMain(this);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.addHeaderView(layoutSideMain);
		xListView.setAdapter(null);
		xListView.setXListViewListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_main, menu);
		this.menu = menu;
		setMenu();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_search)
		{
			startActivity(new Intent(this, ActivitySearchProducts.class));
			return true;
		}
		else if (id == R.id.action_callback_info)
		{
			if (!UserService.isLogin())
			{
				startActivity(new Intent(this, ActivityLogin.class));
			}
			else
			{
				startActivity(new Intent(this, ActivityCallbackInfo.class));
			}
			return true;
		}
		else if (id == R.id.action_contacts)
		{
			startActivity(new Intent(this, ActivityDial.class));
			return true;
		}
		else if (id == R.id.action_settings)
		{
			startActivity(new Intent(this, ActivitySettings.class));
			return true;
		}
		else if (id == R.id.action_check_update)
		{
			//这里返回结果不需要在此类处理，所以重新new一个服务类
			new UpdateService(this).checkUpdate("正在检查更新...", true);
			return true;
		}
		else if (id == R.id.action_share)
		{
			new UserService(this).share("请稍后...", true);
			return true;
		}
		else if (id == R.id.action_logout)
		{
			new UserService(this).logout();
			startActivity(new Intent(this, ActivityLogin.class));
			return true;
		}
		else if (id == R.id.action_exit)
		{
			exit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ConstValues.REQUEST_LOGIN_USER)
		{
			if (resultCode == ConstValues.RESULT_LOGIN_SUCCESS)
			{
				mDrawerLayout.openDrawer(Gravity.LEFT);
				layoutSideMain.refresh();
			}
			else
			{
				Toast.makeText(ActivityMain.this, "登录失败！", Toast.LENGTH_SHORT).show();
			}
		}
		else if (requestCode == ConstValues.REQUEST_EDIT_PIC)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				Bitmap bitmap = (Bitmap) data.getExtras().get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
				picSelected(bitmap);
			}
			else
			{
				Toast.makeText(ActivityMain.this, "用户取消操作！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void picSelected(Bitmap bitmap)
	{
		super.picSelected(bitmap);
		String userId = PreferenceUtils.getInstance().getUsername();
		String userHeadPath = AppConfig.getUserPicCacheDir(userId);
		File file = new File(userHeadPath.substring(0, userHeadPath.lastIndexOf("/")));
		if (!file.exists())
		{
			file.mkdirs();
		}
		ByteArrayOutputStream b = BmpTools.compressToByteArrayOutputStream(bitmap);
		if (b != null)
		{
			new UserService(this).updateUserHead(b.toByteArray(), "正在上传图片...", true);
		}
		else
		{
			Toast.makeText(this, "上传图片失败：数据为空！", Toast.LENGTH_LONG).show();
		}
		FileOutputStream f = BmpTools.compressToFileOutputStream(bitmap, userHeadPath);
		if (f == null)
		{
			Toast.makeText(this, "保存图片到手机失败！", Toast.LENGTH_LONG).show();
		}
		mDrawerLayout.openDrawer(Gravity.LEFT);
		layoutSideMain.refresh();
		layoutSideMain.setHeader(bitmap);
	}

	/**
	 * 设置当前到第几页
	 *
	 * @param index
	 */
	public void setCurrentPage(int index)
	{
		if (index > -1 && index < tabInfos.size())
		{
			viewPager.setCurrentItem(index);
		}
	}

	/**
	 * 设置菜单
	 */
	private void setMenu()
	{
		if (menu == null)
		{
			return;
		}
		int index = viewPager.getCurrentItem();
		if (index == INDEX || index == CATEGORY)
		{
			menu.findItem(R.id.action_search).setVisible(true);
		}
		else
		{
			menu.findItem(R.id.action_search).setVisible(false);
		}
		if (index == DIALER)
		{
			menu.findItem(R.id.action_callback_info).setVisible(true);
			//			menu.findItem(R.id.action_contacts).setVisible(true);
		}
		else
		{
			menu.findItem(R.id.action_callback_info).setVisible(false);
			//			menu.findItem(R.id.action_contacts).setVisible(false);
		}
	}

	@Override
	public void onDrawerOpened(View drawerView)
	{
		//		vHead.setVisibility(View.GONE);
		layoutSideMain.refresh();
	}

	@Override
	public void onDrawerClosed(View drawerView)
	{
		//		int position = viewPager.getCurrentItem();
		//		if (position == FIND || position == DIALER || position == USER)
		//		{
		//			vHead.setVisibility(View.VISIBLE);
		//		}
	}

	@Override
	public void onRefresh()
	{
		layoutSideMain.refresh();
		xListView.stopRefresh();
	}

	@Override
	public void onLoadMore()
	{
		xListView.stopLoadMore();
	}

	@Override
	public void onBackPressed()
	{
		startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	public void exit()
	{
		finish();
		System.exit(0);
	}

	@Subscribe
	public void onEventMainThread(EventTypeLayoutSideMain msg)
	{
		if (msg.getType().equalsIgnoreCase(EventTypeLayoutSideMain.CLOSE_DRAWER.getType()))
		{
			closeDrawerLayout();
		}
	}

	@Subscribe
	public void onEventMainThread(UserEvent userEvent)
	{
		menu.findItem(R.id.action_logout).setVisible(UserService.isLogin());
	}

	//关闭侧滑
	private void closeDrawerLayout()
	{
		if (mDrawerLayout != null)
		{
			mDrawerLayout.closeDrawers();
		}
	}

	public DrawerLayout getmDrawerLayout()
	{
		return mDrawerLayout;
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.CHECK_UPDATE))
		{
			UpdateInfo updateInfo = GsonTools.getReturnObject(result, UpdateInfo.class);
			if (updateInfo != null && AppFactory.getInstance().getVersionCode() < updateInfo.getVersionCode())
			{
				mUpdateService.showUpdataDialog(updateInfo);
			}
		}
		return false;
	}
}
