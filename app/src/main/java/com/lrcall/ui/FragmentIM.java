/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chatuidemo.ui.AddContactActivity;
import com.hyphenate.chatuidemo.ui.ContactListFragment;
import com.hyphenate.chatuidemo.ui.ConversationListFragment;
import com.lrcall.appbst.R;
import com.lrcall.appbst.services.UserService;
import com.lrcall.events.MsgEvent;
import com.lrcall.events.UserEvent;
import com.lrcall.models.TabInfo;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.LogcatTools;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class FragmentIM extends MyBaseFragment implements View.OnClickListener
{
	private static final String TAG = FragmentIM.class.getSimpleName();
	private final List<TabInfo> mTabInfoList = new ArrayList<>();
	private View layoutLogined, layoutNotLogin;
	private ViewPager viewPager;
	private SmartTabLayout viewPagerTab;
	private TextView tvTitle;
	private ImageButton btnMore;
	private int currentIndex = 0;
	private Handler mHandler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_im, container, false);
		viewInit(rootView);
		EventBus.getDefault().register(this);
		if (UserService.isLogin())
		{
			logined();
			viewPager.setCurrentItem(0);
		}
		return rootView;
	}

	@Override
	public void onDestroyView()
	{
		EventBus.getDefault().unregister(this);
		super.onDestroyView();
	}

	private void setTitle(String title)
	{
		if (tvTitle != null)
		{
			tvTitle.setText(title);
		}
	}

	@Override
	protected void viewInit(View rootView)
	{
		tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
		rootView.findViewById(R.id.btn_back).setVisibility(View.INVISIBLE);
		layoutLogined = rootView.findViewById(R.id.layout_logined);
		layoutNotLogin = rootView.findViewById(R.id.layout_not_login);
		btnMore = (ImageButton) rootView.findViewById(R.id.btn_more);
		ViewGroup tab = (ViewGroup) rootView.findViewById(R.id.tab);
		//加载tab布局
		tab.addView(LayoutInflater.from(this.getContext()).inflate(R.layout.layout_tab_text, tab, false));
		viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
		viewPagerTab = (SmartTabLayout) rootView.findViewById(R.id.viewpagertab);
		viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider()
		{
			@Override
			public View createTabView(ViewGroup container, int position, PagerAdapter adapter)
			{
				TabInfo tabInfo = mTabInfoList.get(position);
				View view = LayoutInflater.from(viewPagerTab.getContext()).inflate(R.layout.item_tab_text, container, false);
				TextView textView = (TextView) view.findViewById(R.id.tab_label);
				textView.setText(tabInfo.getLabel());
				tabInfo.setTvLabel(textView);
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
				currentIndex = position;
				int size = mTabInfoList.size();
				for (int i = 0; i < size; i++)
				{
					TabInfo tabInfo = mTabInfoList.get(i);
					if (i == position)
					{
						tabInfo.getTvLabel().setTextColor(getResources().getColor(R.color.tab_text_enabled));
						setTitle(tabInfo.getLabel());
					}
					else
					{
						tabInfo.getTvLabel().setTextColor(getResources().getColor(R.color.tab_text_disabled));
					}
				}
				if (position == 0)
				{
					btnMore.setImageResource(R.drawable.ic_refresh);
				}
				else if (position == 1)
				{
					btnMore.setImageResource(R.drawable.em_add);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
			}
		});
		//初始化Tab
		mTabInfoList.clear();
		mTabInfoList.add(new TabInfo(0, "会话", ConversationListFragment.class));
		mTabInfoList.add(new TabInfo(1, "联系人", ContactListFragment.class));
		FragmentPagerItems pages = new FragmentPagerItems(this.getContext());
		for (TabInfo tabInfo : mTabInfoList)
		{
			pages.add(FragmentPagerItem.of(tabInfo.getLabel(), tabInfo.getLoadClass()));
		}
		FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), pages);
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(mTabInfoList.size());
		viewPagerTab.setViewPager(viewPager);
		mTabInfoList.get(0).getTvLabel().setTextColor(getResources().getColor(R.color.tab_text_enabled));
		setTitle(mTabInfoList.get(0).getLabel());
		viewPager.setCurrentItem(0);
		btnMore.setImageResource(R.drawable.ic_refresh);
		btnMore.setOnClickListener(this);
		rootView.findViewById(R.id.btn_login).setOnClickListener(this);
		super.viewInit(rootView);
	}

	@Subscribe
	public void onEventMainThread(final UserEvent userEvent)
	{
		LogcatTools.debug(TAG, "userEvent收到消息了");
		mHandler.post(new Thread()
		{
			@Override
			public void run()
			{
				super.run();
				if (userEvent != null)
				{
					if (userEvent.getEvent().equals(UserEvent.EVENT_LOGINED))
					{
						logined();
					}
					else if (userEvent.getEvent().equals(UserEvent.EVENT_LOGOUT))
					{
						logout();
					}
				}
			}
		});
	}

	private void logined()
	{
		layoutLogined.setVisibility(View.VISIBLE);
		layoutNotLogin.setVisibility(View.GONE);
		EventBus.getDefault().post(new MsgEvent(MsgEvent.EVENT_CONVERSATION_REFRESH));
		EventBus.getDefault().post(new MsgEvent(MsgEvent.EVENT_CONTACT_REFRESH));
	}

	private void logout()
	{
		layoutLogined.setVisibility(View.GONE);
		layoutNotLogin.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_more:
			{
				if (currentIndex == 0)
				{
					EventBus.getDefault().post(new MsgEvent(MsgEvent.EVENT_CONVERSATION_REFRESH));
				}
				else if (currentIndex == 1)
				{
					startActivity(new Intent(getActivity(), AddContactActivity.class));
				}
				break;
			}
			case R.id.btn_login:
			{
				startActivityForResult(new Intent(this.getContext(), ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
				break;
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ConstValues.REQUEST_LOGIN_USER)
		{
			if (resultCode == ConstValues.RESULT_LOGIN_SUCCESS)
			{
				logined();
			}
			else
			{
				Toast.makeText(this.getContext(), "登录失败！", Toast.LENGTH_SHORT).show();
				logout();
			}
		}
	}
}
