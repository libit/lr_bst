/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.reflect.TypeToken;
import com.lrcall.appbst.R;
import com.lrcall.appbst.models.ReturnInfo;
import com.lrcall.appbst.services.ApiConfig;
import com.lrcall.appbst.services.BackupService;
import com.lrcall.appbst.services.IAjaxDataResponse;
import com.lrcall.contacts.ContactsFactory;
import com.lrcall.events.ContactBackupEvent;
import com.lrcall.events.ContactEvent;
import com.lrcall.events.ContactRestoreEvent;
import com.lrcall.models.ContactInfo;
import com.lrcall.models.FuncInfo;
import com.lrcall.ui.adapter.FuncsHorizontalAdapter;
import com.lrcall.ui.customer.ToastView;
import com.lrcall.ui.dialog.DialogList;
import com.lrcall.ui.dialog.DialogProgress;
import com.lrcall.utils.GsonTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class FragmentBackupContact extends MyBaseFragment implements IAjaxDataResponse, View.OnClickListener
{
	private static final String TAG = FragmentBackupContact.class.getSimpleName();
	private static final int DOWNLOAD_MERGE = 1;
	private static final int DOWNLOAD_COVER = 2;
	private static final int BACKUP_DATA = 10001;
	private static final int RESTORE_FINISH = 10002;
	private TextView tvContactCount, tvContactCount2, tcContactBlackCount;
	private BackupService mBackupService;
	private final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
				case BACKUP_DATA:
				{
					String content = (String) msg.obj;
					mBackupService.updateContactBackupInfos(content, "正在备份联系人...", true);
					break;
				}
				case RESTORE_FINISH:
				{
					ToastView.showCenterToast(getContext(), R.drawable.ic_done, "恢复通讯录完成！");
					break;
				}
			}
		}
	};
	private List<FuncInfo> list = new ArrayList<>();
	private int downloadType = 0;
	private DialogProgress mDialogProgress;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mBackupService = new BackupService(this.getContext());
		mBackupService.addDataResponse(this);
		list.add(new FuncInfo(DOWNLOAD_MERGE, R.drawable.ic_done, "网络与手机合并"));
		list.add(new FuncInfo(DOWNLOAD_COVER, R.drawable.ic_done, "网络覆盖手机"));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_backup_contact, container, false);
		viewInit(rootView);
		EventBus.getDefault().register(this);
		List<ContactInfo> contactInfoList = ContactsFactory.getInstance().getContactInfos(getContext());
		tvContactCount.setText(String.format("通讯录：手机%d/网络", contactInfoList.size()));
		tvContactCount2.setText(String.format("%d", contactInfoList.size()));
		mBackupService.getContactBackupInfoCount(null, false);
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
		tvContactCount = (TextView) rootView.findViewById(R.id.tv_contact_count);
		tvContactCount2 = (TextView) rootView.findViewById(R.id.tv_contact_count2);
		tcContactBlackCount = (TextView) rootView.findViewById(R.id.tv_contact_black_count);
		rootView.findViewById(R.id.btn_download_contact).setOnClickListener(this);
		rootView.findViewById(R.id.btn_upload_contact).setOnClickListener(this);
		super.viewInit(rootView);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_download_contact:
			{
				final DialogList dialogList = new DialogList(getContext());
				FuncsHorizontalAdapter adapter = new FuncsHorizontalAdapter(getContext(), list, new FuncsHorizontalAdapter.IFuncsAdapterItemClicked()
				{
					@Override
					public void onFuncClicked(FuncInfo funcInfo)
					{
						dialogList.dismiss();
						downloadType = funcInfo.getId();
						mBackupService.getContactBackupInfos("正在下载联系人...", false);
					}
				});
				dialogList.setAdapter(adapter);
				dialogList.show();
				break;
			}
			case R.id.btn_upload_contact:
			{
				final List<ContactInfo> contactInfoList = ContactsFactory.getInstance().getContactInfos(getContext());
				if (contactInfoList == null || contactInfoList.size() < 1)
				{
					ToastView.showCenterToast(getContext(), R.drawable.ic_do_fail, "手机通讯录为空！");
					return;
				}
				final int total = contactInfoList.size();
				mDialogProgress = new DialogProgress(getContext(), new DialogProgress.LibitDialogProgressListener()
				{
					@Override
					public void onCancelClick()
					{
					}
				}, "备份手机通讯录", "正在备份第1个，共" + total + "个", total, false);
				mDialogProgress.show();
				new Thread("backupContacts")
				{
					@Override
					public void run()
					{
						super.run();
						List<ContactInfo> newContactInfoList = new ArrayList<>();
						int index = 0;
						for (ContactInfo contactInfo : contactInfoList)
						{
							index++;
							EventBus.getDefault().post(new ContactBackupEvent(index, total));
							ContactInfo newContactInfo = ContactsFactory.getInstance().getContactInfoById(getContext(), contactInfo.getContactId(), false);
							if (newContactInfo != null)
							{
								newContactInfoList.add(newContactInfo);
							}
						}
						if (mDialogProgress != null)
						{
							mDialogProgress.dismiss();
							mDialogProgress = null;
						}
						String content = GsonTools.toJson(newContactInfoList);
						//						EventBus.getDefault().post(new ContactBackupEvent(ContactBackupEvent.BACKUP_READY, content));
						Message msg = mHandler.obtainMessage();
						msg.what = BACKUP_DATA;
						msg.obj = content;
						mHandler.sendMessage(msg);
					}
				}.start();
				break;
			}
		}
	}

	/**
	 * ajax回调函数
	 *
	 * @param url
	 * @param result 返回结果
	 * @param status 状态
	 * @return
	 */
	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.USER_GET_CONTACTS_COUNT))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			List<ContactInfo> contactInfoList = ContactsFactory.getInstance().getContactInfos(getContext());
			String netCount = "";
			if (ReturnInfo.isSuccess(returnInfo))
			{
				netCount = returnInfo.getMsg();
			}
			tvContactCount.setText(String.format("通讯录：手机%d/网络%s", contactInfoList.size(), netCount));
			return true;
		}
		else if (url.endsWith(ApiConfig.USER_BACKUP_CONTACTS))
		{
			if (ReturnInfo.isSuccess(GsonTools.getReturnInfo(result)))
			{
				mBackupService.getContactBackupInfoCount(null, false);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.USER_GET_CONTACTS))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				final List<ContactInfo> contactInfoList = GsonTools.getReturnListObjects(result, new TypeToken<List<ContactInfo>>()
				{
				}.getType());
				if (contactInfoList == null || contactInfoList.size() < 1)
				{
					ToastView.showCenterToast(getContext(), R.drawable.ic_do_fail, "服务器通讯录为空！");
				}
				else
				{
					mDialogProgress = new DialogProgress(getContext(), new DialogProgress.LibitDialogProgressListener()
					{
						@Override
						public void onCancelClick()
						{
						}
					}, "恢复手机通讯录", "正在恢复第1个，共" + contactInfoList.size() + "个", contactInfoList.size(), false);
					mDialogProgress.show();
					//如果是覆盖则先清空手机通讯录
					if (downloadType == DOWNLOAD_COVER)
					{
						ContactsFactory.getInstance().deleteAllContact(getContext());
						downloadType = 0;
					}
					new Thread("restoreContacts")
					{
						@Override
						public void run()
						{
							super.run();
							int total = contactInfoList.size();
							int index = 0;
							for (ContactInfo contactInfo : contactInfoList)
							{
								index++;
								boolean b = ContactsFactory.getInstance().insertContact(getContext(), contactInfo);
								EventBus.getDefault().post(new ContactRestoreEvent(index, total));
							}
							if (mDialogProgress != null)
							{
								mDialogProgress.dismiss();
								mDialogProgress = null;
							}
							//通知联系人已更新
							EventBus.getDefault().post(new ContactEvent(ContactEvent.EVENT_CONTACT_UPDATE));
							mHandler.sendEmptyMessage(RESTORE_FINISH);
						}
					}.start();
				}
			}
			else
			{
				showServerMsg(result, null);
			}
			return true;
		}
		return false;
	}

	@Subscribe
	public void onEventMainThread(ContactRestoreEvent contactRestoreEvent)
	{
		if (contactRestoreEvent != null)
		{
			if (mDialogProgress != null && mDialogProgress.isShowing())
			{
				mDialogProgress.setDialogMessage(String.format("正在恢复第%d个，共%d个", contactRestoreEvent.getCurrent(), contactRestoreEvent.getTotal()));
				mDialogProgress.updateWaiting(contactRestoreEvent.getCurrent());
			}
		}
	}

	@Subscribe
	public void onEventMainThread(ContactBackupEvent contactBackupEvent)
	{
		if (contactBackupEvent != null)
		{
			if (mDialogProgress != null && mDialogProgress.isShowing())
			{
				mDialogProgress.setDialogMessage(String.format("正在备份第%d个，共%d个", contactBackupEvent.getCurrent(), contactBackupEvent.getTotal()));
				mDialogProgress.updateWaiting(contactBackupEvent.getCurrent());
			}
		}
	}
}
