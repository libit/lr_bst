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
	//	public static final String CONTACT_ID = ContactsContract.Contacts._ID;
	//	public static final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
	public static final String SORT_KEY = "phonebook_label";

	@Override
	public String getSortKey()
	{
		return SORT_KEY;
	}
	/**
	 * 获取所有联系人，不查询号码
	 *
	 * @param context 应用Context
	 * @return 联系人数组
	 */
	//	@Override
	//	public List<ContactInfo> getContactInfos(Context context)
	//	{
	//		String sort_key = getSortKey();
	//		Uri uri = ContactsContract.Contacts.CONTENT_URI;
	//		String[] projection = new String[]{CONTACT_ID, DISPLAY_NAME, sort_key};//PHOTO_ID, STARRED,
	//		String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'";
	//		String[] selectionArgs = null;
	//		String sortOrder = sort_key + " COLLATE LOCALIZED ASC";
	//		Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
	//		List<ContactInfo> list = new ArrayList<>();
	//		if (cursor != null)
	//		{
	//			while (cursor.moveToNext())
	//			{
	//				ContactInfo contactInfo = new ContactInfo();
	//				Long id = cursor.getLong(cursor.getColumnIndex(CONTACT_ID));
	//				String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
	//				String py = cursor.getString(cursor.getColumnIndex(sort_key));
	//				Long photoId = null;//cursor.getLong(cursor.getColumnIndex(PHOTO_ID));
	//				boolean starred = false;// cursor.getInt(cursor.getColumnIndex(STARRED)) == 1;
	//				if (photoId == null)
	//				{
	//					photoId = 0L;
	//				}
	//				contactInfo.setContactId(id);
	//				contactInfo.setName(name);
	//				contactInfo.setPy(py);
	//				contactInfo.setPhotoId(photoId);
	//				contactInfo.setStarred(starred);
	//				list.add(contactInfo);
	//			}
	//			if (!cursor.isClosed())
	//			{
	//				cursor.close();
	//			}
	//		}
	//		return list;
	//	}

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
