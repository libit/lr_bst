/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.appbst.services.UserService;
import com.lrcall.calllogs.CallLogsFactory;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.events.CallLogEvent;
import com.lrcall.models.CallLogInfo;
import com.lrcall.models.ContactInfo;
import com.lrcall.models.FuncInfo;
import com.lrcall.ui.adapter.CallLogsAdapter;
import com.lrcall.ui.adapter.ContactsSearchAdapter;
import com.lrcall.ui.adapter.FuncsHorizontalAdapter;
import com.lrcall.ui.adapter.FuncsVerticalAdapter;
import com.lrcall.ui.customer.AddressAware;
import com.lrcall.ui.customer.AddressText;
import com.lrcall.ui.customer.EraseButton;
import com.lrcall.ui.dialog.DialogList;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.DisplayTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class FragmentDialer2 extends MyBaseFragment implements View.OnClickListener, AddressText.InputNumberChangedListener
{
	private static final String TAG = FragmentDialer2.class.getSimpleName();
	private static final int count = 200;
	private static FragmentDialer2 instance = null;
	private TextView tvTitle;
	private ListView xListView;
	private EraseButton erase;
	private AddressText mAddress;
	private View vPad, vDigitPad, layoutInput;//, layoutSwitchPad
	//	private DraftImageView vSwitchPad;
	private ContactsSearchAdapter mContactsSearchAdapter = null;
	private int start = 0;
	private final List<CallLogInfo> mCallLogInfoList = new ArrayList<>();
	private CallLogsAdapter mCallLogsAdapter = null;
	private Handler mHandler = new Handler();
	//设置号码
	//	public static void setAddressNumber(String number)
	//	{
	//		if (instance != null)
	//		{
	//			instance.mAddress.setText(number);
	//		}
	//	}

	public static FragmentDialer2 getInstance()
	{
		return instance;
	}

	public void setTitle(String title)
	{
		if (tvTitle != null)
		{
			tvTitle.setText(title);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//		LogcatTools.debug(TAG, "fragment onCreateView");
		View rootView = inflater.inflate(R.layout.fragment_dialer2, container, false);
		viewInit(rootView);
		setPadVisible(true);
		instance = this;
		EventBus.getDefault().register(this);
		IntentFilter filter = new IntentFilter("android.intent.action.PHONE_STATE");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		getActivity().registerReceiver(receiver, filter);
		onRefresh();
		return rootView;
	}

	@Override
	protected void viewInit(View rootView)
	{
		tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
		rootView.findViewById(R.id.btn_back).setVisibility(View.INVISIBLE);
		setTitle("电话");
		ImageButton btnMore = (ImageButton) rootView.findViewById(R.id.btn_more);
		btnMore.setImageResource(R.drawable.contact);
		btnMore.setOnClickListener(this);
		xListView = (ListView) rootView.findViewById(R.id.xlist);
		mAddress = (AddressText) rootView.findViewById(R.id.tv_number);
		erase = (EraseButton) rootView.findViewById(R.id.btn_delete);
		erase.setAddressWidget(mAddress);
		AddressAware numpad = (AddressAware) rootView.findViewById(R.id.number_pad);
		if (numpad != null)
		{
			numpad.setAddressWidget(mAddress);
		}
		vPad = rootView.findViewById(R.id.number_pad);
		vDigitPad = rootView.findViewById(R.id.layout_digit_pad);
		layoutInput = rootView.findViewById(R.id.layout_input);
		//		vSwitchPad = (DraftImageView) rootView.findViewById(R.id.btn_switch_pad);
		//		vSwitchPad.setOnTouchListener(DisplayTools.getWindowHeight(this.getContext()) - rootView.findViewById(R.id.layout_root).getHeight());
		//		vSwitchPad.setOnClickListener(this);
		//		layoutSwitchPad = rootView.findViewById(R.id.layout_switch);
		//		layoutSwitchPad.setOnClickListener(this);
		rootView.findViewById(R.id.btn_add_contact).setOnClickListener(this);
		xListView.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				setPadVisible(false);
				return false;
			}
		});
		registerForContextMenu(xListView);
		rootView.findViewById(R.id.layout_make_call).setOnClickListener(this);
		mAddress.setInputNumberChangedListener(this);
		ViewGroup.LayoutParams layoutParams = vDigitPad.getLayoutParams();
		layoutParams.height = DisplayTools.getWindowWidth(this.getContext()) * 19 / 33;
		vDigitPad.setLayoutParams(layoutParams);
		super.viewInit(rootView);
	}
	//	@Override
	//	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	//	{
	//		super.onViewCreated(view, savedInstanceState);
	//		//		LogcatTools.debug(TAG, "fragment onViewCreated,start=" + start);
	//		onRefresh();
	//	}

	@Override
	public void fragmentShow()
	{
		hideSoftPad();
		setPadVisible(true);
	}

	@Override
	public void onDestroyView()
	{
		synchronized (mCallLogInfoList)
		{
			mCallLogInfoList.clear();
			mCallLogsAdapter = null;
		}
		getActivity().unregisterReceiver(receiver);
		EventBus.getDefault().unregister(this);
		instance = null;
		super.onDestroyView();
	}

	private void getAllCallLog()
	{
		new SearchCallLogsTask().execute();
	}

	// 解决从外部传过来的号码 光标的位置置为最后
	public void setAddressCursorIndex()
	{
		String number = mAddress.getText().toString();
		if (!StringTools.isNull(number))
		{
			mAddress.setSelection(number.length());
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_more:
			{
				if (UserService.isLogin())
				{
					startActivity(new Intent(this.getContext(), ActivityCallbackInfo.class));
				}
				else
				{
					startActivityForResult(new Intent(this.getContext(), ActivityLogin.class), ConstValues.REQUEST_LOGIN_USER);
				}
				break;
			}
			case R.id.layout_make_call:
			{
				String number = mAddress.getText().toString();
				//				ReturnInfo returnInfo = CallTools.makeCall(this.getContext(), number);
				//				if (!ReturnInfo.isSuccess(returnInfo))
				//				{
				//					Toast.makeText(this.getContext(), returnInfo.getMsg(), Toast.LENGTH_LONG).show();
				//				}
				//				mAddress.setText("");
				makeCall(number);
				break;
			}
			case R.id.btn_switch_pad:
			{
				switchPad();
				break;
			}
			//			case R.id.layout_switch:
			//			{
			//				setPadVisible(false);
			//				break;
			//			}
			case R.id.btn_add_contact:
			{
				//				String number = mAddress.getText().toString();
				//				if (!StringTools.isNull(number))
				//				{
				//					ContactsFactory.getInstance().toSystemAddContactActivity(this.getContext(), number);
				//				}
				Intent intent = new Intent(this.getContext(), ActivityDial.class);
				intent.putExtra(ConstValues.DATA_SHOW_PAD, false);
				startActivity(intent);
			}
		}
	}

	//切换键盘显示
	public void switchPad()
	{
		if (vPad.getVisibility() == View.VISIBLE)
		{
			vPad.setVisibility(View.GONE);
		}
		else
		{
			vPad.setVisibility(View.VISIBLE);
		}
		onPadViewChanged();
	}

	//设置键盘显示状态
	private void setPadVisible(boolean show)
	{
		if (show)
		{
			vPad.setVisibility(View.VISIBLE);
		}
		else
		{
			vPad.setVisibility(View.GONE);
		}
		onPadViewChanged();
	}

	//键盘显示状态变化后的通知
	private void onPadViewChanged()
	{
		if (vPad.getVisibility() == View.VISIBLE)
		{
			//			vSwitchPad.setVisibility(View.GONE);
		}
		else
		{
			//			vSwitchPad.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onInputNumberChanged(String condition)
	{
		hideSoftPad();
		if (!StringTools.isNull(condition))
		{
			layoutInput.setVisibility(View.VISIBLE);
			new SearchContactsTask(condition).execute();
		}
		else
		{
			layoutInput.setVisibility(View.GONE);
			onRefresh();
		}
	}

	public void onRefresh()
	{
		start = 0;
		synchronized (mCallLogInfoList)
		{
			mCallLogInfoList.clear();
		}
		getAllCallLog();
	}

	public void onLoadMore()
	{
		start += count;
		getAllCallLog();
	}

	// 隐藏软键盘
	private void hideSoftPad()
	{
		mAddress.clearFocus();
		((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mAddress.getWindowToken(), 0);
	}

	@Subscribe
	public void onEventMainThread(final CallLogEvent callLogEvent)
	{
		mHandler.post(new Thread()
		{
			@Override
			public void run()
			{
				super.run();
				if (callLogEvent != null)
				{
					if (callLogEvent.getEvent().equalsIgnoreCase(CallLogEvent.EVENT_CALLLOG_ADD) || callLogEvent.getEvent().equalsIgnoreCase(CallLogEvent.EVENT_CALLLOG_UPDATE) || callLogEvent.getEvent().equalsIgnoreCase(CallLogEvent.EVENT_CALLLOG_DELETE))
					{
						onRefresh();
					}
				}
			}
		});
	}

	/**
	 * 搜索通话记录任务
	 */
	class SearchCallLogsTask extends AsyncTask<Long, Void, Boolean>
	{
		public SearchCallLogsTask()
		{
		}

		@Override
		protected Boolean doInBackground(Long... params)
		{
			boolean bAdd = false;
			synchronized (mCallLogInfoList)
			{
				Cursor cursor = CallLogsFactory.getInstance().getCallLogs(FragmentDialer2.this.getContext());
				List<CallLogInfo> list = CallLogsFactory.getInstance().createListSort(cursor, start, count);
				if (cursor != null && !cursor.isClosed())
				{
					cursor.close();
				}
				if (list != null && list.size() > 0)
				{
					bAdd = mCallLogInfoList.addAll(list);
				}
			}
			return bAdd;
		}

		@Override
		protected void onPostExecute(Boolean bAdd)
		{
			super.onPostExecute(bAdd);
			if (mCallLogsAdapter == null || start == 0)
			{
				mCallLogsAdapter = new CallLogsAdapter(FragmentDialer2.this.getContext(), mCallLogInfoList, new CallLogsAdapter.IItemClick()
				{
					@Override
					public void onItemClicked(final CallLogInfo callLogInfo)
					{
						setPadVisible(true);
						if (callLogInfo != null)
						{
							//							mAddress.setText(callLogInfo.getNumber());
							//							setAddressCursorIndex();
							final List<FuncInfo> list = new ArrayList<>();
							list.add(new FuncInfo(R.drawable.ic_done_grey600_18dp, "拨打此号码"));
							list.add(new FuncInfo(R.drawable.ic_done_grey600_18dp, "删除通话记录"));
							if (StringTools.isNull(callLogInfo.getName()) || callLogInfo.getName().contains(callLogInfo.getNumber()))
							{
								list.add(new FuncInfo(R.drawable.ic_done_grey600_18dp, "添加到联系人"));
							}
							final DialogList dialogList = new DialogList(FragmentDialer2.this.getContext());
							FuncsHorizontalAdapter adapter = new FuncsHorizontalAdapter(FragmentDialer2.this.getContext(), list, new FuncsVerticalAdapter.IFuncsAdapterItemClicked()
							{
								@Override
								public void onFuncClicked(FuncInfo funcInfo)
								{
									dialogList.dismiss();
									if (funcInfo.getLabel().equalsIgnoreCase(list.get(0).getLabel()))
									{
										makeCall(callLogInfo.getNumber());
									}
									else if (funcInfo.getLabel().equalsIgnoreCase(list.get(1).getLabel()))
									{
										CallLogsFactory.getInstance().deleteNumberCallLogs(FragmentDialer2.this.getContext(), callLogInfo.getNumber());
										EventBus.getDefault().post(new CallLogEvent(CallLogEvent.EVENT_CALLLOG_DELETE));
									}
									else if (funcInfo.getLabel().equalsIgnoreCase(list.get(2).getLabel()))
									{
										String number = callLogInfo.getNumber();
										if (!StringTools.isNull(number))
										{
											ContactsFactory.getInstance().toSystemAddContactActivity(FragmentDialer2.this.getContext(), number);
										}
									}
								}
							});
							dialogList.setAdapter(adapter);
							dialogList.show();
						}
					}

					@Override
					public void onCallClicked(CallLogInfo callLogInfo)
					{
						setPadVisible(true);
						if (callLogInfo != null)
						{
							//							ReturnInfo returnInfo = CallTools.makeCall(FragmentDialer.this.getContext(), callLogInfo.getNumber());
							//							if (!ReturnInfo.isSuccess(returnInfo))
							//							{
							//								Toast.makeText(FragmentDialer.this.getContext(), returnInfo.getMsg(), Toast.LENGTH_LONG).show();
							//							}
							//							mAddress.setText("");
							makeCall(callLogInfo.getNumber());
						}
					}
				});
				xListView.setAdapter(mCallLogsAdapter);
			}
			else
			{
				if (bAdd)
				{
					mCallLogsAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	/**
	 * 搜索联系人任务
	 */
	class SearchContactsTask extends AsyncTask<Long, Void, List<ContactInfo>>
	{
		private final String condition;

		public SearchContactsTask(String condition)
		{
			this.condition = condition;
		}

		// Decode image in background.
		@Override
		protected List<ContactInfo> doInBackground(Long... params)
		{
			return ContactsFactory.getInstance().getContactInfos(getContext(), condition);
		}

		@Override
		protected void onPostExecute(List<ContactInfo> contactInfoList)
		{
			super.onPostExecute(contactInfoList);
			//			if (contactInfoList.size() > 0)
			{
				mContactsSearchAdapter = new ContactsSearchAdapter(FragmentDialer2.this.getContext(), contactInfoList, new ContactsSearchAdapter.IItemClick()
				{
					@Override
					public void onItemClicked(ContactInfo contactInfo)
					{
						setPadVisible(true);
						if (contactInfo != null && contactInfo.getPhoneInfoList() != null && contactInfo.getPhoneInfoList().size() > 0)
						{
							mAddress.setText(contactInfo.getPhoneInfoList().get(0).getNumber());
							setAddressCursorIndex();
						}
					}
				});
				xListView.setAdapter(mContactsSearchAdapter);
			}
		}
	}

	private void makeCall(String number)
	{
		if (UserService.isLogin())
		{
			Intent intent = new Intent(this.getContext(), ActivityDialWaiting.class);
			intent.putExtra(ConstValues.DATA_NUMBER, number);
			startActivity(intent);
			mAddress.setText("");
		}
		else
		{
			startActivity(new Intent(this.getContext(), ActivityLogin.class));
		}
	}

	private BroadcastReceiver receiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			LogcatTools.debug("onReceive", "来电状态改变！");
			mHandler.post(new Thread()
			{
				@Override
				public void run()
				{
					super.run();
					try
					{
						Thread.sleep(5000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					EventBus.getDefault().post(new CallLogEvent(CallLogEvent.EVENT_CALLLOG_ADD));
				}
			});
		}
	};
}
