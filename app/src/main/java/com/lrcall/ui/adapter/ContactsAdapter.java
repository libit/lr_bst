/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.models.ContactInfo;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by libit on 16/5/7.
 */
public class ContactsAdapter extends BaseContactsAdapter<ContactInfo>
{
	private final HashMap<String, Integer> alphaIndexer;
	private final String[] sections;
	private final String condition;

	public ContactsAdapter(Context context, List<ContactInfo> list, String condition)
	{
		super(context, list);
		this.condition = condition;
		this.alphaIndexer = new HashMap<>();
		for (int i = 0; i < list.size(); i++)
		{
			String name = getAlpha(list.get(i).getPy());
			if (!alphaIndexer.containsKey(name))
			{
				alphaIndexer.put(name, i);
			}
		}
		Set<String> sectionLetters = alphaIndexer.keySet();
		ArrayList<String> sectionList = new ArrayList<>(sectionLetters);
		Collections.sort(sectionList);
		sections = new String[sectionList.size()];
		sectionList.toArray(sections);
	}

	public HashMap<String, Integer> getAlphaIndexer()
	{
		return alphaIndexer;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		String currentStr = getAlpha(list.get(position).getPy());
		String previewStr = (position - 1) >= 0 ? getAlpha(list.get(position - 1).getPy()) : " ";
		ContactViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (ContactViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_phonebook_with_head, null);
			viewHolder = new ContactViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		if (previewStr.equals(currentStr))
		{
			viewHolder.tvAlpha.setVisibility(View.GONE);
		}
		else
		{
			viewHolder.tvAlpha.setVisibility(View.VISIBLE);
			viewHolder.tvAlpha.setText(currentStr);
		}
		ContactInfo contactInfo = list.get(position);
		Long id = contactInfo.getContactId();
		String name = contactInfo.getName();
		String number = "";
		if (contactInfo.getPhoneInfoList() != null && contactInfo.getPhoneInfoList().size() > 0)
		{
			number = contactInfo.getPhoneInfoList().get(0).getNumber();
		}
		viewHolder.contactId = contactInfo.getContactId();
		loadBitmap(id, viewHolder.ivHeader);
		if (!StringTools.isNull(number))
		{
			SpannableStringBuilder name_style = ContactsFactory.getInstance().getSpanNameString(name, condition);
			viewHolder.tvName.setText(name_style);
			SpannableStringBuilder number_style = ContactsFactory.getInstance().getSpanNumberString(number, condition);
			viewHolder.tvNumber.setText(number_style);
			viewHolder.tvNumber.setVisibility(View.VISIBLE);
			viewHolder.tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		}
		else
		{
			viewHolder.tvName.setText(name);
			viewHolder.tvNumber.setText(number);
			viewHolder.tvNumber.setVisibility(View.GONE);
			viewHolder.tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		}
		return convertView;
	}

	private String getAlpha(String str)
	{
		if (StringTools.isNull(str))
		{
			return "#";
		}
		CharSequence c = str.trim().charAt(0) + "";
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c).matches())
		{
			return c.toString().toUpperCase();
		}
		else
		{
			return "#";
		}
	}

	public static class ContactViewHolder
	{
		public Long contactId;
		public ImageView ivHeader;
		public TextView tvName;
		public TextView tvNumber;
		public TextView tvAlpha;

		public void viewInit(View convertView)
		{
			ivHeader = (ImageView) convertView.findViewById(R.id.contact_picture);
			tvName = (TextView) convertView.findViewById(R.id.contact_name);
			tvNumber = (TextView) convertView.findViewById(R.id.contact_number);
			tvAlpha = ((TextView) convertView.findViewById(R.id.alpha));
		}

		public void clear()
		{
			contactId = null;
			ivHeader.setImageBitmap(null);
			tvName.setText("");
			tvNumber.setText("");
			tvAlpha.setText("");
		}
	}
}



