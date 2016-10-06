/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.models.ContactInfo;
import com.lrcall.ui.customer.LocalTools;

import java.util.List;

/**
 * Created by libit on 16/4/30.
 */
public class ContactsSearchAdapter extends BaseContactsAdapter<ContactInfo>
{
	protected final IContactsSearchAdapterItemClick contactsAdapterItemClicked;

	public ContactsSearchAdapter(Context context, List<ContactInfo> list, IContactsSearchAdapterItemClick contactsAdapterItemClicked)
	{
		super(context, list);
		this.contactsAdapterItemClicked = contactsAdapterItemClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ContactViewHolder contactViewHolder = null;
		if (convertView != null)
		{
			contactViewHolder = (ContactViewHolder) convertView.getTag();
		}
		if (contactViewHolder == null)
		{
			contactViewHolder = new ContactViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_contact, null);
			contactViewHolder.ivHeader = (ImageView) convertView.findViewById(R.id.contact_picture);
			contactViewHolder.tvName = (TextView) convertView.findViewById(R.id.contact_name);
			contactViewHolder.tvNumber = (TextView) convertView.findViewById(R.id.contact_number);
			convertView.setTag(contactViewHolder);
		}
		else
		{
			contactViewHolder.clear();
		}
		final ContactInfo contactInfo = list.get(position);
		contactViewHolder.tvName.setText(contactInfo.getName());
		if (contactInfo != null && contactInfo.getPhoneInfoList() != null && contactInfo.getPhoneInfoList().size() > 0)
		{
			String number = contactInfo.getPhoneInfoList().get(0).getNumber();
			contactViewHolder.tvNumber.setText(number);
			LocalTools.setLocal(context, contactViewHolder.tvNumber, number, true);
		}
		loadBitmap(contactInfo.getContactId(), contactViewHolder.ivHeader);
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (contactsAdapterItemClicked != null)
				{
					contactsAdapterItemClicked.onItemClicked(contactInfo);
				}
			}
		});
		return convertView;
	}

	public interface IContactsSearchAdapterItemClick
	{
		void onItemClicked(ContactInfo contactInfo);
	}

	public static class ContactViewHolder
	{
		public ImageView ivHeader;
		public TextView tvName;
		public TextView tvNumber;

		public void clear()
		{
			ivHeader.setImageBitmap(null);
			tvName.setText("");
			tvNumber.setText("");
		}
	}
}
