/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.contacts;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.provider.ContactsContract;

import java.util.ArrayList;

public class ContactsUtils21 extends ContactsUtils14
{
	public static final String SORT_KEY = "phonebook_label";

	@Override
	public String getSortKey()
	{
		return SORT_KEY;
	}

	/**
	 * 删除联系人
	 *
	 * @param context
	 * @param contactId 联系人ID
	 * @return
	 */
	@Override
	public boolean deleteContact(Context context, Long contactId)
	{
		if (contactId == null)
		{
			return false;
		}
		ArrayList<ContentProviderOperation> ops = new ArrayList<>();
		//delete contact
		ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts.CONTACT_ID + "=" + contactId, null).build());
		//delete contact information such as phone tvRemark,email
		ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI).withSelection(ContactsContract.Data.CONTACT_ID + "=" + contactId, null).build());
		try
		{
			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}

	/**
	 * 删除所有联系人
	 *
	 * @return
	 */
	@Override
	public boolean deleteAllContact(Context context)
	{
		ArrayList<ContentProviderOperation> ops = new ArrayList<>();
		//delete contact
		ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).build());
		//delete contact information such as phone tvRemark,email
		ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI).build());
		try
		{
			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}
}
