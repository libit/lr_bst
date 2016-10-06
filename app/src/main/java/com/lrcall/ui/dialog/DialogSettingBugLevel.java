/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.enums.LogLevel;
import com.lrcall.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class DialogSettingBugLevel extends Dialog implements OnClickListener
{
	private ListView lvLevels;
	private CheckBox cbUpdate;
	private String level = "";
	private List<View> views;
	private final DialogCommon.LibitDialogListener listenser;
	private List<LogLevel> levels;

	public DialogSettingBugLevel(Context context, DialogCommon.LibitDialogListener listenser)
	{
		super(context, R.style.MyDialog);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		this.listenser = listenser;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_setting_bug_level);
		views = new ArrayList<>();
		levels = LogLevel.getAllLevels();
		initView();
	}

	private void initView()
	{
		lvLevels = (ListView) findViewById(R.id.list_bug_levels);
		cbUpdate = (CheckBox) findViewById(R.id.cb_bug_update);
		findViewById(R.id.dialog_btn_ok).setOnClickListener(this);
		findViewById(R.id.dialog_btn_cancel).setOnClickListener(this);
		level = PreferenceUtils.getInstance().getStringValue(PreferenceUtils.LOGCAT_LEVEL);
		lvLevels.setAdapter(new ListLevels(this.getContext()));
		cbUpdate.setChecked(PreferenceUtils.getInstance().getBooleanValue(PreferenceUtils.LOGCAT_AUTO_UPDATE));
		if (views != null && views.size() > 0)
		{
			int size = views.size();
			for (int i = 0; i < size; i++)
			{
				//                final TextView tvName = (TextView) views.get(i).findViewById(R.id.tv_name);
				//                ((RadioButton) views.get(i).findViewById(R.id.rb_level)).setChecked(tvName.getText().toString().equals(level));
			}
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.dialog_btn_ok:
			{
				PreferenceUtils.getInstance().setBooleanValue(PreferenceUtils.LOGCAT_AUTO_UPDATE, cbUpdate.isChecked());
				PreferenceUtils.getInstance().setStringValue(PreferenceUtils.LOGCAT_LEVEL, level);
				if (listenser != null)
				{
					listenser.onOkClick();
				}
				dismiss();
				break;
			}
			case R.id.dialog_btn_cancel:
			{
				if (listenser != null)
				{
					listenser.onCancelClick();
				}
				dismiss();
				break;
			}
		}
	}

	class ListLevels extends BaseAdapter
	{
		private final Context context;

		public ListLevels(Context context)
		{
			this.context = context;
		}

		@Override
		public int getCount()
		{
			if (levels != null)
			{
				return levels.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position)
		{
			if (levels != null)
				return levels.get(position);
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_bug_level, null);
			final TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
			final TextView tvDescription = (TextView) convertView.findViewById(R.id.tv_description);
			final RadioButton rbLevel = (RadioButton) convertView.findViewById(R.id.rb_level);
			tvName.setText(levels.get(position).getLevel() + "");
			tvDescription.setText(levels.get(position).getDesc());
			rbLevel.setChecked((levels.get(position).getLevel() + "").equals(level));
			View.OnClickListener listenser = new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					switch (v.getId())
					{
						case R.id.rb_level:
						case R.id.layout_item:
						{
							level = levels.get(position).getLevel() + "";
							int size = views.size();
							for (int i = 0; i < size; i++)
							{
								((RadioButton) views.get(i).findViewById(R.id.rb_level)).setChecked(false);
							}
							rbLevel.setChecked(true);
							break;
						}
					}
				}
			};
			convertView.findViewById(R.id.layout_item).setOnClickListener(listenser);
			convertView.findViewById(R.id.rb_level).setOnClickListener(listenser);
			if (!views.contains(convertView))
			{
				views.add(convertView);
			}
			return convertView;
		}
	}
}
