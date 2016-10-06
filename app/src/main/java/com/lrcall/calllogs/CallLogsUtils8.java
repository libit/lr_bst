/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.calllogs;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;

import com.lrcall.contacts.ContactsFactory;
import com.lrcall.models.CallLogInfo;
import com.lrcall.models.ContactInfo;
import com.lrcall.utils.CallTools;
import com.lrcall.utils.LogcatTools;
import com.lrcall.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class CallLogsUtils8 extends CallLogsFactory
{
	/**
	 * 获取所有通话记录的Cursor
	 *
	 * @param context
	 * @return
	 */
	@Override
	public Cursor getCallLogs(Context context)
	{
		return context.getContentResolver().query(CALLLOG_URL, logs_projection, null, null, DEFAULT_SORT_ORDER);
	}

	/**
	 * 根据条件查询通话记录
	 *
	 * @param context
	 * @param selection 查询条件
	 * @return
	 */
	@Override
	public Cursor getCallLogs(Context context, String selection)
	{
		return context.getContentResolver().query(CALLLOG_URL, logs_projection, selection, null, DEFAULT_SORT_ORDER);
	}

	/**
	 * 获取指定类型的通话记录
	 *
	 * @param context
	 * @param type    通话类型
	 * @return
	 */
	@Override
	public Cursor getCallLogsByType(Context context, int type)
	{
		String selection = NUMBER + " NOT LIKE '-%' AND " + TYPE + "=" + type;
		return getCallLogs(context, selection);
	}

	/**
	 * 根据联系人查询通话记录
	 *
	 * @param context
	 * @param contactId 联系人ID
	 * @return
	 */
	@Override
	public List<CallLogInfo> getCallLogsByContactId(Context context, Long contactId)
	{
		ContactInfo contactInfo = ContactsFactory.getInstance().getContactInfoById(context, contactId, false);
		String selection = "";
		if (contactInfo != null && contactInfo.getPhoneInfoList() != null)
		{
			for (ContactInfo.PhoneInfo phoneInfo : contactInfo.getPhoneInfoList())
			{
				if (!StringTools.isNull(phoneInfo.getNumber()))
				{
					String[] nums = CallTools.parseSqlNumber(phoneInfo.getNumber());
					if (nums != null && nums.length > 0)
					{
						for (int i = 0; i < nums.length; i++)
						{
							selection += " (" + NUMBER + " like '%" + nums[i] + "%') or ";
						}
					}
				}
			}
		}
		selection += " 1 != 1";
		Cursor cursor = context.getContentResolver().query(CALLLOG_URL, logs_projection, selection, null, DEFAULT_SORT_ORDER);
		List<CallLogInfo> list = createList(cursor);
		if (cursor != null && !cursor.isClosed())
		{
			cursor.close();
		}
		return list;
	}

	/**
	 * 根据号码查询通话记录
	 *
	 * @param context
	 * @param number  查询号码
	 * @return
	 */
	@Override
	public List<CallLogInfo> getCallLogsByNumber(Context context, String number)
	{
		String[] nums = CallTools.parseSqlNumber(number);
		String where = "";
		if (nums != null && nums.length > 0)
		{
			for (int i = 0; i < nums.length; i++)
			{
				if (i < nums.length - 1)
				{
					where += " (" + NUMBER + " like '%" + nums[i] + "%') or ";
				}
				else
				{
					where += " (" + NUMBER + " like '%" + nums[i] + "%') ";
				}
			}
		}
		Cursor cursor = context.getContentResolver().query(CALLLOG_URL, logs_projection, where, null, DEFAULT_SORT_ORDER);
		List<CallLogInfo> list = createList(cursor);
		if (cursor != null && !cursor.isClosed())
		{
			cursor.close();
		}
		return list;
	}

	/**
	 * 添加通话记录
	 *
	 * @param context
	 * @param callLogInfo 通话记录信息
	 */
	@Override
	public boolean addCallLogInfo(Context context, CallLogInfo callLogInfo)
	{
		ContentValues values = new ContentValues();
		values.put(CallLog.Calls.NUMBER, callLogInfo.getNumber());
		values.put(CallLog.Calls.DATE, callLogInfo.getDate());
		values.put(CallLog.Calls.DURATION, callLogInfo.getDuration());
		values.put(CallLog.Calls.TYPE, callLogInfo.getType());//未接
		values.put(CallLog.Calls.NEW, 0);//0已看1未看
		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED)
		{
			return false;
		}
		return context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values) != null;
	}

	/**
	 * 删除所有通话记录
	 *
	 * @param context
	 */
	@Override
	public int deleteAllCallLogs(Context context)
	{
		return context.getContentResolver().delete(CALLLOG_URL, null, null);
	}

	/**
	 * 删除通话记录
	 *
	 * @param context
	 * @param where
	 * @param selectionArgs
	 */
	public int deleteCallLog(Context context, String where, String[] selectionArgs)
	{
		return context.getContentResolver().delete(CALLLOG_URL, where, selectionArgs);
	}

	/**
	 * 删除联系人通话记录
	 *
	 * @param context
	 * @param contactId 联系人ID
	 */
	@Override
	public int deleteContactCallLogs(Context context, Long contactId)
	{
		ContactInfo contactInfo = ContactsFactory.getInstance().getContactInfoById(context, contactId, false);
		String selection = "";
		if (contactInfo != null && contactInfo.getPhoneInfoList() != null)
		{
			for (ContactInfo.PhoneInfo phoneInfo : contactInfo.getPhoneInfoList())
			{
				if (!StringTools.isNull(phoneInfo.getNumber()))
				{
					String[] nums = CallTools.parseSqlNumber(phoneInfo.getNumber());
					if (nums != null && nums.length > 0)
					{
						for (int i = 0; i < nums.length; i++)
						{
							selection += " (" + NUMBER + " like '%" + nums[i] + "%') or ";
						}
					}
				}
			}
		}
		selection += " 1 != 1";
		return context.getContentResolver().delete(CALLLOG_URL, selection, null);
	}

	/**
	 * 删除指定号码的通话记录
	 *
	 * @param context
	 * @param number  号码
	 */
	@Override
	public int deleteNumberCallLogs(Context context, String number)
	{
		String where = "";
		String[] nums = CallTools.parseSqlNumber(number);
		if (nums != null && nums.length > 0)
		{
			for (int i = 0; i < nums.length; i++)
			{
				if (i < nums.length - 1)
				{
					where += " (" + NUMBER + " like '%" + nums[i] + "%') or ";
				}
				else
				{
					where += " (" + NUMBER + " like '%" + nums[i] + "%') ";
				}
			}
		}
		return context.getContentResolver().delete(CALLLOG_URL, where, null);
	}

	/**
	 * 创建排序的通话记录列表
	 *
	 * @param cursor
	 * @return
	 */
	@Override
	public List<CallLogInfo> createListSort(Cursor cursor)
	{
		List<CallLogInfo> callLogInfoList = new ArrayList<>();
		List<String> data = new ArrayList<>();
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				String number = cursor.getString(cursor.getColumnIndex(NUMBER)).trim();
				if (data.contains(number))
				{
					parseNumber(callLogInfoList, number);
					continue;
				}
				else
				{
					data.add(number);
				}
				callLogInfoList.add(buildCallLogInfo(cursor));
			}
		}
		return callLogInfoList;
	}

	/**
	 * 创建排序的通话记录列表
	 *
	 * @param cursor
	 * @param start
	 * @param callLogCount
	 * @return
	 */
	@Override
	public List<CallLogInfo> createListSort(Cursor cursor, int start, int callLogCount)
	{
		LogcatTools.debug("createListSort", "start:" + start + ",callLogCount:" + callLogCount);
		List<CallLogInfo> callLogInfoList = new ArrayList<>();
		List<String> data = new ArrayList<>();
		if (cursor != null)
		{
			int index = 0;
			int getCount = 0;
			while (cursor.moveToNext() && getCount < callLogCount)
			{
				index++;
				if (index < start)
				{
					continue;
				}
				String number = cursor.getString(cursor.getColumnIndex(NUMBER)).trim();
				if (data.contains(number))
				{
					parseNumber(callLogInfoList, number);
					//					LogcatTools.debug("createListSort", "index:" + index + ",number已处理");
					continue;
				}
				else
				{
					data.add(number);
					//					LogcatTools.debug("createListSort", "index:" + index + ",添加number:" + number);
				}
				callLogInfoList.add(buildCallLogInfo(cursor));
				getCount++;
			}
		}
		return callLogInfoList;
	}

	private List<CallLogInfo> createList(Cursor cursor)
	{
		List<CallLogInfo> callLogInfoList = new ArrayList<>();
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				callLogInfoList.add(buildCallLogInfo(cursor));
			}
		}
		return callLogInfoList;
	}

	private CallLogInfo buildCallLogInfo(Cursor cursor)
	{
		CallLogInfo callLogInfo = new CallLogInfo();
		callLogInfo.setId(cursor.getString(cursor.getColumnIndex(ID)));
		String cache_name = cursor.getString(cursor.getColumnIndex(CACHED_NAME));
		String number = cursor.getString(cursor.getColumnIndex(NUMBER));
		if (StringTools.isNull(cache_name))
		{
			cache_name = number;
		}
		callLogInfo.setName(cache_name);
		callLogInfo.setNumber(number);
		callLogInfo.setType(cursor.getInt(cursor.getColumnIndex(TYPE)));
		callLogInfo.setDate(cursor.getLong(cursor.getColumnIndex(DATE)));
		callLogInfo.setDuration(cursor.getLong(cursor.getColumnIndex(DURATION)));
		return callLogInfo;
	}

	private void parseNumber(List<CallLogInfo> callLogInfoList, String number)
	{
		for (CallLogInfo callLogInfo : callLogInfoList)
		{
			if (callLogInfo.getNumber().indexOf(number) > -1)
			{
				String cache_name = callLogInfo.getName();
				if ((cache_name.indexOf("(") > -1) && (cache_name.indexOf(")") > -1) && (cache_name.lastIndexOf("(")) < (cache_name.lastIndexOf(")")))
				{
					String strCount = cache_name.substring(cache_name.lastIndexOf("(") + 1, cache_name.lastIndexOf(")"));
					try
					{
						int count = Integer.parseInt(strCount);
						count++;
						cache_name = cache_name.substring(0, cache_name.lastIndexOf("(")) + "(" + count + ")";
						callLogInfo.setName(cache_name);
					}
					catch (Exception e)
					{
						callLogInfo.setName(cache_name + "(2)");
					}
				}
				else
				{
					callLogInfo.setName(cache_name + "(2)");
				}
				break;
			}
		}
	}
}
