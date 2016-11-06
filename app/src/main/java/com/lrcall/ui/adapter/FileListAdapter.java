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
	private final IItemClick iItemClick;

	public FileListAdapter(Context context, File rootFile, List<File> fileList, IItemClick iItemClick)
	{
		super(context, fileList);
		this.rootFile = rootFile;
		this.iItemClick = iItemClick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		FileViewHolder viewHolder = null;
		if (convertView != null)
		{
			viewHolder = (FileViewHolder) convertView.getTag();
		}
		if (viewHolder == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_file, null);
			viewHolder = new FileViewHolder();
			viewHolder.viewInit(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder.clear();
		}
		final File file = list.get(position);
		if (file.isDirectory())
		{
			viewHolder.ivHeader.setImageResource(R.drawable.ic_folder);
		}
		else
		{
			viewHolder.ivHeader.setImageResource(R.drawable.ic_file);
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
				viewHolder.tvName.setText("...");
				viewHolder.tvTime.setText("上层文件夹");
				if (iItemClick != null)
				{
					convertView.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							iItemClick.onParentSelected(file);
						}
					});
				}
			}
		}
		else
		{
			viewHolder.tvName.setText(file.getName());
			if (file.isDirectory())
			{
				viewHolder.tvTime.setText(DateTimeTools.getTime(file.lastModified()));
			}
			else
			{
				viewHolder.tvTime.setText(String.format("%s %.2fK", DateTimeTools.getTime(file.lastModified()), (double) file.length() / (double) 1024));
			}
			if (iItemClick != null)
			{
				convertView.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						iItemClick.onFileSelected(file);
					}
				});
			}
		}
		return convertView;
	}

	public interface IItemClick
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

		public void viewInit(View convertView)
		{
			ivHeader = (ImageView) convertView.findViewById(R.id.iv_icon);
			tvName = (TextView) convertView.findViewById(R.id.tv_file_name);
			tvTime = (TextView) convertView.findViewById(R.id.tv_file_time);
		}

		public void clear()
		{
			ivHeader.setImageBitmap(null);
			tvName.setText("");
			tvTime.setText("");
		}
	}
}
