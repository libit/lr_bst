/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.calllogs.CallLogsFactory;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.db.DbBlackNumberInfoFactory;
import com.lrcall.db.DbWhiteNumberInfoFactory;
import com.lrcall.events.ContactEvent;
import com.lrcall.models.CallLogInfo;
import com.lrcall.models.ContactInfo;
import com.lrcall.ui.adapter.CallLogsAdapter;
import com.lrcall.ui.adapter.ContactCallLogsAdapter;
import com.lrcall.ui.adapter.ContactNumbersAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.customer.ViewHeightCalTools;
import com.lrcall.ui.dialog.DialogCommon;
import com.lrcall.utils.CallTools;
import com.lrcall.utils.ConstValues;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ActivityContactDetail extends MyBaseActivity
{
	private static final String TAG = FragmentDialer.class.getSimpleName();
	private static final int REQ_EDIT = 11;
	private TextView tvName;
	private ImageView ivHead;
	private ListView lvContactNumbers, lvContactCalllogs;
	private Long contactId;
	private ContactNumbersAdapter mContactNumbersAdapter;
	private ContactCallLogsAdapter mContactCallLogsAdapter;
	private List<CallLogInfo> mCallLogInfoList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_detail);
		contactId = getIntent().getLongExtra(ConstValues.DATA_CONTACT_ID, -1);
		viewInit();
		initData();
	}

	@Override
	protected void viewInit()
	{
		super.viewInit();
		setBackButton();
		tvName = (TextView) findViewById(R.id.tv_name);
		ivHead = (ImageView) findViewById(R.id.iv_head);
		lvContactNumbers = (ListView) findViewById(R.id.list_contact_numbers);
		lvContactCalllogs = (ListView) findViewById(R.id.list_contact_calllogs);
	}

	private void initData()
	{
		ContactInfo contactInfo = ContactsFactory.getInstance().getContactInfoById(this, contactId, true);
		if (contactInfo != null)
		{
			tvName.setText(contactInfo.getName());
			ivHead.setImageBitmap(contactInfo.getContactPhoto());
			if (mContactNumbersAdapter == null)
			{
				mContactNumbersAdapter = new ContactNumbersAdapter(ActivityContactDetail.this, contactInfo.getPhoneInfoList(), new ContactNumbersAdapter.IItemClick()
				{
					@Override
					public void onItemClicked(ContactInfo.PhoneInfo phoneInfo)
					{
					}

					@Override
					public void onCallClicked(ContactInfo.PhoneInfo phoneInfo)
					{
						if (phoneInfo != null)
						{
							ReturnInfo returnInfo = CallTools.makeCall(ActivityContactDetail.this, phoneInfo.getNumber());
							if (!ReturnInfo.isSuccess(returnInfo))
							{
								Toast.makeText(ActivityContactDetail.this, returnInfo.getErrmsg(), Toast.LENGTH_LONG).show();
							}
						}
					}
				});
				lvContactNumbers.setAdapter(mContactNumbersAdapter);
			}
			else
			{
				mContactNumbersAdapter.notifyDataSetChanged();
			}
			ViewHeightCalTools.setListViewHeight(lvContactNumbers, true);
			//			if (mContactCallLogsAdapter == null)
			{
				mCallLogInfoList = CallLogsFactory.getInstance().getCallLogsByContactId(ActivityContactDetail.this, contactInfo.getContactId());
				if (mCallLogInfoList == null || mCallLogInfoList.size() < 1)
				{
					findViewById(R.id.tv_no_contact_calllogs).setVisibility(View.VISIBLE);
					lvContactCalllogs.setVisibility(View.GONE);
				}
				else
				{
					findViewById(R.id.tv_no_contact_calllogs).setVisibility(View.GONE);
					lvContactCalllogs.setVisibility(View.VISIBLE);
					mContactCallLogsAdapter = new ContactCallLogsAdapter(ActivityContactDetail.this, mCallLogInfoList, new CallLogsAdapter.IItemClick()
					{
						@Override
						public void onItemClicked(CallLogInfo callLogInfo)
						{
						}

						@Override
						public void onCallClicked(CallLogInfo callLogInfo)
						{
							if (callLogInfo != null)
							{
								ReturnInfo returnInfo = CallTools.makeCall(ActivityContactDetail.this, callLogInfo.getNumber());
								if (!ReturnInfo.isSuccess(returnInfo))
								{
									Toast.makeText(ActivityContactDetail.this, returnInfo.getErrmsg(), Toast.LENGTH_LONG).show();
								}
							}
						}
					});
					lvContactCalllogs.setAdapter(mContactCallLogsAdapter);
					ViewHeightCalTools.setListViewHeight(lvContactCalllogs, true);
				}
			}
			//			else
			//			{
			//				mContactCallLogsAdapter.notifyDataSetChanged();
			//			}
		}
		else
		{
			tvName.setText("");
			lvContactNumbers.setAdapter(null);
			lvContactCalllogs.setAdapter(null);
			ViewHeightCalTools.setListViewHeight(lvContactNumbers, true);
			ViewHeightCalTools.setListViewHeight(lvContactCalllogs, true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_contact_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_contact_edit)
		{
			//			Intent intent = new Intent(this, ActivityContactEdit.class);
			//			intent.putExtra(ConstValues.DATA_CONTACT_ID, contactId);
			//			startActivityForResult(intent, REQ_EDIT);
			ContactsFactory.getInstance().toSystemEditContactActivity(this, contactId);
			return true;
		}
		else if (id == R.id.action_contact_delete)
		{
			new DialogCommon(this, new DialogCommon.LibitDialogListener()
			{
				@Override
				public void onOkClick()
				{
					boolean b = ContactsFactory.getInstance().deleteContact(ActivityContactDetail.this, contactId);
					if (b)
					{
						finish();
						ToastView.showCenterToast(ActivityContactDetail.this, R.drawable.ic_done, "删除联系人成功！");
						EventBus.getDefault().post(new ContactEvent(ContactEvent.EVENT_CONTACT_DELETE));
					}
					else
					{
						ToastView.showCenterToast(ActivityContactDetail.this, R.drawable.ic_do_fail, "删除联系人失败！");
					}
				}

				@Override
				public void onCancelClick()
				{
				}
			}, getString(R.string.title_warning), "确定要删除联系人吗？", true, false, true).show();
			return true;
		}
		else if (id == R.id.action_contact_add_white_list)
		{
			new DialogCommon(this, new DialogCommon.LibitDialogListener()
			{
				@Override
				public void onOkClick()
				{
					ContactInfo contactInfo = ContactsFactory.getInstance().getContactInfoById(ActivityContactDetail.this, contactId, false);
					if (contactInfo != null && contactInfo.getPhoneInfoList() != null && contactInfo.getPhoneInfoList().size() > 0)
					{
						int successCount = 0, failCount = 0;
						for (ContactInfo.PhoneInfo phoneInfo : contactInfo.getPhoneInfoList())
						{
							//先从黑名单移除号码
							DbBlackNumberInfoFactory.getInstance().deleteBlackNumberInfo(phoneInfo.getNumber());
							//加入白名单
							boolean b = DbWhiteNumberInfoFactory.getInstance().addOrUpdateWhiteNumberInfo(phoneInfo.getNumber(), contactInfo.getName(), "");
							if (b)
							{
								successCount++;
							}
							else
							{
								failCount++;
							}
						}
						if (failCount == 0)
						{
							ToastView.showCenterToast(ActivityContactDetail.this, R.drawable.ic_done, "添加联系人白名单成功！");
						}
						else
						{
							ToastView.showCenterToast(ActivityContactDetail.this, String.format("添加成功白名单%s个，失败%s个！", successCount, failCount));
						}
					}
					else
					{
						ToastView.showCenterToast(ActivityContactDetail.this, R.drawable.ic_do_fail, "添加联系人白名单失败，联系人不存在或联系人没有存储电话号码！");
					}
				}

				@Override
				public void onCancelClick()
				{
				}
			}, getString(R.string.title_warning), "确定要将联系人加入白名单吗？", true, false, true).show();
			return true;
		}
		else if (id == R.id.action_contact_add_black_list)
		{
			new DialogCommon(this, new DialogCommon.LibitDialogListener()
			{
				@Override
				public void onOkClick()
				{
					ContactInfo contactInfo = ContactsFactory.getInstance().getContactInfoById(ActivityContactDetail.this, contactId, false);
					if (contactInfo != null && contactInfo.getPhoneInfoList() != null && contactInfo.getPhoneInfoList().size() > 0)
					{
						int successCount = 0, failCount = 0;
						for (ContactInfo.PhoneInfo phoneInfo : contactInfo.getPhoneInfoList())
						{
							//先从白名单移除号码
							DbWhiteNumberInfoFactory.getInstance().deleteWhiteNumberInfo(phoneInfo.getNumber());
							//加入黑名单
							boolean b = DbBlackNumberInfoFactory.getInstance().addOrUpdateBlackNumberInfo(phoneInfo.getNumber(), contactInfo.getName(), "");
							if (b)
							{
								successCount++;
							}
							else
							{
								failCount++;
							}
						}
						if (failCount == 0)
						{
							ToastView.showCenterToast(ActivityContactDetail.this, R.drawable.ic_done, "添加联系人黑名单成功！");
						}
						else
						{
							ToastView.showCenterToast(ActivityContactDetail.this, String.format("添加成功黑名单%s个，失败%s个！", successCount, failCount));
						}
					}
					else
					{
						ToastView.showCenterToast(ActivityContactDetail.this, R.drawable.ic_do_fail, "添加联系人黑名单失败，联系人不存在或联系人没有存储电话号码！");
					}
				}

				@Override
				public void onCancelClick()
				{
				}
			}, getString(R.string.title_warning), "确定要将联系人加入黑名单吗？", true, false, true).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		if (REQ_EDIT == requestCode)
		{
			if (requestCode == RESULT_OK)
			{
				initData();
			}
		}
	}
}
