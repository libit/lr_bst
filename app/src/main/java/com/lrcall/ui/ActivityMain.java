/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
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
import com.easemob.redpacketsdk.constant.RPConstant;
import com.easemob.redpacketui.utils.RedPacketUtil;
import com.external.xlistview.XListView;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.db.InviteMessgeDao;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.chatuidemo.ui.GroupsActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.models.UpdateInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.CallbackService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.appbst.services.UpdateService;
import com.lrcall.appbst.services.UserService;
import com.lrcall.enums.EventTypeLayoutSideMain;
import com.lrcall.events.MsgEvent;
import com.lrcall.events.UserEvent;
import com.lrcall.models.TabInfo;
import com.lrcall.ui.customer.MyActionBarDrawerToggle;
import com.lrcall.utils.AppConfig;
import com.lrcall.utils.BitmapTools;
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
	private static final String TAG = ActivityMain.class.getSimpleName();
	private static final int REQ_SET_USER = 110;
	public static final int INDEX = 0;
	public static final int DIALER = 1;
	public static final int MSG = 2;
	public static final int USER = 3;
	//暂时没用的
	public static final int CATEGORY = 6;
	private static ActivityMain instance = null;
	private final List<TabInfo> mTabInfoList = new ArrayList<>();
	private View vHead;
	private DrawerLayout mDrawerLayout;
	private ViewPager viewPager;
	private XListView xListView;
	private LayoutMainSide layoutMainSide;
	private Menu menu;
	private UpdateService mUpdateService;
	private UserService mUserService;
	// user logged into another device
	public boolean isConflict = false;
	// user account was removed
	private boolean isCurrentAccountRemoved = false;
	private android.app.AlertDialog.Builder exceptionBuilder;
	private boolean isExceptionDialogShow = false;
	private BroadcastReceiver internalDebugReceiver;
	private BroadcastReceiver broadcastReceiver;
	private LocalBroadcastManager broadcastManager;
	private InviteMessgeDao inviteMessgeDao;

	public static ActivityMain getInstance()
	{
		return instance;
	}

	/**
	 * check if current user account was remove
	 */
	public boolean getCurrentAccountRemoved()
	{
		return isCurrentAccountRemoved;
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
		new CallbackService(this).getNumberList(0, -1, null, null, null, true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			try
			{
				String packageName = getPackageName();
				PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
				if (!pm.isIgnoringBatteryOptimizations(packageName))
				{
					Intent intent = new Intent();
					intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
					intent.setData(Uri.parse("package:" + packageName));
					startActivity(intent);
				}
			}
			catch (Exception e)
			{
			}
		}
		//make sure activity will not in background if user is logged into another device or removed
		if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false))
		{
			DemoHelper.getInstance().logout(false, null);
			//			finish();
			startActivity(new Intent(this, ActivityLogin.class));
			return;
		}
		else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
		{
			//			finish();
			startActivity(new Intent(this, ActivityLogin.class));
			return;
		}
		showExceptionDialogFromIntent(getIntent());
		inviteMessgeDao = new InviteMessgeDao(this);
		registerBroadcastReceiver();
		EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
		mUserService = new UserService(this);
		mUserService.addDataResponse(this);
		mUserService.needUpdateInfo(null, false);
	}

	EMMessageListener messageListener = new EMMessageListener()
	{
		@Override
		public void onMessageReceived(List<EMMessage> messages)
		{
			// notify new message
			for (EMMessage message : messages)
			{
				DemoHelper.getInstance().getNotifier().onNewMsg(message);
			}
			refreshUIWithMessage();
		}

		@Override
		public void onCmdMessageReceived(List<EMMessage> messages)
		{
			//red packet code : 处理红包回执透传消息
			for (EMMessage message : messages)
			{
				EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
				final String action = cmdMsgBody.action();//获取自定义action
				if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION))
				{
					RedPacketUtil.receiveRedPacketAckMessage(message);
				}
			}
			//end of red packet code
			refreshUIWithMessage();
		}

		@Override
		public void onMessageReadAckReceived(List<EMMessage> messages)
		{
		}

		@Override
		public void onMessageDeliveryAckReceived(List<EMMessage> message)
		{
		}

		@Override
		public void onMessageChanged(EMMessage message, Object change)
		{
		}
	};

	private void refreshUIWithMessage()
	{
		runOnUiThread(new Runnable()
		{
			public void run()
			{
				// refresh unread count
				updateUnreadLabel();
				//				if (currentTabIndex == 0)
				//				{
				// refresh conversation list
				//					if (conversationListFragment != null)
				//					{
				//						conversationListFragment.refresh();
				//					}
				EventBus.getDefault().post(new MsgEvent(MsgEvent.EVENT_MSG_RECEIVED));
				//				}
			}
		});
	}

	private void registerBroadcastReceiver()
	{
		broadcastManager = LocalBroadcastManager.getInstance(this);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
		intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
		intentFilter.addAction(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION);
		broadcastReceiver = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				updateUnreadLabel();
				updateUnreadAddressLable();
				//				if (currentTabIndex == 0)
				//				{
				//					// refresh conversation list
				//					//					if (conversationListFragment != null)
				//					//					{
				//					//						conversationListFragment.refresh();
				//					//					}
				//					EventBus.getDefault().post(new MsgEvent(MsgEvent.EVENT_MSG_RECEIVED));
				//				}
				//				else if (currentTabIndex == 1)
				//				{
				//					//					if (contactListFragment != null)
				//					//					{
				//					//						contactListFragment.refresh();
				//					//					}
				//				}
				EventBus.getDefault().post(new MsgEvent(MsgEvent.EVENT_MSG_RECEIVED));
				String action = intent.getAction();
				if (action.equals(Constant.ACTION_GROUP_CHANAGED))
				{
					if (EaseCommonUtils.getTopActivity(ActivityMain.this).equals(GroupsActivity.class.getName()))
					{
						GroupsActivity.instance.onResume();
					}
				}
				//red packet code : 处理红包回执透传消息
				if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION))
				{
					//					if (conversationListFragment != null)
					//					{
					//						conversationListFragment.refresh();
					//					}
					EventBus.getDefault().post(new MsgEvent(MsgEvent.EVENT_REFRESH_GROUP_RED_PACKET_ACTION));
				}
				//end of red packet code
			}
		};
		broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
	}

	public class MyContactListener implements EMContactListener
	{
		@Override
		public void onContactAdded(String username)
		{
		}

		@Override
		public void onContactDeleted(final String username)
		{
			runOnUiThread(new Runnable()
			{
				public void run()
				{
					if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
							username.equals(ChatActivity.activityInstance.toChatUsername))
					{
						String st10 = getResources().getString(R.string.have_you_removed);
						Toast.makeText(ActivityMain.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG).show();
						ChatActivity.activityInstance.finish();
					}
				}
			});
		}

		@Override
		public void onContactInvited(String username, String reason)
		{
		}

		@Override
		public void onContactAgreed(String username)
		{
		}

		@Override
		public void onContactRefused(String username)
		{
		}
	}

	private void unregisterBroadcastReceiver()
	{
		broadcastManager.unregisterReceiver(broadcastReceiver);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putBoolean("isConflict", isConflict);
		outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (!isConflict && !isCurrentAccountRemoved)
		{
			updateUnreadLabel();
			updateUnreadAddressLable();
		}
		// unregister this event listener when this activity enters the background
		DemoHelper sdkHelper = DemoHelper.getInstance();
		sdkHelper.pushActivity(this);
		EMClient.getInstance().chatManager().addMessageListener(messageListener);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		closeDrawerLayout();
	}

	@Override
	protected void onStop()
	{
		EMClient.getInstance().chatManager().removeMessageListener(messageListener);
		DemoHelper sdkHelper = DemoHelper.getInstance();
		sdkHelper.popActivity(this);
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		instance = null;
		EventBus.getDefault().unregister(this);
		if (exceptionBuilder != null)
		{
			exceptionBuilder.create().dismiss();
			exceptionBuilder = null;
			isExceptionDialogShow = false;
		}
		unregisterBroadcastReceiver();
		try
		{
			unregisterReceiver(internalDebugReceiver);
		}
		catch (Exception e)
		{
		}
		super.onDestroy();
	}

	/**
	 * update unread message count
	 */
	public void updateUnreadLabel()
	{
		int count = getUnreadMsgCountTotal();
		if (count > 0)
		{
			mTabInfoList.get(MSG).getViewNotificationMark().setVisibility(View.VISIBLE);
			mTabInfoList.get(MSG).getViewNotificationMark().setText(String.valueOf(count));
			//			unreadLabel.setText(String.valueOf(count));
			//			unreadLabel.setVisibility(View.VISIBLE);
		}
		else
		{
			mTabInfoList.get(MSG).getViewNotificationMark().setVisibility(View.GONE);
			//			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * update the total unread count
	 */
	public void updateUnreadAddressLable()
	{
		runOnUiThread(new Runnable()
		{
			public void run()
			{
				int count = getUnreadAddressCountTotal();
				if (count > 0)
				{
					mTabInfoList.get(MSG).getViewNotificationMark().setVisibility(View.VISIBLE);
					mTabInfoList.get(MSG).getViewNotificationMark().setText(String.valueOf(count));
					//					unreadAddressLable.setVisibility(View.VISIBLE);
				}
				else
				{
					mTabInfoList.get(MSG).getViewNotificationMark().setVisibility(View.GONE);
					//					unreadAddressLable.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	/**
	 * get unread event notification count, including application, accepted, etc
	 *
	 * @return
	 */
	public int getUnreadAddressCountTotal()
	{
		int unreadAddressCountTotal = 0;
		try
		{
			unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return unreadAddressCountTotal;
	}

	/**
	 * get unread message count
	 *
	 * @return
	 */
	public int getUnreadMsgCountTotal()
	{
		int unreadMsgCountTotal = 0;
		int chatroomUnreadMsgCount = 0;
		unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
		for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values())
		{
			if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
				chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
		}
		return unreadMsgCountTotal - chatroomUnreadMsgCount;
	}

	@Override
	synchronized protected void viewInit()
	{
		super.viewInit();
		setSwipeBackEnable(false); //禁止滑动返回
		vHead = findViewById(R.id.header);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
		//		ActionBarDrawerToggle mDrawerToggle = new MyActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close, this);
		//		mDrawerToggle.syncState();
		//		mDrawerLayout.setDrawerListener(mDrawerToggle);
		//初始化Tab
		mTabInfoList.add(new TabInfo(INDEX, "首页", R.drawable.ic_tab_dialer_normal, FragmentMain.class));
		//		mTabInfoList.add(new TabInfo(CATEGORY, "分类", R.drawable.ic_tab_contacts_normal, FragmentCategory.class));
		mTabInfoList.add(new TabInfo(DIALER, "电话", R.drawable.ic_tab_money_normal, FragmentDialer2.class));
		mTabInfoList.add(new TabInfo(MSG, "消息", R.drawable.ic_tab_chat_normal, FragmentIM.class));
		//		mTabInfoList.add(new TabInfo(FIND, "发现", R.drawable.ic_tab_money_normal, FragmentFind.class));
		mTabInfoList.add(new TabInfo(USER, "我的", R.drawable.ic_tab_more_normal, FragmentMore.class));
		ViewGroup tab = (ViewGroup) findViewById(R.id.tab);
		//加载tab布局
		tab.addView(LayoutInflater.from(this).inflate(R.layout.layout_tab_icon_and_notification_mark, tab, false));
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
		viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider()
		{
			@Override
			public View createTabView(ViewGroup container, int position, PagerAdapter adapter)
			{
				TabInfo tabInfo = mTabInfoList.get(position);
				View view = LayoutInflater.from(viewPagerTab.getContext()).inflate(R.layout.item_tab_icon_and_notification_mark, container, false);
				tabInfo.setImgIcon((ImageView) view.findViewById(R.id.tab_icon));
				tabInfo.setTvLabel((TextView) view.findViewById(R.id.tab_label));
				tabInfo.getImgIcon().setImageResource(tabInfo.getImgResId());
				tabInfo.setViewNotificationMark((TextView) view.findViewById(R.id.custom_tab_notification_mark));
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
				int size = mTabInfoList.size();
				for (int i = 0; i < size; i++)
				{
					TabInfo tabInfo = mTabInfoList.get(i);
					if (i == position)
					{
						tabInfo.getTvLabel().setTextColor(getResources().getColor(R.color.tab_text_enabled));
						mToolbar.setTitle(tabInfo.getLabel());
					}
					else
					{
						tabInfo.getTvLabel().setTextColor(getResources().getColor(R.color.tab_text_disabled));
					}
				}
				if (position == INDEX || position == CATEGORY || position == DIALER || position == MSG || position == USER)
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
		viewPager.setOffscreenPageLimit(mTabInfoList.size());
		FragmentPagerItems pages = new FragmentPagerItems(this);
		for (TabInfo tabInfo : mTabInfoList)
		{
			pages.add(FragmentPagerItem.of(tabInfo.getLabel(), tabInfo.getLoadClass()));
		}
		FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);
		viewPager.setAdapter(adapter);
		viewPagerTab.setViewPager(viewPager);
		mTabInfoList.get(0).getTvLabel().setTextColor(getResources().getColor(R.color.tab_text_enabled));
		//侧滑布局
		xListView = (XListView) findViewById(R.id.xlist);
		layoutMainSide = new LayoutMainSide(this);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.addHeaderView(layoutMainSide);
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
			startActivity(new Intent(this, ActivityProductsSearch.class));
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
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == ConstValues.REQUEST_LOGIN_USER)
		{
			if (resultCode == ConstValues.RESULT_LOGIN_SUCCESS)
			{
				mDrawerLayout.openDrawer(Gravity.LEFT);
				layoutMainSide.refresh();
			}
			else
			{
				Toast.makeText(ActivityMain.this, "登录失败！", Toast.LENGTH_SHORT).show();
			}
		}
		else if (requestCode == REQ_SET_USER)
		{
			if (resultCode != RESULT_OK)
			{
				Toast.makeText(ActivityMain.this, "请设置用户信息！", Toast.LENGTH_SHORT).show();
				exit();
			}
		}
		else if (requestCode == ConstValues.REQUEST_EDIT_PIC)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				Bitmap bitmap = (Bitmap) intent.getExtras().get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
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
		String userId = PreferenceUtils.getInstance().getUserId();
		String userHeadPath = AppConfig.getUserPicCacheDir(userId);
		File file = new File(userHeadPath.substring(0, userHeadPath.lastIndexOf("/")));
		if (!file.exists())
		{
			file.mkdirs();
		}
		ByteArrayOutputStream b = BitmapTools.compressToByteArrayOutputStream(bitmap);
		if (b != null)
		{
			new UserService(this).updateUserHead(b.toByteArray(), "正在上传图片...", true);
		}
		else
		{
			Toast.makeText(this, "上传图片失败：数据为空！", Toast.LENGTH_LONG).show();
		}
		FileOutputStream f = BitmapTools.compressToFileOutputStream(bitmap, userHeadPath);
		if (f == null)
		{
			Toast.makeText(this, "保存图片到手机失败！", Toast.LENGTH_LONG).show();
		}
		mDrawerLayout.openDrawer(Gravity.LEFT);
		layoutMainSide.refresh();
		layoutMainSide.setHeader(bitmap);
	}

	/**
	 * 设置当前到第几页
	 *
	 * @param index
	 */
	public void setCurrentPage(int index)
	{
		if (index > -1 && index < mTabInfoList.size())
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
		layoutMainSide.refresh();
	}

	@Override
	public void onDrawerClosed(View drawerView)
	{
		//		int position = bannerViewPager.getCurrentItem();
		//		if (position == FIND || position == DIALER || position == USER)
		//		{
		//			vHead.setVisibility(View.VISIBLE);
		//		}
	}

	@Override
	public void onRefresh()
	{
		layoutMainSide.refresh();
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
		try
		{
			startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}
		catch (Exception e)
		{
			exit();
		}
	}

	public void exit()
	{
		finish();
		System.exit(0);
	}

	@Subscribe
	public void onEventMainThread(final EventTypeLayoutSideMain msg)
	{
		runOnUiThread(new Thread()
		{
			@Override
			public void run()
			{
				super.run();
				if (msg != null)
				{
					if (msg.getType().equalsIgnoreCase(EventTypeLayoutSideMain.CLOSE_DRAWER.getType()))
					{
						closeDrawerLayout();
					}
				}
			}
		});
	}

	@Subscribe
	public void onEventMainThread(final UserEvent userEvent)
	{
		runOnUiThread(new Thread()
		{
			@Override
			public void run()
			{
				super.run();
				if (menu != null)
				{
					menu.findItem(R.id.action_logout).setVisible(UserService.isLogin());
				}
			}
		});
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
		else if (url.endsWith(ApiConfig.GET_USER_NEED_UPDATE))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (returnInfo != null && returnInfo.getMsg().equalsIgnoreCase("true"))
			{
				startActivityForResult(new Intent(this, ActivityUserEdit.class), REQ_SET_USER);
			}
		}
		return false;
	}

	private int getExceptionMessageId(String exceptionType)
	{
		if (exceptionType.equals(Constant.ACCOUNT_CONFLICT))
		{
			return R.string.connect_conflict;
		}
		else if (exceptionType.equals(Constant.ACCOUNT_REMOVED))
		{
			return R.string.em_user_remove;
		}
		else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN))
		{
			return R.string.user_forbidden;
		}
		return R.string.Network_error;
	}

	/**
	 * show the dialog when user met some exception: such as login on another device, user removed or user forbidden
	 */
	private void showExceptionDialog(String exceptionType)
	{
		isExceptionDialogShow = true;
		DemoHelper.getInstance().logout(false, null);
		String st = getResources().getString(R.string.Logoff_notification);
		if (!this.isFinishing())
		{
			// clear up global variables
			try
			{
				if (exceptionBuilder == null)
					exceptionBuilder = new android.app.AlertDialog.Builder(this);
				exceptionBuilder.setTitle(st);
				exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
				exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						exceptionBuilder = null;
						isExceptionDialogShow = false;
						finish();
						Intent intent = new Intent(ActivityMain.this, ActivityLogin.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				});
				exceptionBuilder.setCancelable(false);
				exceptionBuilder.create().show();
				isConflict = true;
			}
			catch (Exception e)
			{
				EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
			}
		}
	}

	private void showExceptionDialogFromIntent(Intent intent)
	{
		EMLog.e(TAG, "showExceptionDialogFromIntent");
		if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false))
		{
			showExceptionDialog(Constant.ACCOUNT_CONFLICT);
		}
		else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false))
		{
			showExceptionDialog(Constant.ACCOUNT_REMOVED);
		}
		else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false))
		{
			showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
		}
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		showExceptionDialogFromIntent(intent);
	}
}
