/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.text.SpannableStringBuilder;

import com.lrcall.models.ContactInfo;
import com.lrcall.utils.apptools.AppFactory;

import java.util.List;

public abstract class ContactsFactory
{
	public static final String CONTACT_ID = ContactsContract.Contacts._ID;
	public static final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
	public static final String SORT_KEY = ContactsContract.Contacts.SORT_KEY_PRIMARY;
	public static final String PHOTO_ID = ContactsContract.Contacts.PHOTO_ID;
	public static final String STARRED = ContactsContract.Contacts.STARRED;
	private static ContactsFactory instance;

	protected ContactsFactory()
	{
	}

	synchronized public static ContactsFactory getInstance()
	{
		if (instance == null)
		{
			if (AppFactory.isCompatible(21))
			{
				instance = new ContactsUtils21();
			}
			else if (AppFactory.isCompatible(14))
			{
				instance = new ContactsUtils14();
			}
			else
			{
				instance = new ContactsUtils8();
			}
		}
		return instance;
	}

	/**
	 * 获取排序的关键字，这里在4.4版本以下是“sort_key”，4.4或以上版本则变成了“phonebook_label”
	 *
	 * @return
	 */
	public String getSortKey()
	{
		return SORT_KEY;
	}

	/**
	 * 获取所有联系人
	 *
	 * @param context 应用Context
	 * @return 联系人数组
	 */
	public abstract List<ContactInfo> getContactInfos(Context context);

	/**
	 * 查询联系人列表
	 *
	 * @param context   应用Context
	 * @param condition 查询条件
	 * @return 联系人数组
	 */
	public abstract List<ContactInfo> getContactInfos(Context context, String condition);

	/**
	 * 根据ID获取联系人
	 *
	 * @param context   应用Context
	 * @param contactId 联系人ID
	 * @param showPhoto 是否显示图片
	 * @return 联系人信息
	 */
	public abstract ContactInfo getContactInfoById(Context context, Long contactId, boolean showPhoto);

	/**
	 * 根据姓名获取联系人
	 *
	 * @param context 应用Context
	 * @param name    姓名
	 * @return 联系人列表（考虑到用户可以存储多个同名的名片）
	 */
	public abstract List<ContactInfo> getContactInfosByName(Context context, String name, boolean showPhoto);

	/**
	 * 根据号码获取联系人
	 *
	 * @param context 应用Context
	 * @param number  号码
	 * @return 联系人列表（考虑到用户可以存储多个同号码的名片）
	 */
	public abstract List<ContactInfo> getContactInfosByNumber(Context context, String number, boolean showPhoto);

	/**
	 * 根据号码获取联系人，如果有多个，取第一个联系人
	 *
	 * @param context 应用Context
	 * @param number  号码
	 * @return 联系人
	 */
	public abstract ContactInfo getFirstContactInfoByNumber(Context context, String number, boolean showPhoto);

	/**
	 * 获取联系人图片
	 *
	 * @param context 应用Context
	 * @param photoId
	 * @return
	 */
	public abstract Bitmap getContactPhoto(Context context, Long photoId);

	/**
	 * 返回带颜色的姓名
	 *
	 * @param name
	 * @param condition
	 * @return
	 */
	public abstract SpannableStringBuilder getSpanNameString(String name, String condition);

	/**
	 * 返回带颜色的号码
	 *
	 * @param number
	 * @param condition
	 * @return
	 */
	public abstract SpannableStringBuilder getSpanNumberString(String number, String condition);

	/**
	 * 设置字符串高亮效果
	 *
	 * @param str   要高亮的字符串
	 * @param start 起始位置
	 * @param end   终点位置
	 * @return 高亮的字符串
	 */
	public abstract SpannableStringBuilder getSpanString(String str, int start, int end);

	/**
	 * 添加联系人，跳转到系统添加联系人界面
	 *
	 * @param context 应用Context
	 * @param number
	 */
	public abstract void toSystemAddContactActivity(Context context, String number);

	/**
	 * 编辑联系人，跳转到系统添加联系人界面
	 *
	 * @param context   应用Context
	 * @param contactId 联系人ID
	 */
	public abstract void toSystemEditContactActivity(Context context, Long contactId);

	/**
	 * 添加联系人
	 *
	 * @param contactInfo 联系人信息
	 * @return
	 */
	public abstract boolean insertContact(Context context, ContactInfo contactInfo);

	/**
	 * 删除联系人
	 *
	 * @param contactId 联系人ID
	 * @return
	 */
	public abstract boolean deleteContact(Context context, Long contactId);

	/**
	 * 删除所有联系人
	 *
	 * @return
	 */
	public abstract boolean deleteAllContact(Context context);
}
