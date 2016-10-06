/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.calllogs.CallLogsFactory;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.models.CallLogInfo;
import com.lrcall.models.ContactInfo;
import com.lrcall.ui.adapter.ContactsAndCallLogsAdapter;
import com.lrcall.ui.customer.DisplayTools;
import com.lrcall.utils.CallTools;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.StringTools;

import java.util.List;

public class ActivitySearchContacts extends MyBaseActivity implements View.OnClickListener, ContactsAndCallLogsAdapter.IContactsAdapterItemClick, ContactsAndCallLogsAdapter.ICallLogsAdapterItemClicked
{
	private static final String TAG = ActivitySearchContacts.class.getSimpleName();
	private EditText etSearch;
	private ListView lvContactNumbers;
	private ContactsAndCallLogsAdapter contactsAndCallLogsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_contacts);
		viewInit();
		//设置滑动返回区域
		getSwipeBackLayout().setEdgeSize(DisplayTools.getWindowWidth(this) / 4);
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		lvContactNumbers = (ListView) findViewById(R.id.list_contact_numbers);
		etSearch = (EditText) findViewById(R.id.et_search);
		etSearch.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void afterTextChanged(Editable s)
			{
				String condition = etSearch.getText().toString();
				if (!StringTools.isNull(condition))
				{
					new SearchContactsTask(condition).execute();
				}
				else
				{
					lvContactNumbers.setAdapter(null);
				}
			}
		});
		findViewById(R.id.search_del).setOnClickListener(this);
		//		lvContactNumbers.setOnTouchListener(new View.OnTouchListener()
		//		{
		//			@Override
		//			public boolean onTouch(View v, MotionEvent event)
		//			{
		//				hideSoftPad();
		//				return false;
		//			}
		//		});
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.search_del:
			{
				String number = etSearch.getText().toString();
				if (!StringTools.isNull(number))
				{
					etSearch.setTextKeepState(number.substring(0, number.length() - 1));
				}
				break;
			}
		}
	}

	@Override
	public void onItemClicked(CallLogInfo callLogInfo)
	{
		if (callLogInfo != null)
		{
			Toast.makeText(this, "点击通话记录" + callLogInfo.getName(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onCallClicked(CallLogInfo callLogInfo)
	{
		if (callLogInfo != null)
		{
			ReturnInfo returnInfo = CallTools.makeCall(this, callLogInfo.getNumber());
			if (!ReturnInfo.isSuccess(returnInfo))
			{
				Toast.makeText(this, returnInfo.getErrmsg(), Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onItemClicked(ContactInfo contactInfo)
	{
		if (contactInfo != null)
		{
			Intent intent = new Intent(this, ActivityContactDetail.class);
			intent.putExtra(ConstValues.DATA_CONTACT_ID, contactInfo.getContactId());
			startActivity(intent);
		}
	}

	// 隐藏软键盘
	private void hideSoftPad()
	{
		etSearch.clearFocus();
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
	}

	/**
	 * 搜索联系人任务
	 */
	class SearchContactsTask extends AsyncTask<Void, Void, List<ContactInfo>>
	{
		private final String condition;

		public SearchContactsTask(String condition)
		{
			this.condition = condition;
		}

		// Decode image in background.
		@Override
		protected List<ContactInfo> doInBackground(Void... params)
		{
			List<ContactInfo> contactInfoList = ContactsFactory.getInstance().getContactInfos(ActivitySearchContacts.this, condition);
			Cursor cursor = CallLogsFactory.getInstance().getCallLogs(ActivitySearchContacts.this, CallLogsFactory.CACHED_NAME + " LIKE '%" + condition + "%' OR " + CallLogsFactory.NUMBER + " LIKE '%" + condition + "%'");
			List<CallLogInfo> callLogInfoList = CallLogsFactory.getInstance().createListSort(cursor);
			if (cursor != null && !cursor.isClosed())
			{
				cursor.close();
			}
			contactsAndCallLogsAdapter = new ContactsAndCallLogsAdapter(ActivitySearchContacts.this, contactInfoList, callLogInfoList, ActivitySearchContacts.this, ActivitySearchContacts.this);
			return contactInfoList;
		}

		@Override
		protected void onPostExecute(List<ContactInfo> contactInfoList)
		{
			super.onPostExecute(contactInfoList);
			lvContactNumbers.setAdapter(contactsAndCallLogsAdapter);
		}
	}
}
