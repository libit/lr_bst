/*
 * Libit保留所有版权，如有疑问联系QQ：308062035
 * Copyright (c) 2016.
 */
package com.lrcall.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lrcall.appbst.R;
import com.lrcall.utils.DateTimeTools;

import java.io.File;
import java.util.List;

/**
 * 文件选择器
 * Created by libit on 15/8/31.
 */
public class FileListAdapter extends BaseUserAdapter<File>
{
	private final File rootFile;
	private final IFileListAdapter fileListAdapter;

	public FileListAdapter(Context context, File rootFile, List<File> fileList, IFileListAdapter fileListAdapter)
	{
		super(context, fileList);
		this.rootFile = rootFile;
		this.fileListAdapter = fileListAdapter;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		FileViewHolder contactViewHolder = null;
		if (convertView != null)
		{
			contactViewHolder = (FileViewHolder) convertView.getTag();
		}
		if (contactViewHolder == null)
		{
			contactViewHolder = new FileViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_file, null);
			contactViewHolder.ivHeader = (ImageView) convertView.findViewById(R.id.iv_icon);
			contactViewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_file_name);
			contactViewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_file_time);
			convertView.setTag(contactViewHolder);
		}
		else
		{
			contactViewHolder.clear();
		}
		final File file = list.get(position);
		if (file.isDirectory())
		{
			contactViewHolder.ivHeader.setImageResource(R.drawable.ic_folder);
		}
		else
		{
			contactViewHolder.ivHeader.setImageResource(R.drawable.ic_file);
		}
		//第一个应该是返回上一层
		if (position == 0)
		{
			if (rootFile.getAbsolutePath().equals(file.getAbsolutePath()))
			{
				convertView.setVisibility(View.GONE);
			}
			else
			{
				contactViewHolder.tvName.setText("...");
				contactViewHolder.tvTime.setText("上层文件夹");
				convertView.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if (fileListAdapter != null)
						{
							fileListAdapter.onParentSelected(file);
						}
					}
				});
			}
		}
		else
		{
			contactViewHolder.tvName.setText(file.getName());
			if (file.isDirectory())
			{
				contactViewHolder.tvTime.setText(DateTimeTools.getTime(file.lastModified()));
			}
			else
			{
				contactViewHolder.tvTime.setText(String.format("%s %.2fK", DateTimeTools.getTime(file.lastModified()), (double) file.length() / (double) 1024));
			}
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (fileListAdapter != null)
					{
						fileListAdapter.onFileSelected(file);
					}
				}
			});
		}
		return convertView;
	}

	public interface IFileListAdapter
	{
		//选择上一层
		void onParentSelected(File file);

		//选择文件
		void onFileSelected(File file);
	}

	public static class FileViewHolder
	{
		public ImageView ivHeader;
		public TextView tvName;
		public TextView tvTime;

		public void clear()
		{
			ivHeader.setImageBitmap(null);
			tvName.setText("");
			tvTime.setText("");
		}
	}
}
