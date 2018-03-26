/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;

import com.external.xlistview.XListView;
import com.lrcall.appbst.R;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.events.ContactEvent;
import com.lrcall.models.ContactInfo;
import com.lrcall.ui.adapter.ContactsAdapter;
import com.lrcall.ui.customer.QuickAlphabeticBar;
import com.lrcall.utils.ConstValues;
import com.lrcall.utils.StringTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class FragmentContacts extends MyBaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, XListView.IXListViewListener
{
	private static final String TAG = FragmentContacts.class.getSimpleName();
	private ContactsAdapter adapter;
	private XListView xListView;
	private QuickAlphabeticBar alpha;
	private EditText etSearch;
	private View vList;
	private Handler mHandler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
		viewInit(rootView);
		EventBus.getDefault().register(this);
		return rootView;
	}

	@Override
	public void onDestroyView()
	{
		EventBus.getDefault().unregister(this);
		super.onDestroyView();
	}

	@Override
	protected void viewInit(View rootView)
	{
		vList = rootView.findViewById(R.id.v_list);
		xListView = (XListView) rootView.findViewById(R.id.list_contacts);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(this);
		alpha = (QuickAlphabeticBar) rootView.findViewById(R.id.fast_scroller);
		etSearch = (EditText) rootView.findViewById(R.id.edit_input_number);
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
				getContacts(condition);
			}
		});
		rootView.findViewById(R.id.search_del).setOnClickListener(this);
		getContacts(null);
		alpha.init(vList);
		super.viewInit(rootView);
	}

	private void getContacts(String condition)
	{
		new SearchContactsTask(condition).execute();
	}

	@Override
	public void fragmentHide()
	{
		super.fragmentHide();
		hideAlphaIndex();
	}

	@Override
	public void fragmentShow()
	{
		super.fragmentShow();
		hideSoftPad();
	}

	//隐藏导航字母
	public void hideAlphaIndex()
	{
		if (alpha != null)
		{
			alpha.setDialogTextVisibile(false);
		}
	}

	private void setAdapter(List<ContactInfo> list)
	{
		adapter = new ContactsAdapter(this.getContext(), list, etSearch.getText().toString());
		alpha.setAlphaIndexer(adapter.getAlphaIndexer());
		xListView.setAdapter(adapter);
		alpha.setListView(xListView);
		alpha.setVisibility(View.VISIBLE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		hideSoftPad();
		final ContactsAdapter.ContactViewHolder holder = (ContactsAdapter.ContactViewHolder) view.getTag();
		Long contactId = holder.contactId;
		Intent intent = new Intent(this.getContext(), ActivityContactDetail.class);
		intent.putExtra(ConstValues.DATA_CONTACT_ID, contactId);
		startActivity(intent);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
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
			case R.id.btn_add_contact:
			{
				ContactsFactory.getInstance().toSystemAddContactActivity(this.getContext(), "");
				break;
			}
		}
	}

	@Override
	public void onRefresh()
	{
		String condition = etSearch.getText().toString();
		getContacts(condition);
	}

	@Override
	public void onLoadMore()
	{
	}

	// 隐藏软键盘
	private void hideSoftPad()
	{
		etSearch.clearFocus();
		((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
	}

	@Subscribe
	public void onEventMainThread(final ContactEvent contactEvent)
	{
		mHandler.post(new Thread()
		{
			@Override
			public void run()
			{
				super.run();
				if (contactEvent != null)
				{
					if (contactEvent.getEventType().equalsIgnoreCase(ContactEvent.EVENT_CONTACT_ADD) || contactEvent.getEventType().equalsIgnoreCase(ContactEvent.EVENT_CONTACT_UPDATE) || contactEvent.getEventType().equalsIgnoreCase(ContactEvent.EVENT_CONTACT_DELETE))
					{
						onRefresh();
					}
				}
			}
		});
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
			List<ContactInfo> contactInfoList;
			if (StringTools.isNull(condition))
			{
				contactInfoList = ContactsFactory.getInstance().getContactInfos(getContext());
			}
			else
			{
				contactInfoList = ContactsFactory.getInstance().getContactInfos(getContext(), condition);
			}
			return contactInfoList;
		}

		@Override
		protected void onPostExecute(List<ContactInfo> contactInfoList)
		{
			super.onPostExecute(contactInfoList);
			xListView.stopRefresh();
			setAdapter(contactInfoList);
			try
			{
				etSearch.setHint(getString(R.string.contacts_num, contactInfoList.size()));
			}
			catch (Exception e)
			{
			}
		}
	}
}
