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
import com.lrcall.calllogs.CallLogsFactory;
import com.lrcall.events.CallLogEvent;
import com.lrcall.events.CallLogRestoreEvent;
import com.lrcall.models.CallLogInfo;
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

public class FragmentBackupHistory extends MyBaseFragment implements IAjaxDataResponse, View.OnClickListener
{
	private static final String TAG = FragmentBackupHistory.class.getSimpleName();
	private static final int DOWNLOAD_MERGE = 1;
	private static final int DOWNLOAD_COVER = 2;
	private static final int RESTORE_FINISH = 10002;
	private final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
				case RESTORE_FINISH:
				{
					ToastView.showCenterToast(getContext(), R.drawable.ic_done,"恢复通话记录完成！");
					break;
				}
			}
		}
	};
	private TextView tvHistoryCount, tvHistoryCount2;
	private BackupService mBackupService;
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
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_backup_history, container, false);
		viewInit(rootView);
		EventBus.getDefault().register(this);
		List<CallLogInfo> callLogInfoList = CallLogsFactory.getInstance().createListSort(CallLogsFactory.getInstance().getCallLogs(getContext()));
		tvHistoryCount.setText(String.format("通话记录：手机%d/网络", callLogInfoList.size()));
		tvHistoryCount2.setText(String.format("%d", callLogInfoList.size()));
		mBackupService.getHistoryBackupInfoCount(null, false);
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
		tvHistoryCount = (TextView) rootView.findViewById(R.id.tv_history_count);
		tvHistoryCount2 = (TextView) rootView.findViewById(R.id.tv_history_count2);
		rootView.findViewById(R.id.btn_download_history).setOnClickListener(this);
		rootView.findViewById(R.id.btn_upload_history).setOnClickListener(this);
		super.viewInit(rootView);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_download_history:
			{
				final DialogList dialogList = new DialogList(getContext());
				FuncsHorizontalAdapter adapter = new FuncsHorizontalAdapter(getContext(), list, new FuncsHorizontalAdapter.IFuncsAdapterItemClicked()
				{
					@Override
					public void onFuncClicked(FuncInfo funcInfo)
					{
						dialogList.dismiss();
						downloadType = funcInfo.getId();
						mBackupService.getHistoryBackupInfos("正在下载通话记录...", false);
					}
				});
				dialogList.setAdapter(adapter);
				dialogList.show();
				break;
			}
			case R.id.btn_upload_history:
			{
				final List<CallLogInfo> callLogInfoList = CallLogsFactory.getInstance().createListSort(CallLogsFactory.getInstance().getCallLogs(getContext()));
				if (callLogInfoList == null || callLogInfoList.size() < 1)
				{
					ToastView.showCenterToast(getContext(),R.drawable.ic_do_fail, "手机通话记录为空！");
					return;
				}
				mBackupService.updateHistoryBackupInfos(GsonTools.toJson(callLogInfoList), "正在备份通话记录...", true);
				break;
			}
		}
	}

	@Override
	public boolean onAjaxDataResponse(String url, String result, AjaxStatus status)
	{
		if (url.endsWith(ApiConfig.USER_BACKUP_HISOTRY))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				mBackupService.getHistoryBackupInfoCount(null, false);
			}
			return true;
		}
		else if (url.endsWith(ApiConfig.USER_GET_HISOTRY_COUNT))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			List<CallLogInfo> callLogInfoList = CallLogsFactory.getInstance().createListSort(CallLogsFactory.getInstance().getCallLogs(getContext()));
			String netCount = "";
			if (ReturnInfo.isSuccess(returnInfo))
			{
				netCount = returnInfo.getErrmsg();
			}
			tvHistoryCount.setText(String.format("通话记录：手机%d/网络%s", callLogInfoList.size(), netCount));
			return true;
		}
		else if (url.endsWith(ApiConfig.USER_GET_HISOTRY))
		{
			ReturnInfo returnInfo = GsonTools.getReturnInfo(result);
			if (ReturnInfo.isSuccess(returnInfo))
			{
				final List<CallLogInfo> callLogInfoList = GsonTools.getReturnListObjects(result, new TypeToken<List<CallLogInfo>>()
				{
				}.getType());
				if (callLogInfoList == null || callLogInfoList.size() < 1)
				{
					ToastView.showCenterToast(getContext(),R.drawable.ic_do_fail, "服务器通话记录为空！");
				}
				else
				{
					mDialogProgress = new DialogProgress(getContext(), new DialogProgress.LibitDialogProgressListener()
					{
						@Override
						public void onCancelClick()
						{
						}
					}, "恢复手机通话记录", "正在恢复第1个，共" + callLogInfoList.size() + "个", callLogInfoList.size(), false);
					mDialogProgress.show();
					//如果是覆盖则先清空手机通讯录
					if (downloadType == DOWNLOAD_COVER)
					{
						CallLogsFactory.getInstance().deleteAllCallLogs(getContext());
						downloadType = 0;
					}
					new Thread("restoreHistory")
					{
						@Override
						public void run()
						{
							super.run();
							int total = callLogInfoList.size();
							int index = 0;
							for (CallLogInfo callLogInfo : callLogInfoList)
							{
								index++;
								boolean b = CallLogsFactory.getInstance().addCallLogInfo(getContext(), callLogInfo);
								EventBus.getDefault().post(new CallLogRestoreEvent(index, total));
							}
							if (mDialogProgress != null)
							{
								mDialogProgress.dismiss();
								mDialogProgress = null;
							}
							//通知通话记录已更新
							EventBus.getDefault().post(new CallLogEvent(CallLogEvent.EVENT_CALLLOG_UPDATE));
							mHandler.sendEmptyMessage(RESTORE_FINISH);
						}
					}.start();
				}
			}
			else
			{
				if (returnInfo != null)
				{
					ToastView.showCenterToast(getContext(),R.drawable.ic_do_fail, "下载通话记录失败：" + returnInfo.getErrmsg());
				}
				else
				{
					ToastView.showCenterToast(getContext(), R.drawable.ic_do_fail,"下载通话记录失败：" + result);
				}
			}
			return true;
		}
		return false;
	}

	@Subscribe
	public void onEventMainThread(CallLogRestoreEvent callLogRestoreEvent)
	{
		if (callLogRestoreEvent != null)
		{
			if (mDialogProgress != null && mDialogProgress.isShowing())
			{
				mDialogProgress.setDialogMessage(String.format("正在恢复第%d个，共%d个", callLogRestoreEvent.getCurrent(), callLogRestoreEvent.getTotal()));
				mDialogProgress.updateWaiting(callLogRestoreEvent.getCurrent());
			}
		}
	}
}
