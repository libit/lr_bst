/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.lrcall.appbst.R;
import com.lrcall.models.ContactInfo;
import com.lrcall.ui.customer.BitmapCacheTools;
import com.lrcall.utils.CallTools;
import com.lrcall.utils.PinyinTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class ContactsUtils8 extends ContactsFactory
{
	private static final Uri CONTENT_URI8 = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	private static final String CONTACT_ID8 = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
	private static final String NAME8 = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
	private static final String NUMBER8 = ContactsContract.CommonDataKinds.Phone.NUMBER;
	private static final String TYPE8 = ContactsContract.CommonDataKinds.Phone.TYPE;
	private static final String PHOTO_ID8 = ContactsContract.CommonDataKinds.Phone.PHOTO_ID;
	private static final String STARRED8 = ContactsContract.CommonDataKinds.Phone.STARRED;
	private final String[] projection8 = new String[]{CONTACT_ID8, NAME8, NUMBER8, TYPE8, PHOTO_ID8, STARRED8, getSortKey()};

	private ContactInfo buildContactInfo(Context context, Cursor cursor, boolean showPhoto)
	{
		ContactInfo contactInfo = new ContactInfo();
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				Long contactId = cursor.getLong(cursor.getColumnIndex(CONTACT_ID8));
				String name = cursor.getString(cursor.getColumnIndex(NAME8));
				boolean starred = cursor.getInt(cursor.getColumnIndex(STARRED8)) == 1;
				Long photoId = cursor.getLong(cursor.getColumnIndex(PHOTO_ID8));
				contactInfo.setContactId(contactId);
				contactInfo.setName(name);
				contactInfo.setStarred(starred);
				contactInfo.setPhotoId(photoId);
				//				LogcatTools.debug("联系人图片ID", contactInfo.getName() + "->" + contactInfo.getPhotoId());
				if (showPhoto)
				{
					Bitmap bitmap = BitmapCacheTools.getMemoryCache().get("c" + contactInfo.getContactId());
					if (bitmap == null)
					{
						bitmap = getContactPhoto(context, contactInfo.getPhotoId());
						//						LogcatTools.debug("bitmap", "bitmap:" + (bitmap != null));
					}
					contactInfo.setContactPhoto(bitmap);
				}
				List<ContactInfo.PhoneInfo> phoneInfos = new ArrayList<>();
				do
				{
					String number = cursor.getString(cursor.getColumnIndex(NUMBER8));
					String type = cursor.getString(cursor.getColumnIndex(TYPE8));
					phoneInfos.add(new ContactInfo.PhoneInfo(number, type));
				}
				while (cursor.moveToNext());
				contactInfo.setPhoneInfoList(phoneInfos);
			}
		}
		return contactInfo;
	}

	private List<ContactInfo> buildContactInfos(Context context, Cursor cursor, boolean showPhoto)
	{
		List<ContactInfo> list = new ArrayList<>();
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				Long contactId = cursor.getLong(cursor.getColumnIndex(CONTACT_ID8));
				String number = cursor.getString(cursor.getColumnIndex(NUMBER8));
				String type = cursor.getString(cursor.getColumnIndex(TYPE8));
				// 如果和上个联系人ID一样则合并
				if (list.size() > 0 && list.get(list.size() - 1).getContactId() == contactId)
				{
					list.get(list.size() - 1).getPhoneInfoList().add(new ContactInfo.PhoneInfo(number, type));
				}
				else
				{
					String name = cursor.getString(cursor.getColumnIndex(NAME8));
					String py = cursor.getString(cursor.getColumnIndex(getSortKey()));
					boolean starred = cursor.getInt(cursor.getColumnIndex(STARRED8)) == 1;
					Long photoId = cursor.getLong(cursor.getColumnIndex(PHOTO_ID8));
					ContactInfo contactInfo = new ContactInfo();
					contactInfo.setContactId(contactId);
					contactInfo.setName(name);
					contactInfo.setPy(py);
					contactInfo.setStarred(starred);
					contactInfo.setPhotoId(photoId);
					if (showPhoto)
					{
						Bitmap bitmap = BitmapCacheTools.getMemoryCache().get("c" + contactInfo.getContactId());
						if (bitmap == null)
						{
							bitmap = getContactPhoto(context, contactInfo.getPhotoId());
							//							LogcatTools.debug("bitmap", "bitmap:" + (bitmap != null));
						}
						contactInfo.setContactPhoto(bitmap);
					}
					List<ContactInfo.PhoneInfo> phoneInfos = new ArrayList<>();
					phoneInfos.add(new ContactInfo.PhoneInfo(number, type));
					contactInfo.setPhoneInfoList(phoneInfos);
					list.add(contactInfo);
				}
			}
		}
		return list;
	}

	/**
	 * 获取所有联系人，不查询号码
	 *
	 * @param context 应用Context
	 * @return 联系人数组
	 */
	@Override
	public List<ContactInfo> getContactInfos(Context context)
	{
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[]{CONTACT_ID, DISPLAY_NAME, PHOTO_ID, STARRED, getSortKey()};
		String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'";
		String[] selectionArgs = null;
		String sortOrder = getSortKey() + " COLLATE LOCALIZED ASC";
		Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
		List<ContactInfo> list = new ArrayList<>();
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				ContactInfo contactInfo = new ContactInfo();
				Long id = cursor.getLong(cursor.getColumnIndex(CONTACT_ID));
				String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
				String py = cursor.getString(cursor.getColumnIndex(getSortKey()));
				Long photoId = cursor.getLong(cursor.getColumnIndex(PHOTO_ID));
				boolean starred = cursor.getInt(cursor.getColumnIndex(STARRED)) == 1;
				if (photoId == null)
				{
					photoId = 0L;
				}
				contactInfo.setContactId(id);
				contactInfo.setName(name);
				contactInfo.setPy(py);
				contactInfo.setPhotoId(photoId);
				contactInfo.setStarred(starred);
				//				LogcatTools.debug("getContactInfos", "name:" + name + ",py:" + py + ",sortOrder:" + sortOrder);
				list.add(contactInfo);
			}
			if (!cursor.isClosed())
			{
				cursor.close();
			}
		}
		return list;
	}

	/**
	 * 查询联系人列表
	 *
	 * @param context   应用Context
	 * @param condition 查询条件
	 * @return 联系人数组
	 */
	@Override
	public List<ContactInfo> getContactInfos(Context context, String condition)
	{
		try
		{
			Uri uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, condition);
			//		String selection = CommonDataKinds.Phone.HAS_PHONE_NUMBER + " = '1'";
			//		String selection = CommonDataKinds.Phone.NUMBER + " LIKE '%" + condition + "%' OR " + CommonDataKinds.Phone.DISPLAY_NAME + " LIKE '%" + condition + "%'";
			String sortOrder = getSortKey() + " COLLATE LOCALIZED ASC";
			Cursor cursor = context.getContentResolver().query(uri, projection8, null, null, sortOrder);
			List<ContactInfo> list = buildContactInfos(context, cursor, false);
			if (cursor != null && !cursor.isClosed())
			{
				cursor.close();
			}
			return list;
		}
		catch (Exception e)
		{
			Uri uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, condition);
			String sortOrder = getSortKey() + " COLLATE LOCALIZED ASC";
			Cursor cursor = context.getContentResolver().query(uri, new String[]{CONTACT_ID8, NAME8, NUMBER8, TYPE8, PHOTO_ID8, STARRED8, ContactsContract.Contacts.SORT_KEY_PRIMARY}, null, null, sortOrder);
			List<ContactInfo> list = buildContactInfos(context, cursor, false);
			if (cursor != null && !cursor.isClosed())
			{
				cursor.close();
			}
			return list;
		}
	}

	/**
	 * 根据ID获取联系人
	 *
	 * @param context   应用Context
	 * @param contactId 联系人ID
	 * @param showPhoto 是否显示图片
	 * @return 联系人信息
	 */
	@Override
	public ContactInfo getContactInfoById(Context context, Long contactId, boolean showPhoto)
	{
		if (contactId == null)
		{
			return null;
		}
		String sortOrder = getSortKey() + " COLLATE LOCALIZED ASC";
		Cursor cursor = context.getContentResolver().query(CONTENT_URI8, projection8, CONTACT_ID8 + " = ?", new String[]{contactId.toString()}, sortOrder);
		ContactInfo contactInfo = buildContactInfo(context, cursor, showPhoto);
		if (cursor != null && !cursor.isClosed())
		{
			cursor.close();
		}
		return contactInfo;
	}

	/**
	 * 根据姓名获取联系人
	 *
	 * @param context 应用Context
	 * @param name    姓名
	 * @return 联系人列表（考虑到用户可以存储多个同名的名片）
	 */
	@Override
	public List<ContactInfo> getContactInfosByName(Context context, String name, boolean showPhoto)
	{
		String sortOrder = getSortKey() + " COLLATE LOCALIZED ASC";
		Cursor cursor = context.getContentResolver().query(CONTENT_URI8, projection8, NAME8 + " = ?", new String[]{name}, sortOrder);
		List<ContactInfo> contactInfos = buildContactInfos(context, cursor, showPhoto);
		if (cursor != null && !cursor.isClosed())
		{
			cursor.close();
		}
		return contactInfos;
	}

	/**
	 * 根据号码获取联系人
	 *
	 * @param context 应用Context
	 * @param number  号码
	 * @return 联系人列表（考虑到用户可以存储多个同号码的名片）
	 */
	@Override
	public List<ContactInfo> getContactInfosByNumber(Context context, String number, boolean showPhoto)
	{
		if (StringTools.isNull(number))
		{
			return null;
		}
		number = CallTools.convertToCallPhoneNumber(number);
		//		String sortOrder = getSortKey() + " COLLATE LOCALIZED ASC";
		//		String[] nums = CallTools.parseSqlNumber(number);
		//		String where = "";
		//		if (nums != null && nums.length > 0)
		//		{
		//			for (int i = 0; i < nums.length; i++)
		//			{
		//				if (i < nums.length - 1)
		//				{
		//					where += " (" + NUMBER8 + " like '%" + nums[i] + "%') or ";
		//				}
		//				else
		//				{
		//					where += " (" + NUMBER8 + " like '%" + nums[i] + "%') ";
		//				}
		//			}
		//		}
		//		Log.d("getContactInfosByNumber", "where:" + where);
		//		Cursor cursor = context.getContentResolver().query(CONTENT_URI8, projection8, where, null, sortOrder);
		//		List<ContactInfo> contactInfos = buildContactInfos(context, cursor, showPhoto);
		//		if (cursor != null && !cursor.isClosed())
		//		{
		//			cursor.close();
		//		}
		Uri uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, number);
		String sortOrder = getSortKey() + " COLLATE LOCALIZED ASC";
		Cursor cursor = context.getContentResolver().query(uri, projection8, null, null, sortOrder);
		List<ContactInfo> list = buildContactInfos(context, cursor, showPhoto);
		if (cursor != null && !cursor.isClosed())
		{
			cursor.close();
		}
		return list;
	}

	/**
	 * 根据号码获取联系人，如果有多个，取第一个联系人
	 *
	 * @param context   应用Context
	 * @param number    号码
	 * @param showPhoto
	 * @return 联系人
	 */
	@Override
	public ContactInfo getFirstContactInfoByNumber(Context context, String number, boolean showPhoto)
	{
		List<ContactInfo> contactInfoList = getContactInfosByNumber(context, number, false);
		if (contactInfoList != null && contactInfoList.size() > 0)
		{
			ContactInfo contactInfo = contactInfoList.get(0);
			if (showPhoto)
			{
				Bitmap bitmap = BitmapCacheTools.getMemoryCache().get("c" + contactInfo.getContactId());
				if (bitmap == null)
				{
					bitmap = getContactPhoto(context, contactInfo.getPhotoId());
				}
				contactInfo.setContactPhoto(bitmap);
			}
			return contactInfo;
		}
		return null;
	}

	/**
	 * 获取联系人图片
	 *
	 * @param context 应用Context
	 * @param photoId
	 * @return
	 */
	@Override
	public Bitmap getContactPhoto(Context context, Long photoId)
	{
		Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, ContactsContract.CommonDataKinds.Photo.PHOTO_ID + " = ? " + " AND " + ContactsContract.Contacts.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", new String[]{String.valueOf(photoId)}, null);
		Bitmap img = null;
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				byte[] photo = cursor.getBlob(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO));
				if (photo != null)
				{
					img = BitmapFactory.decodeByteArray(photo, 0, photo.length);
				}
			}
			if (!cursor.isClosed())
			{
				cursor.close();
			}
		}
		if (img == null)
		{
			BitmapDrawable drawableBitmap = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.default_header));
			if (drawableBitmap != null)
			{
				img = drawableBitmap.getBitmap();
			}
		}
		return img;
	}

	/**
	 * 设置字符串高亮效果
	 *
	 * @param str   要高亮的字符串
	 * @param start 起始位置
	 * @param end   终点位置
	 * @return 高亮的字符串
	 */
	@Override
	public SpannableStringBuilder getSpanString(String str, int start, int end)
	{
		if (start < 0)
		{
			start = 0;
		}
		if (end < start)
		{
			end = start;
		}
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		style.setSpan(new ForegroundColorSpan(Color.rgb(0xff, 0x77, 0x00)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return style;
	}

	/**
	 * 添加联系人，跳转到系统添加联系人界面
	 *
	 * @param context 应用Context
	 * @param number
	 */
	@Override
	public void toSystemAddContactActivity(Context context, String number)
	{
		Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
		intent.putExtra("phone", number);
		context.startActivity(intent);
	}

	/**
	 * 编辑联系人，跳转到系统添加联系人界面
	 *
	 * @param context   应用Context
	 * @param contactId 联系人ID
	 */
	@Override
	public void toSystemEditContactActivity(Context context, Long contactId)
	{
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId));
		context.startActivity(intent);
	}

	/**
	 * 返回带颜色的姓名
	 *
	 * @param name
	 * @param condition
	 * @return
	 */
	@Override
	public SpannableStringBuilder getSpanNameString(String name, String condition)
	{
		return PinyinTools.getSpanNumberString5(name, condition);
	}

	/**
	 * 返回带颜色的号码
	 *
	 * @param number
	 * @param condition
	 * @return
	 */
	@Override
	public SpannableStringBuilder getSpanNumberString(String number, String condition)
	{
		return PinyinTools.getSpanNameString5(number, condition);
	}

	/**
	 * 添加联系人
	 *
	 * @param contactInfo 联系人信息
	 * @return
	 */
	@Override
	public boolean insertContact(Context context, ContactInfo contactInfo)
	{
		if (contactInfo == null)
		{
			return false;
		}
		try
		{
		/* 往 raw_contacts 中添加数据，并获取添加的id号*/
			Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
			ContentResolver resolver = context.getContentResolver();
			ContentValues values = new ContentValues();
			long contactId = ContentUris.parseId(resolver.insert(uri, values));

        /* 往 data 中添加数据（要根据前面获取的id号） */
			// 添加姓名
			uri = Uri.parse("content://com.android.contacts/data");
			values.put("raw_contact_id", contactId);
			values.put("mimetype", "vnd.android.cursor.item/name");
			values.put("data2", contactInfo.getName());
			Uri contactUri = resolver.insert(uri, values);
			if (contactUri == null)
			{
				return false;
			}
			// 添加电话
			if (contactInfo.getPhoneInfoList() != null)
			{
				int size = contactInfo.getPhoneInfoList().size();
				for (int i = 0; i < size; i++)
				{
					ContactInfo.PhoneInfo phoneInfo = contactInfo.getPhoneInfoList().get(i);
					values.clear();
					values.put("raw_contact_id", contactId);
					values.put("mimetype", "vnd.android.cursor.item/phone_v2");
					values.put("data2", phoneInfo.getType());
					values.put("data1", phoneInfo.getNumber());
					contactUri = resolver.insert(uri, values);
					if (contactUri == null)
					{
						return false;
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 测试失败
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
		int count = context.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.Contacts.NAME_RAW_CONTACT_ID + " = ?", new String[]{contactId.toString()});
		return count > 0;
	}

	/**
	 * 删除所有联系人
	 *
	 * @return
	 */
	@Override
	public boolean deleteAllContact(Context context)
	{
		int count = context.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, null, null);
		return count > 0;
	}
}
